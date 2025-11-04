package com.mycompany.ehr.tdh.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Prescription {

    public enum PrescriptionStatus {
        DANG_SU_DUNG("Đang sử dụng"),
        DA_HOAN_THANH("Đã hoàn thành"),
        DA_DUNG("Đã dừng"),
        HET_HAN("Hết hạn");

        private String displayName;

        PrescriptionStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private int prescription_id;
    private int member_id;
    private Integer appointment_id; 
    private Integer doctor_id; 
    private LocalDate prescription_date;
    private String diagnosis;
    private String notes;
    private BigDecimal total_cost; 
    private PrescriptionStatus status;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public Prescription() {
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
        this.status = PrescriptionStatus.DANG_SU_DUNG; 
    }

    public Prescription(int member_id, Integer appointment_id, Integer doctor_id,
                        LocalDate prescription_date, String diagnosis, String notes,
                        BigDecimal total_cost, PrescriptionStatus status) {
        this(); 
        this.member_id = member_id;
        this.appointment_id = appointment_id;
        this.doctor_id = doctor_id;
        this.prescription_date = prescription_date;
        this.diagnosis = diagnosis;
        this.notes = notes;
        this.total_cost = total_cost;
        this.status = status;
    }

    public Prescription(int prescription_id, int member_id, Integer appointment_id, Integer doctor_id,
                        LocalDate prescription_date, String diagnosis, String notes,
                        BigDecimal total_cost, String status, LocalDateTime created_at,
                        LocalDateTime updated_at) {
        this.prescription_id = prescription_id;
        this.member_id = member_id;
        this.appointment_id = appointment_id;
        this.doctor_id = doctor_id;
        this.prescription_date = prescription_date;
        this.diagnosis = diagnosis;
        this.notes = notes;
        this.total_cost = total_cost;
        setStatus(status); 
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getPrescriptionId() { return prescription_id; }
    public void setPrescriptionId(int prescription_id) { this.prescription_id = prescription_id; }

    public int getMemberId() { return member_id; }
    public void setMemberId(int member_id) { this.member_id = member_id; }

    public Integer getAppointmentId() { return appointment_id; }
    public void setAppointmentId(Integer appointment_id) { this.appointment_id = appointment_id; }

    public Integer getDoctorId() { return doctor_id; }
    public void setDoctorId(Integer doctor_id) { this.doctor_id = doctor_id; }

    public LocalDate getPrescriptionDate() { return prescription_date; }
    public void setPrescriptionDate(LocalDate prescription_date) { this.prescription_date = prescription_date; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public BigDecimal getTotalCost() { return total_cost; }
    public void setTotalCost(BigDecimal total_cost) { this.total_cost = total_cost; }

    public String getStatus() {
        return status != null ? status.getDisplayName() : null;
    }
    public void setStatus(String status) {
        this.status = status != null ? PrescriptionStatus.valueOf(status) : null;
    }
    // Overloaded setter for direct enum use
    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() { return created_at; }
    public void setCreatedAt(LocalDateTime created_at) { this.created_at = created_at; }

    public LocalDateTime getUpdatedAt() { return updated_at; }
    public void setUpdatedAt(LocalDateTime updated_at) { this.updated_at = updated_at; }

    @Override
    public String toString() {
        return "Prescription{" +
                "prescription_id=" + prescription_id +
                ", member_id=" + member_id +
                ", prescription_date=" + prescription_date +
                ", diagnosis='" + diagnosis + '\'' +
                ", status=" + (status != null ? status.getDisplayName() : "null") +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}