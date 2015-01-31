package com.miningpro.accesslog.event;


import com.miningpro.core.event.Measurable;

/**
 * Medições da junção de url e return code de eventos http Created by gsantiago on 1/18/15.
 */
public class UrlReturnCodeMeasurable extends Measurable {
    private String url, returnCode;

    public UrlReturnCodeMeasurable(String url, String returnCode) {
        this.url = url;
        this.returnCode = returnCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        UrlReturnCodeMeasurable that = (UrlReturnCodeMeasurable) o;

        if (returnCode != null ? !returnCode.equals(that.returnCode) : that.returnCode != null)
            return false;
        if (url != null ? !url.equals(that.url) : that.url != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (returnCode != null ? returnCode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s\t%s", url, returnCode);
    }
}
