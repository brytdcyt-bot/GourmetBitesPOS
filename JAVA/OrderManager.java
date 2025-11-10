package JAVA;

import java.util.Scanner;

public class OrderManager {
    private Order currentOrder;
    private Menu menu;
    private Scanner scanner;

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
                case "1":
                    addItem();
                    break;
                case "2":
                    addDrink();
                    break;
                case "3":
                    addSide();
                    break;
                case "4":
                    checkout();
                    return;
                case "0":
                    cancelOrder();
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void addItem() {
        System.out.println("\nSelect item type:");
        for (int i = 0; i < menu.getItemTypes().size(); i++) {
            System.out.printf("%d) %s\n", i + 1, menu.getItemTypes().get(i));
        }
        System.out.print("Choice: ");
        int typeChoice = Integer.parseInt(scanner.nextLine()) - 1;
        if (typeChoice < 0 || typeChoice >= menu.getItemTypes().size()) {
            System.out.println("Invalid type selection.");
            return;
        }
        String itemType = menu.getItemTypes().get(typeChoice);

        // Select size
        System.out.println("\nSelect size:");
        Drink.Size[] sizes = Drink.Size.values();
        for (int i = 0; i < sizes.length; i++) {
            System.out.printf("%d) %s\n", i + 1, sizes[i].name());
        }
        System.out.print("Choice: ");
        int sizeChoice = Integer.parseInt(scanner.nextLine()) - 1;
        if (sizeChoice < 0 || sizeChoice >= sizes.length) {
            System.out.println("Invalid size selection.");
            return;
        }
        Drink.Size size = sizes[sizeChoice];

        double basePrice = menu.getBasePriceForType(itemType);
        CustomItem item = new CustomItem(itemType, size, basePrice);

        // Add toppings
        addToppingsToItem(item);

        // Special option
        System.out.print("Would you like to add a special option? (yes/no): ");
        String specialOption = scanner.nextLine().trim().toLowerCase();
        if (specialOption.equals("yes")) {
            System.out.print("Enter special option description: ");
            String option = scanner.nextLine().trim();
            item.setSpecialOption(option);
        }

        currentOrder.addItem(item);
        System.out.println("Item added to order.");
    }

    private void addToppingsToItem(CustomItem item) {
        // Regular toppings
        System.out.println("\nAdd Regular Toppings (enter numbers separated by commas, or leave empty):");
        for (int i = 0; i < menu.getToppingsRegular().size(); i++) {
            System.out.printf("%d) %s\n", i + 1, menu.getToppingsRegular().get(i));
        }
        System.out.print("Your choices: ");
        String regInput = scanner.nextLine();
        if (!regInput.trim().isEmpty()) {
            String[] regIndexes = regInput.split(",");
            for (String indexStr : regIndexes) {
                try {
                    int idx = Integer.parseInt(indexStr.trim()) - 1;
                    if (idx >= 0 && idx < menu.getToppingsRegular().size()) {
                        String toppingName = menu.getToppingsRegular().get(idx);
                        item.addTopping(new Topping(toppingName, Topping.ToppingType.Regular, 0), 1);
                    }
                } catch (NumberFormatException ignored) {}
            }
        }

        // Premium toppings (with quantity)
        System.out.println("\nAdd Premium Toppings (enter number:quantity separated by commas, or leave empty):");
        for (int i = 0; i < menu.getToppingsPremium().size(); i++) {
            String toppingName = menu.getToppingsPremium().get(i);
            double price = menu.getPremiumToppingPrice(toppingName);
            System.out.printf("%d) %s ($%.2f for each extra)\n", i + 1, toppingName, price);
        }
        System.out.print("Your choices (e.g. 1:2,3:1): ");
        String premInput = scanner.nextLine();
        if (!premInput.trim().isEmpty()) {
            String[] premParts = premInput.split(",");
            for (String part : premParts) {
                try {
                    String[] pair = part.split(":");
                    int idx = Integer.parseInt(pair[0].trim()) - 1;
                    int quantity = Integer.parseInt(pair[1].trim());
                    if (idx >= 0 && idx < menu.getToppingsPremium().size() && quantity > 0) {
                        String toppingName = menu.getToppingsPremium().get(idx);
                        double price = menu.getPremiumToppingPrice(toppingName);
                        item.addTopping(new Topping(toppingName, Topping.ToppingType.Premium, price), quantity);
                    }
                } catch (Exception ignored) {}
            }
        }
    }

    private void addDrink() {
        System.out.println("\nSelect drink flavor:");
        for (int i = 0; i < menu.getDrinkFlavors().size(); i++) {
            System.out.printf("%d) %s\n", i + 1, menu.getDrinkFlavors().get(i));
        }
        System.out.print("Choice: ");
        int flavorChoice = Integer.parseInt(scanner.nextLine()) - 1;
        if (flavorChoice < 0 || flavorChoice >= menu.getDrinkFlavors().size()) {
            System.out.println("Invalid flavor choice.");
            return;
        }
        String flavor = menu.getDrinkFlavors().get(flavorChoice);

        System.out.println("\nSelect drink size:");
        Drink.Size[] sizes = Drink.Size.values();
        for (int i = 0; i < sizes.length; i++) {
            System.out.printf("%d) %s\n", i + 1, sizes[i].name());
        }
        System.out.print("Choice: ");
        int sizeChoice = Integer.parseInt(scanner.nextLine()) - 1;
        if (sizeChoice < 0 || sizeChoice >= sizes.length) {
            System.out.println("Invalid size choice.");
            return;
        }
        Drink.Size size = sizes[sizeChoice];

        // Base drink price could be fixed or variable, here fixed at 2.00
        Drink drink = new Drink(flavor, size, 2.00);
        currentOrder.addDrink(drink);
        System.out.println("Drink added to order.");
    }

    private void addSide() {
        System.out.println("\nSelect side:");
        List<Side> sides = menu.getSides();
        for (int i = 0; i < sides.size(); i++) {
            Side side = sides.get(i);
            System.out.printf("%d) %s ($%.2f)\n", i + 1, side.getName(), side.getPrice());
        }
        System.out.print("Choice: ");
        int sideChoice = Integer.parseInt(scanner.nextLine()) - 1;
        if (sideChoice < 0 || sideChoice >= sides.size()) {
            System.out.println("Invalid side choice.");
            return;
        }
        Side selectedSide = sides.get(sideChoice);
        currentOrder.addSide(selectedSide);
        System.out.println("Side added to order.");
    }

    private void checkout() {
        System.out.println("\n--- Checkout ---");
        if (!currentOrder.isValidOrder()) {
            System.out.println("Order must contain at least one item, or a drink/side.");
            return;
        }
        System.out.println(currentOrder.getOrderDetails());
        System.out.println("Confirm order? (yes/no)");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (confirm.equals("yes")) {
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
        if (currentOrder.getItems().isEmpty() && currentOrder.getDrinks().isEmpty() && currentOrder.getSides().isEmpty()) {
            System.out.println("No items in order.");
            return;
        }

        if (!currentOrder.getItems().isEmpty()) {
            System.out.println("Items:");
            for (OrderItem item : currentOrder.getItems()) {
                System.out.println("- " + item.getDescription());
            }
        }
        if (!currentOrder.getDrinks().isEmpty()) {
            System.out.println("Drinks:");
            for (Drink drink : currentOrder.getDrinks()) {
                System.out.println("- " + drink.getDescription());
            }
        }
        if (!currentOrder.getSides().isEmpty()) {
            System.out.println("Sides:");
            for (Side side : currentOrder.getSides()) {
                System.out.println("- " + side.getDescription());
            }
        }

        System.out.printf("Current Total: $%.2f\n", currentOrder.calculateTotal());
    }
}