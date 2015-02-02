package com.miningpro.analysis.timeseries;

import java.util.HashMap;
import java.util.Map;

import com.miningpro.core.event.Measurable;

/**
 * Created by gsantiago on 2/1/15.
 */
public class TSUnitResult {
    protected TSUnitStatus status;

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

    public TSUnitResult() {

    }

    public TSUnitResult(String resultId, Measurable measurableThing, TSUnitStatus status) {
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
