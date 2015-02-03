package com.miningpro.analysis.timeseries;

import com.miningpro.analysis.ReportableResult;
import com.miningpro.core.event.Measurable;
import com.miningpro.reporting.Reportable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gsantiago on 2/1/15.
 */
public class TSAnalysisResult implements ReportableResult {
    Map<Measurable, TSMeasurableResult> results;

    public TSAnalysisResult() {
        this.results = new HashMap<Measurable, TSMeasurableResult>();
    }

    public void addMeasurableResult(TSMeasurableResult r) {
        results.put(r.getMeasurable(), r);
    }

    @Override
    public List<Reportable> getReportable() {
        List<Reportable> reportable = new ArrayList<Reportable>();
        for (TSMeasurableResult result : results.values()) {
            reportable.addAll(result.getUnitResults());
        }
        return reportable;
    }
}
