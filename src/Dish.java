public class Dish {
    private String name;
    private double price;

    public Dish(String name, double price) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Dish name cannot be null or empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.name = name.trim();
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public boolean setPrice(double price) {
        if (price < 0) {
            return false;
        }
        this.price = price;
        return true;
    }

    public String getDisplayString() {
        return String.format("%-20s | $%-6.2f", name, price);
    }
}