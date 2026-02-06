package com.flsl.flog;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FRead {
    
    /**
     * Reads the last formatted line from a file.
     * 
     * @param file The file to read from.
     * @param readRaw If true, returns the full line as a String.
     *                 If false, parses the data section into a Map.
     * @return Depending on readRaw: either a String or a Map<String, String>.
     * @throws IOException If the file cannot be read or format is invalid.
     */
    public static Object read(File file, boolean readRaw) throws IOException {
        String lastLine = getLastLine(file);
        
        if (readRaw) {
            return lastLine; // Return the full raw line
        }
        
        // Parse the structured data part into a Map
        return parseDataLine(lastLine);
    }
    
    // Helper to get the last non-empty line
    private static String getLastLine(File file) throws IOException {
        String lastLine = "";
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            long length = raf.length() - 1;
            if (length < 0) return "";
            
            for (long seek = length; seek >= 0; seek--) {
                raf.seek(seek);
                char c = (char) raf.read();
                if (c == '\n' && seek != length) {
                    break;
                }
                lastLine = c + lastLine;
            }
        }
        return lastLine.trim();
    }
    
    // Core parsing logic: extracts "[key = value, ...]" into a Map
    private static Map<String, String> parseDataLine(String line) {
        Map<String, String> result = new HashMap<>();
        
        // Find the data section (after "-> " if timestamp exists)
        int dataStart = line.indexOf("[", line.indexOf("]") + 1);
        if (dataStart == -1) dataStart = line.indexOf("[");
        if (dataStart == -1) return result;
        
        int dataEnd = line.lastIndexOf("]");
        if (dataEnd == -1) return result;
        
        String dataSection = line.substring(dataStart + 1, dataEnd);
        
        // Split by ", " to get key-value pairs
        String[] pairs = dataSection.split(", ");
        for (String pair : pairs) {
            String[] kv = pair.split(" = ", 2);
            if (kv.length == 2) {
                // Convert "yes"/"no" to boolean strings if desired
                String value = kv[1];
                if ("yes".equalsIgnoreCase(value)) value = "true";
                if ("no".equalsIgnoreCase(value)) value = "false";
                result.put(kv[0].trim(), value);
            }
        }
        return result;
    }
}