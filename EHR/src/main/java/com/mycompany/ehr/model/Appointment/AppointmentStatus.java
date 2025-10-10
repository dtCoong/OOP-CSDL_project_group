/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package electronic_health_record.Appointment;

/**
 *
 * @author Admin
 */
public enum AppointmentStatus {
    DA_DAT("Đã đặt"),
    HOAN_THANH("Hoàn thành"),
    HUY_BO("Hủy bỏ");

    private final String displayName;

    AppointmentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
