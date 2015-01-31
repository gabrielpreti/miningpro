package com.miningpro.repository.slot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.miningpro.core.event.Event;
import com.miningpro.core.event.Measurable;

/**
 * Implementação base para MeasurableMetricsSlot.
 * 
 * Created by gsantiago on 1/18/15.
 */
public abstract class AbstractMeasurableMetricsSlot<N extends Number, E extends Event> implements
        MeasurableMetricsSlot<N, E> {

    protected Collection<E> events;
    protected Map<Measurable, N> metrics;
    protected Map<Measurable, Collection<E>> metricsIndexedEvents;

    AbstractMeasurableMetricsSlot() {
        events = instantiateEvents();
        metrics = new HashMap<Measurable, N>();
        metricsIndexedEvents = new HashMap<Measurable, Collection<E>>();
    }

    protected abstract void updateMetric(E e);

    protected abstract Collection<E> instantiateEvents();

    @Override
    public void addEvent(E e) {
        events.add(e);
        updateMetric(e);

        Measurable m = e.getMeasurable();
        if (!metricsIndexedEvents.containsKey(m)) {
            metricsIndexedEvents.put(m, new ArrayList<E>());
        }
        metricsIndexedEvents.get(m).add(e);
    }

    @Override
    public N getMetric(Measurable m) {
        if (metrics.containsKey(m)) {
            return metrics.get(m);
        } else {
            return getDefaultMetricValue();
        }
    }

    @Override
    public Collection<E> getEvents(Measurable m) {
        return metricsIndexedEvents.get(m);
    }

    @Override
    public Set<Measurable> getAllMeasurables() {
        return metricsIndexedEvents.keySet();
    }

    @Override
    public Collection<E> getEvents() {
        return events;
    }
}
