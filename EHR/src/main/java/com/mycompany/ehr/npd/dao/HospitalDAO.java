package com.mycompany.ehr.npd.dao;

import com.mycompany.ehr.util.*;
import com.mycompany.ehr.npd.model.Hospital;
import com.mycompany.ehr.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HospitalDAO implements DAOInterface<Hospital> {

    private static HospitalDAO instance;

    private HospitalDAO() {
    }

    public static HospitalDAO getInstance() {
        if (instance == null) {
            instance = new HospitalDAO();
        }
        return instance;
    }

    private Hospital mapResultSetToHospital(ResultSet rs) throws SQLException {
        Hospital hospital = new Hospital();
        
        hospital.setHospitalId(rs.getInt("hospital_id"));
        hospital.setName(rs.getString("name"));
        hospital.setAddress(rs.getString("address"));
        hospital.setHotline(rs.getString("hotline"));
        hospital.setWebsite(rs.getString("website"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            hospital.setCreatedAt(createdAt.toLocalDateTime());
        }

        return hospital;
    }

    @Override
    public int insert(Hospital t) {
        int result = 0;
        Connection connection = null;
        PreparedStatement statement = null;

        String sql = "INSERT INTO hospitals (name, address, hotline, website, created_at) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try {
            connection = JDBCUtil.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setString(1, t.getName());
            statement.setString(2, t.getAddress());
            statement.setString(3, t.getHotline());
            statement.setString(4, t.getWebsite());
            
            statement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));

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
    public int update(Hospital t) {
        int result = 0;
        Connection connection = null;
        PreparedStatement statement = null;

        String sql = "UPDATE hospitals SET name = ?, address = ?, hotline = ?, website = ? " +
                     "WHERE hospital_id = ?";

        try {
            connection = JDBCUtil.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setString(1, t.getName());
            statement.setString(2, t.getAddress());
            statement.setString(3, t.getHotline());
            statement.setString(4, t.getWebsite());
            
            statement.setInt(5, t.getHospitalId());

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
    public int delete(Hospital t) {
        int result = 0;
        Connection connection = null;
        PreparedStatement statement = null;

        String sql = "DELETE FROM hospitals WHERE hospital_id = ?";

        try {
            connection = JDBCUtil.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, t.getHospitalId());

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
    public Hospital selectById(Hospital t) {
        Hospital hospital = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM hospitals WHERE hospital_id = ?";

        try {
            connection = JDBCUtil.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, t.getHospitalId());

            rs = statement.executeQuery();

            if (rs.next()) {
                hospital = mapResultSetToHospital(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (statement != null) try { statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            JDBCUtil.closeConnection(connection);
        }
        return hospital;
    }

    @Override
    public ArrayList<Hospital> selectAll() throws SQLException {
        ArrayList<Hospital> list = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM hospitals";

        try {
            connection = JDBCUtil.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                Hospital hospital = mapResultSetToHospital(rs);
                list.add(hospital);
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
    public ArrayList<Hospital> selectByCondition(String condition) {
        ArrayList<Hospital> list = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM hospitals WHERE " + condition;

        try {
            connection = JDBCUtil.getConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                Hospital hospital = mapResultSetToHospital(rs);
                list.add(hospital);
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
    public boolean exists(String condition) {
        boolean result = false;
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        String sql = "SELECT 1 FROM hospitals WHERE " + condition + " LIMIT 1";

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
