package com.loanapproval.applicationservice.service;

import com.loanapproval.applicationservice.client.LoanEvaluationFeignClient;
import com.loanapproval.applicationservice.dto.ApplicationStatus;
import com.loanapproval.applicationservice.dto.LoanEvaluationRequest;
import com.loanapproval.applicationservice.dto.LoanEvaluationResponse;
import com.loanapproval.applicationservice.dto.TimelineEvent;
import com.loanapproval.applicationservice.exception.ApplicationNotFoundException;
import com.loanapproval.applicationservice.persistence.LoanApplication;
import com.loanapproval.applicationservice.persistence.LoanApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LoanApplicationService {

    private final LoanApplicationRepository repository;
    private final LoanEvaluationFeignClient loanEvaluationFeignClient;

    @Autowired
    public LoanApplicationService(LoanApplicationRepository repository, LoanEvaluationFeignClient loanEvaluationFeignClient) {
        this.repository = repository;
        this.loanEvaluationFeignClient = loanEvaluationFeignClient;
    }

    public LoanApplication submitApplication(LoanApplication app) {
        // Ensure timeline is initialized
        if (app.getTimeline() == null) {
            app.setTimeline(new ArrayList<>());
        }
        if (app.getId() == null || "Draft".equalsIgnoreCase(app.getStatus())) {
            LoanEvaluationRequest evaluationRequest = new LoanEvaluationRequest();
            // ... (set properties on evaluationRequest as before) ...
            evaluationRequest.setHomePrice(app.getHomePrice());
            evaluationRequest.setLoanAmount(app.getLoanAmount());
            evaluationRequest.setAnnualIncome(app.getAnnualIncome());
            evaluationRequest.setMonthlyDebt(app.getMonthlyDebt());
            evaluationRequest.setCreditScore(app.getCreditScore());
            evaluationRequest.setEmploymentStatus(app.getEmploymentStatus());

            LoanEvaluationResponse response = loanEvaluationFeignClient.evaluateLoan(evaluationRequest);
            app.setEligible(response.isEligible());
            app.setEvaluationReason(response.getReason());
        }

        String previousStatus = app.getStatus(); // Capture status before changing
        boolean isNewSubmission = app.getSubmittedAt() == null;

        if ("Draft".equalsIgnoreCase(app.getStatus()) || isNewSubmission) {
            app.setStatus(ApplicationStatus.SUBMITTED.getStringValue()); // Use enum string value
            if (isNewSubmission) {
                app.setSubmittedAt(new Date());
            }
        }
        app.setUpdatedAt(new Date());

        // Add timeline event for submission or if status changes from draft to submitted
        if (isNewSubmission || ("Draft".equalsIgnoreCase(previousStatus) && ApplicationStatus.SUBMITTED.getStringValue().equals(app.getStatus()))) {
            TimelineEvent submissionEvent = new TimelineEvent();
            submissionEvent.setDate(app.getUpdatedAt()); // Use consistent update time
            submissionEvent.setStatus(ApplicationStatus.SUBMITTED); // Use enum
            submissionEvent.setDescription("Application submitted by " + (StringUtils.hasText(app.getUserId()) ? "user " + app.getUserId() : "customer") + ".");
            submissionEvent.setActor("customer"); // Or "system" if it's an auto-submission from draft
            submissionEvent.setIcon("fas fa-file-alt"); // Example icon
            app.getTimeline().add(0, submissionEvent); // Add to beginning of list
        }

        return repository.save(app);
    }

    public List<LoanApplication> getUserApplications(String userId) {
        return repository.findByUserId(userId);
    }

    public LoanApplication getApplicationById(String id) {
        return repository.findById(id).orElseThrow(() -> new ApplicationNotFoundException("Application not found with id: " + id));
    }

    public LoanApplication updateStatus(String id, String newStatusString) {
        LoanApplication app = getApplicationById(id);
        String oldStatus = app.getStatus();

        // Validate if newStatusString is a valid ApplicationStatus
        ApplicationStatus newStatusEnum = null;
        for (ApplicationStatus enumVal : ApplicationStatus.values()) {
            if (enumVal.getStringValue().equalsIgnoreCase(newStatusString)) {
                newStatusEnum = enumVal;
                break;
            }
        }

        if (newStatusEnum == null) {
            throw new IllegalArgumentException("Invalid status value provided: " + newStatusString);
        }

        app.setStatus(newStatusEnum.getStringValue());
        app.setUpdatedAt(new Date());

        // Add a timeline event for the status change
        TimelineEvent statusChangeEvent = new TimelineEvent();
        statusChangeEvent.setDate(app.getUpdatedAt());
        statusChangeEvent.setStatus(newStatusEnum); // Use the enum
        statusChangeEvent.setDescription("Status changed from " + oldStatus + " to " + newStatusEnum.getStringValue() + " by admin."); // Assuming admin action
        statusChangeEvent.setActor("admin"); // Or get actor from security context if possible
        // Set icon based on newStatusEnum (you can add a helper method for this)
        statusChangeEvent.setIcon(getIconForStatus(newStatusEnum));

        if (app.getTimeline() == null) { // Defensive check
            app.setTimeline(new ArrayList<>());
        }
        app.getTimeline().add(0, statusChangeEvent); // Add to the beginning (most recent)

        return repository.save(app);
    }

    public LoanApplication updateAdminNotes(String id, String notes) {
        LoanApplication app = getApplicationById(id);
        String oldNotes = app.getAdminNotes();
        app.setAdminNotes(notes);
        app.setUpdatedAt(new Date());

        // Optional: Add a timeline event for notes update
        if (!java.util.Objects.equals(oldNotes, notes)) { // Only add if notes actually changed
            TimelineEvent notesUpdateEvent = new TimelineEvent();
            notesUpdateEvent.setDate(app.getUpdatedAt());
            // You might want a different status for notes or just a description
            notesUpdateEvent.setStatus(ApplicationStatus.valueOf(app.getStatus().toUpperCase().replace(" ", "_"))); // Keep current status for this event type
            notesUpdateEvent.setDescription("Admin notes " + (StringUtils.hasText(oldNotes) ? "updated." : "added."));
            notesUpdateEvent.setActor("admin");
            notesUpdateEvent.setIcon("fas fa-sticky-note"); // Example icon

            if (app.getTimeline() == null) {
                app.setTimeline(new ArrayList<>());
            }
            app.getTimeline().add(0, notesUpdateEvent);
        }
        return repository.save(app);
    }

    public List<LoanApplication> getAllApplications() {
        return repository.findAll();
    }

    // Helper for timeline icon (can be expanded)
    private String getIconForStatus(ApplicationStatus status) {
        switch (status) {
            case SUBMITTED: return "fas fa-file-alt";
            case UNDER_REVIEW:
            case AWAITING_REVIEW: // Grouping similar concepts
                return "fas fa-clock"; // Or fa-user-tie if reviewed by a person
            case ADDITIONAL_INFO_REQUIRED: return "fas fa-exclamation-circle";
            case APPROVED: return "fas fa-check-circle";
            case REJECTED: return "fas fa-times-circle";
            default: return "fas fa-info-circle";
        }
    }
}

