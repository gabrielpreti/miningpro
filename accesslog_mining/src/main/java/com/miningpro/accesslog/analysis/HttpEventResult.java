package com.miningpro.accesslog.analysis;

import com.miningpro.core.event.Measurable;

public class HttpEventResult {

    public enum AnalysisResultType {
        NOT_ANALYSEBLE, TRAINING, ALARMING, NORMAL
    }

    private AnalysisResultType resultType;
    private double historicalMean, historicalSd, currentMean, currentSd, score;
    private Measurable eventMeasure;
    private String slotKey;

    private HttpEventResult(AnalysisResultType result, double historicalMean, double historicalSd,
                            double currentMean, double currentSd, double score, Measurable eventMeasure, String slotKey) {
        this.resultType = result;
        this.historicalMean = historicalMean;
        this.historicalSd = historicalSd;
        this.currentMean = currentMean;
        this.currentSd = currentSd;
        this.score = score;
        this.eventMeasure = eventMeasure;
        this.slotKey = slotKey;
    }

    public static HttpEventResult notAnalysebleEvent(Measurable eventMeasure, String slotKey) {
        return new HttpEventResult(AnalysisResultType.NOT_ANALYSEBLE, 0, 0, 0, 0, 0, eventMeasure, slotKey);
    }

    public static HttpEventResult trainingEvent(double historicalMean, double historicalSd,
            double currentMean, double currentSd, double score, Measurable eventMeasure, String slotKey) {
        return new HttpEventResult(AnalysisResultType.TRAINING, historicalMean, historicalSd, currentMean,
                currentSd, score, eventMeasure, slotKey);
    }

    public static HttpEventResult alarmingEvent(double historicalMean, double historicalSd,
            double currentMean, double currentSd, double score, Measurable eventMeasure, String slotKey) {
        return new HttpEventResult(AnalysisResultType.ALARMING, historicalMean, historicalSd, currentMean,
                currentSd, score, eventMeasure, slotKey);
    }

    public static HttpEventResult normalEvent(double historicalMean, double historicalSd, double currentMean,
            double currentSd, double score, Measurable eventMeasure, String slotKey) {
        return new HttpEventResult(AnalysisResultType.NORMAL, historicalMean, historicalSd, currentMean,
                currentSd, score, eventMeasure, slotKey);
    }

    public double getHistoricalMean() {
        return historicalMean;
    }

    public void setHistoricalMean(double historicalMean) {
        this.historicalMean = historicalMean;
    }

    public double getHistoricalSd() {
        return historicalSd;
    }

    public void setHistoricalSd(double historicalSd) {
        this.historicalSd = historicalSd;
    }

    public double getCurrentMean() {
        return currentMean;
    }

    public void setCurrentMean(double currentMean) {
        this.currentMean = currentMean;
    }

    public double getCurrentSd() {
        return currentSd;
    }

    public void setCurrentSd(double currentSd) {
        this.currentSd = currentSd;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public AnalysisResultType getResultType() {
        return resultType;
    }

    public String toString() {
        return resultType.toString();
    }
}
