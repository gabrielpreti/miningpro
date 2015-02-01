package com.miningpro.accesslog.analysis;

import com.miningpro.accesslog.event.HttpEvent;
import com.miningpro.accesslog.event.UrlReturnCodeMeasurable;
import com.miningpro.analysis.timeseries.BatchSlottedAnalysis;
import com.miningpro.analysis.timeseries.IndividualAnalysisStatus;
import com.miningpro.core.event.Event;
import com.miningpro.core.event.Measurable;
import com.miningpro.repository.slot.MeasurableMetricsSlot;
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
public class HttpEventsAnalysis extends BatchSlottedAnalysis<HttpEvent, Integer, HttpEventSlot> {
    private static final Logger log = LoggerFactory.getLogger(HttpEventsAnalysis.class);

    private int trainingSize, historySize, delay;
    private double higherScore;

    private final double THRESHOLD_REL_SD_MEAN = 0.2d;

    public HttpEventsAnalysis(HttpEventsRepository repository, int trainingSize, int historySize, int delay) {
        super();
        super.repository = repository;
        this.trainingSize = trainingSize;
        this.historySize = historySize;
        this.delay = delay;
        this.higherScore = Double.MIN_VALUE;
    }

    @Override
    protected Set<Measurable> identifyMeasurables() {
        Set<Measurable> uniqueMeasurables = new HashSet<Measurable>();
        Collection<HttpEvent> events = repository.getEvents();
        for (HttpEvent event : events) {
            uniqueMeasurables.add(event.getMeasurable());
        }

        // TEMPORÁRIO
        uniqueMeasurables = new HashSet<Measurable>();
        uniqueMeasurables.add(new UrlReturnCodeMeasurable("pagseguro.uol.com.br/index.jhtml", "200"));

        return uniqueMeasurables;
    }

    @Override
    protected boolean isAnalyzable(Measurable m, HttpEventSlot currentSlot, List<HttpEventSlot> slotsHistory) {
        return slotsHistory.size() >= historySize + delay;
    }

    @Override
    protected void analyzeSlot(Measurable m, HttpEventSlot currentSlot, List<HttpEventSlot> slotsHistory) {
        double[] delayMetrics = getCurrentDelayMetrics(m, slotsHistory);
        double[] historyMetrics = getCurrentHistoryMetrics(m, slotsHistory);

        double historicalMean = Stats.mean(historyMetrics);
        double historicalSd = Stats.sdev(historyMetrics);

        // Se média histórica ou o desvio padrão histórico for igual a 0, não da pra analisar e portanto
        // consideramos normal
        if (Stats.isEqual(historicalMean, 0d, 0.001d) || Stats.isEqual(historicalSd, 0d, 0.001d)) {
            log.debug("\tMean or sd equal to 0. Ignoring event.");
            IndividualHttpResult result = new IndividualHttpResult(currentSlot.getKey(), m,
                    IndividualAnalysisStatus.NORMAL, historicalMean, historicalSd, 0, 0, 0);
            log.info(String.format("\tSlot %s for metric %s is %s", currentSlot.getKey(), m, result));
            return;
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
            IndividualHttpResult result = new IndividualHttpResult(currentSlot.getKey(), m,
                    IndividualAnalysisStatus.TRAINING, historicalMean, historicalSd, currentMean, currentSd,
                    scoreHistoricalSd);
            log.info(String.format("\tSlot %s for metric %s is %s", currentSlot.getKey(), m, result));
            higherScore = Stats.max(higherScore, scoreHistoricalSd);
            return;
        }

        IndividualHttpResult result;
        if (isHistoricalVariationSmallEnough && isDifferenceGreatherThanHistoricalScore) {
            currentSlot.markAlarming(m);
            result = new IndividualHttpResult(currentSlot.getKey(), m, IndividualAnalysisStatus.ALARMING,
                    historicalMean, historicalSd, currentMean, currentSd, scoreHistoricalSd);
            log.info(String.format("\tSlot %s for metric %s is %s", currentSlot.getKey(), m, result));
        } else {
            higherScore = Stats.max(higherScore, scoreHistoricalSd);
            result = new IndividualHttpResult(currentSlot.getKey(), m, IndividualAnalysisStatus.NORMAL, historicalMean,
                    historicalSd, currentMean, currentSd, scoreHistoricalSd);
            log.info(String.format("\tSlot %s for metric %s is %s", currentSlot.getKey(), m, result));
        }
    }

    private double[] getCurrentDelayMetrics(Measurable eventMeasurable, List<HttpEventSlot> slotsHistory) {
        double[] metrics = new double[delay];

        ListIterator<HttpEventSlot> backwardIterator = slotsHistory.listIterator(slotsHistory.size());
        for (int i = 0; i < delay; i++) {
            metrics[i] = backwardIterator.previous().getMetric(eventMeasurable);
        }

        return metrics;
    }

    private double[] getCurrentHistoryMetrics(Measurable eventMeasurable, List<HttpEventSlot> slotsHistory) {
        double[] metrics = new double[historySize];

        ListIterator<HttpEventSlot> backwardIterator = slotsHistory.listIterator(slotsHistory.size());
        for (int i = 0; i < delay; i++) {
            backwardIterator.previous();
        }

        int addedEvents = 0;
        while (addedEvents < historySize) {
            HttpEventSlot previousSlot = backwardIterator.previous();
            if (!previousSlot.isAlarming(eventMeasurable)) {
                metrics[addedEvents] = previousSlot.getMetric(eventMeasurable);
                addedEvents++;
            }
        }
        return metrics;
    }
}
