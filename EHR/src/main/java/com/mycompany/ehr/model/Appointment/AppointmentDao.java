package com.mycompany.ehr.model;

import java.util.List;
import java.util.Optional;

public interface AppointmentDao {

    /**
     * Thêm một cuộc hẹn mới vào CSDL.
     * @param appointment Đối tượng Appointment cần thêm.
     */
    void addAppointment(Appointment appointment);

    /**
     * Lấy thông tin cuộc hẹn bằng ID.
     * @param appointmentId ID của cuộc hẹn.
     * @return Optional chứa Appointment nếu tìm thấy.
     */
    Optional<Appointment> getAppointmentById(int appointmentId);

    /**
     * Lấy tất cả các cuộc hẹn.
     * @return List các đối tượng Appointment.
     */
    List<Appointment> getAllAppointments();

    /**
     * Lấy tất cả các cuộc hẹn của một thành viên (bệnh nhân).
     * @param memberId ID của thành viên.
     * @return List các cuộc hẹn.
     */
    List<Appointment> getAppointmentsByMember(int memberId);

    /**
     * Lấy tất cả các cuộc hẹn của một bác sĩ.
     * @param doctorId ID của bác sĩ.
     * @return List các cuộc hẹn.
     */
    List<Appointment> getAppointmentsByDoctor(int doctorId);

    /**
     * Cập nhật thông tin của một cuộc hẹn.
     * (Thường dùng để cập nhật chẩn đoán, ghi chú, trạng thái...)
     * @param appointment Đối tượng Appointment với thông tin đã cập nhật.
     */
    void updateAppointment(Appointment appointment);

    /**
     * Xóa một cuộc hẹn khỏi CSDL (hoặc có thể chỉ cập nhật trạng thái thành "HUY_BO").
     * @param appointmentId ID của cuộc hẹn cần xóa.
     */
    void deleteAppointment(int appointmentId);
}