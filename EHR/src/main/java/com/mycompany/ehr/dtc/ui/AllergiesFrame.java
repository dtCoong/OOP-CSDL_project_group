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
import java.time.format.DateTimeParseException; 
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mycompany.ehr.dtc.dao.AllergiesDAO;
import com.mycompany.ehr.dtc.model.Allergies;
import com.mycompany.ehr.dtc.model.Allergies.AllergySeverity;
import com.mycompany.ehr.dtc.model.Allergies.AllergyType;
import com.mycompany.ehr.dtc.model.FamilyMembers;
import com.mycompany.ehr.dtc.model.User;
import javax.swing.RowFilter;

public class AllergiesFrame extends JFrame {

    private User currentUser;
    private FamilyMembers selectedMember;

    private JTabbedPane tabbedPane;
    private JComboBox<AllergySeverity> severityComboBox;
    private Map<AllergyType, DefaultTableModel> tableModels = new EnumMap<>(AllergyType.class);
    private Map<AllergyType, TableRowSorter<DefaultTableModel>> tableSorters = new EnumMap<>(AllergyType.class);

    private JButton btnAdd, btnEdit, btnDelete, btnBack;

    private List<Allergies> fullAllergyList = new ArrayList<>();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public AllergiesFrame(User user, FamilyMembers member) {
        this.currentUser = user;
        this.selectedMember = member;

        setTitle("Quản lý Dị ứng - " + selectedMember.getName()); 
        setSize(950, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        loadAndPrepareData();
    }

    private void initUI() {
        setLayout(new BorderLayout(5, 5));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Lọc theo mức độ:"));
        severityComboBox = new JComboBox<>();
        severityComboBox.addItem(null); 
        for (AllergySeverity s : AllergySeverity.values()) severityComboBox.addItem(s);
        filterPanel.add(severityComboBox);
        add(filterPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();

        for (AllergyType type : AllergyType.values()) {
            JPanel tabPanel = new JPanel(new BorderLayout());
            DefaultTableModel currentModel = new DefaultTableModel(new String[]{
                "ID", "Tác nhân", "Mức độ", "Triệu chứng", "Ngày phát hiện", "Ghi chú"
            }, 0) {
                 @Override
                 public boolean isCellEditable(int row, int column) { return false; }
            };
            tableModels.put(type, currentModel);

            JTable currentTable = new JTable(currentModel);
            TableRowSorter<DefaultTableModel> currentSorter = new TableRowSorter<>(currentModel);
            tableSorters.put(type, currentSorter);
            currentTable.setRowSorter(currentSorter);


            TableColumnModel columnModel = currentTable.getColumnModel();
            columnModel.getColumn(0).setMinWidth(0);
            columnModel.getColumn(0).setMaxWidth(0);
            columnModel.getColumn(0).setPreferredWidth(0);
            columnModel.getColumn(0).setResizable(false);

            columnModel.getColumn(1).setPreferredWidth(150); // Allergen
            columnModel.getColumn(2).setPreferredWidth(80);  // Severity
            columnModel.getColumn(3).setPreferredWidth(200); // Symptoms
            columnModel.getColumn(4).setPreferredWidth(100); // Discovered Date
            columnModel.getColumn(5).setPreferredWidth(250); // Notes

            currentTable.setDefaultRenderer(Object.class, new AllergyCellRenderer());


            tabPanel.add(new JScrollPane(currentTable), BorderLayout.CENTER);
            tabbedPane.addTab(type.getDisplayName(), tabPanel);
        }

        add(tabbedPane, BorderLayout.CENTER);


        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAdd = new JButton("Thêm");
        btnEdit = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnBack = new JButton("Quay lại");

        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnDelete);
        panelButtons.add(btnBack);

        add(panelButtons, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> openAddDialog());
        btnEdit.addActionListener(e -> openEditDialog());
        btnDelete.addActionListener(e -> deleteSelected());

        btnBack.addActionListener(e -> {
            MemberDetailsFrame detailsFrame = new MemberDetailsFrame(currentUser, selectedMember);
            detailsFrame.setVisible(true);
            AllergiesFrame.this.dispose();
        });

        severityComboBox.addActionListener(e -> applySeverityFilter());
    }


    private void loadAndPrepareData() {
        fullAllergyList = AllergiesDAO.getInstance().selectByCondition("member_id = " + selectedMember.getMemberId());
        populateTables();
        applySeverityFilter();
    }

    private void populateTables() {
        tableModels.values().forEach(model -> model.setRowCount(0));

        for (Allergies allergy : fullAllergyList) {
            DefaultTableModel currentModel = tableModels.get(allergy.getAllergyType());
            if (currentModel != null) {
                currentModel.addRow(new Object[]{
                    allergy.getAllergyId(),
                    allergy.getAllergen(),
                    allergy.getSeverity().getDisplayName(),
                    allergy.getSymptoms(),
                    allergy.getDiscoveredDate() != null ? allergy.getDiscoveredDate().format(DATE_FORMATTER) : null,
                    allergy.getNotes()
                });
            }
        }
    }

    private void applySeverityFilter() {
        if (tabbedPane.getSelectedIndex() < 0) return; 
        AllergyType selectedType = AllergyType.values()[tabbedPane.getSelectedIndex()];
        TableRowSorter<DefaultTableModel> currentSorter = tableSorters.get(selectedType);
        if (currentSorter == null) return;

        AllergySeverity selectedSeverity = (AllergySeverity) severityComboBox.getSelectedItem();
        RowFilter<DefaultTableModel, Object> severityFilter = null;

        if (selectedSeverity != null) {
            severityFilter = RowFilter.regexFilter("^" + selectedSeverity.getDisplayName() + "$", 2); 
        }
        currentSorter.setRowFilter(severityFilter);
    }

    private Allergies findAllergyById(int allergyId) {
        return fullAllergyList.stream()
                .filter(a -> a.getAllergyId() == allergyId)
                .findFirst()
                .orElse(null);
    }


    private void openAddDialog() {
        JTextField txtAllergen = new JTextField(20);
        JComboBox<AllergyType> typeBox = new JComboBox<>(AllergyType.values());
        JComboBox<AllergySeverity> severityBox = new JComboBox<>(AllergySeverity.values());
        JTextArea txtSymptoms = new JTextArea(3, 20);
        JTextField txtDiscoveredDate = new JTextField(10); 
        JTextArea txtNotes = new JTextArea(3, 20);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGbc();
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Tác nhân*:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(txtAllergen, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Loại dị ứng:"), gbc); 
        gbc.gridx = 1; gbc.gridy = 1; panel.add(typeBox, gbc);               
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Mức độ:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(severityBox, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Triệu chứng:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(new JScrollPane(txtSymptoms), gbc);
        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Ngày phát hiện (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; panel.add(txtDiscoveredDate, gbc);
        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Ghi chú:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; panel.add(new JScrollPane(txtNotes), gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Thêm dị ứng", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String allergen = txtAllergen.getText().trim();
                if (allergen.isEmpty()) {
                    throw new IllegalArgumentException("Tên tác nhân không được để trống.");
                }

                LocalDate discoveredDate = parseDate(txtDiscoveredDate.getText().trim());

                Allergies allergy = new Allergies.Builder(
                            selectedMember.getMemberId(),
                            allergen,
                            (AllergyType) typeBox.getSelectedItem() 
                        )
                        .severity((AllergySeverity) severityBox.getSelectedItem())
                        .symptoms(txtSymptoms.getText())
                        .discoveredDate(discoveredDate)
                        .notes(txtNotes.getText())
                        .build();

                AllergiesDAO.getInstance().insert(allergy);
                loadAndPrepareData(); 

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openEditDialog() {
        JTable currentTable = getActiveTable();
        if (currentTable == null) return;
        
        int selectedViewRow = currentTable.getSelectedRow();
        if (selectedViewRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dị ứng để sửa.");
            return;
        }

        int selectedModelRow = currentTable.convertRowIndexToModel(selectedViewRow);
        int allergyId = (int) currentTable.getModel().getValueAt(selectedModelRow, 0);

        Allergies existingAllergy = findAllergyById(allergyId);
        if (existingAllergy == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin dị ứng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField txtAllergen = new JTextField(existingAllergy.getAllergen(), 20);

        JComboBox<AllergyType> typeBox = new JComboBox<>(new AllergyType[]{ existingAllergy.getAllergyType() });
        typeBox.setEnabled(false);
        
        JComboBox<AllergySeverity> severityBox = new JComboBox<>(AllergySeverity.values());
        severityBox.setSelectedItem(existingAllergy.getSeverity());
        JTextArea txtSymptoms = new JTextArea(existingAllergy.getSymptoms(), 3, 20);
        JTextField txtDiscoveredDate = new JTextField(
                existingAllergy.getDiscoveredDate() != null ? existingAllergy.getDiscoveredDate().format(DATE_FORMATTER) : "", 10);
        JTextArea txtNotes = new JTextArea(existingAllergy.getNotes(), 3, 20);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGbc();
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Tác nhân*:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(txtAllergen, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Loại dị ứng:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(typeBox, gbc); 
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Mức độ:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(severityBox, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Triệu chứng:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(new JScrollPane(txtSymptoms), gbc);
        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Ngày phát hiện (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; panel.add(txtDiscoveredDate, gbc);
        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Ghi chú:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; panel.add(new JScrollPane(txtNotes), gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Sửa dị ứng", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
             try {
                String allergen = txtAllergen.getText().trim();
                if (allergen.isEmpty()) {
                    throw new IllegalArgumentException("Tên tác nhân không được để trống.");
                }
                LocalDate discoveredDate = parseDate(txtDiscoveredDate.getText().trim());

                 Allergies updatedAllergy = new Allergies.Builder(
                            existingAllergy.getMemberId(),
                            allergen,
                            existingAllergy.getAllergyType()
                        )
                        .allergyId(allergyId) 
                        .severity((AllergySeverity) severityBox.getSelectedItem())
                        .symptoms(txtSymptoms.getText())
                        .discoveredDate(discoveredDate)
                        .notes(txtNotes.getText())
                        .build();

                AllergiesDAO.getInstance().update(updatedAllergy);
                loadAndPrepareData(); 

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelected() {
        JTable currentTable = getActiveTable();
        if (currentTable == null) return;

        int selectedViewRow = currentTable.getSelectedRow();
        if (selectedViewRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dị ứng cần xóa.");
            return;
        }

        int selectedModelRow = currentTable.convertRowIndexToModel(selectedViewRow);
        int allergyId = (int) currentTable.getModel().getValueAt(selectedModelRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa dị ứng này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Allergies toDelete = new Allergies.Builder(selectedMember.getMemberId(), "temp", AllergyType.KHAC).allergyId(allergyId).build();
                AllergiesDAO.getInstance().delete(toDelete);
                loadAndPrepareData(); 
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private GridBagConstraints createGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }
    
    private JTable getActiveTable() {
        try {
            JPanel tabPanel = (JPanel) tabbedPane.getSelectedComponent();
            JScrollPane scrollPane = (JScrollPane) tabPanel.getComponent(0);
            return (JTable) scrollPane.getViewport().getView();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private LocalDate parseDate(String dateStr) throws IllegalArgumentException {
        if (dateStr == null || dateStr.isEmpty()) {
            return null; 
        }
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Định dạng ngày không hợp lệ (yyyy-MM-dd).");
        }
    }

    private class AllergyCellRenderer extends DefaultTableCellRenderer {
        private final Color DANGER_COLOR = new Color(255, 100, 100);
        private final Color SEVERE_COLOR = new Color(255, 182, 193);
        private final Color MODERATE_COLOR = new Color(255, 223, 186);
        private final Color MILD_COLOR = Color.WHITE;
        private final String ICON_DANGER = " ☠️";
        private final String ICON_SEVERE = " ❗";

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setText(value == null ? "" : value.toString());
            setIcon(null);

            if (row < table.getRowCount()) {
                int modelRow = table.convertRowIndexToModel(row);
                Object severityValue = table.getModel().getValueAt(modelRow, 2); 

                if (severityValue != null) {
                    String severityStr = severityValue.toString();
                    String iconText = "";
                    Color bgColor = MILD_COLOR;

                    if (AllergySeverity.NGUY_HIEM.getDisplayName().equals(severityStr)) {
                        bgColor = DANGER_COLOR;
                        iconText = ICON_DANGER;
                    } else if (AllergySeverity.NANG.getDisplayName().equals(severityStr)) {
                        bgColor = SEVERE_COLOR;
                        iconText = ICON_SEVERE;
                    } else if (AllergySeverity.TRUNG_BINH.getDisplayName().equals(severityStr)) {
                        bgColor = MODERATE_COLOR;
                    }
                    c.setBackground(isSelected ? table.getSelectionBackground() : bgColor);

                     if (column == table.convertColumnIndexToView(2)) { 
                        setText((value == null ? "" : value.toString()) + iconText);
                    }
                }
            }
            return c;
        }
    }
}