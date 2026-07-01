package com.tap.demo;

import com.tap.DAO.UserDAO;
import com.tap.DAOImple.UserDAOImple;
import com.tap.Model.User;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class LaunchUser {
    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        UserDAO userDAO = new UserDAOImple();
        boolean isRunning = true;

        System.out.println("=========================================");
        System.out.println("   ZOMATO CLONE - INTERACTIVE USER DB    ");
        System.out.println("=========================================");

        while (isRunning) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Add a New User");
            System.out.println("2. View a Specific User (by ID)");
            System.out.println("3. View ALL Users");
            System.out.println("4. Update an Existing User");
            System.out.println("5. Delete a User");
            System.out.println("6. Exit");
            System.out.print("Enter your choice (1-6): ");

            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the leftover newline character
            } catch (InputMismatchException e) {
                System.out.println("❌ Invalid input! Please enter a number.");
                scanner.nextLine(); // Clear the bad input
                continue;
            }

            try {
                switch (choice) {
                    case 1:
                        // CREATE
                        System.out.println("\n-- ADD NEW USER --");
                        System.out.print("Enter Username: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Password: ");
                        String pass = scanner.nextLine();
                        System.out.print("Enter Email: ");
                        String email = scanner.nextLine();
                        System.out.print("Enter Phone: ");
                        String phone = scanner.nextLine();
                        System.out.print("Enter Address: ");
                        String address = scanner.nextLine();
                        System.out.print("Enter Role (CUSTOMER, ADMIN, RESTAURANT_OWNER): ");
                        String role = scanner.nextLine().toUpperCase();

                        User newUser = new User(name, pass, email, phone, address, role);
                        userDAO.addUser(newUser);
                        break;

                    case 2:
                        // READ SINGLE
                        System.out.println("\n-- VIEW SPECIFIC USER --");
                        System.out.print("Enter the User ID to fetch: ");
                        int fetchId = scanner.nextInt();
                        
                        User foundUser = userDAO.getUser(fetchId);
                        if (foundUser != null) {
                            System.out.println("✅ User Found: \n" + foundUser.toString());
                        } else {
                            System.out.println("❌ No user found with ID: " + fetchId);
                        }
                        break;

                    case 3:
                        // READ ALL
                        System.out.println("\n-- VIEW ALL USERS --");
                        List<User> userList = userDAO.getAllUsers();
                        if (userList.isEmpty()) {
                            System.out.println("The database is completely empty!");
                        } else {
                            for (User u : userList) {
                                System.out.println("ID: " + u.getUserId() + " | Name: " + u.getUserName() + " | Role: " + u.getRole() + " | Email: " + u.getEmail());
                            }
                        }
                        break;

                    case 4:
                        // UPDATE
                        System.out.println("\n-- UPDATE USER --");
                        System.out.print("Enter the User ID you want to update: ");
                        int updateId = scanner.nextInt();
                        scanner.nextLine(); // consume newline
                        
                        User userToUpdate = userDAO.getUser(updateId);
                        if (userToUpdate != null) {
                            System.out.println("Current Name is '" + userToUpdate.getUserName() + "'. Enter new name: ");
                            userToUpdate.setUserName(scanner.nextLine());
                            
                            System.out.println("Current Phone is '" + userToUpdate.getPhone() + "'. Enter new phone: ");
                            userToUpdate.setPhone(scanner.nextLine());
                            
                            System.out.println("Current Address is '" + userToUpdate.getAddress() + "'. Enter new address: ");
                            userToUpdate.setAddress(scanner.nextLine());
                            
                            // Note: Keeping password the same for this quick test, but you could prompt for it!
                            
                            userDAO.updateUser(userToUpdate);
                        } else {
                            System.out.println("❌ No user found with ID: " + updateId);
                        }
                        break;

                    case 5:
                        // DELETE
                        System.out.println("\n-- DELETE USER --");
                        System.out.print("Enter the User ID you want to PERMANENTLY delete: ");
                        int deleteId = scanner.nextInt();
                        userDAO.deleteUser(deleteId);
                        break;

                    case 6:
                        // EXIT
                        System.out.println("Closing database connection... Goodbye!");
                        isRunning = false;
                        break;

                    default:
                        System.out.println("❌ Invalid choice. Please select 1 through 6.");
                }
            } catch (Exception e) {
                // This will catch our custom DatabaseException (like duplicate emails or foreign key constraints)
                System.out.println("\n" + e.getMessage());
            }
        }
        
        scanner.close();
    }
}