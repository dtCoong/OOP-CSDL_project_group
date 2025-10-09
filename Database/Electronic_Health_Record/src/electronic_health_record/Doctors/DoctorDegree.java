package electronic_health_record.Doctors;

public enum DoctorDegree {
    BAC_SI("Bác sĩ"),
    THAC_SI("Thạc sĩ"),
    TIEN_SI("Tiến sĩ"),
    GIAO_SU("Giáo sư");

    private final String displayName;

    DoctorDegree(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
