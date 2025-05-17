package com.loanapproval.applicationservice.dto;

public enum ApplicationStatus {
    SUBMITTED("Submitted"),
    UNDER_REVIEW("Under Review"),
    ADDITIONAL_INFO_REQUIRED("Additional Info Required"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    AWAITING_REVIEW("Awaiting Review");

    private final String stringValue;

    ApplicationStatus(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue() {
        return stringValue;
    }
}
