package com.example.RestaurantAuthorisation.api.model;

import lombok.Data;

@Data
public class InfoResponse {
    private String username;
    private String email;
    private String role;
    private java.sql.Timestamp created_at;
    private java.sql.Timestamp updated_at;
}
