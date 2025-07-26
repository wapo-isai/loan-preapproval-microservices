package com.loanapproval.applicationservice.persistence;

import com.loanapproval.applicationservice.dto.TimelineEvent;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "loanApplications")
public class LoanApplication {
    @Id
    private String id;

    private String userId;
    private Double homePrice;
    private Double loanAmount;
    private Double annualIncome;
    private Double monthlyDebt;
    private Integer creditScore;
    private String employmentStatus;
    private String status;
    private Date submittedAt;
    private Date updatedAt;
    private Boolean eligible;
    private String evaluationReason;

    private List<TimelineEvent> timeline = new ArrayList<>(); // Initialize to prevent nulls
    private String adminNotes; // New field for admin notes

    public LoanApplication() {
        this.timeline = new ArrayList<>(); // Ensure timeline is initialized
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getHomePrice() {
        return homePrice;
    }

    public void setHomePrice(Double homePrice) {
        this.homePrice = homePrice;
    }

    public Double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Double getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(Double annualIncome) {
        this.annualIncome = annualIncome;
    }

    public Double getMonthlyDebt() {
        return monthlyDebt;
    }

    public void setMonthlyDebt(Double monthlyDebt) {
        this.monthlyDebt = monthlyDebt;
    }

    public Integer getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(Integer creditScore) {
        this.creditScore = creditScore;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Date submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getEligible() {
        return eligible;
    }

    public void setEligible(Boolean eligible) {
        this.eligible = eligible;
    }

    public String getEvaluationReason() {
        return evaluationReason;
    }

    public void setEvaluationReason(String evaluationReason) {
        this.evaluationReason = evaluationReason;
    }

    public List<TimelineEvent> getTimeline() {
        return timeline;
    }

    public void setTimeline(List<TimelineEvent> timeline) {
        this.timeline = timeline;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }
}
