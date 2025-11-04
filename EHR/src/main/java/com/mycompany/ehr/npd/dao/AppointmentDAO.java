package com.mycompany.ehr.npd.dao;

import com.mycompany.ehr.npd.model.Appointment;
import com.mycompany.ehr.npd.model.Appointment.AppointmentStatus;
import com.mycompany.ehr.npd.model.Appointment.AppointmentType;
import com.mycompany.ehr.npd.model.Appointment.PaymentStatus;
import com.mycompany.ehr.util.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AppointmentDAO implements DAOInterface<Appointment> {

    private static AppointmentDAO instance;
    private AppointmentDAO() {}
    public static AppointmentDAO getInstance() {
        if (instance == null) {
            instance = new AppointmentDAO();
        }
        return instance;
    }
    

    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment app = new Appointment();
        app.setAppointmentId(rs.getInt("appointment_id"));
        app.setMemberId(rs.getInt("member_id"));
        app.setDoctorId(rs.getObject("doctor_id", Integer.class));
        app.setHospitalId(rs.getObject("hospital_id", Integer.class));
        app.setDepartmentId(rs.getObject("department_id", Integer.class));
        app.setAppointmentTime(rs.getObject("appointment_date", LocalDateTime.class));
        
        app.setType(AppointmentType.fromDisplayName(rs.getString("type")));
        app.setStatus(AppointmentStatus.fromDisplayName(rs.getString("status")));
        
        app.setChiefComplaint(rs.getString("chief_complaint"));
        app.setCost(rs.getBigDecimal("cost"));
        app.setPaymentStatus(PaymentStatus.fromDisplayName(rs.getString("payment_status")));
        
        return app;
    }

    @Override
    public int insert(Appointment t) throws SQLException {
        int result = 0;
        Connection connection = null;
        PreparedStatement statement = null;

        String sql = "INSERT INTO Appointments (member_id, doctor_id, hospital_id, department_id, " +
                     "appointment_date, chief_complaint, status, appointment_slot) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            connection = JDBCUtil.getConnection();
            statement = connection.prepareStatement(sql);

            statement.setInt(1, t.getMemberId());
            statement.setObject(2, t.getDoctorId());
            statement.setObject(3, t.getHospitalId());
            statement.setObject(4, t.getDepartmentId());
            statement.setTimestamp(5, Timestamp.valueOf(t.getAppointmentDate()));
            statement.setString(6, t.getChiefComplaint()); 
            statement.setString(7, AppointmentStatus.DA_DAT.getDisplayName());

            statement.setString(8, t.getAppointmentSlot()); 

            result = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e; 
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            JDBCUtil.closeConnection(connection);
        }
        return result;
    }
    
    @Override
    public int update(Appointment t) throws SQLException {
        return 0; 
    }
    
    @Override
    public int delete(Appointment t) throws SQLException {
        return 0; 
    }
    
    @Override
    public Appointment selectById(Appointment t) throws SQLException {
        return null; 
    }
    
    @Override
    public ArrayList<Appointment> selectAll() throws SQLException {
        return new ArrayList<>(); 
    }
    
    @Override
    public ArrayList<Appointment> selectByCondition(String condition) throws SQLException {
        return new ArrayList<>(); 
    }
    
    @Override
    public boolean exists(String condition) throws SQLException {
        return false; 
    }
}