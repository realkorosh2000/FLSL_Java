package com.flsl.fileio;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Enhanced file reader with various reading strategies
 */
public class Reader {
    private final File file;
    private BufferedReader reader;
    
    public Reader(File file) {
        this.file = file;
    }
    
    public Reader(String filePath) {
        this.file = new File(filePath);
    }
    
    // ----- OPEN/CLOSE -----
    public void open() throws IOException {
        close(); // Close if already open
        reader = new BufferedReader(
            new InputStreamReader(
                new FileInputStream(file.getPath())
            )
        );
    }
    
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
            reader = null;
        }
    }
    
    // ----- READING METHODS -----
    public String readLine() throws IOException {
        if (reader == null) open();
        return reader.readLine();
    }
    
    public String readAll() throws IOException {
        if (reader == null) open();
        
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append(System.lineSeparator());
        }
        
        // Remove last newline if present
        if (content.length() > 0) {
            content.setLength(content.length() - System.lineSeparator().length());
        }
        
        return content.toString();
    }
    
    public List<String> readLines() throws IOException {
        if (reader == null) open();
        
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }
    
    // ----- ADVANCED READING -----
    public String readUntil(String delimiter) throws IOException {
        if (reader == null) open();
        
        StringBuilder result = new StringBuilder();
        int ch;
        while ((ch = reader.read()) != -1) {
            char c = (char) ch;
            result.append(c);
            
            if (result.toString().endsWith(delimiter)) {
                // Remove delimiter from result
                result.setLength(result.length() - delimiter.length());
                break;
            }
        }
        
        return result.toString();
    }
    
    public String peekLine() throws IOException {
        if (reader == null) open();
        
        reader.mark(1024); // Mark current position
        String line = reader.readLine();
        reader.reset(); // Return to marked position
        return line;
    }
    
    // ----- STATISTICS -----
    public int countLines() throws IOException {
        if (reader == null) open();
        
        int count = 0;
        while (reader.readLine() != null) {
            count++;
        }
        
        // Re-open for future reads
        close();
        open();
        
        return count;
    }
    
    // ----- AUTO-CLOSE PATTERN -----
    public interface ReadOperation<T> {
        T execute(Reader reader) throws IOException;
    }
    
    public static <T> T withReader(String filePath, ReadOperation<T> operation) 
            throws IOException {
        Reader reader = new Reader(filePath);
        try {
            reader.open();
            return operation.execute(reader);
        } finally {
            reader.close();
        }
    }
}