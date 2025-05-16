package com.loanapproval.applicationservice.client;

import com.loanapproval.applicationservice.dto.LoanEvaluationRequest;
import com.loanapproval.applicationservice.dto.LoanEvaluationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "LoanEvaluationFeignClient", url = "${loan.evaluation.url}")
public interface LoanEvaluationFeignClient {
    @PostMapping("/api/evaluation")
    LoanEvaluationResponse evaluateLoan(@RequestBody LoanEvaluationRequest order);
}
