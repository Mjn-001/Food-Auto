<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tap.Model.Order, com.tap.Model.OrderItem, com.tap.Model.Menu" %>
<%@ page import="com.tap.DAO.OrderItemDAO, com.tap.DAOImple.OrderItemDAOImple" %>
<%@ page import="com.tap.DAO.MenuDAO, com.tap.DAOImple.MenuDAOImple" %>

<%
    // Security and Data Fetching
    String role = (String) session.getAttribute("userRole");
    if (role == null || !role.equalsIgnoreCase("Admin")) { response.sendRedirect("home.jsp"); return; }
    
    List<Order> allOrders = (List<Order>) request.getAttribute("allOrders");
    if (allOrders == null) { response.sendRedirect("AdminDashboardServlet"); return; }
    
    OrderItemDAO itemDAO = new OrderItemDAOImple();
    MenuDAO menuDAO = new MenuDAOImple();

    // --- REAL-TIME ANALYTICS ENGINE ---
    int totalOrders = allOrders.size();
    int activeOrders = 0;
    double totalRevenue = 0.0;
    
    for (Order o : allOrders) {
        String stat = o.getStatus() != null ? o.getStatus() : "Pending";
        if (stat.equalsIgnoreCase("Pending") || stat.equalsIgnoreCase("Preparing") || stat.equalsIgnoreCase("Out for Delivery")) {
            activeOrders++;
        }
        if (stat.equalsIgnoreCase("Delivered") || stat.equalsIgnoreCase("Reviewed")) {
            totalRevenue += o.getTotalAmount();
        }
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Command Center | Food Auto</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700&display=swap');
        
        :root { --primary: #cb202d; --bg: #f4f6f8; --surface: #ffffff; --text: #1e293b; --text-muted: #64748b; --border: #e2e8f0; }
        * { margin: 0; padding: 0; box-sizing: border-box; font-family: 'Outfit', sans-serif; }
        body { background: var(--bg); color: var(--text); display: flex; }

        /* --- SIDEBAR --- */
        .sidebar { width: 260px; background: var(--surface); height: 100vh; position: fixed; border-right: 1px solid var(--border); display: flex; flex-direction: column; }
        .brand { padding: 30px 20px; font-size: 26px; font-weight: 800; color: var(--primary); border-bottom: 1px solid var(--border); text-align: center; }
        .nav-menu { padding: 20px 10px; flex-grow: 1; }
        .nav-item { display: flex; align-items: center; gap: 12px; padding: 14px 20px; color: var(--text-muted); text-decoration: none; font-weight: 500; border-radius: 10px; margin-bottom: 5px; transition: 0.3s; }
        .nav-item:hover, .nav-item.active { background: #fff1f2; color: var(--primary); font-weight: 600; }
        .nav-icon { font-size: 18px; }
        
        .sidebar-footer { padding: 20px; border-top: 1px solid var(--border); }
        .admin-profile { display: flex; align-items: center; gap: 10px; }
        .admin-avatar { width: 40px; height: 40px; border-radius: 50%; background: var(--primary); color: white; display: flex; align-items: center; justify-content: center; font-weight: 700; }
        .admin-info h4 { font-size: 14px; color: var(--text); }
        .admin-info p { font-size: 12px; color: var(--text-muted); }

        /* --- MAIN CONTENT --- */
        .main-content { margin-left: 260px; padding: 40px; width: calc(100% - 260px); }
        .header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px; }
        .header h1 { font-size: 28px; font-weight: 700; }
        .header-actions a { background: white; border: 1px solid var(--border); padding: 10px 20px; border-radius: 8px; text-decoration: none; color: var(--text); font-weight: 600; box-shadow: 0 2px 5px rgba(0,0,0,0.02); }

        /* --- ANALYTICS CARDS --- */
        .stats-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 25px; margin-bottom: 40px; }
        .stat-card { background: var(--surface); padding: 25px; border-radius: 16px; box-shadow: 0 4px 15px rgba(0,0,0,0.03); display: flex; align-items: center; gap: 20px; border: 1px solid var(--border); }
        .stat-icon { width: 60px; height: 60px; border-radius: 12px; display: flex; align-items: center; justify-content: center; font-size: 24px; }
        .stat-icon.blue { background: #e0f2fe; color: #0284c7; }
        .stat-icon.green { background: #dcfce7; color: #16a34a; }
        .stat-icon.orange { background: #ffedd5; color: #ea580c; }
        .stat-info p { color: var(--text-muted); font-size: 14px; font-weight: 500; margin-bottom: 5px; }
        .stat-info h3 { font-size: 28px; font-weight: 700; color: var(--text); }

        /* --- KITCHEN TICKETS --- */
        .section-title { font-size: 20px; font-weight: 700; margin-bottom: 20px; padding-bottom: 10px; border-bottom: 2px solid var(--border); }
        .ticket-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 20px; }
        .ticket { background: var(--surface); border-radius: 12px; border: 1px solid var(--border); box-shadow: 0 4px 6px -1px rgba(0,0,0,0.02); display: flex; flex-direction: column; overflow: hidden; }
        .ticket-header { background: #f8fafc; padding: 15px; border-bottom: 1px dashed var(--border); display: flex; justify-content: space-between; align-items: center; }
        .ticket-body { padding: 15px; flex-grow: 1; }
        .food-item { display: flex; justify-content: space-between; margin-bottom: 10px; font-size: 15px; border-bottom: 1px solid #f1f5f9; padding-bottom: 5px; }
        .qty { font-weight: 700; color: var(--primary); background: #fee2e2; padding: 2px 6px; border-radius: 4px; margin-right: 8px; font-size: 13px; }
        
        .ticket-footer { padding: 15px; background: #f8fafc; border-top: 1px solid var(--border); }
        .status-form { display: flex; gap: 8px; }
        .status-select { flex-grow: 1; padding: 8px; border: 1px solid #cbd5e1; border-radius: 6px; font-size: 13px; outline: none; }
        .btn-update { background: var(--text); color: white; border: none; padding: 8px 12px; border-radius: 6px; font-weight: 600; cursor: pointer; }

        .ticket.status-pending { border-left: 5px solid #f59e0b; }
        .ticket.status-preparing { border-left: 5px solid #3b82f6; }
        .ticket.status-out { border-left: 5px solid #8b5cf6; }
    </style>
</head>
<body>

    <aside class="sidebar">
        <div class="brand">Food Auto<span style="color: #0f172a;">.</span></div>
        <nav class="nav-menu">
            <a href="AdminDashboardServlet" class="nav-item active"><span class="nav-icon">📊</span> Dashboard</a>
            <a href="admin-menu.jsp" class="nav-item"><span class="nav-icon">🍔</span> Manage Menu</a>
            <a href="#" class="nav-item"><span class="nav-icon">👥</span> Customers</a>
            <a href="profile.jsp" class="nav-item"><span class="nav-icon">⚙️</span> Settings & Profile</a>
        </nav>
        <div class="sidebar-footer">
            <div class="admin-profile">
                <div class="admin-avatar"><%= ((String)session.getAttribute("loggedInUserName")).substring(0, 1).toUpperCase() %></div>
                <div class="admin-info">
                    <h4><%= session.getAttribute("loggedInUserName") %></h4>
                    <p>Administrator</p>
                </div>
            </div>
            <a href="LogoutServlet" style="display: block; margin-top: 15px; color: #ef4444; text-decoration: none; font-size: 14px; font-weight: 600;">Log Out ➔</a>
        </div>
    </aside>

    <main class="main-content">
        
        <div class="header">
            <div>
                <h1>Dashboard Overview</h1>
                <p style="color: var(--text-muted); margin-top: 5px;">Welcome back, here is what's happening today.</p>
            </div>
            <div class="header-actions">
                <a href="home.jsp">View Customer Site ↗</a>
            </div>
        </div>

        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon blue">📦</div>
                <div class="stat-info">
                    <p>Total Orders (All Time)</p>
                    <h3><%= totalOrders %></h3>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon orange">🔥</div>
                <div class="stat-info">
                    <p>Active / Pending Orders</p>
                    <h3><%= activeOrders %></h3>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon green">💰</div>
                <div class="stat-info">
                    <p>Total Completed Revenue</p>
                    <h3>₹<%= String.format("%.2f", totalRevenue) %></h3>
                </div>
            </div>
        </div>

        <h2 class="section-title">Live Kitchen Queue</h2>
        
        <div class="ticket-grid">
            <% 
                for (Order o : allOrders) { 
                    String status = o.getStatus() != null ? o.getStatus() : "Pending";
                    // We hide delivered/cancelled orders from the kitchen view to keep it clean
                    if (status.equalsIgnoreCase("Delivered") || status.equalsIgnoreCase("Reviewed") || status.equalsIgnoreCase("Cancelled")) {
                        continue; 
                    }
                    
                    String statusClass = "";
                    if (status.equalsIgnoreCase("Pending")) statusClass = "status-pending";
                    else if (status.equalsIgnoreCase("Preparing")) statusClass = "status-preparing";
                    else if (status.equalsIgnoreCase("Out for Delivery")) statusClass = "status-out";
            %>
                <div class="ticket <%= statusClass %>">
                    <div class="ticket-header">
                        <span style="font-weight: 700;">Order #<%= o.getOrderId() %></span>
                        <span style="font-size: 13px; color: var(--text-muted);">₹<%= o.getTotalAmount() %></span>
                    </div>

                    <div class="ticket-body">
                        <% 
                            List<OrderItem> items = itemDAO.getOrderItemsByOrderId(o.getOrderId());
                            if (items != null) {
                                for (OrderItem item : items) {
                                    Menu menuItem = menuDAO.getMenu(item.getMenuId());
                                    String itemName = (menuItem != null) ? menuItem.getItemName() : "Item";
                        %>
                                    <div class="food-item">
                                        <div><span class="qty"><%= item.getQuantity() %>x</span> <%= itemName %></div>
                                    </div>
                        <%      }
                            } 
                        %>
                    </div>

                    <div class="ticket-footer">
                    <form action="UpdateStatusServlet" method="POST" class="status-form" style="flex-direction: column;">
                        <div style="display: flex; gap: 8px; width: 100%;">
                            <input type="hidden" name="orderId" value="<%= o.getOrderId() %>">
                            
                            <select name="newStatus" class="status-select" onchange="showExtraFields(this, <%= o.getOrderId() %>)">
   										<option value="PLACED" <%= status.equalsIgnoreCase("PLACED") ? "selected" : "" %>>Pending</option>
    									<option value="PREPARING" <%= status.equalsIgnoreCase("PREPARING") ? "selected" : "" %>>Preparing</option>
    									<option value="OUT_FOR_DELIVERY" <%= status.equalsIgnoreCase("OUT_FOR_DELIVERY") ? "selected" : "" %>>Out for Delivery</option>
    									<option value="DELIVERED" <%= status.equalsIgnoreCase("DELIVERED") ? "selected" : "" %>>Delivered</option>
    									<option value="CANCELLED" <%= status.equalsIgnoreCase("CANCELLED") ? "selected" : "" %>>Cancel Order</option>
							</select>
                            <button type="submit" class="btn-update">Save</button>
                        </div>
                        
                        <div id="extra-action-<%= o.getOrderId() %>" style="display: none; margin-top: 12px; width: 100%; animation: slideDown 0.3s ease;">
                        </div>
                    </form>
                </div>

            </div> <% } %> </div> </main>

    <script> ... </script>
</body>
</html>
    </main>

</body>
</html>