package com.miningpro.repository;

import java.util.Collection;

import com.miningpro.core.event.Event;

/**
 * Classe base para repositórios
 * 
 * @author gsantiago
 *
 * @param <E>
 *            Tipo do evento do repositório
 */
public interface Repository<E extends Event> {
	void addEvent(E event);

	Collection<E> getEvents();
}
