<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.tap.Model.Restaurant" %>

<%
    // 1. Get the search results and the search term
    List<Restaurant> results = (List<Restaurant>) request.getAttribute("restaurants");
    String searchQuery = request.getParameter("searchQuery");
    if (searchQuery == null) searchQuery = "";

    // 2. INTELLIGENT IMAGE ENGINE (Maps cuisines to specific high-quality images)
    Map<String, String> imageMap = new HashMap<>();
    imageMap.put("south indian", "https://images.unsplash.com/photo-1610192244261-3f33de3f55e4?q=80&w=800"); 
    imageMap.put("biryani", "https://images.unsplash.com/photo-1631515243349-e0cb75fb8d3a?q=80&w=800"); 
    imageMap.put("pizza", "https://images.unsplash.com/photo-1513104890138-7c749659a591?q=80&w=800"); 
    imageMap.put("fast food", "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?q=80&w=800"); 
    imageMap.put("burgers", "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?q=80&w=800"); 
    imageMap.put("chinese", "https://images.unsplash.com/photo-1585032226651-759b368d7246?q=80&w=800"); 
    imageMap.put("ice cream", "https://images.unsplash.com/photo-1497034825429-c343d7c6a68f?q=80&w=800"); 
    imageMap.put("desserts", "https://images.unsplash.com/photo-1551024601-bec78aea704b?q=80&w=800"); 
    imageMap.put("healthy food", "https://images.unsplash.com/photo-1509722747041-616f39b57569?q=80&w=800"); 
    imageMap.put("bbq", "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?q=80&w=800"); 
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Search Results | Food Auto</title>
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
        .nav-actions a { text-decoration: none; color: var(--text-main); font-weight: 500; margin-left: 20px; transition: 0.3s; }
        .nav-actions a:hover { color: var(--zomato-red); }

        /* Header Area */
        .search-header { max-width: 1250px; margin: 40px auto 10px; padding: 0 20px; }
        .search-header h1 { font-size: 28px; font-weight: 700; }
        .search-header span { color: var(--zomato-red); }

        /* Grid & Cards */
        .main-content { max-width: 1250px; margin: 20px auto 50px; padding: 0 20px; }
        .restaurant-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 35px; }
        
        .card { background: white; border-radius: 16px; overflow: hidden; box-shadow: 0 10px 30px rgba(0,0,0,0.04); transition: 0.3s; display: flex; flex-direction: column; }
        .card:hover { transform: translateY(-8px); box-shadow: 0 15px 35px rgba(0,0,0,0.1); }
        .card-img { height: 220px; width: 100%; background-size: cover; background-position: center; border-bottom: 1px solid #f1f1f1; }
        
        .card-info { padding: 20px; flex-grow: 1; display: flex; flex-direction: column; }
        .rest-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 8px; }
        .rest-name { font-size: 20px; font-weight: 700; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; max-width: 75%; }
        .rating { background: #24963f; color: white; padding: 3px 8px; border-radius: 6px; font-size: 13px; font-weight: 700; }
        
        .rest-details { color: var(--text-muted); font-size: 14px; margin-bottom: 20px; flex-grow: 1; }
        .action-row { display: flex; justify-content: space-between; align-items: center; border-top: 1px dashed #e0e0e0; padding-top: 15px; margin-top: auto; }
        
        .delivery-time { font-size: 13px; font-weight: 500; color: var(--text-muted); }
        .btn-order { background-color: var(--zomato-red); color: white; padding: 10px 20px; border-radius: 8px; text-decoration: none; font-size: 14px; font-weight: 600; transition: background 0.3s;}
        .btn-order:hover { background-color: #a81a25; }
        
        /* Empty State Styling */
        .empty-state { text-align: center; padding: 80px 20px; background: white; border-radius: 16px; box-shadow: 0 4px 15px rgba(0,0,0,0.03); margin-top: 30px; }
        .empty-state h2 { margin-bottom: 10px; font-size: 24px; }
        .empty-state p { color: var(--text-muted); margin-bottom: 30px; }
        .btn-home { background-color: var(--zomato-red); color: white; padding: 12px 30px; border-radius: 8px; text-decoration: none; font-weight: 600; }
    </style>
</head>
<body>

    <nav class="navbar">
        <a href="home.jsp" class="logo">Food Auto</a>
        <div class="nav-actions">
            <a href="home.jsp">Home</a>
            <a href="cart.jsp">My Cart</a>
        </div>
    </nav>

    <div class="search-header">
        <h1>Search results for: <span>"<%= searchQuery %>"</span></h1>
    </div>

    <main class="main-content">
        <% if (results != null && !results.isEmpty()) { %>
            <div class="restaurant-grid">
                <%
                    for (Restaurant r : results) {
                        // Image logic
                        String cuisine = r.getCuisineType() != null ? r.getCuisineType().toLowerCase() : "";
                        String currentImageUrl = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?q=80&w=800"; // General food fallback
                        
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
                
                <% } %>
            </div>
        <% } else { %>
            <div class="empty-state">
                <div style="font-size: 60px; margin-bottom: 20px;">🔍</div>
                <h2>We couldn't find a match for "<%= searchQuery %>"</h2>
                <p>Try searching for a different restaurant or cuisine.</p>
                <a href="home.jsp" class="btn-home">Explore All Restaurants</a>
            </div>
        <% } %>
    </main>

</body>
</html>