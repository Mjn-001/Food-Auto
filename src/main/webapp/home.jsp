<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.tap.DAO.RestaurantDAO" %>
<%@ page import="com.tap.DAOImple.RestaurantDAOImple" %>
<%@ page import="com.tap.Model.Restaurant" %>

<%
    // 1. SECURITY & SESSION CHECK
    Integer userId = (Integer) session.getAttribute("loggedInUserId");
    if (userId == null) {
        response.sendRedirect("login.html");
        return;
    }
    
    // Extract first name for a friendly greeting
    String userName = (String) session.getAttribute("loggedInUserName");
    String firstName = (userName != null && userName.contains(" ")) ? userName.substring(0, userName.indexOf(" ")) : userName;

    // 2. FETCH DATABASE DATA
    RestaurantDAO restaurantDAO = new RestaurantDAOImple();
    List<Restaurant> restaurantList = restaurantDAO.getAllRestaurants();

    // 3. INTELLIGENT IMAGE ENGINE (Maps cuisines to specific high-quality images)
    Map<String, String> imageMap = new HashMap<>();
    imageMap.put("south indian", "https://images.unsplash.com/photo-1610192244261-3f33de3f55e4?q=80&w=800"); // Dosa
    imageMap.put("biryani", "https://images.unsplash.com/photo-1631515243349-e0cb75fb8d3a?q=80&w=800"); // Biryani
    imageMap.put("pizza", "https://images.unsplash.com/photo-1513104890138-7c749659a591?q=80&w=800"); // Pizza
    imageMap.put("fast food", "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?q=80&w=800"); // Burgers/Fries
    imageMap.put("burgers", "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?q=80&w=800"); 
    imageMap.put("chinese", "https://images.unsplash.com/photo-1585032226651-759b368d7246?q=80&w=800"); // Noodles/Dumplings
    imageMap.put("ice cream", "https://images.unsplash.com/photo-1497034825429-c343d7c6a68f?q=80&w=800"); // Ice Cream
    imageMap.put("desserts", "https://images.unsplash.com/photo-1551024601-bec78aea704b?q=80&w=800"); // Waffles/Pancakes
    imageMap.put("healthy food", "https://images.unsplash.com/photo-1509722747041-616f39b57569?q=80&w=800"); // Subs/Salads
    imageMap.put("bbq", "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?q=80&w=800"); // Grill/Tandoori
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Food Auto - Discover Great Food</title>
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
            background-color: white;
            padding: 15px 5%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            position: sticky;
            top: 0;
            z-index: 1000;
        }

        .logo { font-size: 32px; font-weight: 800; color: var(--zomato-red); letter-spacing: -1.5px; text-decoration: none; }
        
        /* Integrated Search Bar */
        .nav-search { flex-grow: 1; margin: 0 40px; max-width: 600px; }
        .nav-search input {
            width: 100%; padding: 12px 20px; border: 1px solid #e0e0e0;
            border-radius: 8px; outline: none; font-size: 14px;
            background-color: #f8f8f8; transition: 0.3s;
        }
        .nav-search input:focus {
            border-color: var(--zomato-red); background-color: white;
            box-shadow: 0 0 5px rgba(203, 32, 45, 0.15);
        }

        .nav-actions { display: flex; align-items: center; gap: 25px; }
        .user-greeting { font-weight: 500; color: var(--text-muted); }
        .nav-link { text-decoration: none; color: var(--text-main); font-weight: 500; transition: 0.3s; }
        .nav-link:hover { color: var(--zomato-red); }
        .btn-outline { border: 1px solid #ddd; padding: 8px 16px; border-radius: 6px; }
        .btn-outline:hover { border-color: var(--zomato-red); }

        /* Hero Banner */
        .hero {
            height: 350px; display: flex; flex-direction: column; align-items: center; justify-content: center;
            text-align: center; color: white;
            background: linear-gradient(rgba(0,0,0,0.4), rgba(0,0,0,0.6)), 
                        url('https://images.unsplash.com/photo-1555396273-367ea4eb4db5?q=80&w=2000&auto=format&fit=crop') center/cover no-repeat;
        }
        .hero h1 { font-size: 42px; font-weight: 700; text-shadow: 2px 2px 4px rgba(0,0,0,0.3); }

        /* Responsive Restaurant Grid */
        .main-content { max-width: 1250px; margin: 50px auto; padding: 0 20px; }
        .section-title { font-size: 28px; font-weight: 600; margin-bottom: 30px; }
        
        .restaurant-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 35px; }

        /* Modern Card Design */
        .card { background: white; border-radius: 16px; overflow: hidden; box-shadow: 0 10px 30px rgba(0,0,0,0.04); transition: 0.3s; }
        .card:hover { transform: translateY(-8px); box-shadow: 0 15px 35px rgba(0,0,0,0.1); }
        
        .card-img { height: 220px; width: 100%; background-size: cover; background-position: center; border-bottom: 1px solid #f1f1f1; }
        .card-info { padding: 20px; }
        
        .rest-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 8px; }
        .rest-name { font-size: 20px; font-weight: 700; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; max-width: 75%; }
        .rating { background: #24963f; color: white; padding: 3px 8px; border-radius: 6px; font-size: 13px; font-weight: 700; }
        
        .rest-details { color: var(--text-muted); font-size: 14px; margin-bottom: 20px; }
        .action-row { display: flex; justify-content: space-between; align-items: center; border-top: 1px dashed #e0e0e0; padding-top: 15px; }
        
        .delivery-time { font-size: 13px; font-weight: 500; color: var(--text-muted); }
        .btn-order { background-color: var(--zomato-red); color: white; padding: 10px 20px; border-radius: 8px; text-decoration: none; font-size: 14px; font-weight: 600; }
        
        .empty-state { grid-column: 1 / -1; text-align: center; padding: 60px; background: white; border-radius: 12px; }
    </style>
</head>
<body>

    <nav class="navbar">
        <a href="home.jsp" class="logo">Food Auto</a>
        
       <form class="nav-search" action="SearchServlet" method="GET">
    <input type="text" name="searchQuery" placeholder="Search for restaurant, cuisine, or a dish..." required>
    
    <button type="submit" style="display:none;"></button>
</form>

        <div class="nav-actions">
            <span class="user-greeting">Welcome, <b><%= firstName != null ? firstName : "Foodie" %></b></span>
            <a href="home.jsp" class="nav-link">Home</a>
            <a href="cart.jsp" class="nav-link">My Cart</a>
            <a href="HistoryServlet" style="text-decoration:none; color:inherit;">My Orders</a>
            
            <!-- ADDED PROFILE LINK HERE -->
            <a href="profile.jsp" class="nav-link" style="display: flex; align-items: center; gap: 5px;">👤 Profile</a>
            
            <a href="LogoutServlet" class="nav-link btn-outline">Logout</a>
        </div>
    </nav>

    <header class="hero">
        <h1>Find your favorite food, <%= firstName != null ? firstName : "there" %>!</h1>
    </header>

    <main class="main-content">
        <h2 class="section-title">Delivery Restaurants in Chennai</h2>
        
        <div class="restaurant-grid">
            
            <%
                if (restaurantList != null && !restaurantList.isEmpty()) {
                    for (Restaurant r : restaurantList) {
                        
                        // Default values
                        String cuisine = r.getCuisineType() != null ? r.getCuisineType().toLowerCase() : "";
                        String currentImageUrl = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?q=80&w=800"; // General food fallback
                        
                        // Engine check: Does the cuisine match any of our premium images?
                        for (String key : imageMap.keySet()) {
                            if (cuisine.contains(key)) {
                                currentImageUrl = imageMap.get(key);
                                break;
                            }
                        }
            %>
            
            <article class="card">
                <div class="card-img" style="background-image: url('<%= currentImageUrl %>');"></div>
                
                <div class="card-info">
                    <div class="rest-header">
                        <h3 class="rest-name" title="<%= r.getName() %>"><%= r.getName() %></h3>
                        <div class="rating"><%= r.getRating() %> ★</div>
                    </div>
                    
                    <p class="rest-details"><%= r.getCuisineType() %></p>
                    
                    <div class="action-row">
                        <div class="delivery-time">⏱️ <%= r.getDeliveryTime() %> mins</div>
                        <a href="menu.jsp?restaurantId=<%= r.getRestaurantId() %>" class="btn-order">View Menu</a>
                    </div>
                </div>
            </article>
            
            <%
                    } 
                } else {
            %>
                <div class="empty-state">
                    <h3>No restaurants serving right now!</h3>
                    <p>Make sure your script successfully executed in MySQL Workbench.</p>
                </div>
            <%
                }
            %>

        </div>
    </main>

</body>
</html>