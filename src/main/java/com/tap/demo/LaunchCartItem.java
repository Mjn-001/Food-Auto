package com.tap.demo;

import com.tap.DAO.CartItemDAO;
import com.tap.DAO.MenuDAO;
import com.tap.DAOImple.CartItemDAOImple;
import com.tap.DAOImple.MenuDAOImple;
import com.tap.Model.CartItem;
import com.tap.Model.Menu;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class LaunchCartItem {
    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        CartItemDAO cartItemDAO = new CartItemDAOImple();
        MenuDAO menuDAO = new MenuDAOImple(); // To show available food
        
        boolean isRunning = true;

        System.out.println("=============================================");
        System.out.println("   ZOMATO CLONE - CART ITEM DB TESTER        ");
        System.out.println("=============================================");

        while (isRunning) {
            System.out.println("\n--- CART ITEM MENU ---");
            System.out.println("1. Add a Specific Item to a Cart");
            System.out.println("2. View a Single Cart Item (by Item ID)");
            System.out.println("3. View All Items Inside a Specific Cart");
            System.out.println("4. Update Item Quantity");
            System.out.println("5. Remove Item from Cart");
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
                        // ADD CART ITEM
                        System.out.println("\n-- ADD ITEM TO CART --");
                        System.out.println("Make sure you know an existing Cart ID before proceeding!");
                        System.out.print("Enter the destination Cart ID: ");
                        int cartId = scanner.nextInt();
                        
                        System.out.println("\nAvailable Food Menu:");
                        List<Menu> allFood = menuDAO.getAllMenus();
                        for (Menu m : allFood) {
                            System.out.println(" -> Menu ID: " + m.getMenuId() + " | " + m.getItemName() + " (₹" + m.getPrice() + ")");
                        }
                        
                        System.out.print("\nEnter the Menu ID of the food you want: ");
                        int menuId = scanner.nextInt();
                        System.out.print("Enter the Quantity: ");
                        int qty = scanner.nextInt();
                        
                        CartItem newItem = new CartItem(cartId, menuId, qty);
                        cartItemDAO.addCartItem(newItem);
                        break;

                    case 2:
                        // READ SINGLE CART ITEM
                        System.out.println("\n-- VIEW SPECIFIC CART ITEM --");
                        System.out.print("Enter the CartItem ID (the unique row ID, not the Cart ID): ");
                        int itemId = scanner.nextInt();
                        
                        CartItem foundItem = cartItemDAO.getCartItem(itemId);
                        if (foundItem != null) {
                            System.out.println("✅ Item Found: \n" + foundItem.toString());
                        } else {
                            System.out.println("❌ No CartItem found with ID: " + itemId);
                        }
                        break;

                    case 3:
                        // READ ALL ITEMS IN A CART
                        System.out.println("\n-- VIEW CART CONTENTS --");
                        System.out.print("Enter the Cart ID to see what is inside: ");
                        int viewCartId = scanner.nextInt();
                        
                        List<CartItem> items = cartItemDAO.getCartItemsByCartId(viewCartId);
                        if (items.isEmpty()) {
                            System.out.println("Cart ID " + viewCartId + " is completely empty or does not exist.");
                        } else {
                            System.out.println("Contents of Cart ID " + viewCartId + ":");
                            for (CartItem item : items) {
                                System.out.println(" -> " + item.toString());
                            }
                        }
                        break;

                    case 4:
                        // UPDATE QUANTITY
                        System.out.println("\n-- UPDATE ITEM QUANTITY --");
                        System.out.print("Enter the CartItem ID you want to change: ");
                        int updateItemId = scanner.nextInt();
                        System.out.print("Enter the NEW Quantity: ");
                        int newQty = scanner.nextInt();
                        
                        CartItem itemToUpdate = cartItemDAO.getCartItem(updateItemId);
                        if(itemToUpdate != null) {
                            itemToUpdate.setQuantity(newQty);
                            cartItemDAO.updateCartItem(itemToUpdate);
                        } else {
                            System.out.println("❌ CartItem ID not found.");
                        }
                        break;

                    case 5:
                        // DELETE
                        System.out.println("\n-- REMOVE ITEM --");
                        System.out.print("Enter the CartItem ID you want to permanently remove: ");
                        int removeItemId = scanner.nextInt();
                        cartItemDAO.deleteCartItem(removeItemId);
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
                // Catches constraint errors like trying to add to a Cart ID that doesn't exist
                System.out.println("\n" + e.getMessage());
            }
        }
        
        scanner.close();
    }
}