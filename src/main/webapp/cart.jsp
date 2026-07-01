<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tap.Model.Cart" %>
<%@ page import="com.tap.Model.CartItem" %>
<%@ page import="com.tap.Model.Menu" %>
<%@ page import="com.tap.DAO.MenuDAO" %>
<%@ page import="com.tap.DAOImple.MenuDAOImple" %>

<%
    // 1. SECURITY CHECK
    Integer userId = (Integer) session.getAttribute("loggedInUserId");
    if (userId == null) {
        response.sendRedirect("login.html");
        return;
    }
    
    String userName = (String) session.getAttribute("loggedInUserName");
    String firstName = (userName != null && userName.contains(" ")) ? userName.substring(0, userName.indexOf(" ")) : userName;

    // 2. RETRIEVE CART ITEMS
    List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
    
    // 3. DAO SETUP
    MenuDAO menuDAO = new MenuDAOImple();
    double grandTotal = 0.0;
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>My Cart - Zomato</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800&display=swap');
        :root { --zomato-red: #cb202d; --text-main: #1c1c1c; --text-muted: #696969; --bg-color: #f8f9fa; }
        * { margin: 0; padding: 0; box-sizing: border-box; font-family: 'Poppins', sans-serif; }
        body { background-color: var(--bg-color); color: var(--text-main); }

        .navbar { background-color: white; padding: 15px 5%; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
        .logo { font-size: 32px; font-weight: 800; color: var(--zomato-red); text-decoration: none; }
        .cart-container { max-width: 900px; margin: 50px auto; padding: 0 20px; }
        .cart-table { width: 100%; border-collapse: collapse; background: white; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 15px rgba(0,0,0,0.03); }
        .cart-table th, .cart-table td { padding: 20px; border-bottom: 1px solid #eee; text-align: left; }
        
        .qty-btn { padding: 5px 12px; border: 1px solid #ddd; background: #fff; cursor: pointer; border-radius: 4px; font-weight: bold; }
        .qty-btn:hover { background: #f8f8f8; }
        
        .totals-section { background: white; padding: 30px; border-radius: 12px; margin-top: 30px; text-align: right; box-shadow: 0 4px 15px rgba(0,0,0,0.03); }
        .grand-total { font-size: 24px; font-weight: 700; margin-bottom: 20px; }
        .btn-primary { background-color: var(--zomato-red); color: white; padding: 12px 30px; border-radius: 8px; text-decoration: none; display: inline-block; }
        .btn-secondary { background-color: #f1f1f1; border: none; padding: 12px 30px; border-radius: 8px; cursor: pointer; margin-right: 15px; }
    </style>
</head>
<body>

    <nav class="navbar"><a href="home.jsp" class="logo">Food Auto</a></nav>

    <main class="cart-container">
        <h1>Your Order Summary</h1>
        
        <% if (cartItems != null && !cartItems.isEmpty()) { %>
            <table class="cart-table">
                <thead><tr><th>Item Details</th><th>Price</th><th>Quantity</th><th>Subtotal</th></tr></thead>
                <tbody>
                    <% for (CartItem item : cartItems) {
                        Menu menuItem = menuDAO.getMenu(item.getMenuId());
                        if(menuItem != null) {
                            double subtotal = menuItem.getPrice() * item.getQuantity();
                            grandTotal += subtotal;
                    %>
                        <tr>
                            <td><%= menuItem.getItemName() %></td>
                            <td>₹<%= menuItem.getPrice() %></td>
                            <td>
                                <form action="UpdateCartServlet" method="POST" style="display:flex; gap:10px; align-items:center;">
                                    <input type="hidden" name="menuId" value="<%= item.getMenuId() %>">
                                    <button type="submit" name="action" value="decrease" class="qty-btn">-</button>
                                    <strong><%= item.getQuantity() %></strong>
                                    <button type="submit" name="action" value="increase" class="qty-btn">+</button>
                                </form>
                            </td>
                            <td>₹<%= String.format("%.2f", subtotal) %></td>
                        </tr>
                    <% } } %>
                </tbody>
            </table>
            
            <div class="totals-section">
                <div class="grand-total">Total Amount: ₹<%= String.format("%.2f", grandTotal) %></div>
                
                <form action="UpdateCartServlet" method="POST" style="display:inline;">
                    <button type="submit" name="action" value="clear" class="btn-secondary">Clear Cart</button>
                </form>
                
                <a href="home.jsp" class="btn-secondary" style="text-decoration:none; color:black;">Add More</a>
                <a href="checkout.jsp" class="btn-primary">Proceed to Checkout ➔</a>
            </div>
        <% } else { %>
    <div style="text-align:center; padding:80px 20px; background:white; border-radius:12px; box-shadow: 0 4px 15px rgba(0,0,0,0.03);">
        <div style="font-size: 60px; margin-bottom: 20px;">🍕</div>
        
        <h2 style="margin-bottom: 10px;">Your cart is feeling empty!</h2>
        <p style="color: var(--text-muted); margin-bottom: 30px;">Looks like you haven't added anything to your cart yet.</p>
        
        <a href="home.jsp" class="btn-primary">Browse Restaurants</a>
    </div>
<% } %>+
    </main>
</body>
</html>