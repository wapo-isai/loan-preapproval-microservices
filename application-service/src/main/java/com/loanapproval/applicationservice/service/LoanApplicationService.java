package com.loanapproval.applicationservice.service;

import com.loanapproval.applicationservice.client.LoanEvaluationFeignClient;
import com.loanapproval.applicationservice.dto.LoanEvaluationRequest;
import com.loanapproval.applicationservice.dto.LoanEvaluationResponse;
import com.loanapproval.applicationservice.exception.ApplicationNotFoundException;
import com.loanapproval.applicationservice.persistence.LoanApplication;
import com.loanapproval.applicationservice.persistence.LoanApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LoanApplicationService {

    private LoanApplicationRepository repository;
    private final LoanEvaluationFeignClient loanEvaluationFeignClient;

    @Autowired
    public LoanApplicationService(LoanApplicationRepository repository, LoanEvaluationFeignClient loanEvaluationFeignClient) {
        this.repository = repository;
        this.loanEvaluationFeignClient = loanEvaluationFeignClient;
    }

    public LoanApplication submitApplication(LoanApplication app) {
        LoanEvaluationRequest evaluationRequest = new LoanEvaluationRequest();
        evaluationRequest.setHomePrice(app.getHomePrice());
        evaluationRequest.setLoanAmount(app.getLoanAmount());
        evaluationRequest.setAnnualIncome(app.getAnnualIncome());
        evaluationRequest.setMonthlyDebt(app.getMonthlyDebt());
        evaluationRequest.setCreditScore(app.getCreditScore());
        evaluationRequest.setEmploymentStatus(app.getEmploymentStatus());

        LoanEvaluationResponse response = loanEvaluationFeignClient.evaluateLoan(evaluationRequest);

        app.setEligible(response.isEligible());
        app.setEvaluationReason(response.getReason());

        app.setStatus("Submitted");
        app.setSubmittedAt(new Date());
        app.setUpdatedAt(new Date());
        return repository.save(app);
    }

    public List<LoanApplication> getUserApplications(String userId) {
        return repository.findByUserId(userId);
    }

    public LoanApplication getApplicationById(String id) {
        return repository.findById(id).orElseThrow(() -> new ApplicationNotFoundException("Application not found with id: " + id));
    }

    public LoanApplication updateStatus(String id, String newStatus) {
        LoanApplication app = getApplicationById(id);
        app.setStatus(newStatus);
        app.setUpdatedAt(new Date());
        return repository.save(app);
    }

    public List<LoanApplication> getAllApplications() {
        return repository.findAll();
    }
}

