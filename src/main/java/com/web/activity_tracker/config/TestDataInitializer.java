package com.web.activity_tracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.web.activity_tracker.model.User;
import com.web.activity_tracker.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Component
public class TestDataInitializer {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostConstruct
    public void init() {
        if (userRepository.findByUsername("testuser").isEmpty()) {
            User user = new User();
            user.setUsername("testuser");
            user.setPassword(passwordEncoder.encode("password123"));
            user.setEmail("test@example.com");
            userRepository.save(user);
            System.out.println("TEST USER CREATED: testuser / password123");
        }
    }
}
