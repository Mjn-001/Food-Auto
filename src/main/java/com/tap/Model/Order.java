package com.tap.Model;

import java.sql.Timestamp;

public class Order {
    
    private int orderId;
    private int userId;
    private int restaurantId;
    private Timestamp orderDate;
    private double totalAmount;
    private String status;

    // Default Constructor
    public Order() {
    }

    // Parameterized Constructor (Used when a user first places an order)
    public Order(int userId, int restaurantId, double totalAmount, String status) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Fully Parameterized Constructor (Used when reading history from the DB)
    public Order(int orderId, int userId, int restaurantId, Timestamp orderDate, double totalAmount, String status) {
        this.orderId = orderId;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Getters and Setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

    public Timestamp getOrderDate() { return orderDate; }
    public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Order {" +
                "OrderID=" + orderId +
                ", UserID=" + userId +
                ", RestaurantID=" + restaurantId +
                ", Date=" + orderDate +
                ", Total=₹" + totalAmount +
                ", Status='" + status + '\'' +
                '}';
    }
}