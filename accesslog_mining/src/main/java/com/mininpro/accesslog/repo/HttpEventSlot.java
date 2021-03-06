package com.mininpro.accesslog.repo;

import com.miningpro.accesslog.event.HttpEvent;
import com.miningpro.core.event.AnalysisUnit;
import com.miningpro.repository.slot.AbstractAlarmableByAnalysisUnitSlot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Implementação para slots de evento http. Os eventos estão ordenados por ordem de inserção (a implementação é um
 * ArrayList).
 * 
 * @author gsantiago
 *
 */
public class HttpEventSlot extends AbstractAlarmableByAnalysisUnitSlot<Integer, HttpEvent> {

    private String slotKey;

    public HttpEventSlot(String slotKey, HttpEvent firstEvent) {
        this.slotKey = slotKey;
        addEvent(firstEvent);
    }

    @Override
    protected void updateMetric(HttpEvent e) {
        AnalysisUnit m = e.getAnalysisUnit();
        if (metrics.containsKey(m)) {
            metrics.put(m, metrics.get(m) + 1);
        } else {
            metrics.put(m, 1);
        }

    }

    @Override
    protected Collection<HttpEvent> instantiateEvents() {
        return Collections.synchronizedList(new ArrayList<HttpEvent>());
    }

    public Integer getDefaultMetricValue() {
        return 0;
    }

    @Override
    public String getKey() {
        return slotKey;
    }
}
