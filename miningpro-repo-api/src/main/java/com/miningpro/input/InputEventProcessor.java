package com.miningpro.input;

import com.miningpro.core.event.Event;

/**
 * Created by gsantiago on 2/8/15.
 */
public interface InputEventProcessor<T extends Object, E extends Event> {
    E process(T input);
}
