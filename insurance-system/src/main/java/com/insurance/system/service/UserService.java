package com.insurance.system.service;

import com.insurance.system.entity.User;

public interface UserService {
    User registerUser(User user);
    User loginUser(String email, String password);
}
