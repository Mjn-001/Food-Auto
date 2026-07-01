package com.tap.demo;

import com.tap.DAO.*;
import com.tap.DAOImple.*;
import com.tap.Model.*;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class LaunchOrder {
    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        
        // We need the whole army of DAOs for the checkout process!
        OrderDAO orderDAO = new OrderDAOImple();
        OrderItemDAO orderItemDAO = new OrderItemDAOImple();
        CartDAO cartDAO = new CartDAOImple();
        CartItemDAO cartItemDAO = new CartItemDAOImple();
        MenuDAO menuDAO = new MenuDAOImple();
        UserDAO userDAO = new UserDAOImple();
        
        boolean isRunning = true;

        System.out.println("=============================================");
        System.out.println("    ZOMATO CLONE - CHECKOUT & ORDERS TEST    ");
        System.out.println("=============================================");

        while (isRunning) {
            System.out.println("\n--- ORDER MANAGEMENT MENU ---");
            System.out.println("1. Simulate Checkout (Convert Cart to Order)");
            System.out.println("2. View a Specific Order & Its Receipt");
            System.out.println("3. View Order History (By User ID)");
            System.out.println("4. View Restaurant Dashboard (Orders by Restaurant ID)");
            System.out.println("5. Update Order Status (e.g., PREPARING -> DELIVERED)");
            System.out.println("6. Exit");
            System.out.print("Enter your choice (1-6): ");

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
                        // SIMULATE FULL CHECKOUT
                        System.out.println("\n-- CHECKOUT PROCESS --");
                        System.out.print("Enter the User ID who is checking out: ");
                        int userId = scanner.nextInt();
                        
                        Cart cart = cartDAO.getCartByUserId(userId);
                        if (cart == null) {
                            System.out.println("❌ This user doesn't have an active cart!");
                            break;
                        }
                        
                        List<CartItem> cartItems = cartItemDAO.getCartItemsByCartId(cart.getCartId());
                        if (cartItems.isEmpty()) {
                            System.out.println("❌ The cart is empty! Add food before checking out.");
                            break;
                        }

                        // Calculate Total Price and find which restaurant they ordered from
                        double orderTotal = 0;
                        int restaurantId = -1;
                        
                        System.out.println("Scanning cart items...");
                        for (CartItem item : cartItems) {
                            Menu food = menuDAO.getMenu(item.getMenuId());
                            if (food != null) {
                                orderTotal += (food.getPrice() * item.getQuantity());
                                restaurantId = food.getRestaurantId(); // Assuming all items are from the same restaurant
                            }
                        }

                        // 1. Create the Order
                        System.out.println("Generating total bill: ₹" + orderTotal);
                        Order newOrder = new Order(userId, restaurantId, orderTotal, "PLACED");
                        orderDAO.addOrder(newOrder); // This auto-populates newOrder with the generated orderId!
                        
                        int generatedOrderId = newOrder.getOrderId();

                        // 2. Move CartItems to OrderItems
                        System.out.println("Locking items into permanent receipt...");
                        for (CartItem item : cartItems) {
                            Menu food = menuDAO.getMenu(item.getMenuId());
                            double itemTotal = food.getPrice() * item.getQuantity();
                            
                            OrderItem receiptItem = new OrderItem(generatedOrderId, item.getMenuId(), item.getQuantity(), itemTotal);
                            orderItemDAO.addOrderItem(receiptItem);
                            
                            // 3. Remove the item from the cart now that it's ordered
                            cartItemDAO.deleteCartItem(item.getCartItemId());
                        }

                        // 4. Delete the empty cart
                        cartDAO.deleteCart(cart.getCartId());
                        System.out.println("✅ Checkout Complete! Order #" + generatedOrderId + " has been successfully placed.");
                        break;

                    case 2:
                        // VIEW SPECIFIC ORDER RECEIPT
                        System.out.println("\n-- VIEW ORDER RECEIPT --");
                        System.out.print("Enter the Order ID: ");
                        int fetchOrderId = scanner.nextInt();
                        
                        Order foundOrder = orderDAO.getOrder(fetchOrderId);
                        if (foundOrder != null) {
                            System.out.println("\n================ RECEIPT ================");
                            System.out.println("Order #" + foundOrder.getOrderId() + " | Date: " + foundOrder.getOrderDate());
                            System.out.println("Status: " + foundOrder.getStatus());
                            System.out.println("-----------------------------------------");
                            
                            List<OrderItem> receiptItems = orderItemDAO.getOrderItemsByOrderId(fetchOrderId);
                            for(OrderItem item : receiptItems) {
                                Menu food = menuDAO.getMenu(item.getMenuId());
                                String foodName = (food != null) ? food.getItemName() : "Unknown";
                                System.out.println(foodName + " x" + item.getQuantity() + " ....... ₹" + item.getItemTotal());
                            }
                            System.out.println("-----------------------------------------");
                            System.out.println("TOTAL AMOUNT: ₹" + foundOrder.getTotalAmount());
                            System.out.println("=========================================");
                        } else {
                            System.out.println("❌ No order found with ID: " + fetchOrderId);
                        }
                        break;

                    case 3:
                        // VIEW USER ORDER HISTORY
                        System.out.println("\n-- USER ORDER HISTORY --");
                        System.out.print("Enter User ID to view their past orders: ");
                        int historyUserId = scanner.nextInt();
                        
                        List<Order> history = orderDAO.getOrdersByUserId(historyUserId);
                        if (history.isEmpty()) {
                            System.out.println("This user hasn't ordered anything yet.");
                        } else {
                            for (Order o : history) {
                                System.out.println("Order #" + o.getOrderId() + " | Status: " + o.getStatus() + " | Total: ₹" + o.getTotalAmount());
                            }
                        }
                        break;

                    case 4:
                        // VIEW RESTAURANT ORDERS
                        System.out.println("\n-- RESTAURANT DASHBOARD --");
                        System.out.print("Enter Restaurant ID to see incoming orders: ");
                        int restId = scanner.nextInt();
                        
                        List<Order> incomingOrders = orderDAO.getOrdersByRestaurantId(restId);
                        if (incomingOrders.isEmpty()) {
                            System.out.println("No orders for this restaurant yet.");
                        } else {
                            for (Order o : incomingOrders) {
                                System.out.println("Order #" + o.getOrderId() + " | Status: " + o.getStatus() + " | Total: ₹" + o.getTotalAmount());
                            }
                        }
                        break;

                    case 5:
                        // UPDATE STATUS
                        System.out.println("\n-- UPDATE ORDER STATUS --");
                        System.out.print("Enter the Order ID: ");
                        int updateId = scanner.nextInt();
                        scanner.nextLine(); // consume newline
                        
                        System.out.println("Valid Statuses: PLACED, CONFIRMED, PREPARING, OUT_FOR_DELIVERY, DELIVERED, CANCELLED");
                        System.out.print("Enter new status exactly as written above: ");
                        String newStatus = scanner.nextLine().toUpperCase();
                        
                        orderDAO.updateOrderStatus(updateId, newStatus);
                        break;

                    case 6:
                        // EXIT
                        System.out.println("Closing database connections... Goodbye!");
                        isRunning = false;
                        break;

                    default:
                        System.out.println("❌ Invalid choice. Please select 1 through 6.");
                }
            } catch (Exception e) {
                System.out.println("\n" + e.getMessage());
            }
        }
        
        scanner.close();
    }
}