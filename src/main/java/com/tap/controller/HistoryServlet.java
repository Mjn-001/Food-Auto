package com.tap.controller;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.tap.DAO.OrderDAO;
import com.tap.DAOImple.OrderDAOImple;
import com.tap.Model.Order;

@WebServlet("/HistoryServlet")
public class HistoryServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("loggedInUserId");
        
        if (userId == null) {
            response.sendRedirect("login.html");
            return;
        }
        
        OrderDAO orderDAO = new OrderDAOImple();
        List<Order> history = orderDAO.getOrdersByUserId(userId);
        
        request.setAttribute("orderHistory", history);
        request.getRequestDispatcher("history.jsp").forward(request, response);
    }
}