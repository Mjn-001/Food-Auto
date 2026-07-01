package com.tap.demo;

import com.tap.DAO.MenuDAO;
import com.tap.DAO.OrderDAO;
import com.tap.DAO.OrderItemDAO;
import com.tap.DAOImple.MenuDAOImple;
import com.tap.DAOImple.OrderDAOImple;
import com.tap.DAOImple.OrderItemDAOImple;
import com.tap.Model.Menu;
import com.tap.Model.Order;
import com.tap.Model.OrderItem;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class LaunchOrderItem {
    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        
        // We need these DAOs to display valid Foreign Keys
        OrderItemDAO orderItemDAO = new OrderItemDAOImple();
        OrderDAO orderDAO = new OrderDAOImple();
        MenuDAO menuDAO = new MenuDAOImple();
        
        boolean isRunning = true;

        System.out.println("=============================================");
        System.out.println("   ZOMATO CLONE - ORDER ITEM DB TESTER       ");
        System.out.println("=============================================");

        while (isRunning) {
            System.out.println("\n--- ORDER ITEM MENU ---");
            System.out.println("1. Add a Manual Order Item (Testing Only)");
            System.out.println("2. View a Specific Order Item (by Item ID)");
            System.out.println("3. View All Items for a Specific Order (Receipt View)");
            System.out.println("4. Exit");
            System.out.print("Enter your choice (1-4): ");

            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); 
            } catch (InputMismatchException e) {
                System.out.println("❌ Invalid input! Please enter a number.");
                scanner.nextLine(); 
                continue;
            }

            try {
                switch (choice) {
                    case 1:
                        // ADD MANUAL ORDER ITEM
                        System.out.println("\n-- ADD MANUAL ORDER ITEM --");
                        System.out.println("Note: In a real app, this is done automatically during checkout.");
                        
                        System.out.println("\nRecent Orders:");
                        List<Order> orders = orderDAO.getAllOrders();
                        if(orders.isEmpty()){
                            System.out.println("❌ No orders exist! Go run LaunchOrder first.");
                            break;
                        }
                        for (Order o : orders) {
                            System.out.println(" -> Order ID: " + o.getOrderId() + " | Total: ₹" + o.getTotalAmount());
                        }
                        
                        System.out.print("\nEnter the target Order ID: ");
                        int targetOrderId = scanner.nextInt();
                        
                        System.out.println("\nAvailable Menu Items:");
                        List<Menu> allFood = menuDAO.getAllMenus();
                        for (Menu m : allFood) {
                            System.out.println(" -> Menu ID: " + m.getMenuId() + " | " + m.getItemName() + " (₹" + m.getPrice() + ")");
                        }
                        
                        System.out.print("\nEnter the Menu ID of the food: ");
                        int menuId = scanner.nextInt();
                        System.out.print("Enter Quantity: ");
                        int qty = scanner.nextInt();
                        System.out.print("Enter Total Price for these items (e.g., 500.00): ");
                        double itemTotal = scanner.nextDouble();
                        
                        OrderItem newItem = new OrderItem(targetOrderId, menuId, qty, itemTotal);
                        orderItemDAO.addOrderItem(newItem);
                        break;

                    case 2:
                        // READ SINGLE ITEM
                        System.out.println("\n-- VIEW SPECIFIC ORDER ITEM --");
                        System.out.print("Enter the OrderItem ID (the unique row ID): ");
                        int itemId = scanner.nextInt();
                        
                        OrderItem foundItem = orderItemDAO.getOrderItem(itemId);
                        if (foundItem != null) {
                            System.out.println("✅ Item Found: \n" + foundItem.toString());
                        } else {
                            System.out.println("❌ No OrderItem found with ID: " + itemId);
                        }
                        break;

                    case 3:
                        // READ ALL ITEMS IN AN ORDER
                        System.out.println("\n-- VIEW ORDER RECEIPT --");
                        System.out.print("Enter the Order ID to view its contents: ");
                        int viewOrderId = scanner.nextInt();
                        
                        List<OrderItem> items = orderItemDAO.getOrderItemsByOrderId(viewOrderId);
                        if (items.isEmpty()) {
                            System.out.println("Order ID " + viewOrderId + " has no items or does not exist.");
                        } else {
                            System.out.println("Contents of Order #" + viewOrderId + ":");
                            for (OrderItem item : items) {
                                // Fetching the name to make the output readable
                                Menu food = menuDAO.getMenu(item.getMenuId());
                                String foodName = (food != null) ? food.getItemName() : "Unknown Item";
                                System.out.println(" -> " + foodName + " (Qty: " + item.getQuantity() + ") | Subtotal: ₹" + item.getItemTotal());
                            }
                        }
                        break;

                    case 4:
                        // EXIT
                        System.out.println("Closing database connections... Goodbye!");
                        isRunning = false;
                        break;

                    default:
                        System.out.println("❌ Invalid choice. Please select 1 through 4.");
                }
            } catch (Exception e) {
                System.out.println("\n" + e.getMessage());
            }
        }
        
        scanner.close();
    }
}