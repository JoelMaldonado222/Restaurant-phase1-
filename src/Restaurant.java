import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a restaurant management system that handles employee information,
 * menu items, and basic restaurant operations.
 *
 * This class provides functionality to:
 * - Manage restaurant employees (add, remove, update)
 * - Manage dishes (add, remove, update)
 * - Calculate payroll information
 * - Track restaurant operational status (open late hours)
 *
 * Uses ArrayList collections to store employees and dishes,
 * providing dynamic sizing and efficient access to data.
 *
 * @author [Your Name]
 * @version 1.0
 * @since 2024
 */
public class Restaurant {

    // ========================================
    // INSTANCE VARIABLES
    // ========================================

    /** The name of the restaurant. Cannot be null or empty. */
    private String name;

    /** Flag indicating whether the restaurant operates late hours. */
    private boolean isOpenLate;

    /** List of Employee objects representing staff. */
    private List<Employee> employees;

    /** List of Dish objects representing menu items. */
    private List<Dish> dishes;

    // ========================================
    // CONSTRUCTORS
    // ========================================

    /**
     * Constructs a new Restaurant with the specified name.
     * Initializes empty lists for employees and dishes, and sets default hours.
     *
     * @param name the restaurant name (non-null, non-empty)
     * @throws IllegalArgumentException if name is invalid
     */
    public Restaurant(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant name cannot be null or empty");
        }
        this.name = name.trim();
        this.employees = new ArrayList<>();
        this.dishes = new ArrayList<>();
        this.isOpenLate = false;
    }

    // ========================================
    // GETTERS & SETTERS
    // ========================================

    public String getName() {
        return name;
    }

    public boolean isOpenLate() {
        return isOpenLate;
    }

    public void setOpenLate(boolean openLate) {
        this.isOpenLate = openLate;
    }

    // ========================================
    // EMPLOYEE MANAGEMENT
    // ========================================

    /**
     * Adds a new employee after validating inputs and duplicates.
     *
     * @param name        employee name (non-null, non-empty)
     * @param hourlyRate  positive hourly rate
     * @param hoursWorked non-negative hours worked
     * @return null if successful, or error message
     */
    public String addEmployee(String name, double hourlyRate, double hoursWorked) {
        try {
            Employee e = new Employee(name, hourlyRate, hoursWorked);
            for (Employee existing : employees) {
                if (existing.getName().equalsIgnoreCase(e.getName())) {
                    return "Employee with name '" + e.getName() + "' already exists.";
                }
            }
            employees.add(e);
            return null;
        } catch (IllegalArgumentException ex) {
            return ex.getMessage();
        }
    }

    /**
     * Removes an employee by name (case-insensitive).
     *
     * @param name employee name to remove
     * @return status message
     */
    public String removeEmployee(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Name cannot be null or empty.";
        }
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
     * Updates an employee's rate and hours.
     *
     * @param name     employee name to update
     * @param newRate  new hourly rate
     * @param newHours new hours worked
     * @return status message
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
     * Returns formatted strings for all employees.
     *
     * @return list of employee display strings
     */
    public List<String> getEmployeeDisplayStrings() {
        List<String> out = new ArrayList<>();
        for (Employee e : employees) {
            out.add(e.getDisplayString());
        }
        return out;
    }

    // ========================================
    // DISH MANAGEMENT
    // ========================================

    /**
     * Adds a new dish after validation and duplicate check.
     *
     * @param name  dish name (non-null, non-empty)
     * @param price non-negative price
     * @return null if successful, or error message
     */
    public String addDish(String name, double price) {
        try {
            Dish d = new Dish(name, price);
            for (Dish existing : dishes) {
                if (existing.getName().equalsIgnoreCase(d.getName())) {
                    return "Dish with name '" + d.getName() + "' already exists.";
                }
            }
            dishes.add(d);
            return null;
        } catch (IllegalArgumentException ex) {
            return ex.getMessage();
        }
    }

    /**
     * Removes a dish by name (case-insensitive).
     *
     * @param name dish name to remove
     * @return status message
     */
    public String removeDish(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Name cannot be null or empty.";
        }
        Iterator<Dish> it = dishes.iterator();
        while (it.hasNext()) {
            if (it.next().getName().equalsIgnoreCase(name.trim())) {
                it.remove();
                return "Dish removed successfully.";
            }
        }
        return "Dish not found.";
    }

    /**
     * Updates a dish's price.
     *
     * @param name     dish name to update
     * @param newPrice new price
     * @return status message
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
