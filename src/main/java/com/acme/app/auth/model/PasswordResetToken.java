package com.acme.app.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {
    private UUID id;
    private UUID token;
    private UUID userId;
    private OffsetDateTime expiryDate;
    private boolean used;
    private OffsetDateTime createdAt;
    
    public boolean isExpired() {
        return expiryDate.isBefore(OffsetDateTime.now());
    }
} 
