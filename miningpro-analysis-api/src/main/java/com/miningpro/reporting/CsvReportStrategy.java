package com.miningpro.reporting;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by gsantiago on 2/2/15.
 */
public class CsvReportStrategy implements ReportStrategy {
    File outputFile;
    String delimiter;
    PrintWriter writer;

    public CsvReportStrategy(String delimiter, String outputFile) {
        this.delimiter = delimiter;
        this.outputFile = new File(outputFile);
    }

    @Override
    public void init() {
        try {
            writer = new PrintWriter(outputFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(Map<String, String> data) {
        try {
            writer.println(String.format(generateOutputMask(data.values().size()),
                    data.values().toArray(new String[] {})));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void finish() {
        try {
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String generateOutputMask(int size) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < size; i++) {
            output.append("%s").append(delimiter);
        }
        if (size > 0) {
            output.deleteCharAt(output.lastIndexOf(delimiter));
        }
        return output.toString();
    }
}
