<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.tap.DAO.UserDAO, com.tap.DAOImple.UserDAOImple, com.tap.Model.User" %>

<%
    // 1. Security Check
    Integer userId = (Integer) session.getAttribute("loggedInUserId");
    if (userId == null) {
        response.sendRedirect("login.html");
        return;
    }

    // 2. Fetch fresh user data from the database
    UserDAO userDAO = new UserDAOImple();
    User currentUser = userDAO.getUser(userId);
    
    String successMsg = request.getParameter("success");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>My Profile | Food Auto</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700&display=swap');
        
        :root { 
            --primary: #ef4f5f; 
            --bg: #f4f5f7;
            --surface: #ffffff;
            --text-dark: #1c1c1c;
            --text-muted: #696969;
            --border: #edf2f7;
        }
        
        * { box-sizing: border-box; margin: 0; padding: 0; font-family: 'Outfit', sans-serif; }
        body { background: var(--bg); color: var(--text-dark); }

        /* NAVBAR */
        .navbar { background: var(--surface); padding: 20px 5%; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 10px rgba(0,0,0,0.03); }
        .logo { font-size: 28px; font-weight: 800; color: var(--primary); text-decoration: none; }
        .nav-links a { margin-left: 20px; text-decoration: none; color: var(--text-dark); font-weight: 500; }

        /* LAYOUT */
        .container { max-width: 1000px; margin: 40px auto; padding: 0 20px; display: grid; grid-template-columns: 1fr 2.5fr; gap: 30px; }
        
        .card { background: var(--surface); border-radius: 16px; padding: 30px; box-shadow: 0 10px 40px rgba(0,0,0,0.03); }

        /* LEFT PANEL: AVATAR */
        .profile-sidebar { text-align: center; }
        .avatar-container { position: relative; width: 120px; height: 120px; margin: 0 auto 20px; }
        .avatar { width: 100%; height: 100%; border-radius: 50%; object-fit: cover; border: 4px solid white; box-shadow: 0 4px 15px rgba(0,0,0,0.1); }
        .edit-avatar { position: absolute; bottom: 5px; right: 5px; background: var(--primary); color: white; width: 32px; height: 32px; border-radius: 50%; display: flex; align-items: center; justify-content: center; cursor: pointer; border: 2px solid white; }
        
        .user-name { font-size: 20px; font-weight: 700; }
        .user-email { color: var(--text-muted); font-size: 14px; margin-top: 5px; }

        /* RIGHT PANEL: FORM */
        .section-title { font-size: 20px; font-weight: 600; margin-bottom: 25px; padding-bottom: 15px; border-bottom: 1px solid var(--border); }
        
        .input-group { margin-bottom: 20px; }
        .input-group label { display: block; font-size: 14px; font-weight: 500; margin-bottom: 8px; color: var(--text-muted); }
        .premium-input { width: 100%; padding: 15px; border: 1.5px solid var(--border); border-radius: 10px; font-size: 15px; background: #fcfcfc; transition: 0.3s; }
        .premium-input:focus { border-color: var(--primary); background: var(--surface); outline: none; }
        
        .row { display: flex; gap: 20px; }
        .row .input-group { flex: 1; }

        .btn-save { background: var(--primary); color: white; border: none; padding: 16px 30px; border-radius: 10px; font-size: 16px; font-weight: 600; cursor: pointer; transition: 0.3s; margin-top: 10px; }
        .btn-save:hover { background: #e23746; transform: translateY(-2px); box-shadow: 0 8px 15px rgba(239, 79, 95, 0.2); }

        .alert { background: #e6f4ea; color: #1e8e3e; padding: 15px; border-radius: 8px; margin-bottom: 20px; font-weight: 500; text-align: center; }

        @media (max-width: 768px) { .container { grid-template-columns: 1fr; } }
    </style>
</head>
<body>

    <nav class="navbar">
        <a href="home.jsp" class="logo">Food Auto</a>
        <div class="nav-links">
            <a href="HistoryServlet">Orders</a>
            <a href="cart.jsp">Cart</a>
        </div>
    </nav>

    <div class="container">
        
        <div class="card profile-sidebar">
            <div class="avatar-container">
                <img src="https://ui-avatars.com/api/?name=<%= currentUser.getUserName() %>&background=ef4f5f&color=fff&size=150" alt="Avatar" class="avatar">
                <div class="edit-avatar">📷</div>
            </div>
            <h2 class="user-name"><%= currentUser.getUserName() %></h2>
            <p class="user-email"><%= currentUser.getEmail() %></p>
            
            <div style="margin-top: 30px; padding-top: 20px; border-top: 1px solid var(--border);">
                <p style="font-size: 14px; color: var(--text-muted);">Member since 2024</p>
            </div>
        </div>

        <div class="card">
            <% if ("true".equals(successMsg)) { %>
                <div class="alert">✅ Profile updated successfully!</div>
            <% } %>

            <h2 class="section-title">Account Settings</h2>
            
            <form action="UpdateProfileServlet" method="POST">
                <div class="row">
                    <div class="input-group">
                        <label>Full Name</label>
                        <input type="text" name="name" class="premium-input" value="<%= currentUser.getUserName() %>" required>
                    </div>
                    <div class="input-group">
                        <label>Phone Number</label>
                        <input type="tel" name="phone" class="premium-input" value="<%= currentUser.getPhone() %>" required>
                    </div>
                </div>

                <div class="input-group">
                    <label>Email Address (Cannot be changed)</label>
                    <input type="email" class="premium-input" value="<%= currentUser.getEmail() %>" disabled style="background: #eee; cursor: not-allowed;">
                </div>

                <h2 class="section-title" style="margin-top: 40px;">Security</h2>
                
                <div class="input-group">
                    <label>New Password (Leave blank to keep current password)</label>
                    <input type="password" name="password" class="premium-input" placeholder="Enter new password">
                </div>

                <button type="submit" class="btn-save">Save Changes</button>
            </form>
        </div>

    </div>

</body>
</html>