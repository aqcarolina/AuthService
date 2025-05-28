package com.acme.app.auth.repository;

import com.acme.app.auth.model.VerificationToken;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Repository
public class VerificationTokenRepositoryImpl implements VerificationTokenRepository {

    private final DSLContext dsl;

    @Autowired
    public VerificationTokenRepositoryImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public VerificationToken save(VerificationToken verificationToken) {
        if (verificationToken.getId() == null) {
            verificationToken.setId(UUID.randomUUID());
        }
        
        if (verificationToken.getCreatedAt() == null) {
            verificationToken.setCreatedAt(OffsetDateTime.now());
        }

        dsl.insertInto(table("verification_token"))
                .set(field("id"), verificationToken.getId())
                .set(field("token"), verificationToken.getToken())
                .set(field("user_id"), verificationToken.getUserId())
                .set(field("expiry_date"), verificationToken.getExpiryDate())
                .set(field("used"), verificationToken.isUsed())
                .set(field("created_at"), verificationToken.getCreatedAt())
                .execute();

        return verificationToken;
    }

    @Override
    public Optional<VerificationToken> findByToken(UUID token) {
        return dsl.selectFrom(table("verification_token"))
                .where(field("token").eq(token))
                .fetchOptional(record -> mapToVerificationToken(record));
    }

    @Override
    public void markAsUsed(UUID tokenId) {
        dsl.update(table("verification_token"))
                .set(field("used"), true)
                .where(field("id").eq(tokenId))
                .execute();
    }

    @Override
    public void deleteByUserId(UUID userId) {
        dsl.deleteFrom(table("verification_token"))
                .where(field("user_id").eq(userId))
                .execute();
    }

    private VerificationToken mapToVerificationToken(org.jooq.Record record) {
        return VerificationToken.builder()
                .id(record.get(field("id"), UUID.class))
                .token(record.get(field("token"), UUID.class))
                .userId(record.get(field("user_id"), UUID.class))
                .expiryDate(record.get(field("expiry_date"), OffsetDateTime.class))
                .used(record.get(field("used"), Boolean.class))
                .createdAt(record.get(field("created_at"), OffsetDateTime.class))
                .build();
    }
} 
