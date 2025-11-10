package JAVA;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;

public class Order {
    private List<OrderItem> items;
    private List<Drink> drinks;
    private List<Side> sides;
    private LocalDateTime orderTime;

    public Order() {
        this.items = new ArrayList<>();
        this.drinks = new ArrayList<>();
        this.sides = new ArrayList<>();
        this.orderTime = LocalDateTime.now();
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public void addDrink(Drink drink) {
        drinks.add(drink);
    }

    public void addSide(Side side) {
        sides.add(side);
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public List<Drink> getDrinks() {
        return Collections.unmodifiableList(drinks);
    }

    public List<Side> getSides() {
        return Collections.unmodifiableList(sides);
    }

    public boolean isValidOrder() {
        return !items.isEmpty() || !drinks.isEmpty() || !sides.isEmpty();
    }

    public double calculateTotal() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.calculatePrice();
        }
        for (Drink drink : drinks) {
            total += drink.calculatePrice();
        }
        for (Side side : sides) {
            total += side.getPrice();
        }
        return total;
    }

    public String getOrderDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order placed at: ").append(orderTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n\n");

        if (!items.isEmpty()) {
            sb.append("Items:\n");
            for (OrderItem item : items) {
                sb.append(item.getDescription()).append("\n");
            }
        }
        if (!drinks.isEmpty()) {
            sb.append("Drinks:\n");
            for (Drink drink : drinks) {
                sb.append(drink.getDescription()).append("\n");
            }
        }
        if (!sides.isEmpty()) {
            sb.append("Sides:\n");
            for (Side side : sides) {
                sb.append(side.getDescription()).append("\n");
            }
        }

        sb.append(String.format("\nTotal: $%.2f\n", calculateTotal()));
        return sb.toString();
    }

    public void saveReceipt() throws IOException {
        String timestamp = orderTime.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        File receiptsDir = new File("receipts");
        if (!receiptsDir.exists()) {
            receiptsDir.mkdir();
        }
        File receiptFile = new File(receiptsDir, "receipt_" + timestamp + ".txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(receiptFile))) {
            writer.write(getOrderDetails());
        }
    }

    public void clear() {
        items.clear();
        drinks.clear();
        sides.clear();
    }

    public void printDetails() {
    System.out.println(getOrderDetails());
}

public boolean isEmpty() {
    return items.isEmpty() && drinks.isEmpty() && sides.isEmpty();
}

public void printSummary() {
    System.out.println("Order Summary:");
    
    if (!items.isEmpty()) {
        System.out.println("Items:");
        for (OrderItem item : items) {
            System.out.printf("- %s: $%.2f%n", item.getDescription(), item.calculatePrice());
        }
    }

    if (!drinks.isEmpty()) {
        System.out.println("Drinks:");
        for (Drink drink : drinks) {
            System.out.printf("- %s: $%.2f%n", drink.getDescription(), drink.calculatePrice());
        }
    }

    if (!sides.isEmpty()) {
        System.out.println("Sides:");
        for (Side side : sides) {
            System.out.printf("- %s: $%.2f%n", side.getDescription(), side.getPrice());
        }
    }

    System.out.printf("Total: $%.2f%n", calculateTotal());
}
}