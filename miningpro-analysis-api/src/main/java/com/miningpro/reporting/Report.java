package com.miningpro.reporting;

import com.miningpro.analysis.ReportableResult;

import java.util.List;

/**
 * Created by gsantiago on 2/2/15.
 */
public class Report {
    private ReportStrategy strategy;

    public Report(ReportStrategy s) {
        this.strategy = s;
    }

    public void generateReport(Reportable reportable) {
        strategy.init();
        strategy.apply(reportable.report());
        strategy.finish();
    }

    public void generateReport(List<Reportable> reportable) {
        strategy.init();
        for (Reportable r : reportable) {
            strategy.apply(r.report());
        }
        strategy.finish();
    }

    public void generateReport(ReportableResult result) {
        generateReport(result.getReportable());
    }
}
