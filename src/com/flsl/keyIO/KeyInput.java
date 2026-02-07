package com.flsl.keyIO;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * FLSL Key Input System
 * Usage: Add keyboard listeners to AWT components
 */
public class KeyInput {
    private Component component;
    private Map<Integer, Runnable> keyPressedActions = new HashMap<>();
    private Map<Integer, Runnable> keyReleasedActions = new HashMap<>();
    
    public KeyInput(Component component) {
        this.component = component;
        setupKeyListener();
    }
    
    /**
     * Add action when a key is PRESSED
     * @param keyCode KeyEvent.VK_ constant (e.g., KeyEvent.VK_W)
     * @param action Lambda: () -> { your code }
     */
    public void addKeyPressedListener(int keyCode, Runnable action) {
        keyPressedActions.put(keyCode, action);
    }
    
    /**
     * Add action when a key is RELEASED
     * @param keyCode KeyEvent.VK_ constant
     * @param action Lambda: () -> { your code }
     */
    public void addKeyReleasedListener(int keyCode, Runnable action) {
        keyReleasedActions.put(keyCode, action);
    }
    
    /**
     * Convenience: Add WASD controls
     */
    public void addWASDControls(Runnable up, Runnable left, Runnable down, Runnable right) {
        if (up != null) addKeyPressedListener(KeyEvent.VK_W, up);
        if (left != null) addKeyPressedListener(KeyEvent.VK_A, left);
        if (down != null) addKeyPressedListener(KeyEvent.VK_S, down);
        if (right != null) addKeyPressedListener(KeyEvent.VK_D, right);
    }
    
    /**
     * Add arrow key controls
     */
    public void addArrowControls(Runnable up, Runnable left, Runnable down, Runnable right) {
        if (up != null) addKeyPressedListener(KeyEvent.VK_UP, up);
        if (left != null) addKeyPressedListener(KeyEvent.VK_LEFT, left);
        if (down != null) addKeyPressedListener(KeyEvent.VK_DOWN, down);
        if (right != null) addKeyPressedListener(KeyEvent.VK_RIGHT, right);
    }
    
    /**
     * Remove all key listeners
     */
    public void clearAllListeners() {
        keyPressedActions.clear();
        keyReleasedActions.clear();
    }
    
    private void setupKeyListener() {
        component.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Runnable action = keyPressedActions.get(e.getKeyCode());
                if (action != null) {
                    action.run();
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                Runnable action = keyReleasedActions.get(e.getKeyCode());
                if (action != null) {
                    action.run();
                }
            }
        });
        
        // Make sure component can receive focus
        component.setFocusable(true);
        component.requestFocusInWindow();
    }
    
    /**
     * Static helper to create KeyInput for a component
     */
    public static KeyInput forComponent(Component component) {
        return new KeyInput(component);
    }
}