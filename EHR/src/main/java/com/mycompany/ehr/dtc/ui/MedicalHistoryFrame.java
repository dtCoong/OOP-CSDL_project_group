package com.mycompany.ehr.dtc.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter; 
import java.awt.*;
import java.awt.event.ActionListener; 
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects; 
import java.util.stream.Collectors;

import com.mycompany.ehr.dtc.dao.MedicalHistoryDAO;
import com.mycompany.ehr.dtc.model.MedicalHistory;
import com.mycompany.ehr.dtc.model.MedicalHistory.Severity; 
import com.mycompany.ehr.dtc.model.MedicalHistory.Status;  
import com.mycompany.ehr.dtc.model.FamilyMembers;
import com.mycompany.ehr.dtc.model.User;
import javax.swing.RowFilter; 

public class MedicalHistoryFrame extends JFrame {

    private User currentUser;
    private FamilyMembers selectedMember;

    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter; 
    private JButton btnBack;
    private JComboBox<String> yearComboBox;
    private JComboBox<Severity> severityComboBox;
    private JComboBox<Status> statusComboBox;
    private JCheckBox chronicCheckBox;

    private List<MedicalHistory> fullHistoryList = new ArrayList<>();
    private static final DateTimeFormatter TABLE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public MedicalHistoryFrame(User user, FamilyMembers member) {
        this.currentUser = user;
        this.selectedMember = member;

        setTitle("Lịch sử Bệnh án" + selectedMember.getName());
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        loadAndPrepareData(); 
    }

    private void initUI() {
        setLayout(new BorderLayout(5, 5));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Năm:"));
        yearComboBox = new JComboBox<>();
        yearComboBox.addItem("Tất cả");
        filterPanel.add(yearComboBox);

        filterPanel.add(Box.createHorizontalStrut(10)); 
        filterPanel.add(new JLabel("Mức độ:"));
        severityComboBox = new JComboBox<>();
        severityComboBox.addItem(null); 
        for (Severity s : Severity.values()) severityComboBox.addItem(s);
        filterPanel.add(severityComboBox);

        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.add(new JLabel("Tình trạng:"));
        statusComboBox = new JComboBox<>();
        statusComboBox.addItem(null); 
        for (Status s : Status.values()) statusComboBox.addItem(s);
        filterPanel.add(statusComboBox);

        filterPanel.add(Box.createHorizontalStrut(10));
        chronicCheckBox = new JCheckBox("Chỉ bệnh mãn tính");
        filterPanel.add(chronicCheckBox);

        add(filterPanel, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{
            "ID", "Tên bệnh/Tình trạng", "Ngày chẩn đoán", "Mãn tính", "Mức độ", "Tình trạng",
            "Ngày vào viện", "Ngày ra viện", "Bệnh viện", "Địa chỉ BV", "Ghi chú"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        sorter = new TableRowSorter<>(model); 
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setMinWidth(0);
        columnModel.getColumn(0).setMaxWidth(0);
        columnModel.getColumn(0).setPreferredWidth(0);
        columnModel.getColumn(0).setResizable(false);

        columnModel.getColumn(1).setPreferredWidth(180); // Condition Name
        columnModel.getColumn(2).setPreferredWidth(90);  // Diagnosis Date
        columnModel.getColumn(3).setPreferredWidth(60);  // Chronic
        columnModel.getColumn(4).setPreferredWidth(80);  // Severity
        columnModel.getColumn(5).setPreferredWidth(100); // Status
        columnModel.getColumn(6).setPreferredWidth(90);  // Admission Date
        columnModel.getColumn(7).setPreferredWidth(90);  // Discharge Date
        columnModel.getColumn(8).setPreferredWidth(150); // Hospital Name
        columnModel.getColumn(9).setPreferredWidth(200); // Hospital Address
        columnModel.getColumn(10).setPreferredWidth(200); // Notes

        table.setDefaultRenderer(Object.class, new MedicalHistoryCellRenderer());

        add(scrollPane, BorderLayout.CENTER);


        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnBack = new JButton("Quay lại");
        panelButtons.add(btnBack);
        add(panelButtons, BorderLayout.SOUTH);

        btnBack.addActionListener(e -> {
            MemberDetailsFrame detailsFrame = new MemberDetailsFrame(currentUser, selectedMember);
            detailsFrame.setVisible(true);
            MedicalHistoryFrame.this.dispose();
        });

        ActionListener filterListener = e -> applyFilters();
        yearComboBox.addActionListener(filterListener);
        severityComboBox.addActionListener(filterListener);
        statusComboBox.addActionListener(filterListener);
        chronicCheckBox.addActionListener(filterListener);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedViewRow = table.getSelectedRow();
                    if (selectedViewRow != -1) {
                        int selectedModelRow = table.convertRowIndexToModel(selectedViewRow);
                        int historyId = (int) model.getValueAt(selectedModelRow, 0);

                        MedicalHistory selectedHistory = findHistoryById(historyId);

                        if (selectedHistory != null) {
                            MedicalHistoryDetailDialog detailDialog = new MedicalHistoryDetailDialog(MedicalHistoryFrame.this, selectedHistory);
                            detailDialog.setVisible(true);
                        } else {
                             JOptionPane.showMessageDialog(MedicalHistoryFrame.this,
                                "Không thể tải chi tiết lịch sử bệnh án.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    }

    private void loadAndPrepareData() {
        fullHistoryList = MedicalHistoryDAO.getInstance().selectByCondition("member_id = " + selectedMember.getMemberId());
        populateYearComboBox();
        populateTableModel(fullHistoryList); 
    }

    private void populateYearComboBox() {
        ActionListener listener = yearComboBox.getActionListeners().length > 0 ? yearComboBox.getActionListeners()[0] : null;
        if (listener != null) yearComboBox.removeActionListener(listener);

        yearComboBox.removeAllItems();
        yearComboBox.addItem("Tất cả");

        fullHistoryList.stream()
            .map(MedicalHistory::getDiagnosisDate)
            .filter(Objects::nonNull)
            .map(LocalDate::getYear)
            .distinct()
            .sorted(Comparator.reverseOrder())
            .forEach(year -> yearComboBox.addItem(String.valueOf(year)));

         if (listener != null) yearComboBox.addActionListener(listener);
         yearComboBox.setSelectedItem("Tất cả");
    }

    private void applyFilters() {
        RowFilter<DefaultTableModel, Object> combinedFilter = null;
        List<RowFilter<DefaultTableModel, Object>> filters = new ArrayList<>();

        String selectedYearStr = (String) yearComboBox.getSelectedItem();
        if (selectedYearStr != null && !"Tất cả".equals(selectedYearStr)) {
            try {
                int selectedYear = Integer.parseInt(selectedYearStr);
                filters.add(RowFilter.regexFilter("^" + selectedYear + ".*", 2));
            } catch (NumberFormatException ignored) {}
        }

        Severity selectedSeverity = (Severity) severityComboBox.getSelectedItem();
        if (selectedSeverity != null) {
            filters.add(RowFilter.regexFilter("^" + selectedSeverity.getDisplayName() + "$", 4));
        }

        Status selectedStatus = (Status) statusComboBox.getSelectedItem();
        if (selectedStatus != null) {
            filters.add(RowFilter.regexFilter("^" + selectedStatus.getDisplayName() + "$", 5));
        }

        if (chronicCheckBox.isSelected()) {
            filters.add(RowFilter.regexFilter("^Có$", 3)); 
        }

        if (!filters.isEmpty()) {
            combinedFilter = RowFilter.andFilter(filters);
        }

        sorter.setRowFilter(combinedFilter); 
    }

    private void populateTableModel(List<MedicalHistory> historyList) {
        model.setRowCount(0); 
        for (MedicalHistory h : historyList) {
            model.addRow(new Object[]{
                h.getHistoryId(),
                h.getConditionName(),
                h.getDiagnosisDate() != null ? h.getDiagnosisDate().format(TABLE_DATE_FORMATTER) : null,
                h.isChronic() ? "Có" : "Không", 
                h.getSeverity().getDisplayName(), 
                h.getStatus().getDisplayName(),   
                h.getHospitalAdmissionDate() != null ? h.getHospitalAdmissionDate().format(TABLE_DATE_FORMATTER) : null,
                h.getHospitalDischargeDate() != null ? h.getHospitalDischargeDate().format(TABLE_DATE_FORMATTER) : null,
                h.getHospitalName(),
                h.getHospitalAddress(),
                h.getNotes()
            });
        }
    }


    private MedicalHistory findHistoryById(int historyId) {
        return fullHistoryList.stream()
                .filter(h -> h.getHistoryId() == historyId)
                .findFirst()
                .orElse(null);
    }

   
    private class MedicalHistoryCellRenderer extends DefaultTableCellRenderer {
       
        private final Color SEVERE_COLOR = new Color(255, 182, 193); 
        private final Color MODERATE_COLOR = new Color(255, 223, 186); 
        private final Color MILD_COLOR = new Color(240, 248, 255); 

        private final String ICON_CURED = " ✅";
        private final String ICON_CONTROLLED = " ☑️"; 
        private final String ICON_TREATING = " ⏳"; 
        private final String ICON_SERIOUS = " ⚠️"; 

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setText(value == null ? "" : value.toString()); 
            setIcon(null); 

            if (row < table.getRowCount()) {
                 int modelRow = table.convertRowIndexToModel(row); 
                Object severityValue = table.getModel().getValueAt(modelRow, 4);
                if (severityValue != null) {
                    String severityStr = severityValue.toString();
                    if (Severity.NẶNG.getDisplayName().equals(severityStr)) {
                        c.setBackground(isSelected ? table.getSelectionBackground() : SEVERE_COLOR);
                    } else if (Severity.TRUNG_BÌNH.getDisplayName().equals(severityStr)) {
                        c.setBackground(isSelected ? table.getSelectionBackground() : MODERATE_COLOR);
                    } else if (Severity.NHẸ.getDisplayName().equals(severityStr)) {
                         c.setBackground(isSelected ? table.getSelectionBackground() : MILD_COLOR);
                    }
                }

                 
                 if (column == table.convertColumnIndexToView(5)) { 
                     Object statusValue = table.getModel().getValueAt(modelRow, 5);
                     if (statusValue != null) {
                         String statusStr = statusValue.toString();
                         String iconText = "";
                         if (Status.ĐÃ_KHỎI.getDisplayName().equals(statusStr)) {
                             iconText = ICON_CURED;
                         } else if (Status.KIỂM_SOÁT_ĐƯỢC.getDisplayName().equals(statusStr)) {
                             iconText = ICON_CONTROLLED;
                         } else if (Status.ĐANG_ĐIỀU_TRỊ.getDisplayName().equals(statusStr)) {
                             iconText = ICON_TREATING;
                         } else if (Status.TRẦM_TRỌNG.getDisplayName().equals(statusStr)) {
                             iconText = ICON_SERIOUS;
                         }
                         setText((value == null ? "" : value.toString()) + iconText);
                     }
                 }
            }


            return c;
        }
    }
}