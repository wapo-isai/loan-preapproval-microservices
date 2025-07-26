package com.loanapproval.applicationservice.controller;

import com.loanapproval.applicationservice.dto.AdminNotesUpdateRequest;
import com.loanapproval.applicationservice.dto.NotificationRequest;
import com.loanapproval.applicationservice.dto.StatusUpdateRequest;
import com.loanapproval.applicationservice.persistence.LoanApplication;
import com.loanapproval.applicationservice.service.LoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class LoanApplicationController {

    @Autowired
    private LoanApplicationService service;

    @Autowired
    private StreamBridge streamBridge;

    @PostMapping
    public ResponseEntity<LoanApplication> submit(@RequestBody @Valid LoanApplication app) {
        return ResponseEntity.ok(service.submitApplication(app));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanApplication>> getUserApps(@PathVariable String userId) {
        return ResponseEntity.ok(service.getUserApplications(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanApplication> getApp(@PathVariable String id) {
        return ResponseEntity.ok(service.getApplicationById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<LoanApplication> updateStatus(
            @PathVariable String id, @RequestBody @Valid StatusUpdateRequest request) {
        NotificationRequest notificationRequest = new NotificationRequest();

        streamBridge.send("appEvents-out-0", notificationRequest);

        return ResponseEntity.ok(service.updateStatus(id, request.getStatus()));
    }


    @PatchMapping("/{id}/notes")

    public ResponseEntity<LoanApplication> updateAdminNotes(

            @PathVariable String id, @RequestBody @Valid AdminNotesUpdateRequest request) {

        return ResponseEntity.ok(service.updateAdminNotes(id, request.getNotes()));

    }

    @GetMapping
    public ResponseEntity<List<LoanApplication>> getAllApps() {
        return ResponseEntity.ok(service.getAllApplications());
    }
}

