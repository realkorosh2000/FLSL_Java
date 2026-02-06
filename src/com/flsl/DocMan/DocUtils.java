package com.flsl.DocMan;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Additional document utilities
 */
public class DocUtils {
    
    /**
     * Creates a document with timestamp in filename
     */
    public static DocBuilder createTimestampedDoc(String baseName) {
        String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = baseName + "_" + timestamp + ".txt";
        return new DocBuilder(fileName);
    }
    
    /**
     * Creates backup of a document
     */
    public static boolean backupDocument(DocBuilder original) throws IOException {
        String originalPath = original.getFullPath();
        String backupName = "backup_" + original.getFileName();
        DocBuilder backup = new DocBuilder(backupName);
        
        Files.copy(
            Paths.get(originalPath),
            Paths.get(backup.getFullPath()),
            StandardCopyOption.REPLACE_EXISTING
        );
        
        return backup.exists();
    }
    
    /**
     * Gets list of all .txt documents in user's Documents folder
     */
    public static List<String> listAllDocuments() throws IOException {
        String docsPath = Paths.get(System.getProperty("user.home"), "Documents").toString();
        
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(docsPath), "*.txt")) {
            List<String> documents = new ArrayList<>();
            for (Path entry : stream) {
                documents.add(entry.getFileName().toString());
            }
            return documents;
        }
    }
}