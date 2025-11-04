package com.mycompany.ehr.npd.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FamilyMembers {

    public enum Gender {
        NAM("Nam"),
        NU("Nữ"),
        KHAC("Khác");

        private final String displayName;
        
        private static final Map<String, Gender> LOOKUP = Arrays.stream(values())
                .collect(Collectors.toMap(Gender::getDisplayName, Function.identity()));

        Gender(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public static Gender fromString(String text) {
            return LOOKUP.getOrDefault(text, KHAC);
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public enum Relationship {
        BAN_THAN("Bản thân"),
        CON("Con"),
        VO_CHONG("Vợ/Chồng"),
        CHA_ME("Cha mẹ"),
        KHAC("Khác");

        private final String displayName;

        private static final Map<String, Relationship> LOOKUP = Arrays.stream(values())
                .collect(Collectors.toMap(Relationship::getDisplayName, Function.identity()));

        Relationship(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public static Relationship fromString(String text) {
            return LOOKUP.getOrDefault(text, KHAC);
        }
        
        @Override
        public String toString() {
            return displayName;
        }
    }

    public enum BloodType {
        A_POSITIVE("A+"), A_NEGATIVE("A-"),
        B_POSITIVE("B+"), B_NEGATIVE("B-"),
        AB_POSITIVE("AB+"), AB_NEGATIVE("AB-"),
        O_POSITIVE("O+"), O_NEGATIVE("O-"),
        UNKNOWN("UNKNOWN");

        private final String displayName;
        
        private static final Map<String, BloodType> LOOKUP = Arrays.stream(values())
                .collect(Collectors.toMap(BloodType::getDisplayName, Function.identity()));

        BloodType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
        
        public static BloodType fromString(String text) {
            return LOOKUP.getOrDefault(text, UNKNOWN);
        }
        
        @Override
        public String toString() {
            return displayName;
        }
    }

    private final int memberId;
    private final int userId;
    private final String name;
    private final LocalDate dob;
    private final Gender gender;
    private final Relationship relationship;
    private final BloodType bloodType;
    private final String insuranceNumber;
    private final String phone;
    private final String avatarPath;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private FamilyMembers(Builder builder) {
        this.memberId = builder.memberId;
        this.userId = builder.userId;
        this.name = builder.name;
        this.dob = builder.dob;
        this.gender = builder.gender;
        this.relationship = builder.relationship;
        this.bloodType = builder.bloodType;
        this.insuranceNumber = builder.insuranceNumber;
        this.phone = builder.phone;
        this.avatarPath = builder.avatarPath;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static class Builder {
        private final int userId;
        private final String name;
        private final LocalDate dob;

        private int memberId = 0;
        private Gender gender = Gender.KHAC;
        private Relationship relationship = Relationship.KHAC;
        private BloodType bloodType = BloodType.UNKNOWN;
        private String insuranceNumber;
        private String phone;
        private String avatarPath;
        private LocalDateTime createdAt = LocalDateTime.now();
        private LocalDateTime updatedAt = LocalDateTime.now();

        public Builder(int userId, String name, LocalDate dob) {
            if (name == null || dob == null) {
                throw new IllegalArgumentException("Tên và ngày sinh là bắt buộc.");
            }
            this.userId = userId;
            this.name = name;
            this.dob = dob;
        }

        public Builder memberId(int memberId) {
            this.memberId = memberId;
            return this;
        }

        public Builder gender(Gender gender) {
            this.gender = gender;
            return this;
        }
        
        public Builder relationship(Relationship relationship) {
            this.relationship = relationship;
            return this;
        }

        public Builder bloodType(BloodType bloodType) {
            this.bloodType = bloodType;
            return this;
        }

        public Builder insuranceNumber(String insuranceNumber) {
            this.insuranceNumber = insuranceNumber;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }
        
        public Builder avatarPath(String avatarPath) {
            this.avatarPath = avatarPath;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public FamilyMembers build() {
            return new FamilyMembers(this);
        }
    }
    
    public int getMemberId() { return memberId; }
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public LocalDate getDob() { return dob; }
    public Gender getGender() { return gender; }
    public Relationship getRelationship() { return relationship; }
    public BloodType getBloodType() { return bloodType; }
    public String getInsuranceNumber() { return insuranceNumber; }
    public String getPhone() { return phone; }
    public String getAvatarPath() { return avatarPath; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    @Override
    public String toString() {
        return "Family_Members{" +
                "memberId=" + memberId +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", dob=" + dob +
                ", gender=" + (gender != null ? gender.getDisplayName() : "null") +
                ", relationship=" + (relationship != null ? relationship.getDisplayName() : "null") +
                ", bloodType=" + (bloodType != null ? bloodType.getDisplayName() : "null") +
                ", insuranceNumber='" + insuranceNumber + '\'' +
                ", phone='" + phone + '\'' +
                ", avatarPath='" + avatarPath + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}