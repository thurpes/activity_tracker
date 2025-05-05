package com.web.activity_tracker.controller;

import com.web.activity_tracker.dto.JwtResponseDTO;
import com.web.activity_tracker.dto.LoginRequestDTO;
import com.web.activity_tracker.dto.SignupRequestDTO;
import com.web.activity_tracker.exception.UserAlreadyExistsException;
import com.web.activity_tracker.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful authentication",
                     content = @Content(schema = @Schema(implementation = JwtResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<JwtResponseDTO> authenticateUser(
            @Parameter(description = "Login credentials", required = true)
            @Valid @RequestBody LoginRequestDTO loginRequest) {
        JwtResponseDTO response = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
@Operation(summary = "User registration", description = "Registers a new user account")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "User registered successfully"),
    @ApiResponse(responseCode = "409", description = "Username or email already in use"),
    @ApiResponse(responseCode = "400", description = "Invalid input")
})
public ResponseEntity<String> registerUser(
        @Parameter(description = "Registration details", required = true)
        @Valid @RequestBody SignupRequestDTO signupRequest) {
    try {
        boolean success = authService.registerUser(signupRequest);
        return ResponseEntity.ok("User registered successfully");
    } catch (UserAlreadyExistsException e) {
        throw e; // Let the exception handler deal with this
    } catch (Exception e) {
        // Log the exception
        System.err.println("Error during user registration: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Registration failed due to server error");
    }
}


}