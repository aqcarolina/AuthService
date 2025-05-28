package com.acme.app.auth.service;

import com.acme.app.auth.dto.LoginRequest;
import com.acme.app.auth.dto.LoginResponse;
import com.acme.app.auth.dto.PasswordResetRequest;
import com.acme.app.auth.dto.SetPasswordRequest;
import com.acme.app.auth.model.PasswordResetToken;
import com.acme.app.auth.model.VerificationToken;
import com.acme.app.auth.repository.PasswordResetTokenRepository;
import com.acme.app.auth.repository.VerificationTokenRepository;
import com.acme.app.common.service.EmailService;
import com.acme.app.common.util.JwtUtil;
import com.acme.app.common.util.TokenFactory;
import com.acme.app.user.model.User;
import com.acme.app.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final TokenFactory tokenFactory;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userService.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
        
        if (!user.isActive()) {
            throw new IllegalStateException("Account is not activated");
        }
        
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password");
        }
        
        String token = jwtUtil.generateToken(user.getId(), user.getEmail());
        
        return LoginResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .token(token)
                .build();
    }

    @Override
    @Transactional
    public void activateAccount(SetPasswordRequest setPasswordRequest) {
        UUID tokenUUID = UUID.fromString(setPasswordRequest.getToken());
        
        VerificationToken verificationToken = verificationTokenRepository.findByToken(tokenUUID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        
        if (verificationToken.isExpired()) {
            throw new IllegalStateException("Token has expired");
        }
        
        if (verificationToken.isUsed()) {
            throw new IllegalStateException("Token has already been used");
        }
        
        userService.updatePassword(verificationToken.getUserId(), setPasswordRequest.getPassword());
        userService.activateUser(verificationToken.getUserId());
        
        verificationTokenRepository.markAsUsed(verificationToken.getId());
        
        log.info("User account activated: {}", verificationToken.getUserId());
    }

    @Override
    @Transactional
    public void requestPasswordReset(PasswordResetRequest passwordResetRequest) {
        User user = userService.findByEmail(passwordResetRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        // Delete any existing reset tokens for this user
        passwordResetTokenRepository.deleteByUserId(user.getId());
        
        // Create a new reset token
        PasswordResetToken resetToken = tokenFactory.createPasswordResetToken(user.getId());
        passwordResetTokenRepository.save(resetToken);
        
        // Send password reset email
        emailService.sendPasswordResetEmail(user, resetToken.getToken());
        
        log.info("Password reset requested for user: {}", user.getEmail());
    }

    @Override
    @Transactional
    public void resetPassword(SetPasswordRequest setPasswordRequest) {
        UUID tokenUUID = UUID.fromString(setPasswordRequest.getToken());
        
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(tokenUUID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        
        if (resetToken.isExpired()) {
            throw new IllegalStateException("Token has expired");
        }
        
        if (resetToken.isUsed()) {
            throw new IllegalStateException("Token has already been used");
        }
        
        userService.updatePassword(resetToken.getUserId(), setPasswordRequest.getPassword());
        
        passwordResetTokenRepository.markAsUsed(resetToken.getId());
        
        log.info("Password reset successfully for user ID: {}", resetToken.getUserId());
    }
} 
