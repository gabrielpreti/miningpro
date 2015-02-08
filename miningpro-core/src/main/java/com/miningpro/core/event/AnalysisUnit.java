package com.miningpro.core.event;

/**
 * Indica o que ser� medido de um evento. Implementa��es dessa classe devem sobrescrever equals e hashCode
 * 
 * Created by gsantiago on 1/18/15.
 */
public abstract class AnalysisUnit {

    public abstract boolean equals(Object other);

    public abstract int hashCode();
}
