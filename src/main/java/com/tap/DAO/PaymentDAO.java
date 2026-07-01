package com.tap.DAO;

import com.tap.Model.Payment;

public interface PaymentDAO {
    void addPayment(Payment payment);
    Payment getPayment(int paymentId);
    
    // Very important: Find the payment associated with a specific order
    Payment getPaymentByOrderId(int orderId); 
}