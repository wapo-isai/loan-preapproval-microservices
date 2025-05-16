package com.loanapproval.loanevaluationservice.service;

import org.springframework.stereotype.Service;

import com.loanapproval.loanevaluationservice.dto.LoanEvaluationRequest;
import com.loanapproval.loanevaluationservice.dto.LoanEvaluationResponse;

@Service
public class LoanEligibilityService {

    public LoanEvaluationResponse evaluate(LoanEvaluationRequest request) {
        LoanEvaluationResponse response = new LoanEvaluationResponse();

        double ltv = request.getLoanAmount() / request.getHomePrice(); // e.g., 0.80 for 80%
        response.setLtvRatio(ltv);

        // Rule 1: Credit score
        if (request.getCreditScore() < 620) {
            response.setEligible(false);
            response.setReason("Credit score below threshold");
            return response;
        }

        // Rule 2: DTI
        double monthlyIncome = request.getAnnualIncome() / 12;
        double dti = request.getMonthlyDebt() / monthlyIncome;
        if (dti > 0.43) {
            response.setEligible(false);
            response.setReason("DTI ratio too high");
            return response;
        }

        // Rule 3: Employment
        if (!"Employed".equalsIgnoreCase(request.getEmploymentStatus())) {
            response.setEligible(false);
            response.setReason("Must be employed to qualify");
            return response;
        }

        // Rule 4 (optional): LTV threshold
        if (ltv > 0.90) {
            response.setEligible(false);
            response.setReason("LTV ratio too high (max 90%)");
            return response;
        }

        response.setEligible(true);
        response.setReason("Eligible for loan");
        return response;
    }

}
