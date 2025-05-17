package com.loanapproval.applicationservice.dto;

public class LoanEvaluationResponse {
    private boolean eligible;
    private String reason;
    private Double ltvRatio;

    public LoanEvaluationResponse() {
    }

    public LoanEvaluationResponse(boolean eligible, String reason) {
        this.eligible = eligible;
        this.reason = reason;
    }

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

    public Double getLtvRatio() {
        return ltvRatio;
    }

    public void setLtvRatio(Double ltvRatio) {
        this.ltvRatio = ltvRatio;
    }
}

