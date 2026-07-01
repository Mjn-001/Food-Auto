package com.tap.controller;

import java.io.IOException;

// Using jakarta for Tomcat 10+
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.tap.DAO.UserDAO;
import com.tap.DAOImple.UserDAOImple;
import com.tap.Model.User;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. Get the data typed into the HTML form
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        // 2. Ask the DAO to check the database
        UserDAO userDAO = new UserDAOImple();
        User loggedInUser = userDAO.loginUser(email, password);
        
        if (loggedInUser != null) {
            // 3. SUCCESS! Generate the Session wristband
            HttpSession session = request.getSession();
            
            session.setAttribute("loggedInUserId", loggedInUser.getUserId());
            session.setAttribute("loggedInUserName", loggedInUser.getUserName());
            
            // Handle role (default to Customer if the database column is empty)
            String role = loggedInUser.getRole() != null ? loggedInUser.getRole() : "Customer";
            session.setAttribute("userRole", role);
            
            // 4. THE TRAFFIC COP (Role-Based Routing)
            if (role.equalsIgnoreCase("Admin")) {
                // Send restaurant owners straight to the Kitchen Display
                response.sendRedirect("AdminDashboardServlet");
            } else {
                // Send normal hungry customers to the Home Page
                response.sendRedirect("home.jsp"); 
            }
            
        } else {
            // 5. FAILED! Send them back to the premium login page with an error flag
            // This replaces your old PrintWriter HTML code
            response.sendRedirect("login.jsp?error=invalid");
        }
    }
}