package com.tap.controller;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.tap.Model.CartItem;

@WebServlet("/UpdateCartServlet")
public class UpdateCartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
        String action = request.getParameter("action");

        if ("clear".equals(action)) {
            session.removeAttribute("cartItems");
        } else if (cartItems != null) {
            int menuId = Integer.parseInt(request.getParameter("menuId"));
            for (CartItem item : cartItems) {
                if (item.getMenuId() == menuId) {
                    if ("increase".equals(action)) {
                        item.setQuantity(item.getQuantity() + 1);
                    } else if ("decrease".equals(action)) {
                        item.setQuantity(item.getQuantity() - 1);
                        if (item.getQuantity() <= 0) {
                            cartItems.remove(item);
                        }
                    }
                    break;
                }
            }
            session.setAttribute("cartItems", cartItems);
        }
        response.sendRedirect("cart.jsp");
    }
}