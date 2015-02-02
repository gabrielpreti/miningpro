package com.miningpro.analysis.batch.timeseries;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.miningpro.analysis.timeseries.TSAnalysisResult;
import com.miningpro.analysis.timeseries.TSMeasurableResult;
import com.miningpro.analysis.timeseries.TSUnitResult;
import com.miningpro.analysis.timeseries.TSUnitStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miningpro.analysis.batch.BatchAnalysis;
import com.miningpro.core.event.Event;
import com.miningpro.core.event.Measurable;
import com.miningpro.repository.SlottedRepository;
import com.miningpro.repository.slot.MeasurableMetricsSlot;

/**
 * Created by gsantiago on 2/1/15.
 */
public abstract class TSBatchSlottedAnalysis<E extends Event, N extends Number, S extends MeasurableMetricsSlot<N, E>>
        implements BatchAnalysis {
    private static final Logger log = LoggerFactory.getLogger(TSBatchSlottedAnalysis.class);
    protected SlottedRepository<E, S> repository;

    @Override
    public TSAnalysisResult runBatchAnalysis() {
        TSAnalysisResult result = new TSAnalysisResult();

        log.info("Identifying measurables ...");
        Set<Measurable> measurables = identifyMeasurables();
        log.info(String.format("Identified %d measurables", measurables.size()));

        log.info("Analyzing measurables ...");
        for (Measurable m : measurables) {
            result.addMeasurableResult(executeMeasurableAnalysis(m));
        }
        log.info("Done.");
        return result;
    }

    protected TSMeasurableResult executeMeasurableAnalysis(Measurable m) {
        log.debug(String.format("Analysing measurable %s", m));
        List<S> slotsHistory = new ArrayList<S>();
        TSMeasurableResult measurableResult = new TSMeasurableResult(m);

        for (S currentSlot : repository.getSlots()) {
            slotsHistory.add(currentSlot);
            log.debug(String.format("\tAnalysing slot %s", currentSlot.getKey()));

            if (!isAnalyzable(m, currentSlot, slotsHistory)) {
                TSUnitResult result = new TSUnitResult(currentSlot.getKey(), m,
                        TSUnitStatus.NOT_ANALYZABLE);
                measurableResult.addUnitResult(result);
                log.info(String.format("\tSlot %s for measurable %s is %s", currentSlot.getKey(), m, result));
                continue;
            }

            measurableResult.addUnitResult(analyzeSlot(m, currentSlot, slotsHistory));
        }

        return measurableResult;
    }

    protected abstract Set<Measurable> identifyMeasurables();

    protected abstract boolean isAnalyzable(Measurable m, S currentSlot, List<S> slotsHistory);

    protected abstract TSUnitResult analyzeSlot(Measurable m, S currentSlot, List<S> slotsHistory);
}
