package com.tap.DAO;

import java.util.List;
import com.tap.Model.OrderItem;

public interface OrderItemDAO {
    void addOrderItem(OrderItem orderItem);
    OrderItem getOrderItem(int orderItemId);
    
    // Application-critical method to generate the final bill/receipt
    List<OrderItem> getOrderItemsByOrderId(int orderId); 
}