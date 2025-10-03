package com.mycompany.ehr.model;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Family_Members {

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
    phone VARCHAR(15) UNIQUE,  -- chỉ còn một số điện thoại duy nhất
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
    private String gender;
    private String relationship;
    private String blood_type;
    private String insurance_number;
    private String phone;
    private String avatar_path;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;


    public Family_Members(){}
    public Family_Members(int member_id, int user_id, String name, LocalDate dob,
                        String gender, String relationship, String blood_type,
                        String insurance_number, String phone, String avatar_path,
                        LocalDateTime created_at, LocalDateTime updated_at) {
        this.member_id = member_id;
        this.user_id = user_id;
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.relationship = relationship;
        this.blood_type = blood_type;
        this.insurance_number = insurance_number;
        this.phone = phone;
        this.avatar_path = avatar_path;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
  
    public int getMemberId() { return member_id; }
    public void setMemberId(int member_id) { this.member_id = member_id; }

    public int getUserId() { return user_id; }
    public void setUserId(int user_id) { this.user_id = user_id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }

    public String getBloodType() { return blood_type; }
    public void setBloodType(String blood_type) { this.blood_type = blood_type; }

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
}

