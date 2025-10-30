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
        String sql = "INSERT INTO Medications (name, generic_name, description, side_effects, " +
                     "contraindications, interactions, manufacturer, unit, " +
                     "requires_prescription, barcode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int newId = 0;

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, t.getName());
            pstmt.setString(2, t.getGenericName());
            pstmt.setString(3, t.getDescription());
            pstmt.setString(4, t.getSideEffects());
            pstmt.setString(5, t.getContraindications());
            pstmt.setString(6, t.getInteractions());
            pstmt.setString(7, t.getManufacturer());
            pstmt.setString(8, t.getUnit());
            pstmt.setBoolean(9, t.isRequiresPrescription());
            pstmt.setString(10, t.getBarcode());

            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                newId = rs.getInt(1);
                t.setMedicationId(newId);
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
        String sql = "UPDATE Medications SET name = ?, generic_name = ?, description = ?, " +
                     "side_effects = ?, contraindications = ?, interactions = ?, " +
                     "manufacturer = ?, unit = ?, requires_prescription = ?, barcode = ? " +
                     "WHERE medication_id = ?";
        
        int rowsAffected = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, t.getName());
            pstmt.setString(2, t.getGenericName());
            pstmt.setString(3, t.getDescription());
            pstmt.setString(4, t.getSideEffects());
            pstmt.setString(5, t.getContraindications());
            pstmt.setString(6, t.getInteractions());
            pstmt.setString(7, t.getManufacturer());
            pstmt.setString(8, t.getUnit()); // getUnit() trả về display name
            pstmt.setBoolean(9, t.isRequiresPrescription());
            pstmt.setString(10, t.getBarcode());
            pstmt.setInt(11, t.getMedicationId());

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
    public int delete(Medication t) {
        String sql = "DELETE FROM Medications WHERE medication_id = ?";
        int rowsAffected = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, t.getMedicationId());
            
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
        String sql = "SELECT EXISTS(SELECT 1 FROM Medications WHERE " + condition + ")";
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