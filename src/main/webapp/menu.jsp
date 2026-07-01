<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tap.DAO.RestaurantDAO" %>
<%@ page import="com.tap.DAOImple.RestaurantDAOImple" %>
<%@ page import="com.tap.DAO.MenuDAO" %>
<%@ page import="com.tap.DAOImple.MenuDAOImple" %>
<%@ page import="com.tap.Model.Restaurant" %>
<%@ page import="com.tap.Model.Menu" %>
<%@ page import="com.tap.DAO.UserDAO" %>
<%@ page import="com.tap.DAOImple.UserDAOImple" %>
<%@ page import="com.tap.Model.User" %>

<%
    // 1. SECURITY CHECK
    Integer userId = (Integer) session.getAttribute("loggedInUserId");
    if (userId == null) {
        response.sendRedirect("login.html");
        return;
    }
    
    String userName = (String) session.getAttribute("loggedInUserName");
    String firstName = (userName != null && userName.contains(" ")) ? userName.substring(0, userName.indexOf(" ")) : userName;

    // 2. GET RESTAURANT ID FROM URL
    String restIdParam = request.getParameter("restaurantId");
    if (restIdParam == null || restIdParam.isEmpty()) {
        response.sendRedirect("home.jsp"); 
        return;
    }
    
    int restaurantId = Integer.parseInt(restIdParam);

    // 3. FETCH DATABASE DATA
    RestaurantDAO restaurantDAO = new RestaurantDAOImple();
    Restaurant currentRestaurant = restaurantDAO.getRestaurant(restaurantId);
    
    MenuDAO menuDAO = new MenuDAOImple();
    List<Menu> menuList = menuDAO.getMenusByRestaurant(restaurantId);
    
    
    
    
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= currentRestaurant != null ? currentRestaurant.getName() : "Restaurant Menu" %> - Zomato</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800&display=swap');

        :root { --zomato-red: #cb202d; --text-main: #1c1c1c; --text-muted: #696969; --bg-color: #f8f9fa; }
        * { margin: 0; padding: 0; box-sizing: border-box; font-family: 'Poppins', sans-serif; }
        body { background-color: var(--bg-color); color: var(--text-main); }

        .navbar { background-color: white; padding: 15px 5%; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 10px rgba(0,0,0,0.05); position: sticky; top: 0; z-index: 1000; }
        .logo { font-size: 32px; font-weight: 800; color: var(--zomato-red); letter-spacing: -1.5px; text-decoration: none; }
        .nav-actions { display: flex; align-items: center; gap: 25px; }
        .user-greeting { font-weight: 500; color: var(--text-muted); }
        .nav-link { text-decoration: none; color: var(--text-main); font-weight: 500; transition: 0.3s; }
        .nav-link:hover { color: var(--zomato-red); }
        .btn-outline { border: 1px solid #ddd; padding: 8px 16px; border-radius: 6px; }
        .btn-outline:hover { border-color: var(--zomato-red); }
        
        .rest-header { background: white; padding: 40px 5%; border-bottom: 1px solid #eee; margin-bottom: 30px; text-align: center; }
        .rest-name { font-size: 36px; font-weight: 700; color: var(--text-main); margin-bottom: 5px; }
        .rest-meta { color: var(--text-muted); font-size: 16px; }
        .rest-rating { display: inline-block; background: #24963f; color: white; padding: 4px 10px; border-radius: 6px; font-size: 14px; font-weight: 700; margin-top: 10px; }

        .menu-container { max-width: 900px; margin: 0 auto; padding: 0 20px 50px 20px; }
        .menu-title { font-size: 24px; font-weight: 600; margin-bottom: 25px; border-bottom: 2px solid var(--zomato-red); padding-bottom: 10px; display: inline-block; }
        
        .menu-item { background: white; border-radius: 12px; padding: 20px; margin-bottom: 20px; display: flex; align-items: center; box-shadow: 0 4px 15px rgba(0,0,0,0.03); transition: transform 0.2s; }
        .menu-item:hover { transform: translateY(-3px); box-shadow: 0 8px 25px rgba(0,0,0,0.08); }
        
        .food-img { width: 130px; height: 130px; border-radius: 10px; object-fit: cover; margin-right: 20px; flex-shrink: 0; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
        
        .food-info { flex-grow: 1; padding-right: 20px; }
        .food-name { font-size: 20px; font-weight: 600; margin-bottom: 5px; color: var(--text-main); }
        .food-price { font-size: 18px; font-weight: 500; margin-bottom: 8px; color: var(--text-main); }
        .food-desc { color: #777; font-size: 14px; line-height: 1.4; }
        
        .cart-controls { display: flex; flex-direction: column; align-items: flex-end; gap: 12px; min-width: 120px; }
        .qty-input { width: 60px; padding: 8px; border: 1px solid #ddd; border-radius: 6px; text-align: center; outline: none; }
        .add-btn { background-color: white; color: var(--zomato-red); border: 1px solid var(--zomato-red); padding: 10px 24px; border-radius: 8px; font-weight: 600; cursor: pointer; width: 100%; transition: 0.3s; }
        .add-btn:hover { background-color: var(--zomato-red); color: white; }
    </style>
</head>
<body>

    <nav class="navbar">
        <a href="home.jsp" class="logo">Food Auto</a>
        <div class="nav-actions">
            <span class="user-greeting">Welcome, <b><%= firstName != null ? firstName : "Foodie" %></b></span>
            <a href="home.jsp" class="nav-link">Home</a>
            <a href="cart.jsp" class="nav-link">🛒 My Cart</a>
            <a href="LogoutServlet" class="nav-link btn-outline">Logout</a>
        </div>
    </nav>

    <header class="rest-header">
        <h1 class="rest-name"><%= currentRestaurant != null ? currentRestaurant.getName() : "Restaurant Not Found" %></h1>
        <p class="rest-meta">
            <%= currentRestaurant != null ? currentRestaurant.getCuisineType() : "" %> • 
            <%= currentRestaurant != null ? currentRestaurant.getAddress() : "" %>
        </p>
        <% if (currentRestaurant != null) { %>
            <div class="rest-rating"><%= currentRestaurant.getRating() %> ★</div>
        <% } %>
    </header>

    <main class="menu-container">
        <h2 class="menu-title">Order Online</h2>
        
        <%
            if (menuList != null && !menuList.isEmpty()) {
                for (Menu item : menuList) {
                    
                    // INTELLIGENT IMAGE MAPPER SPECIFIC TO YOUR DATABASE
                    String itemName = item.getItemName().toLowerCase();
                    String imgUrl = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?q=80&w=400"; // Fallback Image
                    
                    // Biryani & Rice
                    if (itemName.contains("biryani")) imgUrl = "https://images.unsplash.com/photo-1631515243349-e0cb75fb8d3a?q=80&w=400";
                    else if (itemName.contains("meals")) imgUrl = "https://images.unsplash.com/photo-1544025162-8315ea07fc0a?q=80&w=400";
                    
                    // South Indian Breakfast
                    else if (itemName.contains("dosa")) imgUrl = "https://images.unsplash.com/photo-1610192244261-3f33de3f55e4?q=80&w=400";
                    else if (itemName.contains("tiffin")) imgUrl = "https://images.unsplash.com/photo-1589301773822-29fc2a2f07d2?q=80&w=400";
                    else if (itemName.contains("vada")) imgUrl = "https://images.unsplash.com/photo-1606491956689-2ea866880c84?q=80&w=400";
                    
                    // Pizza & Breads
                    else if (itemName.contains("pizza")) imgUrl = "https://images.unsplash.com/photo-1513104890138-7c749659a591?q=80&w=400";
                    else if (itemName.contains("garlic bread")) imgUrl = "https://images.unsplash.com/photo-1573140247632-f8fd74997d5c?q=80&w=400";
                    
                    // Burgers, Subs & Fast Food
                    else if (itemName.contains("burger") || itemName.contains("whopper") || itemName.contains("tikki")) imgUrl = "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?q=80&w=400";
                    else if (itemName.contains("sub")) imgUrl = "https://images.unsplash.com/photo-1509722747041-616f39b57569?q=80&w=400";
                    else if (itemName.contains("fries") || itemName.contains("onion rings")) imgUrl = "https://images.unsplash.com/photo-1576107232684-1279f390859f?q=80&w=400";
                    
                    // Chicken & BBQ
                    else if (itemName.contains("chicken 65") || itemName.contains("crispy chicken")) imgUrl = "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?q=80&w=400";
                    else if (itemName.contains("roast") || itemName.contains("chilli chicken")) imgUrl = "https://images.unsplash.com/photo-1604908176997-125f25cc6f3d?q=80&w=400";
                    else if (itemName.contains("tikka") || itemName.contains("skewers")) imgUrl = "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?q=80&w=400";
                    
                    // Chinese
                    else if (itemName.contains("momo") || itemName.contains("dim sum")) imgUrl = "https://images.unsplash.com/photo-1496116218417-1a781b1c416c?q=80&w=400";
                    else if (itemName.contains("noodles")) imgUrl = "https://images.unsplash.com/photo-1585032226651-759b368d7246?q=80&w=400";
                    
                    // Desserts & Beverages
                    else if (itemName.contains("waffle")) imgUrl = "https://images.unsplash.com/photo-1551024601-bec78aea704b?q=80&w=400";
                    else if (itemName.contains("ice cream") || itemName.contains("scoop")) imgUrl = "https://images.unsplash.com/photo-1497034825429-c343d7c6a68f?q=80&w=400";
                    else if (itemName.contains("halwa") || itemName.contains("jamun") || itemName.contains("cookie")) imgUrl = "https://images.unsplash.com/photo-1558961363-fa8fdf82db35?q=80&w=400";
                    else if (itemName.contains("coffee")) imgUrl = "https://images.unsplash.com/photo-1551030173-122aabc4489c?q=80&w=400";
        %>
        
        <article class="menu-item">
            <img src="<%= imgUrl %>" alt="<%= item.getItemName() %>" class="food-img">
            
            <div class="food-info">
                <h3 class="food-name"><%= item.getItemName() %></h3>
                <div class="food-price">₹<%= item.getPrice() %></div>
                <p class="food-desc"><%= item.getDescription() != null ? item.getDescription() : "Freshly prepared and delicious." %></p>
            </div>
            
            <form action="AddToCartServlet" method="POST" class="cart-controls">
                <input type="hidden" name="menuId" value="<%= item.getMenuId() %>">
                <div style="display:flex; align-items:center; gap:8px;">
                    <label style="font-size:12px; color:#666;">Qty:</label>
                    <input type="number" name="quantity" class="qty-input" value="1" min="1" max="15" required>
                </div>
                <button type="submit" class="add-btn">ADD +</button>
            </form>
        </article>

        <%
                }
            } else {
        %>
            <div class="empty-state" style="text-align: center; padding: 60px 20px; background: white; border-radius: 12px;">
                <h3>No menu items found!</h3>
            </div>
        <%
            }
        %>
        
        
        
    </main>

</body>
</html>
