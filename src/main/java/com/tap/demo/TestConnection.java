package com.tap.demo;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestConnection {
    public static void main(String[] args) {
        // Your exact database credentials
        String url = "jdbc:mysql://localhost:3306/fullstackproject";
        String user = "root"; // Assuming your username is root
        String password = "1817";

        try {
            // 1. Load the MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully!");

            // 2. Attempt to connect
            System.out.println("Attempting to connect to the database...");
            Connection connection = DriverManager.getConnection(url, user, password);

            // 3. Verify connection
            if (connection != null) {
                System.out.println("✅ SUCCESS! Connected to the 'fullstackproject' database.");
                connection.close(); // Close the connection when done
            } else {
                System.out.println("❌ FAILED to establish connection.");
            }

        } catch (Exception e) {
            System.out.println("❌ ERROR: Connection failed. Check the error trace below:");
            e.printStackTrace();
        }
    }
}