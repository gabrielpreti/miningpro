package com.miningpro.analysis.timeseries;

import com.miningpro.analysis.timeseries.TSUnitResult;
import com.miningpro.core.event.Measurable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gsantiago on 2/1/15.
 */
public class TSMeasurableResult {
    private Measurable measurable;
    private List<TSUnitResult> unitResults;

    public TSMeasurableResult(Measurable m) {
        this.measurable = m;
        this.unitResults = new ArrayList<TSUnitResult>();
    }

    public Measurable getMeasurable() {
        return measurable;
    }

    public void addUnitResult(TSUnitResult r) {
        unitResults.add(r);
    }

    public List<TSUnitResult> getUnitResults() {
        return unitResults;
    }
}
