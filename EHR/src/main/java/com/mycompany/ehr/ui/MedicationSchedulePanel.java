package com.mycompany.ehr.ui;

import com.mycompany.ehr.dao.MedicationScheduleDAO;
import com.mycompany.ehr.model.MedicationSchedule;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MedicationSchedulePanel extends JPanel {

    
    private final MedicationScheduleDAO scheduleDAO;

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtId;
    private JSpinner spinDetailId;
    private JSpinner timeSpinner; 
    private JTextField txtDailyDosage;
    private JCheckBox chkIsActive;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;

    public MedicationSchedulePanel() {
        this.scheduleDAO = new MedicationScheduleDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        addListeners();
        loadTableData();
    }

    private void initComponents() {
       
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Lịch uống thuốc"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Schedule ID:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtId = new JTextField(15);
        txtId.setEditable(false);
        formPanel.add(txtId, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(new JLabel("Detail ID:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        spinDetailId = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        formPanel.add(spinDetailId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Giờ uống (HH:mm):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        SpinnerDateModel timeModel = new SpinnerDateModel(new Date(), null, null, Calendar.HOUR_OF_DAY);
        timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        formPanel.add(timeSpinner, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Liều lượng/ngày:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtDailyDosage = new JTextField(15);
        formPanel.add(txtDailyDosage, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Đang hoạt động:"), gbc);
        gbc.gridx = 1;
        chkIsActive = new JCheckBox();
        chkIsActive.setSelected(true); // Default
        formPanel.add(chkIsActive, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4; gbc.fill = GridBagConstraints.NONE;
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
            "Schedule ID", "Detail ID", "Giờ uống", 
            "Liều lượng/ngày", "Hoạt động", "Ngày tạo"
        };
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(80);  // Schedule ID
        table.getColumnModel().getColumn(1).setPreferredWidth(80);  // Detail ID
        table.getColumnModel().getColumn(2).setPreferredWidth(80);  // Giờ uống
        table.getColumnModel().getColumn(3).setPreferredWidth(120); // Liều lượng
        table.getColumnModel().getColumn(4).setPreferredWidth(80);  // Hoạt động
        table.getColumnModel().getColumn(5).setPreferredWidth(140); // Ngày tạo
        
        JScrollPane scrollPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
                
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách Lịch uống thuốc"));
        scrollPane.setPreferredSize(new Dimension(800, 250)); 

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        ArrayList<MedicationSchedule> list = scheduleDAO.selectAll();
        
        for (MedicationSchedule s : list) {
            Object[] row = {
                    s.getScheduleId(),
                    s.getDetailId(),
                    s.getScheduledTime(), 
                    s.getDailyDosage(),
                    s.isActive(),
                    s.getCreatedAt()
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
            
            MedicationSchedule s = new MedicationSchedule();
            s.setScheduleId(id);
            MedicationSchedule fullSchedule = scheduleDAO.selectById(s);
            if (fullSchedule != null) {
                populateForm(fullSchedule);
            }
        });

        btnAdd.addActionListener(e -> addSchedule());
        btnUpdate.addActionListener(e -> updateSchedule());
        btnDelete.addActionListener(e -> deleteSchedule());
        btnClear.addActionListener(e -> clearForm());
    }

    private void populateForm(MedicationSchedule s) {
        txtId.setText(String.valueOf(s.getScheduleId()));
        spinDetailId.setValue(s.getDetailId());
        txtDailyDosage.setText(s.getDailyDosage());
        chkIsActive.setSelected(s.isActive());

        if (s.getScheduledTime() != null) {
            Date time = Date.from(s.getScheduledTime().atDate(LocalDate.now())
                    .atZone(ZoneId.systemDefault()).toInstant());
            timeSpinner.setValue(time);
        }
    }

    private MedicationSchedule getScheduleFromForm() {
        MedicationSchedule s = new MedicationSchedule();
        
        if (txtId.getText() != null && !txtId.getText().isEmpty()) {
            s.setScheduleId(Integer.parseInt(txtId.getText()));
        }

        s.setDetailId((Integer) spinDetailId.getValue());
        s.setDailyDosage(txtDailyDosage.getText());
        s.setActive(chkIsActive.isSelected());

        Date time = (Date) timeSpinner.getValue();
        s.setScheduledTime(time.toInstant().atZone(ZoneId.systemDefault()).toLocalTime());

        return s;
    }

    private void clearForm() {
        txtId.setText("");
        spinDetailId.setValue(1);
        txtDailyDosage.setText("");
        chkIsActive.setSelected(true);
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 0);
        timeSpinner.setValue(cal.getTime());
        table.clearSelection();
    }

    private void addSchedule() {
        if (txtDailyDosage.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Liều lượng/ngày không được để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            MedicationSchedule s = getScheduleFromForm();
            
            int rowsAffected = scheduleDAO.insert(s); // Sửa: Kiểm tra kết quả
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Thêm lịch thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm lịch thất bại. (DAO trả về 0 dòng)", "Lỗi Logic/CSDL", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm lịch: " + e.getMessage(), "Lỗi Exception", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    private void updateSchedule() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một lịch để cập nhật.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (txtDailyDosage.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Liều lượng/ngày không được để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            MedicationSchedule s = getScheduleFromForm();
            
            int rowsAffected = scheduleDAO.update(s); // Sửa: Kiểm tra kết quả
            
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật lịch thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại. (DAO trả về 0 dòng)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật lịch: " + e.getMessage(), "Lỗi Exception", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deleteSchedule() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một lịch để xóa.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa lịch này không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                MedicationSchedule s = new MedicationSchedule();
                s.setScheduleId(Integer.parseInt(txtId.getText()));
                
                int rowsAffected = scheduleDAO.delete(s); 
                
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Xóa lịch thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    loadTableData();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại. (DAO trả về 0 dòng)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa lịch: " + e.getMessage(), "Lỗi Exception", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}