package com.acme.app.auth.repository;

import com.acme.app.auth.model.VerificationToken;

import java.util.Optional;
import java.util.UUID;

public interface VerificationTokenRepository {
    VerificationToken save(VerificationToken verificationToken);
    Optional<VerificationToken> findByToken(UUID token);
    void markAsUsed(UUID tokenId);
    void deleteByUserId(UUID userId);
} 
