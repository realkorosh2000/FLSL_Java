package com.flsl.keyIO;

import java.awt.*;
import java.awt.event.*;
import java.util.function.*;

/**
 * FLSL Mouse Input System
 * Usage: Add mouse listeners to AWT components
 */
public class MouseInput {
    private Component component;
    
    // Mouse click actions: (x, y) -> {}
    private Consumer<Point> clickAction;
    private Consumer<Point> pressAction;
    private Consumer<Point> releaseAction;
    
    // Mouse movement actions
    private Consumer<Point> moveAction;
    private Consumer<Point> dragAction;
    
    // Mouse wheel action
    private Consumer<Integer> wheelAction;
    
    public MouseInput(Component component) {
        this.component = component;
        setupMouseListener();
    }
    
    /**
     * Add action when mouse is CLICKED (pressed and released)
     * @param action Lambda: (x, y) -> { your code }
     */
    public void addClickListener(Consumer<Point> action) {
        clickAction = action;
    }
    
    /**
     * Add action when mouse is PRESSED
     * @param action Lambda: (x, y) -> { your code }
     */
    public void addPressListener(Consumer<Point> action) {
        pressAction = action;
    }
    
    /**
     * Add action when mouse is RELEASED
     * @param action Lambda: (x, y) -> { your code }
     */
    public void addReleaseListener(Consumer<Point> action) {
        releaseAction = action;
    }
    
    /**
     * Add action when mouse MOVES
     * @param action Lambda: (x, y) -> { your code }
     */
    public void addMoveListener(Consumer<Point> action) {
        moveAction = action;
    }
    
    /**
     * Add action when mouse is DRAGGED (moved while pressed)
     * @param action Lambda: (x, y) -> { your code }
     */
    public void addDragListener(Consumer<Point> action) {
        dragAction = action;
    }
    
    /**
     * Add action for mouse WHEEL
     * @param action Lambda: (scrollAmount) -> { your code }
     *               Positive = scroll down, Negative = scroll up
     */
    public void addWheelListener(Consumer<Integer> action) {
        wheelAction = action;
    }
    
    /**
     * Simple click listener without coordinates
     * @param action Lambda: () -> { your code }
     */
    public void addSimpleClickListener(Runnable action) {
        clickAction = p -> action.run();
    }
    
    private void setupMouseListener() {
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (clickAction != null) {
                    clickAction.accept(e.getPoint());
                }
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                if (pressAction != null) {
                    pressAction.accept(e.getPoint());
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (releaseAction != null) {
                    releaseAction.accept(e.getPoint());
                }
            }
        });
        
        component.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (moveAction != null) {
                    moveAction.accept(e.getPoint());
                }
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragAction != null) {
                    dragAction.accept(e.getPoint());
                }
            }
        });
        
        component.addMouseWheelListener(e -> {
            if (wheelAction != null) {
                wheelAction.accept(e.getWheelRotation());
            }
        });
    }
    
    /**
     * Static helper to create MouseInput for a component
     */
    public static MouseInput forComponent(Component component) {
        return new MouseInput(component);
    }
}