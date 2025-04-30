package com.web.activity_tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login request with credentials")
public class LoginRequestDTO {
    @NotBlank
    @Schema(description = "Username for authentication", example = "admin", required = true)
    private String username;
    
    @NotBlank
    @Schema(description = "Password for authentication", example = "password", required = true)
    private String password;
}