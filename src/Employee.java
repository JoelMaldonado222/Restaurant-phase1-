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
    // Employee's full name - stored as trimmed string, validated to contain only letters and spaces
    private String name;

    // Employee's hourly wage rate in dollars - must be non-negative to prevent payroll errors
    private double hourlyRate;

    // Number of hours the employee worked in the current week - limited to 0-168 hours (24*7)
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
     * @param name The employee's full name (will be trimmed of whitespace)
     * @param hourlyRate The employee's hourly wage rate in dollars (must be >= 0)
     * @param hoursWorked The number of hours worked this week (must be 0-168)
     * @throws IllegalArgumentException if any parameter fails validation rules
     */
    public Employee(String name, double hourlyRate, double hoursWorked) {
        // Validate name is not null or empty to prevent creation of employees without proper identification
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name cannot be null or empty");
        }

        // Validate name format to ensure consistency and prevent data entry errors
        // Only allows letters (a-z, A-Z) and spaces to maintain professional name standards
        if (!name.matches("[a-zA-Z ]+")) {
            throw new IllegalArgumentException("Employee name must contain only letters and spaces.");
        }

        // Validate hourly rate is non-negative to prevent payroll calculation errors
        // Zero is allowed to support unpaid positions (interns, volunteers, etc.)
        if (hourlyRate < 0) {
            throw new IllegalArgumentException("Hourly rate cannot be negative");
        }

        // Validate hours worked is within realistic bounds for a weekly schedule
        // 168 hours = 24 hours/day * 7 days/week (theoretical maximum)
        // This prevents data entry errors and unrealistic schedules
        if (hoursWorked < 0 || hoursWorked > 168) {
            throw new IllegalArgumentException("Hours worked must be between 0 and 168");
        }

        // Assign validated values to instance variables
        // Trim the name to remove any leading/trailing whitespace for consistency
        this.name = name.trim();
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    /**
     * Retrieves the employee's name.
     * This getter method provides read-only access to the employee's name
     * which was validated and trimmed during construction.
     *
     * @return String containing the employee's full name (trimmed, letters and spaces only)
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the employee's hourly wage rate.
     * This getter method provides read-only access to the employee's compensation rate
     * which is guaranteed to be non-negative due to constructor validation.
     *
     * @return double representing the hourly wage rate in dollars (>= 0)
     */
    public double getHourlyRate() {
        return hourlyRate;
    }

    /**
     * Retrieves the number of hours the employee worked this week.
     * This getter method provides read-only access to the employee's work schedule
     * which is guaranteed to be within 0-168 hours due to validation.
     *
     * @return double representing hours worked this week (0-168 range)
     */
    public double getHoursWorked() {
        return hoursWorked;
    }

    /**
     * Calculates and returns the employee's total weekly pay based on hourly rate and hours worked.
     * This method performs the core payroll calculation by multiplying the validated
     * hourly rate by the validated hours worked. No additional validation is needed
     * since both values are guaranteed to be valid from construction and setter methods.
     *
     * Formula: Weekly Pay = Hourly Rate × Hours Worked
     *
     * Note: This is a simple calculation that doesn't account for overtime rates,
     * taxes, or other payroll complexities that might exist in a real payroll system.
     *
     * @return double representing the total weekly gross pay in dollars
     */
    public double getWeeklyPay() {
        // Simple multiplication - no validation needed since both values are already validated
        return hourlyRate * hoursWorked;
    }

    /**
     * Generates a formatted string representation of the employee for display purposes.
     * This method creates a consistently formatted table row that aligns with the
     * table headers used in the RestaurantApp display methods.
     *
     * The format includes:
     * - Name (left-aligned, 15 characters wide)
     * - Hourly rate (currency format with $ symbol, 8 characters wide)
     * - Hours worked (decimal format with 1 decimal place, right-aligned)
     * - Weekly pay (currency format with $ symbol and 2 decimal places)
     *
     * Example output: "John Smith      | $15.50   |  40.0 hrs | Weekly Pay: $620.00"
     *
     * @return String containing formatted employee information suitable for console display
     */
    public String getDisplayString() {
        // Use String.format for precise formatting control to ensure consistent table alignment
        // %-15s: left-align name in 15-character field
        // $%-8.2f: left-align currency with 2 decimal places in 8-character field
        // %4.1f: right-align hours with 1 decimal place in 4-character field
        // %.2f: format weekly pay with 2 decimal places for currency display
        return String.format("%-15s | $%-8.2f | %4.1f hrs | Weekly Pay: $%.2f",
                name, hourlyRate, hoursWorked, getWeeklyPay());
    }

    /**
     * Updates the employee's hourly rate with validation.
     * This setter method allows modification of the hourly rate while maintaining
     * data integrity by enforcing the non-negative rate business rule.
     *
     * Unlike the constructor, this method uses return values instead of exceptions
     * to indicate success/failure, making it more suitable for user input scenarios
     * where graceful error handling is preferred over program termination.
     *
     * @param rate The new hourly rate in dollars (must be >= 0)
     * @return boolean true if the rate was successfully updated, false if validation failed
     */
    public boolean setHourlyRate(double rate) {
        // Validate the new rate meets business rules (non-negative)
        if (rate < 0) {
            // Return false to indicate validation failure without throwing exception
            return false;
        }

        // Update the hourly rate since validation passed
        this.hourlyRate = rate;

        // Return true to indicate successful update
        return true;
    }

    /**
     * Updates the employee's hours worked with validation.
     * This setter method allows modification of the hours worked while maintaining
     * data integrity by enforcing the 0-168 hours per week business rule.
     *
     * Like setHourlyRate, this method uses return values for error indication
     * rather than exceptions, providing more flexible error handling for
     * user interface scenarios.
     *
     * @param hours The new number of hours worked this week (must be 0-168)
     * @return boolean true if the hours were successfully updated, false if validation failed
     */
    public boolean setHoursWorked(double hours) {
        // Validate the new hours value meets business rules (0-168 range)
        if (hours < 0 || hours > 168) {
            // Return false to indicate validation failure without throwing exception
            return false;
        }

        // Update the hours worked since validation passed
        this.hoursWorked = hours;

        // Return true to indicate successful update
        return true;
    }
}