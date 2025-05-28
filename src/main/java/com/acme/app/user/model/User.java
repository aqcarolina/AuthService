package com.acme.app.user.model;

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
public class User {
    private UUID id;
    private String fullName;
    private String email;
    private String passwordHash;
    private boolean active;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
} 
