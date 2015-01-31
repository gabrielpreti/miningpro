package com.miningpro.repository.slot;

import java.util.Collection;

import com.miningpro.core.event.Event;

/**
 * Interface base para slots.
 * 
 * @author gsantiago
 *
 * @param <E>
 */
public interface Slot<E extends Event> {

    void addEvent(E e);

    Collection<E> getEvents();

    String getKey();
}
