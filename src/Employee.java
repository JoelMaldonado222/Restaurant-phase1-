public class Employee {
    private String name;
    private double hourlyRate;
    private double hoursWorked;

    public Employee(String name, double hourlyRate, double hoursWorked) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name cannot be null or empty");
        }
        if (!name.matches("[a-zA-Z ]+")) {
            throw new IllegalArgumentException("Employee name must contain only letters and spaces.");
        }
        if (hourlyRate < 0) {
            throw new IllegalArgumentException("Hourly rate cannot be negative");
        }
        if (hoursWorked < 0 || hoursWorked > 168) {
            throw new IllegalArgumentException("Hours worked must be between 0 and 168");
        }
        this.name = name.trim();
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    public String getName() {
        return name;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public double getWeeklyPay() {
        return hourlyRate * hoursWorked;
    }

    public String getDisplayString() {
        return String.format("%-15s | $%-8.2f | %4.1f hrs | Weekly Pay: $%.2f",
                name, hourlyRate, hoursWorked, getWeeklyPay());
    }

    public boolean setHourlyRate(double rate) {
        if (rate < 0) {
            return false;
        }
        this.hourlyRate = rate;
        return true;
    }

    public boolean setHoursWorked(double hours) {
        if (hours < 0 || hours > 168) {
            return false;
        }
        this.hoursWorked = hours;
        return true;
    }
}