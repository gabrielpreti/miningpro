package com.miningpro.repository.slot;

import java.util.HashSet;
import java.util.Set;

import com.miningpro.core.event.Event;
import com.miningpro.core.event.AnalysisUnit;

/**
 * Implementação base para AlarmableByAnalysisUnitSlot.
 * 
 * Created by gsantiago on 1/18/15.
 * @param <N>
 * @param <E>
 */
public abstract class AbstractAlarmableByAnalysisUnitSlot<N extends Number, E extends Event> extends
        AbstractAnalysisUnitBasedMetricsSlot<N, E> implements AlarmableByAnalysisUnitSlot<E> {

    private Set<AnalysisUnit> alarmingEvents;

    protected AbstractAlarmableByAnalysisUnitSlot() {
        alarmingEvents = new HashSet<AnalysisUnit>();
    }

    @Override
    public boolean isAlarming(AnalysisUnit m) {
        return alarmingEvents.contains(m);
    }

    @Override
    public void markAlarming(AnalysisUnit m) {
        alarmingEvents.add(m);
    }
}
