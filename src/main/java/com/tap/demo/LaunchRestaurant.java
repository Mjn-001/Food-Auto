package com.tap.demo;

import com.tap.DAO.RestaurantDAO;
import com.tap.DAO.UserDAO;
import com.tap.DAOImple.RestaurantDAOImple;
import com.tap.DAOImple.UserDAOImple;
import com.tap.Model.Restaurant;
import com.tap.Model.User;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class LaunchRestaurant {
    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        RestaurantDAO restaurantDAO = new RestaurantDAOImple();
        UserDAO userDAO = new UserDAOImple(); // We need this to check valid Admin IDs
        boolean isRunning = true;

        System.out.println("=============================================");
        System.out.println("  ZOMATO CLONE - INTERACTIVE RESTAURANT DB   ");
        System.out.println("=============================================");

        while (isRunning) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Add a New Restaurant");
            System.out.println("2. View a Specific Restaurant (by ID)");
            System.out.println("3. View ALL Restaurants");
            System.out.println("4. View Restaurants by Admin/Owner ID");
            System.out.println("5. Update an Existing Restaurant");
            System.out.println("6. Delete a Restaurant");
            System.out.println("7. Exit");
            System.out.print("Enter your choice (1-7): ");

            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the leftover newline
            } catch (InputMismatchException e) {
                System.out.println("❌ Invalid input! Please enter a number.");
                scanner.nextLine(); // Clear the bad input
                continue;
            }

            try {
                switch (choice) {
                    case 1:
                        // CREATE
                        System.out.println("\n-- ADD NEW RESTAURANT --");
                        
                        // Helper: Show valid users so the user knows what ID to type
                        System.out.println("Valid Users you can assign as the Owner/Admin:");
                        List<User> users = userDAO.getAllUsers();
                        for(User u : users) {
                            System.out.println(" -> User ID: " + u.getUserId() + " | Name: " + u.getUserName() + " | Role: " + u.getRole());
                        }
                        
                        System.out.print("\nEnter the Admin User ID from the list above: ");
                        int adminId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        System.out.print("Enter Restaurant Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Cuisine Type (e.g., Italian, Indian, Fast Food): ");
                        String cuisine = scanner.nextLine();
                        System.out.print("Enter Address: ");
                        String address = scanner.nextLine();
                        
                        System.out.print("Enter Est. Delivery Time (in minutes): ");
                        int delivery = scanner.nextInt();
                        
                        System.out.print("Enter Initial Rating (e.g., 4.5): ");
                        double rating = scanner.nextDouble();
                        
                        System.out.print("Is it Active? (true/false): ");
                        boolean isActive = scanner.nextBoolean();

                        Restaurant newRestaurant = new Restaurant(name, cuisine, address, delivery, rating, isActive, adminId);
                        restaurantDAO.addRestaurant(newRestaurant);
                        break;

                    case 2:
                        // READ SINGLE
                        System.out.println("\n-- VIEW SPECIFIC RESTAURANT --");
                        System.out.print("Enter the Restaurant ID to fetch: ");
                        int fetchId = scanner.nextInt();
                        
                        Restaurant foundRes = restaurantDAO.getRestaurant(fetchId);
                        if (foundRes != null) {
                            System.out.println("✅ Restaurant Found: \n" + foundRes.toString());
                        } else {
                            System.out.println("❌ No restaurant found with ID: " + fetchId);
                        }
                        break;

                    case 3:
                        // READ ALL
                        System.out.println("\n-- VIEW ALL RESTAURANTS --");
                        List<Restaurant> resList = restaurantDAO.getAllRestaurants();
                        if (resList.isEmpty()) {
                            System.out.println("The restaurant database is currently empty!");
                        } else {
                            for (Restaurant r : resList) {
                                System.out.println(r.toString());
                            }
                        }
                        break;

                    case 4:
                        // READ BY ADMIN ID (Testing our special method)
                        System.out.println("\n-- VIEW RESTAURANTS BY OWNER/ADMIN --");
                        System.out.print("Enter the Admin User ID: ");
                        int ownerId = scanner.nextInt();
                        
                        List<Restaurant> ownerResList = restaurantDAO.getRestaurantsByAdmin(ownerId);
                        if (ownerResList.isEmpty()) {
                            System.out.println("No restaurants found managed by User ID: " + ownerId);
                        } else {
                            System.out.println("Restaurants managed by ID " + ownerId + ":");
                            for (Restaurant r : ownerResList) {
                                System.out.println(" -> " + r.getName() + " (" + r.getCuisineType() + ")");
                            }
                        }
                        break;

                    case 5:
                        // UPDATE
                        System.out.println("\n-- UPDATE RESTAURANT --");
                        System.out.print("Enter the Restaurant ID you want to update: ");
                        int updateId = scanner.nextInt();
                        scanner.nextLine(); // consume newline
                        
                        Restaurant resToUpdate = restaurantDAO.getRestaurant(updateId);
                        if (resToUpdate != null) {
                            System.out.println("Current Name is '" + resToUpdate.getName() + "'. Enter new name: ");
                            resToUpdate.setName(scanner.nextLine());
                            
                            System.out.println("Current Delivery Time is '" + resToUpdate.getDeliveryTime() + " mins'. Enter new time: ");
                            resToUpdate.setDeliveryTime(scanner.nextInt());
                            
                            System.out.println("Current Status is Active=" + resToUpdate.isActive() + ". Is it active now? (true/false): ");
                            resToUpdate.setActive(scanner.nextBoolean());
                            
                            restaurantDAO.updateRestaurant(resToUpdate);
                        } else {
                            System.out.println("❌ No restaurant found with ID: " + updateId);
                        }
                        break;

                    case 6:
                        // DELETE
                        System.out.println("\n-- DELETE RESTAURANT --");
                        System.out.print("Enter the Restaurant ID you want to PERMANENTLY delete: ");
                        int deleteId = scanner.nextInt();
                        restaurantDAO.deleteRestaurant(deleteId);
                        break;

                    case 7:
                        // EXIT
                        System.out.println("Closing database connections... Goodbye!");
                        isRunning = false;
                        break;

                    default:
                        System.out.println("❌ Invalid choice. Please select 1 through 7.");
                }
            } catch (Exception e) {
                // Catches database constraint errors (like typing an Admin ID that doesn't exist)
                System.out.println("\n" + e.getMessage());
            }
        }
        
        scanner.close();
    }
}