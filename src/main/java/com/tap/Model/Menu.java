package com.tap.Model;

public class Menu {
    
    // 1. Private fields perfectly matching your database schema
    private int menuId;
    private int restaurantId; // Foreign Key linking to Restaurant table
    private String itemName;
    private String description;
    private double price;
    private String category;
    private boolean isAvailable;

    // 2. Default Constructor (Required by Java frameworks)
    public Menu() {
    }

    // 3. Parameterized Constructor (Used when adding a new item, ignores DB-generated menuId)
    public Menu(int restaurantId, String itemName, String description, double price, String category, boolean isAvailable) {
        this.restaurantId = restaurantId;
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.category = category;
        this.isAvailable = isAvailable;
    }

    // 4. Fully Parameterized Constructor (Used when fetching complete data from the database)
    public Menu(int menuId, int restaurantId, String itemName, String description, double price, String category, boolean isAvailable) {
        this.menuId = menuId;
        this.restaurantId = restaurantId;
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.category = category;
        this.isAvailable = isAvailable;
    }

    // 5. Getters and Setters
    public int getMenuId() { return menuId; }
    public void setMenuId(int menuId) { this.menuId = menuId; }

    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean isAvailable) { this.isAvailable = isAvailable; }

    // 6. toString() Method for clean debugging
    @Override
    public String toString() {
        return "Menu Item {" +
                "ID=" + menuId +
                ", RestaurantID=" + restaurantId +
                ", Item='" + itemName + '\'' +
                ", Desc='" + description + '\'' +
                ", Price=₹" + price + // Assuming INR for Zomato clone
                ", Category='" + category + '\'' +
                ", Available=" + isAvailable +
                '}';
    }
}