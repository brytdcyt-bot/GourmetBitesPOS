package JAVA;

import java.util.*;

public class CustomItem extends OrderItem {

    private List<ToppingQuantity> toppings;
    private String specialOption;
    private double basePrice;

    public CustomItem(String type, Size size, double basePrice) {
        super(type, size);
        this.basePrice = basePrice;
        this.toppings = new ArrayList<>();
        this.specialOption = "";
    }

    // ✅ Removed unused duplicate constructor
    // If you need overloaded constructors, define meaningful ones instead of placeholders

    public void addTopping(Topping topping, int quantity) {
        toppings.add(new ToppingQuantity(topping, quantity));
    }

    public void addTopping(Topping topping) {
        addTopping(topping, 1); // Overload for default quantity = 1
    }

    public void setSpecialOption(String option) {
        this.specialOption = option;
    }

    @Override
    public double calculatePrice() {
        double price = basePrice * size.getPriceMultiplier();

        for (ToppingQuantity tq : toppings) {
            // ✅ Corrected access to ToppingType enum
            if (tq.topping.getType() == ToppingType.Premium) {
                // Premium toppings cost extra beyond the first unit
                int extraCount = Math.max(0, tq.quantity - 1);
                price += tq.topping.getPrice() * extraCount;
            } else {
                // Regular toppings cost per quantity
                price += tq.topping.getPrice() * tq.quantity;
            }
        }

        return price;
    }

    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%s %s", size.name(), type));
        if (!specialOption.isEmpty()) {
            sb.append(" with ").append(specialOption);
        }

        sb.append("\nToppings:\n");
        if (toppings.isEmpty()) {
            sb.append("- None\n");
        } else {
            for (ToppingQuantity tq : toppings) {
                sb.append(String.format("- %s x%d\n", tq.topping.getName(), tq.quantity));
            }
        }

        sb.append(String.format("Price: $%.2f", calculatePrice()));
        return sb.toString();
    }

    // ✅ Inner helper class to manage topping and quantity
    private static class ToppingQuantity {
        Topping topping;
        int quantity;

        ToppingQuantity(Topping topping, int quantity) {
            this.topping = topping;
            this.quantity = quantity;
        }
    }
}