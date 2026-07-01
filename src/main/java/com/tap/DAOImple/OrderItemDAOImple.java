package com.tap.DAOImple;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tap.DAO.OrderItemDAO;
import com.tap.Model.OrderItem;
import com.tap.Utility.DBConnection;
import com.tap.Utility.DatabaseException;

public class OrderItemDAOImple implements OrderItemDAO {

    @Override
    public void addOrderItem(OrderItem orderItem) {
        String query = "INSERT INTO orderItem (orderId, menuId, quantity, itemTotal) VALUES (?, ?, ?, ?)";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, orderItem.getOrderId());
            pstmt.setInt(2, orderItem.getMenuId());
            pstmt.setInt(3, orderItem.getQuantity());
            pstmt.setDouble(4, orderItem.getItemTotal());
            
            pstmt.executeUpdate();
            System.out.println("Success: Item locked into order receipt.");
            
        } catch (SQLException e) {
            if (e.getErrorCode() == 1452) {
                throw new DatabaseException("Failed: Order ID or Menu ID does not exist.", e);
            }
            throw new DatabaseException("Failed to add order item.", e);
        }
    }

    @Override
    public OrderItem getOrderItem(int orderItemId) {
        OrderItem item = null;
        String query = "SELECT * FROM orderItem WHERE orderItemId = ?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, orderItemId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    item = new OrderItem(rs.getInt("orderItemId"), rs.getInt("orderId"), rs.getInt("menuId"), rs.getInt("quantity"), rs.getDouble("itemTotal"));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch order item.", e);
        }
        return item;
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> list = new ArrayList<>();
        String query = "SELECT * FROM orderItem WHERE orderId = ?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new OrderItem(rs.getInt("orderItemId"), rs.getInt("orderId"), rs.getInt("menuId"), rs.getInt("quantity"), rs.getDouble("itemTotal")));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve items for this order.", e);
        }
        return list;
    }
}