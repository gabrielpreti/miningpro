package com.miningpro.repository.slot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.miningpro.core.event.AnalysisUnit;
import com.miningpro.core.event.Event;

/**
 * Implementação base para AnalysisUnitBasedMetricsSlot.
 * 
 * Created by gsantiago on 1/18/15.
 */
public abstract class AbstractAnalysisUnitBasedMetricsSlot<N extends Number, E extends Event> implements
        AnalysisUnitBasedMetricsSlot<N, E> {

    protected Collection<E> events;
    protected Map<AnalysisUnit, N> metrics;
    protected Map<AnalysisUnit, Collection<E>> analysisUnitIndexedEvents;

    AbstractAnalysisUnitBasedMetricsSlot() {
        events = instantiateEvents();
        metrics = new HashMap<AnalysisUnit, N>();
        analysisUnitIndexedEvents = new HashMap<AnalysisUnit, Collection<E>>();
    }

    protected abstract void updateMetric(E e);

    protected abstract Collection<E> instantiateEvents();

    @Override
    public void addEvent(E e) {
        events.add(e);
        updateMetric(e);

        AnalysisUnit m = e.getAnalysisUnit();
        if (!analysisUnitIndexedEvents.containsKey(m)) {
            analysisUnitIndexedEvents.put(m, new ArrayList<E>());
        }
        analysisUnitIndexedEvents.get(m).add(e);
    }

    @Override
    public N getMetric(AnalysisUnit m) {
        if (metrics.containsKey(m)) {
            return metrics.get(m);
        } else {
            return getDefaultMetricValue();
        }
    }

    @Override
    public Collection<E> getEvents(AnalysisUnit m) {
        return analysisUnitIndexedEvents.get(m);
    }

    @Override
    public Set<AnalysisUnit> getAllAnalysisUnits() {
        return analysisUnitIndexedEvents.keySet();
    }

    @Override
    public Collection<E> getEvents() {
        return events;
    }
}
