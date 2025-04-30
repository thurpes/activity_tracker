package com.web.activity_tracker.service;

import com.web.activity_tracker.dto.ActivityDTO;

public interface WebSocketService {
    /**
     * Broadcast an activity to all connected clients
     * @param activityDTO The activity to broadcast
     */
    void broadcastActivity(ActivityDTO activityDTO);
}