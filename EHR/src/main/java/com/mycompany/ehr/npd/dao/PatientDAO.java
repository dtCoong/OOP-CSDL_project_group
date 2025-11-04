package com.mycompany.ehr.npd.dao;

import com.mycompany.ehr.util.*;
import com.mycompany.ehr.npd.model.Patient;
import java.sql.*;
import java.util.ArrayList;

public class PatientDAO implements DAOInterface<Patient> {

    private static PatientDAO instance;
    private PatientDAO() {}
    public static PatientDAO getInstance() {
        if (instance == null) {
            instance = new PatientDAO();
        }
        return instance;
    }


    public Patient findOrCreate(String fullName, String phoneNumber) {
        Patient patient = findByPhone(phoneNumber);
        if (patient != null) {
            return patient; 
        }
        

        patient = new Patient();
        patient.setFullName(fullName);
        patient.setPhoneNumber(phoneNumber);
        
        if (insert(patient) > 0) {
            return findByPhone(phoneNumber);
        }
        return null; 
    }

    public Patient findByPhone(String phoneNumber) {
        Patient patient = null;
        String sql = "SELECT * FROM patient WHERE phone_number = ?";
        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, phoneNumber);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                patient = mapResultSetToPatient(rs);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patient;
    }

    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        Patient p = new Patient();
        p.setPatientId(rs.getInt("patient_id"));
        p.setFullName(rs.getString("full_name"));
        p.setPhoneNumber(rs.getString("phone_number"));
        return p;
    }

    @Override
    public int insert(Patient t) {
        int result = 0;
        String sql = "INSERT INTO patient (full_name, phone_number) VALUES (?, ?)";
        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, t.getFullName());
            statement.setString(2, t.getPhoneNumber());
            result = statement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override public int update(Patient t) { return 0; }
    @Override public int delete(Patient t) { return 0; }
    @Override public Patient selectById(Patient t) { return null; }
    @Override public ArrayList<Patient> selectAll() { return new ArrayList<>(); }
    @Override public ArrayList<Patient> selectByCondition(String condition) { return new ArrayList<>(); }
    @Override public boolean exists(String condition) { return false; }
}