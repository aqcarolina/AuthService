package com.acme.app.user.controller;

import com.acme.app.user.dto.UserRegistrationRequest;
import com.acme.app.user.dto.UserResponse;
import com.acme.app.user.service.UserService;
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
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management endpoints")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Register a new user", description = "Creates a new user account and sends a verification email")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegistrationRequest registrationRequest) {
        UserResponse userResponse = userService.registerUser(registrationRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }
} 
