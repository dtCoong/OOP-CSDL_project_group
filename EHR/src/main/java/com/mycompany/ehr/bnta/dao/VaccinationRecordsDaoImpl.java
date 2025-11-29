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
    public ArrayList<VaccinationRecords> selectByMemberId(int memberId) {
        ArrayList<VaccinationRecords> records = new ArrayList<>();
        String sql = "SELECT * FROM Vaccination_Records WHERE member_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    records.add(extractRecordFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }
}
