package electronic_health_record.Departments;

import java.time.LocalDateTime;

public class Department {
    private Integer departmentId;
    private Integer hospitalId;
    private String name;
    private LocalDateTime createdAt;
    private String locationDetails;

    // --- Constructors ---
    public Department() {
    }

    public Department(Integer hospitalId, String name, String locationDetails) {
        this.hospitalId = hospitalId;
        this.name = name;
        this.locationDetails = locationDetails;
    }

    // --- Getters and Setters ---
    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(String locationDetails) {
        this.locationDetails = locationDetails;
    }

    // --- toString() for debugging ---
    @Override
    public String toString() {
        return "Department{" +
                "departmentId=" + departmentId +
                ", hospitalId=" + hospitalId +
                ", name='" + name + '\'' +
                ", locationDetails='" + locationDetails + '\'' +
                '}';
    }
}
