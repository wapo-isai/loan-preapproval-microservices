package com.loanapproval.applicationservice.dto;

public class LoanEvaluationResponse {
    private boolean eligible;
    private String reason;

    public boolean isEligible() {
        return eligible;
    }

    public void setEligible(boolean eligible) {
        this.eligible = eligible;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

