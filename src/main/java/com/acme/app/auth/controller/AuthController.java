package com.acme.app.auth.controller;

import com.acme.app.auth.dto.LoginRequest;
import com.acme.app.auth.dto.LoginResponse;
import com.acme.app.auth.dto.PasswordResetRequest;
import com.acme.app.auth.dto.SetPasswordRequest;
import com.acme.app.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and account management endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate user and return JWT token")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/activate")
    @Operation(summary = "Activate account", description = "Verify email and set password for a newly registered account")
    public ResponseEntity<Void> activateAccount(@Valid @RequestBody SetPasswordRequest setPasswordRequest) {
        authService.activateAccount(setPasswordRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Request password reset", description = "Send a password reset email")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest) {
        authService.requestPasswordReset(passwordResetRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Set a new password using a reset token")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody SetPasswordRequest setPasswordRequest) {
        authService.resetPassword(setPasswordRequest);
        return ResponseEntity.ok().build();
    }
} 
