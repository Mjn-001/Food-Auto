package com.tap.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.tap.DAO.OrderDAO;
import com.tap.DAOImple.OrderDAOImple;
import com.tap.Model.Order;

@WebServlet("/TrackOrderServlet")
public class TrackOrderServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Security check
        if (request.getSession().getAttribute("loggedInUserId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            
            // Fetch the live order data
            OrderDAO orderDAO = new OrderDAOImple();
            Order order = orderDAO.getOrder(orderId);
            
            if (order != null) {
                request.setAttribute("trackOrder", order);
                request.getRequestDispatcher("track-order.jsp").forward(request, response);
            } else {
                response.sendRedirect("HistoryServlet");
            }
        } catch (Exception e) {
            response.sendRedirect("HistoryServlet");
        }
    }
}