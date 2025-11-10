package JAVA;
import java.util.*;

public class Order {
    private final List<OrderItem> items = new ArrayList<>();
    private final List<Drink> drinks = new ArrayList<>();
    private final List<Side> sides = new ArrayList<>();

    public void addItem(OrderItem item) { items.add(item); }
    public void addDrink(Drink drink) { drinks.add(drink); }
    public void addSide(Side side) { sides.add(side); }

    public boolean isValidOrder() {
        return !items.isEmpty() || !drinks.isEmpty() || !sides.isEmpty();
    }

    public void clear() {
        items.clear();
        drinks.clear();
        sides.clear();
    }

    public boolean isEmpty() { return items.isEmpty() && drinks.isEmpty() && sides.isEmpty(); }

    public List<OrderItem> getItems() { return items; }
    public List<Drink> getDrinks() { return drinks; }
    public List<Side> getSides() { return sides; }

    public String getOrderDetails() {
        return "Order Details:\nItems: " + items.size() + "\nDrinks: " + drinks.size() + "\nSides: " + sides.size();
    }

    public void saveReceipt() {
        // Save to file or database here
    }

    public double calculateTotal() {
        double total = 0;
        for (OrderItem i : items) total += i.calculatePrice();
        for (Drink d : drinks) total += d.calculatePrice();
        for (Side s : sides) total += s.getPrice();
        return total;
    }
}