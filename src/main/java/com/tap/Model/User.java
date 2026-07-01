package com.tap.Model;

import java.sql.Timestamp;

public class User {
    
    // 1. Private fields matching your database schema exactly
    private int userId;
    private String userName;
    private String password;
    private String email;
    private String phone;
    private String address;
    private String role;
    private Timestamp createDate;
    private Timestamp lastLoginDate;

    // 2. Default Constructor (Crucial for frameworks and standard Java EE)
    public User() {
    }

    // 3. Parameterized Constructor (Used when registering a new user, ignores DB-generated fields)
    public User(String userName, String password, String email, String phone, String address, String role) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    // 4. Fully Parameterized Constructor (Used when pulling complete data from the database)
    public User(int userId, String userName, String password, String email, String phone, String address, String role, Timestamp createDate, Timestamp lastLoginDate) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.createDate = createDate;
        this.lastLoginDate = lastLoginDate;
    }

    // 5. Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Timestamp lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    // 6. toString() Method for clean debugging and logging
    @Override
    public String toString() {
        return "User {" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                // Note: Password is purposefully omitted from toString() for security best practices!
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", role='" + role + '\'' +
                ", createDate=" + createDate +
                ", lastLoginDate=" + lastLoginDate +
                '}';
    }
}