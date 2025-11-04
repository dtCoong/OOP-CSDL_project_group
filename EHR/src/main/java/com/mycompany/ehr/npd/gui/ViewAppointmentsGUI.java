package com.mycompany.ehr.npd.gui;

import com.mycompany.ehr.util.*; 

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class ViewAppointmentsGUI extends JDialog {
    
    private JTable appointmentsTable;
    private DefaultTableModel tableModel;
    private int currentUserId; 

    public ViewAppointmentsGUI(Frame owner, int userId) {
        super(owner, "Xem Lịch hẹn đã đặt", true); 
        this.currentUserId = userId;
        
        setupUI();
        loadAppointments();
    }

    private void setupUI() {
        setSize(1000, 500); 
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));

        String[] columnNames = {
            "Ngày hẹn", "Ca khám", "Bệnh nhân", "Bác sĩ", 
            "Khoa", "Bệnh viện", "Trạng thái", "Lý do khám"
        };
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        appointmentsTable = new JTable(tableModel);
        appointmentsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        appointmentsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        appointmentsTable.setRowHeight(25);

        appointmentsTable.getColumnModel().getColumn(0).setPreferredWidth(100); // Ngày hẹn
        appointmentsTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Ca khám
        appointmentsTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Bệnh nhân
        appointmentsTable.getColumnModel().getColumn(3).setPreferredWidth(120); // Bác sĩ
        appointmentsTable.getColumnModel().getColumn(5).setPreferredWidth(150); // Bệnh viện
        appointmentsTable.getColumnModel().getColumn(7).setPreferredWidth(200); // Lý do

        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Đóng");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.addActionListener(e -> dispose()); 
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPanel.add(closeButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Tải dữ liệu lịch hẹn bằng câu lệnh SQL JOIN
     */
    private void loadAppointments() {
        String sql = "SELECT " +
                     "A.appointment_date, " +
                     "A.appointment_slot, " +
                     "FM.name AS patient_name, " +
                     "D.full_name AS doctor_name, " +
                     "DEPT.name AS department_name, " +
                     "H.name AS hospital_name, " +
                     "A.status, " +
                     "A.chief_complaint " +
                     "FROM Appointments A " +
                     "LEFT JOIN Family_Members FM ON A.member_id = FM.member_id " +
                     "LEFT JOIN Doctors D ON A.doctor_id = D.doctor_id " +
                     "LEFT JOIN Departments DEPT ON A.department_id = DEPT.department_id " +
                     "LEFT JOIN Hospitals H ON A.hospital_id = H.hospital_id " +
                     "WHERE FM.user_id = ? " + 
                     "ORDER BY A.appointment_date DESC"; 

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, this.currentUserId);
            ResultSet rs = ps.executeQuery();

            tableModel.setRowCount(0);

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                
                Timestamp ts = rs.getTimestamp("appointment_date");
                row.add(ts != null ? ts.toLocalDateTime().format(formatter) : "N/A");

                row.add(rs.getString("appointment_slot"));
                
                row.add(rs.getString("patient_name"));
                row.add(rs.getString("doctor_name"));
                row.add(rs.getString("department_name"));
                row.add(rs.getString("hospital_name"));
                row.add(rs.getString("status"));
                row.add(rs.getString("chief_complaint"));
                
                tableModel.addRow(row);
            }
            
            if (tableModel.getRowCount() == 0) {
                 JOptionPane.showMessageDialog(this, "Không tìm thấy lịch hẹn nào đã đặt.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải lịch hẹn: " + e.getMessage(), "Lỗi Database", JOptionPane.ERROR_MESSAGE);
        }
    }
}