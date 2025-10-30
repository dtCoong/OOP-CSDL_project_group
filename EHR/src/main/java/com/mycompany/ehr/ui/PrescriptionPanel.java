package com.mycompany.ehr.ui;

import com.mycompany.ehr.dao.PrescriptionDAO;
import com.mycompany.ehr.model.Prescription;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class PrescriptionPanel extends JPanel {

    
    private final PrescriptionDAO prescriptionDAO;

    
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtId;
    private JSpinner spinMemberId;
    private JSpinner spinAppointmentId;
    private JSpinner spinDoctorId;
    private JTextField txtPrescriptionDate;
    private JTextArea txtDiagnosis;
    private JTextArea txtNotes;
    private JSpinner spinTotalCost;
    private JComboBox<Prescription.PrescriptionStatus> cmbStatus;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;

    public PrescriptionPanel() {
        this.prescriptionDAO = new PrescriptionDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        addListeners();
        loadTableData();
    }

    private void initComponents() {
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Đơn thuốc"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtId = new JTextField(20);
        txtId.setEditable(false);
        formPanel.add(txtId, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(new JLabel("Ngày kê đơn (YYYY-MM-DD):"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtPrescriptionDate = new JTextField(10);
        formPanel.add(txtPrescriptionDate, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Member ID:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        spinMemberId = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        formPanel.add(spinMemberId, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Appointment ID:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        spinAppointmentId = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        formPanel.add(spinAppointmentId, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Doctor ID:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        spinDoctorId = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        formPanel.add(spinDoctorId, gbc);

        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Tổng chi phí:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        spinTotalCost = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1_000_000_000.0, 1000.0));
        formPanel.add(spinTotalCost, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cmbStatus = new JComboBox<>(Prescription.PrescriptionStatus.values());
        formPanel.add(cmbStatus, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0; gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Chẩn đoán:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0;
        txtDiagnosis = new JTextArea(3, 20);
        txtDiagnosis.setLineWrap(true);
        txtDiagnosis.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(txtDiagnosis), gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0; gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Ghi chú:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0;
        txtNotes = new JTextArea(3, 20);
        txtNotes.setLineWrap(true);
        txtNotes.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(txtNotes), gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 4; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER; gbc.weighty = 0;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new JButton("Thêm");
        btnUpdate = new JButton("Cập nhật");
        btnDelete = new JButton("Xóa");
        btnClear = new JButton("Làm mới");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        formPanel.add(buttonPanel, gbc);

       
        String[] columnNames = {
            "ID", "Member ID", "Appt ID", "Doctor ID", 
            "Ngày kê đơn", "Chẩn đoán", "Ghi chú", "Chi phí", "Trạng thái",
            "Ngày tạo", "Ngày cập nhật" // Thêm 2 cột
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table.getColumnModel().getColumn(0).setPreferredWidth(40);  // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(80);  // Member ID
        table.getColumnModel().getColumn(2).setPreferredWidth(60);  // Appt ID
        table.getColumnModel().getColumn(3).setPreferredWidth(70);  // Doctor ID
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Ngày
        table.getColumnModel().getColumn(5).setPreferredWidth(200); // Chẩn đoán
        table.getColumnModel().getColumn(6).setPreferredWidth(200); // Ghi chú
        table.getColumnModel().getColumn(7).setPreferredWidth(80);  // Chi phí
        table.getColumnModel().getColumn(8).setPreferredWidth(100); // Trạng thái
        table.getColumnModel().getColumn(9).setPreferredWidth(140); // Ngày tạo
        table.getColumnModel().getColumn(10).setPreferredWidth(140);// Ngày cập nhật
        
        JScrollPane scrollPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách Đơn thuốc"));
        scrollPane.setPreferredSize(new Dimension(800, 250));

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        ArrayList<Prescription> list = prescriptionDAO.selectAll();
        
        for (Prescription p : list) {
            Object[] row = {
                    p.getPrescriptionId(),
                    p.getMemberId(),
                    p.getAppointmentId(),
                    p.getDoctorId(),
                    p.getPrescriptionDate(),
                    p.getDiagnosis(),
                    p.getNotes(),
                    p.getTotalCost(),
                    p.getStatus(),
                    p.getCreatedAt(),   
                    p.getUpdatedAt()     
            };
            tableModel.addRow(row);
        }
    }

    private void addListeners() {
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                clearForm();
                return;
            }
            int modelRow = table.convertRowIndexToModel(selectedRow);
            int id = (int) tableModel.getValueAt(modelRow, 0); 
            
            Prescription p = new Prescription();
            p.setPrescriptionId(id);
            Prescription fullPrescription = prescriptionDAO.selectById(p);
            if (fullPrescription != null) {
                populateForm(fullPrescription);
            }
        });

        btnAdd.addActionListener(e -> addPrescription());
        btnUpdate.addActionListener(e -> updatePrescription());
        btnDelete.addActionListener(e -> deletePrescription());
        btnClear.addActionListener(e -> clearForm());
    }

    private void populateForm(Prescription p) {
        txtId.setText(String.valueOf(p.getPrescriptionId()));
        spinMemberId.setValue(p.getMemberId());
        
        spinAppointmentId.setValue(p.getAppointmentId() != null ? p.getAppointmentId() : 0);
        spinDoctorId.setValue(p.getDoctorId() != null ? p.getDoctorId() : 0);

        if (p.getPrescriptionDate() != null) {
            txtPrescriptionDate.setText(p.getPrescriptionDate().toString());
        } else {
            txtPrescriptionDate.setText("");
        }

        txtDiagnosis.setText(p.getDiagnosis());
        txtNotes.setText(p.getNotes());
        
        spinTotalCost.setValue(p.getTotalCost() != null ? p.getTotalCost().doubleValue() : 0.0);

        for (Prescription.PrescriptionStatus s : Prescription.PrescriptionStatus.values()) {
            if (s.getDisplayName().equals(p.getStatus())) {
                cmbStatus.setSelectedItem(s);
                break;
            }
        }
    }

    private Prescription getPrescriptionFromForm() throws DateTimeParseException {
        Prescription p = new Prescription();
        
        if (txtId.getText() != null && !txtId.getText().isEmpty()) {
            p.setPrescriptionId(Integer.parseInt(txtId.getText()));
        }

        p.setMemberId((Integer) spinMemberId.getValue());

        int apptId = (Integer) spinAppointmentId.getValue();
        p.setAppointmentId(apptId == 0 ? null : apptId);
        
        int docId = (Integer) spinDoctorId.getValue();
        p.setDoctorId(docId == 0 ? null : docId);

        p.setPrescriptionDate(LocalDate.parse(txtPrescriptionDate.getText()));

        p.setDiagnosis(txtDiagnosis.getText());
        p.setNotes(txtNotes.getText());

        double cost = (Double) spinTotalCost.getValue();
        p.setTotalCost(BigDecimal.valueOf(cost));

        Prescription.PrescriptionStatus selectedStatus = (Prescription.PrescriptionStatus) cmbStatus.getSelectedItem();
        p.setStatus(selectedStatus.name());

        return p;
    }

    private void clearForm() {
        txtId.setText("");
        spinMemberId.setValue(1);
        spinAppointmentId.setValue(0);
        spinDoctorId.setValue(0);
        txtPrescriptionDate.setText("");
        txtDiagnosis.setText("");
        txtNotes.setText("");
        spinTotalCost.setValue(0.0);
        cmbStatus.setSelectedIndex(0);
        table.clearSelection();
    }
    
    private boolean validateDate() {
        try {
            LocalDate.parse(txtPrescriptionDate.getText());
            return true;
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Ngày kê đơn không hợp lệ. Vui lòng nhập theo định dạng YYYY-MM-DD.",
                    "Lỗi định dạng ngày",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void addPrescription() {
        if (!validateDate()) return;
        
        try {
            Prescription p = getPrescriptionFromForm();
            
            int rowsAffected = prescriptionDAO.insert(p); 
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Thêm đơn thuốc thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
                clearForm();
            } else {
                
                JOptionPane.showMessageDialog(this, "Thêm đơn thuốc thất bại. (DAO trả về 0 dòng)", "Lỗi Logic/CSDL", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (DateTimeParseException e) {
             JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày tháng khi lấy dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm đơn thuốc: " + e.getMessage(), "Lỗi Exception", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updatePrescription() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một đơn thuốc để cập nhật.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateDate()) return;
        
        try {
            Prescription p = getPrescriptionFromForm();
            
            int rowsAffected = prescriptionDAO.update(p); 
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật đơn thuốc thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại. (DAO trả về 0 dòng)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (DateTimeParseException e) {
             JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày tháng khi lấy dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật đơn thuốc: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deletePrescription() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một đơn thuốc để xóa.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Xóa đơn thuốc sẽ xóa TẤT CẢ chi tiết và lịch uống thuốc liên quan (do CASCADE). Bạn có chắc không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Prescription p = new Prescription();
                p.setPrescriptionId(Integer.parseInt(txtId.getText()));
                
                int rowsAffected = prescriptionDAO.delete(p);
                
                if(rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Xóa đơn thuốc thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    loadTableData();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại. (DAO trả về 0 dòng)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa đơn thuốc: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}