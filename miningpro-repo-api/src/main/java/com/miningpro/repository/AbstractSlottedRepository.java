package com.miningpro.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.miningpro.core.event.Event;
import com.miningpro.repository.slot.Slot;

/**
 * Classe base para implementação de repositórios baseados em slots.
 * 
 * Created by gsantiago on 1/18/15.
 */
public abstract class AbstractSlottedRepository<E extends Event, S extends Slot<E>> implements SlottedRepository<E, S> {
    protected Map<String, S> slots;
    private SlotKeyGenerationStrategy slotKeyGenerationStrategy;

    protected AbstractSlottedRepository(SlotKeyGenerationStrategy slotKeyGenerationStrategy) {
        this.slots = new TreeMap<String, S>();
        this.slotKeyGenerationStrategy = slotKeyGenerationStrategy;
    }

    protected abstract S createSlot(String slotKey, E firstEvent);

    @Override
    public Collection<S> getSlots() {
        return slots.values();
    }

    @Override
    public void addEvent(E event) {
        String slotKey = slotKeyGenerationStrategy.generateKey(event);
        S existentSlot = slots.get(slotKey);
        if (existentSlot == null) {
            slots.put(slotKey, createSlot(slotKey, event));
        } else {
            existentSlot.addEvent(event);
        }
    }

    @Override
    public Collection<E> getEvents() {
        List<E> allEvents = new ArrayList<E>();
        for (S slot : slots.values()) {
            allEvents.addAll(slot.getEvents());
        }
        return allEvents;
    }
}
