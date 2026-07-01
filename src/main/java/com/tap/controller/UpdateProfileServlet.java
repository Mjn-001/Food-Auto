package com.tap.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.tap.DAO.UserDAO;
import com.tap.DAOImple.UserDAOImple;
import com.tap.Model.User;

@WebServlet("/UpdateProfileServlet")
public class UpdateProfileServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("loggedInUserId");
        
        if (userId == null) {
            response.sendRedirect("login.html");
            return;
        }

        // 1. Fetch form data
        String newName = request.getParameter("name");
        String newPhone = request.getParameter("phone");
        String newPassword = request.getParameter("password"); // Might be empty

        // 2. Get the current user from DB
        UserDAO userDAO = new UserDAOImple();
        User user = userDAO.getUser(userId);

        if (user != null) {
            // 3. Update the fields
            user.setUserName(newName);
            user.setPhone(newPhone);
            
            // Only update password if they actually typed something new
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                user.setPassword(newPassword);
            }

            // 4. Save back to database (Make sure you have an updateUser method in UserDAO!)
            userDAO.updateUser(user);

            // 5. Update session variables so the Navbar instantly shows the new name
            session.setAttribute("loggedInUserName", newName);
            
            // 6. Redirect back with a success message
            response.sendRedirect("profile.jsp?success=true");
        } else {
            response.sendRedirect("home.jsp");
        }
    }
}