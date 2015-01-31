package com.miningpro.repository.slot;

import com.miningpro.core.event.Event;

/**
 * Slots alarmáveis
 * 
 * Created by gsantiago on 1/18/15.
 */
public interface AlarmableSlot<E extends Event> extends Slot<E> {
    boolean isAlarming();

    void markAlarming();
}
