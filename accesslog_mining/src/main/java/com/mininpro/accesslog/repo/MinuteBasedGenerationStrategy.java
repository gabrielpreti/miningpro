package com.mininpro.accesslog.repo;

import com.miningpro.accesslog.event.HttpEvent;
import com.miningpro.repository.SlotKeyGenerationStrategy;

import java.text.SimpleDateFormat;

/**
 * Created by gsantiago on 1/17/15.
 */
public class MinuteBasedGenerationStrategy implements SlotKeyGenerationStrategy<HttpEvent> {

    @Override
    public String generateKey(HttpEvent event) {
        return new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(event.getTime().getTime());
    }
}
