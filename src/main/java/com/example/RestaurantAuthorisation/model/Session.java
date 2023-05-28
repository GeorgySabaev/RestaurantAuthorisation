package com.example.RestaurantAuthorisation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "session"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "session_token", nullable = false)
    private String token;

    @Column(name = "expires_at", nullable = false)
    private java.sql.Timestamp expires_at;
}
