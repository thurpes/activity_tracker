package com.web.activity_tracker.service;

import com.web.activity_tracker.dto.ActivityDTO;
import com.web.activity_tracker.dto.JwtResponseDTO;
import com.web.activity_tracker.dto.LoginRequestDTO;
import com.web.activity_tracker.model.User;
import com.web.activity_tracker.repository.UserRepository;
import com.web.activity_tracker.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ActivityService activityService;

    @Override
    public JwtResponseDTO authenticateUser(LoginRequestDTO loginRequest) {
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        // Set authentication in SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // Generate JWT token
        String jwt = jwtTokenProvider.generateJwtToken(authentication);
        
        // Get user details
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        // Find the user entity
        Optional<User> userOptional = userRepository.findByUsername(userDetails.getUsername());
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            // Log the login activity
            ActivityDTO activityDTO = ActivityDTO.builder()
                    .userId(user.getId())
                    .action("LOGIN")
                    .description("User logged in")
                    .build();
            
            activityService.logActivity(activityDTO);
            
            // Create and return the response
            return JwtResponseDTO.builder()
                    .token(jwt)
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .build();
        }
        
        return JwtResponseDTO.builder()
                .token(jwt)
                .build();
    }
}
