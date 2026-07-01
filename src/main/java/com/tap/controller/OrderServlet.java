package com.tap.controller;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.tap.Model.*;
import com.tap.DAO.*;
import com.tap.DAOImple.*;

@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // 1. Retrieve data from Session
        Integer userId = (Integer) session.getAttribute("loggedInUserId");
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
        Double totalAmount = (Double) session.getAttribute("checkoutTotal");
        
        if (userId == null || cartItems == null || cartItems.isEmpty()) {
            response.sendRedirect("home.jsp");
            return;
        }

        // 2. Initialize DAOs
        MenuDAO menuDAO = new MenuDAOImple();
        OrderDAO orderDAO = new OrderDAOImple();

        // 3. Get RestaurantID from the first item in the cart
        // (Assuming all items in a single cart belong to the same restaurant)
        int menuId = cartItems.get(0).getMenuId();
        int restaurantId = menuDAO.getMenu(menuId).getRestaurantId();

        // 4. Create and Save the Order (The "Master" record)
        // Status is set to 'PLACED' as per your database structure
        Order order = new Order(userId, restaurantId, totalAmount, "PLACED");
        orderDAO.addOrder(order); // This also sets order.setOrderId() inside the DAO via generated keys

        // 5. Save the Order Items (The "Details")
        for (CartItem item : cartItems) {
            Menu menu = menuDAO.getMenu(item.getMenuId());
            double itemTotal = menu.getPrice() * item.getQuantity();
            
            OrderItem orderItem = new OrderItem(order.getOrderId(), item.getMenuId(), item.getQuantity(), itemTotal);
            orderDAO.addOrderItem(orderItem);
        }

        // 6. Cleanup & Success Redirect
        session.removeAttribute("cartItems");
        session.removeAttribute("checkoutTotal");
        session.setAttribute("latestOrderId", order.getOrderId());
        
        response.sendRedirect("order-success.jsp");
    }
}