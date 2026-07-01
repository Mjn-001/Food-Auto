package com.tap.Model;

public class Cart {
    
    private int cartId;
    private int userId; // Foreign Key to User

    // Default Constructor
    public Cart() {
    }

    // Parameterized Constructor (Without cartId, DB auto-increments)
    public Cart(int userId) {
        this.userId = userId;
    }

    // Fully Parameterized Constructor
    public Cart(int cartId, int userId) {
        this.cartId = cartId;
        this.userId = userId;
    }

    // Getters and Setters
    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    @Override
    public String toString() {
        return "Cart {" +
                "CartID=" + cartId +
                ", UserID=" + userId +
                '}';
    }
}