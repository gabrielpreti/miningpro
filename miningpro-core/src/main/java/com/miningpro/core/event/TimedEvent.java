package com.miningpro.core.event;

import java.util.Date;

/**
 * Eventos que possuem um tempo específico associado.
 * 
 * @author gsantiago
 *
 */
public interface TimedEvent extends Event {
    Date getTime();
}
