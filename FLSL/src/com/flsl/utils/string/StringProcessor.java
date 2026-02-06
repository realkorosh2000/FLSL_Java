package com.flsl.utils.string;

import java.util.List;

/**
 * Modifies and processes strings with various operations
 */
public class StringProcessor {
    private StringBuilder text;
    
    public StringProcessor(String text) {
        this.text = new StringBuilder(text);
    }
    
    public StringProcessor() {
        this.text = new StringBuilder();
    }
    
    // ----- BASIC OPERATIONS -----
    
    /**
     * Appends string to the end
     * @param string String to append
     * @return this processor for chaining
     */
    public StringProcessor append(String string) {
        text.append(string);
        return this;
    }
    
    /**
     * Adds/replaces text at specified position
     * @param str Text to add
     * @param indexStart Start position (inclusive)
     * @param indexEnd End position (exclusive) - if greater than length, uses whitespace
     * @return this processor for chaining
     */
    public StringProcessor add(String str, int indexStart, int indexEnd) {
        // Ensure indices are valid
        if (indexStart < 0) indexStart = 0;
        if (indexStart > text.length()) indexStart = text.length();
        
        // If indexEnd is beyond length, extend with whitespace
        if (indexEnd > text.length()) {
            int whitespaceNeeded = indexEnd - text.length();
            for (int i = 0; i < whitespaceNeeded; i++) {
                text.append(' ');
            }
        }
        
        // Replace the range with new text
        text.replace(indexStart, indexEnd, str);
        return this;
    }
    
    /**
     * Removes characters between positions
     * @param start Start index (inclusive)
     * @param end End index (exclusive)
     * @return this processor for chaining
     */
    public StringProcessor removeFilledIndex(int start, int end) {
        if (start < 0 || end > text.length() || start >= end) {
            return this;
        }
        text.delete(start, end);
        return this;
    }
    
    // ----- ADVANCED OPERATIONS -----
    
    /**
     * Secures string by removing dangerous patterns
     * @return this processor for chaining
     */
    public StringProcessor secure() {
        List<int[]> dangerous = StringScanner.scanForDangerous(text.toString());
        
        // Remove from end to start to maintain indices
        for (int i = dangerous.size() - 1; i >= 0; i--) {
            int[] pos = dangerous.get(i);
            removeFilledIndex(pos[0], pos[1]);
        }
        
        return this;
    }
    
    /**
     * Replaces all occurrences of find with replace
     */
    public StringProcessor replaceAll(String find, String replace) {
        String current = text.toString();
        int[] pos = StringScanner.findIndex(current, find);
        
        while (pos != null) {
            removeFilledIndex(pos[0], pos[1]);
            add(replace, pos[0], pos[0]);
            
            // Get updated string for next search
            current = text.toString();
            pos = StringScanner.findIndex(current, find);
        }
        
        return this;
    }
    
    // ----- UTILITIES -----
    
    /**
     * Gets the processed text
     */
    public String getText() {
        return text.toString();
    }
    
    /**
     * Clears all text
     */
    public void clear() {
        text = new StringBuilder();
    }
    
    /**
     * Gets length of current text
     */
    public int length() {
        return text.length();
    }
    
    @Override
    public String toString() {
        return getText();
    }
}