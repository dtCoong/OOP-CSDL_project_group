package com.mycompany.ehr.dao;

import com.mycompany.ehr.model.Medication;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MedicationDAO implements DAOInterface<Medication> {

    private Medication.Unit getUnitFromDisplayName(String displayName) {
        if (displayName == null) return null;
        for (Medication.Unit u : Medication.Unit.values()) {
            if (u.getDisplayName().equals(displayName)) {
                return u;
            }
        }
        return null;
    }

    private Medication extractFromResultSet(ResultSet rs) throws SQLException {
      
        return new Medication(
                rs.getInt("medication_id"),
                rs.getString("name"),
                rs.getString("generic_name"),
                rs.getString("description"),
                rs.getString("side_effects"),
                rs.getString("contraindications"),
                rs.getString("interactions"),
                rs.getString("manufacturer"),
                getUnitFromDisplayName(rs.getString("unit")).name(), 
                rs.getBoolean("requires_prescription"),
                rs.getString("barcode"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }

    @Override
    public int insert(Medication t) {
        return 0;
    }

    @Override
    public Medication selectById(Medication t) {
        String sql = "SELECT * FROM Medications WHERE medication_id = ?";
        Medication medication = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, t.getMedicationId());
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                medication = extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            JDBCUtil.closeConnection(conn);
        }
        return medication;
    }

    @Override
    public int update(Medication t) {
        return 0;
    }

    @Override
    public int delete(Medication t) {
        return 0;
    }

    @Override
    public ArrayList<Medication> selectAll() {
        String sql = "SELECT * FROM Medications";
        ArrayList<Medication> list = new ArrayList<>();
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
    public ArrayList<Medication> selectByCondition(String condition) {
        String sql = "SELECT * FROM Medications WHERE " + condition;
        ArrayList<Medication> list = new ArrayList<>();
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