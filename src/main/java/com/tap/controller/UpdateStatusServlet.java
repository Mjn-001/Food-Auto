package com.tap.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.tap.DAO.OrderDAO;
import com.tap.DAOImple.OrderDAOImple;

@WebServlet("/UpdateStatusServlet")
public class UpdateStatusServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. Grab the hidden Order ID and the selected Status from the dashboard form
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String newStatus = request.getParameter("newStatus");


        // ADD THESE TWO LINES:
        System.out.println("SERVLET HIT! Trying to update Order ID: " + orderId);
        System.out.println("SERVLET HIT! New Status received: " + newStatus);
        
        // 2. Tell the Database to update this specific order
        OrderDAO orderDAO = new OrderDAOImple();
        orderDAO.updateOrderStatus(orderId, newStatus);
        
        // 3. Immediately refresh the Admin Dashboard so the UI updates
        response.sendRedirect("AdminDashboardServlet");
    }
}