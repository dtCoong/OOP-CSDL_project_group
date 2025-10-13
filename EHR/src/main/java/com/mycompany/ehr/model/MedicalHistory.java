package com.mycompany.ehr.model;

import java.time.LocalDateTime;

public class MedicalHistory {
    public enum Severity {
        NHẸ("Nhẹ"),
        TRUNG_BÌNH("Trung bình"),
        NẶNG("Nặng");

        private String value;

        Severity(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
    public enum Status {
        ĐANG_ĐIỀU_TRỊ("Đang điều trị"),
        ĐÃ_KHỎI("Đã khỏi"),
        KIỂM_SOÁT_ĐƯỢC("Kiểm soát được"),
        TRẦM_TRỌNG("Trầm trọng");

        private String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
    // history_id INT PRIMARY KEY AUTO_INCREMENT,
    // member_id INT NOT NULL,
    // condition_name VARCHAR(200) NOT NULL,
    // diagnosis_date DATE,
    // is_chronic BOOLEAN DEFAULT FALSE,
    // severity ENUM('Nhẹ', 'Trung bình', 'Nặng') DEFAULT 'Nhẹ',
    // status ENUM('Đang điều trị', 'Đã khỏi', 'Kiểm soát được', 'Trầm trọng') DEFAULT 'Đang điều trị',
    // notes TEXT,
    // created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    private int history_id;
    private int member_id;
    private String condition_name;
    private LocalDateTime diagnosis_date;
    private Boolean is_chronic;
    private Severity severity;  // 'Nhẹ', 'Trung bình', 'Nặng'
    private Status status;    // 'Đang điều trị', 'Đã khỏi', 'Kiểm soát được', 'Trầm trọng'
    private String notes;
    private LocalDateTime created_at;

    public MedicalHistory() {
        this.is_chronic = false;
        this.severity = Severity.NHẸ;
        this.status = Status.ĐANG_ĐIỀU_TRỊ;
        this.created_at = LocalDateTime.now();
    }

    public MedicalHistory(int member_id, String condition_name) {
        this();
        this.member_id = member_id;
        this.condition_name = condition_name;
    }
    
    public MedicalHistory(int history_id, int member_id, String condition_name, LocalDateTime diagnosis_date,
            Boolean is_chronic, String severity, String status, String notes, LocalDateTime created_at) {
        this.history_id = history_id;
        this.member_id = member_id;
        this.condition_name = condition_name;
        this.diagnosis_date = diagnosis_date;
        this.is_chronic = is_chronic;
        this.severity = Severity.valueOf(severity);
        this.status = Status.valueOf(status);
        this.notes = notes;
        this.created_at = created_at;
    }

    public int getHistory_id() {
        return history_id;
    }

    public void setHistory_id(int history_id) {
        this.history_id = history_id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getCondition_name() {
        return condition_name;
    }

    public void setCondition_name(String condition_name) {
        this.condition_name = condition_name;
    }

    public LocalDateTime getDiagnosis_date() {
        return diagnosis_date;
    }

    public void setDiagnosis_date(LocalDateTime diagnosis_date) {
        this.diagnosis_date = diagnosis_date;
    }

    public Boolean getIs_chronic() {
        return is_chronic;
    }

    public void setIs_chronic(Boolean is_chronic) {
        this.is_chronic = is_chronic;
    }

    public String getSeverity() {
        return severity.getValue();
    }

    public void setSeverity(String severity) {
        this.severity = Severity.valueOf(severity);
    }

    public String getStatus() {
        return status.getValue();
    }

    public void setStatus(String status) {
        this.status = Status.valueOf(status);
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "MedicalHistory{" +
                "history_id=" + history_id +
                ", member_id=" + member_id +
                ", condition_name='" + condition_name + '\'' +
                ", diagnosis_date=" + diagnosis_date +
                ", is_chronic=" + is_chronic +
                ", severity='" + severity + '\'' +
                ", status='" + status + '\'' +
                ", notes='" + notes + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}