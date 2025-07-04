/**
 * RestaurantAppGUI.java
 *
 * A modern, dark-themed GUI application for managing restaurant operations.
 * This application provides a comprehensive interface for managing employees,
 * menu items, payroll calculations, and restaurant hours.
 *
 * Features:
 * - Modern dark theme with gradient backgrounds
 * - Animated UI components with hover effects
 * - Employee management (add, remove, update, display)
 * - Menu management (add, remove, update, display)
 * - Payroll calculation functionality
 * - Restaurant hours toggle (open late/close early)
 * - File loading capabilities for data import
 * - Custom styled components with rounded corners
 * - Smooth animations and transitions
 *
 * @author Restaurant Management Team
 * @version 1.0
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

/**
 * Main GUI class for the Restaurant Management Application.
 * Extends JFrame to create a desktop application with modern styling.
 *
 * This class handles all UI interactions and delegates business logic
 * to the Restaurant and RestaurantApp classes.
 */
public class RestaurantAppGUI extends JFrame {

    // Core business logic components
    private Restaurant restaurant;           // The restaurant data model
    private JTextArea outputArea;           // Main display area for information
    private RestaurantApp restaurantApp;    // Application logic handler
    private JPanel mainContent;            // Main content panel container
    private Timer animationTimer;          // Timer for background animations

    // Modern color scheme constants for consistent theming
    // Primary colors - darkest shades for main backgrounds
    private static final Color PRIMARY_DARK = new Color(26, 32, 46);     // Deep navy blue
    private static final Color SECONDARY_DARK = new Color(45, 55, 72);   // Lighter navy for secondary elements

    // Accent colors - bright colors for interactive elements and status indicators
    private static final Color ACCENT_BLUE = new Color(66, 153, 225);    // Bright blue for primary actions
    private static final Color ACCENT_GREEN = new Color(72, 187, 120);   // Green for success/positive actions
    private static final Color ACCENT_ORANGE = new Color(237, 137, 54);  // Orange for warning/neutral actions
    private static final Color ACCENT_RED = new Color(245, 101, 101);    // Red for destructive/negative actions

    // Text colors for proper contrast and readability
    private static final Color TEXT_PRIMARY = new Color(237, 242, 247);  // Light gray for primary text
    private static final Color TEXT_SECONDARY = new Color(160, 174, 192); // Medium gray for secondary text

    // Component background colors
    private static final Color CARD_BG = new Color(74, 85, 104);         // Background for card-like components

    /**
     * Constructor for RestaurantAppGUI.
     * Initializes the restaurant instance, creates the RestaurantApp handler,
     * sets up the modern UI, and makes the window visible.
     */
    public RestaurantAppGUI() {
        // Initialize the core restaurant business object with default name
        restaurant = new Restaurant("Emery's");

        // Create the application logic handler that manages restaurant operations
        restaurantApp = new RestaurantApp(restaurant);

        // Setup the modern user interface with all components
        setupModernUI();

        // Make the window visible to the user
        setVisible(true);
    }

    /**
     * Sets up the modern user interface with all components.
     * Creates the main window, applies styling, and initializes all UI elements.
     * This method orchestrates the creation of header, main content, and control panels.
     */
    private void setupModernUI() {
        // Configure main window properties
        setTitle("🍽️ Restaurant Manager Pro");        // Window title with emoji
        setSize(1200, 800);                          // Set window dimensions
        setDefaultCloseOperation(EXIT_ON_CLOSE);     // Exit application when window closes
        setLocationRelativeTo(null);                 // Center window on screen

        // Create custom content pane with gradient background
        // Using custom GradientPanel for visual appeal
        JPanel contentPane = new GradientPanel();
        setContentPane(contentPane);

        // Set up layout with proper spacing between components
        contentPane.setLayout(new BorderLayout(20, 20));
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20)); // Add padding around edges

        // Create and add the main UI sections
        createHeader(contentPane);        // Top section with title and status
        createMainContent(contentPane);   // Center section with output display
        createControlPanel(contentPane);  // Left section with action buttons

        // Initialize subtle background animations for modern feel
        startBackgroundAnimations();
    }

    /**
     * Creates the header section of the application.
     * Contains the main title and status indicator.
     *
     * @param parent The parent container to add the header to
     */
    private void createHeader(JPanel parent) {
        // Create header panel with secondary dark background
        JPanel header = new ModernPanel(SECONDARY_DARK);
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(20, 30, 20, 30)); // Generous padding

        // Create main title label with emoji and modern font
        JLabel titleLabel = new JLabel("🍽️ Restaurant Manager Pro", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Large, bold font
        titleLabel.setForeground(TEXT_PRIMARY); // Light text for dark background

        // Create status indicator panel (right-aligned)
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusPanel.setOpaque(false); // Transparent background

        // Status label showing online status with green dot
        JLabel statusLabel = new JLabel("● ONLINE", SwingConstants.RIGHT);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setForeground(ACCENT_GREEN); // Green color for positive status
        statusPanel.add(statusLabel);

        // Add components to header layout
        header.add(titleLabel, BorderLayout.CENTER);   // Title in center
        header.add(statusPanel, BorderLayout.EAST);    // Status on right

        // Add header to parent container at top
        parent.add(header, BorderLayout.NORTH);
    }

    /**
     * Creates the main content area containing the output display.
     * This is where all application feedback and information is shown to the user.
     *
     * @param parent The parent container to add the main content to
     */
    private void createMainContent(JPanel parent) {
        // Create main content container with proper spacing
        mainContent = new JPanel(new BorderLayout(15, 15));
        mainContent.setOpaque(false); // Transparent to show gradient background

        // Create output text area with modern styling
        outputArea = new JTextArea();
        outputArea.setEditable(false);                                    // Read-only display
        outputArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));  // Monospace font for better readability
        outputArea.setBackground(PRIMARY_DARK);                          // Dark background
        outputArea.setForeground(TEXT_PRIMARY);                          // Light text
        outputArea.setBorder(new EmptyBorder(20, 20, 20, 20));          // Internal padding
        outputArea.setLineWrap(true);                                    // Wrap long lines
        outputArea.setWrapStyleWord(true);                               // Wrap at word boundaries

        // Create custom scroll pane for the output area
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());     // Remove default border
        scrollPane.getViewport().setBackground(PRIMARY_DARK);        // Match text area background
        scrollPane.setBackground(PRIMARY_DARK);

        // Apply custom modern styling to scrollbars
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new ModernScrollBarUI());

        // Wrap scroll pane in a modern panel for rounded corners
        JPanel outputPanel = new ModernPanel(PRIMARY_DARK);
        outputPanel.setLayout(new BorderLayout());
        outputPanel.add(scrollPane, BorderLayout.CENTER);

        // Add output panel to main content
        mainContent.add(outputPanel, BorderLayout.CENTER);

        // Add main content to parent container in center
        parent.add(mainContent, BorderLayout.CENTER);

        // Display welcome message to user
        outputArea.append("🎉 Welcome to Restaurant Manager Pro!\n");
        outputArea.append("═══════════════════════════════════════\n");
        outputArea.append("Ready to manage your restaurant efficiently.\n\n");
    }

    /**
     * Creates the control panel containing all action buttons.
     * Arranged in a grid layout with modern styled buttons for each operation.
     *
     * @param parent The parent container to add the control panel to
     */
    private void createControlPanel(JPanel parent) {
        // Create control panel with grid layout for organized button arrangement
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(4, 2, 15, 15)); // 4 rows, 2 columns, 15px spacing
        controlPanel.setOpaque(false); // Transparent background
        controlPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Padding around buttons

        // Create modern buttons with icons, colors, and action handlers
        // Each button has a specific color scheme based on its function
        addModernButton(controlPanel, "📁 Load Data", ACCENT_BLUE, e -> loadFile());
        addModernButton(controlPanel, "📋 Display All", ACCENT_GREEN, e -> displayAll());
        addModernButton(controlPanel, "➕ Add Record", ACCENT_ORANGE, e -> addRecord());
        addModernButton(controlPanel, "🗑️ Remove Record", ACCENT_RED, e -> removeRecord());
        addModernButton(controlPanel, "✏️ Update Record", ACCENT_BLUE, e -> updateRecord());
        addModernButton(controlPanel, "💰 Calculate Payroll", ACCENT_GREEN, e -> calculatePayroll());
        addModernButton(controlPanel, "🌙 Toggle Hours", ACCENT_ORANGE, e -> toggleOpenLate());
        addModernButton(controlPanel, "🚪 Exit", ACCENT_RED, e -> exitApplication());

        // Wrap control panel in modern styled container
        JPanel controlWrapper = new ModernPanel(SECONDARY_DARK);
        controlWrapper.setLayout(new BorderLayout());
        controlWrapper.setBorder(new EmptyBorder(20, 20, 20, 20)); // Generous padding
        controlWrapper.add(controlPanel, BorderLayout.CENTER);

        // Add control wrapper to parent container on the left side
        parent.add(controlWrapper, BorderLayout.WEST);
    }

    /**
     * Helper method to create and add a modern styled button to the control panel.
     *
     * @param panel The panel to add the button to
     * @param text The text/emoji to display on the button
     * @param accentColor The accent color for hover effects and styling
     * @param action The action listener to execute when button is clicked
     */
    private void addModernButton(JPanel panel, String text, Color accentColor, ActionListener action) {
        // Create custom modern button with specified styling
        ModernButton button = new ModernButton(text, accentColor);
        button.addActionListener(action); // Attach the action handler
        panel.add(button); // Add to the panel
    }

    /**
     * Custom Modern Button Class
     *
     * Extends JButton to provide modern styling with hover animations,
     * rounded corners, and color transitions. Each button has smooth
     * hover effects and professional appearance.
     */
    private class ModernButton extends JButton {
        private Color accentColor;        // The accent color for this button
        private boolean isHovered = false; // Track hover state
        private Timer hoverTimer;         // Timer for smooth hover animations
        private float hoverProgress = 0f; // Progress of hover animation (0.0 to 1.0)

        /**
         * Constructor for ModernButton.
         *
         * @param text The text to display on the button
         * @param accentColor The accent color for hover effects
         */
        public ModernButton(String text, Color accentColor) {
            super(text);
            this.accentColor = accentColor;

            // Set button dimensions and basic styling
            setPreferredSize(new Dimension(180, 50));
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setForeground(TEXT_PRIMARY);                    // Light text
            setBackground(CARD_BG);                         // Default background
            setBorder(BorderFactory.createEmptyBorder());   // No border
            setFocusPainted(false);                         // No focus rectangle
            setContentAreaFilled(false);                    // Custom painting
            setCursor(new Cursor(Cursor.HAND_CURSOR));      // Hand cursor on hover

            // Initialize hover animation system
            setupHoverAnimation();
        }

        /**
         * Sets up the hover animation system for smooth color transitions.
         * Creates a timer that gradually changes the hover progress value
         * to create smooth animations when mouse enters/exits the button.
         */
        private void setupHoverAnimation() {
            // Create animation timer that runs at ~60fps (16ms intervals)
            hoverTimer = new Timer(16, e -> {
                // Animate hover progress based on current hover state
                if (isHovered && hoverProgress < 1f) {
                    // Mouse is hovering, increase progress towards 1.0
                    hoverProgress = Math.min(1f, hoverProgress + 0.1f);
                    repaint(); // Trigger visual update
                } else if (!isHovered && hoverProgress > 0f) {
                    // Mouse is not hovering, decrease progress towards 0.0
                    hoverProgress = Math.max(0f, hoverProgress - 0.1f);
                    repaint(); // Trigger visual update
                }

                // Stop timer when animation is complete
                if ((isHovered && hoverProgress >= 1f) || (!isHovered && hoverProgress <= 0f)) {
                    hoverTimer.stop();
                }
            });

            // Add mouse listeners to detect hover state changes
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    if (!hoverTimer.isRunning()) hoverTimer.start();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    if (!hoverTimer.isRunning()) hoverTimer.start();
                }
            });
        }

        /**
         * Custom paint method for the button.
         * Creates rounded corners, hover effects, and border glow.
         *
         * @param g The Graphics object to paint with
         */
        @Override
        protected void paintComponent(Graphics g) {
            // Create Graphics2D for advanced rendering features
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Calculate background color based on hover progress
            // Blends from default background to accent color
            Color bgColor = interpolateColor(CARD_BG, accentColor, hoverProgress * 0.3f);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12); // Rounded rectangle

            // Draw border glow effect during hover
            if (hoverProgress > 0) {
                // Create semi-transparent accent color for glow
                g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(),
                        (int)(100 * hoverProgress)));
                g2.setStroke(new BasicStroke(2f)); // 2px stroke width
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 12, 12); // Rounded border
            }

            g2.dispose();
            super.paintComponent(g); // Paint the button text
        }
    }

    /**
     * Custom Panel with rounded corners and solid background color.
     * Used throughout the application for consistent modern styling.
     */
    private class ModernPanel extends JPanel {
        private Color backgroundColor; // The background color for this panel

        /**
         * Constructor for ModernPanel.
         *
         * @param backgroundColor The background color to use
         */
        public ModernPanel(Color backgroundColor) {
            this.backgroundColor = backgroundColor;
            setOpaque(false); // Allow custom painting
        }

        /**
         * Custom paint method for rounded corners.
         *
         * @param g The Graphics object to paint with
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Fill rounded rectangle with background color
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Gradient background panel for the main window.
     * Creates a diagonal gradient from primary to secondary dark colors.
     */
    private class GradientPanel extends JPanel {
        /**
         * Custom paint method for gradient background.
         *
         * @param g The Graphics object to paint with
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Create diagonal gradient from top-left to bottom-right
            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(26, 32, 46),          // Start color (top-left)
                    getWidth(), getHeight(), new Color(45, 55, 72) // End color (bottom-right)
            );
            g2.setPaint(gradient);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.dispose();
        }
    }

    /**
     * Modern ScrollBar UI implementation.
     * Provides styled scrollbars that match the application's modern theme.
     */
    private class ModernScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        /**
         * Configure scrollbar colors to match the modern theme.
         */
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = ACCENT_BLUE;  // Blue thumb (draggable part)
            this.trackColor = PRIMARY_DARK; // Dark track background
        }

        /**
         * Create invisible decrease button (removes default arrow buttons).
         *
         * @param orientation The scrollbar orientation
         * @return A zero-sized button
         */
        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        /**
         * Create invisible increase button (removes default arrow buttons).
         *
         * @param orientation The scrollbar orientation
         * @return A zero-sized button
         */
        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        /**
         * Helper method to create a zero-sized button.
         * Used to remove default scrollbar arrow buttons.
         *
         * @return A button with zero dimensions
         */
        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            return button;
        }

        /**
         * Custom paint method for the scrollbar thumb.
         * Creates rounded corners and proper styling.
         *
         * @param g The Graphics object to paint with
         * @param c The component being painted
         * @param thumbBounds The bounds of the thumb area
         */
        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            // Draw rounded rectangle with padding for better appearance
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2,
                    thumbBounds.width - 4, thumbBounds.height - 4, 8, 8);
            g2.dispose();
        }
    }

    /**
     * Utility method for color interpolation between two colors.
     * Used for smooth color transitions in animations.
     *
     * @param c1 The starting color
     * @param c2 The ending color
     * @param fraction The interpolation fraction (0.0 to 1.0)
     * @return The interpolated color
     */
    private Color interpolateColor(Color c1, Color c2, float fraction) {
        // Calculate RGB values based on interpolation fraction
        int red = (int) (c1.getRed() + fraction * (c2.getRed() - c1.getRed()));
        int green = (int) (c1.getGreen() + fraction * (c2.getGreen() - c1.getGreen()));
        int blue = (int) (c1.getBlue() + fraction * (c2.getBlue() - c1.getBlue()));
        return new Color(red, green, blue);
    }

    /**
     * Starts background animations for the application.
     * Currently set up for future enhancements - could include
     * subtle background effects or periodic UI updates.
     */
    private void startBackgroundAnimations() {
        // Create animation timer for potential background effects
        animationTimer = new Timer(50, e -> {
            // Placeholder for subtle background animations
            // Could include particle effects, color shifts, etc.
            repaint();
        });
        animationTimer.start();
    }

    /**
     * Shows a modern styled dialog box with custom content.
     * Provides OK and Cancel buttons with the application's modern styling.
     *
     * @param title The dialog title
     * @param content The content panel to display
     * @param onOk The action to execute when OK is clicked
     */
    private void showModernDialog(String title, JPanel content, Runnable onOk) {
        // Create modal dialog
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this); // Center on parent window
        dialog.setResizable(false);

        // Create main dialog panel with gradient background
        JPanel dialogPanel = new GradientPanel();
        dialogPanel.setLayout(new BorderLayout(20, 20));
        dialogPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Wrap content in modern styled panel
        JPanel contentWrapper = new ModernPanel(SECONDARY_DARK);
        contentWrapper.setLayout(new BorderLayout());
        contentWrapper.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentWrapper.add(content, BorderLayout.CENTER);

        // Create button panel with OK and Cancel buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        // Create styled OK and Cancel buttons
        ModernButton okButton = new ModernButton("✓ OK", ACCENT_GREEN);
        ModernButton cancelButton = new ModernButton("✗ Cancel", ACCENT_RED);

        // Set button dimensions for proper visibility
        okButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.setPreferredSize(new Dimension(100, 40));

        // Add action listeners
        okButton.addActionListener(e -> {
            onOk.run();        // Execute the OK action
            dialog.dispose();  // Close dialog
        });

        cancelButton.addActionListener(e -> dialog.dispose()); // Just close dialog

        // Add buttons to panel
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // Assemble dialog layout
        dialogPanel.add(contentWrapper, BorderLayout.CENTER);
        dialogPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Set dialog content and show
        dialog.setContentPane(dialogPanel);
        dialog.setVisible(true);
    }

    /**
     * Handles the exit application functionality.
     * Shows a confirmation dialog before closing the application.
     * Provides a graceful exit with a farewell message.
     */
    private void exitApplication() {
        // Show confirmation dialog
        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit Restaurant Manager Pro?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        // If user confirms exit
        if (result == JOptionPane.YES_OPTION) {
            // Display farewell message
            outputArea.append("👋 Thank you for using Restaurant Manager Pro!\n");

            // Create timer for delayed exit (allows user to see message)
            Timer exitTimer = new Timer(1000, e -> System.exit(0));
            exitTimer.setRepeats(false); // Only run once
            exitTimer.start();
        }
    }

    /**
     * Handles file loading functionality.
     * Opens a file chooser dialog for the user to select a data file,
     * then delegates to RestaurantApp for actual file processing.
     */
    private void loadFile() {
        // Create file chooser with styled background
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setBackground(SECONDARY_DARK);

        // Show open dialog
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            // Get selected file path
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            // Delegate file reading to RestaurantApp
            String message = restaurantApp.readDataFromFile(filePath);

            // Display result with file icon
            outputArea.append("📁 " + message + "\n");
        } else {
            // User canceled file selection
            outputArea.append("❌ File loading canceled.\n");
        }
    }

    /**
     * Displays all restaurant data in the output area.
     * Shows restaurant name, hours status, employee list, and menu items.
     * Provides a comprehensive overview of the restaurant's current state.
     */
    private void displayAll() {
        // Clear output area for fresh display
        outputArea.setText("");

        // Display restaurant header information
        outputArea.append("🏪 Restaurant: " + restaurant.getName() + "\n");
        outputArea.append("⏰ Status: " + (restaurant.isOpenLate() ? "Open Late ✅" : "Closes Early ❌") + "\n");
        outputArea.append("═══════════════════════════════════════\n");

        // Display employee information
        List<String> employeeLines = restaurant.getEmployeeDisplayStrings();
        if (employeeLines.isEmpty()) {
            outputArea.append("👤 No employees to display.\n");
        } else {
            outputArea.append("👥 EMPLOYEES:\n");
            for (String line : employeeLines) {
                outputArea.append("  " + line + "\n");
            }
        }

        outputArea.append("═══════════════════════════════════════\n");

        // Display menu information
        List<String> menuLines = restaurant.getMenuDisplayStrings();
        if (menuLines.isEmpty()) {
            outputArea.append("🍽️ Menu is empty.\n");
        } else {
            outputArea.append("🍔 MENU:\n");
            for (String line : menuLines) {
                outputArea.append("  " + line + "\n");
            }
        }
        outputArea.append("═══════════════════════════════════════\n");
    }

    /**
     * Handles adding new records (employees or dishes).
     * Shows a dialog for the user to choose between adding an employee or dish,
     * then delegates to the appropriate specific method.
     */
    private void addRecord() {
        // Create options for record type selection
        String[] options = {"👤 Employee", "🍽️ Dish"};

        // Show selection dialog
        int choice = JOptionPane.showOptionDialog(
                this,
                "What type of record would you like to add?",
                "Add Record",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        // Handle user choice
        if (choice == 0) {
            showAddEmployeeDialog();
        } else if (choice == 1) {
            showAddDishDialog();
        }
    }

    /**
     * Shows the dialog for adding a new employee.
     * Collects employee name, hourly rate, and hours worked,
     * then adds the employee to the restaurant.
     */
    private void showAddEmployeeDialog() {
        // Create form panel with grid layout
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setOpaque(false);

        // Create styled input fields
        JTextField nameField = createStyledTextField();
        JTextField rateField = createStyledTextField();
        JTextField hoursField = createStyledTextField();

        // Add labels and fields to panel
        panel.add(createStyledLabel("Name:"));
        panel.add(nameField);
        panel.add(createStyledLabel("Hourly Rate:"));
        panel.add(rateField);
        panel.add(createStyledLabel("Hours Worked:"));
        panel.add(hoursField);

        // Show dialog with form content
        showModernDialog("Add Employee", panel, () -> {
            // Get user input
            String name = nameField.getText().trim();
            String rateText = rateField.getText().trim();
            String hoursText = hoursField.getText().trim();

            try {
                // Parse numeric values
                double rate = Double.parseDouble(rateText);
                double hours = Double.parseDouble(hoursText);

                // Attempt to add employee
                String error = restaurant.addEmployee(name, rate, hours);

                // Display result
                if (error == null) {
                    outputArea.append("✅ Employee added: " + name + "\n");
                } else {
                    outputArea.append("❌ " + error + "\n");
                }
            } catch (NumberFormatException e) {
                // Handle invalid number input
                outputArea.append("❌ Please enter valid numbers for rate and hours.\n");
            }
        });
    }

    /**
     * Shows the dialog for adding a new dish.
     * Collects dish name and price, then adds the dish to the menu.
     */
    private void showAddDishDialog() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setOpaque(false);

        JTextField nameField = createStyledTextField();
        JTextField priceField = createStyledTextField();

        panel.add(createStyledLabel("Dish Name:"));
        panel.add(nameField);
        panel.add(createStyledLabel("Price:"));
        panel.add(priceField);

        showModernDialog("Add Dish", panel, () -> {
            String name = nameField.getText().trim();
            String priceText = priceField.getText().trim();

            try {
                double price = Double.parseDouble(priceText);
                String error = restaurant.addDish(name, price);

                if (error == null) {
                    outputArea.append("✅ Dish added: " + name + "\n");
                } else {
                    outputArea.append("❌ " + error + "\n");
                }
            } catch (NumberFormatException e) {
                outputArea.append("❌ Please enter a valid number for price.\n");
            }
        });
    }

    /**
     * Creates a styled text field for use in dialogs and forms.
     * Applies modern color, border, and font settings.
     *
     * @return a styled JTextField
     */
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setBackground(PRIMARY_DARK);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TEXT_PRIMARY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_BLUE, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return field;
    }

    /**
     * Creates a styled label for use in dialogs and forms.
     * Applies modern color and font settings.
     *
     * @param text The label text
     * @return a styled JLabel
     */
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_PRIMARY);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return label;
    }

    /**
     * Handles removing records (employees or dishes).
     * Prompts user for the type of record and delegates to removal dialogs.
     */
    private void removeRecord() {
        String[] options = {"👤 Employee", "🍽️ Dish"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "What type of record would you like to remove?",
                "Remove Record",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            removeEmployeeDialog();
        } else if (choice == 1) {
            removeDishDialog();
        }
    }

    /**
     * Shows dialog for removing an employee by name.
     * Handles feedback and success/failure reporting.
     */
    private void removeEmployeeDialog() {
        String name = JOptionPane.showInputDialog(this, "Enter employee name to remove:");
        if (name != null && !name.trim().isEmpty()) {
            String message = restaurant.removeEmployee(name.trim());
            outputArea.append((message.startsWith("Employee removed") ? "✅ " : "❌ ") + message + "\n");
        } else {
            outputArea.append("ℹ️ Removal canceled or empty name.\n");
        }
    }

    /**
     * Shows dialog for removing a dish by name.
     * Handles feedback and success/failure reporting.
     */
    private void removeDishDialog() {
        String name = JOptionPane.showInputDialog(this, "Enter dish name to remove:");
        if (name != null && !name.trim().isEmpty()) {
            String message = restaurant.removeDish(name.trim());
            outputArea.append((message.startsWith("Dish removed") ? "✅ " : "❌ ") + message + "\n");
        } else {
            outputArea.append("ℹ️ Removal canceled or empty name.\n");
        }
    }

    /**
     * Handles updating records (employees or dishes).
     * Prompts user for the type of record and delegates to update dialogs.
     */
    private void updateRecord() {
        String[] options = {"👤 Employee", "🍽️ Dish"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "What type of record would you like to update?",
                "Update Record",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            updateEmployeeDialog();
        } else if (choice == 1) {
            updateDishDialog();
        }
    }

    /**
     * Shows dialog for updating an employee's information.
     * Collects new hourly rate and hours worked.
     */
    private void updateEmployeeDialog() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setOpaque(false);

        JTextField nameField = createStyledTextField();
        JTextField rateField = createStyledTextField();
        JTextField hoursField = createStyledTextField();

        panel.add(createStyledLabel("Employee Name:"));
        panel.add(nameField);
        panel.add(createStyledLabel("New Hourly Rate:"));
        panel.add(rateField);
        panel.add(createStyledLabel("New Hours Worked:"));
        panel.add(hoursField);

        showModernDialog("Update Employee", panel, () -> {
            String name = nameField.getText().trim();
            String rateText = rateField.getText().trim();
            String hoursText = hoursField.getText().trim();

            try {
                double newRate = Double.parseDouble(rateText);
                double newHours = Double.parseDouble(hoursText);

                String message = restaurant.updateEmployee(name, newRate, newHours);
                outputArea.append((message.contains("successfully") ? "✅ " : "❌ ") + message + "\n");
            } catch (NumberFormatException e) {
                outputArea.append("❌ Please enter valid numbers for rate and hours.\n");
            }
        });
    }

    /**
     * Opens a dialog to update the price of an existing dish.
     * Builds a simple 2×2 form for entering the dish name and the new price.
     * On confirmation, parses and validates the inputs, calls the Restaurant
     * model’s updateDish(…) method, and logs the outcome (success or error)
     * to the main output area.
     */
    private void updateDishDialog() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setOpaque(false);

        JTextField nameField  = createStyledTextField();  // for the dish name
        JTextField priceField = createStyledTextField();  // for the new price

        panel.add(createStyledLabel("Dish Name:"));
        panel.add(nameField);
        panel.add(createStyledLabel("New Price:"));
        panel.add(priceField);

        showModernDialog("Update Dish", panel, () -> {
            String name      = nameField.getText().trim();
            String priceText = priceField.getText().trim();

            try {
                double newPrice = Double.parseDouble(priceText);
                String message = restaurant.updateDish(name, newPrice);
                outputArea.append((message.contains("successfully") ? "✅ " : "❌ ")
                        + message + "\n");
            } catch (NumberFormatException e) {
                outputArea.append("❌ Please enter a valid number for price.\n");
            }
        });
    }

    /**
     * Calculates and displays the total weekly payroll.
     * Queries the Restaurant model for the sum of all employees’
     * (hourlyRate × hoursWorked), formats it to two decimals, and
     * writes it to the output area.
     */
    private void calculatePayroll() {
        double total = restaurant.getTotalPayroll();
        outputArea.append("💰 Total Weekly Payroll: $"
                + String.format("%.2f", total) + "\n");
    }

    /**
     * Toggles the restaurant’s “open late” flag.
     * Reads the current boolean, flips it, updates the model,
     * and logs the new status with an icon to the output area.
     */
    private void toggleOpenLate() {
        boolean current = restaurant.isOpenLate();
        restaurant.setOpenLate(!current);
        outputArea.append("🌙 Open Late status changed to: "
                + (restaurant.isOpenLate() ? "✅ Open Late" : "❌ Closes Early")
                + "\n");
    }

    /**
     * Application entry point.
     * Sets the native system look-and-feel for consistency with
     * the user’s OS, then launches the GUI on the Swing Event
     * Dispatch Thread to ensure thread safety.
     */
    public static void main(String[] args) {
        try {
            // Apply system L&F (e.g., Windows Aero, macOS Aqua)
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception e) {
            e.printStackTrace(); // fallback to default if it fails
        }
        // Always create Swing GUIs on the EDT
        SwingUtilities.invokeLater(() -> new RestaurantAppGUI());
    }
}