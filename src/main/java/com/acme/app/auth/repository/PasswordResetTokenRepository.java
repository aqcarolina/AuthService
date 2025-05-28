package com.acme.app.auth.repository;

import com.acme.app.auth.model.PasswordResetToken;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository {
    PasswordResetToken save(PasswordResetToken passwordResetToken);
    Optional<PasswordResetToken> findByToken(UUID token);
    void markAsUsed(UUID tokenId);
    void deleteByUserId(UUID userId);
} 
