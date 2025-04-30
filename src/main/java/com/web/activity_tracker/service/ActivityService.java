package com.web.activity_tracker.service;

import com.web.activity_tracker.dto.ActivityDTO;
import com.web.activity_tracker.dto.ActivitySearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActivityService {

    /**
     * Log a user activity
     * @param activityDTO The activity data
     * @return The created activity
     */
    ActivityDTO logActivity(ActivityDTO activityDTO);

    /**
     * Get all activities with pagination
     * @param pageable Pagination information
     * @return Page of activities
     */
    Page<ActivityDTO> getAllActivities(Pageable pageable);

    /**
     * Get activities by user ID
     * @param userId User ID
     * @param pageable Pagination information
     * @return Page of activities
     */
    Page<ActivityDTO> getActivitiesByUserId(Long userId, Pageable pageable);

    /**
     * Search activities based on criteria
     * @param searchDTO Search criteria
     * @param pageable Pagination information
     * @return Page of activities
     */
    Page<ActivityDTO> searchActivities(ActivitySearchDTO searchDTO, Pageable pageable);

    /**
     * Get most recent activities
     * @return List of recent activities
     */
    List<ActivityDTO> getRecentActivities();
}