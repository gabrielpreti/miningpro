package com.miningpro.analysis;

import com.miningpro.reporting.Reportable;

import java.util.List;

/**
 * Created by gsantiago on 2/2/15.
 */
public interface ReportableResult extends Result {
    List<Reportable> getReportable();
}
