// Core JDBC classes for managing database connectivity and statements
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// Used for user input in command-line based apps
import java.util.Scanner;

// Collections framework for managing and returning dynamic lists of records
import java.util.List;
import java.util.ArrayList;


/**
 * Manages all SQLite database interactions for the Restaurant DMS project.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Establish and close JDBC connections (via two overloads of connect).</li>
 *   <li>Initialize schema (create tables if absent).</li>
 *   <li>Fetch all records for GUI wiring.</li>
 *   <li>Perform CRUD operations on employees and dishes.</li>
 *   <li>Compute payroll reports directly from the database.</li>
 * </ul>
 * <p>
 * This class uses a single static Connection instance, which is reused
 * for all database operations and closed explicitly when no longer needed.
 *
 */
public class DatabaseManager {
    private static Connection connection;

    ////////////////////////////////////////////////////////////////////////////////
    // PHASE 4: CONNECTION METHODS
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Prompt-based connect (SQLite only). Loops until a valid file path is given.
     * Uses Scanner to read console input—intended for CLI testing.
     */
    public static void connect() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter SQLite database file name (e.g., restaurant.db): ");
            String userFile = scanner.nextLine().trim();
            if (userFile.isEmpty()) {
                System.out.println("⚠️  File name cannot be blank. Please try again.");
                continue;
            }
            try {
                connect(userFile);
                // If no exception, schema has been initialized
                System.out.println("✅ Connected and schema ensured for: " + userFile);
                break;
            } catch (SQLException e) {
                System.out.println("❌ Connection failed: " + e.getMessage());
                System.out.println("Please check the file path and try again.\n");
            }
        }
    }

    /**
     * Direct connect for a given SQLite file path (no reprompt).
     * Automatically initializes tables upon successful connection.
     *
     * @param filePath path to the .db file (must not be blank)
     * @throws SQLException if connection fails or path is invalid
     */
    public static void connect(String filePath) throws SQLException {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new SQLException("Database path cannot be empty.");
        }
        String dbUrl = "jdbc:sqlite:" + filePath.trim();
        connection = DriverManager.getConnection(dbUrl);
        initializeTables();
    }

    /**
     * Disconnects from the database if a connection is open.
     * Safe to call multiple times.
     */
    public static void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Disconnected from database.");
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error disconnecting: " + e.getMessage());
        }
    }

    /**
     * Retrieves the active JDBC Connection, or null if not connected.
     *
     * @return the Connection instance (or null)
     */
    public static Connection getConnection() {
        return connection;
    }

    ////////////////////////////////////////////////////////////////////////////////
    // PHASE 4: SCHEMA INITIALIZATION
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates the necessary database tables for employees and dishes
     * if they do not already exist in the connected SQLite database.
     *
     * <p>This method is executed automatically when {@code connect(String)} is called
     * to ensure the schema is initialized properly before any data operations occur.</p>
     *
     * <p>Tables Created:
     * <ul>
     *   <li><b>employees</b>: Includes id, name, hoursWorked, hourlyRate</li>
     *   <li><b>dishes</b>: Includes id, name, price</li>
     * </ul>
     * </p>
     */
    private static void initializeTables() {
        // SQL statement to create the employees table with auto-increment ID
        String createEmployeesTable = """
        CREATE TABLE IF NOT EXISTS employees (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            hoursWorked DOUBLE NOT NULL,
            hourlyRate DOUBLE NOT NULL
        );
    """;

        // SQL statement to create the dishes table with auto-increment ID
        String createDishesTable = """
        CREATE TABLE IF NOT EXISTS dishes (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            price DOUBLE NOT NULL
        );
    """;

        try (Statement stmt = connection.createStatement()) {
            // Execute both table creation statements
            stmt.execute(createEmployeesTable);
            stmt.execute(createDishesTable);
            System.out.println("✅ Tables created or already exist.");
        } catch (SQLException e) {
            // Log error if table creation fails
            System.out.println("❌ Error creating tables: " + e.getMessage());
        }
    }


    ////////////////////////////////////////////////////////////////////////////////
    // PHASE 4: DATA FETCHING (for GUI wiring)
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Fetches all employees from the database into a List of Employee objects.
     *
     * @return List of Employee instances populated from the 'employees' table
     * @throws SQLException if query execution fails
     */
    public static List<Employee> getAllEmployees() throws SQLException {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT id, name, hoursWorked, hourlyRate FROM employees";
        try (var stmt = connection.createStatement();
             var rs   = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Employee(
                        rs.getInt("id"),                   // <— now pulling the real PK
                        rs.getString("name"),
                        rs.getDouble("hourlyRate"),
                        rs.getDouble("hoursWorked")
                ));
            }

        }
        return list;
    }

    /**
     * Fetches all dishes from the database.
     *
     * @return List of Dish objects (with their database‐assigned IDs)
     * @throws SQLException if a database error occurs
     */
    public static List<Dish> getAllDishes() throws SQLException {
        List<Dish> list = new ArrayList<>();
        String sql = "SELECT id, name, price FROM dishes";

        try (
                var stmt = connection.createStatement();
                var rs   = stmt.executeQuery(sql)
        ) {
            // Loop over each row in the ResultSet and build a Dish
            while (rs.next()) {
                int    id    = rs.getInt("id");
                String name  = rs.getString("name");
                double price = rs.getDouble("price");

                // Use the id‐aware constructor
                list.add(new Dish(id, name, price));
            }
        }

        return list;
    }


    ////////////////////////////////////////////////////////////////////////////////
    // PHASE 4: CRUD OPERATIONS
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Inserts a new employee into the database.
     *
     * @param name        the employee's name
     * @param hoursWorked total hours worked
     * @param hourlyRate  hourly pay rate
     */
    public static void addEmployeeToDB(String name, double hoursWorked, double hourlyRate) {
        String sql = "INSERT INTO employees (name, hoursWorked, hourlyRate) VALUES (?, ?, ?)";
        try (var pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, hoursWorked);
            pstmt.setDouble(3, hourlyRate);
            pstmt.executeUpdate();
            System.out.println("✅ Employee added to database.");
        } catch (SQLException e) {
            System.out.println("❌ Failed to add employee: " + e.getMessage());
        }
    }

    /**
     * Inserts a new dish into the database.
     *
     * @param name  the dish name
     * @param price the dish price
     */
    public static void addDishToDB(String name, double price) {
        String sql = "INSERT INTO dishes (name, price) VALUES (?, ?)";
        try (var pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.executeUpdate();
            System.out.println("✅ Dish added to database.");
        } catch (SQLException e) {
            System.out.println("❌ Failed to add dish: " + e.getMessage());
        }
    }

    /**
     * Deletes an employee by their database row ID.
     *
     * @param id the ID of the employee to delete
     */
    public static void removeEmployeeFromDB(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (var pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected > 0
                    ? "✅ Employee removed from database."
                    : "⚠️ No employee found with that ID.");
        } catch (SQLException e) {
            System.out.println("❌ Failed to remove employee: " + e.getMessage());
        }
    }

    /**
     * Deletes a dish by its database row ID.
     *
     * @param id the ID of the dish to delete
     */
    public static void removeDishFromDB(int id) {
        String sql = "DELETE FROM dishes WHERE id = ?";
        try (var pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected > 0
                    ? "✅ Dish removed from database."
                    : "⚠️ No dish found with that ID.");
        } catch (SQLException e) {
            System.out.println("❌ Failed to remove dish: " + e.getMessage());
        }
    }

    /**
     * Updates a single column of an existing employee.
     *
     * @param id       the employee ID
     * @param field    the column name ("name", "hoursWorked", or "hourlyRate")
     * @param newValue the new value (parsed as DOUBLE if numeric)
     */
    public static void updateEmployeeInDB(int id, String field, String newValue) {
        String sql = "UPDATE employees SET " + field + " = ? WHERE id = ?";
        try (var pstmt = connection.prepareStatement(sql)) {
            switch (field) {
                case "name"        -> pstmt.setString(1, newValue);
                case "hoursWorked",
                     "hourlyRate"  -> pstmt.setDouble(1, Double.parseDouble(newValue));
                default -> {
                    System.out.println("❌ Invalid field: " + field);
                    return;
                }
            }
            pstmt.setInt(2, id);
            int rows = pstmt.executeUpdate();
            System.out.println(rows > 0
                    ? "✅ Employee updated."
                    : "⚠️ No employee found with that ID.");
        } catch (SQLException | NumberFormatException e) {
            System.out.println("❌ Failed to update employee: " + e.getMessage());
        }
    }

    /**
     * Updates a single column of a dish in the SQLite database.
     *
     * <p>This method dynamically builds and executes an SQL update statement
     * to modify either the {@code name} or {@code price} field of a dish,
     * using the provided dish ID and new value.</p>
     *
     * @param id       the unique ID of the dish to update
     * @param field    the column name to update ("name" or "price")
     * @param newValue the new value to apply; parsed as {@code double} if updating price
     */
    public static void updateDishInDB(int id, String field, String newValue) {
        // Build SQL query using placeholder for value and ID
        String sql = "UPDATE dishes SET " + field + " = ? WHERE id = ?";

        try (var pstmt = connection.prepareStatement(sql)) {

            // Handle field-specific value parsing
            if ("name".equals(field)) {
                pstmt.setString(1, newValue); // Set name directly
            } else if ("price".equals(field)) {
                pstmt.setDouble(1, Double.parseDouble(newValue)); // Parse and set price
            } else {
                // Exit early if an invalid column is specified
                System.out.println("❌ Invalid field: " + field);
                return;
            }

            // Set the ID parameter
            pstmt.setInt(2, id);

            // Execute the update and check rows affected
            int rows = pstmt.executeUpdate();
            System.out.println(rows > 0
                    ? "✅ Dish updated."
                    : "⚠️ No dish found with that ID.");

        } catch (SQLException | NumberFormatException e) {
            // Handle SQL or parsing errors
            System.out.println("❌ Failed to update dish: " + e.getMessage());
        }
    }

    /**
     * Generates and prints a payroll report using employee data from the database.
     *
     * <p>For each employee, this method retrieves their name, hours worked, and
     * hourly rate, calculates their weekly pay, and prints the results to the console.
     * At the end, it prints the total payroll for all employees.</p>
     */
    public static void calculateTotalPayrollFromDB() {
        // SQL query to retrieve payroll-related fields
        String sql = "SELECT name, hoursWorked, hourlyRate FROM employees";

        double totalPayroll = 0.0; // Accumulator for total payroll

        try (
                var stmt = connection.createStatement();
                var rs = stmt.executeQuery(sql)
        ) {
            // Print report header
            System.out.println("📋 Payroll Report:");
            System.out.println("----------------------------");

            // Process each employee's data
            while (rs.next()) {
                String name = rs.getString("name");
                double hrs = rs.getDouble("hoursWorked");
                double rate = rs.getDouble("hourlyRate");
                double pay = hrs * rate; // Calculate weekly pay

                totalPayroll += pay;

                // Output formatted employee payroll info
                System.out.printf("👤 %s | Hours: %.2f | Rate: $%.2f | Pay: $%.2f%n",
                        name, hrs, rate, pay);
            }

            // Print report footer with total payroll
            System.out.println("----------------------------");
            System.out.printf("🧾 Total Payroll: $%.2f%n", totalPayroll);

        } catch (SQLException e) {
            // Handle query execution errors
            System.out.println("❌ Failed to calculate payroll: " + e.getMessage());
        }
    }}
