package com.acme.app.common.util;

import com.acme.app.auth.model.PasswordResetToken;
import com.acme.app.auth.model.VerificationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class TokenFactory {

    @Value("${token.verification.expiration}")
    private long verificationTokenExpiration;

    @Value("${token.password-reset.expiration}")
    private long passwordResetTokenExpiration;

    public VerificationToken createVerificationToken(UUID userId) {
        return VerificationToken.builder()
                .token(UUID.randomUUID())
                .userId(userId)
                .expiryDate(OffsetDateTime.now().plus(Duration.ofMillis(verificationTokenExpiration)))
                .used(false)
                .build();
    }

    public PasswordResetToken createPasswordResetToken(UUID userId) {
        return PasswordResetToken.builder()
                .token(UUID.randomUUID())
                .userId(userId)
                .expiryDate(OffsetDateTime.now().plus(Duration.ofMillis(passwordResetTokenExpiration)))
                .used(false)
                .build();
    }
} 
