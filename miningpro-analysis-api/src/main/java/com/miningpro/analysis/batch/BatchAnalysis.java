package com.miningpro.analysis.batch;

import com.miningpro.analysis.Analysis;
import com.miningpro.core.analysis.Result;

/**
 * Created by gsantiago on 2/1/15.
 */
public interface BatchAnalysis extends Analysis {
    Result runBatchAnalysis();
}
