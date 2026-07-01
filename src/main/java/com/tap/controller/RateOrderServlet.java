package com.tap.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.tap.DAO.OrderDAO;
import com.tap.DAOImple.OrderDAOImple;
import com.tap.DAO.RestaurantDAO;
import com.tap.DAOImple.RestaurantDAOImple;
import com.tap.Model.Restaurant;

@WebServlet("/RateOrderServlet")
public class RateOrderServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. Security Check
        if (request.getSession().getAttribute("loggedInUserId") == null) {
            response.sendRedirect("login.html");
            return;
        }

        // 2. Fetch the form data
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        int restaurantId = Integer.parseInt(request.getParameter("restaurantId"));
        double userRating = Double.parseDouble(request.getParameter("rating"));

        RestaurantDAO restaurantDAO = new RestaurantDAOImple();
        OrderDAO orderDAO = new OrderDAOImple();

        // 3. Get the current restaurant data
        Restaurant restaurant = restaurantDAO.getRestaurant(restaurantId);

        if (restaurant != null) {
            // 4. Calculate the new average
            // If the restaurant has a 0.0 rating, just take the user's rating.
            // Otherwise, average the current rating and the new rating together.
            double currentRating = restaurant.getRating();
            double newAverage = (currentRating == 0.0) ? userRating : ((currentRating + userRating) / 2.0);

            // Round it cleanly to one decimal place (e.g., 4.256 becomes 4.3)
            newAverage = Math.round(newAverage * 10.0) / 10.0;

            // 5. Save the new rating to the Restaurant database
            restaurant.setRating(newAverage);
            restaurantDAO.updateRestaurant(restaurant);

            // 6. Change order status to "Reviewed" so they cannot rate it again
            orderDAO.updateOrderStatus(orderId, "Reviewed");
        }

        // 7. Refresh the page to show the gold "⭐ Reviewed" badge
        response.sendRedirect("HistoryServlet");
    }
}