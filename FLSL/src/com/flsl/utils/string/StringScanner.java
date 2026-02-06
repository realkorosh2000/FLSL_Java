package com.flsl.utils.string;

/**
 * Finds patterns and indices in strings
 */
public class StringScanner {
    
    /**
     * Finds start and end index of a substring
     * @param source String to search in
     * @param value Substring to find
     * @return int[2] where [0]=start, [1]=end, or null if not found
     */
    public static int[] findIndex(String source, String value) {
        if (source == null || value == null || value.isEmpty()) {
            return null;
        }
        
        int start = source.indexOf(value);
        if (start == -1) {
            return null;
        }
        
        return new int[]{start, start + value.length()};
    }
    
    /**
     * Finds all occurrences of a substring
     */
    public static java.util.List<int[]> findAllIndices(String source, String value) {
        java.util.List<int[]> results = new java.util.ArrayList<>();
        
        if (source == null || value == null || value.isEmpty()) {
            return results;
        }
        
        int index = 0;
        while (index < source.length()) {
            int start = source.indexOf(value, index);
            if (start == -1) break;
            
            results.add(new int[]{start, start + value.length()});
            index = start + 1;
        }
        
        return results;
    }
    
    /**
     * Security scan for dangerous patterns
     */
    public static java.util.List<int[]> scanForDangerous(String text) {
        String[] dangerous = {"//", "--", "/*", "*/", ";", "'", "\"", 
                              "<script", "<?php", "${", "`"};
        java.util.List<int[]> results = new java.util.ArrayList<>();
        
        for (String pattern : dangerous) {
            results.addAll(findAllIndices(text, pattern));
        }
        
        return results;
    }
}