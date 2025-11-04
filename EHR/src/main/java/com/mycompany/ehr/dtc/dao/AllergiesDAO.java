package com.mycompany.ehr.dtc.dao;

import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

import com.mycompany.ehr.dtc.model.Allergies;
import com.mycompany.ehr.dtc.model.Allergies.AllergySeverity;
import com.mycompany.ehr.dtc.model.Allergies.AllergyType;
import com.mycompany.ehr.util.DAOInterface;
import com.mycompany.ehr.util.JDBCUtil;

public class AllergiesDAO implements DAOInterface<Allergies> {

    public static AllergiesDAO getInstance() {
        return new AllergiesDAO();
    }

    @Override
    public int insert(Allergies t) {
        int result = 0;
        String sql = "INSERT INTO allergies (member_id, allergen, allergy_type, severity, symptoms, discovered_date, notes, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, t.getMemberId());
            ps.setString(2, t.getAllergen());
            ps.setString(3, t.getAllergyType().getDisplayName());
            ps.setString(4, t.getSeverity().getDisplayName());
            ps.setString(5, t.getSymptoms());

            if (t.getDiscoveredDate() != null) {
                ps.setDate(6, java.sql.Date.valueOf(t.getDiscoveredDate()));
            } else {
                ps.setNull(6, Types.DATE);
            }

            ps.setString(7, t.getNotes());
            ps.setTimestamp(8, Timestamp.valueOf(t.getCreatedAt()));

            result = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(Allergies t) {
        int result = 0;
        String sql = "UPDATE allergies SET " +
                     "allergen = ?, allergy_type = ?, severity = ?, symptoms = ?, " +
                     "discovered_date = ?, notes = ? " +
                     "WHERE allergy_id = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, t.getAllergen());
            ps.setString(2, t.getAllergyType().getDisplayName());
            ps.setString(3, t.getSeverity().getDisplayName());
            ps.setString(4, t.getSymptoms());

            if (t.getDiscoveredDate() != null) {
                ps.setDate(5, java.sql.Date.valueOf(t.getDiscoveredDate()));
            } else {
                ps.setNull(5, Types.DATE);
            }

            ps.setString(6, t.getNotes());
            ps.setInt(7, t.getAllergyId());

            result = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int delete(Allergies t) {
        int result = 0;
        String sql = "DELETE FROM allergies WHERE allergy_id = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, t.getAllergyId());
            result = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Allergies selectById(Allergies t) {
        Allergies result = null;
        String sql = "SELECT * FROM allergies WHERE allergy_id = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, t.getAllergyId());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                result = buildAllergyFromResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Allergies> selectAll() {
        ArrayList<Allergies> result = new ArrayList<>();
        String sql = "SELECT * FROM allergies";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(buildAllergyFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<Allergies> selectByCondition(String condition) {
        ArrayList<Allergies> result = new ArrayList<>();
        String sql = "SELECT * FROM allergies WHERE " + condition;

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(buildAllergyFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean exists(String condition) {
        boolean result = false;
        String sql = "SELECT EXISTS(SELECT 1 FROM allergies WHERE " + condition + ")";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                result = rs.getBoolean(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    

    private Allergies buildAllergyFromResultSet(ResultSet rs) throws SQLException {
        java.sql.Date discoveredSqlDate = rs.getDate("discovered_date");
        
        return new Allergies.Builder(
                rs.getInt("member_id"),
                rs.getString("allergen"),
                AllergyType.fromString(rs.getString("allergy_type")))
                .allergyId(rs.getInt("allergy_id"))
                .severity(AllergySeverity.fromString(rs.getString("severity")))
                .symptoms(rs.getString("symptoms"))
                .discoveredDate(discoveredSqlDate != null ? discoveredSqlDate.toLocalDate() : null)
                .notes(rs.getString("notes"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .build();
    }
}