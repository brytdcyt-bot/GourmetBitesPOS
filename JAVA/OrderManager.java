package JAVA;

import java.util.*;

public class OrderManager {
    private Order currentOrder;
    private final Menu menu;
    private final Scanner scanner;

    public OrderManager(Menu menu) {
        this.menu = menu;
        this.scanner = new Scanner(System.in);
    }

    public void startNewOrder() {
        currentOrder = new Order();
        System.out.println("Starting new order...");
        orderScreen();
    }

    private void orderScreen() {
        while (true) {
            System.out.println("\n--- Order Screen ---");
            displayCurrentOrderSummary();
            System.out.println("1) Add Item");
            System.out.println("2) Add Drink");
            System.out.println("3) Add Main Side");
            System.out.println("4) Checkout");
            System.out.println("0) Cancel Order");
            System.out.print("Select an option: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> addItem();
                case "2" -> addDrink();
                case "3" -> addSide();
                case "4" -> { checkout(); return; }
                case "0" -> { cancelOrder(); return; }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void addItem() {
        System.out.println("\nSelect item type:");
        List<String> itemTypes = menu.getItemTypes();
        for (int i = 0; i < itemTypes.size(); i++)
            System.out.printf("%d) %s%n", i + 1, itemTypes.get(i));

        int typeChoice = getChoice(itemTypes.size());
        if (typeChoice == -1) return;
        String itemType = itemTypes.get(typeChoice);

        System.out.println("\nSelect size:");
        Size[] sizes = Size.values();
        for (int i = 0; i < sizes.length; i++)
            System.out.printf("%d) %s%n", i + 1, sizes[i].name());
        int sizeChoice = getChoice(sizes.length);
        if (sizeChoice == -1) return;

        Size size = sizes[sizeChoice];
        double basePrice = menu.getBasePriceForType(itemType);
        CustomItem item = new CustomItem(itemType, size, basePrice);

        addToppingsToItem(item);

        System.out.print("Add special option? (yes/no): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            System.out.print("Enter description: ");
            item.setSpecialOption(scanner.nextLine().trim());
        }

        currentOrder.addItem(item);
        System.out.println("Item added to order.");
    }

    private void addToppingsToItem(CustomItem item) {
        // Regular
        System.out.println("\nAdd Regular Toppings:");
        List<String> regulars = menu.getToppingsRegular();
        for (int i = 0; i < regulars.size(); i++)
            System.out.printf("%d) %s%n", i + 1, regulars.get(i));
        System.out.print("Your choices: ");
        String regInput = scanner.nextLine();
        if (!regInput.isEmpty()) {
            for (String s : regInput.split(",")) {
                try {
                    int idx = Integer.parseInt(s.trim()) - 1;
                    if (idx >= 0 && idx < regulars.size())
                        item.addTopping(new Topping(regulars.get(idx), Topping.SMALL, 0), 1);
                } catch (Exception ignored) {}
            }
        }

        // Premium
        System.out.println("\nAdd Premium Toppings:");
        List<String> premiums = menu.getToppingsPremium();
        for (int i = 0; i < premiums.size(); i++) {
            String name = premiums.get(i);
            double price = menu.getPremiumToppingPrice(name);
            System.out.printf("%d) %s ($%.2f)%n", i + 1, name, price);
        }
        System.out.print("Your choices (e.g. 1:2,3:1): ");
        String premInput = scanner.nextLine();
        if (!premInput.isEmpty()) {
            for (String p : premInput.split(",")) {
                try {
                    String[] parts = p.split(":");
                    int idx = Integer.parseInt(parts[0].trim()) - 1;
                    int qty = Integer.parseInt(parts[1].trim());
                    if (idx >= 0 && idx < premiums.size())
                        item.addTopping(new Topping(premiums.get(idx), ToppingType.PREMIUM,
                            menu.getPremiumToppingPrice(premiums.get(idx))), qty);
                } catch (Exception ignored) {}
            }
        }
    }

    private void addDrink() {
        System.out.println("\nSelect drink flavor:");
        List<String> flavors = menu.getDrinkFlavors();
        for (int i = 0; i < flavors.size(); i++)
            System.out.printf("%d) %s%n", i + 1, flavors.get(i));
        int flavorChoice = getChoice(flavors.size());
        if (flavorChoice == -1) return;

        System.out.println("\nSelect size:");
        Size[] sizes = Size.values();
        for (int i = 0; i < sizes.length; i++)
            System.out.printf("%d) %s%n", i + 1, sizes[i].name());
        int sizeChoice = getChoice(sizes.length);
        if (sizeChoice == -1) return;

        currentOrder.addDrink(new Drink(flavors.get(flavorChoice), sizes[sizeChoice], 2.00));
        System.out.println("Drink added to order.");
    }

    private void addSide() {
        System.out.println("\nSelect side:");
        List<Side> sides = menu.getSides();
        for (int i = 0; i < sides.size(); i++)
            System.out.printf("%d) %s ($%.2f)%n", i + 1, sides.get(i).getName(), sides.get(i).getPrice());
        int sideChoice = getChoice(sides.size());
        if (sideChoice == -1) return;

        currentOrder.addSide(sides.get(sideChoice));
        System.out.println("Side added to order.");
    }

    private void checkout() {
        System.out.println("\n--- Checkout ---");
        if (!currentOrder.isValidOrder()) {
            System.out.println("Order must contain at least one item, drink, or side.");
            return;
        }
        System.out.println(currentOrder.getOrderDetails());
        System.out.print("Confirm order? (yes/no): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            try {
                currentOrder.saveReceipt();
                System.out.println("Order confirmed! Receipt saved.");
            } catch (Exception e) {
                System.out.println("Failed to save receipt: " + e.getMessage());
            }
            currentOrder.clear();
        } else {
            System.out.println("Order canceled.");
            currentOrder.clear();
        }
    }

    private void cancelOrder() {
        System.out.println("Order canceled.");
        currentOrder.clear();
    }

    private void displayCurrentOrderSummary() {
        System.out.println("\nCurrent Order Summary:");
        if (currentOrder.isEmpty()) {
            System.out.println("No items in order.");
            return;
        }

        currentOrder.getItems().forEach(i -> System.out.println("- " + i.getDescription()));
        currentOrder.getDrinks().forEach(d -> System.out.println("- " + d.getDescription()));
        currentOrder.getSides().forEach(s -> System.out.println("- " + s.getDescription()));

        System.out.printf("Current Total: $%.2f%n", currentOrder.calculateTotal());
    }

    private int getChoice(int max) {
        try {
            int val = Integer.parseInt(scanner.nextLine().trim());
            return (val < 1 || val > max) ? -1 : val - 1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}