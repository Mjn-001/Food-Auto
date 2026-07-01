package com.tap.DAOImple;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tap.DAO.RestaurantDAO;
import com.tap.Model.Restaurant;
import com.tap.Utility.DBConnection;
import com.tap.Utility.DatabaseException;

public class RestaurantDAOImple implements RestaurantDAO {

    @Override
    public void addRestaurant(Restaurant restaurant) {
        String query = "INSERT INTO restaurant (name, cuisineType, address, deliveryTime, rating, isActive, adminUserId) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setString(1, restaurant.getName());
            pstmt.setString(2, restaurant.getCuisineType());
            pstmt.setString(3, restaurant.getAddress());
            pstmt.setInt(4, restaurant.getDeliveryTime());
            pstmt.setDouble(5, restaurant.getRating());
            pstmt.setBoolean(6, restaurant.isActive());
            pstmt.setInt(7, restaurant.getAdminUserId());
            
            pstmt.executeUpdate();
            System.out.println("Success: Restaurant added.");
            
        } catch (SQLException e) {
            if(e.getErrorCode() == 1452) {
                throw new DatabaseException("Failed: The adminUserId provided does not exist in the User table.", e);
            }
            throw new DatabaseException("Failed to add restaurant.", e);
        }
    }

    @Override
    public Restaurant getRestaurant(int restaurantId) {
        Restaurant restaurant = null;
        String query = "SELECT * FROM restaurant WHERE restaurantId = ?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, restaurantId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    restaurant = extractRestaurantFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch restaurant with ID: " + restaurantId, e);
        }
        return restaurant;
    }

    @Override
    public void updateRestaurant(Restaurant restaurant) {
        String query = "UPDATE restaurant SET name=?, cuisineType=?, address=?, deliveryTime=?, rating=?, isActive=? WHERE restaurantId=?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setString(1, restaurant.getName());
            pstmt.setString(2, restaurant.getCuisineType());
            pstmt.setString(3, restaurant.getAddress());
            pstmt.setInt(4, restaurant.getDeliveryTime());
            pstmt.setDouble(5, restaurant.getRating());
            pstmt.setBoolean(6, restaurant.isActive());
            pstmt.setInt(7, restaurant.getRestaurantId());
            
            pstmt.executeUpdate();
            System.out.println("Success: Restaurant updated.");
            
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update restaurant data for ID: " + restaurant.getRestaurantId(), e);
        }
    }

    @Override
    public void deleteRestaurant(int restaurantId) {
        String query = "DELETE FROM restaurant WHERE restaurantId=?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, restaurantId);
            pstmt.executeUpdate();
            System.out.println("Success: Restaurant deleted.");
            
        } catch (SQLException e) {
            if(e.getErrorCode() == 1451) {
                throw new DatabaseException("Cannot delete: This restaurant is linked to menus or orders.", e);
            }
            throw new DatabaseException("Failed to delete restaurant with ID: " + restaurantId, e);
        }
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> list = new ArrayList<>();
        String query = "SELECT * FROM restaurant";
        
        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                list.add(extractRestaurantFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve the list of restaurants.", e);
        }
        return list;
    }

    @Override
    public List<Restaurant> getRestaurantsByAdmin(int adminUserId) {
        List<Restaurant> list = new ArrayList<>();
        String query = "SELECT * FROM restaurant WHERE adminUserId = ?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, adminUserId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractRestaurantFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve restaurants for admin ID: " + adminUserId, e);
        }
        return list;
    }
    @Override
    public List<Restaurant> searchRestaurants(String keyword) {
        List<Restaurant> list = new ArrayList<>();
        // Using LIKE with wildcards allows partial matches (e.g., "Bur" finds "Burger King")
        String query = "SELECT * FROM restaurant WHERE name LIKE ?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            // Setting the parameter with wildcards
            pstmt.setString(1, "%" + keyword + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Using your existing helper method!
                    list.add(extractRestaurantFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to search restaurants with keyword: " + keyword, e);
        }
        return list;
    }
    
    

    // Helper method to keep code clean and avoid repeating ResultSet extraction logic
    private Restaurant extractRestaurantFromResultSet(ResultSet rs) throws SQLException {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantId(rs.getInt("restaurantId"));
        restaurant.setName(rs.getString("name"));
        restaurant.setCuisineType(rs.getString("cuisineType"));
        restaurant.setAddress(rs.getString("address"));
        restaurant.setDeliveryTime(rs.getInt("deliveryTime"));
        restaurant.setRating(rs.getDouble("rating"));
        restaurant.setActive(rs.getBoolean("isActive"));
        restaurant.setAdminUserId(rs.getInt("adminUserId"));
        return restaurant;
    }
}