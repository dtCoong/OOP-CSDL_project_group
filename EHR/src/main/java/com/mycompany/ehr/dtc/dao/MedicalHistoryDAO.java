package com.mycompany.ehr.dtc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

import com.mycompany.ehr.dtc.model.MedicalHistory;
import com.mycompany.ehr.dtc.model.MedicalHistory.Severity;
import com.mycompany.ehr.dtc.model.MedicalHistory.Status;
import com.mycompany.ehr.util.DAOInterface;
import com.mycompany.ehr.util.JDBCUtil;

public class MedicalHistoryDAO implements DAOInterface<MedicalHistory> {

    private static MedicalHistoryDAO instance;
    private MedicalHistoryDAO() {}
    public static MedicalHistoryDAO getInstance() {
        if (instance == null) {
            instance = new MedicalHistoryDAO();
        }
        return instance;
    }

    private String lastError = null;
    public String getLastError() { return lastError; }
    public void clearLastError() { lastError = null; }

    @Override
    public int insert(MedicalHistory t) {
        clearLastError();
        int result = 0;
        String sql = "INSERT INTO medical_history (member_id, condition_name, diagnosis_date, is_chronic, " +
                     "severity, status, notes, hospital_admission_date, hospital_discharge_date, " +
                     "hospital_name, hospital_address, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; 

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, t.getMemberId());
            ps.setString(2, t.getConditionName());

            if (t.getDiagnosisDate() != null) {
                ps.setDate(3, java.sql.Date.valueOf(t.getDiagnosisDate()));
            } else {
                ps.setNull(3, Types.DATE);
            }

            ps.setBoolean(4, t.isChronic());
            ps.setString(5, t.getSeverity().getDisplayName());
            ps.setString(6, t.getStatus().getDisplayName());
            ps.setString(7, t.getNotes());

            if (t.getHospitalAdmissionDate() != null) {
                ps.setDate(8, java.sql.Date.valueOf(t.getHospitalAdmissionDate()));
            } else {
                ps.setNull(8, Types.DATE);
            }
            if (t.getHospitalDischargeDate() != null) {
                ps.setDate(9, java.sql.Date.valueOf(t.getHospitalDischargeDate()));
            } else {
                ps.setNull(9, Types.DATE);
            }
            ps.setString(10, t.getHospitalName());
            ps.setString(11, t.getHospitalAddress());

            ps.setTimestamp(12, Timestamp.valueOf(t.getCreatedAt()));

            result = ps.executeUpdate();

        } catch (SQLException e) {
            lastError = e.getMessage(); 
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int update(MedicalHistory t) {
        clearLastError();
        int result = 0;
        String sql = "UPDATE medical_history SET " +
                     "condition_name = ?, diagnosis_date = ?, is_chronic = ?, " +
                     "severity = ?, status = ?, notes = ?, " +
                     "hospital_admission_date = ?, hospital_discharge_date = ?, " +
                     "hospital_name = ?, hospital_address = ? " + 
                     "WHERE history_id = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, t.getConditionName());
            if (t.getDiagnosisDate() != null) {
                ps.setDate(2, java.sql.Date.valueOf(t.getDiagnosisDate()));
            } else {
                ps.setNull(2, Types.DATE);
            }
            ps.setBoolean(3, t.isChronic());
            ps.setString(4, t.getSeverity().getDisplayName());
            ps.setString(5, t.getStatus().getDisplayName());
            ps.setString(6, t.getNotes());

            if (t.getHospitalAdmissionDate() != null) {
                ps.setDate(7, java.sql.Date.valueOf(t.getHospitalAdmissionDate()));
            } else {
                ps.setNull(7, Types.DATE);
            }
            if (t.getHospitalDischargeDate() != null) {
                ps.setDate(8, java.sql.Date.valueOf(t.getHospitalDischargeDate()));
            } else {
                ps.setNull(8, Types.DATE);
            }
            ps.setString(9, t.getHospitalName());
            ps.setString(10, t.getHospitalAddress());

            ps.setInt(11, t.getHistoryId()); 

            result = ps.executeUpdate();

        } catch (SQLException e) {
            lastError = e.getMessage();
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int delete(MedicalHistory t) {
        clearLastError(); 
        int result = 0;
        String sql = "DELETE FROM medical_history WHERE history_id = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, t.getHistoryId());
            result = ps.executeUpdate();

        } catch (SQLException e) {
            lastError = e.getMessage(); 
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public MedicalHistory selectById(MedicalHistory t) {
        clearLastError(); 
        MedicalHistory result = null;
        String sql = "SELECT * FROM medical_history WHERE history_id = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, t.getHistoryId());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                result = buildMedicalHistoryFromResultSet(rs);
            }

        } catch (SQLException e) {
            lastError = e.getMessage(); 
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<MedicalHistory> selectAll() {
        clearLastError(); 
        ArrayList<MedicalHistory> result = new ArrayList<>();
        String sql = "SELECT * FROM medical_history";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(buildMedicalHistoryFromResultSet(rs));
            }

        } catch (SQLException e) {
            lastError = e.getMessage(); 
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<MedicalHistory> selectByCondition(String condition) {
        clearLastError(); 
        ArrayList<MedicalHistory> result = new ArrayList<>();
        String sql = "SELECT * FROM medical_history WHERE " + condition;

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(buildMedicalHistoryFromResultSet(rs));
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
        String sql = "SELECT EXISTS(SELECT 1 FROM medical_history WHERE " + condition + ")";

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

    private MedicalHistory buildMedicalHistoryFromResultSet(ResultSet rs) throws SQLException {
        java.sql.Date diagnosisSqlDate = rs.getDate("diagnosis_date");
        java.sql.Date admissionSqlDate = rs.getDate("hospital_admission_date");
        java.sql.Date dischargeSqlDate = rs.getDate("hospital_discharge_date");

        return new MedicalHistory.Builder(
                rs.getInt("member_id"),
                rs.getString("condition_name"))
                .historyId(rs.getInt("history_id"))
                .diagnosisDate(diagnosisSqlDate != null ? diagnosisSqlDate.toLocalDate() : null)
                .isChronic(rs.getBoolean("is_chronic"))
                .severity(Severity.fromString(rs.getString("severity")))
                .status(Status.fromString(rs.getString("status")))
                .notes(rs.getString("notes"))
                .hospitalAdmissionDate(admissionSqlDate != null ? admissionSqlDate.toLocalDate() : null)
                .hospitalDischargeDate(dischargeSqlDate != null ? dischargeSqlDate.toLocalDate() : null)
                .hospitalName(rs.getString("hospital_name"))
                .hospitalAddress(rs.getString("hospital_address"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .build();
    }
}