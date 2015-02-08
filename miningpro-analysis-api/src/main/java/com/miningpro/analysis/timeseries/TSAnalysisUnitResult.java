package com.miningpro.analysis.timeseries;

import com.miningpro.core.event.AnalysisUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gsantiago on 2/1/15.
 */
public class TSAnalysisUnitResult {
    private AnalysisUnit analysisUnit;
    private List<TSUnitResult> unitResults;

    public TSAnalysisUnitResult(AnalysisUnit m) {
        this.analysisUnit = m;
        this.unitResults = new ArrayList<TSUnitResult>();
    }

    public AnalysisUnit getAnalysisUnit() {
        return analysisUnit;
    }

    public void addUnitResult(TSUnitResult r) {
        unitResults.add(r);
    }

    public List<TSUnitResult> getUnitResults() {
        return unitResults;
    }
}
