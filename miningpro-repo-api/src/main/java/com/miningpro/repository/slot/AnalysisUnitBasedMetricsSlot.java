package com.miningpro.repository.slot;

import com.miningpro.core.event.Event;
import com.miningpro.core.event.AnalysisUnit;

/**
 * Slots que armazenam métricas para cada AnalysisUnit relacionada ao conjunto de eventos.
 * 
 * @author gsantiago
 *
 * @param <N>
 *            Tipo da métrica, deve ser numérica
 * @param <E>
 *            Tipo do evento armazenado.
 */
public interface AnalysisUnitBasedMetricsSlot<N extends Number, E extends Event> extends AnalysisUnitBasedSlot<E> {

    N getMetric(AnalysisUnit m);

    N getDefaultMetricValue();
}
