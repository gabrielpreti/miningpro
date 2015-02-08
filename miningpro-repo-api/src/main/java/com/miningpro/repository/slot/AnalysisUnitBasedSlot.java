package com.miningpro.repository.slot;

import java.util.Collection;
import java.util.Set;

import com.miningpro.core.event.Event;
import com.miningpro.core.event.AnalysisUnit;

/**
 * Slots que mantém uma indexação dos eventos por AnalysisUnit.
 *
 * Created by gsantiago on 1/18/15.
 */
public interface AnalysisUnitBasedSlot<E extends Event> extends Slot<E> {
    Collection<E> getEvents(AnalysisUnit m);

    Set<AnalysisUnit> getAllAnalysisUnits();
}
