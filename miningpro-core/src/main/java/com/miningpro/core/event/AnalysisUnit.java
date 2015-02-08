package com.miningpro.core.event;

/**
 * Indica o que será medido de um evento. Implementações dessa classe devem sobrescrever equals e hashCode
 * 
 * Created by gsantiago on 1/18/15.
 */
public abstract class AnalysisUnit {

    public abstract boolean equals(Object other);

    public abstract int hashCode();
}
