<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.tap.Model.Order" %>

<%
    Order order = (Order) request.getAttribute("trackOrder");
    if (order == null) { response.sendRedirect("HistoryServlet"); return; }

    String currentStatus = order.getStatus() != null ? order.getStatus() : "Pending";
    
    // Determine the active progress step
    int step = 1; // Default: Order Placed
    boolean isCancelled = currentStatus.equalsIgnoreCase("Cancelled");
    
    if (!isCancelled) {
        if (currentStatus.equalsIgnoreCase("Preparing")) step = 2;
        else if (currentStatus.equalsIgnoreCase("Out for Delivery")) step = 3;
        else if (currentStatus.equalsIgnoreCase("Delivered") || currentStatus.equalsIgnoreCase("Reviewed")) step = 4;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Track Order | Food Auto</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap');
        
        :root { --zomato-red: #cb202d; --success: #24963f; --bg: #f4f5f7; --text-dark: #1c1c1c; --text-muted: #888; }
        * { margin: 0; padding: 0; box-sizing: border-box; font-family: 'Poppins', sans-serif; }
        body { background: var(--bg); color: var(--text-dark); padding: 40px 20px; }

        .tracking-container {
            max-width: 600px; margin: 0 auto; background: white; padding: 40px;
            border-radius: 16px; box-shadow: 0 10px 30px rgba(0,0,0,0.05);
        }

        .header { border-bottom: 1px solid #eee; padding-bottom: 20px; margin-bottom: 30px; text-align: center; }
        .header h1 { font-size: 24px; font-weight: 700; }
        .header p { color: var(--text-muted); font-size: 14px; margin-top: 5px; }

        /* TIMELINE CSS */
        .timeline { padding-left: 20px; margin-top: 30px; }
        .timeline-step { position: relative; padding-bottom: 40px; padding-left: 35px; color: var(--text-muted); transition: 0.3s; }
        
        /* The Icon Circle */
        .timeline-step .icon {
            position: absolute; left: -16px; top: 0; width: 32px; height: 32px;
            background: #eee; border-radius: 50%; display: flex; align-items: center; 
            justify-content: center; font-size: 14px; z-index: 2; transition: 0.4s;
        }
        
        /* The Connecting Line */
        .timeline-step::after {
            content: ''; position: absolute; left: -1px; top: 32px; width: 3px; 
            height: calc(100% - 32px); background: #eee; z-index: 1; transition: 0.4s;
        }
        .timeline-step:last-child { padding-bottom: 0; }
        .timeline-step:last-child::after { display: none; }

        .step-title { font-size: 16px; font-weight: 600; margin-bottom: 4px; }
        .step-desc { font-size: 13px; }

        /* ACTIVE STATES */
        .timeline-step.active { color: var(--text-dark); }
        .timeline-step.active .icon { background: var(--success); color: white; box-shadow: 0 0 0 4px rgba(36, 150, 63, 0.2); }
        .timeline-step.active::after { background: var(--success); }

        /* CANCELLED STATE */
        .timeline-step.cancelled .icon { background: var(--zomato-red); color: white; }
        .timeline-step.cancelled { color: var(--zomato-red); }

        .btn-back {
            display: block; width: 100%; text-align: center; background: var(--zomato-red);
            color: white; padding: 15px; border-radius: 8px; text-decoration: none;
            font-weight: 600; margin-top: 40px; transition: 0.3s;
        }
        .btn-back:hover { background: #a81a25; }
    </style>
</head>
<body>

    <div class="tracking-container">
        <div class="header">
            <h1>Track Your Order</h1>
            <p>Order #<%= order.getOrderId() %> &nbsp;•&nbsp; Amount: ₹<%= String.format("%.2f", order.getTotalAmount()) %></p>
        </div>

        <% if (isCancelled) { %>
            <div class="timeline">
                <div class="timeline-step cancelled">
                    <div class="icon">✖</div>
                    <div class="step-title">Order Cancelled</div>
                    <div class="step-desc">This order was cancelled and will not be delivered.</div>
                </div>
            </div>
        <% } else { %>
            <div class="timeline">
                
                <div class="timeline-step <%= step >= 1 ? "active" : "" %>">
                    <div class="icon">📝</div>
                    <div class="step-title">Order Placed</div>
                    <div class="step-desc">We have received your order.</div>
                </div>

                <div class="timeline-step <%= step >= 2 ? "active" : "" %>">
                    <div class="icon">🍳</div>
                    <div class="step-title">Kitchen Preparing</div>
                    <div class="step-desc">Your food is being prepared with care.</div>
                </div>

                <div class="timeline-step <%= step >= 3 ? "active" : "" %>">
                    <div class="icon">🛵</div>
                    <div class="step-title">Out for Delivery</div>
                    <div class="step-desc">Your delivery partner is on the way.</div>
                </div>

                <div class="timeline-step <%= step >= 4 ? "active" : "" %>">
                    <div class="icon">✅</div>
                    <div class="step-title">Delivered</div>
                    <div class="step-desc">Enjoy your meal!</div>
                </div>

            </div>
        <% } %>

        <a href="HistoryServlet" class="btn-back">Return to History</a>
    </div>

</body>
</html>