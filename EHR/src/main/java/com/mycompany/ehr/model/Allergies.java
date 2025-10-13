package com.mycompany.ehr.model;

import java.time.LocalDateTime;

public class Allergies {
    public enum AllergyType {
        THUOC("Thuốc"),
        THUC_AN("Thức ăn"),
        MOI_TRUONG("Môi trường"),
        KHAC("Khác");

        private String displayName;

        AllergyType(String displayName) {
            this.displayName = displayName;
        }

        public String getValue() {
            return displayName;
        }
    }
    public enum AllergySeverity {
        NHE("Nhẹ"),
        TRUNG_BINH("Trung bình"),
        NANG("Nặng"),
        NGUY_HIEM("Nguy hiểm");

        private String displayName;

        AllergySeverity(String displayName) {
            this.displayName = displayName;
        }

        public String getValue() {
            return displayName;
        }
    }
    // allergy_id INT PRIMARY KEY AUTO_INCREMENT,
    // member_id INT NOT NULL,
    // allergen VARCHAR(200) NOT NULL,
    // allergy_type ENUM('Thuốc', 'Thức ăn', 'Môi trường', 'Khác') NOT NULL,
    // severity ENUM('Nhẹ', 'Trung bình', 'Nặng', 'Nguy hiểm') DEFAULT 'Nhẹ',
    // symptoms TEXT,
    // discovered_date DATE,
    // notes TEXT,
    // created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    private int allergy_id;
    private int member_id;
    private String allergen;
    private AllergyType allergy_type;  // 'Thuốc', 'Thức ăn', 'Môi trường', 'Khác'
    private AllergySeverity severity;      // 'Nhẹ', 'Trung bình', 'Nặng', 'Nguy hiểm'
    private String symptoms;
    private LocalDateTime discovered_date;
    private String notes;
    private LocalDateTime created_at;

    public Allergies() {
        this.allergy_type = AllergyType.KHAC ;
        this.severity = AllergySeverity.NHE;
        this.created_at = LocalDateTime.now();
    }

    public Allergies(int member_id, String allergen, String allergy_type) {
        this();
        this.member_id = member_id;
        this.allergen = allergen;
        this.allergy_type = AllergyType.valueOf(allergy_type);
    }
    
    public Allergies(int allergy_id, int member_id, String allergen, String allergy_type, String severity,
            String symptoms, LocalDateTime discovered_date, String notes, LocalDateTime created_at) {
        this.allergy_id = allergy_id;
        this.member_id = member_id;
        this.allergen = allergen;
        this.allergy_type = AllergyType.valueOf(allergy_type);
        this.severity = AllergySeverity.valueOf(severity);
        this.symptoms = symptoms;
        this.discovered_date = discovered_date;
        this.notes = notes;
        this.created_at = created_at;
    }

    public int getAllergy_id() {
        return allergy_id;
    }
    public int getMember_id() {
        return member_id;
    }

    public String getAllergen() {
        return allergen;
    }
    public void setAllergen(String allergen) {
        this.allergen = allergen;
    }

    public String getAllergy_type() {
        return allergy_type.getValue();
    }
    public void setAllergy_type(String allergy_type) {
        this.allergy_type = AllergyType.valueOf(allergy_type);
    }

    public String getSeverity() {
        return severity.getValue();
    }
    public void setSeverity(String severity) {
        this.severity = AllergySeverity.valueOf(severity);
    }

    public String getSymptoms() {
        return symptoms;
    }
    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public LocalDateTime getDiscovered_date() {
        return discovered_date;
    }
    public void setDiscovered_date(LocalDateTime discovered_date) {
        this.discovered_date = discovered_date;
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
        return "Allergies{" +
                "allergy_id=" + allergy_id +
                ", member_id=" + member_id +
                ", allergen='" + allergen + '\'' +
                ", allergy_type='" + allergy_type + '\'' +
                ", severity='" + severity + '\'' +
                ", symptoms='" + symptoms + '\'' +
                ", discovered_date=" + discovered_date +
                ", notes='" + notes + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}