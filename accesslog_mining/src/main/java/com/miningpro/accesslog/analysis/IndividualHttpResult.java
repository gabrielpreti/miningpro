package com.miningpro.accesslog.analysis;

import com.miningpro.analysis.timeseries.IndividualAnalysisResult;
import com.miningpro.analysis.timeseries.IndividualAnalysisStatus;
import com.miningpro.core.event.Measurable;

/**
 * Created by gsantiago on 2/1/15.
 */
public class IndividualHttpResult extends IndividualAnalysisResult {

    private final String HISTORICAL_MEAN_KEY = "historicalMean";
    private final String HISTORICAL_SD_KEY = "historicalSd";
    private final String CURRENT_MEAN_KEY = "currentMean";
    private final String CURRENT_SD_KEY = "currentSd";
    private final String SCORE_KEY = "score";

    public IndividualHttpResult(String resultId, Measurable measurableThing, IndividualAnalysisStatus status) {
        super(resultId, measurableThing, status);
    }

    public IndividualHttpResult(String resultId, Measurable measurableThing, IndividualAnalysisStatus status,
            double historicalMean, double historicalSd, double currentMean, double currentSd, double score) {
        super(resultId, measurableThing, status);
        details.put(HISTORICAL_MEAN_KEY, historicalMean);
        details.put(HISTORICAL_SD_KEY, historicalSd);
        details.put(CURRENT_MEAN_KEY, currentMean);
        details.put(CURRENT_SD_KEY, currentSd);
        details.put(SCORE_KEY, score);
    }
}
