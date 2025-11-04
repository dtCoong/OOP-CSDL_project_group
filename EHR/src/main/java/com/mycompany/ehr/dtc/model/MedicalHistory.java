package com.mycompany.ehr.dtc.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MedicalHistory {
    public enum Severity {
        NHẸ("Nhẹ"),
        TRUNG_BÌNH("Trung bình"),
        NẶNG("Nặng");

        private final String displayName;
        private static final Map<String, Severity> LOOKUP = Arrays.stream(values())
                .collect(Collectors.toMap(Severity::getDisplayName, Function.identity()));

        Severity(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
        public static Severity fromString(String text) { return LOOKUP.getOrDefault(text, NHẸ); }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public enum Status {
        ĐANG_ĐIỀU_TRỊ("Đang điều trị"),
        ĐÃ_KHỎI("Đã khỏi"),
        KIỂM_SOÁT_ĐƯỢC("Kiểm soát được"),
        TRẦM_TRỌNG("Trầm trọng");

        private final String displayName;
        private static final Map<String, Status> LOOKUP = Arrays.stream(values())
                .collect(Collectors.toMap(Status::getDisplayName, Function.identity()));

        Status(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
        public static Status fromString(String text) { return LOOKUP.getOrDefault(text, ĐANG_ĐIỀU_TRỊ); }

        @Override
        public String toString() {
            return displayName;
        }
    }

    private final int historyId;
    private final int memberId;
    private final String conditionName;
    private final LocalDate diagnosisDate;
    private final boolean isChronic;
    private final Severity severity;
    private final Status status;
    private final String notes;
    private final LocalDate hospitalAdmissionDate;
    private final LocalDate hospitalDischargeDate;
    private final String hospitalName;
    private final String hospitalAddress;
    private final LocalDateTime createdAt;

    private MedicalHistory(Builder builder) {
        this.historyId = builder.historyId;
        this.memberId = builder.memberId;
        this.conditionName = builder.conditionName;
        this.diagnosisDate = builder.diagnosisDate;
        this.isChronic = builder.isChronic;
        this.severity = builder.severity;
        this.status = builder.status;
        this.notes = builder.notes;
        this.hospitalAdmissionDate = builder.hospitalAdmissionDate;
        this.hospitalDischargeDate = builder.hospitalDischargeDate;
        this.hospitalName = builder.hospitalName;
        this.hospitalAddress = builder.hospitalAddress;
        this.createdAt = builder.createdAt;
    }

    public static class Builder {
        private final int memberId;
        private final String conditionName;
        private int historyId = 0;
        private LocalDate diagnosisDate;
        private boolean isChronic = false;
        private Severity severity = Severity.NHẸ;
        private Status status = Status.ĐANG_ĐIỀU_TRỊ;
        private String notes;
        private LocalDate hospitalAdmissionDate;
        private LocalDate hospitalDischargeDate;
        private String hospitalName;
        private String hospitalAddress;
        private LocalDateTime createdAt = LocalDateTime.now();

        public Builder(int memberId, String conditionName) {
            if (conditionName == null || conditionName.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên bệnh/tình trạng là bắt buộc.");
            }
            this.memberId = memberId;
            this.conditionName = conditionName;
        }

        public Builder historyId(int historyId) { this.historyId = historyId; return this; }
        public Builder diagnosisDate(LocalDate diagnosisDate) { this.diagnosisDate = diagnosisDate; return this; }
        public Builder isChronic(boolean isChronic) { this.isChronic = isChronic; return this; }
        public Builder severity(Severity severity) { this.severity = severity; return this; }
        public Builder status(Status status) { this.status = status; return this; }
        public Builder notes(String notes) { this.notes = notes; return this; }
        public Builder hospitalAdmissionDate(LocalDate date) { this.hospitalAdmissionDate = date; return this; }
        public Builder hospitalDischargeDate(LocalDate date) { this.hospitalDischargeDate = date; return this; }
        public Builder hospitalName(String name) { this.hospitalName = name; return this; }
        public Builder hospitalAddress(String address) { this.hospitalAddress = address; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public MedicalHistory build() { return new MedicalHistory(this); }
    }

    public int getHistoryId() { return historyId; }
    public int getMemberId() { return memberId; }
    public String getConditionName() { return conditionName; }
    public LocalDate getDiagnosisDate() { return diagnosisDate; }
    public boolean isChronic() { return isChronic; }
    public Severity getSeverity() { return severity; }
    public Status getStatus() { return status; }
    public String getNotes() { return notes; }
    public LocalDate getHospitalAdmissionDate() { return hospitalAdmissionDate; }
    public LocalDate getHospitalDischargeDate() { return hospitalDischargeDate; }
    public String getHospitalName() { return hospitalName; }
    public String getHospitalAddress() { return hospitalAddress; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "MedicalHistory{" +
                "historyId=" + historyId +
                ", memberId=" + memberId +
                ", conditionName='" + conditionName + '\'' +
                ", diagnosisDate=" + diagnosisDate +
                ", isChronic=" + isChronic +
                ", severity=" + severity.getDisplayName() +
                ", status=" + status.getDisplayName() +
                ", notes='" + notes + '\'' +
                ", hospitalAdmissionDate=" + hospitalAdmissionDate +
                ", hospitalDischargeDate=" + hospitalDischargeDate +
                ", hospitalName='" + hospitalName + '\'' +
                ", hospitalAddress='" + hospitalAddress + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}