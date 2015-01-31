package com.miningpro.accesslog.analysis;

import com.miningpro.core.analysis.Result;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by gsantiago on 1/31/15.
 */
public class HttpEventsAnalysisResult implements Result {
    private Collection<HttpEventResult> results;

    public HttpEventsAnalysisResult() {
        results = new ArrayList<HttpEventResult>();
    }

    public void addResult(HttpEventResult result) {
        results.add(result);
    }

    public void addResults(Collection<HttpEventResult> results) {
        this.results.addAll(results);
    }

    public Collection<HttpEventResult> getResults() {
        return this.results;
    }
}
