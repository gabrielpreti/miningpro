package com.miningpro.reporting;

import java.util.Map;

/**
 * Created by gsantiago on 2/2/15.
 */
public interface ReportStrategy {
    void init();

    void apply(Map<String, String> data);

    void finish();
}
