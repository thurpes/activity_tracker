package com.web.activity_tracker.controller;

import com.web.activity_tracker.dto.ActivityDTO;
import com.web.activity_tracker.service.ActivityService;
import com.web.activity_tracker.service.WebSocketService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import java.security.Principal;

@Controller
@Tag(name = "WebSocket", description = "WebSocket message handling")
public class WebSocketController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private WebSocketService webSocketService;

    @MessageMapping("/activity")
    @SendTo("/topic/activities")
    @Hidden // Hide from OpenAPI docs as this is a WebSocket endpoint, not REST
    public ActivityDTO processActivity(ActivityDTO activityDTO, SimpMessageHeaderAccessor headerAccessor) {
        Principal principal = headerAccessor.getUser();
        if (principal != null) {
            // User is authenticated, can log activity
            return activityService.logActivity(activityDTO);
        }
        return null;
    }
}
