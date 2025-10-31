package com.mycompany.ehr.dao;

import com.mycompany.ehr.model.VaccinationRecords;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;


public class VaccinationRecordsDaoImpl implements VaccinationRecordsDao {

    private VaccinationRecords extractRecordFromResultSet(ResultSet rs) throws SQLException {
        VaccinationRecords record = new VaccinationRecords();
        record.setVaccinationId(rs.getInt("vaccinationId"));
        record.setMemberId(rs.getInt("memberId"));
        Integer templateId = rs.getInt("templateId");
        if (rs.wasNull()) record.setTemplateId(null);
        else record.setTemplateId(templateId);
        
        record.setVaccineName(rs.getString("vaccineName"));
        record.setDoseNumber(rs.getInt("doseNumber"));
        Date vaxDate = rs.getDate("vaccinationDate");
        if (vaxDate != null) record.setVaccinationDate(vaxDate.toLocalDate());
        Date dueDate = rs.getDate("nextDueDate");
        if (dueDate != null) record.setNextDueDate(dueDate.toLocalDate());
        record.setBatchNumber(rs.getString("batchNumber"));
        record.setStatus(rs.getString("status"));
        record.setNotes(rs.getString("notes"));
        Timestamp ts = rs.getTimestamp("createdAt");
        if (ts != null) record.setCreatedAt(ts.toLocalDateTime());
        return record;
    }

    @Override
    public int insert(VaccinationRecords t) {
        String sql = "INSERT INTO VaccinationRecords (memberId, templateId, vaccineName, doseNumber, vaccinationDate, nextDueDate, batchNumber, status, notes, createdAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, t.getMemberId());
            if (t.getTemplateId() != null) ps.setInt(2, t.getTemplateId());
            else ps.setNull(2, java.sql.Types.INTEGER);
            ps.setString(3, t.getVaccineName());
            ps.setInt(4, t.getDoseNumber());
            ps.setDate(5, Date.valueOf(t.getVaccinationDate()));
            if (t.getNextDueDate() != null) ps.setDate(6, Date.valueOf(t.getNextDueDate()));
            else ps.setNull(6, java.sql.Types.DATE);
            ps.setString(7, t.getBatchNumber());
            ps.setString(8, t.getStatus());
            ps.setString(9, t.getNotes());
            ps.setTimestamp(10, Timestamp.valueOf(t.getCreatedAt()));

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
            return 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi insert VaccinationRecord: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public VaccinationRecords selectById(VaccinationRecords t) {
        if (t == null) return null;
        int vaccinationId = t.getVaccinationId();

        VaccinationRecords record = null;
        String sql = "SELECT * FROM VaccinationRecords WHERE vaccinationId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, vaccinationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    record = extractRecordFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi selectById VaccinationRecord: " + e.getMessage());
            e.printStackTrace();
        }
        return record;
    }

    @Override
    public int update(VaccinationRecords t) {
        String sql = "UPDATE VaccinationRecords SET memberId = ?, templateId = ?, vaccineName = ?, doseNumber = ?, vaccinationDate = ?, nextDueDate = ?, batchNumber = ?, status = ?, notes = ? WHERE vaccinationId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, t.getMemberId());
            if (t.getTemplateId() != null) ps.setInt(2, t.getTemplateId());
            else ps.setNull(2, java.sql.Types.INTEGER);
            ps.setString(3, t.getVaccineName());
            ps.setInt(4, t.getDoseNumber());
            ps.setDate(5, Date.valueOf(t.getVaccinationDate()));
            if (t.getNextDueDate() != null) ps.setDate(6, Date.valueOf(t.getNextDueDate()));
            else ps.setNull(6, java.sql.Types.DATE);
            ps.setString(7, t.getBatchNumber());
            ps.setString(8, t.getStatus());
            ps.setString(9, t.getNotes());
            ps.setInt(10, t.getVaccinationId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi update VaccinationRecord: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int delete(VaccinationRecords t) {
        if (t == null) return 0;
        int vaccinationId = t.getVaccinationId();

        String sql = "DELETE FROM VaccinationRecords WHERE vaccinationId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, vaccinationId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi delete VaccinationRecord: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public ArrayList<VaccinationRecords> selectAll() {
        ArrayList<VaccinationRecords> records = new ArrayList<>();
        String sql = "SELECT * FROM VaccinationRecords";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                records.add(extractRecordFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi selectAll VaccinationRecords: " + e.getMessage());
            e.printStackTrace();
        }
        return records;
    }

    @Override
    public ArrayList<VaccinationRecords> selectByCondition(String condition) {
        ArrayList<VaccinationRecords> records = new ArrayList<>();
        String sql = "SELECT * FROM VaccinationRecords WHERE " + condition; 
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                records.add(extractRecordFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi selectByCondition VaccinationRecords: " + e.getMessage());
            e.printStackTrace();
        }
        return records;
    }
    
    @Override
    public boolean exists(String condition) {
        String sql = "SELECT EXISTS (SELECT 1 FROM VaccinationRecords WHERE " + condition + ")";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi exists VaccinationRecords: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}