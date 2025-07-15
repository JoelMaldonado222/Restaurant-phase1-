/**
 * Represents a single menu item (dish) in the restaurant management system.
 * This class encapsulates all dish-related data and business logic including
 * the dish name, pricing information, display formatting, and database ID.
 *
 * The Dish class enforces business rules such as:
 * - Dish IDs are assigned by the database (AUTOINCREMENT).
 * - Dish names cannot be null, empty, or contain only whitespace.
 * - Prices must be non-negative (free items are allowed with $0.00 price).
 *
 * This class follows encapsulation principles by keeping all fields private
 * and providing controlled access through getter and setter methods with
 * appropriate validation to maintain data integrity.
 *
 * @author Your Name
 * @since 2024
 */
public class Dish {
    // ========================================
    // INSTANCE VARIABLES
    // ========================================

    /**
     * The unique database ID for this dish.
     * Assigned by the SQLite AUTOINCREMENT mechanism.
     * A value of 0 indicates "not yet persisted" (in-memory only).
     */
    private final int id;                  // holds the database primary key for this dish

    /**
     * The name of the dish as it appears on the menu.
     * Stored as a trimmed string to ensure consistency.
     */
    private String name;                   // user-facing dish name

    /**
     * The price of the dish in dollars.
     * Must be non-negative to prevent pricing errors.
     */
    private double price;                  // pricing information

    // ========================================
    // CONSTRUCTORS
    // ========================================

    /**
     * Constructs a new Dish instance for in-memory use only.
     * Assigns a default ID of 0 (not yet persisted).
     *
     * @param name  The dish name (will be trimmed; cannot be null or empty)
     * @param price The price in dollars (must be >= 0; zero allowed)
     * @throws IllegalArgumentException if name is invalid or price is negative
     */
    public Dish(String name, double price) {
        this(0, name, price);             // delegate to main constructor with id=0
    }

    /**
     * Constructs a new Dish instance with a specific database ID.
     * Use this when loading from or syncing with the database.
     *
     * @param id    The database ID (AUTOINCREMENT value; must be >= 0)
     * @param name  The dish name (will be trimmed; cannot be null or empty)
     * @param price The price in dollars (must be >= 0; zero allowed)
     * @throws IllegalArgumentException if id < 0, name is invalid, or price is negative
     */
    public Dish(int id, String name, double price) {
        if (id < 0) {                      // validate that ID is non-negative
            throw new IllegalArgumentException("Dish ID cannot be negative");
        }
        if (name == null || name.trim().isEmpty()) {  // validate name
            throw new IllegalArgumentException("Dish name cannot be null or empty");
        }
        if (price < 0) {                   // validate price
            throw new IllegalArgumentException("Price cannot be negative");
        }

        this.id = id;                      // assign validated ID
        this.name = name.trim();          // assign trimmed name
        this.price = price;               // assign validated price
    }

    // ========================================
    // GETTERS
    // ========================================

    /**
     * Retrieves the database‑assigned ID for this dish.
     *
     * @return the dish’s ID (0 if not yet persisted)
     */
    public int getId() {
        return id;                        // return primary key
    }

    /**
     * Retrieves the dish’s name.
     *
     * @return the trimmed, non-null name of the dish
     */
    public String getName() {
        return name;                      // return current name
    }

    /**
     * Retrieves the dish’s price.
     *
     * @return the non-negative price in dollars
     */
    public double getPrice() {
        return price;                     // return current price
    }

    // ========================================
    // SETTERS
    // ========================================

    /**
     * Updates the dish’s price with validation.
     * Enforces the non-negative business rule.
     *
     * @param price the new price in dollars (must be >= 0)
     * @return true if update succeeded; false if validation failed
     */
    public boolean setPrice(double price) {
        if (price < 0) {                  // reject negative prices
            return false;                 // indicate failure
        }
        this.price = price;               // assign new price
        return true;                      // indicate success
    }

    // ========================================
    // DISPLAY/FORMAT
    // ========================================

    /**
     * Generates a formatted string representation of the dish for display.
     * Format: [ID] Name (20-char left-align) | $Price (6-char left-align, 2 decimals)
     *
     * Example: "[12] Grilled Salmon      | $18.99"
     *
     * @return a string suitable for menu listings or console output
     */
    public String getDisplayString() {
        return String.format(
                "[%d] %-20s | $%-6.2f",
                id,                             // include ID in brackets
                name,                           // left-align name in 20-char field
                price                           // left-align price with 2 decimal places
        );
    }
}
