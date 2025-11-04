package com.mycompany.ehr.npd.gui;

import com.mycompany.ehr.npd.dao.HospitalDAO;
import com.mycompany.ehr.npd.model.Hospital;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Giao diện JDialog để Bệnh nhân LỌC và CHỌN một Bệnh viện.
 */
public class HospitalSelectionGUI extends JDialog {

    // GUI Components
    private JTable hospitalTable;
    private HospitalTableModel tableModel;
    private TableRowSorter<HospitalTableModel> sorter;
    private JTextField filterField;
    private JButton chooseButton;
    private JButton cancelButton;

    // Data
    private HospitalDAO hospitalDAO;
    private Hospital selectedHospital = null; // Kết quả Bệnh viện được chọn

    /**
     * Constructor
     */
    public HospitalSelectionGUI(Frame owner) {
        // true = Modal Dialog
        super(owner, "Chọn Bệnh viện", true); 
        
        this.hospitalDAO = HospitalDAO.getInstance();

        setupUI();
        loadData();
    }

    private void setupUI() {
        setSize(800, 600);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(10, 10));

        // Panel Lọc (Phía trên - NORTH)
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        filterPanel.add(new JLabel("Lọc theo Tỉnh/Thành phố (trong địa chỉ):"));
        filterField = new JTextField(30);
        filterPanel.add(filterField);
        add(filterPanel, BorderLayout.NORTH);

        // Bảng (Trung tâm - CENTER)
        tableModel = new HospitalTableModel(new ArrayList<>());
        hospitalTable = new JTable(tableModel);
        hospitalTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        hospitalTable.setFont(new Font("Arial", Font.PLAIN, 14));
        hospitalTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        // Cấu hình Sorter để lọc
        sorter = new TableRowSorter<>(tableModel);
        hospitalTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(hospitalTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Panel Nút bấm (Phía dưới - SOUTH)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        chooseButton = new JButton("Chọn Bệnh viện này");
        chooseButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        cancelButton = new JButton("Hủy");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));

        buttonPanel.add(cancelButton);
        buttonPanel.add(chooseButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Xử lý Sự kiện
        // Sự kiện LỌC khi gõ văn bản
        filterField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFilter();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFilter();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFilter();
            }
        });

        chooseButton.addActionListener(e -> onChoose());
        cancelButton.addActionListener(e -> onCancel());
    }

    /**
     * Cập nhật bộ lọc của JTable dựa trên nội dung của filterField
     */
    private void updateFilter() {
        String text = filterField.getText();
        if (text.trim().length() == 0) {
            // Nếu ô lọc trống, hiển thị tất cả
            sorter.setRowFilter(null);
        } else {
            // Ngược lại, lọc cột "Địa chỉ" (index = 2)
            // "(?i)" = không phân biệt hoa thường
            // Pattern.quote() = coi văn bản nhập vào là chuỗi, không phải regex
            try {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(text), 2));
            } catch (java.util.regex.PatternSyntaxException e) {
                // Xử lý nếu người dùng gõ regex không hợp lệ
                sorter.setRowFilter(null);
            }
        }
    }

    /**
     * Xử lý khi nhấn nút "Chọn"
     */
    private void onChoose() {
        int viewRow = hospitalTable.getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn một bệnh viện từ danh sách.", 
                "Chưa chọn", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Chuyển đổi index của View (đã lọc) sang index của Model
        int modelRow = hospitalTable.convertRowIndexToModel(viewRow);
        
        // Lấy bệnh viện đã chọn từ model và lưu lại
        this.selectedHospital = tableModel.getHospitalAt(modelRow);
        
        // Đóng cửa sổ
        dispose();
    }

    /**
     * Xử lý khi nhấn nút "Hủy"
     */
    private void onCancel() {
        this.selectedHospital = null; // Không chọn gì cả
        dispose(); // Đóng cửa sổ
    }

    /**
     * Tải dữ liệu từ DAO lên bảng
     */
    private void loadData() {
    try {
        // Hàm này có thể ném ra lỗi SQL
        ArrayList<Hospital> hospitals = hospitalDAO.selectAll(); 
        tableModel.updateData(hospitals);

    } catch (Exception e) {
        e.printStackTrace();

        // HIỂN THỊ LỖI CHO NGƯỜI DÙNG
        JOptionPane.showMessageDialog(this,
                "LỖI KẾT NỐI DATABASE:\n" + e.getMessage(),
                "Lỗi Database",
                JOptionPane.ERROR_MESSAGE);

        // Đóng cửa sổ pop-up lại vì nó bị lỗi, không thể hiển thị
        dispose(); 
    }
}

    /**
     * Phương thức public để cửa sổ cha có thể lấy Bệnh viện đã được chọn
     */
    public Hospital getSelectedHospital() {
        return this.selectedHospital;
    }

    // LỚP LỒNG NHAU (INNER CLASS) CHO TABLE MODEL
    private class HospitalTableModel extends AbstractTableModel {

        private List<Hospital> hospitals;

        private final String[] columnNames = {"ID", "Tên Bệnh viện", "Địa chỉ", "Hotline", "Website"};

        public HospitalTableModel(List<Hospital> hospitals) {
            this.hospitals = hospitals;
        }

        public void updateData(List<Hospital> newHospitals) {
            this.hospitals = newHospitals;
            fireTableDataChanged();
        }

        public Hospital getHospitalAt(int rowIndex) {
            if (rowIndex >= 0 && rowIndex < hospitals.size()) {
                return hospitals.get(rowIndex);
            }
            return null;
        }

        @Override
        public int getRowCount() {
            return hospitals.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Hospital hospital = hospitals.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return hospital.getHospitalId();
                case 1:
                    return hospital.getName();
                case 2:
                    return hospital.getAddress(); 
                case 3:
                    return hospital.getHotline();
                case 4:
                    return hospital.getWebsite();
                default:
                    return null;
            }
        }
    }

    // PHƯƠNG THỨC MAIN ĐỂ TEST
    public static void main(String[] args) {
        // Giả lập một Frame cha
        JFrame testFrame = new JFrame();
        testFrame.setSize(300, 300);
        testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        System.out.println("Đang mở cửa sổ chọn Bệnh viện...");

        // 1. Tạo dialog
        HospitalSelectionGUI hospitalDialog = new HospitalSelectionGUI(testFrame);
        
        // 2. Hiển thị
        hospitalDialog.setVisible(true);

        // 3. Lấy kết quả
        Hospital chosenHospital = hospitalDialog.getSelectedHospital();

        // 4. Xử lý kết quả
        if (chosenHospital != null) {
            System.out.println("Bệnh viện bạn đã chọn là:");
            System.out.println("ID: " + chosenHospital.getHospitalId());
            System.out.println("Tên: " + chosenHospital.getName());
            System.out.println("Địa chỉ: " + chosenHospital.getAddress());
        } else {
            System.out.println("Người dùng đã nhấn Hủy.");
        }
        
        // Thoát chương trình test
        System.exit(0);
    }
}