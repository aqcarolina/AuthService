package com.acme.app.user.repository;

import com.acme.app.user.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    void updatePassword(UUID userId, String passwordHash);
    void activateUser(UUID userId);
} 
