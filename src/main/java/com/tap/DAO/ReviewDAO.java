package com.tap.DAO;

import java.util.List;
import com.tap.Model.Review;

public interface ReviewDAO {
    void addReview(Review review);
    Review getReview(int reviewId);
    void updateReview(Review review);
    void deleteReview(int reviewId);
    
    // Application-critical methods
    List<Review> getReviewsByRestaurantId(int restaurantId);
    List<Review> getReviewsByUserId(int userId);
}