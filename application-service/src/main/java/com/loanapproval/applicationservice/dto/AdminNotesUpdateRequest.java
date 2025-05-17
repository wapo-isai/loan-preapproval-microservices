package com.loanapproval.applicationservice.dto;

import jakarta.validation.constraints.Size;

public class AdminNotesUpdateRequest {
    @Size(max = 2000) // Example max size for notes
    private String notes;

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
