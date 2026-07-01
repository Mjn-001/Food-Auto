package com.tap.Model;

public class Restaurant {
    
    // 1. Private fields matching your database schema
    private int restaurantId;
    private String name;
    private String cuisineType;
    private String address;
    private int deliveryTime;
    private double rating;
    private boolean isActive;
    private int adminUserId; // The Foreign Key

    // 2. Default Constructor (Required by frameworks)
    public Restaurant() {
    }

    // 3. Parameterized Constructor (Used when adding a new restaurant, ignores DB-generated ID)
    public Restaurant(String name, String cuisineType, String address, int deliveryTime, double rating, boolean isActive, int adminUserId) {
        this.name = name;
        this.cuisineType = cuisineType;
        this.address = address;
        this.deliveryTime = deliveryTime;
        this.rating = rating;
        this.isActive = isActive;
        this.adminUserId = adminUserId;
    }

    // 4. Fully Parameterized Constructor (Used when fetching complete data from the database)
    public Restaurant(int restaurantId, String name, String cuisineType, String address, int deliveryTime, double rating, boolean isActive, int adminUserId) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.cuisineType = cuisineType;
        this.address = address;
        this.deliveryTime = deliveryTime;
        this.rating = rating;
        this.isActive = isActive;
        this.adminUserId = adminUserId;
    }

    // 5. Getters and Setters
    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCuisineType() { return cuisineType; }
    public void setCuisineType(String cuisineType) { this.cuisineType = cuisineType; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getDeliveryTime() { return deliveryTime; }
    public void setDeliveryTime(int deliveryTime) { this.deliveryTime = deliveryTime; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean isActive) { this.isActive = isActive; }

    public int getAdminUserId() { return adminUserId; }
    public void setAdminUserId(int adminUserId) { this.adminUserId = adminUserId; }

    // 6. toString() Method for clean debugging
    @Override
    public String toString() {
        return "Restaurant {" +
                "ID=" + restaurantId +
                ", Name='" + name + '\'' +
                ", Cuisine='" + cuisineType + '\'' +
                ", Address='" + address + '\'' +
                ", Est. Delivery=" + deliveryTime + " mins" +
                ", Rating=" + rating +
                ", Active=" + isActive +
                ", AdminID=" + adminUserId +
                '}';
    }
}