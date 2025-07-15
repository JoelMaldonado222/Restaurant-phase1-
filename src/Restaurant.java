import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a restaurant management system that handles employee information,
 * menu items, and basic restaurant operations.
 *
 * <p>This class provides functionality to:
 * - Manage restaurant employees (add, remove, update)
 * - Manage dishes (add, remove, update)
 * - Calculate payroll information
 * - Track restaurant operational status (open late hours)
 * </p>
 *
 * <p>Internally, it uses {@link ArrayList} collections to store employees and dishes,
 * allowing for dynamic sizing and fast access.</p>
 *
 * @author Joel Maldonado
 * @version 1.0
 * @since 2024
 */
public class Restaurant {

    // ========================================
    // INSTANCE VARIABLES
    // ========================================

    /** The name of the restaurant. Cannot be null or empty. */
    private String name;

    /** Flag indicating whether the restaurant operates during late hours. */
    private boolean isOpenLate;

    /** A list to store employee records. */
    private List<Employee> employees;

    /** A list to store dish/menu item records. */
    private List<Dish> dishes;

    // ========================================
    // CONSTRUCTORS
    // ========================================

    /**
     * Constructs a new {@code Restaurant} object with the specified name.
     * Initializes empty lists for employees and dishes.
     * Sets the default late-hours flag to false.
     *
     * @param name the name of the restaurant (must not be null or empty)
     * @throws IllegalArgumentException if the name is null or blank
     */
    public Restaurant(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant name cannot be null or empty");
        }

        this.name = name.trim();           // Set and clean name input
        this.employees = new ArrayList<>(); // Initialize empty employee list
        this.dishes = new ArrayList<>();    // Initialize empty dish list
        this.isOpenLate = false;            // Default status: not open late
    }


    // ========================================
// GETTERS & SETTERS
// ========================================

    /**
     * Gets the name of the restaurant.
     *
     * @return the restaurant name
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if the restaurant is open late.
     *
     * @return {@code true} if open late; {@code false} otherwise
     */
    public boolean isOpenLate() {
        return isOpenLate;
    }

    /**
     * Sets the restaurant's late-night open status.
     *
     * @param openLate {@code true} to mark the restaurant as open late; {@code false} otherwise
     */
    public void setOpenLate(boolean openLate) {
        this.isOpenLate = openLate;
    }

// ========================================
// EMPLOYEE MANAGEMENT
// ========================================

    /**
     * Adds a new employee to the restaurant after validating input and checking for duplicates.
     *
     * @param name        the employee's name (non-null, non-empty)
     * @param hourlyRate  the employee's hourly rate (must be positive)
     * @param hoursWorked the number of hours worked (must be non-negative)
     * @return {@code null} if added successfully, or a validation error message
     */
    public String addEmployee(String name, double hourlyRate, double hoursWorked) {
        try {
            // Attempt to construct a new Employee object
            Employee e = new Employee(name, hourlyRate, hoursWorked);

            // Check for duplicates by name (case-insensitive)
            for (Employee existing : employees) {
                if (existing.getName().equalsIgnoreCase(e.getName())) {
                    return "Employee with name '" + e.getName() + "' already exists.";
                }
            }

            // Add to the list if validation passes
            employees.add(e);
            return null;

        } catch (IllegalArgumentException ex) {
            // Return validation error from Employee constructor
            return ex.getMessage();
        }
    }

    /**
     * Removes an employee by their name (case-insensitive).
     *
     * @param name the name of the employee to remove
     * @return status message indicating success or failure
     */
    public String removeEmployee(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Name cannot be null or empty.";
        }

        // Iterate with Iterator to safely remove while looping
        Iterator<Employee> it = employees.iterator();
        while (it.hasNext()) {
            if (it.next().getName().equalsIgnoreCase(name.trim())) {
                it.remove();
                return "Employee removed successfully.";
            }
        }

        return "Employee not found.";
    }

    /**
     * Updates an existing employee's hourly rate and hours worked.
     *
     * @param name     the employee's name to update
     * @param newRate  the new hourly rate to apply
     * @param newHours the new hours worked to apply
     * @return status message indicating result of update attempt
     */
    public String updateEmployee(String name, double newRate, double newHours) {
        if (name == null || name.trim().isEmpty()) {
            return "Name cannot be null or empty.";
        }

        for (Employee e : employees) {
            if (e.getName().equalsIgnoreCase(name.trim())) {
                boolean rateOk = e.setHourlyRate(newRate);
                boolean hoursOk = e.setHoursWorked(newHours);

                if (rateOk && hoursOk) {
                    return "Employee updated successfully.";
                } else {
                    return "Employee update completed with validation errors.";
                }
            }
        }

        return "Employee not found.";
    }

    /**
     * Returns formatted string representations for all employees.
     *
     * <p>Each string includes the employee's name, hourly rate, and hours worked,
     * as returned by the {@code getDisplayString()} method of the Employee class.</p>
     *
     * @return a list of formatted display strings for each employee
     */
    public List<String> getEmployeeDisplayStrings() {
        List<String> out = new ArrayList<>();
        for (Employee e : employees) {
            out.add(e.getDisplayString()); // Add formatted string to output list
        }
        return out;
    }

// ========================================
// DISH MANAGEMENT
// ========================================

    /**
     * Adds a new dish to the restaurant menu after validating input and checking for duplicates.
     *
     * @param name  the dish name (must not be null or empty)
     * @param price the price of the dish (must be non-negative)
     * @return {@code null} if added successfully, or a validation error message
     */
    public String addDish(String name, double price) {
        try {
            Dish d = new Dish(name, price);

            // Check for duplicates by name (case-insensitive)
            for (Dish existing : dishes) {
                if (existing.getName().equalsIgnoreCase(d.getName())) {
                    return "Dish with name '" + d.getName() + "' already exists.";
                }
            }

            dishes.add(d); // Add the new dish
            return null;

        } catch (IllegalArgumentException ex) {
            // Return error message from Dish constructor validation
            return ex.getMessage();
        }
    }

    /**
     * Removes a dish from the menu based on its name.
     *
     * @param name the name of the dish to remove (case-insensitive)
     * @return status message indicating result
     */
    public String removeDish(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Name cannot be null or empty.";
        }

        Iterator<Dish> it = dishes.iterator();
        while (it.hasNext()) {
            if (it.next().getName().equalsIgnoreCase(name.trim())) {
                it.remove(); // Safely remove dish during iteration
                return "Dish removed successfully.";
            }
        }

        return "Dish not found.";
    }

    /**
     * Updates the price of an existing dish.
     *
     * @param name     the name of the dish to update
     * @param newPrice the new price to apply
     * @return status message indicating success or failure
     */
    public String updateDish(String name, double newPrice) {
        if (name == null || name.trim().isEmpty()) {
            return "Name cannot be null or empty.";
        }

        for (Dish d : dishes) {
            if (d.getName().equalsIgnoreCase(name.trim())) {
                if (d.setPrice(newPrice)) {
                    return "Dish updated successfully.";
                } else {
                    return "Dish update failed due to validation.";
                }
            }
        }

        return "Dish not found.";
    }

    /**
     * Returns formatted strings for all dishes.
     *
     * @return list of dish display strings
     */
    public List<String> getMenuDisplayStrings() {
        List<String> out = new ArrayList<>();
        for (Dish d : dishes) {
            out.add(d.getDisplayString());
        }
        return out;
    }

    // ========================================
    // FINANCIAL METHODS
    // ========================================

    /**
     * Calculates total weekly payroll for all employees.
     *
     * @return total payroll amount
     */
    public double getTotalPayroll() {
        double total = 0;
        for (Employee e : employees) {
            total += e.getWeeklyPay();
        }
        return total;
    }

    // ========================================
    // GUI RELOAD HELPERS
    // ========================================

    /**
     * Clears in-memory lists of employees and dishes.
     */
    public void clearAll() {
        employees.clear();
        dishes.clear();
    }

    /**
     * Adds an Employee object into memory (used after DB load).
     *
     * @param e Employee to add
     */
    public void addEmployee(Employee e) {
        employees.add(e);
    }

    /**
     * Adds a Dish object into memory (used after DB load).
     *
     * @param d Dish to add
     */
    public void addDish(Dish d) {
        dishes.add(d);
    }
    /**
     * Returns a **copy** of the current in-memory employee list
     * so callers can iterate without mutating our internal list.
     */
    public List<Employee> getEmployees() {
        return new ArrayList<>(employees);
    }

    /**
     * Returns a **copy** of the current in-memory dishes list
     * so callers can iterate without mutating our internal list.
     */
    public List<Dish> getDishes() {
        return new ArrayList<>(dishes);
    }
    /**
     * Removes the first employee whose database‐ID matches the given id.
     * @param id the database ID to remove
     * @return true if an employee was found & removed, false otherwise
     */
    public boolean removeEmployeeById(int id) {
        return employees.removeIf(e -> e.getId() == id);
    }

    /**
     * Removes the first dish whose database‐ID matches the given id.
     * @param id the database ID to remove
     * @return true if a dish was found & removed, false otherwise
     */
    public boolean removeDishById(int id) {
        return dishes.removeIf(d -> d.getId() == id);
    }

}
