package com.acme.app.auth.service;

import com.acme.app.auth.dto.LoginRequest;
import com.acme.app.auth.dto.LoginResponse;
import com.acme.app.auth.dto.PasswordResetRequest;
import com.acme.app.auth.dto.SetPasswordRequest;

import java.util.UUID;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
    void activateAccount(SetPasswordRequest setPasswordRequest);
    void requestPasswordReset(PasswordResetRequest passwordResetRequest);
    void resetPassword(SetPasswordRequest setPasswordRequest);
} 
