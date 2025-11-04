package com.mycompany.ehr.tdh.dao;

import com.mycompany.ehr.tdh.model.MedicationSchedule;
import com.mycompany.ehr.util.*;
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
        return 0;
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
        return 0;
    }

    @Override
    public int delete(MedicationSchedule t) {
        return 0;
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
        return false;
    }
}