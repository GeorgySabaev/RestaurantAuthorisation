package com.example.RestaurantAuthorisation.repository;

import java.util.List;
import java.util.Optional;

import com.example.RestaurantAuthorisation.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findByUsername(String email);
}