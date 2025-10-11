package com.mycompany.ehr.model;
import java.util.*;
import java.time.*;
public class Prescription {
    private int prescriptionId;
    private int memberId;
    private Integer appointmentId;
    private Integer doctorId;
    private LocalDate prescriptionDate;
    private String diagnosis;
    private String notes;
    private double totalCost;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Prescription() {}

    public Prescription(int prescriptionId, int memberId, Integer appointmentId, Integer doctorId,
                         LocalDate prescriptionDate, String diagnosis, String notes,
                         double totalCost, String status, LocalDateTime createdAt,
                         LocalDateTime updatedAt) {
        this.prescriptionId = prescriptionId;
        this.memberId = memberId;
        this.appointmentId = appointmentId;
        this.doctorId = doctorId;
        this.prescriptionDate = prescriptionDate;
        this.diagnosis = diagnosis;
        this.notes = notes;
        this.totalCost = totalCost;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(int prescriptionId) { this.prescriptionId = prescriptionId; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }

    public Integer getDoctorId() { return doctorId; }
    public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }

    public LocalDate getPrescriptionDate() { return prescriptionDate; }
    public void setPrescriptionDate(LocalDate prescriptionDate) { this.prescriptionDate = prescriptionDate; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
