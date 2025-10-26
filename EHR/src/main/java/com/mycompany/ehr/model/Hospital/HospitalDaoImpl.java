package electronic_health_record.Hospital;

import electronic_health_record.Util.DatabaseConnector; // Giả định bạn có lớp này

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HospitalDaoImpl implements HospitalDao {

    @Override
    public void addHospital(Hospital hospital) {
        // Giả định 'created_at' sẽ được CSDL tự động gán (DEFAULT CURRENT_TIMESTAMP)
        String sql = "INSERT INTO hospitals (name, address, hotline, website) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, hospital.getName());
            pstmt.setString(2, hospital.getAddress());
            pstmt.setString(3, hospital.getHotline());
            pstmt.setString(4, hospital.getWebsite());
            pstmt.executeUpdate();
            System.out.println("Hospital added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Hospital> getHospitalById(int hospitalId) {
        String sql = "SELECT * FROM hospitals WHERE hospital_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, hospitalId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToHospital(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Hospital> getAllHospitals() {
        List<Hospital> hospitals = new ArrayList<>();
        String sql = "SELECT * FROM hospitals";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                hospitals.add(mapResultSetToHospital(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hospitals;
    }

    @Override
    public void updateHospital(Hospital hospital) {
        String sql = "UPDATE hospitals SET name = ?, address = ?, hotline = ?, website = ? WHERE hospital_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, hospital.getName());
            pstmt.setString(2, hospital.getAddress());
            pstmt.setString(3, hospital.getHotline());
            pstmt.setString(4, hospital.getWebsite());
            pstmt.setInt(5, hospital.getHospitalId()); // Điều kiện WHERE

            pstmt.executeUpdate();
            System.out.println("Hospital updated successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteHospital(int hospitalId) {
        String sql = "DELETE FROM hospitals WHERE hospital_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, hospitalId);
            pstmt.executeUpdate();
            System.out.println("Hospital deleted successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- Phương thức hỗ trợ để map ResultSet sang đối tượng Hospital ---
    private Hospital mapResultSetToHospital(ResultSet rs) throws SQLException {
        Hospital hospital = new Hospital();
        hospital.setHospitalId(rs.getInt("hospital_id"));
        hospital.setName(rs.getString("name"));
        hospital.setAddress(rs.getString("address"));
        hospital.setHotline(rs.getString("hotline"));
        hospital.setWebsite(rs.getString("website"));

        // Chuyển đổi Timestamp từ CSDL sang LocalDateTime
        Timestamp tsCreated = rs.getTimestamp("created_at");
        if (tsCreated != null) {
            hospital.setCreatedAt(tsCreated.toLocalDateTime());
        }

        return hospital;
    }
}