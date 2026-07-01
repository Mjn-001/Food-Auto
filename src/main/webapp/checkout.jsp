<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.tap.Model.CartItem" %>
<%@ page import="com.tap.Model.Menu" %>
<%@ page import="com.tap.DAO.MenuDAO" %>
<%@ page import="com.tap.DAOImple.MenuDAOImple" %>

<%
    // SECURITY & DATA INTEGRITY
    Integer userId = (Integer) session.getAttribute("loggedInUserId");
    if (userId == null) { response.sendRedirect("login.html"); return; }

    List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
    if (cartItems == null || cartItems.isEmpty()) { response.sendRedirect("cart.jsp"); return; }

    MenuDAO menuDAO = new MenuDAOImple();
    double subTotal = 0.0;
    for (CartItem item : cartItems) {
        Menu m = menuDAO.getMenu(item.getMenuId());
        if(m != null) subTotal += (m.getPrice() * item.getQuantity());
    }
    
    double tax = subTotal * 0.05; 
    double deliveryFee = (subTotal > 500) ? 0.0 : 40.0;
    double grandTotal = subTotal + tax + deliveryFee;
    session.setAttribute("checkoutTotal", grandTotal);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Secure Checkout | Food Auto</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700&display=swap');
        
        :root { 
            --primary: #ef4f5f; 
            --primary-hover: #e23746;
            --bg: #f4f5f7;
            --surface: #ffffff;
            --text-dark: #1c1c1c;
            --text-muted: #696969;
            --border: #edf2f7;
            --radius: 16px;
        }
        
        * { box-sizing: border-box; margin: 0; padding: 0; font-family: 'Outfit', sans-serif; }
        body { background: var(--bg); color: var(--text-dark); -webkit-font-smoothing: antialiased; }

        /* NAVBAR */
        .navbar { background: var(--surface); padding: 20px 5%; display: flex; justify-content: center; box-shadow: 0 2px 10px rgba(0,0,0,0.03); }
        .logo { font-size: 32px; font-weight: 800; color: var(--primary); text-decoration: none; letter-spacing: -1px; }

        /* LAYOUT */
        .container { max-width: 1100px; margin: 40px auto; padding: 0 20px; display: grid; grid-template-columns: 1.2fr 1fr; gap: 40px; }
        
        /* PREMIUM CARDS */
        .premium-card { background: var(--surface); border-radius: var(--radius); padding: 35px; box-shadow: 0 10px 40px rgba(0,0,0,0.04); }
        .section-title { font-size: 20px; font-weight: 600; margin-bottom: 25px; display: flex; align-items: center; gap: 10px; }
        .section-title span { background: var(--primary); color: white; width: 28px; height: 28px; display: flex; align-items: center; justify-content: center; border-radius: 50%; font-size: 14px; }

        /* MODERN INPUTS */
        .input-group { margin-bottom: 15px; }
        .premium-input { width: 100%; padding: 16px 20px; border: 1.5px solid var(--border); border-radius: 12px; font-size: 15px; background: #fcfcfc; transition: 0.3s ease; outline: none; }
        .premium-input:focus { border-color: var(--primary); background: var(--surface); box-shadow: 0 0 0 4px rgba(239, 79, 95, 0.1); }
        .premium-input::placeholder { color: #a0aec0; }
        
        .row { display: flex; gap: 15px; }
        .row .input-group { flex: 1; margin-bottom: 0; }

        /* DYNAMIC PAYMENT SELECTORS */
        .payment-grid { display: grid; grid-template-columns: 1fr; gap: 15px; }
        .pay-option-wrapper { border: 2px solid var(--border); border-radius: 12px; background: #fafafa; overflow: hidden; transition: 0.3s; }
        .pay-option-wrapper.active { border-color: var(--primary); background: white; box-shadow: 0 4px 15px rgba(239, 79, 95, 0.08); }
        
        .pay-header { padding: 20px; cursor: pointer; display: flex; align-items: center; justify-content: space-between; }
        .pay-info { display: flex; align-items: center; gap: 15px; }
        .pay-icon { width: 40px; height: 40px; background: white; border-radius: 8px; display: flex; align-items: center; justify-content: center; box-shadow: 0 2px 5px rgba(0,0,0,0.05); font-size: 20px; }
        .pay-text h4 { font-size: 16px; font-weight: 600; color: var(--text-dark); }
        .pay-text p { font-size: 13px; color: var(--text-muted); margin-top: 2px; }
        
        .custom-radio { width: 22px; height: 22px; border: 2px solid #cbd5e1; border-radius: 50%; display: flex; align-items: center; justify-content: center; }
        .pay-option-wrapper.active .custom-radio { border-color: var(--primary); }
        .pay-option-wrapper.active .custom-radio::after { content: ''; width: 10px; height: 10px; background: var(--primary); border-radius: 50%; }
        input[type="radio"] { display: none; }

        /* EXPANDABLE DETAILS */
        .pay-details { display: none; padding: 0 20px 20px 20px; border-top: 1px dashed #e2e8f0; margin-top: 5px; padding-top: 20px; animation: slideDown 0.3s ease-out forwards; }
        .pay-option-wrapper.active .pay-details { display: block; }

        @keyframes slideDown {
            from { opacity: 0; transform: translateY(-10px); }
            to { opacity: 1; transform: translateY(0); }
        }

        /* UPI SPECIFIC BUTTONS */
        .upi-apps { display: flex; gap: 10px; margin-bottom: 15px; flex-wrap: wrap; }
        .upi-btn { flex: 1; min-width: 80px; padding: 12px 10px; background: white; border: 1.5px solid var(--border); border-radius: 8px; font-size: 13px; font-weight: 600; color: var(--text-dark); cursor: pointer; transition: 0.2s; display: flex; flex-direction: column; align-items: center; gap: 5px; }
        .upi-btn:hover, .upi-btn.selected { border-color: var(--primary); background: #fff5f6; }
        .upi-btn img { height: 20px; }

        /* THE "CORRECT" BUTTON */
        .btn-submit { background: var(--primary); color: white; border: none; width: 100%; padding: 20px; border-radius: 14px; font-size: 18px; font-weight: 600; cursor: pointer; margin-top: 30px; display: flex; justify-content: space-between; align-items: center; transition: 0.3s; box-shadow: 0 10px 20px rgba(239, 79, 95, 0.25); }
        .btn-submit:hover { background: var(--primary-hover); transform: translateY(-2px); box-shadow: 0 12px 25px rgba(239, 79, 95, 0.35); }

        /* RIGHT SIDE: SUMMARY */
        .summary-wrapper { position: sticky; top: 30px; }
        .receipt-item { display: flex; justify-content: space-between; margin-bottom: 15px; font-size: 15px; }
        .receipt-item .qty { color: var(--primary); font-weight: 600; background: #fff5f6; padding: 2px 8px; border-radius: 6px; margin-right: 8px; }
        
        .divider { height: 1px; background: var(--border); margin: 20px 0; }
        .totals-row { display: flex; justify-content: space-between; margin-bottom: 12px; font-size: 15px; color: var(--text-muted); }
        .grand-total { font-size: 22px; font-weight: 700; color: var(--text-dark); margin-top: 20px; display: flex; justify-content: space-between; align-items: center; }
        .trust-badge { display: flex; align-items: center; justify-content: center; gap: 8px; margin-top: 25px; color: var(--text-muted); font-size: 13px; font-weight: 500; }

        @media (max-width: 900px) { .container { grid-template-columns: 1fr; } .summary-wrapper { position: static; } }
    </style>
</head>
<body>

    <nav class="navbar"><a href="home.jsp" class="logo">Food Auto</a></nav>

    <div class="container">
        
        <!-- We use a dummy action if we are preventing real submission of fake cards -->
        <form action="OrderServlet" method="POST" id="premiumCheckout">
            
            <div class="premium-card" style="margin-bottom: 25px;">
                <h2 class="section-title"><span>1</span> Delivery Details</h2>
                
                <div class="input-group">
                    <input type="text" name="name" class="premium-input" placeholder="Full Name" required>
                </div>
                <div class="input-group">
                    <input type="tel" name="phone" class="premium-input" placeholder="Phone Number" required pattern="[0-9]{10}">
                </div>
                <div class="input-group">
                    <textarea name="address" class="premium-input" placeholder="Complete Delivery Address" rows="3" required></textarea>
                </div>
                <div class="row">
                    <div class="input-group"><input type="text" name="city" class="premium-input" placeholder="City" required></div>
                    <div class="input-group"><input type="text" name="pin" class="premium-input" placeholder="Pincode" required></div>
                </div>
            </div>

            <div class="premium-card">
                <h2 class="section-title"><span>2</span> Payment Method</h2>
                
                <div class="payment-grid">
                    
                    <!-- UPI SECTION -->
                    <div class="pay-option-wrapper active" id="wrapper-UPI">
                        <div class="pay-header" onclick="selectPayment('UPI')">
                            <input type="radio" name="paymentMode" value="UPI" id="radio-UPI" checked>
                            <div class="pay-info">
                                <div class="pay-icon">📱</div>
                                <div class="pay-text">
                                    <h4>UPI / Google Pay</h4>
                                    <p>Instant payment via any UPI app</p>
                                </div>
                            </div>
                            <div class="custom-radio"></div>
                        </div>
                        
                        <div class="pay-details">
                            <p style="font-size: 13px; color: var(--text-muted); margin-bottom: 12px;">Select an app to pay securely</p>
                            <div class="upi-apps">
                                <div class="upi-btn">
                                    <img src="https://upload.wikimedia.org/wikipedia/commons/f/f2/Google_Pay_Logo.svg" alt="GPay">
                                    GPay
                                </div>
                                <div class="upi-btn">
                                    <img src="https://upload.wikimedia.org/wikipedia/commons/7/71/PhonePe_Logo.svg" alt="PhonePe">
                                    PhonePe
                                </div>
                                <div class="upi-btn">
                                    <img src="https://upload.wikimedia.org/wikipedia/commons/2/24/Paytm_Logo_%28standalone%29.svg" alt="Paytm">
                                    Paytm
                                </div>
                            </div>
                            <div class="input-group" style="margin-bottom: 0;">
                                <input type="text" class="premium-input" placeholder="Or enter your UPI ID (e.g., name@okhdfcbank)">
                            </div>
                        </div>
                    </div>

                    <!-- CARD SECTION -->
                    <div class="pay-option-wrapper" id="wrapper-CARD">
                        <div class="pay-header" onclick="selectPayment('CARD')">
                            <input type="radio" name="paymentMode" value="ONLINE" id="radio-CARD">
                            <div class="pay-info">
                                <div class="pay-icon">💳</div>
                                <div class="pay-text">
                                    <h4>Credit / Debit Card</h4>
                                    <p>Visa, MasterCard, RuPay</p>
                                </div>
                            </div>
                            <div class="custom-radio"></div>
                        </div>
                        
                        <div class="pay-details">
                            <div class="input-group">
                                <input type="text" class="premium-input" placeholder="Card Number (0000 0000 0000 0000)" maxlength="19">
                            </div>
                            <div class="row" style="margin-bottom: 15px;">
                                <div class="input-group">
                                    <input type="text" class="premium-input" placeholder="MM/YY" maxlength="5">
                                </div>
                                <div class="input-group">
                                    <input type="password" class="premium-input" placeholder="CVV" maxlength="3">
                                </div>
                            </div>
                            <div class="input-group" style="margin-bottom: 0;">
                                <input type="text" class="premium-input" placeholder="Name on Card">
                            </div>
                            <p style="font-size: 12px; color: #10b981; margin-top: 10px; text-align: center;">🛡️ Your card details are securely encrypted.</p>
                        </div>
                    </div>

                    <!-- COD SECTION -->
                    <div class="pay-option-wrapper" id="wrapper-COD">
                        <div class="pay-header" onclick="selectPayment('COD')">
                            <input type="radio" name="paymentMode" value="COD" id="radio-COD">
                            <div class="pay-info">
                                <div class="pay-icon">💵</div>
                                <div class="pay-text">
                                    <h4>Cash on Delivery</h4>
                                    <p>Pay at your doorstep</p>
                                </div>
                            </div>
                            <div class="custom-radio"></div>
                        </div>
                        
                        <div class="pay-details">
                            <div style="background: #f8f9fa; padding: 15px; border-radius: 8px; text-align: center;">
                                <span style="font-size: 24px; display: block; margin-bottom: 10px;">🛵</span>
                                <p style="font-size: 14px; font-weight: 500; color: var(--text-dark);">Please keep exact change ready.</p>
                                <p style="font-size: 13px; color: var(--text-muted); margin-top: 5px;">Our delivery executive may not carry large amounts of change.</p>
                            </div>
                        </div>
                    </div>

                </div>

                <button type="submit" class="btn-submit">
                    <span>Place Order</span>
                    <span>₹<%= String.format("%.2f", grandTotal) %> &rarr;</span>
                </button>
                
                <div class="trust-badge">
                    🔒 100% Safe & Secure Payments
                </div>
            </div>
        </form>

        <div class="summary-wrapper">
            <div class="premium-card">
                <h2 style="font-size: 18px; font-weight: 600; margin-bottom: 20px;">Order Summary</h2>
                
                <div class="receipt-items">
                    <% for (CartItem item : cartItems) {
                        Menu m = menuDAO.getMenu(item.getMenuId());
                        if(m != null) { %>
                        <div class="receipt-item">
                            <div>
                                <span class="qty"><%= item.getQuantity() %>x</span>
                                <span style="font-weight: 500;"><%= m.getItemName() %></span>
                            </div>
                            <span style="font-weight: 500;">₹<%= String.format("%.2f", m.getPrice() * item.getQuantity()) %></span>
                        </div>
                    <% } } %>
                </div>
                
                <div class="divider"></div>
                
                <div class="totals-row">
                    <span>Item Total</span>
                    <span>₹<%= String.format("%.2f", subTotal) %></span>
                </div>
                <div class="totals-row">
                    <span>Taxes & Fees (5%)</span>
                    <span>₹<%= String.format("%.2f", tax) %></span>
                </div>
                <div class="totals-row">
                    <span>Delivery Partner Fee</span>
                    <% if (deliveryFee == 0) { %>
                        <span style="color: #059669; font-weight: 600;">Free</span>
                    <% } else { %>
                        <span>₹<%= String.format("%.2f", deliveryFee) %></span>
                    <% } %>
                </div>
                
                <div class="divider"></div>
                
                <div class="grand-total">
                    <span>To Pay</span>
                    <span>₹<%= String.format("%.2f", grandTotal) %></span>
                </div>
            </div>
        </div>

    </div>

    <script>
        function selectPayment(method) {
            // 1. Remove active class from all wrappers
            document.querySelectorAll('.pay-option-wrapper').forEach(wrapper => {
                wrapper.classList.remove('active');
            });
            
            // 2. Add active class to the clicked wrapper
            document.getElementById('wrapper-' + method).classList.add('active');
            
            // 3. Check the hidden radio button so the form sends the correct data
            document.getElementById('radio-' + method).checked = true;
        }

        // Optional UX: Add selection styling to the UPI app buttons
        document.querySelectorAll('.upi-btn').forEach(btn => {
            btn.addEventListener('click', function(e) {
                // Prevent form submission if they click an app button
                e.preventDefault(); 
                document.querySelectorAll('.upi-btn').forEach(b => b.classList.remove('selected'));
                this.classList.add('selected');
            });
        });
    </script>
</body>
</html>