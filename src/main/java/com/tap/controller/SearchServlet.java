package com.tap.controller;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.tap.DAO.RestaurantDAO;
import com.tap.DAOImple.RestaurantDAOImple;
import com.tap.Model.Restaurant;

@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Get the search term from the URL
        String query = request.getParameter("searchQuery");
        
        // 2. Fetch data from the database using your DAO
        RestaurantDAO dao = new RestaurantDAOImple();
        List<Restaurant> results = dao.searchRestaurants(query);
        
        // 3. Attach the list of results to the request object
        // The JSP will read this "restaurants" attribute
        request.setAttribute("restaurants", results);
        
        // 4. Forward the request to your JSP page instead of printing HTML
        request.getRequestDispatcher("search-results.jsp").forward(request, response);
    }
}