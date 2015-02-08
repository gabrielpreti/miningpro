package com.miningpro.repository.slot;

import com.miningpro.core.event.Event;

/**
 * Slots alarmáveis por eventos.
 * 
 * @author gsantiago
 *
 * @param <E>
 *            Tipo do evento que o slot armazena
 */
public interface AlarmableByEventSlot<E extends Event> extends Slot<E> {

    boolean isAlarming(E event);

    void markAlarming(E event);
}
