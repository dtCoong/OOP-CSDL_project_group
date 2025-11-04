package com.mycompany.ehr.bnta.dao;

import com.mycompany.ehr.bnta.model.VaccineTemplates;
import com.mycompany.ehr.util.JDBCUtil;

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
        template.setTemplate_id(rs.getInt("template_id"));
        template.setVaccine_name(rs.getString("vaccine_name"));
        template.setDescription(rs.getString("description"));
        template.setAge_from_days(rs.getInt("age_from_days"));
        
       
        template.setAge_to_days((Integer) rs.getObject("age_to_days"));
        template.setInterval_days((Integer) rs.getObject("interval_days"));
        
        template.setTotal_doses(rs.getInt("total_doses"));
        template.setNotes(rs.getString("notes"));
        
        
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            template.setCreated_at(ts.toLocalDateTime());
        }
        return template;
    }

    @Override
    public int insert(VaccineTemplates t) {
       
        String sql = "INSERT INTO Vaccine_Templates (vaccine_name, description, age_from_days, age_to_days, interval_days, total_doses, notes, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            
            ps.setString(1, t.getVaccine_name());
            ps.setString(2, t.getDescription());
            ps.setInt(3, t.getAge_from_days());
            ps.setObject(4, t.getAge_to_days()); 
            ps.setObject(5, t.getInterval_days()); 
            ps.setInt(6, t.getTotal_doses());
            ps.setString(7, t.getNotes());
            ps.setTimestamp(8, t.getCreated_at() != null ? Timestamp.valueOf(t.getCreated_at()) : null);

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
            System.err.println("Lỗi khi insert Vaccine_Templates: " + e.getMessage());
            e.printStackTrace();
            return 0; 
        }
    }

    @Override
    public VaccineTemplates selectById(VaccineTemplates t) {
        if (t == null) return null;
        int templateId = t.getTemplate_id(); 
        
        VaccineTemplates template = null;

        String sql = "SELECT * FROM Vaccine_Templates WHERE template_id = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, templateId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    template = extractTemplateFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi selectById Vaccine_Templates: " + e.getMessage());
            e.printStackTrace();
        }
        return template;
    }

    @Override
    public int update(VaccineTemplates t) {
       
        String sql = "UPDATE Vaccine_Templates SET vaccine_name = ?, description = ?, age_from_days = ?, age_to_days = ?, interval_days = ?, total_doses = ?, notes = ? WHERE template_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
           
            ps.setString(1, t.getVaccine_name());
            ps.setString(2, t.getDescription());
            ps.setInt(3, t.getAge_from_days());
            ps.setObject(4, t.getAge_to_days());
            ps.setObject(5, t.getInterval_days());
            ps.setInt(6, t.getTotal_doses());
            ps.setString(7, t.getNotes());
            ps.setInt(8, t.getTemplate_id()); 

            return ps.executeUpdate(); 
        } catch (SQLException e) {
            System.err.println("Lỗi khi update Vaccine_Templates: " + e.getMessage());
            e.printStackTrace();
            return 0; 
        }
    }

    @Override
    public int delete(VaccineTemplates t) {
        if (t == null) return 0;
        int templateId = t.getTemplate_id(); 

       
        String sql = "DELETE FROM Vaccine_Templates WHERE template_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, templateId);
            return ps.executeUpdate(); 
        } catch (SQLException e) {
            System.err.println("Lỗi khi delete Vaccine_Templates: " + e.getMessage());
            e.printStackTrace();
            return 0; 
        }
    }

    @Override
    public ArrayList<VaccineTemplates> selectAll() {
        ArrayList<VaccineTemplates> templates = new ArrayList<>();
        
        String sql = "SELECT * FROM Vaccine_Templates"; 

        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                templates.add(extractTemplateFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi selectAll Vaccine_Templates: " + e.getMessage());
            e.printStackTrace();
        }
        return templates;
    }

    @Override
    public ArrayList<VaccineTemplates> selectByCondition(String condition) {
        ArrayList<VaccineTemplates> templates = new ArrayList<>();
        
        String sql = "SELECT * FROM Vaccine_Templates WHERE " + condition; 

        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                templates.add(extractTemplateFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi selectByCondition Vaccine_Templates: " + e.getMessage());
            e.printStackTrace();
        }
        return templates;
    }

    @Override
    public boolean exists(String condition) {
       
        String sql = "SELECT EXISTS (SELECT 1 FROM Vaccine_Templates WHERE " + condition + ")";
        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi exists Vaccine_Templates: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}