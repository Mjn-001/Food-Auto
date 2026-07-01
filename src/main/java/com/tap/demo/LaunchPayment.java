package com.tap.demo;

import com.tap.DAO.OrderDAO;
import com.tap.DAO.PaymentDAO;
import com.tap.DAOImple.OrderDAOImple;
import com.tap.DAOImple.PaymentDAOImple;
import com.tap.Model.Order;
import com.tap.Model.Payment;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class LaunchPayment {
    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        
        // We need the OrderDAO to fetch the bill amount!
        PaymentDAO paymentDAO = new PaymentDAOImple();
        OrderDAO orderDAO = new OrderDAOImple();
        
        boolean isRunning = true;

        System.out.println("=============================================");
        System.out.println("    ZOMATO CLONE - PAYMENT GATEWAY TESTER    ");
        System.out.println("=============================================");

        while (isRunning) {
            System.out.println("\n--- PAYMENT MENU ---");
            System.out.println("1. Process a New Payment (for an existing Order)");
            System.out.println("2. View a Specific Payment (by Payment ID)");
            System.out.println("3. Find Payment by Order ID (Receipt Lookup)");
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
                        // PROCESS NEW PAYMENT
                        System.out.println("\n-- PROCESS PAYMENT --");
                        
                        System.out.println("Pending Orders Awaiting Payment:");
                        List<Order> orders = orderDAO.getAllOrders();
                        if(orders.isEmpty()){
                            System.out.println("❌ No orders exist! Go run LaunchOrder first to create an order.");
                            break;
                        }
                        for (Order o : orders) {
                            System.out.println(" -> Order ID: " + o.getOrderId() + " | Bill Amount: ₹" + o.getTotalAmount());
                        }
                        
                        System.out.print("\nEnter the Order ID you want to pay for: ");
                        int targetOrderId = scanner.nextInt();
                        scanner.nextLine();
                        
                        // Fetch the order to get the exact amount owed
                        Order orderToPay = orderDAO.getOrder(targetOrderId);
                        if (orderToPay == null) {
                            System.out.println("❌ Order ID not found.");
                            break;
                        }
                        
                        double amountDue = orderToPay.getTotalAmount();
                        System.out.println("Amount Due for Order #" + targetOrderId + " is: ₹" + amountDue);
                        
                        System.out.println("Select Payment Method (CREDIT_CARD, DEBIT_CARD, UPI, NET_BANKING, CASH_ON_DELIVERY): ");
                        String method = scanner.nextLine().toUpperCase();
                        
                        System.out.println("Select Payment Status (SUCCESS, PENDING, FAILED): ");
                        String status = scanner.nextLine().toUpperCase();
                        
                        Payment newPayment = new Payment(targetOrderId, amountDue, method, status);
                        paymentDAO.addPayment(newPayment);
                        break;

                    case 2:
                        // READ SINGLE PAYMENT
                        System.out.println("\n-- VIEW SPECIFIC PAYMENT --");
                        System.out.print("Enter the Payment ID: ");
                        int paymentId = scanner.nextInt();
                        
                        Payment foundPayment = paymentDAO.getPayment(paymentId);
                        if (foundPayment != null) {
                            System.out.println("✅ Transaction Found: \n" + foundPayment.toString());
                        } else {
                            System.out.println("❌ No payment record found with ID: " + paymentId);
                        }
                        break;

                    case 3:
                        // LOOKUP BY ORDER ID
                        System.out.println("\n-- CHECK ORDER PAYMENT STATUS --");
                        System.out.print("Enter the Order ID to check if it was paid: ");
                        int checkOrderId = scanner.nextInt();
                        
                        Payment orderPayment = paymentDAO.getPaymentByOrderId(checkOrderId);
                        if (orderPayment != null) {
                            System.out.println("Transaction Details for Order #" + checkOrderId + ":");
                            System.out.println(" -> Paid: ₹" + orderPayment.getAmount() + " via " + orderPayment.getPaymentMethod());
                            System.out.println(" -> Status: " + orderPayment.getPaymentStatus());
                        } else {
                            System.out.println("❌ No payment found for Order ID " + checkOrderId + ". It might still be unpaid!");
                        }
                        break;

                    case 4:
                        // EXIT
                        System.out.println("Closing secure payment connections... Goodbye!");
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