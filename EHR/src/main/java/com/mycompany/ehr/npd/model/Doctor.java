package com.mycompany.ehr.npd.model;

import java.time.LocalDateTime;


public class Doctor {
    public enum DoctorDegree {
        BAC_SI("Bác sỹ"),
        THAC_SI("Thạc sỹ"),
        TIEN_SI("Tiến sỹ"),
        GIAO_SU("Giáo sư");
        
        private final String displayName;

        DoctorDegree(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
        
        public static DoctorDegree fromDisplayName(String displayName) {
            if (displayName == null) return null;
            for (DoctorDegree degree : DoctorDegree.values()) {
                if (degree.getDisplayName().equalsIgnoreCase(displayName)) {
                    return degree;
                }
            }
            return null; 
        }
    }
    
    private Integer doctorId;
    private Integer departmentId;
    private DoctorDegree degree;
    private Integer experienceYears;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String fullName;
    private String avatarPath;
    private String email;

    // Constructors 
    public Doctor() {
    }

    // Getters and Setters
    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public DoctorDegree getDegree() {
        return degree;
    }

    public void setDegree(DoctorDegree degree) {
        this.degree = degree;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    // toString() 
    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId=" + doctorId +
                ", fullName='" + fullName + '\'' +
                ", degree=" + degree +
                ", workPhone='" + email + '\'' +
                '}';
    }
}
