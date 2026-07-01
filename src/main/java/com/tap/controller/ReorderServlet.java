package com.tap.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.tap.DAO.OrderItemDAO;
import com.tap.DAOImple.OrderItemDAOImple;
import com.tap.Model.OrderItem;
import com.tap.Model.CartItem;

@WebServlet("/ReorderServlet")
public class ReorderServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // 1. Security Check
        if (session.getAttribute("loggedInUserId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        // 2. Get the target Order and Restaurant IDs
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        int restaurantId = Integer.parseInt(request.getParameter("restaurantId"));

        // 3. Fetch the historical items for this specific order
        OrderItemDAO itemDAO = new OrderItemDAOImple();
        List<OrderItem> pastItems = itemDAO.getOrderItemsByOrderId(orderId);

        // 4. Create a fresh Cart
        List<CartItem> newCart = new ArrayList<>();
        
        if (pastItems != null) {
            for (OrderItem pastItem : pastItems) {
                // Convert the old OrderItem into a fresh CartItem
                CartItem freshItem = new CartItem();
                freshItem.setMenuId(pastItem.getMenuId());
                freshItem.setQuantity(pastItem.getQuantity());
                
                // Note: We don't set the old price here! 
                // The cart/checkout JSP will fetch the current, live price using the menuId.
                
                newCart.add(freshItem);
            }
        }

        // 5. Overwrite the current session cart and set the active restaurant
        session.setAttribute("cartItems", newCart);
        session.setAttribute("currentRestaurantId", restaurantId);

        // 6. Fast-track the user straight to the cart or checkout
        // (Sending them to cart.jsp allows them to review live prices before paying)
        response.sendRedirect("cart.jsp"); 
    }
}