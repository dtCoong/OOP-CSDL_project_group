package electronic_health_record.Departments;

import electronic_health_record.Util.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepartmentDaoImpl implements DepartmentDao {

    @Override
    public void addDepartment(Department department) {
        String sql = "INSERT INTO departments (hospital_id, name, location_details) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, department.getHospitalId());
            pstmt.setString(2, department.getName());
            pstmt.setString(3, department.getLocationDetails());
            pstmt.executeUpdate();
            System.out.println("Department added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Department> getDepartmentById(int departmentId) {
        String sql = "SELECT * FROM departments WHERE department_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, departmentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToDepartment(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM departments";
        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                departments.add(mapResultSetToDepartment(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
    }

    @Override
    public List<Department> getDepartmentsByHospital(int hospitalId) {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM departments WHERE hospital_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, hospitalId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                departments.add(mapResultSetToDepartment(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
    }

    @Override
    public void updateDepartment(Department department) {
        String sql = "UPDATE departments SET hospital_id = ?, name = ?, location_details = ? WHERE department_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, department.getHospitalId());
            pstmt.setString(2, department.getName());
            pstmt.setString(3, department.getLocationDetails());
            pstmt.setInt(4, department.getDepartmentId());
            pstmt.executeUpdate();
            System.out.println("Department updated successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteDepartment(int departmentId) {
        String sql = "DELETE FROM departments WHERE department_id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, departmentId);
            pstmt.executeUpdate();
            System.out.println("Department deleted successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- Helper method to map ResultSet to Department object ---
    private Department mapResultSetToDepartment(ResultSet rs) throws SQLException {
        Department department = new Department();
        department.setDepartmentId(rs.getInt("department_id"));
        department.setHospitalId(rs.getInt("hospital_id"));
        department.setName(rs.getString("name"));
        department.setLocationDetails(rs.getString("location_details"));
        Timestamp createdAtTimestamp = rs.getTimestamp("created_at");
        if (createdAtTimestamp != null) {
            department.setCreatedAt(createdAtTimestamp.toLocalDateTime());
        }
        return department;
    }
}