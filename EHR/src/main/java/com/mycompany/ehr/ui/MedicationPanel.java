package com.mycompany.ehr.ui;

import com.mycompany.ehr.dao.MedicationDAO;
import com.mycompany.ehr.model.Medication;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;


public class MedicationPanel extends JPanel {

    private final MedicationDAO medicationDAO;

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtGenericName;
    private JTextField txtManufacturer;
    private JTextField txtBarcode;
    private JComboBox<Medication.Unit> cmbUnit;
    private JCheckBox chkRequiresPrescription;
    private JTextArea txtDescription;
    private JTextArea txtSideEffects;
    private JTextArea txtContraindications;
    private JTextArea txtInteractions;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;

    public MedicationPanel() {
        this.medicationDAO = new MedicationDAO();
        setLayout(new BorderLayout(10, 10)); 
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        addListeners();
        loadTableData();
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Thuốc"));

        JPanel formFieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formFieldsPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtId = new JTextField(15);
        txtId.setEditable(false);
        formFieldsPanel.add(txtId, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        formFieldsPanel.add(new JLabel("Barcode:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtBarcode = new JTextField(15);
        formFieldsPanel.add(txtBarcode, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formFieldsPanel.add(new JLabel("Tên thuốc:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        txtName = new JTextField(30);
        formFieldsPanel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formFieldsPanel.add(new JLabel("Tên gốc:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        txtGenericName = new JTextField(30);
        formFieldsPanel.add(txtGenericName, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; gbc.gridwidth = 1;
        formFieldsPanel.add(new JLabel("Nhà SX:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtManufacturer = new JTextField(15);
        formFieldsPanel.add(txtManufacturer, gbc);

        gbc.gridx = 2; gbc.gridy = 3; gbc.weightx = 0;
        formFieldsPanel.add(new JLabel("Đơn vị:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        cmbUnit = new JComboBox<>(Medication.Unit.values());
        formFieldsPanel.add(cmbUnit, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        formFieldsPanel.add(new JLabel("Cần kê đơn:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        chkRequiresPrescription = new JCheckBox();
        chkRequiresPrescription.setSelected(true);
        formFieldsPanel.add(chkRequiresPrescription, gbc);

        JPanel textAreasPanel = new JPanel(new GridLayout(2, 2, 10, 10)); 
        txtDescription = new JTextArea(4, 20);
        txtSideEffects = new JTextArea(4, 20);
        txtContraindications = new JTextArea(4, 20);
        txtInteractions = new JTextArea(4, 20);
        
        textAreasPanel.add(createTitledScrollPane("Mô tả", txtDescription));
        textAreasPanel.add(createTitledScrollPane("Tác dụng phụ", txtSideEffects));
        textAreasPanel.add(createTitledScrollPane("Chống chỉ định", txtContraindications));
        textAreasPanel.add(createTitledScrollPane("Tương tác thuốc", txtInteractions));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAdd = new JButton("Thêm");
        btnUpdate = new JButton("Cập nhật");
        btnDelete = new JButton("Xóa");
        btnClear = new JButton("Làm mới");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        formPanel.add(formFieldsPanel, BorderLayout.NORTH);
        formPanel.add(textAreasPanel, BorderLayout.CENTER);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);

        String[] columnNames = {
            "ID", 
            "Tên thuốc", 
            "Tên gốc", 
            "Mô tả", 
            "Tác dụng phụ",
            "Chống chỉ định",
            "Tương tác thuốc",
            "Nhà SX", 
            "Đơn vị", 
            "Kê đơn?", 
            "Barcode"
        };
        

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
        
        table.getColumnModel().getColumn(0).setPreferredWidth(40);  // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Tên thuốc
        table.getColumnModel().getColumn(2).setPreferredWidth(150); // Tên gốc
        table.getColumnModel().getColumn(3).setPreferredWidth(200); // Mô tả
        table.getColumnModel().getColumn(4).setPreferredWidth(200); // Tác dụng phụ
        table.getColumnModel().getColumn(5).setPreferredWidth(200); // Chống chỉ định
        table.getColumnModel().getColumn(6).setPreferredWidth(200); // Tương tác
        table.getColumnModel().getColumn(7).setPreferredWidth(100); // Nhà SX
        table.getColumnModel().getColumn(8).setPreferredWidth(60);  // Đơn vị
        table.getColumnModel().getColumn(9).setPreferredWidth(60);  // Kê đơn
        table.getColumnModel().getColumn(10).setPreferredWidth(100); // Barcode

        JScrollPane scrollPane = new JScrollPane(table, 
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách Thuốc"));
        scrollPane.setPreferredSize(new Dimension(800, 250));

        add(formPanel, BorderLayout.NORTH); 
        add(scrollPane, BorderLayout.CENTER);
    }

    
    private JScrollPane createTitledScrollPane(String title, JTextArea textArea) {
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(title));
        return scrollPane;
    }

  
    private void loadTableData() {
        tableModel.setRowCount(0);
        ArrayList<Medication> list = medicationDAO.selectAll();

        for (Medication med : list) {
            Object[] row = {
                    med.getMedicationId(),
                    med.getName(),
                    med.getGenericName(),
                    med.getDescription(),         
                    med.getSideEffects(),         
                    med.getContraindications(),   
                    med.getInteractions(),        
                    med.getManufacturer(),
                    med.getUnit(),
                    med.isRequiresPrescription(),
                    med.getBarcode()
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
            
            Medication dummyMed = new Medication();
            dummyMed.setMedicationId(id);
            
            Medication med = medicationDAO.selectById(dummyMed);
            if (med != null) {
                populateForm(med);
            }
        });

        btnAdd.addActionListener(e -> addMedication());
        btnUpdate.addActionListener(e -> updateMedication());
        btnDelete.addActionListener(e -> deleteMedication());
        btnClear.addActionListener(e -> clearForm());
    }

    
    private void populateForm(Medication med) {
        txtId.setText(String.valueOf(med.getMedicationId()));
        txtName.setText(med.getName());
        txtGenericName.setText(med.getGenericName());
        txtManufacturer.setText(med.getManufacturer());
        txtBarcode.setText(med.getBarcode());
        chkRequiresPrescription.setSelected(med.isRequiresPrescription());

        txtDescription.setText(med.getDescription());
        txtSideEffects.setText(med.getSideEffects());
        txtContraindications.setText(med.getContraindications());
        txtInteractions.setText(med.getInteractions());

        for (Medication.Unit u : Medication.Unit.values()) {
            if (u.getDisplayName().equals(med.getUnit())) {
                cmbUnit.setSelectedItem(u);
                break;
            }
        }
    }

    
    private Medication getMedicationFromForm() {
        Medication med = new Medication();

        if (txtId.getText() != null && !txtId.getText().isEmpty()) {
            med.setMedicationId(Integer.parseInt(txtId.getText()));
        }
        
        med.setName(txtName.getText());
        med.setGenericName(txtGenericName.getText());
        med.setDescription(txtDescription.getText());
        med.setSideEffects(txtSideEffects.getText());
        med.setContraindications(txtContraindications.getText());
        med.setInteractions(txtInteractions.getText());
        med.setManufacturer(txtManufacturer.getText());
        med.setRequiresPrescription(chkRequiresPrescription.isSelected());
        med.setBarcode(txtBarcode.getText());
        
        Medication.Unit selectedUnit = (Medication.Unit) cmbUnit.getSelectedItem();
        med.setUnit(selectedUnit.name());
        
        return med;
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtGenericName.setText("");
        txtManufacturer.setText("");
        txtBarcode.setText("");
        chkRequiresPrescription.setSelected(true);
        
        txtDescription.setText("");
        txtSideEffects.setText("");
        txtContraindications.setText("");
        txtInteractions.setText("");
        
        cmbUnit.setSelectedIndex(0); 
        table.clearSelection();
    }

    private void addMedication() {
        if (txtName.getText().trim().isEmpty()) {
             JOptionPane.showMessageDialog(this, "Tên thuốc không được để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Medication med = getMedicationFromForm();
            medicationDAO.insert(med);
            JOptionPane.showMessageDialog(this, "Thêm thuốc thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            loadTableData();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm thuốc: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updateMedication() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một loại thuốc để cập nhật.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
         if (txtName.getText().trim().isEmpty()) {
             JOptionPane.showMessageDialog(this, "Tên thuốc không được để trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Medication med = getMedicationFromForm();
            medicationDAO.update(med);
            JOptionPane.showMessageDialog(this, "Cập nhật thuốc thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            loadTableData();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật thuốc: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deleteMedication() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một loại thuốc để xóa.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa loại thuốc này không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Medication med = new Medication();
                med.setMedicationId(Integer.parseInt(txtId.getText()));
                
                medicationDAO.delete(med);
                JOptionPane.showMessageDialog(this, "Xóa thuốc thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadTableData();
                clearForm();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa thuốc: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}