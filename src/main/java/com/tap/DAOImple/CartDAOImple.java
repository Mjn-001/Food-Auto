package com.tap.DAOImple;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tap.DAO.CartDAO;
import com.tap.Model.Cart;
import com.tap.Utility.DBConnection;
import com.tap.Utility.DatabaseException;

public class CartDAOImple implements CartDAO {

    @Override
    public void addCart(Cart cart) {
        String query = "INSERT INTO cart (userId) VALUES (?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, cart.getUserId());
            pstmt.executeUpdate();
            System.out.println("Success: Cart created for User ID: " + cart.getUserId());
            
        } catch (SQLException e) {
            if(e.getErrorCode() == 1452) {
                throw new DatabaseException("Failed: The User ID provided does not exist.", e);
            }
            throw new DatabaseException("Failed to create cart.", e);
        }
    }

    @Override
    public Cart getCart(int cartId) {
        Cart cart = null;
        String query = "SELECT * FROM cart WHERE cartId = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, cartId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    cart = new Cart(rs.getInt("cartId"), rs.getInt("userId"));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch cart with ID: " + cartId, e);
        }
        return cart;
    }

    @Override
    public Cart getCartByUserId(int userId) {
        Cart cart = null;
        String query = "SELECT * FROM cart WHERE userId = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    cart = new Cart(rs.getInt("cartId"), rs.getInt("userId"));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch cart for User ID: " + userId, e);
        }
        return cart;
    }

    @Override
    public void deleteCart(int cartId) {
        String query = "DELETE FROM cart WHERE cartId=?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, cartId);
            pstmt.executeUpdate();
            System.out.println("Success: Cart deleted.");
            
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                throw new DatabaseException("Cannot delete: Please empty the cart items first before deleting the cart.", e);
            }
            throw new DatabaseException("Failed to delete cart.", e);
        }
    }
}