package JAVA;

import java.util.*;

public class CustomItem extends OrderItem {
    private List<ToppingQuantity> toppings;
    private String specialOption;
    private double basePrice;

    public CustomItem(String type, Drink.Size size, double basePrice) {
        super(type, size);
        this.basePrice = basePrice;
        this.toppings = new ArrayList<>();
        this.specialOption = "";
    }

    public void addTopping(Topping topping, int quantity) {
        toppings.add(new ToppingQuantity(topping, quantity));
    }

    public void setSpecialOption(String option) {
        this.specialOption = option;
    }

    @Override
    public double calculatePrice() {
        double price = basePrice * size.getPriceMultiplier();

        for (ToppingQuantity tq : toppings) {
            if (tq.topping.getType() == Topping.ToppingType.Premium) {
                // Only extras (quantity > 1) cost extra
                int extraCount = Math.max(0, tq.quantity - 1);
                price += tq.topping.getPrice() * extraCount;
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
        for (ToppingQuantity tq : toppings) {
            sb.append(String.format("- %s x%d\n", tq.topping.getName(), tq.quantity));
        }
        sb.append(String.format("Price: $%.2f", calculatePrice()));
        return sb.toString();
    }

    private static class ToppingQuantity {
        Topping topping;
        int quantity;

        ToppingQuantity(Topping topping, int quantity) {
            this.topping = topping;
            this.quantity = quantity;
        }
    }

    @Override
    public decimal CalculatePrice() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'CalculatePrice'");
    }

    @Override
    public string GetDescription() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'GetDescription'");
    }
}