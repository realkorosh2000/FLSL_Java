package com.flsl.fileio;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Enhanced file reader with various reading strategies.
 * IMPLEMENTED AutoCloseable to prevent resource leaks.
 */
public class Reader implements AutoCloseable {  
    private final File file;
    private BufferedReader reader;
    private boolean isOpen = false; 
    
    public Reader(File file) {
        this.file = file;
    }
    
    public Reader(String filePath) {
        this.file = new File(filePath);
    }
    
    // ----- OPEN/CLOSE -----
    public void open() throws IOException {
        if (isOpen) {
            return;
        }
        
        reader = new BufferedReader(
            new InputStreamReader(
                new FileInputStream(file.getPath())
            )
        );
        isOpen = true; 
    }
    
    @Override
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
            reader = null;
            isOpen = false;
        }
    }
    
    // ----- READING METHODS -----
    public String readLine() throws IOException {
        ensureOpen();
        return reader.readLine();
    }
    
    public String readAll() throws IOException {
        ensureOpen();
        
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append(System.lineSeparator());
        }
        
        if (content.length() > 0) {
            content.setLength(content.length() - System.lineSeparator().length());
        }
        
        return content.toString();
    }
    
    public List<String> readLines() throws IOException {
        ensureOpen();
        
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }
    
    // ----- ADVANCED READING -----
    public String readUntil(String delimiter) throws IOException {
        ensureOpen();
        
        StringBuilder result = new StringBuilder();
        int ch;
        while ((ch = reader.read()) != -1) {
            char c = (char) ch;
            result.append(c);
            
            if (result.toString().endsWith(delimiter)) {
                result.setLength(result.length() - delimiter.length());
                break;
            }
        }
        
        return result.toString();
    }
    
    public String peekLine() throws IOException {
        ensureOpen();
        
        reader.mark(1024);
        String line = reader.readLine();
        reader.reset();
        return line;
    }
    
    // ----- STATISTICS -----
    public int countLines() throws IOException {
        ensureOpen();
        
        int count = 0;
        while (reader.readLine() != null) {
            count++;
        }
        
        
        close();
        open();
        
        return count;
    }
    
    // ----- PRIVATE HELPER -----
    private void ensureOpen() throws IOException {
        if (!isOpen) {
            open();
        }
        if (reader == null) {
            throw new IOException("Reader is not properly initialized");
        }
    }
    
    // ----- AUTO-CLOSE PATTERN -----
    public interface ReadOperation<T> {
        T execute(Reader reader) throws IOException;
    }
    
    public static <T> T withReader(String filePath, ReadOperation<T> operation) 
            throws IOException {
        try (Reader reader = new Reader(filePath)) {  
            reader.open();
            return operation.execute(reader);
            
        }
    }
    
    
    @Override
    protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }
}