package JAVA;

import java.io.IOException;
import java.util.*;

public class Menu {

    private Scanner scanner = new Scanner(System.in);

    public void start() throws IOException {
        boolean running = true;

        while (running) {
            System.out.println("\n=== Welcome to Bryan's Gourmet Bites ===");
            System.out.println("1) New Order");
            System.out.println("0) Exit");
            System.out.print("Select an option: ");

            int choice = getValidInt(scanner);

            switch (choice) {
                case 1 -> handleNewOrder();
                case 0 -> {
                    System.out.println("Thank you for visiting! Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // === Core Order Flow ===
    private void handleNewOrder() {
        Order currentOrder = new Order();
        boolean ordering = true;

        while (ordering) {
            System.out.println("\n--- Current Order ---");
            if (currentOrder.isEmpty()) {
                System.out.println("[No items, drinks, or sides added yet]");
            } else {
                currentOrder.printSummary();
            }

            System.out.println("\n1) Add Item");
            System.out.println("2) Add Drink");
            System.out.println("3) Add Side");
            System.out.println("4) Checkout");
            System.out.println("0) Cancel Order");
            System.out.print("Select an option: ");

            int orderChoice = getValidInt(scanner);

            switch (orderChoice) {
                case 1 -> addItemFlow(scanner, currentOrder);
                case 2 -> addDrinkFlow(scanner, currentOrder);
                case 3 -> addSideFlow(scanner, currentOrder);
                case 4 -> {
                    checkoutFlow(scanner, currentOrder);
                    ordering = false;
                }
                case 0 -> {
                    System.out.println("Order canceled.");
                    ordering = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // === Item Addition ===
    private void addItemFlow(Scanner scanner, Order currentOrder) {
        System.out.println("\nSelect an item type:");
        List<String> itemTypes = getItemTypes();

        for (int i = 0; i < itemTypes.size(); i++) {
            System.out.printf("%d) %s\n", i + 1, itemTypes.get(i));
        }
        System.out.print("Choice: ");
        int typeChoice = getValidInt(scanner) - 1;

        if (typeChoice < 0 || typeChoice >= itemTypes.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        String itemType = itemTypes.get(typeChoice);
        double basePrice = getBasePriceForType(itemType);

        System.out.println("Select size: 1) SMALL  2) MEDIUM  3) LARGE");
        int sizeChoice = getValidInt(scanner);
        Drink.Size size = switch (sizeChoice) {
            case 2 -> Drink.Size.MEDIUM;
            case 3 -> Drink.Size.LARGE;
            default -> Drink.Size.SMALL;
        };

        CustomItem item = new CustomItem(itemType, size, basePrice);

        System.out.println("\nAdd toppings? (y/n): ");
        if (scanner.next().equalsIgnoreCase("y")) {
            addToppingsFlow(scanner, item);
        }

        currentOrder.addItem(item);
        System.out.println("Item added successfully!");
    }

    // === Drink Addition ===
    private void addDrinkFlow(Scanner scanner, Order currentOrder) {
        System.out.println("\nSelect drink flavor:");
        List<String> flavors = getDrinkFlavors();

        for (int i = 0; i < flavors.size(); i++) {
            System.out.printf("%d) %s\n", i + 1, flavors.get(i));
        }
        System.out.print("Choice: ");
        int flavorChoice = getValidInt(scanner) - 1;

        if (flavorChoice < 0 || flavorChoice >= flavors.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        String flavor = flavors.get(flavorChoice);

        System.out.println("Select size: 1) SMALL  2) MEDIUM  3) LARGE");
        int sizeChoice = getValidInt(scanner);
        Drink.Size size = switch (sizeChoice) {
            case 2 -> Drink.Size.MEDIUM;
            case 3 -> Drink.Size.LARGE;
            default -> Drink.Size.SMALL;
        };

        System.out.print("Enter base price for drink: ");
        double basePrice = scanner.nextDouble();

        Drink drink = new Drink(flavor, size, basePrice);
        currentOrder.addDrink(drink);
        System.out.println("Drink added successfully!");
    }

    // === Side Addition (simplified placeholder) ===
    private void addSideFlow(Scanner scanner, Order currentOrder) {
        System.out.print("\nEnter side name: ");
        scanner.nextLine(); // clear newline
        String side = scanner.nextLine();

        System.out.print("Enter price: ");
        double price = scanner.nextDouble();

        currentOrder.addSide(side, price);
        System.out.println("Side added successfully!");
    }

    // === Checkout ===
    private void checkoutFlow(Scanner scanner, Order currentOrder) {
        System.out.println("\n=== Checkout ===");
        currentOrder.printSummary();

        System.out.printf("Total: $%.2f\n", currentOrder.calculateTotal());
        System.out.println("Confirm order? (y/n): ");
        if (scanner.next().equalsIgnoreCase("y")) {
            System.out.println("Order placed successfully!");
        } else {
            System.out.println("Order canceled at checkout.");
        }
    }

    // === Toppings Flow ===
    private void addToppingsFlow(Scanner scanner, CustomItem item) {
        List<String> regularToppings = getToppingsRegular();
        System.out.println("Available Toppings:");

        for (int i = 0; i < regularToppings.size(); i++) {
            System.out.printf("%d) %s\n", i + 1, regularToppings.get(i));
        }

        boolean adding = true;
        while (adding) {
            System.out.print("Select topping number (0 to stop): ");
            int choice = getValidInt(scanner);
            if (choice == 0) break;

            if (choice < 1 || choice > regularToppings.size()) {
                System.out.println("Invalid topping number.");
                continue;
            }

            String toppingName = regularToppings.get(choice - 1);
            System.out.print("Quantity: ");
            int qty = getValidInt(scanner);

            item.addTopping(new Topping(toppingName, ToppingType.Regular, 0.5), qty);
            System.out.println(toppingName + " added.");
        }
    }

    // === Helpers and Data ===
    private int getValidInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    // === Static Mock Data (replace with DB or file later) ===
    public List<String> getDrinkFlavors() {
        return List.of("Cola", "Lemonade", "Iced Tea", "Vanilla Shake");
    }

    public List<String> getItemTypes() {
        return List.of("Burger", "Sandwich", "Wrap");
    }

    public double getBasePriceForType(String itemType) {
        return switch (itemType.toLowerCase()) {
            case "burger" -> 5.00;
            case "sandwich" -> 4.50;
            case "wrap" -> 4.25;
            default -> 3.00;
        };
    }

    public List<String> getToppingsRegular() {
        return List.of("Lettuce", "Tomato", "Onion", "Cheese", "Bacon");
    }

    // === Entry Point ===
    public static void main(String[] args) {
        Menu menu = new Menu();
        try {
            menu.start();
        } catch (IOException e) {
            System.err.println("An error occurred while processing the order: " + e.getMessage());
        }
    }
}