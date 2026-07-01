package com.tap.DAO;

import java.util.List;
import com.tap.Model.User;

public interface UserDAO {
    void addUser(User user);
    User getUser(int userId);
    void updateUser(User user);
    void deleteUser(int userId);
    User loginUser(String email,String password);
    List<User> getAllUsers();
}