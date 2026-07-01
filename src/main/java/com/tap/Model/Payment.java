package com.tap.Model;

public class Payment {
    
    private int paymentId;
    private int orderId;
    private double amount;
    private String paymentMethod;
    private String paymentStatus;

    // 1. Default Constructor
    public Payment() {
    }

    // 2. Parameterized Constructor (Used when adding a new payment, ignores auto-increment ID)
    public Payment(int orderId, double amount, String paymentMethod, String paymentStatus) {
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
    }

    // 3. Fully Parameterized Constructor (Used when fetching from the database)
    public Payment(int paymentId, int orderId, double amount, String paymentMethod, String paymentStatus) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
    }

    // 4. Getters and Setters
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    // 5. toString() for debugging
    @Override
    public String toString() {
        return "Payment {" +
                "PaymentID=" + paymentId +
                ", OrderID=" + orderId +
                ", Amount=₹" + amount +
                ", Method='" + paymentMethod + '\'' +
                ", Status='" + paymentStatus + '\'' +
                '}';
    }
}