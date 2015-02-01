package com.miningpro.analysis.timeseries;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miningpro.analysis.batch.MeasurableAnalysis;
import com.miningpro.core.event.Event;
import com.miningpro.core.event.Measurable;
import com.miningpro.repository.SlottedRepository;
import com.miningpro.repository.slot.MeasurableMetricsSlot;

/**
 * Created by gsantiago on 2/1/15.
 */
public abstract class BatchSlottedAnalysis<E extends Event, N extends Number, S extends MeasurableMetricsSlot<N, E>>
        extends MeasurableAnalysis {
    private static final Logger log = LoggerFactory.getLogger(BatchSlottedAnalysis.class);
    protected SlottedRepository<E, S> repository;

    protected void executeMeasurableAnalysis(Measurable m) {
        log.debug(String.format("Analysing measurable %s", m));
        List<S> slotsHistory = new ArrayList<S>();

        for (S currentSlot : repository.getSlots()) {
            slotsHistory.add(currentSlot);
            log.debug(String.format("\tAnalysing slot %s", currentSlot.getKey()));

            if (!isAnalyzable(m, currentSlot, slotsHistory)) {
                IndividualAnalysisResult result = new IndividualAnalysisResult(currentSlot.getKey(), m,
                        IndividualAnalysisStatus.NOT_ANALYZABLE);
                log.info(String.format("\tSlot %s for measurable %s is %s", currentSlot.getKey(), m, result));
                continue;
            }

            analyzeSlot(m, currentSlot, slotsHistory);
        }
    }

    protected abstract boolean isAnalyzable(Measurable m, S currentSlot, List<S> slotsHistory);

    protected abstract void analyzeSlot(Measurable m, S currentSlot, List<S> slotsHistory);
}
