package com.miningpro.accesslog.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miningpro.accesslog.event.HttpEvent;
import com.miningpro.accesslog.event.UrlReturnCodeMeasurable;
import com.miningpro.analysis.BatchedAnalysis;
import com.miningpro.core.event.Measurable;
import com.mininpro.accesslog.repo.HttpEventSlot;
import com.mininpro.accesslog.repo.HttpEventsRepository;
import com.ml.miningpro.tools.Stats;

/**
 * Created by gsantiago on 1/31/15.
 */
public class HttpEventsModel implements BatchedAnalysis {
    private static final Logger log = LoggerFactory.getLogger(HttpEventsModel.class);

    private int trainingSize, historySize, delay;
    private HttpEventsRepository repository;
    private double higherScore;

    private final double THRESOLD_REL_SD_MEAN = 0.2d;

    public HttpEventsModel(HttpEventsRepository repository, int trainingSize, int historySize, int delay) {
        super();

        this.repository = repository;
        this.trainingSize = trainingSize;
        this.historySize = historySize;
        this.delay = delay;
        this.higherScore = Double.MIN_VALUE;
    }

    @Override
    public HttpEventsAnalysisResult runBatchAnalysis() {
        HttpEventsAnalysisResult output = new HttpEventsAnalysisResult();

        log.info("Identifing unique events ...");
        Set<Measurable> uniqueMeasurables = new HashSet<Measurable>();
        Collection<HttpEvent> events = repository.getEvents();
        for (HttpEvent event : events) {
            uniqueMeasurables.add(event.getMeasurable());
        }
        log.info(String.format("Identified %d unique events", uniqueMeasurables.size()));

        // TEMPORÁRIO
        uniqueMeasurables = new HashSet<Measurable>();
        uniqueMeasurables.add(new UrlReturnCodeMeasurable("pagseguro.uol.com.br/index.jhtml", "200"));

        for (Measurable uniqueEvent : uniqueMeasurables) {
            output.addResults(executeUniqueEventKeyAnalysis(uniqueEvent));
        }

        return output;
    }

    private List<HttpEventResult> executeUniqueEventKeyAnalysis(Measurable eventMeasurable) {
        log.info(String.format("Analysing event %s", eventMeasurable));
        EventAnalysisStack stack = new EventAnalysisStack(historySize, delay);
        List<HttpEventResult> results = new ArrayList<HttpEventResult>();

        for (HttpEventSlot currentSlot : repository.getSlots()) {
            stack.addSlot(currentSlot);
            log.info(String.format("\tAnalysing slot starting at %s", currentSlot.getKey()));

            if (stack.countSlots() < (historySize + delay)) {
                HttpEventResult result = HttpEventResult.notAnalysebleEvent(eventMeasurable, currentSlot.getKey());
                results.add(result);
                log.info(String.format("\tSlot %s for metric %s is %s", currentSlot.getKey(), eventMeasurable, result));
                continue;
            }

            double[] delayMetrics = stack.getCurrentDelayMetrics(eventMeasurable);
            double[] historyMetrics = stack.getCurrentHistorySlots(eventMeasurable);

            double historicalMean = Stats.mean(historyMetrics);
            double historicalSd = Stats.sdev(historyMetrics);

            // Se média histórica ou o desvio padrão histórico for igual a 0, não da pra analisar e portanto
            // consideramos normal
            if (Stats.isEqual(historicalMean, 0d, 0.001d) || Stats.isEqual(historicalSd, 0d, 0.001d)) {
                log.debug("\tMean or sd equal to 0. Ignoring event.");
                HttpEventResult result = HttpEventResult.normalEvent(historicalMean, historicalSd, 0, 0, 0,
                        eventMeasurable, currentSlot.getKey());
                results.add(result);
                log.info(String.format("\tSlot %s for metric %s is %s", currentSlot.getKey(), eventMeasurable, result));
                continue;
            }

            double currentMean = Stats.mean(delayMetrics);
            double currentSd = Stats.sdev(delayMetrics);
            double difference = Math.abs(currentMean - historicalMean);

            // O score é uma medida da relação entre o valor absoluto da diferença entre as médias com o desvio padrão:
            // em situações normais, o valor absoluto da diferença entre as
            // médias deveria estar dentro da marge de desvio padrão. Quanto maior esse valor for em relação ao desvio
            // padrão, maior o indício de anormalidade.
            // double scoreCurrentSd = difference / currentSd;
            double scoreHistoricalSd = difference / historicalSd;
            boolean isHistoricalVariationSmallEnough = historicalSd / historicalMean < THRESOLD_REL_SD_MEAN; // Se os
                                                                                                             // dados
                                                                                                             // históricos
                                                                                                             // tiverem
                                                                                                             // uma
                                                                                                             // variação
                                                                                                             // muito
                                                                                                             // grande,
                                                                                                             // não tem
                                                                                                             // como
                                                                                                             // analisar.
            boolean isDifferenceGreatherThanHistoricalScore = scoreHistoricalSd > higherScore;

            if (stack.countSlots() < (trainingSize + historySize + delay)) {
                HttpEventResult result = HttpEventResult.trainingEvent(historicalMean, historicalSd, currentMean,
                        currentSd, scoreHistoricalSd, eventMeasurable, currentSlot.getKey());
                results.add(result);
                log.info(String.format("\tSlot %s for metric %s is %s", currentSlot.getKey(), eventMeasurable, result));
                higherScore = Stats.max(higherScore, scoreHistoricalSd);
                continue;
            }

            HttpEventResult result;
            if (isHistoricalVariationSmallEnough && isDifferenceGreatherThanHistoricalScore) {
                currentSlot.markAlarming(eventMeasurable);
                result = HttpEventResult.alarmingEvent(historicalMean, historicalMean, currentMean, currentSd,
                        scoreHistoricalSd, eventMeasurable, currentSlot.getKey());
                log.info(String.format("\tSlot %s for metric %s is %s", currentSlot.getKey(), eventMeasurable, result));
            } else {
                higherScore = Stats.max(higherScore, scoreHistoricalSd);
                result = HttpEventResult.normalEvent(historicalMean, historicalMean, currentMean, currentSd,
                        scoreHistoricalSd, eventMeasurable, currentSlot.getKey());
                log.info(String.format("\tSlot %s for metric %s is %s", currentSlot.getKey(), eventMeasurable, result));
            }
            results.add(result);
        }

        return results;
    }

    public class EventAnalysisStack {
        private List<HttpEventSlot> analysedSlots;
        private int historySize, delaySize;

        public EventAnalysisStack(int historySize, int delaySize) {
            this.historySize = historySize;
            this.delaySize = delaySize;
            this.analysedSlots = new ArrayList<HttpEventSlot>();
        }

        public int countSlots() {
            return analysedSlots.size();
        }

        public void addSlot(HttpEventSlot slot) {
            analysedSlots.add(slot);
        }

        public double[] getCurrentDelayMetrics(Measurable eventMeasurable) {
            double[] metrics = new double[delaySize];

            ListIterator<HttpEventSlot> backwardIterator = analysedSlots.listIterator(analysedSlots.size());
            for (int i = 0; i < delaySize; i++) {
                metrics[i] = backwardIterator.previous().getMetric(eventMeasurable);
            }

            return metrics;
        }

        public double[] getCurrentHistorySlots(Measurable eventMeasurable) {
            double[] metrics = new double[historySize];

            ListIterator<HttpEventSlot> backwardIterator = analysedSlots.listIterator(analysedSlots.size());
            for (int i = 0; i < delaySize; i++) {
                backwardIterator.previous();
            }

            int addedEvents = 0;
            while (addedEvents < historySize) {
                HttpEventSlot previousSlot = backwardIterator.previous();
                if (!previousSlot.isAlarming(eventMeasurable)) {
                    Number metricValue = metrics[addedEvents] = previousSlot.getMetric(eventMeasurable);
                    addedEvents++;
                }
            }
            return metrics;
        }
    }
}
