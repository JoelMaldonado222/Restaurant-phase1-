/**
 * Represents an individual employee within the restaurant management system.
 * This class encapsulates all employee-related data and business logic including
 * personal information, compensation details, and work schedule information.
 *
 * The Employee class enforces business rules such as:
 * - Name validation (letters and spaces only, cannot be empty)
 * - Non-negative hourly rates (prevents data entry errors)
 * - Realistic hours worked limits (0-168 hours per week maximum)
 * - Automatic weekly pay calculations based on rate and hours
 *
 * This class follows the principle of data encapsulation by keeping all fields
 * private and providing controlled access through getter and setter methods
 * with appropriate validation.
 */
public class Employee {
    /**
     * Unique identifier for this employee record (database primary key).
     */
    private final int id;

    /**
     * Employee's full name - stored as trimmed string, validated to contain only letters and spaces.
     */
    private String name;

    /**
     * Employee's hourly wage rate in dollars - must be non-negative to prevent payroll errors.
     */
    private double hourlyRate;

    /**
     * Number of hours the employee worked in the current week - limited to 0-168 hours (24*7).
     */
    private double hoursWorked;

    /**
     * Constructs a new Employee instance with comprehensive input validation.
     * This constructor enforces all business rules to ensure data integrity
     * and prevents the creation of Employee objects with invalid data.
     *
     * Validation rules enforced:
     * - Name cannot be null, empty, or contain only whitespace
     * - Name must contain only alphabetic characters and spaces (no numbers/symbols)
     * - Hourly rate must be non-negative (zero is allowed for unpaid positions)
     * - Hours worked must be between 0 and 168 (maximum possible hours in a week)
     *
     * @param id          The unique database ID (primary key) for this employee record.
     * @param name        The employee's full name (will be trimmed of whitespace)
     * @param hourlyRate  The employee's hourly wage rate in dollars (must be >= 0)
     * @param hoursWorked The number of hours worked this week (must be 0-168)
     * @throws IllegalArgumentException if any parameter fails validation rules
     */
    public Employee(int id, String name, double hourlyRate, double hoursWorked) {
        // Validate name is not null or empty to prevent creation of employees without proper identification
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name cannot be null or empty");
        }

        // Validate name format to ensure consistency and prevent data entry errors
        if (!name.matches("[a-zA-Z ]+")) {
            throw new IllegalArgumentException("Employee name must contain only letters and spaces.");
        }

        // Validate hourly rate is non-negative to prevent payroll calculation errors
        if (hourlyRate < 0) {
            throw new IllegalArgumentException("Hourly rate cannot be negative");
        }

        // Validate hours worked is within realistic bounds for a weekly schedule
        if (hoursWorked < 0 || hoursWorked > 168) {
            throw new IllegalArgumentException("Hours worked must be between 0 and 168");
        }

        // Assign validated values
        this.id = id;                   // now storing the database ID
        this.name = name.trim();       // Remove leading/trailing whitespace
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    /**
     * Convenience constructor for creating a new Employee before database insertion.
     * The ID will be assigned by the database upon insertion. In-memory usage defaults ID to 0.
     *
     * @param name        The employee's full name
     * @param hourlyRate  The employee's hourly wage rate
     * @param hoursWorked The number of hours worked this week
     */
    public Employee(String name, double hourlyRate, double hoursWorked) {
        this(0, name, hourlyRate, hoursWorked);
    }

    /**
     * Retrieves the employee record ID.
     *
     * @return int representing the unique database ID (primary key)
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves the employee's name.
     *
     * @return String containing the employee's full name
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the employee's hourly wage rate.
     *
     * @return double representing the hourly wage rate in dollars
     */
    public double getHourlyRate() {
        return hourlyRate;
    }

    /**
     * Retrieves the number of hours the employee worked this week.
     *
     * @return double representing hours worked (0-168 range)
     */
    public double getHoursWorked() {
        return hoursWorked;
    }

    /**
     * Calculates and returns the employee's total weekly pay based on hourly rate and hours worked.
     *
     * @return double representing the total weekly gross pay in dollars
     */
    public double getWeeklyPay() {
        return hourlyRate * hoursWorked;
    }

    /**
     * Generates a formatted string representation suitable for GUI or console display.
     *
     * @return formatted String with name, rate, hours, and pay
     */
    public String getDisplayString() {
        return String.format("%d | %-15s | $%-8.2f | %4.1f hrs | Weekly Pay: $%.2f",
                id, name, hourlyRate, hoursWorked, getWeeklyPay());
    }

    /**
     * Updates the employee's hourly rate with validation.
     *
     * @param rate The new hourly rate in dollars (must be >= 0)
     * @return boolean true if update succeeded, false otherwise
     */
    public boolean setHourlyRate(double rate) {
        if (rate < 0) return false;
        this.hourlyRate = rate;
        return true;
    }

    /**
     * Updates the employee's hours worked with validation.
     *
     * @param hours The new number of hours worked (must be 0-168)
     * @return boolean true if update succeeded, false otherwise
     */
    public boolean setHoursWorked(double hours) {
        if (hours < 0 || hours > 168) return false;
        this.hoursWorked = hours;
        return true;
    }
}
