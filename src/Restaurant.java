import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Restaurant {
    private String name;
    private boolean isOpenLate;
    private ArrayList<Employee> employees;
    private ArrayList<Dish> menu;

    public Restaurant(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant name cannot be null or empty");
        }
        this.name = name.trim();
        this.employees = new ArrayList<>();
        this.menu = new ArrayList<>();
        this.isOpenLate = false;
    }

    public String getName() {
        return name;
    }

    public boolean isOpenLate() {
        return isOpenLate;
    }

    public void setOpenLate(boolean openLate) {
        this.isOpenLate = openLate;
    }

    // Returns null if successful, or an error message if failed.
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

    // Now returns String, not boolean!
    public String addDish(String name, double price) {
        try {
            Dish d = new Dish(name, price);
            for (Dish existing : menu) {
                if (existing.getName().equalsIgnoreCase(d.getName())) {
                    return "Dish with name '" + d.getName() + "' already exists.";
                }
            }
            menu.add(d);
            return null;
        } catch (IllegalArgumentException ex) {
            return ex.getMessage();
        }
    }

    public double getTotalPayroll() {
        double total = 0;
        for (Employee e : employees) {
            total += e.getWeeklyPay();
        }
        return total;
    }

    // Now returns String, not boolean!
    public String removeEmployee(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Name cannot be null or empty.";
        }
        Iterator<Employee> iterator = employees.iterator();
        while (iterator.hasNext()) {
            Employee e = iterator.next();
            if (e.getName().equalsIgnoreCase(name.trim())) {
                iterator.remove();
                return "Employee removed successfully.";
            }
        }
        return "Employee not found.";
    }

    // Now returns String, not boolean!
    public String updateEmployee(String name, double newRate, double newHours) {
        if (name == null || name.trim().isEmpty()) {
            return "Name cannot be null or empty.";
        }
        for (Employee e : employees) {
            if (e.getName().equalsIgnoreCase(name.trim())) {
                boolean rateUpdated = e.setHourlyRate(newRate);
                boolean hoursUpdated = e.setHoursWorked(newHours);
                if (rateUpdated && hoursUpdated) {
                    return "Employee updated successfully.";
                } else {
                    return "Employee update completed with some validation errors.";
                }
            }
        }
        return "Employee not found.";
    }

    public List<String> getEmployeeDisplayStrings() {
        List<String> employeeStrings = new ArrayList<>();
        for (Employee e : employees) {
            employeeStrings.add(e.getDisplayString());
        }
        return employeeStrings;
    }

    // Now returns String, not boolean!
    public String removeDish(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Name cannot be null or empty.";
        }
        Iterator<Dish> iterator = menu.iterator();
        while (iterator.hasNext()) {
            Dish d = iterator.next();
            if (d.getName().equalsIgnoreCase(name.trim())) {
                iterator.remove();
                return "Dish removed successfully.";
            }
        }
        return "Dish not found.";
    }

    // Now returns String, not boolean!
    public String updateDish(String name, double newPrice) {
        if (name == null || name.trim().isEmpty()) {
            return "Name cannot be null or empty.";
        }
        for (Dish d : menu) {
            if (d.getName().equalsIgnoreCase(name.trim())) {
                if (d.setPrice(newPrice)) {
                    return "Dish updated successfully.";
                } else {
                    return "Dish update failed due to validation error.";
                }
            }
        }
        return "Dish not found.";
    }

    public List<String> getMenuDisplayStrings() {
        List<String> menuStrings = new ArrayList<>();
        for (Dish d : menu) {
            menuStrings.add(d.getDisplayString());
        }
        return menuStrings;
    }
}