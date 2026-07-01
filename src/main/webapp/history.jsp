<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.tap.Model.Order" %>

<%
    // Security check & User Info
    Integer userId = (Integer) session.getAttribute("loggedInUserId");
    if (userId == null) {
        response.sendRedirect("login.html");
        return;
    }
    String userName = (String) session.getAttribute("loggedInUserName");
    String firstName = (userName != null && userName.contains(" ")) ? userName.substring(0, userName.indexOf(" ")) : userName;

    // Fetch History
    List<Order> history = (List<Order>) request.getAttribute("orderHistory");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>My Orders | Food Auto</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800&display=swap');
        
        :root { 
            --zomato-red: #cb202d; 
            --text-main: #1c1c1c; 
            --text-muted: #696969; 
            --bg-color: #f8f9fa; 
        }
        
        * { margin: 0; padding: 0; box-sizing: border-box; font-family: 'Poppins', sans-serif; }
        body { background-color: var(--bg-color); color: var(--text-main); }

        /* Premium Sticky Navbar */
        .navbar {
            background-color: white; padding: 15px 5%; display: flex;
            justify-content: space-between; align-items: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05); position: sticky; top: 0; z-index: 1000;
        }
        .logo { font-size: 32px; font-weight: 800; color: var(--zomato-red); letter-spacing: -1.5px; text-decoration: none; }
        .nav-actions { display: flex; align-items: center; gap: 25px; }
        .user-greeting { font-weight: 500; color: var(--text-muted); }
        .nav-link { text-decoration: none; color: var(--text-main); font-weight: 500; transition: 0.3s; }
        .nav-link:hover { color: var(--zomato-red); }

        /* Page Container */
        .container { max-width: 900px; margin: 50px auto; padding: 0 20px; }
        .header-section { display: flex; justify-content: space-between; align-items: flex-end; margin-bottom: 30px; }
        h1 { font-size: 32px; font-weight: 700; }
        .subtitle { color: var(--text-muted); font-size: 15px; }

        /* The Premium Card */
        .order-card { 
            background: white; border-radius: 16px; margin-bottom: 20px; 
            box-shadow: 0 4px 15px rgba(0,0,0,0.03); transition: 0.3s;
            display: flex; align-items: center; padding: 20px; gap: 20px;
            border: 1px solid #f1f1f1;
        }
        .order-card:hover { transform: translateY(-3px); box-shadow: 0 12px 25px rgba(0,0,0,0.08); border-color: #e8e8e8; }
        
        .order-img {
            width: 80px; height: 80px; border-radius: 12px; object-fit: cover;
            background-color: #f0f0f0; flex-shrink: 0;
        }

        .order-meta { flex-grow: 1; }
        .order-meta h3 { font-size: 18px; font-weight: 600; margin-bottom: 4px; }
        .order-meta p { font-size: 13px; color: var(--text-muted); margin: 0; display: flex; gap: 10px; align-items: center; }
        
        .right-section { text-align: right; min-width: 120px; }
        .price-tag { font-size: 18px; font-weight: 700; margin-bottom: 8px; }
        
        /* Dynamic Status Pills */
        .status-pill { 
            padding: 5px 12px; border-radius: 6px; font-size: 12px; font-weight: 600; text-transform: uppercase; letter-spacing: 0.5px; display: inline-block;
        }
        .status-delivered { background: #e6f4ea; color: #1e8e3e; }
        .status-pending { background: #fef7e0; color: #b06000; }
        .status-cancelled { background: #fce8e6; color: #d93025; }
        .status-default { background: #f1f3f4; color: #5f6368; }

        /* Empty State */
        .empty-state { text-align: center; padding: 80px 20px; background: white; border-radius: 16px; box-shadow: 0 4px 15px rgba(0,0,0,0.03); }
        .empty-state-icon { font-size: 60px; margin-bottom: 20px; }
        .empty-state h2 { font-size: 24px; margin-bottom: 10px; }
        .empty-state p { color: var(--text-muted); margin-bottom: 30px; }
        .btn-primary { background-color: var(--zomato-red); color: white; padding: 12px 30px; border-radius: 8px; text-decoration: none; font-weight: 600; transition: 0.3s; }
        .btn-primary:hover { background-color: #a81a25; }
    </style>
</head>
<body>

    <nav class="navbar">
        <a href="home.jsp" class="logo">Food Auto</a>
        <div class="nav-actions">
            <span class="user-greeting">Welcome, <b><%= firstName != null ? firstName : "Foodie" %></b></span>
            <a href="home.jsp" class="nav-link">Home</a>
            <a href="cart.jsp" class="nav-link">My Cart</a>
            <a href="LogoutServlet" class="nav-link" style="border: 1px solid #ddd; padding: 5px 15px; border-radius: 6px;">Logout</a>
        </div>
    </nav>

    <div class="container">
        <div class="header-section">
            <div>
                <h1>Past Orders</h1>
                <p class="subtitle">Review your previous meals and cravings.</p>
            </div>
        </div>
        
        <% if(history != null && !history.isEmpty()) { 
            for(Order o : history) { 
                // Determine the correct CSS class for the status badge
                String statusClass = "status-default";
                String currentStatus = (o.getStatus() != null) ? o.getStatus().toLowerCase() : "";
                
                if(currentStatus.contains("delivered") || currentStatus.contains("completed")) statusClass = "status-delivered";
                else if(currentStatus.contains("pending") || currentStatus.contains("processing")) statusClass = "status-pending";
                else if(currentStatus.contains("cancelled") || currentStatus.contains("failed")) statusClass = "status-cancelled";
        %>
                <div class="order-card">
                    <img src="https://images.unsplash.com/photo-1504674900247-0877df9cc836?q=80&w=200&auto=format&fit=crop" class="order-img" alt="Food Order">
                    
                   <div class="order-meta">
    <% 
        // 1. Setup the DAOs
        com.tap.DAO.OrderItemDAO itemDAO = new com.tap.DAOImple.OrderItemDAOImple();
        com.tap.DAO.MenuDAO menuDAO = new com.tap.DAOImple.MenuDAOImple();
        
        // 2. Get the items for this specific order
        java.util.List<com.tap.Model.OrderItem> items = itemDAO.getOrderItemsByOrderId(o.getOrderId());
        
        String itemSummary = "";
        if (items != null && !items.isEmpty()) {
            for (com.tap.Model.OrderItem item : items) {
                // 3. Fetch the actual Menu object using the menuId
                com.tap.Model.Menu menuItem = menuDAO.getMenu(item.getMenuId());
                
                // 4. Extract the name and build the string
                if (menuItem != null) {
                    itemSummary += item.getQuantity() + "x " + menuItem.getItemName() + ", ";
                }
            }
            // Remove the trailing comma and space
            if(itemSummary.length() > 2) {
                itemSummary = itemSummary.substring(0, itemSummary.length() - 2); 
            }
        }
        
        // Fallback if the string is still empty
        if (itemSummary.isEmpty()) {
            itemSummary = "Delicious meal from Food Auto"; 
        }
    %>
    
    <!-- The food names are now the main title -->
    <h3 title="<%= itemSummary %>" style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis; max-width: 250px;">
        <%= itemSummary %>
    </h3>
    
    <!-- Order ID and Date become the subtitle -->
    <p>
        <span>Order #<%= o.getOrderId() %></span> &nbsp;•&nbsp; 
        <span>📅 <%= o.getOrderDate() %></span>
    </p>
</div>
                   <div class="right-section">
    <div class="price-tag">₹<%= String.format("%.2f", o.getTotalAmount()) %></div>
    
    <% 
        String currentStat = o.getStatus() != null ? o.getStatus() : "";
        if (currentStat.equalsIgnoreCase("Delivered")) { 
    %>
    <% if (!currentStat.equalsIgnoreCase("Delivered") && !currentStat.equalsIgnoreCase("Reviewed") && !currentStat.equalsIgnoreCase("Cancelled")) { %>
    <a href="TrackOrderServlet?orderId=<%= o.getOrderId() %>" style="display: block; margin-bottom: 12px; font-size: 13px; color: var(--success); text-decoration: none; font-weight: 600;">📍 Track Order</a>
<% } %>
        <form action="RateOrderServlet" method="POST" style="margin-bottom: 10px;">
            <input type="hidden" name="orderId" value="<%= o.getOrderId() %>">
            <input type="hidden" name="restaurantId" value="<%= o.getRestaurantId() %>">
            
            <div style="display: flex; gap: 5px; align-items: center; justify-content: flex-end;">
                <select name="rating" style="padding: 4px; border-radius: 4px; border: 1px solid #ddd; font-size: 12px;">
                    <option value="5">5 ⭐</option>
                    <option value="4">4 ⭐</option>
                    <option value="3">3 ⭐</option>
                    <option value="2">2 ⭐</option>
                    <option value="1">1 ⭐</option>
                </select>
                <button type="submit" style="background-color: #f5b041; color: white; border: none; padding: 5px 10px; border-radius: 6px; font-weight: 600; cursor: pointer; font-size: 12px; transition: 0.3s;">Rate</button>
            </div>
        </form>
    <% } else if (currentStat.equalsIgnoreCase("Reviewed")) { %>
        <span class="status-pill" style="background: #fff3cd; color: #856404; display: inline-block; margin-bottom: 10px;">⭐ Reviewed</span>
    <% } else { %>
        <span class="status-pill <%= statusClass %>" style="display: inline-block; margin-bottom: 10px;"><%= o.getStatus() %></span>
    <% } %>

    <a href="OrderDetailsServlet?orderId=<%= o.getOrderId() %>" style="display: block; margin-bottom: 12px; font-size: 13px; color: var(--text-muted); text-decoration: none; font-weight: 500;">View Receipt ➔</a>
    
    <form action="ReorderServlet" method="POST">
        <input type="hidden" name="orderId" value="<%= o.getOrderId() %>">
        <input type="hidden" name="restaurantId" value="<%= o.getRestaurantId() %>">
        
        <button type="submit" style="background-color: var(--zomato-red); color: white; border: none; padding: 8px 16px; border-radius: 6px; font-weight: 600; font-size: 13px; width: 100%; cursor: pointer; transition: 0.3s; box-shadow: 0 4px 10px rgba(203, 32, 45, 0.2);">
            ↻ Reorder
        </button>
    </form>
</div>                </div>
        <% } } else { %>
            <div class="empty-state">
                <div class="empty-state-icon">🧾</div>
                <h2>No orders found</h2>
                <p>Looks like you haven't placed any orders yet. Let's find you something delicious!</p>
                <a href="home.jsp" class="btn-primary">Explore Restaurants</a>
            </div>
        <% } %>
    </div>

</body>
</html>