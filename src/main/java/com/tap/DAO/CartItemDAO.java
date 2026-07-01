package com.tap.DAO;

import java.util.List;
import com.tap.Model.CartItem;

public interface CartItemDAO {
    void addCartItem(CartItem cartItem);
    CartItem getCartItem(int cartItemId);
    void updateCartItem(CartItem cartItem); // Used for changing the quantity (+ / -)
    void deleteCartItem(int cartItemId);    // Used for removing an item from the cart
    List<CartItem> getCartItemsByCartId(int cartId);
}