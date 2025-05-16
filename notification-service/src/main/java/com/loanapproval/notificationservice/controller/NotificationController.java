package com.loanapproval.notificationservice.controller;

import com.loanapproval.notificationservice.dto.NotificationRequest;
import com.loanapproval.notificationservice.persistence.Notification;
import com.loanapproval.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
    Add email support via AWS SES or JavaMailSender

    Trigger notifications from other services using event bus (RabbitMQ, Kafka, etc.)

    Add retry and dead-letter queue for failed sends

    Use an @Async method for non-blocking processing
* */

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Notification> sendNotification(@RequestBody NotificationRequest request) {
        return ResponseEntity.ok(notificationService.send(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable String userId) {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }
}

