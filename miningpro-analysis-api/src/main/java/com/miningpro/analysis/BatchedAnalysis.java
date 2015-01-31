package com.miningpro.analysis;

import com.miningpro.core.analysis.Result;

/**
 * Created by gsantiago on 1/31/15.
 */
public interface BatchedAnalysis extends Analysis {
    Result runBatchAnalysis();
}
