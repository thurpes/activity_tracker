package com.web.activity_tracker.service;

import com.web.activity_tracker.dto.ActivityDTO;
import com.web.activity_tracker.dto.ActivitySearchDTO;
import com.web.activity_tracker.exception.ResourceNotFoundException;
import com.web.activity_tracker.model.Activity;
import com.web.activity_tracker.model.User;
import com.web.activity_tracker.repository.ActivityRepository;
import com.web.activity_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebSocketService webSocketService;

    @Override
    @Transactional
    @CacheEvict(value = {"activities", "recentActivities"}, allEntries = true)
    public ActivityDTO logActivity(ActivityDTO activityDTO) {
        User user = userRepository.findById(activityDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + activityDTO.getUserId()));

        Activity activity = Activity.builder()
                .user(user)
                .action(activityDTO.getAction())
                .description(activityDTO.getDescription())
                .ipAddress(activityDTO.getIpAddress())
                .userAgent(activityDTO.getUserAgent())
                .build();

        Activity savedActivity = activityRepository.save(activity);
        ActivityDTO savedActivityDTO = mapToDTO(savedActivity);
        
        // Broadcast the new activity via WebSocket
        webSocketService.broadcastActivity(savedActivityDTO);
        
        return savedActivityDTO;
    }

    @Override
    @Cacheable(value = "activities")
    public Page<ActivityDTO> getAllActivities(Pageable pageable) {
        Page<Activity> activities = activityRepository.findAll(pageable);
        return activities.map(this::mapToDTO);
    }

    @Override
    @Cacheable(value = "activities", key = "#userId")
    public Page<ActivityDTO> getActivitiesByUserId(Long userId, Pageable pageable) {
        Page<Activity> activities = activityRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return activities.map(this::mapToDTO);
    }

    @Override
    public Page<ActivityDTO> searchActivities(ActivitySearchDTO searchDTO, Pageable pageable) {
        Page<Activity> activities = activityRepository.searchActivities(
                searchDTO.getUserId(),
                searchDTO.getStartDate(),
                searchDTO.getEndDate(),
                searchDTO.getAction(),
                pageable);
        return activities.map(this::mapToDTO);
    }

    @Override
    @Cacheable(value = "recentActivities")
    public List<ActivityDTO> getRecentActivities() {
        List<Activity> activities = activityRepository.findTop10ByOrderByCreatedAtDesc();
        return activities.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ActivityDTO mapToDTO(Activity activity) {
        return ActivityDTO.builder()
                .id(activity.getId())
                .userId(activity.getUser().getId())
                .username(activity.getUser().getUsername())
                .action(activity.getAction())
                .description(activity.getDescription())
                .ipAddress(activity.getIpAddress())
                .userAgent(activity.getUserAgent())
                .createdAt(activity.getCreatedAt())
                .build();
    }
}