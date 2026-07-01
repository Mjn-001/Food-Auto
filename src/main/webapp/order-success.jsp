<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Ensure the user actually just placed an order
    Object idObj = session.getAttribute("latestOrderId");
String orderId = (idObj != null) ? idObj.toString() : null;    if (orderId == null) {
        response.sendRedirect("home.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Confirmed</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700&display=swap');
        * { box-sizing: border-box; margin: 0; padding: 0; font-family: 'Outfit', sans-serif; }
        body { background: #f4f5f7; display: flex; justify-content: center; align-items: center; min-height: 100vh; text-align: center; color: #1c1c1c; }
        
        .success-card { background: white; padding: 50px 40px; border-radius: 20px; box-shadow: 0 10px 40px rgba(0,0,0,0.05); max-width: 450px; width: 90%; }
        
        .check-circle { width: 80px; height: 80px; background: #10b981; border-radius: 50%; display: flex; justify-content: center; align-items: center; margin: 0 auto 25px; box-shadow: 0 10px 25px rgba(16, 185, 129, 0.3); animation: popIn 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275); }
        .check-circle svg { width: 40px; height: 40px; color: white; }
        
        h1 { font-size: 28px; font-weight: 700; margin-bottom: 10px; }
        p { color: #696969; font-size: 16px; margin-bottom: 30px; line-height: 1.5; }
        .order-id { background: #f8f9fa; padding: 12px 20px; border-radius: 10px; font-weight: 600; font-size: 18px; letter-spacing: 1px; margin-bottom: 30px; display: inline-block; border: 1.5px dashed #cbd5e1; }
        
        .btn-home { background: #ef4f5f; color: white; text-decoration: none; padding: 16px 30px; border-radius: 12px; font-weight: 600; font-size: 16px; display: block; transition: 0.3s; }
        .btn-home:hover { background: #e23746; transform: translateY(-2px); }
        
        @keyframes popIn { 0% { transform: scale(0); } 100% { transform: scale(1); } }
    </style>
</head>
<body>

    <div class="success-card">
        <div class="check-circle">
            <svg fill="none" stroke="currentColor" stroke-width="3" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path stroke-linecap="round" stroke-linejoin="round" d="M4.5 12.75l6 6 9-13.5"></path></svg>
        </div>
        
        <h1>Order Confirmed!</h1>
        <p>Your food is being prepared. Our delivery executive will reach your location shortly.</p>
        
        <div class="order-id">
            ID: <%= orderId %>
        </div>
        
        <a href="home.jsp" class="btn-home">Back to Home</a>
    </div>

</body>
</html>