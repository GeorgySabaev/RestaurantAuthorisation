package com.example.RestaurantAuthorisation.service;

import com.example.RestaurantAuthorisation.api.model.ApiProblemException;
import com.example.RestaurantAuthorisation.api.model.LoginRequest;
import com.example.RestaurantAuthorisation.api.model.RegistrationRequest;
import com.example.RestaurantAuthorisation.api.model.UserResponse;
import com.example.RestaurantAuthorisation.model.Session;
import com.example.RestaurantAuthorisation.model.User;
import com.example.RestaurantAuthorisation.repository.SessionRepository;
import com.example.RestaurantAuthorisation.repository.UserRepository;
import com.example.RestaurantAuthorisation.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    //private static final MyResponse SIMPLE_SUCCESS_RESPONSE = createSimpleSuccessResponse();

    private static final int BCRYPT_ROUNDS = 13;


    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    @Transactional
    public String createUser(RegistrationRequest registrationRequest) {
        if (registrationRequest.getEmail() == null || registrationRequest.getPassword() == null || registrationRequest.getUsername() == null || registrationRequest.getEmail().isEmpty() || registrationRequest.getPassword().isEmpty() || registrationRequest.getUsername().isEmpty()) {
            System.out.println(registrationRequest.getEmail());
            System.out.println(registrationRequest.getPassword());
            throw new ApiProblemException(
                    HttpStatus.FORBIDDEN,
                    "Invalid token");
        }

        if (!userRepository.findByUsername(registrationRequest.getUsername()).isEmpty()) {
            throw new ApiProblemException(
                    HttpStatus.FORBIDDEN,
                    "Username taken");
        }

        if (userRepository.findByEmail(registrationRequest.getEmail()).isPresent()) {
            throw new ApiProblemException(
                    HttpStatus.FORBIDDEN,
                    "Email taken");
        }

        if (!registrationRequest.getEmail().contains("@")) {
            throw new ApiProblemException(
                    HttpStatus.FORBIDDEN,
                    "Incorrect email");
        }

        if (null == registrationRequest.getRole() || !(Arrays.asList(new String[]{"chef", "customer", "manager"}).contains(registrationRequest.getRole()))) {
            throw new ApiProblemException(
                    HttpStatus.CONFLICT,
                    "Incorrect role");
        }

        User newUser = new User();
        newUser.setUsername(registrationRequest.getUsername());
        newUser.setPassword_hash(BCrypt.hashpw(registrationRequest.getPassword(), BCrypt.gensalt(BCRYPT_ROUNDS)));
        newUser.setEmail(registrationRequest.getEmail());
        newUser.setRole(registrationRequest.getRole());

        userRepository.save(newUser);

        return "Registration successful!";
    }

    @Transactional
    @Override
    public UserResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getLogin()).orElseThrow(
                () -> new ApiProblemException(
                        HttpStatus.FORBIDDEN,
                        "Invalid credentials")
        );

        if (!checkPassword(user.getPassword_hash(), loginRequest.getPassword())) {
            throw new ApiProblemException(
                    HttpStatus.FORBIDDEN,
                    "Invalid credentials");
        }

        var session = new Session();
        session.setUser(user);
        session.setToken(JwtUtils.generateToken(user));
        session.setExpires_at(Timestamp.from(LocalDateTime.now().plusMinutes(30)
                .atZone(ZoneId.systemDefault()).toInstant()));
        sessionRepository.save(session);

        var resp = new UserResponse();
        resp.setUsername(user.getUsername());
        resp.setEmail(user.getEmail());
        resp.setAccessToken(session.getToken());
        return resp;
    }

    public static boolean checkPassword(String passwordHash, String password) {
        String salt = passwordHash.substring(0, 30);

        String newHash = BCrypt.hashpw(password, salt);
//        System.out.println(newHash);
        return passwordHash.equals(newHash);
    }


}
