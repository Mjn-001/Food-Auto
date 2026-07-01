package com.tap.DAOImple;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tap.DAO.PaymentDAO;
import com.tap.Model.Payment;
import com.tap.Utility.DBConnection;
import com.tap.Utility.DatabaseException;

public class PaymentDAOImple implements PaymentDAO {

    @Override
    public void addPayment(Payment payment) {
        String query = "INSERT INTO payment (orderId, amount, paymentMethod, paymentStatus) VALUES (?, ?, ?, ?)";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, payment.getOrderId());
            pstmt.setDouble(2, payment.getAmount());
            pstmt.setString(3, payment.getPaymentMethod());
            pstmt.setString(4, payment.getPaymentStatus());
            
            pstmt.executeUpdate();
            System.out.println("Success: Payment recorded for Order ID: " + payment.getOrderId());
            
        } catch (SQLException e) {
            if (e.getErrorCode() == 1452) {
                throw new DatabaseException("Failed: The Order ID provided does not exist.", e);
            }
            throw new DatabaseException("Failed to add payment record.", e);
        }
    }

    @Override
    public Payment getPayment(int paymentId) {
        Payment payment = null;
        String query = "SELECT * FROM payment WHERE paymentId = ?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, paymentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    payment = extractPaymentFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch payment with ID: " + paymentId, e);
        }
        return payment;
    }

    @Override
    public Payment getPaymentByOrderId(int orderId) {
        Payment payment = null;
        String query = "SELECT * FROM payment WHERE orderId = ?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    payment = extractPaymentFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch payment for Order ID: " + orderId, e);
        }
        return payment;
    }

    // Helper method to keep code clean
    private Payment extractPaymentFromResultSet(ResultSet rs) throws SQLException {
        return new Payment(
            rs.getInt("paymentId"),
            rs.getInt("orderId"),
            rs.getDouble("amount"),
            rs.getString("paymentMethod"),
            rs.getString("paymentStatus")
        );
    }
}