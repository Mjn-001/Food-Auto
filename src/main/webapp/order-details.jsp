<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tap.Model.Order, com.tap.Model.OrderItem, com.tap.Model.Restaurant, com.tap.Model.Menu" %>
<%@ page import="com.tap.DAO.MenuDAO, com.tap.DAOImple.MenuDAOImple" %>

<%
    // Retrieve the data passed from the Servlet
    Order order = (Order) request.getAttribute("order");
    List<OrderItem> items = (List<OrderItem>) request.getAttribute("orderItems");
    Restaurant restaurant = (Restaurant) request.getAttribute("restaurant");
    
    // We need MenuDAO to get the names of the food items
    MenuDAO menuDAO = new MenuDAOImple();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Order Receipt | Food Auto</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap');
        
        :root { --zomato-red: #cb202d; --bg-color: #f8f9fa; --text-main: #1c1c1c; --text-muted: #696969; }
        * { margin: 0; padding: 0; box-sizing: border-box; font-family: 'Poppins', sans-serif; }
        body { background-color: var(--bg-color); color: var(--text-main); padding: 40px 20px; }

        .receipt-container {
            max-width: 500px; margin: 0 auto; background: white; padding: 40px;
            border-radius: 12px; box-shadow: 0 10px 30px rgba(0,0,0,0.08);
            position: relative;
        }
        
        /* The zigzag edge effect for a physical receipt look */
        .receipt-container::after {
            content: ""; position: absolute; bottom: -10px; left: 0; width: 100%; height: 10px;
            background: radial-gradient(circle, transparent, transparent 50%, white 50%, white);
            background-size: 20px 20px; background-position: -10px 10px;
        }

        .header { text-align: center; margin-bottom: 30px; border-bottom: 2px dashed #eee; padding-bottom: 20px; }
        .header h1 { font-size: 24px; font-weight: 700; color: var(--zomato-red); }
        .header p { color: var(--text-muted); font-size: 14px; margin-top: 5px; }

        .order-meta { display: flex; justify-content: space-between; margin-bottom: 25px; font-size: 14px; }
        .order-meta div { display: flex; flex-direction: column; }
        .meta-label { color: var(--text-muted); font-size: 12px; text-transform: uppercase; letter-spacing: 1px; }
        .meta-value { font-weight: 600; margin-top: 4px; }

        .item-list { width: 100%; border-collapse: collapse; margin-bottom: 25px; }
        .item-list th { text-align: left; padding-bottom: 10px; color: var(--text-muted); font-size: 12px; text-transform: uppercase; border-bottom: 1px solid #eee; }
        .item-list td { padding: 12px 0; border-bottom: 1px solid #f9f9f9; }
        .item-qty { font-weight: 600; width: 40px; }
        .item-name { font-weight: 500; }
        .item-price { text-align: right; font-weight: 500; }

        .totals { border-top: 2px dashed #eee; padding-top: 20px; }
        .total-row { display: flex; justify-content: space-between; margin-bottom: 10px; font-size: 14px; color: var(--text-muted); }
        .grand-total { display: flex; justify-content: space-between; margin-top: 15px; font-size: 20px; font-weight: 700; color: var(--text-main); }

        .btn-back {
            display: block; width: 100%; text-align: center; background: var(--bg-color);
            color: var(--text-main); padding: 15px; border-radius: 8px; text-decoration: none;
            font-weight: 600; margin-top: 30px; transition: 0.3s;
        }
        .btn-back:hover { background: #e0e0e0; }
    </style>
</head>
<body>

    <div class="receipt-container">
        <% if (order != null && restaurant != null) { %>
            
            <div class="header">
                <h1><%= restaurant.getName() %></h1>
                <p><%= restaurant.getAddress() %></p>
            </div>

            <div class="order-meta">
                <div>
                    <span class="meta-label">Order ID</span>
                    <span class="meta-value">#<%= order.getOrderId() %></span>
                </div>
                <div style="text-align: right;">
                    <span class="meta-label">Date & Time</span>
                    <span class="meta-value"><%= order.getOrderDate() %></span>
                </div>
            </div>

            <table class="item-list">
                <thead>
                    <tr>
                        <th>Qty</th>
                        <th>Item</th>
                        <th style="text-align: right;">Price</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        if (items != null) {
                            for (OrderItem item : items) {
                                // Fetch the real name of the food
                                Menu menuItem = menuDAO.getMenu(item.getMenuId());
                                String itemName = (menuItem != null) ? menuItem.getItemName() : "Unknown Item";
                    %>
                    <tr>
                        <td class="item-qty"><%= item.getQuantity() %>x</td>
                        <td class="item-name"><%= itemName %></td>
                        <td class="item-price">₹<%= String.format("%.2f", item.getItemTotal()) %></td>
                    </tr>
                    <%      }
                        } 
                    %>
                </tbody>
            </table>

            <div class="totals">
                <div class="total-row">
                    <span>Subtotal</span>
                    <span>₹<%= String.format("%.2f", order.getTotalAmount()) %></span>
                </div>
                <div class="total-row">
                    <span>Taxes & Fees (Included)</span>
                    <span>₹0.00</span>
                </div>
                <div class="grand-total">
                    <span>Total Paid</span>
                    <span>₹<%= String.format("%.2f", order.getTotalAmount()) %></span>
                </div>
            </div>

        <% } else { %>
            <div style="text-align: center; color: var(--zomato-red);">
                <h3>Oops! We couldn't find this receipt.</h3>
            </div>
        <% } %>
        
        <a href="HistoryServlet" class="btn-back">Back to Order History</a>
    </div>

</body>
</html>