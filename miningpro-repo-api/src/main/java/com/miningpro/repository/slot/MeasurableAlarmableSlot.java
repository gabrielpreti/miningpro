package com.miningpro.repository.slot;

import com.miningpro.core.event.Event;
import com.miningpro.core.event.Measurable;

/**
 * Slots alarmáveis individualmente por Measurables
 *
 * Created by gsantiago on 1/18/15.
 */
public interface MeasurableAlarmableSlot<E extends Event> extends MeasurableSlot<E> {
    boolean isAlarming(Measurable event);

    void markAlarming(Measurable event);
}
