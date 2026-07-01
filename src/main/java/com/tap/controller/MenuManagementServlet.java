package com.tap.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.tap.DAO.MenuDAO;
import com.tap.DAOImple.MenuDAOImple;
import com.tap.Model.Menu;

@WebServlet("/MenuManagementServlet")
public class MenuManagementServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        MenuDAO menuDAO = new MenuDAOImple();

        try {
            if ("add".equals(action)) {
                // 1. Extract parameters from the form
                int rId = Integer.parseInt(request.getParameter("restaurantId"));
                String name = request.getParameter("itemName");
                String desc = request.getParameter("description");
                double price = Double.parseDouble(request.getParameter("price"));
                String category = request.getParameter("category");
                boolean available = Boolean.parseBoolean(request.getParameter("isAvailable"));

                // 2. Use your 7-parameter constructor. 
                // We pass 0 for menuId because the database auto-increments it.
                Menu m = new Menu(0, rId, name, desc, price, category, available);
                
                // 3. Save to database
                menuDAO.addMenu(m);
                
            } else if ("delete".equals(action)) {
                // 1. Get ID
                int menuId = Integer.parseInt(request.getParameter("menuId"));
                
                // 2. Delete from database
                menuDAO.deleteMenu(menuId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Optional: redirect to an error page or back to admin-menu with an error message
        }

        // Always redirect back to the menu list so the Admin sees the updated data
        response.sendRedirect("admin-menu.jsp");
    }
}