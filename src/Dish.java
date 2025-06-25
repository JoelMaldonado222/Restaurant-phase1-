// Represents a single menu item (dish) in the restaurant management system.
// This class encapsulates all dish-related data and business logic including
// the dish name, pricing information, and display formatting.
//
// The Dish class enforces business rules such as:
// - Dish names cannot be null, empty, or contain only whitespace
// - Prices must be non-negative (free items are allowed with $0.00 price)
// - Consistent formatting for menu display purposes
//
// This class follows encapsulation principles by keeping all fields private
// and providing controlled access through getter and setter methods with
// appropriate validation to maintain data integrity.
public class Dish {
    // The name of the dish as it appears on the menu - stored as trimmed string to ensure consistency
    private String name;

    // The price of the dish in dollars - must be non-negative to prevent pricing errors
    private double price;

    // Constructor that creates a new Dish instance with comprehensive input validation.
    // This constructor enforces all business rules to ensure data integrity and
    // prevents the creation of Dish objects with invalid data that could cause
    // problems in menu displays or pricing calculations.
    //
    // Validation rules enforced:
    // - Name cannot be null, empty, or contain only whitespace (ensures menu readability)
    // - Price must be non-negative (prevents pricing errors, allows free items at $0.00)
    //
    // Parameters:
    // - name: The dish name as it should appear on the menu (will be trimmed of whitespace)
    // - price: The price in dollars (must be >= 0, zero allowed for promotional items)
    //
    // Throws IllegalArgumentException if any parameter fails validation rules
    public Dish(String name, double price) {
        // Validate name is not null or empty to prevent menu items without proper identification
        // This ensures every dish has a displayable name for customer-facing menus
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Dish name cannot be null or empty");
        }

        // Validate price is non-negative to prevent pricing errors and maintain business logic
        // Zero is explicitly allowed to support promotional items, free samples, or complimentary dishes
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        // Assign validated values to instance variables after all validation passes
        // Trim the name to remove leading/trailing whitespace for consistent menu formatting
        this.name = name.trim();
        this.price = price;
    }

    // Retrieves the dish's name.
    // This getter method provides read-only access to the dish's name which was
    // validated and trimmed during construction to ensure it's never null or empty.
    //
    // Returns: String containing the dish name (trimmed, never null or empty)
    public String getName() {
        return name;
    }

    // Retrieves the dish's price.
    // This getter method provides read-only access to the dish's price which is
    // guaranteed to be non-negative due to constructor and setter validation.
    //
    // Returns: double representing the price in dollars (>= 0)
    public double getPrice() {
        return price;
    }

    // Updates the dish's price with validation to maintain data integrity.
    // This setter method allows modification of the price while enforcing the
    // non-negative price business rule to prevent pricing errors.
    //
    // Unlike the constructor, this method uses return values instead of exceptions
    // to indicate success/failure, making it more suitable for user input scenarios
    // where graceful error handling is preferred over program termination.
    //
    // Parameters:
    // - price: The new price in dollars (must be >= 0)
    //
    // Returns: boolean true if the price was successfully updated, false if validation failed
    public boolean setPrice(double price) {
        // Validate the new price meets business rules (non-negative)
        // This prevents accidental negative pricing that could cause financial losses
        if (price < 0) {
            // Return false to indicate validation failure without throwing exception
            // This allows calling code to handle the error gracefully
            return false;
        }

        // Update the price since validation passed
        this.price = price;

        // Return true to indicate successful update
        return true;
    }

    // Generates a formatted string representation of the dish for display purposes.
    // This method creates a consistently formatted table row that aligns with the
    // table headers used in the RestaurantApp menu display methods.
    //
    // The format includes:
    // - Dish name (left-aligned, 20 characters wide to accommodate longer dish names)
    // - Price (currency format with $ symbol, left-aligned, 6 characters wide with 2 decimal places)
    //
    // Example output: "Grilled Salmon      | $18.95"
    // Example output: "Coffee              | $2.50 "
    //
    // Returns: String containing formatted dish information suitable for console menu display
    public String getDisplayString() {
        // Use String.format for precise formatting control to ensure consistent table alignment
        // %-20s: left-align dish name in 20-character field (handles long dish names)
        // $%-6.2f: left-align currency with $ symbol and 2 decimal places in 6-character field
        // The 2 decimal places ensure proper currency formatting (e.g., $5.00 not $5)
        return String.format("%-20s | $%-6.2f", name, price);
    }
}