package com.miningpro.repository;

import com.miningpro.core.event.Event;

public interface SlotKeyGenerationStrategy<E extends Event> {

    String generateKey(E e);
}
