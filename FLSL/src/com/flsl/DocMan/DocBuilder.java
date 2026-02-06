package com.flsl.DocMan;

import java.io.File;
import java.nio.file.Paths;

/**
 * Creates and manages documents in user's Documents folder
 */
public class DocBuilder {
    private final String fileName;
    private File documentFile;
    private String userHome;
    
    public DocBuilder(String fileName) {
        this.fileName = fileName;
        this.userHome = System.getProperty("user.home");
        initializeDocument();
    }
    
    private void initializeDocument() {
        // Create Documents folder path
        String documentsPath = Paths.get(userHome, "Documents").toString();
        File documentsFolder = new File(documentsPath);
        
        // Create Documents folder if it doesn't exist
        if (!documentsFolder.exists()) {
            documentsFolder.mkdirs();
        }
        
        // Create the document file
        this.documentFile = new File(Paths.get(documentsPath, fileName).toString());
        
        try {
            if (!documentFile.exists()) {
                documentFile.createNewFile();
                System.out.println("✓ Created: " + getFullPath());
            } else {
                System.out.println("✓ Using existing: " + getFullPath());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create document: " + e.getMessage(), e);
        }
    }
    
    public String getFullPath() {
        return documentFile.getAbsolutePath();
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public File getFile() {
        return documentFile;
    }
    
    public boolean delete() {
        return documentFile.delete();
    }
    
    public boolean exists() {
        return documentFile.exists();
    }
    
    public long size() {
        return documentFile.length();
    }
}