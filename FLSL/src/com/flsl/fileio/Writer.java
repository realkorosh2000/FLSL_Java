package com.flsl.fileio;

import java.io.*;
import java.util.List;

/**
 * Enhanced file writer with various writing strategies
 */
public class Writer {
    private final File file;
    private BufferedWriter writer;
    private boolean appendMode;
    
    public Writer(File file) {
        this(file, false);
    }
    
    public Writer(File file, boolean append) {
        this.file = file;
        this.appendMode = append;
    }
    
    public Writer(String filePath) {
        this(new File(filePath), false);
    }
    
    public Writer(String filePath, boolean append) {
        this(new File(filePath), append);
    }
    
    // ----- OPEN/CLOSE -----
    public void open() throws IOException {
        close(); // Close if already open
        
        // Create parent directories if needed
        String parent = file.getParent();
        if (parent != null) {
            new java.io.File(parent).mkdirs();
        }
        
        writer = new BufferedWriter(
            new OutputStreamWriter(
                new FileOutputStream(file.getPath(), appendMode)
            )
        );
    }
    
    public void close() throws IOException {
        if (writer != null) {
            writer.flush();
            writer.close();
            writer = null;
        }
    }
    
    // ----- WRITING METHODS -----
    public void write(String text) throws IOException {
        if (writer == null) open();
        writer.write(text);
    }
    
    public void writeLine(String line) throws IOException {
        if (writer == null) open();
        writer.write(line);
        writer.newLine();
    }
    
    public void writeLines(List<String> lines) throws IOException {
        if (writer == null) open();
        
        for (String line : lines) {
            writer.write(line);
            writer.newLine();
        }
    }
    
    // ----- FORMATTED WRITING -----
    public void writef(String format, Object... args) throws IOException {
        write(String.format(format, args));
    }
    
    public void writeLinef(String format, Object... args) throws IOException {
        writeLine(String.format(format, args));
    }
    
    // ----- BULK OPERATIONS -----
    public void writeAll(String content) throws IOException {
        open();
        write(content);
        close();
    }
    
    public void writeAllLines(List<String> lines) throws IOException {
        open();
        writeLines(lines);
        close();
    }
    
    // ----- FLUSH CONTROL -----
    public void flush() throws IOException {
        if (writer != null) {
            writer.flush();
        }
    }
    
    public void setAutoFlush(boolean autoFlush) throws IOException {
        if (autoFlush && writer != null) {
            writer.flush();
        }
    }
    
    // ----- AUTO-CLOSE PATTERN -----
    public interface WriteOperation {
        void execute(Writer writer) throws IOException;
    }
    
    public static void withWriter(String filePath, WriteOperation operation) 
            throws IOException {
        withWriter(filePath, false, operation);
    }
    
    public static void withWriter(String filePath, boolean append, 
                                  WriteOperation operation) throws IOException {
        Writer writer = new Writer(filePath, append);
        try {
            writer.open();
            operation.execute(writer);
        } finally {
            writer.close();
        }
    }
}