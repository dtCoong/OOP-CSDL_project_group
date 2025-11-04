package com.mycompany.ehr.dtc.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Allergies {

    public enum AllergyType {
        THUOC("Thuốc"),
        THUC_AN("Thức ăn"),
        MOI_TRUONG("Môi trường"),
        KHAC("Khác");

        private final String displayName;
        private static final Map<String, AllergyType> LOOKUP = Arrays.stream(values())
                .collect(Collectors.toMap(AllergyType::getDisplayName, Function.identity()));

        AllergyType(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
        public static AllergyType fromString(String text) { return LOOKUP.getOrDefault(text, KHAC); }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public enum AllergySeverity {
        NHE("Nhẹ"),
        TRUNG_BINH("Trung bình"),
        NANG("Nặng"),
        NGUY_HIEM("Nguy hiểm");

        private final String displayName;
        private static final Map<String, AllergySeverity> LOOKUP = Arrays.stream(values())
                .collect(Collectors.toMap(AllergySeverity::getDisplayName, Function.identity()));

        AllergySeverity(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
        public static AllergySeverity fromString(String text) { return LOOKUP.getOrDefault(text, NHE); }

        @Override
        public String toString() {
            return displayName;
        }
    }

    private final int allergyId;
    private final int memberId;
    private final String allergen;
    private final AllergyType allergyType;
    private final AllergySeverity severity;
    private final String symptoms;
    private final LocalDate discoveredDate;
    private final String notes;
    private final LocalDateTime createdAt;

    private Allergies(Builder builder) {
        this.allergyId = builder.allergyId;
        this.memberId = builder.memberId;
        this.allergen = builder.allergen;
        this.allergyType = builder.allergyType;
        this.severity = builder.severity;
        this.symptoms = builder.symptoms;
        this.discoveredDate = builder.discoveredDate;
        this.notes = builder.notes;
        this.createdAt = builder.createdAt;
    }

    public static class Builder {
        private final int memberId;
        private final String allergen;
        private final AllergyType allergyType;

        private int allergyId = 0;
        private AllergySeverity severity = AllergySeverity.NHE;
        private String symptoms;
        private LocalDate discoveredDate;
        private String notes;
        private LocalDateTime createdAt = LocalDateTime.now();

        public Builder(int memberId, String allergen, AllergyType allergyType) {
            if (allergen == null || allergen.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên tác nhân dị ứng là bắt buộc.");
            }
            this.memberId = memberId;
            this.allergen = allergen;
            this.allergyType = allergyType;
        }

        public Builder allergyId(int allergyId) { this.allergyId = allergyId; return this; }
        public Builder severity(AllergySeverity severity) { this.severity = severity; return this; }
        public Builder symptoms(String symptoms) { this.symptoms = symptoms; return this; }
        public Builder discoveredDate(LocalDate discoveredDate) { this.discoveredDate = discoveredDate; return this; }
        public Builder notes(String notes) { this.notes = notes; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Allergies build() { return new Allergies(this); }
    }

    public int getAllergyId() { return allergyId; }
    public int getMemberId() { return memberId; }
    public String getAllergen() { return allergen; }
    public AllergyType getAllergyType() { return allergyType; }
    public AllergySeverity getSeverity() { return severity; }
    public String getSymptoms() { return symptoms; }
    public LocalDate getDiscoveredDate() { return discoveredDate; }
    public String getNotes() { return notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "Allergies{" +
                "allergyId=" + allergyId +
                ", memberId=" + memberId +
                ", allergen='" + allergen + '\'' +
                ", allergyType=" + allergyType.getDisplayName() +
                ", severity=" + severity.getDisplayName() +
                ", symptoms='" + symptoms + '\'' +
                ", discoveredDate=" + discoveredDate +
                ", notes='" + notes + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}