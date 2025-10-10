package electronic_health_record.Hospital;

import java.time.LocalDateTime;

public class Hospital {
    private Integer hospitalId;
    private String name;
    private String address;
    private String hotline;
    private String website;
    private LocalDateTime createdAt;

    // --- Constructors ---
    public Hospital() {
    }

    public Hospital(String name, String address, String hotline, String website) {
        this.name = name;
        this.address = address;
        this.hotline = hotline;
        this.website = website;
    }

    // --- Getters and Setters ---
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // --- toString() for debugging ---
    @Override
    public String toString() {
        return "Hospital{" +
                "hospitalId=" + hospitalId +
                ", name='" + name + '\'' +
                ", hotline='" + hotline + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}
