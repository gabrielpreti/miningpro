package com.miningpro.analysis.timeseries;

import com.miningpro.core.analysis.Result;
import com.miningpro.core.event.Measurable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gsantiago on 2/1/15.
 */
public class TSAnalysisResult implements Result {
    Map<Measurable, TSMeasurableResult> results;

    public TSAnalysisResult() {
        this.results = new HashMap<Measurable, TSMeasurableResult>();
    }

    public void addMeasurableResult(TSMeasurableResult r) {
        results.put(r.getMeasurable(), r);
    }
}
