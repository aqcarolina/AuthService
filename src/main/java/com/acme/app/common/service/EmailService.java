package com.acme.app.common.service;

import com.acme.app.user.model.User;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class EmailService {

    private final Resend resendClient;
    
    @Value("${resend.from-email}")
    private String fromEmail;
    
    @Value("${resend.from-name}")
    private String fromName;
    
    @Value("${app.frontend-url}")
    private String frontendUrl;

    public EmailService(@Value("${resend.api-key}") String apiKey) {
        this.resendClient = new Resend(apiKey);
    }

    public void sendVerificationEmail(User user, UUID token) {
        String verificationLink = frontendUrl + "/activate?token=" + token;
        
        String htmlContent = String.format(
                "<h1>Welcome to Auth Service, %s!</h1>" +
                "<p>Thank you for registering. Please click the link below to verify your email address:</p>" +
                "<p>Verify Email: %s</p>" +
                "<p>If you did not create an account, please ignore this email.</p>",
                user.getFullName(), verificationLink);

        sendEmail(user.getEmail(), "Verify Your Email", htmlContent);
    }

    public void sendPasswordResetEmail(User user, UUID token) {
        String resetLink = frontendUrl + "/reset-password?token=" + token;
        
        String htmlContent = String.format(
                "<h1>Password Reset Request</h1>" +
                "<p>Hi %s,</p>" +
                "<p>You have requested to reset your password. Please click the link below to reset it:</p>" +
                "<p>Reset Password: %s</p>" +
                "<p>If you did not request a password reset, please ignore this email.</p>",
                user.getFullName(), resetLink);

        sendEmail(user.getEmail(), "Reset Your Password", htmlContent);
    }

    private void sendEmail(String to, String subject, String htmlContent) {
        try {
            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from(fromName + " <" + fromEmail + ">")
                    .to(to)
                    .subject(subject)
                    .html(htmlContent)
                    .build();

            resendClient.emails().send(params);
            log.info("Email sent successfully to: {}", to);
        } catch (ResendException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
} 
