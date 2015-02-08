package com.miningpro.run;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.miningpro.accesslog.event.HttpEvent;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miningpro.input.InputEventProcessor;

/**
 * Created by gsantiago on 2/8/15.
 */
public class AccessLogLineProcessor implements InputEventProcessor<String, HttpEvent>, Processor {
    private static final Logger log = LoggerFactory.getLogger(AccessLogLineProcessor.class);
    Pattern p;
    String dateFormat;

    public AccessLogLineProcessor() {
        p = Pattern
                .compile(".*?\\[(?<date>.*?)\\s\\-0\\d00]\\s\"(GET|POST)\\s\\/(?<url>.*?)(\\s|\\?).*?\"\\s(?<returncode>\\d*)\\s");
        dateFormat = "dd/MMM/yyyy:HH:mm:ss";
    }

    @Override
    public HttpEvent process(String input) {
        Matcher m = p.matcher(input);
        if (!m.find()) {
            log.warn(String.format("Line %s does not match", input));
            return null;
        }

        Date date;
        try {
            date = new SimpleDateFormat(dateFormat).parse(m.group("date"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return new HttpEvent(date, m.group("url"), m.group("returncode"));
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody(String.class);
        exchange.getIn().setBody(process(body));
    }
}
