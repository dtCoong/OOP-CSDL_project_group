package com.mycompany.ehr.npd.dao;

import com.mycompany.ehr.npd.model.Doctor;
import com.mycompany.ehr.npd.model.Doctor.DoctorDegree;
import com.mycompany.ehr.util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DoctorDAO implements DAOInterface<Doctor> {

    private static DoctorDAO instance;

    private DoctorDAO() {
    }

    public static DoctorDAO getInstance() {
        if (instance == null) {
            instance = new DoctorDAO();
        }
        return instance;
    }


    private Doctor mapResultSetToDoctor(ResultSet rs) throws SQLException {
        Doctor doc = new Doctor();
        
        doc.setDoctorId(rs.getInt("doctor_id"));
        doc.setDepartmentId(rs.getObject("department_id", Integer.class)); 
        
        String degreeStr = rs.getString("degree");
        if (degreeStr != null) {
            doc.setDegree(DoctorDegree.fromDisplayName(degreeStr)); 
        }

        doc.setExperienceYears(rs.getObject("experience_years", Integer.class));
        doc.setFullName(rs.getString("full_name"));
        doc.setAvatarPath(rs.getString("avatar_path"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            doc.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            doc.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return doc;
    }

    @Override
    public int insert(Doctor t) {
        int result = 0;
        Connection connection = null;
        PreparedStatement statement = null;

        String sql = "INSERT INTO doctor (department_id, degree, experience_years, full_name, " +
                     "avatar_path, email, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            connection = JDBCUtil.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setObject(1, t.getDepartmentId());
            statement.setObject(2, t.getDegree() != null ? t.getDegree().name() : null);
            statement.setObject(3, t.getExperienceYears());
            statement.setString(4, t.getFullName());
            statement.setString(5, t.getAvatarPath());
            statement.setString(6, t.getemail());
            

            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            statement.setTimestamp(7, now);
            statement.setTimestamp(8, now);

            result = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            JDBCUtil.closeConnection(connection);
        }
        return result;
    }

    @Override
    public int update(Doctor t) {
        int result = 0;
        Connection connection = null;
        PreparedStatement statement = null;

        String sql = "UPDATE doctor SET department_id = ?, degree = ?, experience_years = ?, " +
                     "full_name = ?, avatar_path = ?, email = ?, updated_at = ? " +
                     "WHERE doctor_id = ?";

        try {
            connection = JDBCUtil.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setObject(1, t.getDepartmentId());
            statement.setObject(2, t.getDegree() != null ? t.getDegree().name() : null);
            statement.setObject(3, t.getExperienceYears());
            statement.setString(4, t.getFullName());
            statement.setString(5, t.getAvatarPath());
            statement.setString(6, t.getemail());
            
          
            statement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            

            statement.setInt(8, t.getDoctorId());

            result = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            JDBCUtil.closeConnection(connection);
        }
        return result;
    }

    @Override
    public int delete(Doctor t) {
        int result = 0;
        Connection connection = null;
        PreparedStatement statement = null;

        String sql = "DELETE FROM doctor WHERE doctor_id = ?";

        try {
            connection = JDBCUtil.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, t.getDoctorId());

            result = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            JDBCUtil.closeConnection(connection);
        }
        return result;
    }

    @Override
    public Doctor selectById(Doctor t) {
        Doctor doc = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM doctor WHERE doctor_id = ?";

        try {
            connection = JDBCUtil.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, t.getDoctorId());

            rs = statement.executeQuery();

            if (rs.next()) {
                doc = mapResultSetToDoctor(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (statement != null) try { statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            JDBCUtil.closeConnection(connection);
        }
        return doc;
    }

    @Override
    public ArrayList<Doctor> selectAll() throws SQLException {
        ArrayList<Doctor> list = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM Doctors";

        try {
            connection = JDBCUtil.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                Doctor doc = mapResultSetToDoctor(rs);
                list.add(doc);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e; 
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (statement != null) try { statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            JDBCUtil.closeConnection(connection);
        }
        return list;
    }

    @Override
    public ArrayList<Doctor> selectByCondition(String condition) throws SQLException {
        ArrayList<Doctor> list = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM Doctors WHERE " + condition;

        try {
            connection = JDBCUtil.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                Doctor doc = mapResultSetToDoctor(rs);
                list.add(doc);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e; 
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (statement != null) try { statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            JDBCUtil.closeConnection(connection);
        }
        return list;
    }

    @Override
    public boolean exists(String condition) {
        boolean result = false;
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        String sql = "SELECT 1 FROM doctor WHERE " + condition + " LIMIT 1";

        try {
            connection = JDBCUtil.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            if (rs.next()) {
                result = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (statement != null) try { statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            JDBCUtil.closeConnection(connection);
        }
        return result;
    }
}