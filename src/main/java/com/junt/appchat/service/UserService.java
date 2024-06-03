package com.junt.appchat.service;

import com.junt.appchat.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> getUsers();
    User getUserById(UUID id);
    User getUserByUsername(String username);
    User saveUser(User user);
    void deleteUser(UUID id);
}
