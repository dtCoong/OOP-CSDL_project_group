package electronic_health_record.Doctors;

import electronic_health_record.Util.DatabaseConnector; // Giả định bạn có lớp này

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoctorDaoImpl implements DoctorDao {

    @Override
    public void addDoctor(Doctor doctor) {
        // createdAt và updatedAt thường sẽ được CSDL tự động xử lý.
        String sql = "INSERT INTO doctors (department_id, degree, experience_years, full_name, avatar_path, work_phone) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, doctor.getDepartmentId());
            // Lưu Enum dưới dạng String (ví dụ: "BAC_SI")
            pstmt.setString(2, doctor.getDegree().name());
            pstmt.setInt(3, doctor.getExperienceYears());
            pstmt.setString(4, doctor.getFullName());
            pstmt.setString(5, doctor.getAvatarPath());
            pstmt.setString(6, doctor.getWorkPhone());

            pstmt.executeUpdate();
            System.out.println("Doctor added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Doctor> getDoctorById(int doctorId) {
        String sql = "SELECT * FROM doctors WHERE doctor_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToDoctor(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                doctors.add(mapResultSetToDoctor(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    @Override
    public List<Doctor> getDoctorsByDepartment(int departmentId) {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors WHERE department_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, departmentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                doctors.add(mapResultSetToDoctor(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    @Override
    public void updateDoctor(Doctor doctor) {
        // Cột updated_at sẽ tự động cập nhật nếu được cấu hình trong CSDL
        String sql = "UPDATE doctors SET department_id = ?, degree = ?, experience_years = ?, " +
                "full_name = ?, avatar_path = ?, work_phone = ? WHERE doctor_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, doctor.getDepartmentId());
            pstmt.setString(2, doctor.getDegree().name());
            pstmt.setInt(3, doctor.getExperienceYears());
            pstmt.setString(4, doctor.getFullName());
            pstmt.setString(5, doctor.getAvatarPath());
            pstmt.setString(6, doctor.getWorkPhone());
            pstmt.setInt(7, doctor.getDoctorId()); // Điều kiện WHERE

            pstmt.executeUpdate();
            System.out.println("Doctor updated successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteDoctor(int doctorId) {
        String sql = "DELETE FROM doctors WHERE doctor_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, doctorId);
            pstmt.executeUpdate();
            System.out.println("Doctor deleted successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- Phương thức hỗ trợ để map ResultSet sang đối tượng Doctor ---
    private Doctor mapResultSetToDoctor(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setDoctorId(rs.getInt("doctor_id"));
        doctor.setDepartmentId(rs.getInt("department_id"));
        doctor.setExperienceYears(rs.getInt("experience_years"));
        doctor.setFullName(rs.getString("full_name"));
        doctor.setAvatarPath(rs.getString("avatar_path"));
        doctor.setWorkPhone(rs.getString("work_phone"));

        // Chuyển đổi String từ CSDL sang Enum
        String degreeStr = rs.getString("degree");
        if (degreeStr != null) {
            doctor.setDegree(DoctorDegree.valueOf(degreeStr));
        }

        // Chuyển đổi Timestamp từ CSDL sang LocalDateTime
        Timestamp tsCreated = rs.getTimestamp("created_at");
        if (tsCreated != null) {
            doctor.setCreatedAt(tsCreated.toLocalDateTime());
        }

        Timestamp tsUpdated = rs.getTimestamp("updated_at");
        if (tsUpdated != null) {
            doctor.setUpdatedAt(tsUpdated.toLocalDateTime());
        }

        return doctor;
    }
}