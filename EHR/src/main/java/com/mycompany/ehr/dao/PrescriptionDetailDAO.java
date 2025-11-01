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
        return 0;
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
        return 0;
    }

    @Override
    public int delete(PrescriptionDetail t) {
        return 0;
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
        return false;
    }
}