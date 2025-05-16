package com.loanapproval.applicationservice.dto;

public class LoanEvaluationRequest {
    private Double homePrice;
    private Double loanAmount;
    private Double annualIncome;
    private Double monthlyDebt;
    private Integer creditScore;
    private String employmentStatus;

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
}

