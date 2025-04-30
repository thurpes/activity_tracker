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
@Schema(description = "Search criteria for activities")
public class ActivitySearchDTO {
    @Schema(description = "Filter by user ID", example = "1")
    private Long userId;
    
    @Schema(description = "Filter by start date (inclusive)", example = "2023-01-01T00:00:00")
    private LocalDateTime startDate;
    
    @Schema(description = "Filter by end date (inclusive)", example = "2023-12-31T23:59:59")
    private LocalDateTime endDate;
    
    @Schema(description = "Filter by action type", example = "LOGIN")
    private String action;
}