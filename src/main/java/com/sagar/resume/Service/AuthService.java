package com.sagar.resume.Service;

import com.sagar.resume.Document.User;
import com.sagar.resume.Dto.AuthResponse;
import com.sagar.resume.Dto.LoginRequest;
import com.sagar.resume.Dto.RegisterRequest;
import com.sagar.resume.Exception.ResourceExistsException;
import com.sagar.resume.Repository.UserRepository;
import com.sagar.resume.Util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class AuthService {


    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${app.base.url:http://localhost:8080}")
    private String appBaseUrl;

    public AuthService(UserRepository userRepository, EmailService emailService,PasswordEncoder passwordEncoder,JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder=passwordEncoder;
        this.jwtUtil=jwtUtil;
    }

    public AuthResponse register(RegisterRequest request) {

        log.info("Inside AuthService: register() {}", request);

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceExistsException("User already exists with this email");
        }

        User user = toDocument(request);
        userRepository.save(user);

        sendVerificationEmail(user);

        return toResponse(user);
    }

    private AuthResponse toResponse(User user) {
        AuthResponse response = new AuthResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setProfileImageUrl(user.getProfileImageUrl());
        response.setEmailVerified(user.isEmailVerified());
        response.setSubscriptionPlan(user.getSubscriptionPlan());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

    private User toDocument(RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setProfileImageUrl(request.getProfileImageUrl());
        user.setSubscriptionPlan("Basic");
        user.setEmailVerified(false);
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setVerificationExpires(LocalDateTime.now().plusHours(24));
        return user;
    }

    public void sendVerificationEmail(User user) {
        log.info("Inside AuthService - sendVerificationEmail() {}",user);
        try {
            String link = appBaseUrl + "/api/auth/verify-email?token=" + user.getVerificationToken();

            String html =
                    "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; " +
                            "border: 1px solid #e6e6e6; border-radius: 8px; background: #ffffff;\">" +

                            "<h2 style=\"color: #4a4a4a; text-align:center;\">Verify Your Email Address</h2>" +

                            "<p style=\"font-size: 16px; color: #333;\">Hi <strong>" + user.getName() + "</strong>,</p>" +

                            "<p style=\"font-size: 15px; color: #555; line-height: 1.6;\">" +
                            "Thanks for signing up! Please confirm your email address to activate your account." +
                            "</p>" +

                            "<div style=\"text-align:center; margin: 30px 0;\">" +
                            "<a href=\"" + link + "\" " +
                            "style=\"display:inline-block; padding: 12px 20px; background:#4F46E5; color:#ffffff; " +
                            "text-decoration:none; border-radius:6px; font-size:16px;\">" +
                            "Verify Email" +
                            "</a>" +
                            "</div>" +

                            "<p style=\"font-size: 14px; color: #666;\">If the button above doesn't work, copy the link below:</p>" +
                            "<p style=\"font-size: 14px; color: #0066cc; word-break: break-all;\">" + link + "</p>" +

                            "<p style=\"font-size: 13px; color: #999;\">This link expires in <strong>24 hours</strong>.</p>" +

                            "<p style=\"font-size: 13px; color: #999; margin-top: 30px;\">" +
                            "Regards,<br><strong>Your App Team</strong>" +
                            "</p>" +

                            "</div>";

            emailService.sendHtmlEmail(user.getEmail(), "Verify your email", html);

        } catch (Exception e) {
            log.error("Exception occured at sendVerificationEmail(): {}",e.getMessage());
            throw new RuntimeException("Failed to send verification email: " + e.getMessage());
        }
    }

    public void verifyEmail(String token)
    {
        log.info("Inside AuthService : verifyEmail(): {}",token);
        User user=userRepository.findByVerificationToken(token)
                 .orElseThrow(()->new RuntimeException("Invalid or expired verification token"));

        if(user.getVerificationExpires()!=null && user.getVerificationExpires().isBefore(LocalDateTime.now()))
        {
          throw new RuntimeException("Verification token has expired. Please request new one");
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationExpires(null);
        userRepository.save(user);

    }

    public AuthResponse login(LoginRequest request)
    {
        User existingUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), existingUser.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        if (!existingUser.isEmailVerified()) {
            throw new RuntimeException("Please verify your email before logging in.");
        }

        String token = jwtUtil.generateToken(existingUser.getId());

        AuthResponse response = toResponse(existingUser);
        response.setToken(token);

        return response;
    }
    public void resendVerification(String email)
    {
        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));

        if(user.isEmailVerified())
        {
            throw new RuntimeException("User's email is already verified");
        }

        user.setVerificationToken(UUID.randomUUID().toString());
        user.setVerificationExpires(LocalDateTime.now().plusHours(24));
        userRepository.save(user);
        sendVerificationEmail(user);

    }

    public AuthResponse getProfile(Object principalObject)
    {
        User user= (User) principalObject;
         return  toResponse(user);
    }

}
