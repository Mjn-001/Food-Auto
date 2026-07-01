package com.tap.Utility;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    // Updated to your specific database name "fullstackproject"
    private static final String URL = "jdbc:mysql://localhost:3306/fullstackproject";
    
    // Assuming your MySQL username is the default "root"
    private static final String USER = "root"; 
    
    // Updated to your specific password
    private static final String PASS = "1817"; 

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Load the MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish the connection using your credentials
            connection = DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}