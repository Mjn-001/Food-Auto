package com.tap.DAOImple;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tap.DAO.CartItemDAO;
import com.tap.Model.CartItem;
import com.tap.Utility.DBConnection;
import com.tap.Utility.DatabaseException;

public class CartItemDAOImple implements CartItemDAO {

    @Override
    public void addCartItem(CartItem cartItem) {
        String query = "INSERT INTO cartItem (cartId, menuId, quantity) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, cartItem.getCartId());
            pstmt.setInt(2, cartItem.getMenuId());
            pstmt.setInt(3, cartItem.getQuantity());
            
            pstmt.executeUpdate();
            System.out.println("Success: Item added to cart.");
            
        } catch (SQLException e) {
            if(e.getErrorCode() == 1452) {
                throw new DatabaseException("Failed: Either the Cart ID or Menu ID does not exist.", e);
            }
            throw new DatabaseException("Failed to add item to cart.", e);
        }
    }

    @Override
    public CartItem getCartItem(int cartItemId) {
        CartItem item = null;
        String query = "SELECT * FROM cartItem WHERE cartItemId = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, cartItemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    item = new CartItem(rs.getInt("cartItemId"), rs.getInt("cartId"), rs.getInt("menuId"), rs.getInt("quantity"));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch cart item.", e);
        }
        return item;
    }

    @Override
    public void updateCartItem(CartItem cartItem) {
        String query = "UPDATE cartItem SET quantity=? WHERE cartItemId=?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, cartItem.getQuantity());
            pstmt.setInt(2, cartItem.getCartItemId());
            
            pstmt.executeUpdate();
            System.out.println("Success: Cart item quantity updated.");
            
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update cart item.", e);
        }
    }

    @Override
    public void deleteCartItem(int cartItemId) {
        String query = "DELETE FROM cartItem WHERE cartItemId=?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, cartItemId);
            pstmt.executeUpdate();
            System.out.println("Success: Item removed from cart.");
            
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete cart item.", e);
        }
    }

    @Override
    public List<CartItem> getCartItemsByCartId(int cartId) {
        List<CartItem> list = new ArrayList<>();
        String query = "SELECT * FROM cartItem WHERE cartId = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, cartId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new CartItem(rs.getInt("cartItemId"), rs.getInt("cartId"), rs.getInt("menuId"), rs.getInt("quantity")));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve cart items.", e);
        }
        return list;
    }
}