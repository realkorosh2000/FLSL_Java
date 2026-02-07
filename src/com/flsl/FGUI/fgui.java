package com.flsl.FGUI;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


/**
 * FLSL GUI Framework - Simplified AWT wrappers
 * Now with built-in keyboard and mouse input support
 */
public class fgui {
    
    // ==================== INPUT SYSTEMS ====================
    public static class KeyInput {
        private int keyCode;
        private Runnable action;
        
        public KeyInput(int keyCode, Runnable action) {
            this.keyCode = keyCode;
            this.action = action;
        }
        
        public int getKeyCode() { return keyCode; }
        public Runnable getAction() { return action; }
        
        // Common key constants for convenience
        public static final int KEY_W = KeyEvent.VK_W;
        public static final int KEY_A = KeyEvent.VK_A;
        public static final int KEY_S = KeyEvent.VK_S;
        public static final int KEY_D = KeyEvent.VK_D;
        public static final int KEY_UP = KeyEvent.VK_UP;
        public static final int KEY_LEFT = KeyEvent.VK_LEFT;
        public static final int KEY_DOWN = KeyEvent.VK_DOWN;
        public static final int KEY_RIGHT = KeyEvent.VK_RIGHT;
        public static final int KEY_SPACE = KeyEvent.VK_SPACE;
        public static final int KEY_ENTER = KeyEvent.VK_ENTER;
        public static final int KEY_ESC = KeyEvent.VK_ESCAPE;
    }
    
    public static class MouseInput {
        public enum EventType { CLICK, PRESS, RELEASE, MOVE, DRAG, WHEEL }
        
        private EventType type;
        private Consumer<Point> action;
        private Consumer<Integer> wheelAction;
        
        // For click/press/release/move/drag events
        public MouseInput(EventType type, Consumer<Point> action) {
            this.type = type;
            this.action = action;
        }
        
        // For wheel events
        public MouseInput(Consumer<Integer> wheelAction) {
            this.type = EventType.WHEEL;
            this.wheelAction = wheelAction;
        }
        
        public EventType getType() { return type; }
        public Consumer<Point> getAction() { return action; }
        public Consumer<Integer> getWheelAction() { return wheelAction; }
    }
    
    // ==================== BASE COMPONENT ====================
    public static abstract class FComponent {
        protected Component awtComponent;
        protected List<KeyInput> keyInputs = new ArrayList<>();
        protected List<MouseInput> mouseInputs = new ArrayList<>();
        
        public FComponent(Component comp) {
            this.awtComponent = comp;
        }
        
        public void setSize(int width, int height) {
            awtComponent.setSize(width, height);
        }
        
        public void setLocation(int x, int y) {
            awtComponent.setLocation(x, y);
        }
        
        public void setVisible(boolean visible) {
            awtComponent.setVisible(visible);
        }
        
        public Component getAWTComponent() {
            return awtComponent;
        }
        
        public void setFocusable(boolean focusable) {
            awtComponent.setFocusable(focusable);
        }
        
        public void requestFocus() {
            awtComponent.requestFocusInWindow();
        }
        
        /**
         * Add keyboard listener to this component
         * @param keyInput KeyInput object with key code and action
         */
        public void addKeyInput(KeyInput keyInput) {
            keyInputs.add(keyInput);
            setupKeyListeners(); // Re-setup listeners
        }
        
        /**
         * Add mouse listener to this component
         * @param mouseInput MouseInput object with event type and action
         */
        public void addMouseInput(MouseInput mouseInput) {
            mouseInputs.add(mouseInput);
            setupMouseListeners(); // Re-setup listeners
        }
        
        /**
         * Convenience: Add keyboard listener with lambda
         * @param keyCode KeyEvent.VK_ constant or KeyInput.KEY_W, etc.
         * @param action Lambda: () -> { your code }
         */
        public void addKeyListener(int keyCode, Runnable action) {
            addKeyInput(new KeyInput(keyCode, action));
        }
        
        /**
         * Convenience: Add WASD controls
         */
        public void addWASDControls(Runnable up, Runnable left, Runnable down, Runnable right) {
            if (up != null) addKeyListener(KeyInput.KEY_W, up);
            if (left != null) addKeyListener(KeyInput.KEY_A, left);
            if (down != null) addKeyListener(KeyInput.KEY_S, down);
            if (right != null) addKeyListener(KeyInput.KEY_D, right);
        }
        
        protected void setupKeyListeners() {
            // Remove existing listeners
            for (KeyListener listener : awtComponent.getKeyListeners()) {
                awtComponent.removeKeyListener(listener);
            }
            
            // Add new listener that checks all registered keys
            awtComponent.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    for (KeyInput ki : keyInputs) {
                        if (e.getKeyCode() == ki.getKeyCode()) {
                            ki.getAction().run();
                        }
                    }
                }
            });
            
            // Make sure component can receive key events
            awtComponent.setFocusable(true);
        }
        
        protected void setupMouseListeners() {
            // Remove existing listeners
            for (MouseListener listener : awtComponent.getMouseListeners()) {
                awtComponent.removeMouseListener(listener);
            }
            for (MouseMotionListener listener : awtComponent.getMouseMotionListeners()) {
                awtComponent.removeMouseMotionListener(listener);
            }
            for (MouseWheelListener listener : awtComponent.getMouseWheelListeners()) {
                awtComponent.removeMouseWheelListener(listener);
            }
            
            // Add new listener that checks all registered mouse events
            awtComponent.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    for (MouseInput mi : mouseInputs) {
                        if (mi.getType() == MouseInput.EventType.CLICK && mi.getAction() != null) {
                            mi.getAction().accept(e.getPoint());
                        }
                    }
                }
                
                @Override
                public void mousePressed(MouseEvent e) {
                    for (MouseInput mi : mouseInputs) {
                        if (mi.getType() == MouseInput.EventType.PRESS && mi.getAction() != null) {
                            mi.getAction().accept(e.getPoint());
                        }
                    }
                }
                
                @Override
                public void mouseReleased(MouseEvent e) {
                    for (MouseInput mi : mouseInputs) {
                        if (mi.getType() == MouseInput.EventType.RELEASE && mi.getAction() != null) {
                            mi.getAction().accept(e.getPoint());
                        }
                    }
                }
            });
            
            awtComponent.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    for (MouseInput mi : mouseInputs) {
                        if (mi.getType() == MouseInput.EventType.MOVE && mi.getAction() != null) {
                            mi.getAction().accept(e.getPoint());
                        }
                    }
                }
                
                @Override
                public void mouseDragged(MouseEvent e) {
                    for (MouseInput mi : mouseInputs) {
                        if (mi.getType() == MouseInput.EventType.DRAG && mi.getAction() != null) {
                            mi.getAction().accept(e.getPoint());
                        }
                    }
                }
            });
            
            awtComponent.addMouseWheelListener(e -> {
                for (MouseInput mi : mouseInputs) {
                    if (mi.getType() == MouseInput.EventType.WHEEL && mi.getWheelAction() != null) {
                        mi.getWheelAction().accept(e.getWheelRotation());
                    }
                }
            });
        }
    }
    
    // ==================== CUSTOM DRAWING SYSTEM ====================
    public static abstract class FDrawer {
        public abstract void draw(Graphics g);
    }
    
    // ==================== PANEL WITH DRAWING SUPPORT ====================
    public static class FPanel extends FComponent {
        private Panel panel;
        private static Map<Panel, FDrawer> drawerMap = new HashMap<>();
        
        public FPanel() {
            super(new Panel() {
                @Override
                public void paint(Graphics g) {
                    super.paint(g);
                    FDrawer drawer = drawerMap.get(this);
                    if (drawer != null && this.isVisible()) {
                        drawer.draw(g);
                    }
                }
            });
            
            this.panel = (Panel) awtComponent;
            panel.setLayout(new FlowLayout());
        }
        
        public void addDrawClass(FDrawer drawer) {
            drawerMap.put(panel, drawer);
            panel.repaint();
        }
        
        public void removeDrawer() {
            drawerMap.remove(panel);
            panel.repaint();
        }
        
        public void add(FComponent component) {
            panel.add(component.getAWTComponent());
        }
        
        public void setLayout(LayoutManager manager) {
            panel.setLayout(manager);
        }
        
        public void refresh() {
            panel.repaint();
        }
        
        /**
         * Convenience: Add key listener directly to panel
         * Example: panel.addListener(new KeyInput(KeyInput.KEY_W, () -> moveUp()))
         */
        public void addListener(KeyInput keyInput) {
            addKeyInput(keyInput);
        }
        
        /**
         * Convenience: Add mouse listener directly to panel
         */
        public void addListener(MouseInput mouseInput) {
            addMouseInput(mouseInput);
        }
    }
    
    // ==================== MAIN WINDOW ====================
    public static class FFrame extends FComponent {
        private Frame frame;
        
        public FFrame(String title) {
            super(new Frame(title));
            this.frame = (Frame) awtComponent;
            this.frame.setLayout(new FlowLayout());
            
            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    frame.dispose();
                    System.exit(0);
                }
            });
        }
        
        public void add(FComponent component) {
            frame.add(component.getAWTComponent());
        }
        
        public void showFrame() {
            frame.pack();
            frame.setVisible(true);
        }
        
        public void setSize(int width, int height) {
            frame.setSize(width, height);
        }
        
        public Frame getFrame() {
            return frame;
        }
    }
    
    // ==================== BUTTON ====================
    public static class FButton extends FComponent {
        private Button button;
        
        public FButton(String text) {
            super(new Button(text));
            this.button = (Button) awtComponent;
        }
        
        public void setOnClick(ActionListener listener) {
            button.addActionListener(listener);
        }
        
        // Also support lambda style
        public void setOnClick(Runnable action) {
            button.addActionListener(e -> action.run());
        }
        
        public String getText() {
            return button.getLabel();
        }
        
        public void setText(String text) {
            button.setLabel(text);
        }
    }
    
    // ==================== LABEL ====================
    public static class FLabel extends FComponent {
        private Label label;
        
        public FLabel() { this(""); }
        public FLabel(String text) {
            super(new Label(text));
            this.label = (Label) awtComponent;
        }
        public FLabel(String text, int alignment) {
            super(new Label(text, alignment));
            this.label = (Label) awtComponent;
        }
        
        public String getText() { return label.getText(); }
        public void setText(String text) { label.setText(text); }
        public void setAlignment(int alignment) { label.setAlignment(alignment); }
        
        public static final int LEFT = Label.LEFT;
        public static final int CENTER = Label.CENTER;
        public static final int RIGHT = Label.RIGHT;
    }
    
    // ==================== TEXT FIELD ====================
    public static class FTextField extends FComponent {
        private TextField textField;
        
        public FTextField() { this(20); }
        public FTextField(int columns) {
            super(new TextField(columns));
            this.textField = (TextField) awtComponent;
        }
        
        public String getText() { return textField.getText(); }
        public void setText(String text) { textField.setText(text); }
        
        public void setOnEnter(ActionListener listener) {
            textField.addActionListener(listener);
        }
        
        public void setOnEnter(Runnable action) {
            textField.addActionListener(e -> action.run());
        }
    }
    
    // ==================== TEXT EDITOR ====================
    public static class FTextEditor extends FComponent {
        private TextArea textArea;
        
        public FTextEditor() { this(10, 40); }
        public FTextEditor(int rows, int columns) {
            super(new TextArea(rows, columns));
            this.textArea = (TextArea) awtComponent;
        }
        
        public String getText() { return textArea.getText(); }
        public void setText(String text) { textArea.setText(text); }
        public void append(String text) { textArea.append(text); }
        public void clear() { textArea.setText(""); }
    }
    
    // ==================== MENU SYSTEM ====================
    public static class FMenuBar {
        private MenuBar menuBar;
        public FMenuBar() { menuBar = new MenuBar(); }
        public void add(FMenu menu) { menuBar.add(menu.getAWTMenu()); }
        public MenuBar getAWTMenuBar() { return menuBar; }
    }
    
    public static class FMenu {
        private Menu menu;
        public FMenu(String label) { menu = new Menu(label); }
        public void add(FMenuItem item) { menu.add(item.getAWTMenuItem()); }
        public Menu getAWTMenu() { return menu; }
    }
    
    public static class FMenuItem {
        private MenuItem menuItem;
        public FMenuItem(String label) { menuItem = new MenuItem(label); }
        
        public void setOnClick(ActionListener listener) {
            menuItem.addActionListener(listener);
        }
        
        public void setOnClick(Runnable action) {
            menuItem.addActionListener(e -> action.run());
        }
        
        public MenuItem getAWTMenuItem() { return menuItem; }
    }
    
    // ==================== UTILITY METHOD ====================
    public static void showMessageDialog(String title, String message) {
        Frame dialogFrame = new Frame(title);
        Dialog dialog = new Dialog(dialogFrame, title, true);
        dialog.setLayout(new FlowLayout());
        dialog.add(new Label(message));
        
        Button okButton = new Button("OK");
        okButton.addActionListener(e -> dialog.dispose());
        dialog.add(okButton);
        
        dialog.setSize(300, 100);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}