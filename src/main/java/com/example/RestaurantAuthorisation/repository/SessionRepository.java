package com.example.RestaurantAuthorisation.repository;

import com.example.RestaurantAuthorisation.model.Session;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, Long> {
    Optional<Session> findByToken(String token);
}
