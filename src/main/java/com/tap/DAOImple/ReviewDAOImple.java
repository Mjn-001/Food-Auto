package com.tap.DAOImple;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tap.DAO.ReviewDAO;
import com.tap.Model.Review;
import com.tap.Utility.DBConnection;
import com.tap.Utility.DatabaseException;

public class ReviewDAOImple implements ReviewDAO {

    @Override
    public void addReview(Review review) {
        String query = "INSERT INTO review (userId, restaurantId, rating, comment) VALUES (?, ?, ?, ?)";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, review.getUserId());
            pstmt.setInt(2, review.getRestaurantId());
            pstmt.setInt(3, review.getRating());
            pstmt.setString(4, review.getComment());
            
            pstmt.executeUpdate();
            System.out.println("Success: Review posted!");
            
        } catch (SQLException e) {
            if (e.getErrorCode() == 1452) {
                throw new DatabaseException("Failed: Either the User ID or Restaurant ID does not exist.", e);
            }
            throw new DatabaseException("Failed to add review.", e);
        }
    }

    @Override
    public Review getReview(int reviewId) {
        Review review = null;
        String query = "SELECT * FROM review WHERE reviewId = ?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, reviewId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    review = extractReviewFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch review.", e);
        }
        return review;
    }

    @Override
    public void updateReview(Review review) {
        String query = "UPDATE review SET rating=?, comment=? WHERE reviewId=?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, review.getRating());
            pstmt.setString(2, review.getComment());
            pstmt.setInt(3, review.getReviewId());
            
            pstmt.executeUpdate();
            System.out.println("Success: Review updated.");
            
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update review.", e);
        }
    }

    @Override
    public void deleteReview(int reviewId) {
        String query = "DELETE FROM review WHERE reviewId=?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, reviewId);
            pstmt.executeUpdate();
            System.out.println("Success: Review deleted.");
            
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete review.", e);
        }
    }

    @Override
    public List<Review> getReviewsByRestaurantId(int restaurantId) {
        return fetchReviewsByQuery("SELECT * FROM review WHERE restaurantId = ? ORDER BY reviewDate DESC", restaurantId);
    }

    @Override
    public List<Review> getReviewsByUserId(int userId) {
        return fetchReviewsByQuery("SELECT * FROM review WHERE userId = ? ORDER BY reviewDate DESC", userId);
    }

    // --- Helper Methods ---
    
    private List<Review> fetchReviewsByQuery(String query, int param) {
        List<Review> list = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setInt(1, param);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractReviewFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve reviews.", e);
        }
        return list;
    }

    private Review extractReviewFromResultSet(ResultSet rs) throws SQLException {
        return new Review(
            rs.getInt("reviewId"),
            rs.getInt("userId"),
            rs.getInt("restaurantId"),
            rs.getInt("rating"),
            rs.getString("comment"),
            rs.getTimestamp("reviewDate")
        );
    }
}