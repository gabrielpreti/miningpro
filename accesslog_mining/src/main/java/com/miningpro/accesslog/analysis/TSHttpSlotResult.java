package com.miningpro.accesslog.analysis;

import com.miningpro.analysis.timeseries.TSUnitResult;
import com.miningpro.analysis.timeseries.TSUnitStatus;
import com.miningpro.core.event.Measurable;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by gsantiago on 2/1/15.
 */
public class TSHttpSlotResult extends TSUnitResult {

    private final String HISTORICAL_MEAN_KEY = "historicalMean";
    private final String HISTORICAL_SD_KEY = "historicalSd";
    private final String CURRENT_MEAN_KEY = "currentMean";
    private final String CURRENT_SD_KEY = "currentSd";
    private final String SCORE_KEY = "score";

    public TSHttpSlotResult(String resultId, Measurable measurableThing, TSUnitStatus status) {
        super(resultId, measurableThing, status);
    }

    public TSHttpSlotResult(String resultId, Measurable measurableThing, TSUnitStatus status, double historicalMean,
            double historicalSd, double currentMean, double currentSd, double score) {
        super(resultId, measurableThing, status);
        details.put(HISTORICAL_MEAN_KEY, historicalMean);
        details.put(HISTORICAL_SD_KEY, historicalSd);
        details.put(CURRENT_MEAN_KEY, currentMean);
        details.put(CURRENT_SD_KEY, currentSd);
        details.put(SCORE_KEY, score);
    }
}
