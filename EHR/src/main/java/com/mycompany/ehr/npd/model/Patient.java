package com.mycompany.ehr.npd.model;

public class Patient {
    private Integer patientId;
    private String fullName;
    private String phoneNumber;

    public Patient() {}

    // --- Getters and Setters ---
    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}