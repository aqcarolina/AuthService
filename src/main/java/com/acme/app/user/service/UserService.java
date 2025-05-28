package com.acme.app.user.service;

import com.acme.app.user.dto.UserRegistrationRequest;
import com.acme.app.user.dto.UserResponse;
import com.acme.app.user.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserResponse registerUser(UserRegistrationRequest registrationRequest);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);
    boolean existsByEmail(String email);
    void updatePassword(UUID userId, String newPassword);
    void activateUser(UUID userId);
} 
