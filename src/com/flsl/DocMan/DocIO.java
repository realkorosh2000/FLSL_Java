package com.flsl.DocMan;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles reading/writing operations for documents
 */
public class DocIO {
    private final DocBuilder docBuilder;
    private final File documentFile;
    private BufferedWriter writer;
    private boolean appendMode = true; // Default: append to file
    
    public DocIO(DocBuilder docBuilder) {
        this.docBuilder = docBuilder;
        this.documentFile = docBuilder.getFile();
    }
    
    // ----- WRITING METHODS -----
    
    /**
     * Writes text to document (auto-appends newline)
     */
    public void write(String text) throws IOException {
        write(text, true);
    }
    
    /**
     * Writes text to document with newline control
     */
    public void write(String text, boolean addNewline) throws IOException {
        try (FileWriter fw = new FileWriter(documentFile, appendMode);
             BufferedWriter bw = new BufferedWriter(fw)) {
            
            bw.write(text);
            if (addNewline) {
                bw.newLine();
            }
        }
    }
    
    /**
     * Writes multiple lines at once
     */
    public void writeAll(List<String> lines) throws IOException {
        try (FileWriter fw = new FileWriter(documentFile, appendMode);
             BufferedWriter bw = new BufferedWriter(fw)) {
            
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        }
    }
    
    /**
     * Overwrites entire document with new content
     */
    public void overwrite(String content) throws IOException {
        setAppendMode(false);
        write(content, false);
        setAppendMode(true); // Reset to append mode
    }
    
    /**
     * Clears the document (makes it empty)
     */
    public void clear() throws IOException {
        try (FileWriter fw = new FileWriter(documentFile, false)) {
            fw.write(""); // Write empty string
        }
    }
    
    // ----- READING METHODS -----
    
    /**
     * Reads entire document as a single string
     */
    public String read() throws IOException {
        List<String> lines = Files.readAllLines(documentFile.toPath());
        return String.join(System.lineSeparator(), lines);
    }
    
    /**
     * Reads document as list of lines
     */
    public List<String> readLines() throws IOException {
        return Files.readAllLines(documentFile.toPath());
    }
    
    /**
     * Reads first N lines
     */
    public List<String> readFirstLines(int count) throws IOException {
        List<String> allLines = readLines();
        return allLines.subList(0, Math.min(count, allLines.size()));
    }
    
    /**
     * Reads last N lines
     */
    public List<String> readLastLines(int count) throws IOException {
        List<String> allLines = readLines();
        int start = Math.max(0, allLines.size() - count);
        return allLines.subList(start, allLines.size());
    }
    
    /**
     * Searches for lines containing text
     */
    public List<String> search(String text) throws IOException {
        List<String> results = new ArrayList<>();
        List<String> lines = readLines();
        
        for (String line : lines) {
            if (line.contains(text)) {
                results.add(line);
            }
        }
        
        return results;
    }
    
    // ----- UTILITY METHODS -----
    
    public void setAppendMode(boolean append) {
        this.appendMode = append;
    }
    
    public boolean isAppendMode() {
        return appendMode;
    }
    
    public long getLineCount() throws IOException {
        return readLines().size();
    }
    
    public boolean isEmpty() throws IOException {
        return read().trim().isEmpty();
    }
    
    // ----- STATIC HELPERS -----
    
    public static DocIO createForUser(String fileName) {
        DocBuilder builder = new DocBuilder(fileName);
        return new DocIO(builder);
    }
    
    public static boolean userDocumentExists(String fileName) {
        String userHome = System.getProperty("user.home");
        String docPath = Paths.get(userHome, "Documents", fileName).toString();
        return new File(docPath).exists();
    }
}