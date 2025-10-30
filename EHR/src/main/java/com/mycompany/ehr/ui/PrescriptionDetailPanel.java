package com.mycompany.ehr.ui;

import com.mycompany.ehr.dao.PrescriptionDetailDAO;
import com.mycompany.ehr.model.PrescriptionDetail;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class PrescriptionDetailPanel extends JPanel {

    private final PrescriptionDetailDAO detailDAO;

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtId;
    private JSpinner spinPrescriptionId;
    private JSpinner spinMedicationId;
    private JTextField txtDosage;
    private JTextField txtFrequency;
    private JSpinner spinDuration;
    private JSpinner spinQuantity;
    private JTextArea txtInstructions;
    private JTextField txtDateStart; 
    private JTextField txtDateEnd;
    private JComboBox<PrescriptionDetail.DetailStatus> cmbStatus;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;

    public PrescriptionDetailPanel() {
        this.detailDAO = new PrescriptionDetailDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        addListeners();
        loadTableData();
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Chi tiết Đơn thuốc"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Detail ID:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtId = new JTextField(15);
        txtId.setEditable(false);
        formPanel.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Prescription ID:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        spinPrescriptionId = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        formPanel.add(spinPrescriptionId, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Medication ID:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        spinMedicationId = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        formPanel.add(spinMedicationId, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Liều lượng (Dosage):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtDosage = new JTextField(15);
        formPanel.add(txtDosage, gbc);

        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Tần suất (Frequency):"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtFrequency = new JTextField(15);
        formPanel.add(txtFrequency, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(new JLabel("Số ngày dùng:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        spinDuration = new JSpinner(new SpinnerNumberModel(1, 1, 365, 1));
        formPanel.add(spinDuration, gbc);

        gbc.gridx = 2; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(new JLabel("Tổng số lượng:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        spinQuantity = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        formPanel.add(spinQuantity, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        formPanel.add(new JLabel("Ngày BĐ (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtDateStart = new JTextField(10);
        formPanel.add(txtDateStart, gbc);

        gbc.gridx = 2; gbc.gridy = 4; gbc.weightx = 0;
        formPanel.add(new JLabel("Ngày KT (YYYY-MM-DD):"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtDateEnd = new JTextField(10);
        formPanel.add(txtDateEnd, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        formPanel.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cmbStatus = new JComboBox<>(PrescriptionDetail.DetailStatus.values());
        formPanel.add(cmbStatus, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0; gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Hướng dẫn:"), gbc);
        gbc.gridx = 1; gbc.gridy = 6; gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0;
        txtInstructions = new JTextArea(3, 20);
        txtInstructions.setLineWrap(true);
        txtInstructions.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(txtInstructions), gbc);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 4; gbc.fill = GridBagConstraints.NONE;
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
            "Detail ID", "Presc. ID", "Med. ID", 
            "Liều lượng", "Tần suất", "Số ngày", "Tổng SL",
            "Hướng dẫn", "Bắt đầu", "Kết thúc", "Trạng thái", "Ngày tạo"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(60);  // Detail ID
        table.getColumnModel().getColumn(1).setPreferredWidth(60);  // Presc. ID
        table.getColumnModel().getColumn(2).setPreferredWidth(60);  // Med. ID
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Liều lượng
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Tần suất
        table.getColumnModel().getColumn(5).setPreferredWidth(60);  // Số ngày
        table.getColumnModel().getColumn(6).setPreferredWidth(60);  // Tổng SL
        table.getColumnModel().getColumn(7).setPreferredWidth(200); // Hướng dẫn
        table.getColumnModel().getColumn(8).setPreferredWidth(100); // Bắt đầu
        table.getColumnModel().getColumn(9).setPreferredWidth(100); // Kết thúc
        table.getColumnModel().getColumn(10).setPreferredWidth(100); // Trạng thái
        table.getColumnModel().getColumn(11).setPreferredWidth(140); // Ngày tạo
        
        JScrollPane scrollPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
                
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách Chi tiết Đơn thuốc"));
        scrollPane.setPreferredSize(new Dimension(800, 250)); 

        // --- 3. Thêm các panel con vào panel chính ---
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        ArrayList<PrescriptionDetail> list = detailDAO.selectAll();
        
        for (PrescriptionDetail d : list) {
            Object[] row = {
                    d.getDetailId(),
                    d.getPrescriptionId(),
                    d.getMedicationId(),
                    d.getDosage(),
                    d.getFrequency(),
                    d.getDurationDays(),   
                    d.getTotalQuantity(),   
                    d.getInstructions(),    
                    d.getStartDate(),
                    d.getEndDate(),
                    d.getStatus(),
                    d.getCreatedAt()        
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
            
            PrescriptionDetail d = new PrescriptionDetail();
            d.setDetailId(id);
            PrescriptionDetail fullDetail = detailDAO.selectById(d);
            if (fullDetail != null) {
                populateForm(fullDetail);
            }
        });

        // Các nút bấm
        btnAdd.addActionListener(e -> addDetail());
        btnUpdate.addActionListener(e -> updateDetail());
        btnDelete.addActionListener(e -> deleteDetail());
        btnClear.addActionListener(e -> clearForm());
    }

    private void populateForm(PrescriptionDetail d) {
        txtId.setText(String.valueOf(d.getDetailId()));
        spinPrescriptionId.setValue(d.getPrescriptionId());
        spinMedicationId.setValue(d.getMedicationId());
        txtDosage.setText(d.getDosage());
        txtFrequency.setText(d.getFrequency());
        spinDuration.setValue(d.getDurationDays());
        spinQuantity.setValue(d.getTotalQuantity());
        txtInstructions.setText(d.getInstructions());

        txtDateStart.setText(d.getStartDate().toString());
        txtDateEnd.setText(d.getEndDate().toString());

        for (PrescriptionDetail.DetailStatus s : PrescriptionDetail.DetailStatus.values()) {
            if (s.getDisplayName().equals(d.getStatus())) {
                cmbStatus.setSelectedItem(s);
                break;
            }
        }
    }

    private PrescriptionDetail getDetailFromForm() throws DateTimeParseException {
        PrescriptionDetail d = new PrescriptionDetail();
        
        if (txtId.getText() != null && !txtId.getText().isEmpty()) {
            d.setDetailId(Integer.parseInt(txtId.getText()));
        }

        d.setPrescriptionId((Integer) spinPrescriptionId.getValue());
        d.setMedicationId((Integer) spinMedicationId.getValue());
        d.setDosage(txtDosage.getText());
        d.setFrequency(txtFrequency.getText());
        d.setDurationDays((Integer) spinDuration.getValue());
        d.setTotalQuantity((Integer) spinQuantity.getValue());
        d.setInstructions(txtInstructions.getText());

        d.setStartDate(LocalDate.parse(txtDateStart.getText()));
        d.setEndDate(LocalDate.parse(txtDateEnd.getText()));

        PrescriptionDetail.DetailStatus selectedStatus = (PrescriptionDetail.DetailStatus) cmbStatus.getSelectedItem();
        d.setStatus(selectedStatus.name());

        return d;
    }

    private void clearForm() {
        txtId.setText("");
        spinPrescriptionId.setValue(1);
        spinMedicationId.setValue(1);
        txtDosage.setText("");
        txtFrequency.setText("");
        spinDuration.setValue(1);
        spinQuantity.setValue(1);
        txtInstructions.setText("");
        txtDateStart.setText("");
        txtDateEnd.setText("");
        cmbStatus.setSelectedIndex(0);
        table.clearSelection();
    }

    private boolean validateDates() {
        try {
            LocalDate start = LocalDate.parse(txtDateStart.getText());
            LocalDate end = LocalDate.parse(txtDateEnd.getText());
            
            if (end.isBefore(start)) {
                JOptionPane.showMessageDialog(this, "Ngày kết thúc không thể trước ngày bắt đầu.", "Lỗi logic", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Ngày bắt đầu hoặc kết thúc không hợp lệ. Vui lòng nhập theo định dạng YYYY-MM-DD.",
                    "Lỗi định dạng ngày",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void addDetail() {
        if (!validateDates()) return;
        try {
            PrescriptionDetail d = getDetailFromForm();
            
            int rowsAffected = detailDAO.insert(d); 
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Thêm chi tiết thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm chi tiết thất bại. (DAO trả về 0 dòng)", "Lỗi Logic/CSDL", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày tháng khi lấy dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm chi tiết: " + e.getMessage(), "Lỗi Exception", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updateDetail() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một chi tiết để cập nhật.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateDates()) return;
        try {
            PrescriptionDetail d = getDetailFromForm();
            
            int rowsAffected = detailDAO.update(d);
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật chi tiết thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
                clearForm();
            } else {
                 JOptionPane.showMessageDialog(this, "Cập nhật thất bại. (DAO trả về 0 dòng)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày tháng khi lấy dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật chi tiết: " + e.getMessage(), "Lỗi Exception", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deleteDetail() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một chi tiết để xóa.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa chi tiết này không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                PrescriptionDetail d = new PrescriptionDetail();
                d.setDetailId(Integer.parseInt(txtId.getText()));
                
                int rowsAffected = detailDAO.delete(d); 
                
                if(rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Xóa chi tiết thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    loadTableData();
                    clearForm();
                } else {
                     JOptionPane.showMessageDialog(this, "Xóa thất bại. (DAO trả về 0 dòng)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa chi tiết: " + e.getMessage(), "Lỗi Exception", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}