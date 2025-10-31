package com.mycompany.ehr.dao;

import com.mycompany.ehr.model.VaccineTemplates;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

public class VaccineTemplatesDaoImpl implements VaccineTemplatesDao {

    private VaccineTemplates extractTemplateFromResultSet(ResultSet rs) throws SQLException {
        VaccineTemplates template = new VaccineTemplates();
        template.setVaccineTemplateId(rs.getInt("vaccineTemplateId"));
        template.setVaccineName(rs.getString("vaccineName"));
        template.setDescription(rs.getString("description"));
        template.setAgeFromDays(rs.getInt("ageFromDays"));
        template.setAgeToDays(rs.getInt("ageToDays"));
        template.setIntervalDays(rs.getInt("intervalDays"));
        template.setTotalDoses(rs.getInt("totalDoses"));
        template.setNotes(rs.getString("notes"));
        Timestamp ts = rs.getTimestamp("createdAt");
        if (ts != null) {
            template.setCreatedAt(ts.toLocalDateTime());
        }
        return template;
    }

    @Override
    public int insert(VaccineTemplates t) {
        String sql = "INSERT INTO VaccineTemplates (vaccineName, description, ageFromDays, ageToDays, intervalDays, totalDoses, notes, createdAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, t.getVaccineName());
            ps.setString(2, t.getDescription());
            ps.setInt(3, t.getAgeFromDays());
            ps.setInt(4, t.getAgeToDays());
            ps.setInt(5, t.getIntervalDays());
            ps.setInt(6, t.getTotalDoses());
            ps.setString(7, t.getNotes());
            ps.setTimestamp(8, Timestamp.valueOf(t.getCreatedAt())); 

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
            System.err.println("Lỗi khi insert VaccineTemplate: " + e.getMessage());
            e.printStackTrace();
            return 0; 
        }
    }

    @Override
    public VaccineTemplates selectById(VaccineTemplates t) {
        if (t == null) return null;
        int templateId = t.getVaccineTemplateId();
        
        VaccineTemplates template = null;
        String sql = "SELECT * FROM VaccineTemplates WHERE vaccineTemplateId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, templateId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    template = extractTemplateFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi selectById VaccineTemplate: " + e.getMessage());
            e.printStackTrace();
        }
        return template;
    }

    @Override
    public int update(VaccineTemplates t) {
        String sql = "UPDATE VaccineTemplates SET vaccineName = ?, description = ?, ageFromDays = ?, ageToDays = ?, intervalDays = ?, totalDoses = ?, notes = ? WHERE vaccineTemplateId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, t.getVaccineName());
            ps.setString(2, t.getDescription());
            ps.setInt(3, t.getAgeFromDays());
            ps.setInt(4, t.getAgeToDays());
            ps.setInt(5, t.getIntervalDays());
            ps.setInt(6, t.getTotalDoses());
            ps.setString(7, t.getNotes());
            ps.setInt(8, t.getVaccineTemplateId());

            return ps.executeUpdate(); 
        } catch (SQLException e) {
            System.err.println("Lỗi khi update VaccineTemplate: " + e.getMessage());
            e.printStackTrace();
            return 0; 
        }
    }

    @Override
    public int delete(VaccineTemplates t) {
        if (t == null) return 0;
        int templateId = t.getVaccineTemplateId();

        String sql = "DELETE FROM VaccineTemplates WHERE vaccineTemplateId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, templateId);
            return ps.executeUpdate(); 
        } catch (SQLException e) {
            System.err.println("Lỗi khi delete VaccineTemplate: " + e.getMessage());
            e.printStackTrace();
            return 0; 
        }
    }

    @Override
    public ArrayList<VaccineTemplates> selectAll() {
        ArrayList<VaccineTemplates> templates = new ArrayList<>();
        String sql = "SELECT * FROM VaccineTemplates"; 

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                templates.add(extractTemplateFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi selectAll VaccineTemplates: " + e.getMessage());
            e.printStackTrace();
        }
        return templates;
    }

    @Override
    public ArrayList<VaccineTemplates> selectByCondition(String condition) {
        ArrayList<VaccineTemplates> templates = new ArrayList<>();
        String sql = "SELECT * FROM VaccineTemplates WHERE " + condition; 

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                templates.add(extractTemplateFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi selectByCondition VaccineTemplates: " + e.getMessage());
            e.printStackTrace();
        }
        return templates;
    }

    @Override
    public boolean exists(String condition) {
        String sql = "SELECT EXISTS (SELECT 1 FROM VaccineTemplates WHERE " + condition + ")";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi exists VaccineTemplates: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}