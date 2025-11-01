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

    private JTextField txtSearchId;
    private JButton btnSearch;
    private JButton btnShowAll;

    public MedicationPanel() {
        this.medicationDAO = new MedicationDAO();
        
        setLayout(new BorderLayout(10, 10)); 
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        addListeners();
        loadTableData();
    }

    private void initComponents() {
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        searchPanel.add(new JLabel("Tìm theo ID thuốc:"));
        txtSearchId = new JTextField(10);
        btnSearch = new JButton("Tìm");
        btnShowAll = new JButton("Hiện tất cả");
        
        searchPanel.add(txtSearchId);
        searchPanel.add(btnSearch);
        searchPanel.add(btnShowAll);

        String[] columnNames = {
            "ID", "Tên thuốc", "Tên gốc", "Mô tả", "Tác dụng phụ",
            "Chống chỉ định", "Tương tác thuốc", "Nhà SX", "Đơn vị", 
            "Kê đơn?", "Barcode"
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
        
        table.getColumnModel().getColumn(0).setPreferredWidth(40);  
        table.getColumnModel().getColumn(1).setPreferredWidth(150); 
        table.getColumnModel().getColumn(2).setPreferredWidth(150); 
        table.getColumnModel().getColumn(3).setPreferredWidth(200); 
        table.getColumnModel().getColumn(4).setPreferredWidth(200); 
        table.getColumnModel().getColumn(5).setPreferredWidth(200); 
        table.getColumnModel().getColumn(6).setPreferredWidth(200); 
        table.getColumnModel().getColumn(7).setPreferredWidth(100); 
        table.getColumnModel().getColumn(8).setPreferredWidth(60);  
        table.getColumnModel().getColumn(9).setPreferredWidth(60);  
        table.getColumnModel().getColumn(10).setPreferredWidth(100); 

        JScrollPane scrollPane = new JScrollPane(table, 
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách Thuốc"));

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadTableData() {
        tableModel.setRowCount(0); 
        
        ArrayList<Medication> list = medicationDAO.selectAll();
        for (Medication med : list) {
            addMedicationToTable(med);
        }
    }

    private void addMedicationToTable(Medication med) {
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
    
    private void addListeners() {
        btnSearch.addActionListener(e -> searchById());
        
        btnShowAll.addActionListener(e -> {
            txtSearchId.setText("");
            loadTableData();
        });
    }

    private void searchById() {
        String idText = txtSearchId.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập ID để tìm kiếm.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            
            Medication dummyMed = new Medication();
            dummyMed.setMedicationId(id);
            
            Medication med = medicationDAO.selectById(dummyMed);
            
            tableModel.setRowCount(0); 
            
            if (med != null) {
                addMedicationToTable(med);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thuốc với ID: " + id, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID phải là một con số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}