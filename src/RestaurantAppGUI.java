// Import File class for file system operations and validation
import java.io.File;
// Import SQLException for database error handling
import java.sql.SQLException;
// Import all Swing components for GUI development
import javax.swing.*;
// Import EmptyBorder for component padding and spacing
import javax.swing.border.EmptyBorder;
// Import all AWT components for layout management and graphics
import java.awt.*;
// Import event handling classes for user interactions
import java.awt.event.*;
// Import RoundRectangle2D for creating rounded rectangular shapes
import java.awt.geom.RoundRectangle2D;
// Import List interface for working with collections of objects
import java.util.List;

/**
 * RestaurantAppGUI.java
 *
 * A modern, dark-themed GUI application for managing restaurant operations.
 * This application provides a comprehensive interface for managing employees,
 * menu items, payroll calculations, and restaurant hours.
 *
 * <p>Features include:
 * - Modern dark theme with gradient backgrounds
 * - Animated UI components with hover effects
 * - Employee management (add, remove, update, display)
 * - Menu management (add, remove, update, display)
 * - Payroll calculation functionality
 * - Restaurant hours toggle (open late/close early)
 * - File loading capabilities for data import
 * - Custom styled components with rounded corners
 * - Smooth animations and transitions
 * </p>
 *
 * @author Joel Maldonado
 */
public class RestaurantAppGUI extends JFrame {

    /** The main Restaurant model containing employee, dish, and status data */
    private Restaurant restaurant;

    /** Area to display feedback and output messages to the user */
    private JTextArea outputArea;

    /** Controller class that handles all logical operations for the restaurant */
    private RestaurantApp restaurantApp;

    /** The central panel container for displaying all dynamic content views */
    private JPanel mainContent;

    /** Animation handler for smooth GUI transitions */
    private Timer animationTimer;

    // ------------------- Color Palette for Theming -------------------

    /** Dark primary background color for the entire frame */
    private static final Color PRIMARY_DARK   = new Color(26, 32, 46);

    /** Secondary background color for inner components and content panels */
    private static final Color SECONDARY_DARK = new Color(45, 55, 72);

    /** Bright blue accent for primary interactive elements like buttons */
    private static final Color ACCENT_BLUE    = new Color(66, 153, 225);

    /** Green color used to indicate successful operations or states */
    private static final Color ACCENT_GREEN   = new Color(72, 187, 120);

    /** Orange accent for caution, warning, or secondary attention items */
    private static final Color ACCENT_ORANGE  = new Color(237, 137, 54);

    /** Red accent for critical errors, deletion, or alert buttons */
    private static final Color ACCENT_RED     = new Color(245, 101, 101);

    /** Main readable text color for headers and general content */
    private static final Color TEXT_PRIMARY   = new Color(237, 242, 247);

    /** Lighter text used for subtitles or supporting information */
    private static final Color TEXT_SECONDARY = new Color(160, 174, 192);

    /** Background color used for card-like UI elements and button bases */
    private static final Color CARD_BG        = new Color(74, 85, 104);

    /**
     * Constructs the RestaurantAppGUI.
     * <p>
     * Initializes the Restaurant data model, attaches it to the controller logic,
     * configures the custom GUI, and displays the window.
     * </p>
     */
    public RestaurantAppGUI() {
        // Create a new Restaurant instance with the name "Emery's"
        restaurant = new Restaurant("Emery's");

        // Initialize the business logic layer with the restaurant data
        restaurantApp = new RestaurantApp(restaurant);

        // Build and configure all GUI components
        setupModernUI();

        // Make the GUI visible
        setVisible(true);
    }


    /**
     * Loads employee and dish data from an SQLite database file
     * into the in-memory restaurant model.
     * <p>
     * Prompts the user for a file path, validates the file,
     * connects to the database using {@link DatabaseManager},
     * retrieves all employee and dish records, and populates
     * the restaurant object with this data.
     * </p>
     *
     * <p>
     * Provides user feedback through the GUI's output area, including
     * warnings for invalid paths, missing files, or non-.db extensions.
     * </p>
     */
    /**
     * Prompts the user to select an SQLite database file using a visual file picker,
     * then loads employee and dish records from the selected database into the restaurant system.
     * Displays detailed feedback in the output area regarding load success or failure.
     */
    private void loadDataFromDatabase() {
        // Create a file chooser dialog for selecting the .db file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select SQLite .db File");

        // Show the file chooser dialog and store user result
        int result = fileChooser.showOpenDialog(this); // 'this' = main GUI window

        // Check if user clicked cancel or closed the dialog
        if (result != JFileChooser.APPROVE_OPTION) {
            // Display cancellation message to user
            outputArea.append("❌ Load canceled.\n");
            return;
        }

        // Get the file the user selected
        File dbFile = fileChooser.getSelectedFile();
        String dbPath = dbFile.getAbsolutePath();

        // Validate file extension and warn if needed
        if (!dbPath.toLowerCase().endsWith(".db")) {
            outputArea.append("⚠️ Warning: \"" + dbPath + "\" does not end with \".db\". Proceeding anyway…\n");
        }

        // Verify that the selected file actually exists
        if (!dbFile.exists() || !dbFile.isFile()) {
            outputArea.append("❌ File not found: " + dbPath + "\n");
            return;
        }

        // Try-catch block for database operations that might fail
        try {
            // Connect to the database using the selected file path
            DatabaseManager.connect(dbPath);

            // Fetch records from the database
            var employees = DatabaseManager.getAllEmployees(); // Fetch employee list
            var dishes    = DatabaseManager.getAllDishes();    // Fetch dish list

            // Re-sync in‑memory model by clearing current data
            restaurant.clearAll();

            // Add all employees and dishes from the database to the restaurant
            employees.forEach(restaurant::addEmployee);
            dishes.forEach(restaurant::addDish);

            // Display success message with number of records loaded
            outputArea.append(String.format(
                    "📁 ✅ Load complete: %d employees, %d dishes.%n",
                    employees.size(), dishes.size()
            ));
        } catch (Exception ex) {
            // Display error message in case of exception
            outputArea.append("❌ Load failed: " + ex.getMessage() + "\n");
            ex.printStackTrace();
        }
    }


    /**
     * Sets up the modern UI
     */
    // Method to configure and initialize the entire user interface
    private void setupModernUI() {
        // Set the main window title with emoji for visual appeal
        setTitle("🍽️ Restaurant Manager Pro");
        // Set the initial window size (width, height)
        setSize(1200, 800);
        // Set application to exit when window is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Center the window on the screen
        setLocationRelativeTo(null);

        // Create main content panel with gradient background
        JPanel contentPane = new GradientPanel();
        // Set this panel as the main content pane
        setContentPane(contentPane);
        // Set border layout with spacing between components
        contentPane.setLayout(new BorderLayout(20, 20));
        // Add padding around the entire content area
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Create and add the header section
        createHeader(contentPane);
        // Create and add the main content display area
        createMainContent(contentPane);
        // Create and add the control panel with buttons
        createControlPanel(contentPane);
        // Start background animations for smooth UI effects
        startBackgroundAnimations();
    }

    /** Header with title and status */
    // Method to create the top header section of the application
    private void createHeader(JPanel parent) {
        // Create header panel with modern styling
        JPanel header = new ModernPanel(SECONDARY_DARK);
        // Use border layout for header organization
        header.setLayout(new BorderLayout());
        // Add padding inside the header
        header.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Create main title label with emoji and center alignment
        JLabel titleLabel = new JLabel("🍽️ Restaurant Manager Pro", SwingConstants.CENTER);
        // Set large, bold font for the title
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        // Set text color to primary text color
        titleLabel.setForeground(TEXT_PRIMARY);

        // Create panel for status information on the right side
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        // Make status panel background transparent
        statusPanel.setOpaque(false);
        // Create status label indicating system is online
        JLabel statusLabel = new JLabel("● ONLINE", SwingConstants.RIGHT);
        // Set smaller bold font for status
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        // Set green color to indicate positive status
        statusLabel.setForeground(ACCENT_GREEN);
        // Add status label to status panel
        statusPanel.add(statusLabel);

        // Add title to center of header
        header.add(titleLabel, BorderLayout.CENTER);
        // Add status panel to right side of header
        header.add(statusPanel, BorderLayout.EAST);
        // Add complete header to top of parent container
        parent.add(header, BorderLayout.NORTH);
    }

    /** Main output display area */
    // Method to create the central content area for displaying information
    private void createMainContent(JPanel parent) {
        // Create main content panel with border layout and spacing
        mainContent = new JPanel(new BorderLayout(15, 15));
        // Make background transparent to show gradient
        mainContent.setOpaque(false);

        // Create text area for displaying output messages
        outputArea = new JTextArea();
        // Prevent user from editing the output area
        outputArea.setEditable(false);
        // Set monospace font for consistent text formatting
        outputArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        // Set dark background color
        outputArea.setBackground(PRIMARY_DARK);
        // Set light text color for contrast
        outputArea.setForeground(TEXT_PRIMARY);
        // Add padding inside the text area
        outputArea.setBorder(new EmptyBorder(20, 20, 20, 20));
        // Enable word wrapping for long lines
        outputArea.setLineWrap(true);
        // Wrap at word boundaries for better readability
        outputArea.setWrapStyleWord(true);

        // Create scroll pane to contain the text area
        JScrollPane scrollPane = new JScrollPane(outputArea);
        // Remove default border from scroll pane
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        // Set viewport background to match text area
        scrollPane.getViewport().setBackground(PRIMARY_DARK);
        // Set scroll pane background color
        scrollPane.setBackground(PRIMARY_DARK);
        // Apply custom styling to vertical scrollbar
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        // Apply custom styling to horizontal scrollbar
        scrollPane.getHorizontalScrollBar().setUI(new ModernScrollBarUI());

        // Create panel to wrap the scroll pane with modern styling
        JPanel outputPanel = new ModernPanel(PRIMARY_DARK);
        // Use border layout for the output panel
        outputPanel.setLayout(new BorderLayout());
        // Add scroll pane to center of output panel
        outputPanel.add(scrollPane, BorderLayout.CENTER);

        // Add output panel to center of main content
        mainContent.add(outputPanel, BorderLayout.CENTER);
        // Add main content to center of parent container
        parent.add(mainContent, BorderLayout.CENTER);

        // Display welcome message to user
        outputArea.append("🎉 Welcome to Restaurant Manager Pro!\n");
        // Add visual separator line
        outputArea.append("═══════════════════════════════════════\n");
        // Display ready message
        outputArea.append("Ready to manage your restaurant efficiently.\n\n");
    }

    /** Control panel with all action buttons */
    // Method to create the side panel containing all action buttons
    private void createControlPanel(JPanel parent) {
        // Create grid layout for buttons (4 rows, 2 columns with spacing)
        JPanel controlPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        // Make background transparent
        controlPanel.setOpaque(false);
        // Add padding around the control panel
        controlPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Add load data button with blue accent color
        addModernButton(controlPanel, "📁 Load Data", ACCENT_BLUE, e -> loadDataFromDatabase());
        // Add display all button with green accent color
        addModernButton(controlPanel, "📋 Display All", ACCENT_GREEN, e -> displayAll());
        // Add record addition button with orange accent color
        addModernButton(controlPanel, "➕ Add Record", ACCENT_ORANGE, e -> addRecord());
        // Add record removal button with red accent color
        addModernButton(controlPanel, "🔴 Remove Record", ACCENT_RED,    e -> removeRecord());
        // Add record update button with blue accent color
        addModernButton(controlPanel, "📝 Update Record", ACCENT_BLUE, e -> updateRecord());
        // Add payroll calculation button with green accent color
        addModernButton(controlPanel, "💰 Calculate Payroll", ACCENT_GREEN, e -> calculatePayroll());
        // Add hours toggle button with orange accent color
        addModernButton(controlPanel, "🌙 Toggle Hours", ACCENT_ORANGE, e -> toggleOpenLate());
        // Add exit button with red accent color
        addModernButton(controlPanel, "🚪 Exit", ACCENT_RED,             e -> exitApplication());

        // Create wrapper panel with modern styling for the control panel
        JPanel wrapper = new ModernPanel(SECONDARY_DARK);
        // Use border layout for wrapper
        wrapper.setLayout(new BorderLayout());
        // Add padding inside the wrapper
        wrapper.setBorder(new EmptyBorder(20, 20, 20, 20));
        // Add control panel to center of wrapper
        wrapper.add(controlPanel, BorderLayout.CENTER);
        // Add wrapper to left side of parent container
        parent.add(wrapper, BorderLayout.WEST);
    }

    /** Button factory */
    // Helper method to create and add modern styled buttons
    private void addModernButton(JPanel panel, String text, Color accent, ActionListener a) {
        // Create new modern button with specified text and accent color
        ModernButton b = new ModernButton(text, accent);
        // Add action listener to handle button clicks
        b.addActionListener(a);
        // Add button to the specified panel
        panel.add(b);
    }

    /** -------------------- NEW CRUD METHODS -------------------- **/

    /**
     * Adds a new Employee or Dish by invoking DatabaseManager,
     * then re-syncs the in-memory model.
     */
    // Method to add new records (employees or dishes) to the database
    private void addRecord() {
        // Check if database connection exists before proceeding
        if (DatabaseManager.getConnection() == null) {
            // Display warning if no database connection
            outputArea.append("⚠️ Please Load Data (connect to a database) before adding records.\n");
            // Exit method if no connection
            return;
        }
        // Define available record types for selection
        String[] types = {"Employee", "Dish"};
        // Show option dialog for user to select record type
        int choice = JOptionPane.showOptionDialog(
                this, // Parent component
                "What type of record would you like to add?", // Message
                "Add Record", // Title
                JOptionPane.DEFAULT_OPTION, // Option type
                JOptionPane.QUESTION_MESSAGE, // Message type
                null, // Icon
                types, // Options array
                types[0] // Default selection
        );
        // Check if user canceled the dialog
        if (choice < 0) {
            // Display cancellation message
            outputArea.append("❌ Add canceled.\n");
            // Exit method
            return;
        }
        // Try-catch block for input validation and database operations
        try {
            // Handle employee addition
            if (choice == 0) {
                // Get employee name from user
                String name    = JOptionPane.showInputDialog(this, "Employee name:");
                // Get hourly rate from user
                String rateStr = JOptionPane.showInputDialog(this, "Hourly rate (e.g. 15.50):");
                // Get hours worked from user
                String hrStr   = JOptionPane.showInputDialog(this, "Hours worked (e.g. 40):");
                // Check if any input is missing or canceled
                if (name==null||rateStr==null||hrStr==null) {
                    // Display error message
                    outputArea.append("❌ Add canceled or missing input.\n");
                    // Exit method
                    return;
                }
                // Parse hourly rate string to double
                double rate  = Double.parseDouble(rateStr.trim());
                // Parse hours worked string to double
                double hours = Double.parseDouble(hrStr.trim());
                // Add employee to database
                DatabaseManager.addEmployeeToDB(name.trim(), hours, rate);
                // Handle dish addition
            } else {
                // Get dish name from user
                String dname    = JOptionPane.showInputDialog(this, "Dish name:");
                // Get dish price from user
                String priceStr = JOptionPane.showInputDialog(this, "Price (e.g. 12.99):");
                // Check if any input is missing or canceled
                if (dname==null||priceStr==null) {
                    // Display error message
                    outputArea.append("❌ Add canceled or missing input.\n");
                    // Exit method
                    return;
                }
                // Parse price string to double
                double price = Double.parseDouble(priceStr.trim());
                // Add dish to database
                DatabaseManager.addDishToDB(dname.trim(), price);
            }
            // Clear existing in-memory data
            restaurant.clearAll();
            // Reload employees from database to in-memory model
            DatabaseManager.getAllEmployees().forEach(restaurant::addEmployee);
            // Reload dishes from database to in-memory model
            DatabaseManager.getAllDishes()   .forEach(restaurant::addDish);
            // Display success message
            outputArea.append("✅ " + types[choice] + " added and data refreshed.\n");
        }
        // Catch number format exceptions from parsing
        catch(NumberFormatException nfe) {
            // Display error message for invalid numbers
            outputArea.append("❌ Invalid number format. Please enter valid digits.\n");
        }
        // Catch any other exceptions
        catch(Exception ex) {
            // Display error message with exception details
            outputArea.append("❌ Failed to add record: " + ex.getMessage() + "\n");
        }
    }

    /**
     * Removes an Employee or Dish by ID via DatabaseManager,
     * then re-syncs the in-memory model.
     */
    // Method to remove records (employees or dishes) from the database
    private void removeRecord() {
        // Check if database connection exists
        if (DatabaseManager.getConnection() == null) {
            // Display warning if no connection
            outputArea.append("⚠️ Please Load Data before removing records.\n");
            // Exit method
            return;
        }
        // Define available record types for removal
        String[] types = {"Employee","Dish"};
        // Show option dialog for user to select record type
        int choice = JOptionPane.showOptionDialog(
                this, // Parent component
                "Which type of record would you like to remove?", // Message
                "Remove Record", // Title
                JOptionPane.DEFAULT_OPTION, // Option type
                JOptionPane.QUESTION_MESSAGE, // Message type
                null, // Icon
                types, // Options array
                types[0] // Default selection
        );
        // Check if user canceled the dialog
        if (choice < 0) {
            // Display cancellation message
            outputArea.append("❌ Remove canceled.\n");
            // Exit method
            return;
        }
        // Try-catch block for input validation and database operations
        try {
            // Get record ID from user
            String idStr = JOptionPane.showInputDialog(this,
                    String.format("Enter %s ID to remove:", types[choice]));
            // Check if input is missing or canceled
            if (idStr==null||idStr.trim().isEmpty()) {
                // Display error message
                outputArea.append("❌ Remove canceled or no ID entered.\n");
                // Exit method
                return;
            }
            // Parse ID string to integer
            int id = Integer.parseInt(idStr.trim());
            // Remove employee if choice is 0, otherwise remove dish
            if (choice==0) DatabaseManager.removeEmployeeFromDB(id);
            else          DatabaseManager.removeDishFromDB(id);

            // Clear existing in-memory data
            restaurant.clearAll();
            // Reload employees from database
            DatabaseManager.getAllEmployees().forEach(restaurant::addEmployee);
            // Reload dishes from database
            DatabaseManager.getAllDishes()   .forEach(restaurant::addDish);

            // Display success message with removed record details
            outputArea.append(String.format(
                    "✅ %s with ID %d removed and data refreshed.%n",
                    types[choice], id
            ));
        }
        // Catch number format exceptions from ID parsing
        catch(NumberFormatException nfe) {
            // Display error message for invalid ID format
            outputArea.append("❌ Invalid ID format. Please enter a numeric ID.\n");
        }
        // Catch any other exceptions
        catch(Exception ex) {
            // Display error message with exception details
            outputArea.append("❌ Remove failed: " + ex.getMessage() + "\n");
        }
    }

    /**
     * Updates a field of Employee or Dish by ID via DatabaseManager,
     * then re-syncs the in-memory model.
     */
    // Method to update existing records in the database
    private void updateRecord() {
        // Check if database connection exists
        if (DatabaseManager.getConnection() == null) {
            // Display warning if no connection
            outputArea.append("⚠️ Please Load Data before updating records.\n");
            // Exit method
            return;
        }
        // Define available record types for updating
        String[] types = {"Employee","Dish"};
        // Show option dialog for user to select record type
        int choice = JOptionPane.showOptionDialog(
                this, // Parent component
                "What type of record would you like to update?", // Message
                "Update Record", // Title
                JOptionPane.DEFAULT_OPTION, // Option type
                JOptionPane.QUESTION_MESSAGE, // Message type
                null, // Icon
                types, // Options array
                types[0] // Default selection
        );
        // Check if user canceled the dialog
        if (choice < 0) return;
        // Try-catch block for input validation and database operations
        try {
            // Get record ID from user
            String idStr = JOptionPane.showInputDialog(this,
                    "Enter " + types[choice] + " ID:");
            // Check if input is missing or canceled
            if (idStr==null||idStr.trim().isEmpty()) {
                // Display cancellation message
                outputArea.append("❌ Update canceled.\n");
                // Exit method
                return;
            }
            // Parse ID string to integer
            int id = Integer.parseInt(idStr.trim());

            // Define available fields based on record type
            String[] fields = choice==0
                    ? new String[]{"hoursWorked","hourlyRate"} // Employee fields
                    : new String[]{"name","price"}; // Dish fields
            // Show field selection dialog
            String field = (String) JOptionPane.showInputDialog(
                    this, // Parent component
                    "Which field to update?", // Message
                    "Field", // Title
                    JOptionPane.PLAIN_MESSAGE, // Message type
                    null, // Icon
                    fields, // Options array
                    fields[0] // Default selection
            );
            // Check if user canceled field selection
            if (field==null) {
                // Display cancellation message
                outputArea.append("❌ Update canceled.\n");
                // Exit method
                return;
            }

            // Get new value from user
            String newVal = JOptionPane.showInputDialog(this,
                    "New value for " + field + ":");
            // Check if input is missing or canceled
            if (newVal==null||newVal.trim().isEmpty()) {
                // Display cancellation message
                outputArea.append("❌ Update canceled.\n");
                // Exit method
                return;
            }

            // Update employee or dish based on choice
            if (choice==0) DatabaseManager.updateEmployeeInDB(id, field, newVal.trim());
            else           DatabaseManager.updateDishInDB(id, field, newVal.trim());

            // Clear existing in-memory data
            restaurant.clearAll();
            // Reload employees from database
            DatabaseManager.getAllEmployees().forEach(restaurant::addEmployee);
            // Reload dishes from database
            DatabaseManager.getAllDishes()   .forEach(restaurant::addDish);

            // Display success message with update details
            outputArea.append(String.format(
                    "✅ %s ID %d updated: %s = %s%n",
                    types[choice], id, field, newVal.trim()
            ));
        }
        // Catch number format exceptions from ID parsing
        catch(NumberFormatException nfe) {
            // Display error message for invalid ID
            outputArea.append("❌ Invalid ID. Please enter a numeric ID.\n");
        }
        // Catch any other exceptions
        catch(Exception ex) {
            // Display error message with exception details
            outputArea.append("❌ Update failed: " + ex.getMessage() + "\n");
        }
    }

    /** Displays all data */
    // Method to display all restaurant data in the output area
    private void displayAll() {
        // Clear existing text in output area
        outputArea.setText("");
        // Display restaurant name
        outputArea.append("🏪 Restaurant: " + restaurant.getName() + "\n");
        // Display restaurant status (open late or closes early)
        outputArea.append("⏰ Status: " + (restaurant.isOpenLate()?"Open Late ✅":"Closes Early ❌") + "\n");
        // Add visual separator
        outputArea.append("═══════════════════════════════════════\n");

        // Get list of all employees
        List<Employee> emps = restaurant.getEmployees();
        // Check if employee list is empty
        if (emps.isEmpty()) {
            // Display message if no employees
            outputArea.append("👤 No employees to display.\n");
        } else {
            // Display employee section header
            outputArea.append("👥 EMPLOYEES:\n");
            // Iterate through all employees
            for (Employee e : emps) {
                // Display employee ID and details
                outputArea.append(String.format("  [%d] %s\n", e.getId(), e.getDisplayString()));
            }
        }
        // Add spacing after employee section
        outputArea.append("\n");

        // Get list of dish display strings
        List<String> dishLines = restaurant.getMenuDisplayStrings();
        // Check if dish list is empty
        if (dishLines.isEmpty()) {
            // Display message if no dishes
            outputArea.append("🍽️ No dishes to display.\n");
        } else {
            // Display dishes section header
            outputArea.append("🍝 DISHES:\n");
            // Display each dish line with indentation
            dishLines.forEach(line -> outputArea.append("  " + line + "\n"));
        }
        // Add visual separator
        outputArea.append("═══════════════════════════════════════\n");

        // Get menu display strings (same as dishes in this case)
        List<String> menuLines = restaurant.getMenuDisplayStrings();
        // Check if menu is empty
        if (menuLines.isEmpty()) {
            // Display message if menu is empty
            outputArea.append("🍔 MENU is empty.\n");
        } else {
            // Display menu section header
            outputArea.append("🍔 MENU:\n");
            // Display each menu line with indentation
            menuLines.forEach(line -> outputArea.append("  " + line + "\n"));
        }
        // Add final visual separator
        outputArea.append("═══════════════════════════════════════\n");
    }

    /** Calculates payroll */
    // Method to calculate and display total payroll for all employees
    private void calculatePayroll() {
        // Get total payroll amount from restaurant
        double total = restaurant.getTotalPayroll();
        // Display formatted payroll total
        outputArea.append("💰 Total Weekly Payroll: $" + String.format("%.2f", total) + "\n");
    }

    /** Toggles openLate flag */
    // Method to toggle the restaurant's open late status
    private void toggleOpenLate() {
        // Toggle the open late flag to opposite value
        restaurant.setOpenLate(!restaurant.isOpenLate());
        // Display status change message
        outputArea.append("🌙 Open Late status changed to: " + (restaurant.isOpenLate()?"✅ Open Late":"❌ Closes Early") + "\n");
    }

    /** Exits application */
    // Method to safely exit the application with user confirmation
    private void exitApplication() {
        // Show confirmation dialog before exiting
        int res = JOptionPane.showConfirmDialog(
                this, // Parent component
                "Are you sure you want to exit?", // Message
                "Confirm Exit", // Title
                JOptionPane.YES_NO_OPTION, // Option type
                JOptionPane.QUESTION_MESSAGE // Message type
        );
        // Check if user confirmed exit
        if (res==JOptionPane.YES_OPTION) {
            // Display goodbye message
            outputArea.append("👋 Thank you for using Restaurant Manager Pro!\n");
            // Create timer to exit application after 1 second delay
            new Timer(1000, e -> System.exit(0)).start();
        }
    }

    // ----------------------------------------
// --- Helper UI classes below (unchanged)
// ----------------------------------------

    private class ModernButton extends JButton { // Custom JButton with modern styling and hover animations
        private final Color accentColor;         // Accent color used for hover effect
        private boolean isHovered;               // Tracks whether the mouse is currently over the button
        private Timer hoverTimer;                // Timer to drive the hover animation
        private float hoverProgress;             // Animation progress (0.0 to 1.0)

        ModernButton(String text, Color accent) { // Constructor: sets up text, colors, and hover
            super(text);                           // Initialize button text
            accentColor = accent;                  // Store accent color
            setPreferredSize(new Dimension(180, 50));               // Set fixed size
            setFont(new Font("Segoe UI", Font.BOLD, 12));           // Set font
            setForeground(TEXT_PRIMARY);                            // Text color
            setBackground(CARD_BG);                                 // Background color
            setBorder(BorderFactory.createEmptyBorder());           // Remove default border
            setFocusPainted(false);                                 // Disable focus painting
            setContentAreaFilled(false);                            // We'll draw the background ourselves
            setCursor(new Cursor(Cursor.HAND_CURSOR));              // Hand cursor on hover
            setupHoverAnimation();                                  // Initialize hover animation
        }

        private void setupHoverAnimation() { // Configures the hover animation timer and listeners
            hoverProgress = 0f;              // Start with no hover effect
            isHovered = false;               // Not hovered initially
            hoverTimer = new Timer(16, ev -> { // ~60 FPS timer
                if (isHovered && hoverProgress < 1f) {
                    hoverProgress = Math.min(1f, hoverProgress + 0.1f); // Fade in
                } else if (!isHovered && hoverProgress > 0f) {
                    hoverProgress = Math.max(0f, hoverProgress - 0.1f); // Fade out
                }
                repaint(); // Redraw with updated progress
                if ((isHovered && hoverProgress >= 1f) || (!isHovered && hoverProgress <= 0f)) {
                    hoverTimer.stop(); // Stop when animation completes
                }
            });
            addMouseListener(new MouseAdapter() { // Listen for enter/exit to start/stop timer
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

        @Override
        protected void paintComponent(Graphics g) { // Custom paint for rounded corners and glow
            Graphics2D g2 = (Graphics2D) g.create(); // Create a copy of Graphics2D
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Smooth edges

            // Interpolate between default background and accent based on hover progress
            Color bg = interpolateColor(CARD_BG, accentColor, hoverProgress * 0.3f);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12); // Draw rounded rect

            if (hoverProgress > 0) { // Draw glow when hovered
                Color glow = new Color(
                        accentColor.getRed(),
                        accentColor.getGreen(),
                        accentColor.getBlue(),
                        (int) (100 * hoverProgress)
                );
                g2.setColor(glow);
                g2.setStroke(new BasicStroke(2f)); // Thicker stroke for glow
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 12, 12);
            }

            g2.dispose();       // Clean up graphics
            super.paintComponent(g); // Draw the button's text
        }
    }

    private class ModernPanel extends JPanel { // Panel with rounded corners and solid background
        private final Color backgroundColor;     // Background color for this panel

        ModernPanel(Color bg) {
            backgroundColor = bg;                // Store the background color
            setOpaque(false);                    // We'll paint our own background
        }

        @Override
        protected void paintComponent(Graphics g) { // Custom paint for rounded panel
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(backgroundColor);        // Use the specified background
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); // Rounded rectangle

            g2.dispose();
            super.paintComponent(g);             // Paint child components, if any
        }
    }

    private class GradientPanel extends JPanel { // Panel with a diagonal gradient background
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Gradient from PRIMARY_DARK (top-left) to SECONDARY_DARK (bottom-right)
            GradientPaint gp = new GradientPaint(
                    0, 0, PRIMARY_DARK,
                    getWidth(), getHeight(), SECONDARY_DARK
            );
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.dispose();
        }
    }

    private class ModernScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI { // Custom scrollbar
        @Override
        protected void configureScrollBarColors() {
            thumbColor = ACCENT_BLUE;           // Color for the draggable thumb
            trackColor = PRIMARY_DARK;          // Background track color
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();          // Remove default arrow
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();          // Remove default arrow
        }

        private JButton createZeroButton() {    // Invisible zero-sized button
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(0, 0));
            return b;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle r) { // Paint the thumb
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(thumbColor);
            g2.fillRoundRect(r.x + 2, r.y + 2, r.width - 4, r.height - 4, 8, 8); // Rounded thumb

            g2.dispose();
        }
    }

    private Color interpolateColor(Color c1, Color c2, float f) { // Blends two colors by fraction f
        int r = (int) (c1.getRed() + f * (c2.getRed() - c1.getRed()));
        int g = (int) (c1.getGreen() + f * (c2.getGreen() - c1.getGreen()));
        int b = (int) (c1.getBlue() + f * (c2.getBlue() - c1.getBlue()));
        return new Color(r, g, b);
    }

    private void startBackgroundAnimations() { // Starts a timer for periodic redraw (background effects)
        animationTimer = new Timer(50, e -> repaint()); // Every 50ms, repaint the frame
        animationTimer.start();
    }

    public static void main(String[] args) { // Application entry point
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // Use system look-and-feel
        } catch (Exception e) {
            e.printStackTrace();                  // Log but continue if it fails
        }
        SwingUtilities.invokeLater(() -> new RestaurantAppGUI()); // Launch GUI on EDT
    }
}