package com.example.RestaurantAuthorisation.controller;

import com.example.RestaurantAuthorisation.api.model.*;
import com.example.RestaurantAuthorisation.model.Session;
import com.example.RestaurantAuthorisation.model.User;
import com.example.RestaurantAuthorisation.repository.SessionRepository;
import com.example.RestaurantAuthorisation.service.UserService;
import com.example.RestaurantAuthorisation.util.JwtUtils;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final SessionRepository sessionRepository;

    @PostMapping("/registration")
    public ResponseEntity registration(@RequestBody RegistrationRequest registrationRequest) {
        try {
            return ResponseEntity.ok(userService.createUser(registrationRequest));
        }catch (ApiProblemException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {
        try{
            return ResponseEntity.ok(userService.login(loginRequest));
        }catch ( ApiProblemException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @GetMapping(path = "/info")
    public ResponseEntity info(@RequestHeader("Token") String token) {
        try{
            var session = sessionRepository.findByToken(token);
            if(session != null && session.isPresent()){
                User user = session.get().getUser();
                InfoResponse response = new InfoResponse();
                response.setCreated_at(user.getCreated_at());
                response.setEmail(user.getEmail());
                response.setRole(user.getRole());
                response.setUsername(user.getUsername());
                response.setUpdated_at(user.getUpdated_at());
                return ResponseEntity.ok(response);
            }
            throw new ApiProblemException(
                    HttpStatus.BAD_REQUEST,
                    "Not all fields provided");
        }catch ( ApiProblemException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

}
