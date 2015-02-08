package com.miningpro.repository.slot;

import com.miningpro.core.event.Event;
import com.miningpro.core.event.AnalysisUnit;

/**
 * Slots alarmáveis individualmente por Measurables
 *
 * Created by gsantiago on 1/18/15.
 */
public interface AlarmableByAnalysisUnitSlot<E extends Event> extends AnalysisUnitBasedSlot<E> {
    boolean isAlarming(AnalysisUnit event);

    void markAlarming(AnalysisUnit event);
}
