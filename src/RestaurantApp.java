import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.List;

public class RestaurantApp {
    private Restaurant restaurant;
    private Scanner scanner;

    public RestaurantApp() {
        restaurant = new Restaurant("Emery's");
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        new RestaurantApp().run();
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println(getMenuText());
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    System.out.println(readDataFromFile());
                    break;
                case "2":
                    for (String line : getDisplayAllLines()) {
                        System.out.println(line);
                    }
                    break;
                case "3":
                    System.out.println(addRecord());
                    break;
                case "4":
                    System.out.println(removeRecord());
                    break;
                case "5":
                    System.out.println(updateRecord());
                    break;
                case "6":
                    System.out.println(getTotalPayrollText());
                    break;
                case "7":
                    running = false;
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                case "8":
                    restaurant.setOpenLate(!restaurant.isOpenLate());
                    System.out.println("Open late is now set to: " + (restaurant.isOpenLate() ? "✅" : "❌"));
                    break;
                default:
                    System.out.println("Invalid option. Please select 1–8.");
            }
        }
        scanner.close();
    }

    public String getMenuText() {
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

    public List<String> getDisplayAllLines() {
        List<String> lines = new java.util.ArrayList<>();
        lines.add("Restaurant: " + restaurant.getName());
        lines.add("Status: " + (restaurant.isOpenLate() ? "Open Late ✅" : "Closes Early ❌"));
        lines.add("------------------------------------");

        List<String> employees = restaurant.getEmployeeDisplayStrings();
        if (employees.isEmpty()) {
            lines.add("No employees to display.");
        } else {
            lines.add("\nEmployees:");
            lines.add("Name            | Rate     | Hours | Weekly Pay");
            lines.add("----------------|----------|-------|------------");
            lines.addAll(employees);
        }

        List<String> menu = restaurant.getMenuDisplayStrings();
        if (menu.isEmpty()) {
            lines.add("Menu is empty.");
        } else {
            lines.add("\nMenu:");
            lines.add("Dish Name           | Price");
            lines.add("--------------------|--------");
            lines.addAll(menu);
        }
        return lines;
    }

    public String readDataFromFile() {
        System.out.print("Enter file name to load data from: ");
        String filename = scanner.nextLine().trim();

        if (filename.isEmpty() || filename.contains("..") || filename.contains("/") ||
                filename.contains("\\") || filename.contains(":")) {
            return "Invalid filename. Please use a simple filename without path separators.";
        }

        File file = new File(filename);
        if (!file.exists()) {
            return "File not found. Please try again.";
        }

        int successCount = 0;
        int errorCount = 0;

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",");
                boolean success = false;
                if (parts.length == 3) {
                    String name = parts[0].trim();
                    double rate = Double.parseDouble(parts[1].trim());
                    double hours = Double.parseDouble(parts[2].trim());
                    String error = restaurant.addEmployee(name, rate, hours);
                    if (error == null) {
                        success = true;
                    } else {
                        System.out.println("Failed to add employee: " + error);
                        success = false;
                    }
                } else if (parts.length == 2) {
                    String name = parts[0].trim();
                    double price = Double.parseDouble(parts[1].trim());
                    String error = restaurant.addDish(name, price);
                    if (error == null) {
                        success = true;
                    } else {
                        System.out.println("Failed to add dish: " + error);
                        success = false;
                    }
                } else {
                    System.out.println("Invalid line format: " + line);
                    success = false;
                }
                if (success) successCount++; else errorCount++;
            }
            return String.format("Data loading complete: %d successful, %d errors", successCount, errorCount);
        } catch (FileNotFoundException | NumberFormatException e) {
            return "Error reading file: " + e.getMessage();
        }
    }

    public String addRecord() {
        System.out.println("Add (1) Employee or (2) Dish?");
        String type = scanner.nextLine().trim();

        if ("1".equals(type)) {
            try {
                System.out.print("Employee name: ");
                String name = scanner.nextLine().trim();
                if (name.isEmpty() || !name.matches("[a-zA-Z ]+")) {
                    return "Name must contain only letters and spaces, and cannot be empty.";
                }
                System.out.print("Hourly rate: ");
                double rate = Double.parseDouble(scanner.nextLine().trim());
                System.out.print("Hours worked: ");
                double hours = Double.parseDouble(scanner.nextLine().trim());
                String error = restaurant.addEmployee(name, rate, hours);
                if (error != null) {
                    return "Failed to add employee: " + error;
                }
                return "Employee added successfully.";
            } catch (NumberFormatException e) {
                return "Invalid input; please enter numbers for rate/hours.";
            }
        } else if ("2".equals(type)) {
            try {
                System.out.print("Dish name: ");
                String dish = scanner.nextLine().trim();
                if (dish.isEmpty()) {
                    return "Dish name cannot be empty.";
                }
                System.out.print("Price: ");
                double price = Double.parseDouble(scanner.nextLine().trim());
                String error = restaurant.addDish(dish, price);
                if (error != null) {
                    return "Failed to add dish: " + error;
                }
                return "Dish added successfully.";
            } catch (NumberFormatException e) {
                return "Invalid input; please enter a number for price.";
            }
        } else {
            return "Invalid selection.";
        }
    }

    public String removeRecord() {
        System.out.println("Remove (1) Employee or (2) Dish?");
        String type = scanner.nextLine().trim();

        if ("1".equals(type)) {
            System.out.print("Employee name to remove: ");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) {
                return restaurant.removeEmployee(name);
            } else {
                return "Name cannot be empty.";
            }
        } else if ("2".equals(type)) {
            System.out.print("Dish name to remove: ");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) {
                return restaurant.removeDish(name);
            } else {
                return "Name cannot be empty.";
            }
        } else {
            return "Invalid selection.";
        }
    }

    public String updateRecord() {
        System.out.println("Update (1) Employee or (2) Dish?");
        String type = scanner.nextLine().trim();

        if ("1".equals(type)) {
            System.out.print("Enter employee name to update: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty() || !name.matches("[a-zA-Z ]+")) {
                return "Name must contain only letters and spaces, and cannot be empty.";
            }
            try {
                System.out.print("New hourly rate: ");
                double newRate = Double.parseDouble(scanner.nextLine().trim());
                System.out.print("New hours worked: ");
                double newHours = Double.parseDouble(scanner.nextLine().trim());
                return restaurant.updateEmployee(name, newRate, newHours);
            } catch (NumberFormatException e) {
                return "Invalid input; please enter numeric values.";
            }
        } else if ("2".equals(type)) {
            System.out.print("Enter dish name to update: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                return "Dish name cannot be empty.";
            }
            try {
                System.out.print("New price: ");
                double newPrice = Double.parseDouble(scanner.nextLine().trim());
                return restaurant.updateDish(name, newPrice);
            } catch (NumberFormatException e) {
                return "Invalid input; please enter a number for price.";
            }
        } else {
            return "Invalid selection.";
        }
    }

    public String getTotalPayrollText() {
        double total = restaurant.getTotalPayroll();
        return String.format("Total weekly payroll: $%.2f", total);
    }
}