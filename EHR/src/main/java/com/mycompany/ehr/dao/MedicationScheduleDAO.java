package com.mycompany.ehr.dao;

import com.mycompany.ehr.model.MedicationSchedule;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;

public class MedicationScheduleDAO implements DAOInterface<MedicationSchedule> {
    
    private MedicationSchedule extractFromResultSet(ResultSet rs) throws SQLException {
        
        return new MedicationSchedule(
                rs.getInt("schedule_id"),
                rs.getInt("detail_id"),
                rs.getTime("scheduled_time").toLocalTime(),
                rs.getString("daily_dosage"),
                rs.getBoolean("is_active"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
    
    @Override
    public int insert(MedicationSchedule t) {
        String sql = "INSERT INTO Medication_Schedule (detail_id, scheduled_time, " +
                     "daily_dosage, is_active) VALUES (?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int newId = 0;

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, t.getDetailId());
            pstmt.setTime(2, Time.valueOf(t.getScheduledTime()));
            pstmt.setString(3, t.getDailyDosage());
            pstmt.setBoolean(4, t.isActive());

            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                newId = rs.getInt(1);
                t.setScheduleId(newId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            JDBCUtil.closeConnection(conn);
        }
        return newId;
    }

    @Override
    public MedicationSchedule selectById(MedicationSchedule t) {
        String sql = "SELECT * FROM Medication_Schedule WHERE schedule_id = ?";
        MedicationSchedule schedule = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, t.getScheduleId());
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                schedule = extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            JDBCUtil.closeConnection(conn);
        }
        return schedule;
    }

    @Override
    public int update(MedicationSchedule t) {
        String sql = "UPDATE Medication_Schedule SET detail_id = ?, scheduled_time = ?, " +
                     "daily_dosage = ?, is_active = ? WHERE schedule_id = ?";
        
        int rowsAffected = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, t.getDetailId());
            pstmt.setTime(2, Time.valueOf(t.getScheduledTime()));
            pstmt.setString(3, t.getDailyDosage());
            pstmt.setBoolean(4, t.isActive());
            pstmt.setInt(5, t.getScheduleId());

            rowsAffected = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            JDBCUtil.closeConnection(conn);
        }
        return rowsAffected;
    }

    @Override
    public int delete(MedicationSchedule t) {
        String sql = "DELETE FROM Medication_Schedule WHERE schedule_id = ?";
        int rowsAffected = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, t.getScheduleId());
            
            rowsAffected = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            JDBCUtil.closeConnection(conn);
        }
        return rowsAffected;
    }

    @Override
    public ArrayList<MedicationSchedule> selectAll() {
        String sql = "SELECT * FROM Medication_Schedule";
        ArrayList<MedicationSchedule> list = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                list.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            JDBCUtil.closeConnection(conn);
        }
        return list;
    }

    @Override
    public ArrayList<MedicationSchedule> selectByCondition(String condition) {
        String sql = "SELECT * FROM Medication_Schedule WHERE " + condition;
        ArrayList<MedicationSchedule> list = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                list.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            JDBCUtil.closeConnection(conn);
        }
        return list;
    }

    @Override
    public boolean exists(String condition) {
        String sql = "SELECT EXISTS(SELECT 1 FROM Medication_Schedule WHERE " + condition + ")";
        boolean exists = false;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                exists = rs.getBoolean(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            JDBCUtil.closeConnection(conn);
        }
        return exists;
    }
}