package com.flsl.utils.Arrays;

/**
 * 3D array utility with depth formatting.
 * Format: [[0,0,0],[0,0,0],[0,0,0]]
 */
public class Array3D {
    
    /**
     * Creates a string representation of a 3D array.
     * @param array The 3D int array.
     * @return Formatted string.
     */
    public static String toString(int[][][] array) {
        if (array == null) return "null";
        if (array.length == 0) return "[[]]";
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append("[");
            for (int j = 0; j < array[i].length; j++) {
                sb.append("[");
                for (int k = 0; k < array[i][j].length; k++) {
                    sb.append(array[i][j][k]);
                    if (k < array[i][j].length - 1) sb.append(",");
                }
                sb.append("]");
                if (j < array[i].length - 1) sb.append(",");
            }
            sb.append("]");
            if (i < array.length - 1) sb.append(",");
        }
        return sb.toString();
    }
    
    /**
     * Creates a zero-filled 3D array.
     */
    public static int[][][] create(int depth, int rows, int cols) {
        return new int[depth][rows][cols];
    }
    
    /**
     * Fills the entire array with a value.
     */
    public static void fill(int[][][] array, int value) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                for (int k = 0; k < array[i][j].length; k++) {
                    array[i][j][k] = value;
                }
            }
        }
    }
    
    /**
     * Gets value at position.
     */
    public static int get(int[][][] array, int depth, int row, int col) {
        return array[depth][row][col];
    }
    
    /**
     * Sets value at position.
     */
    public static void set(int[][][] array, int depth, int row, int col, int value) {
        array[depth][row][col] = value;
    }
}