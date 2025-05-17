package com.loanapproval.userservice.dto;

import jakarta.validation.constraints.Size;

public class UpdateUserRequest {
    @Size(max = 100)
    private String fullName;

    @Size(max = 200)
    private String streetAddress;
    @Size(max = 100)
    private String city;
    @Size(max = 50)
    private String state;
    @Size(max = 20)
    private String zipCode;

    @Size(max = 100)
    private String employmentStatus;
    @Size(max = 500)
    private String employmentDetails;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getEmploymentDetails() {
        return employmentDetails;
    }

    public void setEmploymentDetails(String employmentDetails) {
        this.employmentDetails = employmentDetails;
    }
}
