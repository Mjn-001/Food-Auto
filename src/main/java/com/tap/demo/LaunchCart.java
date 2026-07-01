package com.tap.demo;

import com.tap.DAO.CartDAO;
import com.tap.DAO.CartItemDAO;
import com.tap.DAO.MenuDAO;
import com.tap.DAO.UserDAO;
import com.tap.DAOImple.CartDAOImple;
import com.tap.DAOImple.CartItemDAOImple;
import com.tap.DAOImple.MenuDAOImple;
import com.tap.DAOImple.UserDAOImple;
import com.tap.Model.Cart;
import com.tap.Model.CartItem;
import com.tap.Model.Menu;
import com.tap.Model.User;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class LaunchCart {
    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        
        // We need all these DAOs to make the shopping experience make sense
        CartDAO cartDAO = new CartDAOImple();
        CartItemDAO cartItemDAO = new CartItemDAOImple();
        UserDAO userDAO = new UserDAOImple();
        MenuDAO menuDAO = new MenuDAOImple();
        
        boolean isRunning = true;

        System.out.println("=============================================");
        System.out.println("    ZOMATO CLONE - INTERACTIVE CART TEST     ");
        System.out.println("=============================================");

        while (isRunning) {
            System.out.println("\n--- SHOPPING MENU ---");
            System.out.println("1. Find or Create a Cart for a User");
            System.out.println("2. View Restaurant Menus (to find Food IDs)");
            System.out.println("3. Add Item to Cart");
            System.out.println("4. View Cart Contents");
            System.out.println("5. Update Item Quantity");
            System.out.println("6. Remove Item from Cart");
            System.out.println("7. Delete Entire Cart");
            System.out.println("8. Exit");
            System.out.print("Enter your choice (1-8): ");

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
                        // FIND OR CREATE CART
                        System.out.println("\n-- FIND / CREATE CART --");
                        System.out.println("Valid Users:");
                        for(User u : userDAO.getAllUsers()) {
                            System.out.println(" -> User ID: " + u.getUserId() + " | Name: " + u.getUserName());
                        }
                        
                        System.out.print("\nEnter the User ID to simulate logging in: ");
                        int userId = scanner.nextInt();
                        
                        Cart existingCart = cartDAO.getCartByUserId(userId);
                        if (existingCart != null) {
                            System.out.println("✅ Found existing Cart! Your Cart ID is: " + existingCart.getCartId());
                        } else {
                            System.out.println("No cart found. Creating a new one...");
                            cartDAO.addCart(new Cart(userId));
                            // Fetch it immediately to show the user their new Cart ID
                            Cart newCart = cartDAO.getCartByUserId(userId);
                            if(newCart != null) {
                                System.out.println("✅ New Cart Created! Your Cart ID is: " + newCart.getCartId());
                            }
                        }
                        break;

                    case 2:
                        // VIEW FOOD ITEMS
                        System.out.println("\n-- VIEW FOOD CATALOG --");
                        List<Menu> allFood = menuDAO.getAllMenus();
                        if (allFood.isEmpty()) {
                            System.out.println("❌ No food in the database yet!");
                        } else {
                            for (Menu m : allFood) {
                                System.out.println("Menu ID: " + m.getMenuId() + " | " + m.getItemName() + " (₹" + m.getPrice() + ")");
                            }
                        }
                        break;

                    case 3:
                        // ADD TO CART
                        System.out.println("\n-- ADD TO CART --");
                        System.out.print("Enter your Cart ID: ");
                        int cartId = scanner.nextInt();
                        System.out.print("Enter the Menu ID of the food: ");
                        int menuId = scanner.nextInt();
                        System.out.print("Enter Quantity: ");
                        int qty = scanner.nextInt();
                        
                        CartItem newItem = new CartItem(cartId, menuId, qty);
                        cartItemDAO.addCartItem(newItem);
                        break;

                    case 4:
                        // VIEW CART CONTENTS
                        System.out.println("\n-- VIEW CART CONTENTS --");
                        System.out.print("Enter your Cart ID: ");
                        int viewCartId = scanner.nextInt();
                        
                        List<CartItem> items = cartItemDAO.getCartItemsByCartId(viewCartId);
                        if (items.isEmpty()) {
                            System.out.println("Your cart is completely empty!");
                        } else {
                            System.out.println("Items in Cart ID " + viewCartId + ":");
                            for (CartItem item : items) {
                                // Bonus: Fetch the actual food name so it looks nice!
                                Menu foodDetails = menuDAO.getMenu(item.getMenuId());
                                String foodName = (foodDetails != null) ? foodDetails.getItemName() : "Unknown Item";
                                
                                System.out.println(" -> CartItem ID: " + item.getCartItemId() + " | " + foodName + " | Quantity: " + item.getQuantity());
                            }
                        }
                        break;

                    case 5:
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

                    case 6:
                        // REMOVE ITEM
                        System.out.println("\n-- REMOVE ITEM FROM CART --");
                        System.out.print("Enter the CartItem ID you want to permanently remove: ");
                        int removeItemId = scanner.nextInt();
                        cartItemDAO.deleteCartItem(removeItemId);
                        break;

                    case 7:
                        // DELETE ENTIRE CART
                        System.out.println("\n-- DELETE ENTIRE CART --");
                        System.out.print("Enter the Cart ID you want to destroy: ");
                        int destroyCartId = scanner.nextInt();
                        cartDAO.deleteCart(destroyCartId);
                        break;

                    case 8:
                        System.out.println("Closing database connections... Goodbye!");
                        isRunning = false;
                        break;

                    default:
                        System.out.println("❌ Invalid choice. Please select 1 through 8.");
                }
            } catch (Exception e) {
                System.out.println("\n" + e.getMessage());
            }
        }
        
        scanner.close();
    }
}