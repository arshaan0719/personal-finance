package com.personalfinance.personal_finance.controller;

import com.personalfinance.personal_finance.entity.User;
import com.personalfinance.personal_finance.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request) {

        try {
            User user = authService.register(
                    request.getName(),
                    request.getEmail(),
                    request.getPassword()
            );

            // Don't send the password/hash back to the frontend
            user.setPassword(null);

            return ResponseEntity.ok(user);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request) {

        try {
            User user = authService.login(
                    request.getEmail(),
                    request.getPassword()
            );

            // Don't send the password/hash back to the frontend
            user.setPassword(null);

            return ResponseEntity.ok(user);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(401)
                    .body(e.getMessage());
        }
    }

    // Register request
    public static class RegisterRequest {

        private String name;
        private String email;
        private String password;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // Login request
    public static class LoginRequest {

        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}