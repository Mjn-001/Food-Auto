package com.tap.demo;

import com.tap.DAO.MenuDAO;
import com.tap.DAO.RestaurantDAO;
import com.tap.DAOImple.MenuDAOImple;
import com.tap.DAOImple.RestaurantDAOImple;
import com.tap.Model.Menu;
import com.tap.Model.Restaurant;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class LaunchMenu {
    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        MenuDAO menuDAO = new MenuDAOImple();
        RestaurantDAO restaurantDAO = new RestaurantDAOImple(); // To fetch valid restaurants
        boolean isRunning = true;

        System.out.println("=============================================");
        System.out.println("     ZOMATO CLONE - INTERACTIVE MENU DB      ");
        System.out.println("=============================================");

        while (isRunning) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Add a New Menu Item");
            System.out.println("2. View a Specific Menu Item");
            System.out.println("3. View Menu Items by Restaurant ID");
            System.out.println("4. Update an Existing Menu Item");
            System.out.println("5. Delete a Menu Item");
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
                        // CREATE
                        System.out.println("\n-- ADD NEW MENU ITEM --");
                        
                        System.out.println("Valid Restaurants you can add food to:");
                        List<Restaurant> restaurants = restaurantDAO.getAllRestaurants();
                        if (restaurants.isEmpty()) {
                            System.out.println("❌ No restaurants exist yet! Go create one first.");
                            break;
                        }
                        for(Restaurant r : restaurants) {
                            System.out.println(" -> Rest. ID: " + r.getRestaurantId() + " | Name: " + r.getName());
                        }
                        
                        System.out.print("\nEnter the Restaurant ID from the list above: ");
                        int restId = scanner.nextInt();
                        scanner.nextLine(); 

                        System.out.print("Enter Item Name (e.g., Chicken Biryani): ");
                        String itemName = scanner.nextLine();
                        System.out.print("Enter Description: ");
                        String desc = scanner.nextLine();
                        
                        System.out.print("Enter Price (e.g., 250.50): ");
                        double price = scanner.nextDouble();
                        scanner.nextLine(); 

                        System.out.print("Enter Category (e.g., Main Course, Starter, Dessert): ");
                        String category = scanner.nextLine();
                        
                        System.out.print("Is it currently available? (true/false): ");
                        boolean isAvailable = scanner.nextBoolean();

                        Menu newMenu = new Menu(restId, itemName, desc, price, category, isAvailable);
                        menuDAO.addMenu(newMenu);
                        break;

                    case 2:
                        // READ SINGLE
                        System.out.println("\n-- VIEW SPECIFIC MENU ITEM --");
                        System.out.print("Enter the Menu ID to fetch: ");
                        int fetchId = scanner.nextInt();
                        
                        Menu foundMenu = menuDAO.getMenu(fetchId);
                        if (foundMenu != null) {
                            System.out.println("✅ Item Found: \n" + foundMenu.toString());
                        } else {
                            System.out.println("❌ No menu item found with ID: " + fetchId);
                        }
                        break;

                    case 3:
                        // READ BY RESTAURANT (The most important feature)
                        System.out.println("\n-- VIEW MENU BY RESTAURANT --");
                        System.out.print("Enter the Restaurant ID to see its menu: ");
                        int queryRestId = scanner.nextInt();
                        
                        List<Menu> restMenu = menuDAO.getMenusByRestaurant(queryRestId);
                        if (restMenu.isEmpty()) {
                            System.out.println("No food items found for Restaurant ID: " + queryRestId);
                        } else {
                            System.out.println("Menu for Restaurant ID " + queryRestId + ":");
                            for (Menu m : restMenu) {
                                System.out.println(" -> " + m.getItemName() + " | ₹" + m.getPrice() + " | " + (m.isAvailable() ? "Available" : "Sold Out"));
                            }
                        }
                        break;

                    case 4:
                        // UPDATE
                        System.out.println("\n-- UPDATE MENU ITEM --");
                        System.out.print("Enter the Menu ID you want to update: ");
                        int updateId = scanner.nextInt();
                        scanner.nextLine(); 
                        
                        Menu menuToUpdate = menuDAO.getMenu(updateId);
                        if (menuToUpdate != null) {
                            System.out.println("Current Price is ₹" + menuToUpdate.getPrice() + ". Enter new price: ");
                            menuToUpdate.setPrice(scanner.nextDouble());
                            
                            System.out.println("Current Status is Available=" + menuToUpdate.isAvailable() + ". Is it available now? (true/false): ");
                            menuToUpdate.setAvailable(scanner.nextBoolean());
                            
                            menuDAO.updateMenu(menuToUpdate);
                        } else {
                            System.out.println("❌ No menu item found with ID: " + updateId);
                        }
                        break;

                    case 5:
                        // DELETE
                        System.out.println("\n-- DELETE MENU ITEM --");
                        System.out.print("Enter the Menu ID you want to PERMANENTLY delete: ");
                        int deleteId = scanner.nextInt();
                        menuDAO.deleteMenu(deleteId);
                        break;

                    case 6:
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