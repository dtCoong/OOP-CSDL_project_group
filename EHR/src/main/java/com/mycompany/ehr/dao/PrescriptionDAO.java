package com.mycompany.ehr.dao;

import com.mycompany.ehr.model.Prescription;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

public class PrescriptionDAO implements DAOInterface<Prescription> {

    private Prescription.PrescriptionStatus getStatusFromDisplayName(String displayName) {
        if (displayName == null) return null;
        for (Prescription.PrescriptionStatus s : Prescription.PrescriptionStatus.values()) {
            if (s.getDisplayName().equals(displayName)) {
                return s;
            }
        }
        return null;
    }

    private Prescription extractFromResultSet(ResultSet rs) throws SQLException {
        
        return new Prescription(
                rs.getInt("prescription_id"),
                rs.getInt("member_id"),
                rs.getObject("appointment_id", Integer.class),
                rs.getObject("doctor_id", Integer.class),
                rs.getDate("prescription_date").toLocalDate(),
                rs.getString("diagnosis"),
                rs.getString("notes"),
                rs.getBigDecimal("total_cost"),
                getStatusFromDisplayName(rs.getString("status")).name(),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime()
        );
    }
    
    @Override
    public int insert(Prescription t) {
        String sql = "INSERT INTO Prescriptions (member_id, appointment_id, doctor_id, " +
                     "prescription_date, diagnosis, notes, total_cost, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int newId = 0;

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, t.getMemberId());
            pstmt.setObject(2, t.getAppointmentId());
            pstmt.setObject(3, t.getDoctorId());
            pstmt.setDate(4, Date.valueOf(t.getPrescriptionDate()));
            pstmt.setString(5, t.getDiagnosis());
            pstmt.setString(6, t.getNotes());
            pstmt.setBigDecimal(7, t.getTotalCost());
            pstmt.setString(8, t.getStatus());

            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                newId = rs.getInt(1);
                t.setPrescriptionId(newId);
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
    public Prescription selectById(Prescription t) {
        String sql = "SELECT * FROM Prescriptions WHERE prescription_id = ?";
        Prescription prescription = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, t.getPrescriptionId());
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                prescription = extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            JDBCUtil.closeConnection(conn);
        }
        return prescription;
    }

    @Override
    public int update(Prescription t) {
        String sql = "UPDATE Prescriptions SET member_id = ?, appointment_id = ?, doctor_id = ?, " +
                     "prescription_date = ?, diagnosis = ?, notes = ?, total_cost = ?, status = ?, " +
                     "updated_at = ? WHERE prescription_id = ?";
        
        int rowsAffected = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, t.getMemberId());
            pstmt.setObject(2, t.getAppointmentId());
            pstmt.setObject(3, t.getDoctorId());
            pstmt.setDate(4, Date.valueOf(t.getPrescriptionDate()));
            pstmt.setString(5, t.getDiagnosis());
            pstmt.setString(6, t.getNotes());
            pstmt.setBigDecimal(7, t.getTotalCost());
            pstmt.setString(8, t.getStatus());
            pstmt.setTimestamp(9, Timestamp.valueOf(java.time.LocalDateTime.now()));
            pstmt.setInt(10, t.getPrescriptionId());

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
    public int delete(Prescription t) {
        String sql = "DELETE FROM Prescriptions WHERE prescription_id = ?";
        int rowsAffected = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, t.getPrescriptionId());
            
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
    public ArrayList<Prescription> selectAll() {
        String sql = "SELECT * FROM Prescriptions";
        ArrayList<Prescription> list = new ArrayList<>();
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
    public ArrayList<Prescription> selectByCondition(String condition) {
        String sql = "SELECT * FROM Prescriptions WHERE " + condition;
        ArrayList<Prescription> list = new ArrayList<>();
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
        String sql = "SELECT EXISTS(SELECT 1 FROM Prescriptions WHERE " + condition + ")";
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