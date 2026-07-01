package com.tap.demo;

import com.tap.DAO.RestaurantDAO;
import com.tap.DAO.ReviewDAO;
import com.tap.DAO.UserDAO;
import com.tap.DAOImple.RestaurantDAOImple;
import com.tap.DAOImple.ReviewDAOImple;
import com.tap.DAOImple.UserDAOImple;
import com.tap.Model.Restaurant;
import com.tap.Model.Review;
import com.tap.Model.User;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class LaunchReview {
    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        ReviewDAO reviewDAO = new ReviewDAOImple();
        UserDAO userDAO = new UserDAOImple();
        RestaurantDAO restaurantDAO = new RestaurantDAOImple();
        
        boolean isRunning = true;

        System.out.println("=============================================");
        System.out.println("    ZOMATO CLONE - REVIEW SYSTEM TESTER      ");
        System.out.println("=============================================");

        while (isRunning) {
            System.out.println("\n--- REVIEW MENU ---");
            System.out.println("1. Write a New Review");
            System.out.println("2. View All Reviews for a Restaurant");
            System.out.println("3. View All Reviews written by a User");
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
                        // ADD REVIEW
                        System.out.println("\n-- WRITE A REVIEW --");
                        
                        System.out.print("Enter your User ID: ");
                        int userId = scanner.nextInt();
                        
                        System.out.println("\nRestaurants you can review:");
                        List<Restaurant> restaurants = restaurantDAO.getAllRestaurants();
                        for (Restaurant r : restaurants) {
                            System.out.println(" -> Rest. ID: " + r.getRestaurantId() + " | " + r.getName());
                        }
                        
                        System.out.print("\nEnter the Restaurant ID: ");
                        int restId = scanner.nextInt();
                        
                        System.out.print("Enter Rating (1 to 5): ");
                        int rating = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        
                        System.out.print("Write your comment: ");
                        String comment = scanner.nextLine();
                        
                        Review newReview = new Review(userId, restId, rating, comment);
                        reviewDAO.addReview(newReview);
                        break;

                    case 2:
                        // READ BY RESTAURANT
                        System.out.println("\n-- RESTAURANT REVIEWS --");
                        System.out.print("Enter the Restaurant ID to view its reviews: ");
                        int viewRestId = scanner.nextInt();
                        
                        List<Review> restReviews = reviewDAO.getReviewsByRestaurantId(viewRestId);
                        if (restReviews.isEmpty()) {
                            System.out.println("No reviews yet for this restaurant.");
                        } else {
                            for (Review r : restReviews) {
                                // Bonus: Fetch the user's name to make it look realistic!
                                User author = userDAO.getUser(r.getUserId());
                                String authorName = (author != null) ? author.getUserName() : "Anonymous User";
                                
                                System.out.println("\n⭐⭐⭐⭐⭐ " + r.getRating() + "/5 Stars by " + authorName);
                                System.out.println("\"" + r.getComment() + "\"");
                                System.out.println("Posted on: " + r.getReviewDate());
                            }
                        }
                        break;

                    case 3:
                        // READ BY USER
                        System.out.println("\n-- USER REVIEW HISTORY --");
                        System.out.print("Enter User ID to see their past reviews: ");
                        int viewUserId = scanner.nextInt();
                        
                        List<Review> userReviews = reviewDAO.getReviewsByUserId(viewUserId);
                        if (userReviews.isEmpty()) {
                            System.out.println("This user hasn't written any reviews.");
                        } else {
                            for (Review r : userReviews) {
                                System.out.println(" -> Rated Rest. ID " + r.getRestaurantId() + " | " + r.getRating() + " Stars | \"" + r.getComment() + "\"");
                            }
                        }
                        break;

                    case 4:
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