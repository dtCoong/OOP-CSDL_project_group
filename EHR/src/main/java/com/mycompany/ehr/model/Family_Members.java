package com.mycompany.ehr.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Family_Members {

    public enum Gender {
        NAM("Nam"),
        NU("Nữ"),
        KHAC("Khác");

        private String displayName;

        Gender(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum Relationship {
        BAN_THAN("Bản thân"),
        CON("Con"),
        VO_CHONG("Vợ/Chồng"),
        CHA_ME("Cha mẹ"),
        KHAC("Khác");

        private String displayName;

        Relationship(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum BloodType {
        A_POSITIVE("A+"),
        A_NEGATIVE("A-"),
        B_POSITIVE("B+"),
        B_NEGATIVE("B-"),
        AB_POSITIVE("AB+"),
        AB_NEGATIVE("AB-"),
        O_POSITIVE("O+"),
        O_NEGATIVE("O-"),
        KHAC("NULL");

        private String displayName;

        BloodType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /*
     * CREATE TABLE Family_Members (
    member_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    dob DATE NOT NULL,
    gender ENUM('Nam', 'Nữ', 'Khác') NOT NULL,
    relationship ENUM('Bản thân', 'Con', 'Vợ/Chồng', 'Cha mẹ', 'Khác') NOT NULL,
    blood_type ENUM('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'),
    insurance_number VARCHAR(50),
    phone VARCHAR(15) UNIQUE,
    avatar_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);
     */
    private int member_id;
    private int user_id;
    private String name;
    private LocalDate dob;
    private Gender gender;
    private Relationship relationship;
    private BloodType blood_type;
    private String insurance_number;
    private String phone;
    private String avatar_path;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public Family_Members() {
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
        this.gender = Gender.KHAC;
        this.blood_type = BloodType.KHAC;
    }

    public Family_Members(int user_id, String name, LocalDate dob, String gender, String relationship) {
        this();
        this.user_id = user_id;
        this.name = name;
        this.dob = dob;
        this.gender = Gender.valueOf(gender);
        this.relationship = Relationship.valueOf(relationship);
    }

    public Family_Members(int member_id, int user_id, String name, LocalDate dob,
                        String gender, String relationship, String blood_type,
                        String insurance_number, String phone, String avatar_path,
                        LocalDateTime created_at, LocalDateTime updated_at) {
        this.member_id = member_id;
        this.user_id = user_id;
        this.name = name;
        this.dob = dob;
        this.gender = gender != null ? Gender.valueOf(gender) : null;
        this.relationship = relationship != null ? Relationship.valueOf(relationship) : null;
        this.blood_type = blood_type != null ? BloodType.valueOf(blood_type) : null;
        this.insurance_number = insurance_number;
        this.phone = phone;
        this.avatar_path = avatar_path;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Family_Members(int user_id, String name, LocalDate dob,
                        Gender gender, Relationship relationship, BloodType blood_type,
                        String insurance_number, String phone, String avatar_path) {
        this();
        this.user_id = user_id;
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.relationship = relationship;
        this.blood_type = blood_type;
        this.insurance_number = insurance_number;
        this.phone = phone;
        this.avatar_path = avatar_path;
    }
  
    public int getMemberId() { return member_id; }
    public void setMemberId(int member_id) { this.member_id = member_id; }

    public int getUserId() { return user_id; }
    public void setUserId(int user_id) { this.user_id = user_id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public String getGender() { 
        return gender != null ? gender.getDisplayName() : null; 
    }
    public void setGender(String gender) { 
        this.gender = gender != null ? Gender.valueOf(gender) : null; 
    }

    public String getRelationship() { 
        return relationship != null ? relationship.getDisplayName() : null; 
    }
    public void setRelationship(String relationship) { 
        this.relationship = relationship != null ? Relationship.valueOf(relationship) : null; 
    }

    public String getBloodType() { 
        return blood_type != null ? blood_type.getDisplayName() : null; 
    }
    public void setBloodType(String blood_type) { 
        this.blood_type = blood_type != null ? BloodType.valueOf(blood_type) : null; 
    }

    public String getInsuranceNumber() { return insurance_number; }
    public void setInsuranceNumber(String insurance_number) { this.insurance_number = insurance_number; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAvatarPath() { return avatar_path; }
    public void setAvatarPath(String avatar_path) { this.avatar_path = avatar_path; }

    public LocalDateTime getCreatedAt() { return created_at; }
    public void setCreatedAt(LocalDateTime created_at) { this.created_at = created_at; }

    public LocalDateTime getUpdatedAt() { return updated_at; }
    public void setUpdatedAt(LocalDateTime updated_at) { this.updated_at = updated_at; }
    
    @Override
    public String toString() {
        return "Family_Members{" +
                "member_id=" + member_id +
                ", user_id=" + user_id +
                ", name='" + name + '\'' +
                ", dob=" + dob +
                ", gender=" + (gender != null ? gender.getDisplayName() : "null") +
                ", relationship=" + (relationship != null ? relationship.getDisplayName() : "null") +
                ", blood_type=" + (blood_type != null ? blood_type.getDisplayName() : "null") +
                ", insurance_number='" + insurance_number + '\'' +
                ", phone='" + phone + '\'' +
                ", avatar_path='" + avatar_path + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}