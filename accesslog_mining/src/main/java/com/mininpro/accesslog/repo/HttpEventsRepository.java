package com.mininpro.accesslog.repo;

import com.miningpro.accesslog.event.HttpEvent;
import com.miningpro.core.event.Measurable;
import com.miningpro.repository.AbstractSlottedRepository;
import com.miningpro.repository.SlotKeyGenerationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by gsantiago on 1/17/15.
 */
public class HttpEventsRepository extends AbstractSlottedRepository<HttpEvent, HttpEventSlot> {
    private static final Logger log = LoggerFactory.getLogger(HttpEventsRepository.class);

    private SlotKeyGenerationStrategy slotKeyGenerationStrategy;

    public HttpEventsRepository(SlotKeyGenerationStrategy slotKeyGenerationStrategy) {
        super(slotKeyGenerationStrategy);
    }

    @Override
    protected HttpEventSlot createSlot(String slotKey, HttpEvent firstEvent) {
        return new HttpEventSlot(slotKey, firstEvent);
    }

    @Override
    public void addEvent(HttpEvent event) {
        if (event == null) {
            log.warn("Event is null");
            return;
        }

        super.addEvent(event);
    }

    public void printSlotEvents() throws IOException {
        File outputDir = new File("/tmp/slots/events/");
        if (outputDir.exists())
            outputDir.delete();
        outputDir.mkdir();

        for (String slotKey : slots.keySet()) {
            PrintWriter writer = new PrintWriter(new File(outputDir, "slot_" + slotKey));
            for (HttpEvent event : slots.get(slotKey).getEvents()) {
                writer.println(event);
            }
            writer.flush();
            writer.close();
        }
    }

    public void printSlotMetrics() throws IOException {
        File outputDir = new File("/tmp/slots/metrics/");
        if (outputDir.exists()) {
            outputDir.delete();
        }
        outputDir.mkdir();

        for (String slotKey : slots.keySet()) {
            PrintWriter writer = new PrintWriter(new File(outputDir, "slot_" + slotKey));
            HttpEventSlot slot = slots.get(slotKey);
            for (Measurable measurablePieces : slot.getAllMeasurables()) {
                writer.println(String.format("%s\t%d", measurablePieces, slot.getMetric(measurablePieces)));
            }
            writer.flush();
            writer.close();
        }
    }
}
