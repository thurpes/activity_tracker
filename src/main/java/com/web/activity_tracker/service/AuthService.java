package com.web.activity_tracker.service;

import com.web.activity_tracker.dto.JwtResponseDTO;
import com.web.activity_tracker.dto.LoginRequestDTO;

public interface AuthService {
    /**
     * Authenticate a user and generate JWT token
     * @param loginRequest Login credentials
     * @return JWT response with token
     */
    JwtResponseDTO authenticateUser(LoginRequestDTO loginRequest);
}