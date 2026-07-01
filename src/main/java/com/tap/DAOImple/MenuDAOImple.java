package com.tap.DAOImple;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tap.DAO.MenuDAO;
import com.tap.Model.Menu;
import com.tap.Utility.DBConnection;
import com.tap.Utility.DatabaseException;

public class MenuDAOImple implements MenuDAO {

    @Override
    public void addMenu(Menu menu) {
        String query = "INSERT INTO menu (restaurantId, itemName, description, price, category, isAvailable) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, menu.getRestaurantId());
            pstmt.setString(2, menu.getItemName());
            pstmt.setString(3, menu.getDescription());
            pstmt.setDouble(4, menu.getPrice());
            pstmt.setString(5, menu.getCategory());
            pstmt.setBoolean(6, menu.isAvailable());
            
            pstmt.executeUpdate();
            System.out.println("Success: Menu item added.");
            
        } catch (SQLException e) {
            if (e.getErrorCode() == 1452) {
                // MySQL Error 1452: Foreign Key Constraint Fails (Parent row doesn't exist)
                throw new DatabaseException("Failed: The Restaurant ID provided does not exist in the database.", e);
            }
            throw new DatabaseException("Failed to add menu item.", e);
        }
    }

    @Override
    public Menu getMenu(int menuId) {
        Menu menu = null;
        String query = "SELECT * FROM menu WHERE menuId = ?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, menuId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    menu = extractMenuFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch menu item with ID: " + menuId, e);
        }
        return menu;
    }

    @Override
    public void updateMenu(Menu menu) {
        String query = "UPDATE menu SET itemName=?, description=?, price=?, category=?, isAvailable=? WHERE menuId=?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setString(1, menu.getItemName());
            pstmt.setString(2, menu.getDescription());
            pstmt.setDouble(3, menu.getPrice());
            pstmt.setString(4, menu.getCategory());
            pstmt.setBoolean(5, menu.isAvailable());
            pstmt.setInt(6, menu.getMenuId()); // Note: We do NOT update the restaurantId
            
            pstmt.executeUpdate();
            System.out.println("Success: Menu item updated.");
            
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update menu item data for ID: " + menu.getMenuId(), e);
        }
    }

    @Override
    public void deleteMenu(int menuId) {
        String query = "DELETE FROM menu WHERE menuId=?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, menuId);
            pstmt.executeUpdate();
            System.out.println("Success: Menu item deleted.");
            
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                // MySQL Error 1451: Foreign Key Constraint Fails (Child row exists)
                throw new DatabaseException("Cannot delete: This menu item is currently in a user's cart or an active order.", e);
            }
            throw new DatabaseException("Failed to delete menu item with ID: " + menuId, e);
        }
    }

    @Override
    public List<Menu> getAllMenus() {
        List<Menu> list = new ArrayList<>();
        String query = "SELECT * FROM menu";
        
        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                list.add(extractMenuFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve the full list of menu items.", e);
        }
        return list;
    }

    @Override
    public List<Menu> getMenusByRestaurant(int restaurantId) {
        List<Menu> list = new ArrayList<>();
        String query = "SELECT * FROM menu WHERE restaurantId = ?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, restaurantId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractMenuFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve menu items for Restaurant ID: " + restaurantId, e);
        }
        return list;
    }

    // Private helper method to keep our code DRY (Don't Repeat Yourself)
    private Menu extractMenuFromResultSet(ResultSet rs) throws SQLException {
        Menu menu = new Menu();
        menu.setMenuId(rs.getInt("menuId"));
        menu.setRestaurantId(rs.getInt("restaurantId"));
        menu.setItemName(rs.getString("itemName"));
        menu.setDescription(rs.getString("description"));
        menu.setPrice(rs.getDouble("price"));
        menu.setCategory(rs.getString("category"));
        menu.setAvailable(rs.getBoolean("isAvailable"));
        return menu;
    }
}