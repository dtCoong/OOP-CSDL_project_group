package com.mycompany.ehr.npd.gui;

import com.mycompany.ehr.npd.dao.DepartmentDAO;
import com.mycompany.ehr.npd.model.Department;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.sql.SQLException; 
import java.util.ArrayList;
import java.util.List;

/**
 * Giao diện JDialog để Bệnh nhân LỌC và CHỌN một Khoa (Department)
 */
public class DepartmentSelectionGUI extends JDialog {

    // GUI Components
    private JTable departmentTable;
    private DepartmentTableModel tableModel;
    private JButton chooseButton;
    private JButton cancelButton;

    // Data
    private DepartmentDAO departmentDAO;
    private Department selectedDepartment = null;
    private int hospitalId; 

    /**
     * Constructor
     */
    public DepartmentSelectionGUI(Frame owner, int hospitalId) {
        super(owner, "Bước 2: Chọn Khoa", true); 
        this.departmentDAO = DepartmentDAO.getInstance();
        this.hospitalId = hospitalId; 

        setupUI();
        loadData(); 
    }

    private void setupUI() {
        setSize(700, 500);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));

        // Bảng (Trung tâm - CENTER)
        tableModel = new DepartmentTableModel(new ArrayList<>());
        departmentTable = new JTable(tableModel);
        departmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        departmentTable.setFont(new Font("Arial", Font.PLAIN, 14));
        departmentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(departmentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Panel Nút bấm (Phía dưới - SOUTH)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        chooseButton = new JButton("Chọn Khoa này");
        chooseButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        cancelButton = new JButton("Hủy");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));

        buttonPanel.add(cancelButton);
        buttonPanel.add(chooseButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        chooseButton.addActionListener(e -> onChoose());
        cancelButton.addActionListener(e -> onCancel());
    }

    /**
     * Tải dữ liệu từ DAO (ĐÃ LỌC) lên bảng
     */
    private void loadData() {
        try {
            // LỌC THEO HOSPITAL ID
            String condition = "hospital_id = " + this.hospitalId;
            ArrayList<Department> departments = departmentDAO.selectByCondition(condition);
            tableModel.updateData(departments);
            if (departments.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Không tìm thấy khoa nào cho bệnh viện này.", 
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải danh sách khoa: " + e.getMessage(),
                    "Lỗi Database",
                    JOptionPane.ERROR_MESSAGE);
            dispose(); // Đóng cửa sổ nếu lỗi
        }
    }

    private void onChoose() {
        int viewRow = departmentTable.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một khoa.", "Chưa chọn", JOptionPane.WARNING_MESSAGE);
            return;
        }

        this.selectedDepartment = tableModel.getDepartmentAt(viewRow);
        dispose();
    }

    private void onCancel() {
        this.selectedDepartment = null;
        dispose();
    }

    public Department getSelectedDepartment() {
        return this.selectedDepartment;
    }

    // LỚP LỒNG NHAU (INNER CLASS) CHO TABLE MODEL

    private class DepartmentTableModel extends AbstractTableModel {

        private List<Department> departments;
        private final String[] columnNames = {"ID Khoa", "Tên Khoa", "Địa điểm chi tiết"};

        public DepartmentTableModel(List<Department> departments) {
            this.departments = departments;
        }

        public void updateData(List<Department> newDepartments) {
            this.departments = newDepartments;
            fireTableDataChanged();
        }

        public Department getDepartmentAt(int rowIndex) {
            return departments.get(rowIndex);
        }

        @Override public int getRowCount() { return departments.size(); }
        @Override public int getColumnCount() { return columnNames.length; }
        @Override public String getColumnName(int column) { return columnNames.length > column ? columnNames[column] : ""; }


        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Department dept = departments.get(rowIndex);
            switch (columnIndex) {
                case 0: return dept.getDepartmentId();
                case 1: return dept.getName();
                case 2: return dept.getLocationDetails();
                default: return null;
            }
        }
    }
}
