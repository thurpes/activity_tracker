package com.web.activity_tracker.controller;

import com.web.activity_tracker.dto.ActivityDTO;
import com.web.activity_tracker.dto.ActivitySearchDTO;
import com.web.activity_tracker.service.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Activity", description = "Activity Management API")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @PostMapping
    @Operation(summary = "Log a new activity", description = "Creates a new user activity record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Activity created successfully",
                    content = @Content(schema = @Schema(implementation = ActivityDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<ActivityDTO> logActivity(
            @Parameter(description = "Activity data", required = true)
            @Valid @RequestBody ActivityDTO activityDTO, 
            HttpServletRequest request) {
        // Enhance the activity with request info
        activityDTO.setIpAddress(request.getRemoteAddr());
        activityDTO.setUserAgent(request.getHeader("User-Agent"));
        
        ActivityDTO createdActivity = activityService.logActivity(activityDTO);
        return ResponseEntity.ok(createdActivity);
    }

    @GetMapping
    @Operation(summary = "Get all activities", description = "Returns a paginated list of all activities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Page<ActivityDTO>> getAllActivities(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sort) {
        
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sort));
        Page<ActivityDTO> activities = activityService.getAllActivities(pageRequest);
        
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get activities by user ID", description = "Returns a paginated list of activities for a specific user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Page<ActivityDTO>> getActivitiesByUserId(
            @Parameter(description = "User ID", required = true) @PathVariable Long userId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ActivityDTO> activities = activityService.getActivitiesByUserId(userId, pageRequest);
        
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/recent")
    @Operation(summary = "Get recent activities", description = "Returns a list of most recent activities")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<ActivityDTO>> getRecentActivities() {
        List<ActivityDTO> activities = activityService.getRecentActivities();
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/search")
    @Operation(summary = "Search activities", description = "Returns a paginated list of activities that match the search criteria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Page<ActivityDTO>> searchActivities(
            @Parameter(description = "Search parameters") @ModelAttribute ActivitySearchDTO searchDTO,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ActivityDTO> activities = activityService.searchActivities(searchDTO, pageRequest);
        
        return ResponseEntity.ok(activities);
    }
}