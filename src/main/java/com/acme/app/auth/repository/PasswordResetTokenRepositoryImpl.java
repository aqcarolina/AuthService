package com.acme.app.auth.repository;

import com.acme.app.auth.model.PasswordResetToken;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Repository
public class PasswordResetTokenRepositoryImpl implements PasswordResetTokenRepository {

    private final DSLContext dsl;

    @Autowired
    public PasswordResetTokenRepositoryImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public PasswordResetToken save(PasswordResetToken passwordResetToken) {
        if (passwordResetToken.getId() == null) {
            passwordResetToken.setId(UUID.randomUUID());
        }
        
        if (passwordResetToken.getCreatedAt() == null) {
            passwordResetToken.setCreatedAt(OffsetDateTime.now());
        }

        dsl.insertInto(table("password_reset_token"))
                .set(field("id"), passwordResetToken.getId())
                .set(field("token"), passwordResetToken.getToken())
                .set(field("user_id"), passwordResetToken.getUserId())
                .set(field("expiry_date"), passwordResetToken.getExpiryDate())
                .set(field("used"), passwordResetToken.isUsed())
                .set(field("created_at"), passwordResetToken.getCreatedAt())
                .execute();

        return passwordResetToken;
    }

    @Override
    public Optional<PasswordResetToken> findByToken(UUID token) {
        return dsl.selectFrom(table("password_reset_token"))
                .where(field("token").eq(token))
                .fetchOptional(record -> mapToPasswordResetToken(record));
    }

    @Override
    public void markAsUsed(UUID tokenId) {
        dsl.update(table("password_reset_token"))
                .set(field("used"), true)
                .where(field("id").eq(tokenId))
                .execute();
    }

    @Override
    public void deleteByUserId(UUID userId) {
        dsl.deleteFrom(table("password_reset_token"))
                .where(field("user_id").eq(userId))
                .execute();
    }

    private PasswordResetToken mapToPasswordResetToken(org.jooq.Record record) {
        return PasswordResetToken.builder()
                .id(record.get(field("id"), UUID.class))
                .token(record.get(field("token"), UUID.class))
                .userId(record.get(field("user_id"), UUID.class))
                .expiryDate(record.get(field("expiry_date"), OffsetDateTime.class))
                .used(record.get(field("used"), Boolean.class))
                .createdAt(record.get(field("created_at"), OffsetDateTime.class))
                .build();
    }
} 
