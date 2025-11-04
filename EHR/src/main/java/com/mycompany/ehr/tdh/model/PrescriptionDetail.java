package com.mycompany.ehr.tdh.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PrescriptionDetail {

    public enum DetailStatus {
        CHUA_BAT_DAU("Chưa bắt đầu"),
        DANG_DUNG("Đang dùng"),
        DA_HOAN_THANH("Đã hoàn thành"),
        DA_DUNG("Đã dừng");

        private String displayName;

        DetailStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private int detail_id;
    private int prescription_id;
    private int medication_id;
    private String dosage;
    private String frequency;
    private int duration_days;
    private int total_quantity;
    private String instructions;
    private LocalDate start_date;
    private LocalDate end_date;
    private DetailStatus status;
    private LocalDateTime created_at;

    public PrescriptionDetail() {
        this.created_at = LocalDateTime.now();
        this.status = DetailStatus.CHUA_BAT_DAU; 
    }

    public PrescriptionDetail(int prescription_id, int medication_id, String dosage, String frequency,
                              int duration_days, int total_quantity, String instructions,
                              LocalDate start_date, LocalDate end_date, DetailStatus status) {
        this();
        this.prescription_id = prescription_id;
        this.medication_id = medication_id;
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration_days = duration_days;
        this.total_quantity = total_quantity;
        this.instructions = instructions;
        this.start_date = start_date;
        this.end_date = end_date;
        this.status = status;
    }

    public PrescriptionDetail(int detail_id, int prescription_id, int medication_id, String dosage,
                              String frequency, int duration_days, int total_quantity,
                              String instructions, LocalDate start_date, LocalDate end_date,
                              String status, LocalDateTime created_at) {
        this.detail_id = detail_id;
        this.prescription_id = prescription_id;
        this.medication_id = medication_id;
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration_days = duration_days;
        this.total_quantity = total_quantity;
        this.instructions = instructions;
        this.start_date = start_date;
        this.end_date = end_date;
        setStatus(status); 
        this.created_at = created_at;
    }

    public int getDetailId() { return detail_id; }
    public void setDetailId(int detail_id) { this.detail_id = detail_id; }

    public int getPrescriptionId() { return prescription_id; }
    public void setPrescriptionId(int prescription_id) { this.prescription_id = prescription_id; }

    public int getMedicationId() { return medication_id; }
    public void setMedicationId(int medication_id) { this.medication_id = medication_id; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public int getDurationDays() { return duration_days; }
    public void setDurationDays(int duration_days) { this.duration_days = duration_days; }

    public int getTotalQuantity() { return total_quantity; }
    public void setTotalQuantity(int total_quantity) { this.total_quantity = total_quantity; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public LocalDate getStartDate() { return start_date; }
    public void setStartDate(LocalDate start_date) { this.start_date = start_date; }

    public LocalDate getEndDate() { return end_date; }
    public void setEndDate(LocalDate end_date) { this.end_date = end_date; }

    public String getStatus() {
        return status != null ? status.getDisplayName() : null;
    }
    public void setStatus(String status) {
        this.status = status != null ? DetailStatus.valueOf(status) : null;
    }
    public void setStatus(DetailStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() { return created_at; }
    public void setCreatedAt(LocalDateTime created_at) { this.created_at = created_at; }

    @Override
    public String toString() {
        return "PrescriptionDetail{" +
                "detail_id=" + detail_id +
                ", prescription_id=" + prescription_id +
                ", medication_id=" + medication_id +
                ", dosage='" + dosage + '\'' +
                ", frequency='" + frequency + '\'' +
                ", start_date=" + start_date +
                ", end_date=" + end_date +
                ", status=" + (status != null ? status.getDisplayName() : "null") +
                '}';
    }
}