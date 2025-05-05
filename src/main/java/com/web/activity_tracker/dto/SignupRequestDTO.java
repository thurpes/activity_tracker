package com.web.activity_tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Signup request with user information")
public class SignupRequestDTO {
    
    @NotBlank
    @Size(min = 3, max = 50)
    @Schema(description = "Username for the new account", example = "john.doe", required = true)
    private String username;
    
    @NotBlank
    @Size(min = 6, max = 100)
    @Schema(description = "Password for the new account", example = "password123", required = true)
    private String password;
    
    @NotBlank
    @Size(max = 100)
    @Email
    @Schema(description = "Email address for the new account", example = "john.doe@example.com", required = true)
    private String email;
    
    @Size(max = 50)
    @Schema(description = "User's first name", example = "John")
    private String firstName;
    
    @Size(max = 50)
    @Schema(description = "User's last name", example = "Doe")
    private String lastName;
}