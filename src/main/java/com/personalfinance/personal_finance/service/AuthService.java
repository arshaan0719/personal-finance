package com.personalfinance.personal_finance.service;

import com.personalfinance.personal_finance.entity.User;
import com.personalfinance.personal_finance.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String name, String email, String password) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException(
                    "Password must be at least 6 characters"
            );
        }

        email = email.trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email is already registered"
            );
        }

        User user = new User();

        user.setName(name.trim());
        user.setEmail(email);

        // Never store the plain-text password
        user.setPassword(
                passwordEncoder.encode(password)
        );

        return userRepository.save(user);
    }

    public User login(String email, String password) {

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        User user = userRepository
                .findByEmail(email.trim().toLowerCase())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid email or password"
                        )
                );

        if (!passwordEncoder.matches(
                password,
                user.getPassword())) {

            throw new IllegalArgumentException(
                    "Invalid email or password"
            );
        }

        return user;
    }
}