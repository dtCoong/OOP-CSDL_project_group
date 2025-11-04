package com.mycompany.ehr.npd.dao;

import com.mycompany.ehr.util.*;
import com.mycompany.ehr.npd.model.Department;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DepartmentDAO implements DAOInterface<Department> {


    private static DepartmentDAO instance;

    private DepartmentDAO() {
    }

    public static DepartmentDAO getInstance() {
        if (instance == null) {
            instance = new DepartmentDAO();
        }
        return instance;
    }

    private Department mapResultSetToDepartment(ResultSet rs) throws SQLException {
        Department dept = new Department();
        
        dept.setDepartmentId(rs.getInt("department_id"));
        dept.setHospitalId(rs.getObject("hospital_id", Integer.class));
        dept.setName(rs.getString("name"));
        dept.setLocationDetails(rs.getString("location_details"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            dept.setCreatedAt(createdAt.toLocalDateTime());
        }

        return dept;
    }

    @Override
    public int insert(Department t) {
        int result = 0;
        Connection connection = null;
        PreparedStatement statement = null;

        String sql = "INSERT INTO departments (hospital_id, name, location_details, created_at) " +
                     "VALUES (?, ?, ?, ?)";

        try {
            connection = JDBCUtil.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setObject(1, t.getHospitalId()); // Dùng setObject cho Integer
            statement.setString(2, t.getName());
            statement.setString(3, t.getLocationDetails());
            
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

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
    public int update(Department t) {
        int result = 0;
        Connection connection = null;
        PreparedStatement statement = null;

        String sql = "UPDATE departments SET hospital_id = ?, name = ?, location_details = ? " +
                     "WHERE department_id = ?";

        try {
            connection = JDBCUtil.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setObject(1, t.getHospitalId());
            statement.setString(2, t.getName());
            statement.setString(3, t.getLocationDetails());

            statement.setInt(4, t.getDepartmentId());

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
    public int delete(Department t) {
        int result = 0;
        Connection connection = null;
        PreparedStatement statement = null;

        String sql = "DELETE FROM departments WHERE department_id = ?";

        try {
            connection = JDBCUtil.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, t.getDepartmentId());

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
    public Department selectById(Department t) {
        Department dept = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM departments WHERE department_id = ?";

        try {
            connection = JDBCUtil.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, t.getDepartmentId());

            rs = statement.executeQuery();

            if (rs.next()) {
                dept = mapResultSetToDepartment(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (statement != null) try { statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            JDBCUtil.closeConnection(connection);
        }
        return dept;
    }

    @Override
    public ArrayList<Department> selectAll() throws SQLException {
        ArrayList<Department> list = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM departments";

        try {
            connection = JDBCUtil.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                Department dept = mapResultSetToDepartment(rs);
                list.add(dept);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (statement != null) try { statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            JDBCUtil.closeConnection(connection);
        }
        return list;
    }

    @Override
    public ArrayList<Department> selectByCondition(String condition) throws SQLException {
        ArrayList<Department> list = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM departments WHERE " + condition;

        try {
            connection = JDBCUtil.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                Department dept = mapResultSetToDepartment(rs);
                list.add(dept);
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

        String sql = "SELECT 1 FROM departments WHERE " + condition + " LIMIT 1";

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