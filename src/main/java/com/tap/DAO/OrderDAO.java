package com.tap.DAO;

import java.util.List;
import com.tap.Model.Order;
import com.tap.Model.OrderItem; // You will need to import this model

public interface OrderDAO {
    // Master (Header) Methods
    void addOrder(Order order);
    Order getOrder(int orderId);
    void updateOrderStatus(int orderId, String status);
    void deleteOrder(int orderId); 
    
    // Detail (Item) Methods - IMPORTANT: Add this!
    void addOrderItem(OrderItem orderItem);
    
    // Retrieval Methods
    List<Order> getAllOrders();
    List<Order> getOrdersByUserId(int userId);
    List<Order> getOrdersByRestaurantId(int restaurantId);
}