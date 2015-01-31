package com.miningpro.repository.slot;

import com.miningpro.core.event.Event;
import com.miningpro.core.event.Measurable;

/**
 * Slots que armazenam métricas para cada Measurable relacionada ao conjunto de eventos.
 * 
 * @author gsantiago
 *
 * @param <N>
 *            Tipo da métrica, deve ser numérica
 * @param <E>
 *            Tipo do evento armazenado.
 */
public interface MeasurableMetricsSlot<N extends Number, E extends Event> extends MeasurableSlot<E> {

    N getMetric(Measurable m);

    N getDefaultMetricValue();
}
