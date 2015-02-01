package com.miningpro.analysis.timeseries;

import com.miningpro.core.event.Measurable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gsantiago on 2/1/15.
 */
public class IndividualAnalysisResult {
    protected IndividualAnalysisStatus status;

    /**
     * O que está sendo analisado
     */
    protected Measurable measurableThing;

    /**
     * Identificação da unidade sendo analisda. Ex: id do slot
     */
    protected String resultId;

    /**
     * Métricas utilizadas para análise.
     */
    protected Map<String, Object> details;

    public IndividualAnalysisResult() {

    }

    public IndividualAnalysisResult(String resultId, Measurable measurableThing, IndividualAnalysisStatus status) {
        this.resultId = resultId;
        this.measurableThing = measurableThing;
        this.status = status;
        this.details = new HashMap<String, Object>();
    }

    @Override
    public String toString() {
        StringBuilder detailsStr = new StringBuilder();
        for (String key : details.keySet()) {
            detailsStr.append(key).append("=\"").append(details.get(key)).append("\",");
        }
        if (detailsStr.toString().endsWith(",")) {
            detailsStr.deleteCharAt(detailsStr.lastIndexOf(","));
        }

        return String.format("measurableThing=[%s], resultId=[%s], status=[%s], details=[%s]",
                String.valueOf(measurableThing), resultId, status, detailsStr);
    }
}
