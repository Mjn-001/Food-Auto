package com.tap.Model;

public class CartItem {
    
    private int cartItemId;
    private int cartId;   // Foreign Key to Cart
    private int menuId;   // Foreign Key to Menu
    private int quantity; 

    // Default Constructor
    public CartItem() {
    }

    // Parameterized Constructor (Without cartItemId)
    public CartItem(int cartId, int menuId, int quantity) {
        this.cartId = cartId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    // Fully Parameterized Constructor
    public CartItem(int cartItemId, int cartId, int menuId, int quantity) {
        this.cartItemId = cartItemId;
        this.cartId = cartId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getCartItemId() { return cartItemId; }
    public void setCartItemId(int cartItemId) { this.cartItemId = cartItemId; }

    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }

    public int getMenuId() { return menuId; }
    public void setMenuId(int menuId) { this.menuId = menuId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return "CartItem {" +
                "ItemID=" + cartItemId +
                ", CartID=" + cartId +
                ", MenuID=" + menuId +
                ", Quantity=" + quantity +
                '}';
    }
}