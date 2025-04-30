package com.web.activity_tracker.controller;

import com.web.activity_tracker.dto.ActivityDTO;
import com.web.activity_tracker.dto.ActivitySearchDTO;
import com.web.activity_tracker.service.ActivityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ActivityController.class)
public class ActivityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivityService activityService;

    @Autowired
    private ObjectMapper objectMapper;

    private ActivityDTO testActivityDTO;
    private List<ActivityDTO> activityList;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        
        testActivityDTO = ActivityDTO.builder()
                .id(1L)
                .userId(1L)
                .username("testuser")
                .action("LOGIN")
                .description("User logged in")
                .ipAddress("127.0.0.1")
                .userAgent("Mozilla/5.0")
                .createdAt(now)
                .build();

        ActivityDTO secondActivity = ActivityDTO.builder()
                .id(2L)
                .userId(1L)
                .username("testuser")
                .action("VIEW_PROFILE")
                .description("User viewed profile")
                .ipAddress("127.0.0.1")
                .userAgent("Mozilla/5.0")
                .createdAt(now.minusHours(1))
                .build();

        activityList = Arrays.asList(testActivityDTO, secondActivity);
    }

    @Test
    @WithMockUser
    void logActivity_ShouldReturnCreatedActivity() throws Exception {
        // Arrange
        when(activityService.logActivity(any(ActivityDTO.class))).thenReturn(testActivityDTO);

        // Act & Assert
        mockMvc.perform(post("/api/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testActivityDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.action", is("LOGIN")));

        // Verify service calls
        verify(activityService, times(1)).logActivity(any(ActivityDTO.class));
    }

    @Test
    @WithMockUser
    void getAllActivities_ShouldReturnPageOfActivities() throws Exception {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ActivityDTO> activityPage = new PageImpl<>(activityList, pageRequest, activityList.size());
        
        when(activityService.getAllActivities(any(PageRequest.class))).thenReturn(activityPage);

        // Act & Assert
        mockMvc.perform(get("/api/activities")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "createdAt"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.totalElements", is(2)));

        // Verify service calls
        verify(activityService, times(1)).getAllActivities(any(PageRequest.class));
    }

    @Test
    @WithMockUser
    void getActivitiesByUserId_ShouldReturnUserActivities() throws Exception {
        // Arrange
        Long userId = 1L;
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ActivityDTO> activityPage = new PageImpl<>(activityList, pageRequest, activityList.size());
        
        when(activityService.getActivitiesByUserId(eq(userId), any(PageRequest.class))).thenReturn(activityPage);

        // Act & Assert
        mockMvc.perform(get("/api/activities/user/{userId}", userId)
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].userId", is(1)))
                .andExpect(jsonPath("$.content[1].userId", is(1)))
                .andExpect(jsonPath("$.totalElements", is(2)));

        // Verify service calls
        verify(activityService, times(1)).getActivitiesByUserId(eq(userId), any(PageRequest.class));
    }

    @Test
    @WithMockUser
    void getRecentActivities_ShouldReturnListOfActivities() throws Exception {
        // Arrange
        when(activityService.getRecentActivities()).thenReturn(activityList);

        // Act & Assert
        mockMvc.perform(get("/api/activities/recent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));

        // Verify service calls
        verify(activityService, times(1)).getRecentActivities();
    }

    @Test
    @WithMockUser
    void searchActivities_ShouldReturnFilteredActivities() throws Exception {
        // Arrange
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ActivityDTO> activityPage = new PageImpl<>(activityList, pageRequest, activityList.size());
        
        when(activityService.searchActivities(any(ActivitySearchDTO.class), any(PageRequest.class)))
                .thenReturn(activityPage);

        // Act & Assert
        mockMvc.perform(get("/api/activities/search")
                .param("userId", "1")
                .param("action", "LOGIN")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements", is(2)));

        // Verify service calls
        verify(activityService, times(1)).searchActivities(any(ActivitySearchDTO.class), any(PageRequest.class));
    }
}