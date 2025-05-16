package com.loanapproval.applicationservice.controller;

import com.loanapproval.applicationservice.dto.StatusUpdateRequest;
import com.loanapproval.applicationservice.persistence.LoanApplication;
import com.loanapproval.applicationservice.service.LoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class LoanApplicationController {

    @Autowired
    private LoanApplicationService service;

    @PostMapping
    public ResponseEntity<LoanApplication> submit(@RequestBody LoanApplication app) {
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
            @PathVariable String id, @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(service.updateStatus(id, request.getStatus()));
    }

    @GetMapping
    public ResponseEntity<List<LoanApplication>> getAllApps() {
        return ResponseEntity.ok(service.getAllApplications());
    }
}

