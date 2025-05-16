package com.loanapproval.loanevaluationservice.controller;

import com.loanapproval.loanevaluationservice.dto.LoanEvaluationRequest;
import com.loanapproval.loanevaluationservice.dto.LoanEvaluationResponse;
import com.loanapproval.loanevaluationservice.service.LoanEligibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/evaluation")
public class LoanEvaluationController {

    @Autowired
    private LoanEligibilityService eligibilityService;

    @PostMapping
    public ResponseEntity<LoanEvaluationResponse> evaluateLoan(@RequestBody LoanEvaluationRequest request) {
        LoanEvaluationResponse response = eligibilityService.evaluate(request);
        return ResponseEntity.ok(response);
    }
}

