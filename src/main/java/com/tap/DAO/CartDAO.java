package com.tap.DAO;

import com.tap.Model.Cart;

public interface CartDAO {
    void addCart(Cart cart);
    Cart getCart(int cartId);
    Cart getCartByUserId(int userId); // Essential for finding a user's session cart
    void deleteCart(int cartId);
}