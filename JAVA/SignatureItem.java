package JAVA;

import java.util.*;
import java.text.DecimalFormat;

public class SignatureItem extends OrderItem {

    private List<ToppingQuantity> toppings = new ArrayList<>();
    private double basePrice;
    private String name;
    private Size size;

    public SignatureItem(String name, Size size, double basePrice) {
        this.name = name;
        this.size = size;
        this.basePrice = basePrice;
        initializeDefaultToppings();
    }

    // Helper class to store topping + quantity pairs
    private static class ToppingQuantity {
        Topping topping;
        int quantity;

        ToppingQuantity(Topping topping, int quantity) {
            this.topping = topping;
            this.quantity = quantity;
        }
    }

    private void initializeDefaultToppings() {
        // Example default toppings (customize as needed)
        // toppings.add(new ToppingQuantity(new Topping("Cheese"), 1));
        // toppings.add(new ToppingQuantity(new Topping("Tomato Sauce"), 1));
    }

    public void addTopping(Topping topping, int quantity) {
        // Check if topping already exists, increase quantity instead
        for (ToppingQuantity tq : toppings) {
            if (tq.topping.getName().equalsIgnoreCase(topping.getName())) {
                tq.quantity += quantity;
                return;
            }
        }
        toppings.add(new ToppingQuantity(topping, quantity));
    }

    public void removeTopping(String toppingName) {
        toppings.removeIf(tq -> tq.topping.getName().equalsIgnoreCase(toppingName));
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    @Override
    public double calculatePrice() {
        double total = basePrice;
        for (ToppingQuantity tq : toppings) {
            total += tq.topping.getPrice() * tq.quantity;
        }
        return total;
    }

    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" (").append(size).append(") - Base: $")
          .append(new DecimalFormat("#0.00").format(basePrice))
          .append("\nToppings:\n");

        for (ToppingQuantity tq : toppings) {
            sb.append(" - ").append(tq.topping.getName())
              .append(" x").append(tq.quantity)
              .append(" ($").append(new DecimalFormat("#0.00").format(tq.topping.getPrice()))
              .append(" each)\n");
        }

        sb.append("Total: $").append(new DecimalFormat("#0.00").format(calculatePrice()));
        return sb.toString();
    }
}