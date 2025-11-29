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

}
