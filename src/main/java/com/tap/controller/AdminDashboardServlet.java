package com.tap.controller;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.tap.DAO.OrderDAO;
import com.tap.DAOImple.OrderDAOImple;
import com.tap.Model.Order;

@WebServlet("/AdminDashboardServlet")
public class AdminDashboardServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Optional: Add a check here to ensure the logged-in user is actually an ADMIN.
        // For testing purposes, we will just ensure someone is logged in.
        if (request.getSession().getAttribute("loggedInUserId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        OrderDAO orderDAO = new OrderDAOImple();
        
        // Fetch EVERY order in the system
        List<Order> allOrders = orderDAO.getAllOrders();
        
        request.setAttribute("allOrders", allOrders);
        request.getRequestDispatcher("admin-dashboard.jsp").forward(request, response);
    }
}