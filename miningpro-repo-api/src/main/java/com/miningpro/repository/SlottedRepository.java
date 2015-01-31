package com.miningpro.repository;

import java.util.Collection;

import com.miningpro.core.event.Event;
import com.miningpro.repository.slot.Slot;

/**
 * Repositórios baseados em slots.
 * 
 * @author gsantiago
 *
 * @param <E>
 *            Tipo do evento
 * @param <S>
 *            Tipo do Slot
 */
public interface SlottedRepository<E extends Event, S extends Slot<E>> extends Repository<E> {

	Collection<S> getSlots();
}
