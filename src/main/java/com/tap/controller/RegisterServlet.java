package com.tap.controller;

import java.io.IOException;
import java.io.PrintWriter;

// Notice the change from javax to jakarta here!
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.tap.DAO.UserDAO;
import com.tap.DAOImple.UserDAOImple;
import com.tap.Model.User;
import com.tap.Utility.DatabaseException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        try {
            // Capture the data typed into the HTML form
            String name = request.getParameter("userName");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String role = request.getParameter("role");
            
            // Package the data into our POJO
            User newUser = new User(name, password, email, phone, address, role);
            
            // Send the POJO to the DAO to be saved in MySQL
            UserDAO userDAO = new UserDAOImple();
            userDAO.addUser(newUser);
            response.sendRedirect("registration-success.jsp?name=" + name);
            
            // Send a success message back to the browser
            out.println("<html><body>");
            out.println("<h2 style='color: green;'>Registration Successful!</h2>");
            out.println("<p>Welcome to Zomato, " + name + "!</p>");
            out.println("<a href='login.html'>Click here to login</a>");
            out.println("</body></html>");
            
        } catch (DatabaseException e) {
            // If the database fails (e.g., email already exists), tell the user safely
            out.println("<html><body>");
            out.println("<h2 style='color: red;'>Registration Failed</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='Register.html'>Try Again</a>");
            out.println("</body></html>");
        }
    }
}