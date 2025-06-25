import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// This class tests the add functionality for both employees and dishes in the Restaurant class
public class RestaurantAddTest {

    private Restaurant restaurant;

    // This method runs before each test to reset the Restaurant object
    @BeforeEach
    public void setup() {
        restaurant = new Restaurant("UnitTest "); // Set up a fresh restaurant for each test
    }

    // Test: Adding a valid employee should succeed (no error message)
    @Test
    public void testAddValidEmployee() {
        String result = restaurant.addEmployee("John Doe", 15.50, 40);
        assertNull(result, "Employee should be added successfully with no error.");
    }

    // Test: Adding a duplicate employee (same name) should return an error
    @Test
    public void testAddDuplicateEmployee() {
        restaurant.addEmployee("John Doe", 15.50, 40); // Add first time
        String result = restaurant.addEmployee("John Doe", 18.00, 30); // Try again with same name
        assertNotNull(result, "Duplicate employee name should return an error message.");
    }

    // Test: Adding an employee with invalid values should return an error
    @Test
    public void testAddInvalidEmployee() {
        String result = restaurant.addEmployee("", -10, -5); // Invalid name and numbers
        assertNotNull(result, "Invalid employee data should return an error message.");
    }

    // Test: Adding a valid dish should succeed
    @Test
    public void testAddValidDish() {
        String result = restaurant.addDish("Pasta", 12.99);
        assertNull(result, "Dish should be added successfully with no error.");
    }

    // Test: Adding a dish with the same name twice should return an error
    @Test
    public void testAddDuplicateDish() {
        restaurant.addDish("Pasta", 12.99); // Add first time
        String result = restaurant.addDish("Pasta", 10.00); // Try again with same name
        assertNotNull(result, "Duplicate dish name should return an error message.");
    }

    // Test: Adding a dish with invalid name and price should return an error
    @Test
    public void testAddInvalidDish() {
        String result = restaurant.addDish("", -9.99); // Empty name and negative price
        assertNotNull(result, "Invalid dish data should return an error message.");
    }
}
