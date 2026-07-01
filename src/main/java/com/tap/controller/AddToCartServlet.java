package com.tap.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// USING JAKARTA IMPORTS
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.tap.Model.CartItem;

@WebServlet("/AddToCartServlet")
public class AddToCartServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Fetch cartItems from session
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
        
        // Catch the parameters
        int menuId = Integer.parseInt(request.getParameter("menuId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        
        // Check if item exists in list
        boolean itemExists = false;
        for (CartItem item : cartItems) {
            if (item.getMenuId() == menuId) {
                item.setQuantity(item.getQuantity() + quantity);
                itemExists = true;
                break;
            }
        }
        
        if (!itemExists) {
            // tempCartId is 0 because the order isn't in DB yet
            CartItem newItem = new CartItem(0, menuId, quantity);
            cartItems.add(newItem);
        }
        
        session.setAttribute("cartItems", cartItems);
        response.sendRedirect("cart.jsp");
    }
}