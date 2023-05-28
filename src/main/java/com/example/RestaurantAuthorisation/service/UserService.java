package com.example.RestaurantAuthorisation.service;

import com.example.RestaurantAuthorisation.api.model.LoginRequest;
import com.example.RestaurantAuthorisation.api.model.RegistrationRequest;
import com.example.RestaurantAuthorisation.api.model.UserResponse;

public interface UserService {
    String createUser(RegistrationRequest user);
    UserResponse login(LoginRequest loginRequest);
}
