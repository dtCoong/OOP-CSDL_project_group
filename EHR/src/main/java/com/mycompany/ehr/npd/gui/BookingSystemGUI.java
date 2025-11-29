package com.mycompany.ehr.npd.gui;

import com.toedter.calendar.JDateChooser; 
import java.util.Date;
import java.time.ZoneId;                 
import java.time.LocalDate;                 

import com.mycompany.ehr.dtc.dao.FamilyMembersDAO;
import com.mycompany.ehr.dtc.model.FamilyMembers;
import com.mycompany.ehr.npd.dao.AppointmentDAO;
import com.mycompany.ehr.npd.model.Appointment; 
import com.mycompany.ehr.npd.model.Department;
import com.mycompany.ehr.npd.model.Doctor;
import com.mycompany.ehr.npd.model.Hospital;
import com.mycompany.ehr.util.*;

import com.mycompany.ehr.dtc.model.User;
import com.mycompany.ehr.dtc.ui.HomeFrame;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList; 

public class BookingSystemGUI extends JFrame {

    private Hospital selectedHospital;
    private Department selectedDepartment;
    private Doctor selectedDoctor;
    
    private User currentUser;

    private JComboBox<FamilyMembers> memberComboBox;
    private JDateChooser dateChooser;
    private JComboBox<String> timeSlotComboBox;
    private JTextArea reasonArea;
    private JButton stepButton, confirmButton, resetButton;
    private JLabel hospitalLabel, departmentLabel, locationLabel, selectedDoctorLabel;
    private JPanel patientInfoPanel;

    public BookingSystemGUI(User user) {
        this.currentUser = user;
        
        setTitle("Hệ thống Đặt lịch hẹn");
        setSize(800, 600); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setupUI();
        loadFamilyMembers(); 
        resetForm(); 
    }

    private void viewAppointments() {
        ViewAppointmentsGUI viewDialog = new ViewAppointmentsGUI(this, this.currentUser.getUserId());
        viewDialog.setVisible(true);
    }
    
    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel selectionPanel = new JPanel(new GridBagLayout());
        selectionPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "Thông tin đã chọn", 
            TitledBorder.LEFT, TitledBorder.TOP));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        selectionPanel.add(new JLabel("Bệnh viện:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        hospitalLabel = createInfoLabel();
        selectionPanel.add(hospitalLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        selectionPanel.add(new JLabel("Khoa:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        departmentLabel = createInfoLabel();
        selectionPanel.add(departmentLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        selectionPanel.add(new JLabel("Địa điểm:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        locationLabel = createInfoLabel();
        selectionPanel.add(locationLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        selectionPanel.add(new JLabel("Bác sĩ:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        selectedDoctorLabel = createInfoLabel();
        selectionPanel.add(selectedDoctorLabel, gbc);

        patientInfoPanel = new JPanel(new GridBagLayout());
        patientInfoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "Thông tin Bệnh nhân và Lịch hẹn", 
            TitledBorder.LEFT, TitledBorder.TOP));
        gbc.gridwidth = 1; gbc.weightx = 0;
        gbc.gridx = 0; gbc.gridy = 0;
        patientInfoPanel.add(new JLabel("Chọn bệnh nhân:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        memberComboBox = new JComboBox<>();
        memberComboBox.setRenderer(new FamilyMemberRenderer());
        patientInfoPanel.add(memberComboBox, gbc);
        gbc.gridx = 0; gbc.gridy = 1; 
        patientInfoPanel.add(new JLabel("Chọn ngày:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; 
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setFont(new Font("Arial", Font.PLAIN, 14));
        patientInfoPanel.add(dateChooser, gbc);
        gbc.gridx = 0; gbc.gridy = 2; 
        patientInfoPanel.add(new JLabel("Chọn ca khám:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; 
        String[] timeSlots = {"Sáng (06:00 - 11:30)", "Chiều (13:30 - 17:00)"};
        timeSlotComboBox = new JComboBox<>(timeSlots);
        timeSlotComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        patientInfoPanel.add(timeSlotComboBox, gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.NORTH; 
        patientInfoPanel.add(new JLabel("Lý do khám:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0; 
        reasonArea = new JTextArea(4, 20);
        patientInfoPanel.add(new JScrollPane(reasonArea), gbc);

        // 3. Panel Nút bấm (Phía dưới)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        JButton btnBack = new JButton("Quay lại Trang chủ");
        btnBack.setFont(new Font("Arial", Font.PLAIN, 14));
        btnBack.setBackground(new Color(220, 220, 220));
        btnBack.addActionListener(e -> {
            HomeFrame home = new HomeFrame(currentUser);
            home.setVisible(true);
            this.dispose();
        });

        JButton viewButton = new JButton("Xem lịch đã đặt");
        viewButton.setFont(new Font("Arial", Font.PLAIN, 14));
        viewButton.addActionListener(e -> viewAppointments());
        
        stepButton = new JButton("Bước 1: Chọn Bệnh viện");
        stepButton.setFont(new Font("Arial", Font.BOLD, 14));
        stepButton.addActionListener(e -> handleStep());
        
        confirmButton = new JButton("Xác nhận Đặt lịch");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.addActionListener(e -> handleConfirmBooking());
        
        resetButton = new JButton("Đặt lại");
        resetButton.setFont(new Font("Arial", Font.PLAIN, 14));
        resetButton.addActionListener(e -> resetForm());
        
        buttonPanel.add(btnBack); // THÊM MỚI
        buttonPanel.add(viewButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(stepButton);
        buttonPanel.add(confirmButton);

        mainPanel.add(selectionPanel, BorderLayout.NORTH);
        mainPanel.add(patientInfoPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadFamilyMembers() {
        try {
            String condition = "user_id = " + this.currentUser.getUserId();
            ArrayList<FamilyMembers> members = FamilyMembersDAO.getInstance().selectByCondition(condition);
            
            memberComboBox.removeAllItems();
            
            if (members.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Không tìm thấy hồ sơ thành viên nào cho User ID: " + this.currentUser.getUserId(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                setPatientFormEnabled(false);
            } else {
                for (FamilyMembers member : members) {
                    memberComboBox.addItem(member);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách bệnh nhân: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleStep() {
        if (selectedHospital == null) {
            HospitalSelectionGUI hospitalDialog = new HospitalSelectionGUI(this);
            hospitalDialog.setVisible(true);
            selectedHospital = hospitalDialog.getSelectedHospital();
            if (selectedHospital != null) {
                setLabelSuccess(hospitalLabel, selectedHospital.getName());
                stepButton.setText("Bước 2: Chọn Khoa");
            }
        } else if (selectedDepartment == null) {
            DepartmentSelectionGUI deptDialog = new DepartmentSelectionGUI(this, selectedHospital.getHospitalId());
            deptDialog.setVisible(true);
            selectedDepartment = deptDialog.getSelectedDepartment();
            if (selectedDepartment != null) {
                setLabelSuccess(departmentLabel, selectedDepartment.getName());
                setLabelSuccess(locationLabel, selectedDepartment.getLocationDetails());
                stepButton.setText("Bước 3: Chọn Bác sĩ");
            }
        } else if (selectedDoctor == null) {
            DoctorGUI doctorDialog = new DoctorGUI(this, selectedDepartment.getDepartmentId());
            doctorDialog.setVisible(true);
            selectedDoctor = doctorDialog.getSelectedDoctor();
            if (selectedDoctor != null) {
                setLabelSuccess(selectedDoctorLabel, selectedDoctor.getFullName());
                stepButton.setText("Đã chọn xong!");
                stepButton.setEnabled(false);
                setPatientFormEnabled(true);    
                confirmButton.setEnabled(true);
            }
        }
    }

    private void handleConfirmBooking() {
        if (this.selectedDoctor == null) return;
        FamilyMembers selectedMember = (FamilyMembers) memberComboBox.getSelectedItem();
        if (selectedMember == null) {
             JOptionPane.showMessageDialog(this, "Vui lòng chọn một bệnh nhân.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Date selectedDate = dateChooser.getDate();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày khám.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        LocalDate date = selectedDate.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
        String selectedSlot = (String) timeSlotComboBox.getSelectedItem();
        String reason = reasonArea.getText(); 
        String timeStr = selectedSlot.startsWith("Sáng") ? "09:00" : "14:00"; 
        LocalDateTime appointmentTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        try {
            appointmentTime = LocalDateTime.parse(date.toString() + " " + timeStr, formatter);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi ghép ngày và giờ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        LocalDateTime now = LocalDateTime.now(); 
        if (appointmentTime.isBefore(now)) {
            JOptionPane.showMessageDialog(this, 
                "Không thể đặt lịch hẹn vào thời điểm trong quá khứ.\n" +
                "Vui lòng chọn lại ngày hoặc giờ khác.", 
                "Lỗi Thời gian", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Appointment newAppointment = new Appointment();
            newAppointment.setMemberId(selectedMember.getMemberId());
            newAppointment.setDoctorId(this.selectedDoctor.getDoctorId());
            newAppointment.setDepartmentId(this.selectedDepartment.getDepartmentId());
            newAppointment.setHospitalId(this.selectedHospital.getHospitalId());
            newAppointment.setAppointmentTime(appointmentTime);
            newAppointment.setChiefComplaint(reason);
            newAppointment.setAppointmentSlot(selectedSlot);

            int result = AppointmentDAO.getInstance().insert(newAppointment);

            if (result > 0) {
                 String successMessage = String.format(
                    "ĐẶT LỊCH THÀNH CÔNG\n\n" +
                    "Bệnh nhân: %s\n" +
                    "Ngày khám: %s\n" +
                    "Ca khám: %s\n" + 
                    "Lý do: %s\n\n" + 
                    "Bệnh viện: %s\n" +
                    "Khoa: %s\n" +
                    "Bác sĩ: %s",
                    selectedMember.getName(), 
                    date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                    selectedSlot,
                    reason.isEmpty() ? "(Không có lý do)" : reason, 
                    hospitalLabel.getText(),
                    departmentLabel.getText(),
                    selectedDoctorLabel.getText()
                );
                JOptionPane.showMessageDialog(this, 
                    successMessage,
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, "Đặt lịch thất bại. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi lưu vào CSDL: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void setPatientFormEnabled(boolean enabled) {
        memberComboBox.setEnabled(enabled);
        dateChooser.setEnabled(enabled);
        timeSlotComboBox.setEnabled(enabled);
        reasonArea.setEnabled(enabled);
        for (Component c : patientInfoPanel.getComponents()) {
            c.setEnabled(enabled);
        }
    }

    private void resetForm() {
        selectedHospital = null;
        selectedDepartment = null;
        selectedDoctor = null;
        setLabelNeutral(hospitalLabel, "[Chưa chọn]");
        setLabelNeutral(departmentLabel, "[Chưa chọn]");
        setLabelNeutral(locationLabel, "[Chưa chọn]");
        setLabelNeutral(selectedDoctorLabel, "[Chưa chọn]");
        if (memberComboBox.getItemCount() > 0) {
            memberComboBox.setSelectedIndex(0);
        }
        dateChooser.setDate(null);
        timeSlotComboBox.setSelectedIndex(0);
        reasonArea.setText("");
        setPatientFormEnabled(false);
        stepButton.setText("Bước 1: Chọn Bệnh viện");
        stepButton.setEnabled(true);
        confirmButton.setEnabled(false);
    }
    
    private JLabel createInfoLabel() {
        JLabel label = new JLabel("[...]");
        label.setFont(new Font("Arial", Font.ITALIC, 14));
        label.setForeground(Color.GRAY);
        return label;
    }
    private void setLabelSuccess(JLabel label, String text) {
        label.setText(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(0, 102, 0)); 
    }
    private void setLabelError(JLabel label, String text) {
        label.setText(text);
        label.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 14));
        label.setForeground(Color.RED);
    }
    private void setLabelNeutral(JLabel label, String text) {
        label.setText(text);
        label.setFont(new Font("Arial", Font.ITALIC, 14));
        label.setForeground(Color.GRAY);
    }
    
    private static class FamilyMemberRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                                                      int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof FamilyMembers) {
                FamilyMembers member = (FamilyMembers) value;
                setText(member.getName() + " (" + member.getRelationship().getDisplayName() + ")");
            }
            return this;
        }
    }

}
