package com.miningpro.analysis.timeseries;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.miningpro.core.event.Measurable;
import com.miningpro.reporting.Reportable;

/**
 * Created by gsantiago on 2/1/15.
 */
public class TSUnitResult implements Reportable {
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

    @Override
    public Map<String, String> report() {
        Map<String, String> report = new LinkedHashMap<String, String>();// LinkedHashMap para manter ordem de inserção.

        report.put("measurableThing", measurableThing.toString());
        report.put("resultId", resultId);
        report.put("status", status.toString());
        for (String key : details.keySet()) {
            report.put(key, String.valueOf(details.get(key)));
        }

        return report;
    }
}
