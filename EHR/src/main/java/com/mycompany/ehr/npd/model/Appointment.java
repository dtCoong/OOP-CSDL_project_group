package com.mycompany.ehr.npd.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Lớp Model ánh xạ tới bảng 'Appointments' trong CSDL
 */
public class Appointment {

    // Enum Khớp với CSDL: ('Khám tổng quát', 'Tái khám', 'Khẩn cấp')
    public enum AppointmentType {
        KHAM_TONG_QUAT("Khám tổng quát"),
        TAI_KHAM("Tái khám"),
        KHAN_CAP("Khẩn cấp");
        
        private final String displayName;
        AppointmentType(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
        
        // HÀM HELPER ĐỂ TÌM ENUM TỪ CHUỖI
        public static AppointmentType fromDisplayName(String text) {
            if (text == null) return null;
            for (AppointmentType b : AppointmentType.values()) {
                if (b.displayName.equalsIgnoreCase(text)) {
                    return b;
                }
            }
            // Mặc định là KHAM_TONG_QUAT nếu không tìm thấy
            return KHAM_TONG_QUAT; 
        }
    }

    // Enum Khớp với CSDL: ('Đã đặt', 'Hoàn thành', 'Hủy bỏ', 'Không đến')
    public enum AppointmentStatus {
        DA_DAT("Đã đặt"),
        HOAN_THANH("Hoàn thành"),
        HUY_BO("Hủy bỏ"),
        KHONG_DEN("Không đến");
        
        private final String displayName;
        AppointmentStatus(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }

        // HÀM HELPER ĐỂ TÌM ENUM TỪ CHUỖI
        public static AppointmentStatus fromDisplayName(String text) {
            if (text == null) return null;
            for (AppointmentStatus b : AppointmentStatus.values()) {
                if (b.displayName.equalsIgnoreCase(text)    ) {
                    return b;
                }
            }
            // Mặc định là DA_DAT nếu không tìm thấy
            return DA_DAT; 
        }
    }

    // Enum Khớp với CSDL: ('Chưa thanh toán', 'Đã thanh toán', 'Bảo hiểm')
    public enum PaymentStatus {
        CHUA_THANH_TOAN("Chưa thanh toán"),
        DA_THANH_TOAN("Đã thanh toán"),
        BAO_HIEM("Bảo hiểm");
        
        private final String displayName;
        PaymentStatus(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }

        // HÀM HELPER ĐỂ TÌM ENUM TỪ CHUỖI
        public static PaymentStatus fromDisplayName(String text) {
            if (text == null) return null;
            for (PaymentStatus b : PaymentStatus.values()) {
                if (b.displayName.equalsIgnoreCase(text)) {
                    return b;
                }
            }
            // Mặc định là CHUA_THANH_TOAN nếu không tìm thấy
            return CHUA_THANH_TOAN;
        }
    }

    // Các trường dữ liệu (Khớp với bảng Appointments) 
    private Integer appointmentId;
    private Integer memberId;
    private Integer doctorId;
    private Integer hospitalId;
    private Integer departmentId;
    private LocalDateTime appointmentDate;
    private AppointmentType type;
    private AppointmentStatus status;
    private String chiefComplaint; 
    private String diagnosis;
    private String treatmentNotes;
    private LocalDate followUpDate;
    private BigDecimal cost;
    private PaymentStatus paymentStatus;
    private String roomNumber;
    private Integer queueNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String appointmentSlot;
    
    public Appointment() {}

    // Getters and Setters 
    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }
    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }
    public Integer getDoctorId() { return doctorId; }
    public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }
    public Integer getHospitalId() { return hospitalId; }
    public void setHospitalId(Integer hospitalId) { this.hospitalId = hospitalId; }
    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }
    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentTime(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }
    public AppointmentType getType() { return type; }
    public void setType(AppointmentType type) { this.type = type; }
    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
    public String getChiefComplaint() { return chiefComplaint; }
    public void setChiefComplaint(String chiefComplaint) { this.chiefComplaint = chiefComplaint; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getTreatmentNotes() { return treatmentNotes; }
    public void setTreatmentNotes(String treatmentNotes) { this.treatmentNotes = treatmentNotes; }
    public LocalDate getFollowUpDate() { return followUpDate; }
    public void setFollowUpDate(LocalDate followUpDate) { this.followUpDate = followUpDate; }
    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public Integer getQueueNumber() { return queueNumber; }
    public void setQueueNumber(Integer queueNumber) { this.queueNumber = queueNumber; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public String getAppointmentSlot() { return appointmentSlot; }
    public void setAppointmentSlot(String appointmentSlot) { this.appointmentSlot = appointmentSlot; }
    
}
