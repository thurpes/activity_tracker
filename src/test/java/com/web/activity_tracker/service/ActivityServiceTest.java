package com.web.activity_tracker.service;

import com.web.activity_tracker.dto.ActivityDTO;
import com.web.activity_tracker.dto.ActivitySearchDTO;
import com.web.activity_tracker.exception.ResourceNotFoundException;
import com.web.activity_tracker.model.Activity;
import com.web.activity_tracker.model.User;
import com.web.activity_tracker.repository.ActivityRepository;
import com.web.activity_tracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WebSocketService webSocketService;

    @InjectMocks
    private ActivityServiceImpl activityService;

    private User testUser;
    private Activity testActivity;
    private ActivityDTO testActivityDTO;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .build();

        // Setup test activity
        testActivity = Activity.builder()
                .id(1L)
                .user(testUser)
                .action("LOGIN")
                .description("User logged in")
                .ipAddress("127.0.0.1")
                .userAgent("Mozilla/5.0")
                .createdAt(LocalDateTime.now())
                .build();

        // Setup test activity DTO
        testActivityDTO = ActivityDTO.builder()
                .userId(1L)
                .action("LOGIN")
                .description("User logged in")
                .ipAddress("127.0.0.1")
                .userAgent("Mozilla/5.0")
                .build();
    }

    @Test
    void logActivity_ShouldCreateAndReturnActivityDTO() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(activityRepository.save(any(Activity.class))).thenReturn(testActivity);
        doNothing().when(webSocketService).broadcastActivity(any(ActivityDTO.class));

        // Act
        ActivityDTO result = activityService.logActivity(testActivityDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testActivity.getId(), result.getId());
        assertEquals(testUser.getId(), result.getUserId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testActivity.getAction(), result.getAction());
        assertEquals(testActivity.getDescription(), result.getDescription());
        
        // Verify repository and service calls
        verify(userRepository, times(1)).findById(1L);
        verify(activityRepository, times(1)).save(any(Activity.class));
        verify(webSocketService, times(1)).broadcastActivity(any(ActivityDTO.class));
    }

    @Test
    void logActivity_WithNonExistentUser_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            activityService.logActivity(testActivityDTO);
        });
        
        // Verify repository calls
        verify(userRepository, times(1)).findById(1L);
        verify(activityRepository, never()).save(any(Activity.class));
        verify(webSocketService, never()).broadcastActivity(any(ActivityDTO.class));
    }

    @Test
    void getAllActivities_ShouldReturnPageOfActivities() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Activity> activities = Arrays.asList(testActivity);
        Page<Activity> activityPage = new PageImpl<>(activities, pageable, activities.size());
        
        when(activityRepository.findAll(pageable)).thenReturn(activityPage);

        // Act
        Page<ActivityDTO> result = activityService.getAllActivities(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testActivity.getId(), result.getContent().get(0).getId());
        
        // Verify repository calls
        verify(activityRepository, times(1)).findAll(pageable);
    }

    @Test
    void getActivitiesByUserId_ShouldReturnUserActivities() {
        // Arrange
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        List<Activity> activities = Arrays.asList(testActivity);
        Page<Activity> activityPage = new PageImpl<>(activities, pageable, activities.size());
        
        when(activityRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)).thenReturn(activityPage);

        // Act
        Page<ActivityDTO> result = activityService.getActivitiesByUserId(userId, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testActivity.getId(), result.getContent().get(0).getId());
        assertEquals(userId, result.getContent().get(0).getUserId());
        
        // Verify repository calls
        verify(activityRepository, times(1)).findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Test
    void searchActivities_ShouldReturnFilteredActivities() {
        // Arrange
        ActivitySearchDTO searchDTO = ActivitySearchDTO.builder()
                .userId(1L)
                .action("LOGIN")
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now())
                .build();
        
        Pageable pageable = PageRequest.of(0, 10);
        List<Activity> activities = Arrays.asList(testActivity);
        Page<Activity> activityPage = new PageImpl<>(activities, pageable, activities.size());
        
        when(activityRepository.searchActivities(
                searchDTO.getUserId(),
                searchDTO.getStartDate(),
                searchDTO.getEndDate(),
                searchDTO.getAction(),
                pageable)).thenReturn(activityPage);

        // Act
        Page<ActivityDTO> result = activityService.searchActivities(searchDTO, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testActivity.getId(), result.getContent().get(0).getId());
        
        // Verify repository calls
        verify(activityRepository, times(1)).searchActivities(
                searchDTO.getUserId(),
                searchDTO.getStartDate(),
                searchDTO.getEndDate(),
                searchDTO.getAction(),
                pageable);
    }

    @Test
    void getRecentActivities_ShouldReturnTopTenActivities() {
        // Arrange
        List<Activity> activities = Arrays.asList(testActivity);
        when(activityRepository.findTop10ByOrderByCreatedAtDesc()).thenReturn(activities);

        // Act
        List<ActivityDTO> result = activityService.getRecentActivities();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testActivity.getId(), result.get(0).getId());
        
        // Verify repository calls
        verify(activityRepository, times(1)).findTop10ByOrderByCreatedAtDesc();
    }
}
