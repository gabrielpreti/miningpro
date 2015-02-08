package com.miningpro.analysis.timeseries;

import com.miningpro.analysis.ReportableResult;
import com.miningpro.core.event.AnalysisUnit;
import com.miningpro.reporting.Reportable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gsantiago on 2/1/15.
 */
public class TSAnalysisResult implements ReportableResult {
    Map<AnalysisUnit, TSAnalysisUnitResult> results;

    public TSAnalysisResult() {
        this.results = new HashMap<AnalysisUnit, TSAnalysisUnitResult>();
    }

    public void addResult(TSAnalysisUnitResult r) {
        results.put(r.getAnalysisUnit(), r);
    }

    @Override
    public List<Reportable> getReportable() {
        List<Reportable> reportable = new ArrayList<Reportable>();
        for (TSAnalysisUnitResult result : results.values()) {
            reportable.addAll(result.getUnitResults());
        }
        return reportable;
    }
}
