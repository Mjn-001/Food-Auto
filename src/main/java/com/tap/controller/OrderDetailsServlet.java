package com.tap.controller;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.tap.DAO.*;
import com.tap.DAOImple.*;
import com.tap.Model.*;

@WebServlet("/OrderDetailsServlet")
public class OrderDetailsServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Security Check
        if (request.getSession().getAttribute("loggedInUserId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        // 2. Get the Order ID from the URL
        int orderId = Integer.parseInt(request.getParameter("orderId"));

        // 3. Initialize your DAOs
        OrderDAO orderDAO = new OrderDAOImple();
        OrderItemDAO itemDAO = new OrderItemDAOImple();
        RestaurantDAO restaurantDAO = new RestaurantDAOImple();

        // 4. Fetch the data
        Order order = orderDAO.getOrder(orderId);
        List<OrderItem> items = itemDAO.getOrderItemsByOrderId(orderId);
        
        // Fetch restaurant details to make the receipt look premium
        Restaurant restaurant = null;
        if (order != null) {
            restaurant = restaurantDAO.getRestaurant(order.getRestaurantId());
        }

        // 5. Attach data to the request
        request.setAttribute("order", order);
        request.setAttribute("orderItems", items);
        request.setAttribute("restaurant", restaurant);

        // 6. Forward to the premium receipt page
        request.getRequestDispatcher("order-details.jsp").forward(request, response);
    }
}