package com.web.activity_tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object for user activities")
public class ActivityDTO {
    @Schema(description = "Unique identifier of the activity", example = "1")
    private Long id;
    
    @Schema(description = "ID of the user who performed the activity", example = "1", required = true)
    private Long userId;
    
    @Schema(description = "Username of the user who performed the activity", example = "john.doe")
    private String username;
    
    @Schema(description = "Type of action performed", example = "LOGIN", required = true)
    private String action;
    
    @Schema(description = "Detailed description of the activity", example = "User logged in from web browser")
    private String description;
    
    @Schema(description = "IP address of the client", example = "192.168.1.1")
    private String ipAddress;
    
    @Schema(description = "User agent of the client", example = "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
    private String userAgent;
    
    @Schema(description = "Timestamp when the activity occurred")
    private LocalDateTime createdAt;
}
