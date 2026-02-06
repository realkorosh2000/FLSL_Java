package com.flsl.utils.Arrays;

/**
 * 2D array utility with row-major formatting.
 * Format: [0,0,0,0,
 *          0,0,0,0,
 *          0,0,0,0,
 *          0,0,0,0]
 */
public class Array2D {
    
    /**
     * Creates a string representation of a 2D array.
     * @param matrix The 2D int array.
     * @return Formatted string with line breaks.
     */
    public static String toString(int[][] matrix) {
        if (matrix == null) return "null";
        if (matrix.length == 0) return "[]";
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            sb.append("[");
            for (int j = 0; j < matrix[i].length; j++) {
                sb.append(matrix[i][j]);
                if (j < matrix[i].length - 1) sb.append(",");
            }
            sb.append("]");
            if (i < matrix.length - 1) {
                sb.append(",");
                sb.append(System.lineSeparator());
                sb.append(" ");
            }
        }
        return sb.toString();
    }
    
    /**
     * Creates a zero-filled 2D array.
     */
    public static int[][] create(int rows, int cols) {
        return new int[rows][cols];
    }
    
    /**
     * Fills the entire array with a value.
     */
    public static void fill(int[][] matrix, int value) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = value;
            }
        }
    }
    
    /**
     * Gets value at position.
     */
    public static int get(int[][] matrix, int row, int col) {
        return matrix[row][col];
    }
    
    /**
     * Sets value at position.
     */
    public static void set(int[][] matrix, int row, int col, int value) {
        matrix[row][col] = value;
    }
}