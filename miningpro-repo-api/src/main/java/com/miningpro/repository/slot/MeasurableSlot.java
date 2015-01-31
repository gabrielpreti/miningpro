package com.miningpro.repository.slot;

import java.util.Collection;
import java.util.Set;

import com.miningpro.core.event.Event;
import com.miningpro.core.event.Measurable;

/**
 * Slots que mantém uma indexação dos eventos por Measurable.
 *
 * Created by gsantiago on 1/18/15.
 */
public interface MeasurableSlot<E extends Event> extends Slot<E> {
    Collection<E> getEvents(Measurable m);

    Set<Measurable> getAllMeasurables();
}
