package com.loanapproval.loanevaluationservice.persistence;

import jakarta.persistence.*;

@Entity
public class ApplicationEvaluation {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(nullable=false, updatable=false)
    private long id;

    private boolean eligible;

    private String reason;

    private Double ltvRatio;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
