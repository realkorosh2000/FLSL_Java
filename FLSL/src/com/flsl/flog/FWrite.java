package com.flsl.flog;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class FWrite {
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("[Date: MM/dd/yyyy HH:mm]");
    
    /**
     * Writes data to a file in a structured format.
     * 
     * @param file The file to write to (will be created/appended).
     * @param data A Map where key is the event/field name and value is its state.
     * @param enableDateExport If true, prefixes the line with a timestamp.
     * @throws IOException If the file cannot be written.
     */
    public static void write(File file, Map<String, Object> data, boolean enableDateExport) throws IOException {
        try (FileWriter fw = new FileWriter(file, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            
            StringBuilder line = new StringBuilder();
            
            // Add timestamp if enabled
            if (enableDateExport) {
                line.append(DATE_FORMAT.format(new Date())).append(" -> ");
            }
            
            // Format the data: [key = value, key2 = value2]
            line.append("[");
            boolean firstEntry = true;
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (!firstEntry) {
                    line.append(", ");
                }
                line.append(entry.getKey()).append(" = ").append(entry.getValue());
                firstEntry = false;
            }
            line.append("]");
            
            // Write to file
            bw.write(line.toString());
            bw.newLine();
        }
    }
}