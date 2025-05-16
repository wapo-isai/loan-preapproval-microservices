package com.loanapproval.notificationservice.service;

import com.loanapproval.notificationservice.dto.NotificationRequest;
import com.loanapproval.notificationservice.persistence.Notification;
import com.loanapproval.notificationservice.persistence.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification send(NotificationRequest request) {
        // Mock sending logic
        System.out.println("[NOTIFICATION] (" + request.getType() + "): " + request.getMessage());

        Notification notification = new Notification();
        notification.setUserId(request.getUserId());
        notification.setType(request.getType());
        notification.setMessage(request.getMessage());
        notification.setStatus("Sent");
        notification.setSentAt(new Date());

        return notificationRepository.save(notification);
    }

    public List<Notification> getUserNotifications(String userId) {
        return notificationRepository.findByUserId(userId);
    }
}

