package JAVA;

import java.io.IOException;
import java.util.Scanner;

public class Menu {

    public void start() throws IOException {
    Scanner scanner = new Scanner(System.in);
    boolean running = true;

    while (running) {
        System.out.println("\n=== Welcome to Bryan's Gourmet Bites ===");
        System.out.println("1) New Order");
        System.out.println("0) Exit");
        System.out.print("Select an option: ");

        int choice = getValidInt(scanner);

        switch (choice) {
            case 1:
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
                        case 1:
                            addItemFlow(scanner, currentOrder);
                            break;
                        case 2:
                            addDrinkFlow(scanner, currentOrder);
                            break;
                        case 3:
                            addSideFlow(scanner, currentOrder);
                            break;
                        case 4:
                            checkoutFlow(scanner, currentOrder);
                            ordering = false;
                            break;
                        case 0:
                            System.out.println("Order canceled.");
                            ordering = false;
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }
                break;

            case 0:
                System.out.println("Thank you for visiting! Goodbye!");
                running = false;
                break;

            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
}

    private void checkoutFlow(Scanner scanner, Order currentOrder) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkoutFlow'");
    }

    private void addDrinkFlow(Scanner scanner, Order currentOrder) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addDrinkFlow'");
    }

    private void addSideFlow(Scanner scanner, Order currentOrder) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addSideFlow'");
    }

    private void addItemFlow(Scanner scanner, Order currentOrder) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addItemFlow'");
    }

    private int getValidInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    public static void main(String[] args) {
        Menu menu = new Menu();
        try {
            menu.start();
        } catch (IOException e) {
            System.err.println("An error occurred while processing the order: " + e.getMessage());
        }
    }
}