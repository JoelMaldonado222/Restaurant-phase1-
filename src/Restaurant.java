import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a restaurant management system that handles employee information,
 * menu items, and basic restaurant operations.
 *
 * This class provides functionality to:
 * - Manage restaurant employees (add, remove, update)
 * - Manage menu items (add, remove, update)
 * - Calculate payroll information
 * - Track restaurant operational status (open late hours)
 *
 * The class uses ArrayList collections to store employees and menu items,
 * providing dynamic sizing and efficient access to restaurant data.
 *
 * @author [Your Name]
 * @version 1.0
 * @since 2024
 */
public class Restaurant {

    // ========================================
    // INSTANCE VARIABLES
    // ========================================

    /**
     * The name of the restaurant.
     * This field stores the restaurant's display name and cannot be null or empty.
     * The name is trimmed of leading/trailing whitespace during initialization.
     */
    private String name;

    /**
     * Flag indicating whether the restaurant operates late hours.
     * Default value is false (restaurant does not stay open late).
     * This can be used to track special operating hours or late-night service availability.
     */
    private boolean isOpenLate;

    /**
     * Dynamic list of all employees working at the restaurant.
     * Uses ArrayList for efficient random access and dynamic resizing.
     * Each employee is represented by an Employee object containing
     * name, hourly rate, and hours worked information.
     */
    private ArrayList<Employee> employees;

    /**
     * Dynamic list of all dishes available on the restaurant's menu.
     * Uses ArrayList for efficient random access and dynamic resizing.
     * Each dish is represented by a Dish object containing
     * name and price information.
     */
    private ArrayList<Dish> menu;

    // ========================================
    // CONSTRUCTORS
    // ========================================

    /**
     * Constructs a new Restaurant with the specified name.
     *
     * Initializes the restaurant with:
     * - The provided name (after validation and trimming)
     * - Empty employee list
     * - Empty menu list
     * - Default late hours setting (false)
     *
     * @param name The name of the restaurant (cannot be null or empty)
     * @throws IllegalArgumentException if name is null, empty, or contains only whitespace
     *
     * @example
     * Restaurant myRestaurant = new Restaurant("Joe's Diner");
     */
    public Restaurant(String name) {
        // Validate that the restaurant name is not null or empty
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant name cannot be null or empty");
        }

        // Initialize restaurant properties
        this.name = name.trim(); // Remove leading/trailing whitespace
        this.employees = new ArrayList<>(); // Create empty employee list
        this.menu = new ArrayList<>(); // Create empty menu list
        this.isOpenLate = false; // Set default operating hours
    }

    // ========================================
    // GETTER METHODS
    // ========================================

    /**
     * Retrieves the name of the restaurant.
     *
     * @return The restaurant's name as a String (never null or empty)
     *
     * @example
     * String restaurantName = myRestaurant.getName();
     * System.out.println("Restaurant: " + restaurantName);
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if the restaurant operates late hours.
     *
     * @return true if the restaurant is open late, false otherwise
     *
     * @example
     * if (myRestaurant.isOpenLate()) {
     *     System.out.println("This restaurant serves late night customers!");
     * }
     */
    public boolean isOpenLate() {
        return isOpenLate;
    }

    // ========================================
    // SETTER METHODS
    // ========================================

    /**
     * Sets the late hours operational status of the restaurant.
     *
     * This method allows updating whether the restaurant provides
     * late-night service or extended operating hours.
     *
     * @param openLate true to indicate late hours operation, false for regular hours
     *
     * @example
     * myRestaurant.setOpenLate(true); // Restaurant now serves late customers
     */
    public void setOpenLate(boolean openLate) {
        this.isOpenLate = openLate;
    }

    // ========================================
    // EMPLOYEE MANAGEMENT METHODS
    // ========================================

    /**
     * Adds a new employee to the restaurant's staff.
     *
     * This method creates a new Employee object with the provided parameters
     * and adds it to the restaurant's employee list. It performs validation
     * to ensure no duplicate employee names exist (case-insensitive comparison).
     *
     * Validation includes:
     * - Employee name cannot be null or empty
     * - Hourly rate must be positive
     * - Hours worked must be non-negative
     * - Employee name must be unique within the restaurant
     *
     * @param name The employee's full name (cannot be null or empty)
     * @param hourlyRate The employee's hourly wage rate (must be positive)
     * @param hoursWorked The number of hours the employee has worked (must be non-negative)
     * @return null if the employee was successfully added, or an error message string if validation failed
     *
     * @example
     * String result = myRestaurant.addEmployee("John Smith", 15.50, 40.0);
     * if (result == null) {
     *     System.out.println("Employee added successfully!");
     * } else {
     *     System.out.println("Error: " + result);
     * }
     */
    public String addEmployee(String name, double hourlyRate, double hoursWorked) {
        try {
            // Create new employee object (this will validate the parameters)
            Employee e = new Employee(name, hourlyRate, hoursWorked);

            // Check for duplicate employee names (case-insensitive)
            for (Employee existing : employees) {
                if (existing.getName().equalsIgnoreCase(e.getName())) {
                    return "Employee with name '" + e.getName() + "' already exists.";
                }
            }

            // Add the new employee to the list
            employees.add(e);
            return null; // Success - no error message
        } catch (IllegalArgumentException ex) {
            // Return the validation error message from Employee constructor
            return ex.getMessage();
        }
    }

    /**
     * Removes an employee from the restaurant's staff by name.
     *
     * This method searches for an employee with the specified name
     * (case-insensitive search) and removes them from the employee list.
     * Uses Iterator to safely remove items during iteration.
     *
     * @param name The name of the employee to remove (cannot be null or empty)
     * @return A status message indicating the result of the operation:
     *         - "Employee removed successfully." if employee was found and removed
     *         - "Employee not found." if no matching employee exists
     *         - "Name cannot be null or empty." if invalid name provided
     *
     * @example
     * String result = myRestaurant.removeEmployee("John Smith");
     * System.out.println(result); // "Employee removed successfully." or error message
     */
    public String removeEmployee(String name) {
        // Validate input parameter
        if (name == null || name.trim().isEmpty()) {
            return "Name cannot be null or empty.";
        }

        // Use iterator for safe removal during iteration
        Iterator<Employee> iterator = employees.iterator();
        while (iterator.hasNext()) {
            Employee e = iterator.next();
            // Case-insensitive name comparison
            if (e.getName().equalsIgnoreCase(name.trim())) {
                iterator.remove();
                return "Employee removed successfully.";
            }
        }
        return "Employee not found.";
    }

    /**
     * Updates an existing employee's hourly rate and hours worked.
     *
     * This method searches for an employee by name and updates their
     * employment information with new values. Both hourly rate and
     * hours worked are updated in a single operation.
     *
     * @param name The name of the employee to update (cannot be null or empty)
     * @param newRate The new hourly rate for the employee (must be positive)
     * @param newHours The new hours worked for the employee (must be non-negative)
     * @return A status message indicating the result:
     *         - "Employee updated successfully." if both updates succeeded
     *         - "Employee update completed with some validation errors." if updates had validation issues
     *         - "Employee not found." if no matching employee exists
     *         - "Name cannot be null or empty." if invalid name provided
     *
     * @example
     * String result = myRestaurant.updateEmployee("John Smith", 16.00, 45.0);
     * System.out.println(result); // Status of the update operation
     */
    public String updateEmployee(String name, double newRate, double newHours) {
        // Validate input parameter
        if (name == null || name.trim().isEmpty()) {
            return "Name cannot be null or empty.";
        }

        // Search for the employee and update their information
        for (Employee e : employees) {
            if (e.getName().equalsIgnoreCase(name.trim())) {
                // Attempt to update both hourly rate and hours worked
                boolean rateUpdated = e.setHourlyRate(newRate);
                boolean hoursUpdated = e.setHoursWorked(newHours);

                // Return appropriate status based on update results
                if (rateUpdated && hoursUpdated) {
                    return "Employee updated successfully.";
                } else {
                    return "Employee update completed with some validation errors.";
                }
            }
        }
        return "Employee not found.";
    }

    /**
     * Retrieves display strings for all employees in the restaurant.
     *
     * This method generates a list of formatted strings representing
     * each employee's information. The format is determined by the
     * Employee class's getDisplayString() method.
     *
     * @return A List of String objects, each containing formatted employee information.
     *         Returns an empty list if no employees exist.
     *
     * @example
     * List<String> employeeList = myRestaurant.getEmployeeDisplayStrings();
     * for (String employee : employeeList) {
     *     System.out.println(employee);
     * }
     */
    public List<String> getEmployeeDisplayStrings() {
        List<String> employeeStrings = new ArrayList<>();

        // Generate display string for each employee
        for (Employee e : employees) {
            employeeStrings.add(e.getDisplayString());
        }

        return employeeStrings;
    }

    // ========================================
    // MENU MANAGEMENT METHODS
    // ========================================

    /**
     * Adds a new dish to the restaurant's menu.
     *
     * This method creates a new Dish object with the provided parameters
     * and adds it to the restaurant's menu list. It performs validation
     * to ensure no duplicate dish names exist (case-insensitive comparison).
     *
     * Validation includes:
     * - Dish name cannot be null or empty
     * - Price must be non-negative
     * - Dish name must be unique within the menu
     *
     * @param name The name of the dish (cannot be null or empty)
     * @param price The price of the dish (must be non-negative)
     * @return null if the dish was successfully added, or an error message string if validation failed
     *
     * @example
     * String result = myRestaurant.addDish("Grilled Salmon", 18.99);
     * if (result == null) {
     *     System.out.println("Dish added to menu successfully!");
     * } else {
     *     System.out.println("Error: " + result);
     * }
     */
    public String addDish(String name, double price) {
        try {
            // Create new dish object (this will validate the parameters)
            Dish d = new Dish(name, price);

            // Check for duplicate dish names (case-insensitive)
            for (Dish existing : menu) {
                if (existing.getName().equalsIgnoreCase(d.getName())) {
                    return "Dish with name '" + d.getName() + "' already exists.";
                }
            }

            // Add the new dish to the menu
            menu.add(d);
            return null; // Success - no error message
        } catch (IllegalArgumentException ex) {
            // Return the validation error message from Dish constructor
            return ex.getMessage();
        }
    }

    /**
     * Removes a dish from the restaurant's menu by name.
     *
     * This method searches for a dish with the specified name
     * (case-insensitive search) and removes it from the menu list.
     * Uses Iterator to safely remove items during iteration.
     *
     * @param name The name of the dish to remove (cannot be null or empty)
     * @return A status message indicating the result of the operation:
     *         - "Dish removed successfully." if dish was found and removed
     *         - "Dish not found." if no matching dish exists
     *         - "Name cannot be null or empty." if invalid name provided
     *
     * @example
     * String result = myRestaurant.removeDish("Grilled Salmon");
     * System.out.println(result); // "Dish removed successfully." or error message
     */
    public String removeDish(String name) {
        // Validate input parameter
        if (name == null || name.trim().isEmpty()) {
            return "Name cannot be null or empty.";
        }

        // Use iterator for safe removal during iteration
        Iterator<Dish> iterator = menu.iterator();
        while (iterator.hasNext()) {
            Dish d = iterator.next();
            // Case-insensitive name comparison
            if (d.getName().equalsIgnoreCase(name.trim())) {
                iterator.remove();
                return "Dish removed successfully.";
            }
        }
        return "Dish not found.";
    }

    /**
     * Updates the price of an existing dish on the menu.
     *
     * This method searches for a dish by name and updates its price
     * with the new value provided. The dish name remains unchanged.
     *
     * @param name The name of the dish to update (cannot be null or empty)
     * @param newPrice The new price for the dish (must be non-negative)
     * @return A status message indicating the result:
     *         - "Dish updated successfully." if price was updated successfully
     *         - "Dish update failed due to validation error." if new price is invalid
     *         - "Dish not found." if no matching dish exists
     *         - "Name cannot be null or empty." if invalid name provided
     *
     * @example
     * String result = myRestaurant.updateDish("Grilled Salmon", 19.99);
     * System.out.println(result); // Status of the update operation
     */
    public String updateDish(String name, double newPrice) {
        // Validate input parameter
        if (name == null || name.trim().isEmpty()) {
            return "Name cannot be null or empty.";
        }

        // Search for the dish and update its price
        for (Dish d : menu) {
            if (d.getName().equalsIgnoreCase(name.trim())) {
                // Attempt to update the dish price
                if (d.setPrice(newPrice)) {
                    return "Dish updated successfully.";
                } else {
                    return "Dish update failed due to validation error.";
                }
            }
        }
        return "Dish not found.";
    }

    /**
     * Retrieves display strings for all dishes on the restaurant's menu.
     *
     * This method generates a list of formatted strings representing
     * each dish's information. The format is determined by the
     * Dish class's getDisplayString() method.
     *
     * @return A List of String objects, each containing formatted dish information.
     *         Returns an empty list if no dishes exist on the menu.
     *
     * @example
     * List<String> menuList = myRestaurant.getMenuDisplayStrings();
     * System.out.println("Menu:");
     * for (String dish : menuList) {
     *     System.out.println("- " + dish);
     * }
     */
    public List<String> getMenuDisplayStrings() {
        List<String> menuStrings = new ArrayList<>();

        // Generate display string for each dish
        for (Dish d : menu) {
            menuStrings.add(d.getDisplayString());
        }

        return menuStrings;
    }

    // ========================================
    // FINANCIAL CALCULATION METHODS
    // ========================================

    /**
     * Calculates and returns the total weekly payroll for all employees.
     *
     * This method iterates through all employees and sums up their
     * individual weekly pay amounts. The weekly pay for each employee
     * is calculated as (hourly rate × hours worked).
     *
     * @return The total weekly payroll amount as a double.
     *         Returns 0.0 if no employees exist or if all employees have zero pay.
     *
     * @example
     * double totalPayroll = myRestaurant.getTotalPayroll();
     * System.out.printf("Total weekly payroll: $%.2f%n", totalPayroll);
     */
    public double getTotalPayroll() {
        double total = 0;

        // Sum up weekly pay for all employees
        for (Employee e : employees) {
            total += e.getWeeklyPay();
        }

        return total;
    }
}