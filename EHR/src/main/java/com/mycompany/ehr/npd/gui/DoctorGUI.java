package com.mycompany.ehr.npd.gui;

import com.mycompany.ehr.npd.dao.DoctorDAO;
import com.mycompany.ehr.npd.model.Doctor;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Giao diện JDialog để CHỌN một bác sĩ từ danh sách.
 * Chỉ cho phép xem và chọn, không Thêm/Sửa/Xóa.
 */
public class DoctorGUI extends JDialog {

    // --- GUI Components ---
    private JTable doctorTable;
    private DoctorTableModel tableModel;
    private JButton chooseButton;
    private JButton cancelButton;
    private JButton refreshButton;

    // --- Data ---
    private DoctorDAO doctorDAO;
    private Doctor selectedDoctor = null;
    private int departmentId; // ID của khoa để lọc

    /**
     * Constructor
     * @param owner Frame cha (cửa sổ gọi nó)
     */
    public DoctorGUI(Frame owner, int departmentId) {
        super(owner, "Bước 3: Chọn Bác sĩ", true); 
        this.doctorDAO = DoctorDAO.getInstance();
        this.departmentId = departmentId; // Lưu lại ID khoa

        setupUI();
        loadData(); // Tải dữ liệu đã lọc
    }

    private void setupUI() {
        setSize(700, 500);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));
        tableModel = new DoctorTableModel(new ArrayList<>());
        doctorTable = new JTable(tableModel);
        doctorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        doctorTable.setFont(new Font("Arial", Font.PLAIN, 14));
        doctorTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(doctorTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(scrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        chooseButton = new JButton("Chọn");
        chooseButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton = new JButton("Hủy");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        refreshButton = new JButton("Tải lại danh sách");
        refreshButton.setFont(new Font("Arial", Font.PLAIN, 14));
        chooseButton.addActionListener(e -> onChoose());
        cancelButton.addActionListener(e -> onCancel());
        refreshButton.addActionListener(e -> loadData());
        buttonPanel.add(refreshButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(chooseButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Xử lý khi nhấn nút "Chọn"
     */
    private void onChoose() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một bác sĩ.", "Chưa chọn", JOptionPane.WARNING_MESSAGE);
            return;
        }
        this.selectedDoctor = tableModel.getDoctorAt(selectedRow);
        dispose();
    }

    /**
     * Xử lý khi nhấn nút "Hủy"
     */
    private void onCancel() {
        this.selectedDoctor = null;
        dispose();
    }

    /**
     * Tải dữ liệu từ DAO lên bảng
     */
    private void loadData() {
        try {
            ArrayList<Doctor> doctors;
            
            // *** ĐIỂM QUAN TRỌNG: LỌC THEO DEPARTMENT ID ***
            if (this.departmentId <= 0) {
                 // Fallback nếu không có ID khoa
                doctors = doctorDAO.selectAll();
            } else {
                String condition = "department_id = " + this.departmentId;
                // CẢNH BÁO: Tương tự, nên dùng PreparedStatement
                doctors = doctorDAO.selectByCondition(condition);
            }
            
            tableModel.updateData(doctors);

                if (doctors.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Không tìm thấy bác sĩ nào cho khoa này.", 
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải danh sách bác sĩ: " + e.getMessage(),
                    "Lỗi Database",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Phương thức public để cửa sổ cha có thể lấy Bác sĩ đã được chọn
     * (Sau khi cửa sổ này đã đóng)
     * @return Bác sĩ đã chọn, hoặc null nếu nhấn "Hủy".
     */
    public Doctor getSelectedDoctor() {
        return this.selectedDoctor;
    }

    // =========================================================================
    // LỚP LỒNG NHAU (INNER CLASS) CHO TABLE MODEL
    // =========================================================================
    private class DoctorTableModel extends AbstractTableModel {
        private List<Doctor> doctors;
        private final String[] columnNames = {"ID", "Họ Tên", "Học vị", "Kinh nghiệm (năm)", "Email"};
        public DoctorTableModel(List<Doctor> doctors) { this.doctors = doctors; }
        public void updateData(List<Doctor> newDoctors) {
            this.doctors = newDoctors;
            fireTableDataChanged();
        }
        public Doctor getDoctorAt(int rowIndex) {
            if (rowIndex >= 0 && rowIndex < doctors.size()) return doctors.get(rowIndex);
            return null;
        }
        @Override public int getRowCount() { return doctors.size(); }
        @Override public int getColumnCount() { return columnNames.length; }
        @Override public String getColumnName(int column) { return columnNames[column]; }
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Doctor doctor = doctors.get(rowIndex);
            switch (columnIndex) {
                case 0: return doctor.getDoctorId();
                case 1: return doctor.getFullName();
                case 2: return (doctor.getDegree() != null) ? doctor.getDegree().getDisplayName() : "N/A";
                case 3: return doctor.getExperienceYears();
                case 4: return doctor.getemail();
                default: return null;
            }
        }
    }

    // =========================================================================
    // PHƯƠNG THỨC MAIN ĐỂ TEST
    // =========================================================================
    /**
     * Đây là main method ví dụ cách SỬ DỤNG JDialog này.
     * Bạn có thể chạy file này để xem cửa sổ chọn.
     */
    public static void main(String[] args) {
        // Giả lập một Frame cha
        JFrame testFrame = new JFrame();
        testFrame.setSize(300, 300);
        testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        System.out.println("Đang mở cửa sổ chọn Bác sĩ...");

        // --- CÁCH SỬ DỤNG DIALOG ---
        
        // SỬA ĐỔI Ở ĐÂY:
        // Cung cấp một ID khoa (departmentId) bất kỳ để test, ví dụ: 1
        int testDepartmentId = 1; 
        
        // 1. Tạo dialog
        DoctorGUI doctorDialog = new DoctorGUI(testFrame, testDepartmentId);
        
        // 2. Hiển thị (code sẽ tạm dừng ở đây cho đến khi dialog đóng)
        doctorDialog.setVisible(true);

        // ... (phần còn lại của hàm main giữ nguyên) ...
        
        Doctor chosenDoctor = doctorDialog.getSelectedDoctor();
        if (chosenDoctor != null) {
            System.out.println("Bác sĩ bạn đã chọn là:");
            System.out.println("ID: " + chosenDoctor.getDoctorId());
            System.out.println("Tên: " + chosenDoctor.getFullName());
        } else {
            System.out.println("Người dùng đã nhấn Hủy.");
        }
        
        System.exit(0);
    }
}
