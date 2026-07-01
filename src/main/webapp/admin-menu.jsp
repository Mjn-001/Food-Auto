<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tap.Model.Menu" %>
<%@ page import="com.tap.DAO.MenuDAO, com.tap.DAOImple.MenuDAOImple" %>

<%
    // Security Check: Only allow Admins
    String role = (String) session.getAttribute("userRole");
    if (role == null || !role.equalsIgnoreCase("Admin")) { response.sendRedirect("home.jsp"); return; }
    
    MenuDAO menuDAO = new MenuDAOImple();
    List<Menu> menuList = menuDAO.getAllMenus();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Menu | Admin</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700&display=swap');
        :root { --primary: #cb202d; --bg: #f4f6f8; --surface: #ffffff; --text: #1e293b; --border: #e2e8f0; }
        * { box-sizing: border-box; margin: 0; padding: 0; font-family: 'Outfit', sans-serif; }
        body { background: var(--bg); display: flex; }
        
        /* Sidebar Layout */
        .sidebar { width: 260px; background: var(--surface); height: 100vh; position: fixed; border-right: 1px solid var(--border); padding: 20px; }
        .main-content { margin-left: 260px; padding: 40px; width: calc(100% - 260px); }
        
        .card { background: var(--surface); padding: 25px; border-radius: 12px; box-shadow: 0 4px 6px rgba(0,0,0,0.02); margin-bottom: 30px; border: 1px solid var(--border); }
        
        /* Form Styling */
        .form-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 15px; }
        .premium-input { padding: 12px; border: 1px solid var(--border); border-radius: 8px; font-size: 14px; }
        .btn-save { background: var(--primary); color: white; border: none; padding: 12px 20px; border-radius: 8px; font-weight: 600; cursor: pointer; }
        
        /* Table Styling */
        table { width: 100%; border-collapse: collapse; background: white; border-radius: 12px; overflow: hidden; }
        th { background: #f8fafc; text-align: left; padding: 16px; font-weight: 600; border-bottom: 1px solid var(--border); }
        td { padding: 16px; border-bottom: 1px solid #f1f5f9; }
        .btn-delete { color: #ef4444; background: #fee2e2; padding: 6px 12px; border-radius: 6px; text-decoration: none; font-size: 13px; font-weight: 600; border: none; cursor: pointer; }
    </style>
</head>
<body>

    <aside class="sidebar">
        <h2 style="color: var(--primary); margin-bottom: 30px;">Food Auto</h2>
        <a href="AdminDashboardServlet" style="display:block; margin-bottom: 15px; text-decoration:none; color:var(--text);">📊 Dashboard</a>
        <a href="admin-menu.jsp" style="display:block; margin-bottom: 15px; text-decoration:none; color:var(--primary); font-weight:600;">🍔 Manage Menu</a>
        <a href="LogoutServlet" style="display:block; margin-top: 50px; color:#ef4444; text-decoration:none;">Log Out</a>
    </aside>

    <main class="main-content">
        <h1>Manage Menu</h1>
        
        <div class="card">
            <h3 style="margin-bottom: 15px;">Add New Menu Item</h3>
            <form action="MenuManagementServlet?action=add" method="POST">
                <div class="form-grid">
                    <input type="number" name="restaurantId" placeholder="Restaurant ID" required class="premium-input">
                    <input type="text" name="itemName" placeholder="Item Name" required class="premium-input">
                    <input type="text" name="description" placeholder="Description" required class="premium-input">
                    <input type="number" step="0.01" name="price" placeholder="Price" required class="premium-input">
                    <input type="text" name="category" placeholder="Category" required class="premium-input">
                    <select name="isAvailable" class="premium-input">
                        <option value="true">Available</option>
                        <option value="false">Unavailable</option>
                    </select>
                </div>
                <button type="submit" class="btn-save" style="margin-top: 15px;">Add Item to Menu</button>
            </form>
        </div>

        <div class="card">
            <table>
                <tr>
                    <th>Item Name</th>
                    <th>Category</th>
                    <th>Price</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
                <% if(menuList != null) { for(Menu m : menuList) { %>
                <tr>
                    <td><%= m.getItemName() %></td>
                    <td><%= m.getCategory() %></td>
                    <td>₹<%= String.format("%.2f", m.getPrice()) %></td>
                    <td><%= m.isAvailable() ? "Available" : "Unavailable" %></td>
                    <td>
                        <form action="MenuManagementServlet?action=delete" method="POST">
                            <input type="hidden" name="menuId" value="<%= m.getMenuId() %>">
                            <button type="submit" class="btn-delete">Delete</button>
                        </form>
                    </td>
                </tr>
                <% }} %>
            </table>
        </div>
    </main>

</body>
</html>