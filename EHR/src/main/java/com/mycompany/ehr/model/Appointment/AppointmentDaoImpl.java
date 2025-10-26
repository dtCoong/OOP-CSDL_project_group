package com.mycompany.ehr.model;

import com.mycompany.ehr.model.Util.DatabaseConnector; // Giả định bạn có lớp này

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppointmentDaoImpl implements AppointmentDao {

    @Override
    public void addAppointment(Appointment app) {
        // Giả định createdAt và updatedAt được CSDL xử lý tự động
        String sql = "INSERT INTO appointments (member_id, doctor_id, hospital_id, department_id, " +
                "appointment_date, type, status, chief_complaint, diagnosis, treatment_notes, " +
                "follow_up_date, cost, payment_status, room_number, queue_number) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, app.getMemberId());
            pstmt.setObject(2, app.getDoctorId());
            pstmt.setObject(3, app.getHospitalId());
            pstmt.setObject(4, app.getDepartmentId());
            pstmt.setTimestamp(5, Timestamp.valueOf(app.getAppointmentDate()));
            pstmt.setString(6, app.getType().name()); // Lưu Enum dưới dạng String
            pstmt.setString(7, app.getStatus().name()); // Lưu Enum dưới dạng String
            pstmt.setString(8, app.getChiefComplaint());
            pstmt.setString(9, app.getDiagnosis());
            pstmt.setString(10, app.getTreatmentNotes());

            // Xử lý LocalDate (nullable)
            if (app.getFollowUpDate() != null) {
                pstmt.setDate(11, Date.valueOf(app.getFollowUpDate()));
            } else {
                pstmt.setNull(11, Types.DATE);
            }

            pstmt.setBigDecimal(12, app.getCost());
            pstmt.setString(13, app.getPaymentStatus().name()); // Lưu Enum dưới dạng String
            pstmt.setString(14, app.getRoomNumber());
            pstmt.setObject(15, app.getQueueNumber()); // Dùng setObject cho Integer (nullable)

            pstmt.executeUpdate();
            System.out.println("Appointment added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Appointment> getAppointmentById(int appointmentId) {
        String sql = "SELECT * FROM appointments WHERE appointment_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, appointmentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToAppointment(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments ORDER BY appointment_date DESC";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    @Override
    public List<Appointment> getAppointmentsByMember(int memberId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE member_id = ? ORDER BY appointment_date DESC";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    @Override
    public List<Appointment> getAppointmentsByDoctor(int doctorId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE doctor_id = ? ORDER BY appointment_date DESC";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    @Override
    public void updateAppointment(Appointment app) {
        String sql = "UPDATE appointments SET " +
                "member_id = ?, doctor_id = ?, hospital_id = ?, department_id = ?, " +
                "appointment_date = ?, type = ?, status = ?, chief_complaint = ?, " +
                "diagnosis = ?, treatment_notes = ?, follow_up_date = ?, " +
                "cost = ?, payment_status = ?, room_number = ?, queue_number = ? " +
                "WHERE appointment_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, app.getMemberId());
            pstmt.setObject(2, app.getDoctorId());
            pstmt.setObject(3, app.getHospitalId());
            pstmt.setObject(4, app.getDepartmentId());
            pstmt.setTimestamp(5, Timestamp.valueOf(app.getAppointmentDate()));
            pstmt.setString(6, app.getType().name());
            pstmt.setString(7, app.getStatus().name());
            pstmt.setString(8, app.getChiefComplaint());
            pstmt.setString(9, app.getDiagnosis());
            pstmt.setString(10, app.getTreatmentNotes());

            if (app.getFollowUpDate() != null) {
                pstmt.setDate(11, Date.valueOf(app.getFollowUpDate()));
            } else {
                pstmt.setNull(11, Types.DATE);
            }

            pstmt.setBigDecimal(12, app.getCost());
            pstmt.setString(13, app.getPaymentStatus().name());
            pstmt.setString(14, app.getRoomNumber());
            pstmt.setObject(15, app.getQueueNumber());

            // Điều kiện WHERE
            pstmt.setInt(16, app.getAppointmentId());

            pstmt.executeUpdate();
            System.out.println("Appointment updated successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAppointment(int appointmentId) {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, appointmentId);
            pstmt.executeUpdate();
            System.out.println("Appointment deleted successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- Phương thức hỗ trợ quan trọng để map ResultSet sang đối tượng Appointment ---
    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment app = new Appointment();
        app.setAppointmentId(rs.getInt("appointment_id"));

        // Dùng getObject để xử lý NULL cho các khóa ngoại Integer
        app.setMemberId(rs.getObject("member_id", Integer.class));
        app.setDoctorId(rs.getObject("doctor_id", Integer.class));
        app.setHospitalId(rs.getObject("hospital_id", Integer.class));
        app.setDepartmentId(rs.getObject("department_id", Integer.class));

        // Xử lý LocalDateTime
        Timestamp tsAppDate = rs.getTimestamp("appointment_date");
        if (tsAppDate != null) {
            app.setAppointmentDate(tsAppDate.toLocalDateTime());
        }

        // Xử lý Enums
        String typeStr = rs.getString("type");
        if (typeStr != null) {
            app.setType(AppointmentType.valueOf(typeStr));
        }

        String statusStr = rs.getString("status");
        if (statusStr != null) {
            app.setStatus(AppointmentStatus.valueOf(statusStr));
        }

        app.setChiefComplaint(rs.getString("chief_complaint"));
        app.setDiagnosis(rs.getString("diagnosis"));
        app.setTreatmentNotes(rs.getString("treatment_notes"));

        // Xử lý LocalDate
        Date sqlDate = rs.getDate("follow_up_date");
        if (sqlDate != null) {
            app.setFollowUpDate(sqlDate.toLocalDate());
        }

        // Xử lý BigDecimal
        app.setCost(rs.getBigDecimal("cost"));

        // Xử lý Enum
        String paymentStatusStr = rs.getString("payment_status");
        if (paymentStatusStr != null) {
            app.setPaymentStatus(PaymentStatus.valueOf(paymentStatusStr));
        }

        app.setRoomNumber(rs.getString("room_number"));

        // Xử lý Integer (nullable)
        app.setQueueNumber(rs.getObject("queue_number", Integer.class));

        // Xử lý Timestamps (created/updated)
        Timestamp tsCreated = rs.getTimestamp("created_at");
        if (tsCreated != null) {
            app.setCreatedAt(tsCreated.toLocalDateTime());
        }

        Timestamp tsUpdated = rs.getTimestamp("updated_at");
        if (tsUpdated != null) {
            app.setUpdatedAt(tsUpdated.toLocalDateTime());
        }

        return app;
    }
}