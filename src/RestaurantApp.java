// Import statements for file operations, exception handling, and data structures
import java.io.File;                    // For file system operations and file existence checking
import java.io.FileNotFoundException;   // Exception thrown when specified file cannot be found
import java.util.Scanner;              // For reading user input from console and file input
import java.util.List;                 // Interface for ordered collections (used for display data)

/**
 * Main application class that serves as the entry point and user interface controller
 * for a restaurant management system. This class handles all user interactions,
 * menu navigation, and delegates business logic operations to the Restaurant class.
 *
 * The application provides functionality to:
 * - Load employee and dish data from CSV files
 * - Display all restaurant information in formatted tables
 * - Add, remove, and update employee and dish records
 * - Calculate total weekly payroll for all employees
 * - Toggle restaurant's "open late" operational status
 */
public class RestaurantApp {
    // Instance variable to hold the restaurant business object that contains all data and operations
    private Restaurant restaurant;

    // Scanner instance for reading user input from the console throughout the application lifecycle
    private Scanner scanner;

    /**
     * Default constructor that initializes the application with a pre-configured restaurant
     * and sets up the input scanner for user interaction.
     *
     * Creates a new Restaurant instance named "Emery's" and initializes the Scanner
     * to read from System.in for console input throughout the application.
     */
    public RestaurantApp() {
        // Initialize restaurant with hardcoded name - could be made configurable in future versions
        restaurant = new Restaurant("Emery's");

        // Create scanner for reading user input from standard input stream
        scanner = new Scanner(System.in);
    }

    /**
     * Application entry point that creates a new RestaurantApp instance and starts the main loop.
     * This is the method called by the JVM when the program is executed.
     *
     * @param args Command-line arguments (not used in this application)
     */
    public static void main(String[] args) {
        // Create new application instance and immediately start the main menu loop
        new RestaurantApp().run();
    }

    /**
     * Main application loop that displays the menu and processes user selections.
     * This method contains the core program flow and continues running until the user
     * chooses to exit. It handles all menu navigation and delegates specific operations
     * to appropriate helper methods.
     *
     * The loop continues indefinitely until the user selects option 7 (exit) or
     * an unexpected error occurs. Each iteration displays the menu, reads user input,
     * and executes the corresponding functionality.
     */
    public void run() {
        // Flag to control the main application loop - set to false when user chooses to exit
        boolean running = true;

        // Main menu loop - continues until user exits or program terminates
        while (running) {
            // Display the formatted menu options to the user
            System.out.println(getMenuText());

            // Read user's menu choice and trim any whitespace to handle input variations
            String choice = scanner.nextLine().trim();

            // Process the user's menu selection using a switch statement for clarity
            switch (choice) {
                case "1":
                    // File loading operation - prompts for file path and attempts to load data
                    System.out.print("Enter full file path: ");
                    String filePath = scanner.nextLine().trim();

                    // Delegate file reading to specialized method and display the result
                    String result = readDataFromFile(filePath);
                    System.out.println(result);
                    break;

                case "2":
                    // Display all restaurant data in formatted tables
                    // Iterate through all formatted display lines and print each one
                    for (String line : getDisplayAllLines()) {
                        System.out.println(line);
                    }
                    break;

                case "3":
                    // Add new employee or dish record based on user input
                    // Display the result message (success or error) returned by addRecord()
                    System.out.println(addRecord());
                    break;

                case "4":
                    // Remove existing employee or dish record
                    // Display the result message returned by removeRecord()
                    System.out.println(removeRecord());
                    break;

                case "5":
                    // Update existing employee or dish record with new values
                    // Display the result message returned by updateRecord()
                    System.out.println(updateRecord());
                    break;

                case "6":
                    // Calculate and display total weekly payroll for all employees
                    System.out.println(getTotalPayrollText());
                    break;

                case "7":
                    // Exit the program gracefully
                    running = false;  // Set flag to false to exit the main loop
                    System.out.println("Exiting the program. Goodbye!");
                    break;

                case "8":
                    // Toggle the restaurant's "Open Late" operational status
                    // Use the current status to set the opposite value (true becomes false, false becomes true)
                    restaurant.setOpenLate(!restaurant.isOpenLate());

                    // Display confirmation with visual indicators (checkmark for true, X for false)
                    System.out.println("Open late is now set to: " + (restaurant.isOpenLate() ? "✅" : "❌"));
                    break;

                default:
                    // Handle invalid menu selections with helpful error message
                    System.out.println("Invalid option. Please select 1–8.");
            }
        }

        // Clean up resources by closing the scanner when exiting the application
        // This prevents resource leaks and is good practice for I/O operations
        scanner.close();
    }

    /**
     * Generates and returns the formatted main menu text displayed to users.
     * This method centralizes the menu text for easy maintenance and consistent formatting.
     *
     * @return String containing the complete formatted menu with all available options
     */
    public String getMenuText() {
        // Return multi-line string with menu title and all available options
        // Uses string concatenation for clarity and readability
        return "\n--- Restaurant Manager ---\n" +
                "1. Load data from file\n" +
                "2. Display all data\n" +
                "3. Add new record\n" +
                "4. Remove record\n" +
                "5. Update record\n" +
                "6. Calculate total payroll\n" +
                "7. Exit\n" +
                "8. Toggle Open Late Status\n" +
                "Enter your choice: ";
    }

    /**
     * Builds and returns a formatted list of all restaurant data for display purposes.
     * This method creates a comprehensive view of the restaurant including basic info,
     * operational status, employee records in tabular format, and menu items.
     *
     * The method handles empty datasets gracefully by displaying appropriate messages
     * when no employees or dishes exist in the system.
     *
     * @return List<String> containing formatted lines ready for console display
     */
    public List<String> getDisplayAllLines() {
        // Create ArrayList to hold all formatted display lines
        List<String> lines = new java.util.ArrayList<>();

        // Add restaurant header information with name and operational status
        lines.add("Restaurant: " + restaurant.getName());
        lines.add("Status: " + (restaurant.isOpenLate() ? "Open Late ✅" : "Closes Early ❌"));
        lines.add("------------------------------------");

        // Generate and add employee section with formatted table or empty message
        List<String> employees = restaurant.getEmployeeDisplayStrings();
        if (employees.isEmpty()) {
            // Handle case where no employees exist in the system
            lines.add("No employees to display.");
        } else {
            // Add employee section header and table formatting
            lines.add("\nEmployees:");
            lines.add("Name            | Rate     | Hours | Weekly Pay");
            lines.add("----------------|----------|-------|------------");

            // Add all formatted employee records to the display
            lines.addAll(employees);
        }

        // Generate and add menu section with formatted table or empty message
        List<String> menu = restaurant.getMenuDisplayStrings();
        if (menu.isEmpty()) {
            // Handle case where no dishes exist on the menu
            lines.add("Menu is empty.");
        } else {
            // Add menu section header and table formatting
            lines.add("\nMenu:");
            lines.add("Dish Name           | Price");
            lines.add("--------------------|--------");

            // Add all formatted dish records to the display
            lines.addAll(menu);
        }

        // Return the complete list of formatted display lines
        return lines;
    }

    /**
     * Reads and processes data from a CSV file to populate the restaurant with employees and dishes.
     * This method handles file validation, parsing, error recovery, and provides detailed feedback
     * about the loading process including success and error counts.
     *
     * The method expects CSV format with either:
     * - 3 columns for employees: name, hourly_rate, hours_worked
     * - 2 columns for dishes: name, price
     *
     * @param filename Path to the CSV file to be processed
     * @return String containing summary of the loading operation with success/error counts
     */
    public String readDataFromFile(String filename) {
        // Validate that filename is not null or empty to prevent processing invalid input
        if (filename == null || filename.trim().isEmpty()) {
            return "Filename cannot be empty.";
        }

        // Create File object and check if the specified file exists in the filesystem
        File file = new File(filename.trim());
        if (!file.exists()) {
            return "File not found: " + filename;
        }

        // Initialize counters to track successful and failed record additions
        int successCount = 0;  // Number of records successfully added to the restaurant
        int errorCount = 0;    // Number of records that failed to be processed

        // Use try-with-resources to automatically close the file scanner when done
        try (Scanner fileScanner = new Scanner(file)) {
            // Process each line in the file until end of file is reached
            while (fileScanner.hasNextLine()) {
                // Read the next line and remove leading/trailing whitespace
                String line = fileScanner.nextLine().trim();

                // Skip empty lines to avoid processing blank entries
                if (line.isEmpty()) continue;

                // Split the line by commas to extract individual data fields
                String[] parts = line.split(",");

                // Flag to track whether the current line was processed successfully
                boolean success = false;

                // Use try-catch to handle number parsing errors for individual lines
                try {
                    if (parts.length == 3) {
                        // Process employee record: name, hourly_rate, hours_worked
                        String name = parts[0].trim();                    // Employee name
                        double rate = Double.parseDouble(parts[1].trim()); // Hourly pay rate
                        double hours = Double.parseDouble(parts[2].trim());// Hours worked per week

                        // Attempt to add employee to restaurant and check for business rule violations
                        String result = restaurant.addEmployee(name, rate, hours);
                        success = (result == null);  // null result indicates successful addition

                        // Display warning if employee addition failed due to business rules
                        if (!success) System.out.println("⚠️ Failed to add employee: " + result);

                    } else if (parts.length == 2) {
                        // Process dish record: name, price
                        String name = parts[0].trim();                     // Dish name
                        double price = Double.parseDouble(parts[1].trim()); // Menu price

                        // Attempt to add dish to restaurant menu and check for business rule violations
                        String result = restaurant.addDish(name, price);
                        success = (result == null);  // null result indicates successful addition

                        // Display warning if dish addition failed due to business rules
                        if (!success) System.out.println("⚠️ Failed to add dish: " + result);

                    } else {
                        // Handle lines with incorrect number of fields (not 2 or 3)
                        System.out.println("⚠️ Invalid format: " + line);
                    }

                    // Update appropriate counter based on success or failure
                    if (success) successCount++;
                    else errorCount++;

                } catch (NumberFormatException ex) {
                    // Handle cases where numeric fields cannot be parsed (invalid numbers)
                    System.out.println("⚠️ Invalid number in line: " + line);
                    errorCount++;
                }
            }
        } catch (FileNotFoundException e) {
            // Handle the case where file was deleted between existence check and opening
            return "Error: " + e.getMessage();
        }

        // Return formatted summary with success and error counts using emoji for visual clarity
        return String.format("✅ Load complete: %d entries added, %d errors.", successCount, errorCount);
    }

    /**
     * Handles adding new employee or dish records based on user input and selection.
     * This method prompts the user to choose between adding an employee or dish,
     * then collects the appropriate data and validates it before attempting to add
     * the record to the restaurant.
     *
     * For employees: collects name (letters/spaces only), hourly rate, and hours worked
     * For dishes: collects name and price
     *
     * @return String containing success confirmation or detailed error message
     */
    public String addRecord() {
        // Prompt user to select what type of record to add
        System.out.println("Add (1) Employee or (2) Dish?");
        String type = scanner.nextLine().trim();

        if ("1".equals(type)) {
            // Handle employee addition with comprehensive input validation
            try {
                // Collect employee name with validation for letters and spaces only
                System.out.print("Employee name: ");
                String name = scanner.nextLine().trim();

                // Validate name is not empty and contains only letters and spaces
                if (name.isEmpty() || !name.matches("[a-zA-Z ]+")) {
                    return "Name must contain only letters and spaces, and cannot be empty.";
                }

                // Collect hourly rate with automatic number parsing
                System.out.print("Hourly rate: ");
                double rate = Double.parseDouble(scanner.nextLine().trim());

                // Collect hours worked with automatic number parsing
                System.out.print("Hours worked: ");
                double hours = Double.parseDouble(scanner.nextLine().trim());

                // Attempt to add employee to restaurant and handle business rule violations
                String error = restaurant.addEmployee(name, rate, hours);
                if (error != null) return "Failed to add employee: " + error;

                // Return success message if no errors occurred
                return "Employee added successfully.";

            } catch (NumberFormatException e) {
                // Handle invalid numeric input for rate or hours
                return "Invalid input; please enter numbers for rate/hours.";
            }

        } else if ("2".equals(type)) {
            // Handle dish addition with input validation
            try {
                // Collect dish name with basic validation
                System.out.print("Dish name: ");
                String dish = scanner.nextLine().trim();

                // Validate dish name is not empty
                if (dish.isEmpty()) return "Dish name cannot be empty.";

                // Collect price with automatic number parsing
                System.out.print("Price: ");
                double price = Double.parseDouble(scanner.nextLine().trim());

                // Attempt to add dish to restaurant menu and handle business rule violations
                String error = restaurant.addDish(dish, price);
                if (error != null) return "Failed to add dish: " + error;

                // Return success message if no errors occurred
                return "Dish added successfully.";

            } catch (NumberFormatException e) {
                // Handle invalid numeric input for price
                return "Invalid input; please enter a number for price.";
            }
        } else {
            // Handle invalid selection (not 1 or 2)
            return "Invalid selection.";
        }
    }

    /**
     * Handles removing existing employee or dish records from the restaurant.
     * This method prompts the user to choose between removing an employee or dish,
     * then collects the name and delegates the removal operation to the restaurant.
     *
     * @return String containing confirmation of removal or error message if record not found
     */
    public String removeRecord() {
        // Prompt user to select what type of record to remove
        System.out.println("Remove (1) Employee or (2) Dish?");
        String type = scanner.nextLine().trim();

        if ("1".equals(type)) {
            // Handle employee removal
            System.out.print("Employee name to remove: ");
            String name = scanner.nextLine().trim();

            // Validate name is not empty, then delegate removal to restaurant
            return name.isEmpty() ? "Name cannot be empty." : restaurant.removeEmployee(name);

        } else if ("2".equals(type)) {
            // Handle dish removal
            System.out.print("Dish name to remove: ");
            String name = scanner.nextLine().trim();

            // Validate name is not empty, then delegate removal to restaurant
            return name.isEmpty() ? "Name cannot be empty." : restaurant.removeDish(name);

        } else {
            // Handle invalid selection (not 1 or 2)
            return "Invalid selection.";
        }
    }

    /**
     * Handles updating existing employee or dish records with new values.
     * This method prompts the user to choose between updating an employee or dish,
     * collects the name to identify the record, then collects new values and
     * validates them before attempting the update operation.
     *
     * For employees: updates hourly rate and hours worked
     * For dishes: updates price
     *
     * @return String containing confirmation of update or error message if record not found or validation fails
     */
    public String updateRecord() {
        // Prompt user to select what type of record to update
        System.out.println("Update (1) Employee or (2) Dish?");
        String type = scanner.nextLine().trim();

        if ("1".equals(type)) {
            // Handle employee update with comprehensive validation
            System.out.print("Enter employee name to update: ");
            String name = scanner.nextLine().trim();

            // Validate name format matches employee naming requirements
            if (name.isEmpty() || !name.matches("[a-zA-Z ]+")) {
                return "Name must contain only letters and spaces, and cannot be empty.";
            }

            try {
                // Collect new hourly rate with automatic number parsing
                System.out.print("New hourly rate: ");
                double newRate = Double.parseDouble(scanner.nextLine().trim());

                // Collect new hours worked with automatic number parsing
                System.out.print("New hours worked: ");
                double newHours = Double.parseDouble(scanner.nextLine().trim());

                // Delegate update operation to restaurant and return the result
                return restaurant.updateEmployee(name, newRate, newHours);

            } catch (NumberFormatException e) {
                // Handle invalid numeric input for rate or hours
                return "Invalid input; please enter numeric values.";
            }

        } else if ("2".equals(type)) {
            // Handle dish update with validation
            System.out.print("Enter dish name to update: ");
            String name = scanner.nextLine().trim();

            // Validate dish name is not empty
            if (name.isEmpty()) return "Dish name cannot be empty.";

            try {
                // Collect new price with automatic number parsing
                System.out.print("New price: ");
                double newPrice = Double.parseDouble(scanner.nextLine().trim());

                // Delegate update operation to restaurant and return the result
                return restaurant.updateDish(name, newPrice);

            } catch (NumberFormatException e) {
                // Handle invalid numeric input for price
                return "Invalid input; please enter a number for price.";
            }
        } else {
            // Handle invalid selection (not 1 or 2)
            return "Invalid selection.";
        }
    }

    /**
     * Calculates and returns a formatted string showing the total weekly payroll for all employees.
     * This method delegates the calculation to the restaurant object and formats the result
     * with appropriate currency formatting for display purposes.
     *
     * @return String containing formatted total payroll amount in currency format
     */
    public String getTotalPayrollText() {
        // Get total payroll calculation from restaurant business object
        double total = restaurant.getTotalPayroll();

        // Format the total as currency with 2 decimal places and return with descriptive text
        return String.format("Total weekly payroll: $%.2f", total);
    }
    public RestaurantApp(Restaurant sharedRestaurant) {
        this.restaurant = sharedRestaurant;
        this.scanner = new Scanner(System.in); // still used for logic reuse
    }

}