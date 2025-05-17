package com.loanapproval.userservice.persistence;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
    @Id
    private String id;

    @NotBlank
    @Size(max = 100)
    private String fullName;

    @NotBlank
    @Size(max = 100)
    @Email
    @org.springframework.data.mongodb.core.index.Indexed(unique = true) // Ensure email is unique in MongoDB
    private String email;
    @NotBlank
    @Size(max = 120)
    private String passwordHash;

    @Size(max = 200)
    private String streetAddress;

    @Size(max = 100)
    private String city;

    @Size(max = 50)
    private String state; // e.g., "CA", "NY"

    @Size(max = 20)
    private String zipCode;
    @Size(max = 100)
    private String employmentStatus; // e.g., "Employed", "Self-Employed"

    @Size(max = 500)
    private String employmentDetails; // Free text for employer, position, duration
    private Date createdAt;
    private Date updatedAt;

//    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles = new HashSet<>();

    public User() {}
    
    public User(String fullName, String email, String passwordHash, String employmentStatus) {
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.roles.add("ROLE_USER"); // Default role
    }
    
    // Getters
    public String getId() {
        return id;
    }
    
    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    // Setters (crucial for the UserService to function correctly)
    public void setId(String id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
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
