/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package electronic_health_record.Appointment;

/**
 *
 * @author Admin
 */
public enum AppointmentType {
    KHAM_TONG_QUAT("Khám tổng quát"),
    TAI_KHAM("Tái khám"),
    KHAN_CAP("Khẩn cấp");

    private final String displayName;

    AppointmentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
