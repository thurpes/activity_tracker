package com.web.activity_tracker.service;

import com.web.activity_tracker.dto.JwtResponseDTO;
import com.web.activity_tracker.dto.LoginRequestDTO;
import com.web.activity_tracker.dto.SignupRequestDTO;

public interface AuthService {
    /**
     * Authenticate a user and generate JWT token
     * @param loginRequest Login credentials
     * @return JWT response with token
     */
    JwtResponseDTO authenticateUser(LoginRequestDTO loginRequest);
    
    /**
     * Register a new user
     * @param signupRequest User registration details
     * @return true if registration is successful
     */
    boolean registerUser(SignupRequestDTO signupRequest);
}

