package com.mycompany.ehr.dao;

import com.mycompany.ehr.model.PrescriptionDetail;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PrescriptionDetailDAO implements DAOInterface<PrescriptionDetail> {

    private PrescriptionDetail.DetailStatus getStatusFromDisplayName(String displayName) {
        if (displayName == null) return null;
        for (PrescriptionDetail.DetailStatus s : PrescriptionDetail.DetailStatus.values()) {
            if (s.getDisplayName().equals(displayName)) {
                return s;
            }
        }
        return null;
    }

    private PrescriptionDetail extractFromResultSet(ResultSet rs) throws SQLException {

        return new PrescriptionDetail(
                rs.getInt("detail_id"),
                rs.getInt("prescription_id"),
                rs.getInt("medication_id"),
                rs.getString("dosage"),
                rs.getString("frequency"),
                rs.getInt("duration_days"),
                rs.getInt("total_quantity"),
                rs.getString("instructions"),
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("end_date").toLocalDate(),
                getStatusFromDisplayName(rs.getString("status")).name(),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
    
    @Override
    public int insert(PrescriptionDetail t) {
        String sql = "INSERT INTO Prescription_Details (prescription_id, medication_id, dosage, " +
                     "frequency, duration_days, total_quantity, instructions, " +
                     "start_date, end_date, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int newId = 0;

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, t.getPrescriptionId());
            pstmt.setInt(2, t.getMedicationId());
            pstmt.setString(3, t.getDosage());
            pstmt.setString(4, t.getFrequency());
            pstmt.setInt(5, t.getDurationDays());
            pstmt.setInt(6, t.getTotalQuantity());
            pstmt.setString(7, t.getInstructions());
            pstmt.setDate(8, Date.valueOf(t.getStartDate()));
            pstmt.setDate(9, Date.valueOf(t.getEndDate()));
            pstmt.setString(10, t.getStatus()); 

            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                newId = rs.getInt(1);
                t.setDetailId(newId);
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
    public PrescriptionDetail selectById(PrescriptionDetail t) {
        String sql = "SELECT * FROM Prescription_Details WHERE detail_id = ?";
        PrescriptionDetail detail = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, t.getDetailId());
            
            rs = pstmt.executeQuery();
            if (rs.next()) {
                detail = extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            JDBCUtil.closeConnection(conn);
        }
        return detail;
    }

    @Override
    public int update(PrescriptionDetail t) {
        String sql = "UPDATE Prescription_Details SET prescription_id = ?, medication_id = ?, " +
                     "dosage = ?, frequency = ?, duration_days = ?, total_quantity = ?, " +
                     "instructions = ?, start_date = ?, end_date = ?, status = ? " +
                     "WHERE detail_id = ?";
        
        int rowsAffected = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, t.getPrescriptionId());
            pstmt.setInt(2, t.getMedicationId());
            pstmt.setString(3, t.getDosage());
            pstmt.setString(4, t.getFrequency());
            pstmt.setInt(5, t.getDurationDays());
            pstmt.setInt(6, t.getTotalQuantity());
            pstmt.setString(7, t.getInstructions());
            pstmt.setDate(8, Date.valueOf(t.getStartDate()));
            pstmt.setDate(9, Date.valueOf(t.getEndDate()));
            pstmt.setString(10, t.getStatus());
            pstmt.setInt(11, t.getDetailId());

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
    public int delete(PrescriptionDetail t) {
        String sql = "DELETE FROM Prescription_Details WHERE detail_id = ?";
        int rowsAffected = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, t.getDetailId());
            
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
    public ArrayList<PrescriptionDetail> selectAll() {
        String sql = "SELECT * FROM Prescription_Details";
        ArrayList<PrescriptionDetail> list = new ArrayList<>();
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
    public ArrayList<PrescriptionDetail> selectByCondition(String condition) {
        String sql = "SELECT * FROM Prescription_Details WHERE " + condition;
        ArrayList<PrescriptionDetail> list = new ArrayList<>();
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
        String sql = "SELECT EXISTS(SELECT 1 FROM Prescription_Details WHERE " + condition + ")";
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