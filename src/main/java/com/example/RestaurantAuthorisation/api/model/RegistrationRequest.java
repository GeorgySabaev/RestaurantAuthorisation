package com.example.RestaurantAuthorisation.api.model;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String username;
    private String email;
    private String password;
    private String role;
}
