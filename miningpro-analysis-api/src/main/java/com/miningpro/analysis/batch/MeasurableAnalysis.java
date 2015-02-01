package com.miningpro.analysis.batch;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miningpro.core.event.Measurable;

/**
 * Created by gsantiago on 1/31/15.
 */
public abstract class MeasurableAnalysis implements BatchAnalysis {
    private static final Logger log = LoggerFactory.getLogger(MeasurableAnalysis.class);

    public void runBatchAnalysis() {

        log.info("Identifying measurables ...");
        Set<Measurable> measurables = identifyMeasurables();
        log.info(String.format("Identified %d measurables", measurables.size()));

        log.info("Analyzing measurables ...");
        for (Measurable m : measurables) {
            executeMeasurableAnalysis(m);
        }
        log.info("Done.");

    }

    protected abstract Set<Measurable> identifyMeasurables();

    protected abstract void executeMeasurableAnalysis(Measurable m);
}
