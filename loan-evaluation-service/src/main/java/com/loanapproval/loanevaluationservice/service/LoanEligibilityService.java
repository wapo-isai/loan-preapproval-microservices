package com.loanapproval.loanevaluationservice.service;

import com.loanapproval.loanevaluationservice.persistence.ApplicationEvaluation;
import com.loanapproval.loanevaluationservice.persistence.ApplicationEvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loanapproval.loanevaluationservice.dto.LoanEvaluationRequest;
import com.loanapproval.loanevaluationservice.dto.LoanEvaluationResponse;

@Service
public class LoanEligibilityService {

    @Autowired
    ApplicationEvaluationRepository applicationEvaluationRepository;

    public LoanEvaluationResponse evaluate(LoanEvaluationRequest request) {
        LoanEvaluationResponse response = new LoanEvaluationResponse();

        double ltv = request.getLoanAmount() / request.getHomePrice(); // e.g., 0.80 for 80%
        response.setLtvRatio(ltv);

        // Rule 1: Credit score
        if (request.getCreditScore() < 620) {
            response.setEligible(false);
            response.setReason("Credit score below threshold");
            responseToEvaluationEntity(response);
            return response;
        }

        // Rule 2: DTI
        double monthlyIncome = request.getAnnualIncome() / 12;
        double dti = request.getMonthlyDebt() / monthlyIncome;
        if (dti > 0.43) {
            response.setEligible(false);
            response.setReason("DTI ratio too high");
            responseToEvaluationEntity(response);
            return response;
        }

        // Rule 3: Employment
        if (!"Employed".equalsIgnoreCase(request.getEmploymentStatus())) {
            response.setEligible(false);
            response.setReason("Must be employed to qualify");
            responseToEvaluationEntity(response);
            return response;
        }

        // Rule 4 (optional): LTV threshold
        if (ltv > 0.90) {
            response.setEligible(false);
            response.setReason("LTV ratio too high (max 90%)");
            responseToEvaluationEntity(response);
            return response;
        }

        response.setEligible(true);
        response.setReason("Eligible for loan");
        responseToEvaluationEntity(response);

        return response;
    }

    public void responseToEvaluationEntity(LoanEvaluationResponse response) {
        ApplicationEvaluation applicationEvaluation = new ApplicationEvaluation();
        applicationEvaluation.setEligible(response.isEligible());
        applicationEvaluation.setLtvRatio(response.getLtvRatio());
        applicationEvaluation.setReason(response.getReason());

        applicationEvaluationRepository.save(applicationEvaluation);
    }
}
