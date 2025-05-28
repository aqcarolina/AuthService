package com.acme.app.user.repository;

import com.acme.app.user.model.User;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final DSLContext dsl;

    @Autowired
    public UserRepositoryImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
        
        OffsetDateTime now = OffsetDateTime.now();
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(now);
        }
        user.setUpdatedAt(now);

        dsl.insertInto(table("users"))
                .set(field("id"), user.getId())
                .set(field("full_name"), user.getFullName())
                .set(field("email"), user.getEmail())
                .set(field("password_hash"), user.getPasswordHash())
                .set(field("active"), user.isActive())
                .set(field("created_at"), user.getCreatedAt())
                .set(field("updated_at"), user.getUpdatedAt())
                .execute();

        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return dsl.selectFrom(table("users"))
                .where(field("id").eq(id))
                .fetchOptional(record -> mapToUser(record));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return dsl.selectFrom(table("users"))
                .where(field("email").eq(email))
                .fetchOptional(record -> mapToUser(record));
    }

    @Override
    public boolean existsByEmail(String email) {
        return dsl.fetchExists(
                dsl.selectFrom(table("users"))
                        .where(field("email").eq(email))
        );
    }

    @Override
    public void updatePassword(UUID userId, String passwordHash) {
        dsl.update(table("users"))
                .set(field("password_hash"), passwordHash)
                .set(field("updated_at"), OffsetDateTime.now())
                .where(field("id").eq(userId))
                .execute();
    }

    @Override
    public void activateUser(UUID userId) {
        dsl.update(table("users"))
                .set(field("active"), true)
                .set(field("updated_at"), OffsetDateTime.now())
                .where(field("id").eq(userId))
                .execute();
    }

    private User mapToUser(org.jooq.Record record) {
        return User.builder()
                .id(record.get(field("id"), UUID.class))
                .fullName(record.get(field("full_name"), String.class))
                .email(record.get(field("email"), String.class))
                .passwordHash(record.get(field("password_hash"), String.class))
                .active(record.get(field("active"), Boolean.class))
                .createdAt(record.get(field("created_at"), OffsetDateTime.class))
                .updatedAt(record.get(field("updated_at"), OffsetDateTime.class))
                .build();
    }
} 
