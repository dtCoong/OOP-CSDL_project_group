package com.mycompany.ehr.dtc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.mycompany.ehr.dtc.model.FamilyMembers;
import com.mycompany.ehr.dtc.model.FamilyMembers.BloodType;
import com.mycompany.ehr.dtc.model.FamilyMembers.Gender;
import com.mycompany.ehr.dtc.model.FamilyMembers.Relationship;
import com.mycompany.ehr.util.DAOInterface;
import com.mycompany.ehr.util.JDBCUtil;

public class FamilyMembersDAO implements DAOInterface<FamilyMembers> {

    private static FamilyMembersDAO instance;
    private FamilyMembersDAO() {}
    public static FamilyMembersDAO getInstance() {
        if (instance == null) {
            instance = new FamilyMembersDAO();
        }
        return instance;
    }
    
    private String lastError = null;

    public String getLastError() {
        return lastError;
    }

    public void clearLastError() {
        lastError = null;
    }

    @Override
    public int insert(FamilyMembers t) {
        clearLastError();
        int result = 0;
        
        String sql = "INSERT INTO family_members (user_id, name, dob, gender, relationship, blood_type, insurance_number, phone, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = JDBCUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, t.getUserId());
            ps.setString(2, t.getName());
            ps.setDate(3, java.sql.Date.valueOf(t.getDob()));
            ps.setString(4, t.getGender().getDisplayName());
            ps.setString(5, t.getRelationship().getDisplayName());
            
            ps.setString(6, t.getBloodType().getDisplayName());
            
            ps.setString(7, t.getInsuranceNumber());
            ps.setString(8, t.getPhone());
            ps.setTimestamp(9, Timestamp.valueOf(t.getCreatedAt())); 
            ps.setTimestamp(10, Timestamp.valueOf(t.getUpdatedAt()));

            result = ps.executeUpdate();

        } catch (SQLException e) {
            lastError = e.getMessage(); 
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(FamilyMembers t) {
        clearLastError();
        int result = 0;
        
        String sql = "UPDATE family_members SET " +
                     "name = ?, dob = ?, gender = ?, relationship = ?, blood_type = ?, " +
                     "insurance_number = ?, phone = ?, updated_at = ? " +
                     "WHERE member_id = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, t.getName());
            ps.setDate(2, java.sql.Date.valueOf(t.getDob()));
            ps.setString(3, t.getGender().getDisplayName());
            ps.setString(4, t.getRelationship().getDisplayName());
            
            ps.setString(5, t.getBloodType().getDisplayName());
            
            ps.setString(6, t.getInsuranceNumber());
            ps.setString(7, t.getPhone());
            ps.setTimestamp(8, Timestamp.valueOf(t.getUpdatedAt()));

            ps.setInt(9, t.getMemberId());

            result = ps.executeUpdate();

        } catch (SQLException e) {
            lastError = e.getMessage();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int delete(FamilyMembers t) {
        clearLastError();
        int result = 0;
        String sql = "DELETE FROM family_members WHERE member_id = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, t.getMemberId());
            result = ps.executeUpdate();

        } catch (SQLException e) {
            lastError = e.getMessage();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public FamilyMembers selectById(FamilyMembers t) {
        clearLastError();
        FamilyMembers result = null;
        String sql = "SELECT * FROM family_members WHERE member_id = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, t.getMemberId());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                result = buildFamilyMemberFromResultSet(rs);
            }

        } catch (SQLException e) {
            lastError = e.getMessage();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<FamilyMembers> selectAll() {
        clearLastError();
        ArrayList<FamilyMembers> result = new ArrayList<>();
        String sql = "SELECT * FROM family_members";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(buildFamilyMemberFromResultSet(rs));
            }

        } catch (SQLException e) {
            lastError = e.getMessage();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<FamilyMembers> selectByCondition(String condition) {
        clearLastError();
        ArrayList<FamilyMembers> result = new ArrayList<>();
        String sql = "SELECT * FROM family_members WHERE " + condition;

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(buildFamilyMemberFromResultSet(rs));
            }

        } catch (SQLException e) {
            lastError = e.getMessage();
            e.printStackTrace();
        }
        return result;
    }
    
    @Override
    public boolean exists(String condition) {
        clearLastError();
        boolean result = false;
        String sql = "SELECT EXISTS(SELECT 1 FROM family_members WHERE " + condition + ")";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                result = rs.getBoolean(1);
            }

        } catch (SQLException e) {
            lastError = e.getMessage();
            e.printStackTrace();
        }
        return result;
    }

    private FamilyMembers buildFamilyMemberFromResultSet(ResultSet rs) throws SQLException {

        String avatarPath = null;
        try {
            int avatarColIndex = rs.findColumn("avatar_path");
            avatarPath = rs.getString(avatarColIndex);
        } catch (SQLException e) {

        }
        
        return new FamilyMembers.Builder(
                rs.getInt("user_id"),
                rs.getString("name"),
                rs.getDate("dob").toLocalDate()) 
                .memberId(rs.getInt("member_id"))
                .gender(Gender.fromString(rs.getString("gender")))
                .relationship(Relationship.fromString(rs.getString("relationship")))
                .bloodType(BloodType.fromString(rs.getString("blood_type")))
                .insuranceNumber(rs.getString("insurance_number"))
                .phone(rs.getString("phone"))
                .avatarPath(avatarPath)
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime()) 
                .build();
    }
}