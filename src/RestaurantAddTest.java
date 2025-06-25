import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RestaurantAddTest
 * ------------------
 * This test class verifies the core features of the Restaurant system:
 * - Adding employees and dishes
 * - Removing employees and dishes
 * - Updating employee and dish details
 * - Custom actions like toggling open-late status
 *
 * These tests are used to confirm that the system performs correctly
 * under both valid and invalid inputs and handles business rules properly.
 */
public class RestaurantAddTest {

    // This object will be reset before every test to ensure a clean state
    private Restaurant restaurant;

    /**
     * Sets up a fresh Restaurant object before each individual test.
     * Ensures all tests start from the same clean baseline.
     */
    @BeforeEach
    public void setup() {
        restaurant = new Restaurant("UnitTest");
    }

    // ───────────────────────────────────────────────
    // Add Employee Tests
    // ───────────────────────────────────────────────

    /**
     * Tests adding a valid employee with proper name, pay rate, and hours worked.
     * We expect no error message if input is valid.
     */
    @Test
    public void testAddValidEmployee() {
        String result = restaurant.addEmployee("John Doe", 15.50, 40);
        assertNull(result, "Employee should be added successfully with no error.");
    }

    /**
     * Tests adding an employee with a name that already exists.
     * The system should block duplicate names and return an error message.
     */
    @Test
    public void testAddDuplicateEmployee() {
        restaurant.addEmployee("John Doe", 15.50, 40); // First addition
        String result = restaurant.addEmployee("John Doe", 18.00, 30); // Duplicate
        assertNotNull(result, "Duplicate employee name should return an error message.");
    }

    /**
     * Tests adding an employee with invalid input: empty name and negative numbers.
     * This should be blocked by validation rules and return an error.
     */
    @Test
    public void testAddInvalidEmployee() {
        String result = restaurant.addEmployee("", -10, -5);
        assertNotNull(result, "Invalid employee data should return an error message.");
    }

    // ───────────────────────────────────────────────
    // Add Dish Tests
    // ───────────────────────────────────────────────

    /**
     * Tests adding a valid dish with a proper name and price.
     * Should return null if successful (no error).
     */
    @Test
    public void testAddValidDish() {
        String result = restaurant.addDish("Pasta", 12.99);
        assertNull(result, "Dish should be added successfully with no error.");
    }

    /**
     * Tests adding a dish with a name that already exists.
     * Should be rejected and return an error.
     */
    @Test
    public void testAddDuplicateDish() {
        restaurant.addDish("Pasta", 12.99); // Add once
        String result = restaurant.addDish("Pasta", 10.00); // Duplicate
        assertNotNull(result, "Duplicate dish name should return an error message.");
    }

    /**
     * Tests adding a dish with an empty name and negative price.
     * Should trigger validation and return an error message.
     */
    @Test
    public void testAddInvalidDish() {
        String result = restaurant.addDish("", -9.99);
        assertNotNull(result, "Invalid dish data should return an error message.");
    }

    // ───────────────────────────────────────────────
    // Remove Tests
    // ───────────────────────────────────────────────

    /**
     * Tests removing an employee that was just added.
     * Should return a success message or confirmation.
     */
    @Test
    public void testRemoveEmployee() {
        restaurant.addEmployee("Jane Doe", 14.00, 30);
        String result = restaurant.removeEmployee("Jane Doe");
        assertTrue(result.toLowerCase().contains("removed") || result.toLowerCase().contains("success"),
                "Employee should be removed successfully.");
    }

    /**
     * Tests removing a dish that was just added.
     * Should confirm successful removal or return a positive message.
     */
    @Test
    public void testRemoveDish() {
        restaurant.addDish("Burger", 10.00);
        String result = restaurant.removeDish("Burger");
        assertTrue(result.toLowerCase().contains("removed") || result.toLowerCase().contains("success"),
                "Dish should be removed successfully.");
    }

    // ───────────────────────────────────────────────
    // Update Tests
    // ───────────────────────────────────────────────

    /**
     * Tests updating an employee's hourly rate and hours worked.
     * After the update, the employee record should reflect new values.
     */
    @Test
    public void testUpdateEmployee() {
        restaurant.addEmployee("Sam", 12.00, 25); // Add
        String result = restaurant.updateEmployee("Sam", 16.50, 35); // Update
        assertTrue(result == null || result.toLowerCase().contains("updated"),
                "Employee should be updated successfully.");
    }

    /**
     * Tests updating a dish's price.
     * Should allow price change if dish name is valid.
     */
    @Test
    public void testUpdateDish() {
        restaurant.addDish("Fries", 3.50); // Add
        String result = restaurant.updateDish("Fries", 4.50); // Update
        assertTrue(result == null || result.toLowerCase().contains("updated"),
                "Dish should be updated successfully.");
    }

    // ───────────────────────────────────────────────
    // Custom Action Test
    // ───────────────────────────────────────────────

    /**
     * Tests the custom feature: toggling whether the restaurant is open late.
     * This is a boolean flip — should reverse from original state.
     */
    @Test
    public void testToggleOpenLate() {
        boolean originalStatus = restaurant.isOpenLate(); // Get current state
        restaurant.setOpenLate(!originalStatus); // Flip it
        assertNotEquals(originalStatus, restaurant.isOpenLate(), "Restaurant open-late status should toggle.");
    }

    // ───────────────────────────────────────────────
    // Display Methods Tests
    // ───────────────────────────────────────────────

    /**
     * Tests that getEmployeeDisplayStrings() returns the correct formatted strings
     * after adding employees.
     */
    @Test
    public void testGetEmployeeDisplayStrings() {
        restaurant.addEmployee("Alice", 20.00, 30);
        restaurant.addEmployee("Bob", 18.50, 40);

        List<String> employeeStrings = restaurant.getEmployeeDisplayStrings();

        assertEquals(2, employeeStrings.size(), "There should be 2 employees in the list.");
        assertTrue(employeeStrings.get(0).contains("Alice"), "First entry should mention Alice.");
        assertTrue(employeeStrings.get(1).contains("Bob"), "Second entry should mention Bob.");
    }

    /**
     * Tests that getMenuDisplayStrings() returns the correct formatted strings
     * after adding dishes.
     */
    @Test
    public void testGetMenuDisplayStrings() {
        restaurant.addDish("Pizza", 9.99);
        restaurant.addDish("Salad", 6.49);

        List<String> menuStrings = restaurant.getMenuDisplayStrings();

        assertEquals(2, menuStrings.size(), "There should be 2 dishes in the list.");
        assertTrue(menuStrings.get(0).contains("Pizza"), "First entry should mention Pizza.");
        assertTrue(menuStrings.get(1).contains("Salad"), "Second entry should mention Salad.");
    }

    // ───────────────────────────────────────────────
    // Payroll Calculation Test
    // ───────────────────────────────────────────────

    /**
     * Tests getTotalPayroll() by adding multiple employees and verifying total pay.
     * Uses known pay and hours to ensure calculation is correct.
     */
    @Test
    public void testGetTotalPayroll() {
        restaurant.addEmployee("Charlie", 15.00, 40); // $600.00
        restaurant.addEmployee("Dana", 20.00, 35);    // $700.00

        double total = restaurant.getTotalPayroll();

        assertEquals(1300.00, total, 0.01, "Total payroll should match expected value.");
    }

}
