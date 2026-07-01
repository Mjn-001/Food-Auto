package com.tap.DAO;

import java.util.List;
import com.tap.Model.Restaurant;

public interface RestaurantDAO {
    void addRestaurant(Restaurant restaurant);
    Restaurant getRestaurant(int restaurantId);
    void updateRestaurant(Restaurant restaurant);
    void deleteRestaurant(int restaurantId);
    List<Restaurant> getAllRestaurants();
    List<Restaurant> getRestaurantsByAdmin(int adminUserId);
    
    // ADD THIS LINE BELOW:
    List<Restaurant> searchRestaurants(String keyword);
}