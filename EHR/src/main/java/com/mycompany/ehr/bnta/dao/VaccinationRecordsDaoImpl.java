package com.mycompany.ehr.bnta.dao;

import com.mycompany.ehr.bnta.model.VaccinationRecords; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import com.mycompany.ehr.util.JDBCUtil;

public class VaccinationRecordsDaoImpl implements VaccinationRecordsDao {

    private VaccinationRecords extractRecordFromResultSet(ResultSet rs) throws SQLException {
        VaccinationRecords record = new VaccinationRecords();
        record.setVaccination_id(rs.getInt("vaccination_id"));
        record.setMember_id(rs.getInt("member_id"));
        
        record.setTemplate_id((Integer) rs.getObject("template_id"));
        
        record.setVaccine_name(rs.getString("vaccine_name"));
        record.setDose_number(rs.getInt("dose_number"));
        
        Date vaxDate = rs.getDate("vaccination_date");
        if (vaxDate != null) record.setVaccination_date(vaxDate.toLocalDate());
        
        Date dueDate = rs.getDate("next_due_date");
        if (dueDate != null) record.setNext_due_date(dueDate.toLocalDate());
        
        record.setBatch_number(rs.getString("batch_number"));
        record.setStatus(rs.getString("status"));
        record.setSide_effects(rs.getString("side_effects"));
        record.setNotes(rs.getString("notes"));
        
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            record.setCreated_at(ts.toLocalDateTime());
        }
        return record;
    }

    @Override
    public int insert(VaccinationRecords t) {
        String sql = "INSERT INTO Vaccination_Records (member_id, template_id, vaccine_name, dose_number, vaccination_date, next_due_date, batch_number, status, side_effects, notes, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, t.getMember_id());
            ps.setObject(2, t.getTemplate_id()); 
            ps.setString(3, t.getVaccine_name());
            ps.setInt(4, t.getDose_number());
            ps.setDate(5, t.getVaccination_date() != null ? Date.valueOf(t.getVaccination_date()) : null);
            ps.setDate(6, t.getNext_due_date() != null ? Date.valueOf(t.getNext_due_date()) : null);
            ps.setString(7, t.getBatch_number());
            ps.setString(8, t.getStatus());
            ps.setString(9, t.getSide_effects());
            ps.setString(10, t.getNotes());
            ps.setTimestamp(11, t.getCreated_at() != null ? Timestamp.valueOf(t.getCreated_at()) : null);

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
            System.err.println("Lỗi khi insert Vaccination_Records: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public VaccinationRecords selectById(VaccinationRecords t) {
        if (t == null) return null;

        int vaccinationId = t.getVaccination_id(); 
        VaccinationRecords record = null;
        String sql = "SELECT * FROM Vaccination_Records WHERE vaccination_id = ?";
    try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, vaccinationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    record = extractRecordFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi selectById Vaccination_Records: " + e.getMessage());
            e.printStackTrace();
        }
        return record;
    }

    @Override
    public int update(VaccinationRecords t) {
        String sql = "UPDATE Vaccination_Records SET member_id = ?, template_id = ?, vaccine_name = ?, dose_number = ?, vaccination_date = ?, next_due_date = ?, batch_number = ?, status = ?, side_effects = ?, notes = ? WHERE vaccination_id = ?";
    try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, t.getMember_id());
            ps.setObject(2, t.getTemplate_id());
            ps.setString(3, t.getVaccine_name());
            ps.setInt(4, t.getDose_number());
            ps.setDate(5, t.getVaccination_date() != null ? Date.valueOf(t.getVaccination_date()) : null);
            ps.setDate(6, t.getNext_due_date() != null ? Date.valueOf(t.getNext_due_date()) : null);
            ps.setString(7, t.getBatch_number());
            ps.setString(8, t.getStatus());
            ps.setString(9, t.getSide_effects());
            ps.setString(10, t.getNotes());
            ps.setInt(11, t.getVaccination_id());

            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi update Vaccination_Records: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int delete(VaccinationRecords t) {
        if (t == null) return 0;
        int vaccinationId = t.getVaccination_id();

        String sql = "DELETE FROM Vaccination_Records WHERE vaccination_id = ?";
    try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, vaccinationId);
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi delete Vaccination_Records: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public ArrayList<VaccinationRecords> selectAll() {
        ArrayList<VaccinationRecords> records = new ArrayList<>();

        String sql = "SELECT * FROM Vaccination_Records";
    try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                records.add(extractRecordFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi selectAll Vaccination_Records: " + e.getMessage());
            e.printStackTrace();
        }
        return records;
    }

    @Override
    public ArrayList<VaccinationRecords> selectByCondition(String condition) {
        ArrayList<VaccinationRecords> records = new ArrayList<>();

        String sql = "SELECT * FROM Vaccination_Records WHERE " + condition; 
    try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                records.add(extractRecordFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi selectByCondition Vaccination_Records: " + e.getMessage());
            e.printStackTrace();
        }
        return records;
    }
    
    @Override
    public boolean exists(String condition) {

        String sql = "SELECT EXISTS (SELECT 1 FROM Vaccination_Records WHERE " + condition + ")";
    try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi exists Vaccination_Records: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}