/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package electronic_health_record.Appointment;

/**
 *
 * @author Admin
 */
public enum PaymentStatus {
    CHUA_THANH_TOAN("Chưa thanh toán"),
    DA_THANH_TOAN("Đã thanh toán"),
    BAO_HIEM("Bảo hiểm chi trả");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
