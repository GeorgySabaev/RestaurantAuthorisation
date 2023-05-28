package com.example.RestaurantAuthorisation.api.model;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class UserResponse {
    private String username;
    private String email;
    private String accessToken;
}
