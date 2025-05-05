package com.web.activity_tracker.service;

import com.web.activity_tracker.dto.ActivityDTO;
import com.web.activity_tracker.dto.JwtResponseDTO;
import com.web.activity_tracker.dto.LoginRequestDTO;
import com.web.activity_tracker.dto.SignupRequestDTO;
import com.web.activity_tracker.exception.UserAlreadyExistsException;
import com.web.activity_tracker.model.User;
import com.web.activity_tracker.repository.UserRepository;
import com.web.activity_tracker.security.JwtTokenProvider;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public JwtResponseDTO authenticateUser(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String jwt = jwtTokenProvider.generateJwtToken(authentication);
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        Optional<User> userOptional = userRepository.findByUsername(userDetails.getUsername());
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            ActivityDTO activityDTO = ActivityDTO.builder()
                    .userId(user.getId())
                    .action("LOGIN")
                    .description("User logged in")
                    .build();
            
            activityService.logActivity(activityDTO);
            
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

    @Override
    @Transactional
    public boolean registerUser(SignupRequestDTO signupRequest) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new UserAlreadyExistsException("Username is already taken");
        }
        
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email is already in use");
        }
        
        User user = User.builder()
                .username(signupRequest.getUsername())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .email(signupRequest.getEmail())
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        User savedUser = userRepository.save(user);
        
        try {
            ActivityDTO activityDTO = ActivityDTO.builder()
                    .userId(savedUser.getId())
                    .action("REGISTRATION")
                    .description("New user registration")
                    .build();
            
            activityService.logActivity(activityDTO);
        } catch (Exception e) {
            System.err.println("Failed to log registration activity: " + e.getMessage());
        }
        
        return true;
    }
}
