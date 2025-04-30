package com.web.activity_tracker.service;

import com.web.activity_tracker.dto.ActivityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Value("${websocket.topic.activities}")
    private String activitiesTopic;

    @Override
    public void broadcastActivity(ActivityDTO activityDTO) {
        messagingTemplate.convertAndSend(activitiesTopic, activityDTO);
    }
}
