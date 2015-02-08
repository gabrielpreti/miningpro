package com.miningpro.accesslog.analysis;

import com.miningpro.accesslog.event.HttpEvent;
import com.miningpro.accesslog.event.UrlReturnCodeAnalysisUnit;
import com.miningpro.analysis.batch.timeseries.TSBatchSlottedAnalysis;
import com.miningpro.analysis.timeseries.TSUnitStatus;
import com.miningpro.core.event.AnalysisUnit;
import com.mininpro.accesslog.repo.HttpEventSlot;
import com.mininpro.accesslog.repo.HttpEventsRepository;
import com.ml.miningpro.tools.Stats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * Created by gsantiago on 2/1/15.
 */
public class TSHttpEventsAnalysis extends TSBatchSlottedAnalysis<HttpEvent, Integer, HttpEventSlot> {
    private static final Logger log = LoggerFactory.getLogger(TSHttpEventsAnalysis.class);

    private int trainingSize, historySize, delay;
    private double higherScore;

    private final double THRESHOLD_REL_SD_MEAN = 0.2d;

    public TSHttpEventsAnalysis(HttpEventsRepository repository, int trainingSize, int historySize, int delay) {
        super();
        super.repository = repository;
        this.trainingSize = trainingSize;
        this.historySize = historySize;
        this.delay = delay;
        this.higherScore = Double.MIN_VALUE;
    }

    @Override
    protected Set<AnalysisUnit> identifyAnalysisUnits() {
        Set<AnalysisUnit> uniqueAnalysisUnits = new HashSet<AnalysisUnit>();
        Collection<HttpEvent> events = repository.getEvents();
        for (HttpEvent event : events) {
            uniqueAnalysisUnits.add(event.getAnalysisUnit());
        }

        // TEMPORÁRIO
        uniqueAnalysisUnits = new HashSet<AnalysisUnit>();
        uniqueAnalysisUnits.add(new UrlReturnCodeAnalysisUnit("pagseguro.uol.com.br/index.jhtml", "200"));

        return uniqueAnalysisUnits;
    }

    @Override
    protected boolean isAnalyzable(AnalysisUnit m, HttpEventSlot currentSlot, List<HttpEventSlot> slotsHistory) {
        return slotsHistory.size() >= historySize + delay;
    }

    @Override
    protected TSHttpSlotResult analyzeSlot(AnalysisUnit m, HttpEventSlot currentSlot,
            List<HttpEventSlot> slotsHistory) {
        double[] delayMetrics = getCurrentDelayMetrics(m, slotsHistory);
        double[] historyMetrics = getCurrentHistoryMetrics(m, slotsHistory);

        double historicalMean = Stats.mean(historyMetrics);
        double historicalSd = Stats.sdev(historyMetrics);

        // Se média histórica ou o desvio padrão histórico for igual a 0, não da pra analisar e portanto
        // consideramos normal
        if (Stats.isEqual(historicalMean, 0d, 0.001d) || Stats.isEqual(historicalSd, 0d, 0.001d)) {
            log.debug("\tMean or sd equal to 0. Ignoring event.");
            TSHttpSlotResult result = new TSHttpSlotResult(currentSlot.getKey(), m,
                    TSUnitStatus.NORMAL, historicalMean, historicalSd, 0, 0, 0);
            log.debug(String.format("\tSlot %s for metric %s is %s", currentSlot.getKey(), m, result));
            return result;
        }

        double currentMean = Stats.mean(delayMetrics);
        double currentSd = Stats.sdev(delayMetrics);
        double difference = Math.abs(currentMean - historicalMean);

        /*
         * O score é uma medida da relação entre o valor absoluto da diferença entre as médias com o desvio padrão: em
         * situações normais, o valor absoluto da diferença entre as médias deveria estar dentro da marge de desvio
         * padrão. Quanto maior esse valor for em relação ao desvio padrão, maior o indício de anormalidade. double
         * scoreCurrentSd = difference / currentSd;
         */
        double scoreHistoricalSd = difference / historicalSd;

        /*
         * Se os dados históricos tiverem uma variação muito grande, não tem como analisar.
         */
        boolean isHistoricalVariationSmallEnough = historicalSd / historicalMean < THRESHOLD_REL_SD_MEAN;

        boolean isDifferenceGreatherThanHistoricalScore = scoreHistoricalSd > higherScore;

        if (slotsHistory.size() < (trainingSize + historySize + delay)) {
            TSHttpSlotResult result = new TSHttpSlotResult(currentSlot.getKey(), m,
                    TSUnitStatus.TRAINING, historicalMean, historicalSd, currentMean, currentSd,
                    scoreHistoricalSd);
            log.debug(String.format("\tSlot %s for metric %s is %s", currentSlot.getKey(), m, result));
            higherScore = Stats.max(higherScore, scoreHistoricalSd);
            return result;
        }

        TSHttpSlotResult result;
        if (isHistoricalVariationSmallEnough && isDifferenceGreatherThanHistoricalScore) {
            currentSlot.markAlarming(m);
            result = new TSHttpSlotResult(currentSlot.getKey(), m, TSUnitStatus.ALARMING,
                    historicalMean, historicalSd, currentMean, currentSd, scoreHistoricalSd);
            log.debug(String.format("\tSlot %s for metric %s is %s", currentSlot.getKey(), m, result));
        } else {
            higherScore = Stats.max(higherScore, scoreHistoricalSd);
            result = new TSHttpSlotResult(currentSlot.getKey(), m, TSUnitStatus.NORMAL, historicalMean,
                    historicalSd, currentMean, currentSd, scoreHistoricalSd);
            log.debug(String.format("\tSlot %s for metric %s is %s", currentSlot.getKey(), m, result));
        }
        return result;
    }

    private double[] getCurrentDelayMetrics(AnalysisUnit eventAnalysisUnit, List<HttpEventSlot> slotsHistory) {
        double[] metrics = new double[delay];

        ListIterator<HttpEventSlot> backwardIterator = slotsHistory.listIterator(slotsHistory.size());
        for (int i = 0; i < delay; i++) {
            metrics[i] = backwardIterator.previous().getMetric(eventAnalysisUnit);
        }

        return metrics;
    }

    private double[] getCurrentHistoryMetrics(AnalysisUnit eventAnalysisUnit, List<HttpEventSlot> slotsHistory) {
        double[] metrics = new double[historySize];

        ListIterator<HttpEventSlot> backwardIterator = slotsHistory.listIterator(slotsHistory.size());
        for (int i = 0; i < delay; i++) {
            backwardIterator.previous();
        }

        int addedEvents = 0;
        while (addedEvents < historySize) {
            HttpEventSlot previousSlot = backwardIterator.previous();
            if (!previousSlot.isAlarming(eventAnalysisUnit)) {
                metrics[addedEvents] = previousSlot.getMetric(eventAnalysisUnit);
                addedEvents++;
            }
        }
        return metrics;
    }
}
