package com.tap.DAOImple;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tap.DAO.UserDAO;
import com.tap.Model.User;
import com.tap.Utility.DBConnection;
import com.tap.Utility.DatabaseException;

public class UserDAOImple implements UserDAO {

    @Override
    public void addUser(User user) {
        String query = "INSERT INTO user (userName, password, email, phone, address, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, user.getAddress());
            pstmt.setString(6, user.getRole());
            
            pstmt.executeUpdate();
            System.out.println("Success: User added.");
        } catch (SQLException e) {
            if(e.getErrorCode() == 1062) {
                throw new DatabaseException("Registration Failed: Email already in use.", e);
            }
            throw new DatabaseException("Failed to add user.", e);
        }
    }

    @Override
    public User getUser(int userId) {
        User user = null;
        String query = "SELECT * FROM user WHERE userId = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUserId(rs.getInt("userId"));
                    user.setUserName(rs.getString("userName"));
                    user.setPassword(rs.getString("password"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setRole(rs.getString("role"));
                    user.setCreateDate(rs.getTimestamp("createDate"));
                    user.setLastLoginDate(rs.getTimestamp("lastLoginDate"));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch user.", e);
        }
        return user;
    }

    @Override
    public void updateUser(User user) {
        String query = "UPDATE user SET userName=?, password=?, phone=?, address=? WHERE userId=?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getAddress());
            pstmt.setInt(5, user.getUserId());
            
            pstmt.executeUpdate();
            System.out.println("Success: User updated.");
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update user.", e);
        }
    }

    @Override
    public void deleteUser(int userId) {
        String query = "DELETE FROM user WHERE userId=?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            System.out.println("Success: User deleted.");
        } catch (SQLException e) {
            if(e.getErrorCode() == 1451) {
                throw new DatabaseException("Cannot delete: User is linked to existing data.", e);
            }
            throw new DatabaseException("Failed to delete user.", e);
        }
    }
    
 // Add this to UserDAOImple.java
    public User loginUser(String email, String password) {
        User user = null;
        String query = "SELECT * FROM user WHERE email = ? AND password = ?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUserId(rs.getInt("userId"));
                    user.setUserName(rs.getString("userName"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setPhone(rs.getString("phone"));
                    user.setAddress(rs.getString("address"));
                    user.setRole(rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user; // Returns the User object if successful, or null if login fails
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM user";
        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("userId"));
                user.setUserName(rs.getString("userName"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setRole(rs.getString("role"));
                user.setCreateDate(rs.getTimestamp("createDate"));
                user.setLastLoginDate(rs.getTimestamp("lastLoginDate"));
                userList.add(user);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve users.", e);
        }
        return userList;
    }
}