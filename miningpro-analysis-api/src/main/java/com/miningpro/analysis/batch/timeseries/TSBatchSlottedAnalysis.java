package com.miningpro.analysis.batch.timeseries;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.miningpro.analysis.timeseries.TSAnalysisResult;
import com.miningpro.analysis.timeseries.TSAnalysisUnitResult;
import com.miningpro.analysis.timeseries.TSUnitResult;
import com.miningpro.analysis.timeseries.TSUnitStatus;
import com.miningpro.core.event.AnalysisUnit;
import com.miningpro.repository.slot.AnalysisUnitBasedMetricsSlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miningpro.analysis.batch.BatchAnalysis;
import com.miningpro.core.event.Event;
import com.miningpro.repository.SlottedRepository;

/**
 * Created by gsantiago on 2/1/15.
 */
public abstract class TSBatchSlottedAnalysis<E extends Event, N extends Number, S extends AnalysisUnitBasedMetricsSlot<N, E>>
        implements BatchAnalysis {
    private static final Logger log = LoggerFactory.getLogger(TSBatchSlottedAnalysis.class);
    protected SlottedRepository<E, S> repository;

    @Override
    public TSAnalysisResult runBatchAnalysis() {
        TSAnalysisResult result = new TSAnalysisResult();

        log.info("Identifying analysis unit ...");
        Set<AnalysisUnit> analysisUnits = identifyAnalysisUnits();
        log.info(String.format("Identified %d analysis units", analysisUnits.size()));

        log.info("Analyzing analysis units ...");
        for (AnalysisUnit m : analysisUnits) {
            result.addResult(executeAnalysis(m));
        }
        log.info("Done.");
        return result;
    }

    protected TSAnalysisUnitResult executeAnalysis(AnalysisUnit m) {
        log.debug(String.format("Analysing measurable %s", m));
        List<S> slotsHistory = new ArrayList<S>();
        TSAnalysisUnitResult measurableResult = new TSAnalysisUnitResult(m);

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

    protected abstract Set<AnalysisUnit> identifyAnalysisUnits();

    protected abstract boolean isAnalyzable(AnalysisUnit m, S currentSlot, List<S> slotsHistory);

    protected abstract TSUnitResult analyzeSlot(AnalysisUnit m, S currentSlot, List<S> slotsHistory);
}
