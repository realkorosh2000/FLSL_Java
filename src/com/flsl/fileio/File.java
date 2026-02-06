package com.flsl.fileio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Enhanced file wrapper with utility methods
 */
public class File {
    private Path path;
    
    public File(String path) {
        this.path = Paths.get(path);
    }
    
    // ----- FILE PROPERTIES -----
    public boolean exists() {
        return Files.exists(path);
    }
    
    public boolean isDirectory() {
        return Files.isDirectory(path);
    }
    
    public long size() throws IOException {
        return Files.size(path);
    }
    
    public String getName() {
        return path.getFileName().toString();
    }
    
    public String getParent() {
        Path parent = path.getParent();
        return parent != null ? parent.toString() : null;
    }
    
    public String getPath() {
        return path.toString();
    }
    
    // ----- FILE OPERATIONS -----
    public boolean create() throws IOException {
        if (!exists()) {
            Files.createFile(path);
            return true;
        }
        return false;
    }
    
    public boolean delete() throws IOException {
        return Files.deleteIfExists(path);
    }
    
    public boolean moveTo(String newPath) throws IOException {
        Path dest = Paths.get(newPath);
        Files.move(path, dest);
        this.path = dest;  // NOW THIS WORKS!
        return true;
    }
    
    public boolean copyTo(String newPath) throws IOException {
        Path dest = Paths.get(newPath);
        Files.copy(path, dest);
        return true;
    }
    
    // ----- QUICK ACCESS -----
    public byte[] readBytes() throws IOException {
        return Files.readAllBytes(path);
    }
    
    public List<String> readLines() throws IOException {
        return Files.readAllLines(path);
    }
    
    public void writeBytes(byte[] data) throws IOException {
        Files.write(path, data);
    }
    
    public void writeLines(List<String> lines) throws IOException {
        Files.write(path, lines);
    }
    
    // ----- STATIC METHODS -----
    public static File fromPath(String path) {
        return new File(path);
    }
    
    public static boolean exists(String path) {
        return Files.exists(Paths.get(path));
    }
    
    public static void createDirectory(String path) throws IOException {
        Files.createDirectories(Paths.get(path));
    }
    
    // ----- GETTER/SETTER -----
    public Path getNioPath() {
        return path;
    }
    
    public void setPath(String newPath) {
        this.path = Paths.get(newPath);
    }
}