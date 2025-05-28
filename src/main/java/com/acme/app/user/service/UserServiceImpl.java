package com.acme.app.user.service;

import com.acme.app.auth.model.VerificationToken;
import com.acme.app.auth.repository.VerificationTokenRepository;
import com.acme.app.common.service.EmailService;
import com.acme.app.common.util.TokenFactory;
import com.acme.app.user.dto.UserRegistrationRequest;
import com.acme.app.user.dto.UserResponse;
import com.acme.app.user.model.User;
import com.acme.app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenFactory tokenFactory;
    private final EmailService emailService;

    @Override
    @Transactional
    public UserResponse registerUser(UserRegistrationRequest registrationRequest) {
        // Check if user already exists
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new IllegalArgumentException("Email already in use: " + registrationRequest.getEmail());
        }

        // Create user
        User user = User.builder()
                .fullName(registrationRequest.getFullName())
                .email(registrationRequest.getEmail())
                .active(false)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered: {}", savedUser.getEmail());

        // Create verification token
        VerificationToken verificationToken = tokenFactory.createVerificationToken(savedUser.getId());
        verificationTokenRepository.save(verificationToken);

        // Send verification email
        emailService.sendVerificationEmail(savedUser, verificationToken.getToken());

        return mapToUserResponse(savedUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void updatePassword(UUID userId, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        userRepository.updatePassword(userId, encodedPassword);
        log.info("Password updated for user ID: {}", userId);
    }

    @Override
    @Transactional
    public void activateUser(UUID userId) {
        userRepository.activateUser(userId);
        log.info("User activated: {}", userId);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
} 
