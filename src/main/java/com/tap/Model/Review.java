package com.tap.Model;

import java.sql.Timestamp;

public class Review {
    
    private int reviewId;
    private int userId;
    private int restaurantId;
    private int rating;
    private String comment;
    private Timestamp reviewDate;

    // 1. Default Constructor
    public Review() {
    }

    // 2. Parameterized Constructor (Used when a user submits a new review)
    public Review(int userId, int restaurantId, int rating, String comment) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.rating = rating;
        this.comment = comment;
    }

    // 3. Fully Parameterized Constructor (Used when fetching from DB)
    public Review(int reviewId, int userId, int restaurantId, int rating, String comment, Timestamp reviewDate) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }

    // 4. Getters and Setters
    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Timestamp getReviewDate() { return reviewDate; }
    public void setReviewDate(Timestamp reviewDate) { this.reviewDate = reviewDate; }

    // 5. toString() for debugging
    @Override
    public String toString() {
        return "Review {" +
                "ReviewID=" + reviewId +
                ", UserID=" + userId +
                ", RestaurantID=" + restaurantId +
                ", Rating=" + rating + " Stars" +
                ", Comment='" + comment + '\'' +
                ", Date=" + reviewDate +
                '}';
    }
}