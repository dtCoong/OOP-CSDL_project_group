package com.mycompany.ehr.dtc.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import com.mycompany.ehr.dtc.dao.FamilyMembersDAO;
import com.mycompany.ehr.dtc.model.FamilyMembers;
import com.mycompany.ehr.dtc.model.User;
import javax.swing.DefaultComboBoxModel;


public class FamilyMembersFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnAdd, btnEdit, btnDelete, btnBack;

    private User currentUser;

    public FamilyMembersFrame(User user) {
        this.currentUser = user;

        setTitle("Danh sách thành viên gia đình");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{
            "ID", "Họ tên", "Ngày sinh", "Giới tính", "Quan hệ", "Nhóm máu", "Bảo hiểm", "SĐT"
        }, 0) {
             @Override
            public boolean isCellEditable(int row, int column) {
               return false;
            }
        };
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        table.setAutoCreateRowSorter(true);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setMinWidth(0);
        columnModel.getColumn(0).setMaxWidth(0);
        columnModel.getColumn(0).setPreferredWidth(0);
        columnModel.getColumn(0).setResizable(false);

        add(scrollPane, BorderLayout.CENTER);


        JPanel panelButtons = new JPanel();
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
            HomeFrame home = new HomeFrame(currentUser);
            home.setVisible(true);
            FamilyMembersFrame.this.dispose();
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedViewRow = table.getSelectedRow();
                    if (selectedViewRow != -1) { 
                        int selectedModelRow = table.convertRowIndexToModel(selectedViewRow);
                        int memberId = (int) model.getValueAt(selectedModelRow, 0);

                        FamilyMembers selectedMember = FamilyMembersDAO.getInstance().selectById(
                            new FamilyMembers.Builder(currentUser.getUserId(), "", LocalDate.now()).memberId(memberId).build()
                        );

                        if (selectedMember != null) {
                            MemberDetailsFrame detailsFrame = new MemberDetailsFrame(currentUser, selectedMember);
                            detailsFrame.setVisible(true);
                            FamilyMembersFrame.this.dispose();
                        } else {
                            JOptionPane.showMessageDialog(FamilyMembersFrame.this,
                                "Không thể tải thông tin thành viên.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    }

     private void loadData() {
        model.setRowCount(0);
        List<FamilyMembers> list = FamilyMembersDAO.getInstance().selectByCondition("user_id = " + currentUser.getUserId());
        for (FamilyMembers f : list) {
            model.addRow(new Object[]{
                f.getMemberId(),
                f.getName(),
                f.getDob(),
                f.getGender().getDisplayName(),
                f.getRelationship().getDisplayName(),
                f.getBloodType().getDisplayName(),
                f.getInsuranceNumber(),
                f.getPhone()
            });
        }
    }

    private JComboBox<String> createYearBox(int startOffset) {
        int currentYear = LocalDate.now().getYear();
        int maxYear = currentYear - startOffset;
        int minYear = maxYear - 100;
        String[] years = new String[maxYear - minYear + 1];
        for (int i = 0; i <= (maxYear - minYear); i++) {
            years[i] = String.valueOf(maxYear - i);
        }
        return new JComboBox<>(years);
    }
    
    private JComboBox<String> createMonthBox() {
        String[] months = new String[12];
        for (int i = 0; i < 12; i++) {
            months[i] = String.valueOf(i + 1);
        }
        return new JComboBox<>(months);
    }

    private JComboBox<String> createDayBox(int year, int month) {
        JComboBox<String> dayBox = new JComboBox<>();
        updateDayBox(dayBox, year, month);
        return dayBox;
    }
    
    private void updateDayBox(JComboBox<String> dayBox, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int maxDays = yearMonth.lengthOfMonth();
        String[] days = new String[maxDays];
        for (int i = 0; i < maxDays; i++) {
            days[i] = String.valueOf(i + 1);
        }
        dayBox.setModel(new DefaultComboBoxModel<>(days));
    }

    private void openAddDialog() {
        JTextField txtName = new JTextField(20);
        
        JComboBox<String> yearBox = createYearBox(0); 
        JComboBox<String> monthBox = createMonthBox();
        JComboBox<String> dayBox = createDayBox(Integer.parseInt(yearBox.getItemAt(0)), 1);

        ItemListener dateListener = e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                updateDayBox(dayBox, Integer.parseInt((String)yearBox.getSelectedItem()), Integer.parseInt((String)monthBox.getSelectedItem()));
            }
        };
        yearBox.addItemListener(dateListener);
        monthBox.addItemListener(dateListener);
        
        JPanel datePanel = new JPanel();
        datePanel.add(new JLabel("Năm:")); datePanel.add(yearBox);
        datePanel.add(new JLabel("Tháng:")); datePanel.add(monthBox);
        datePanel.add(new JLabel("Ngày:")); datePanel.add(dayBox);

        JComboBox<FamilyMembers.Gender> genderBox = new JComboBox<>(FamilyMembers.Gender.values());
        
        JComboBox<FamilyMembers.Relationship> relationBox = new JComboBox<>(FamilyMembers.Relationship.values());
        relationBox.removeItem(FamilyMembers.Relationship.BAN_THAN);
        
        JComboBox<FamilyMembers.BloodType> bloodBox = new JComboBox<>(FamilyMembers.BloodType.values());
        bloodBox.setSelectedItem(FamilyMembers.BloodType.UNKNOWN); 

        JTextField txtInsurance = new JTextField(15);
        
        JTextField txtPhone = new JTextField(currentUser.getPhone(), 15);
        
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5)); 
        panel.add(new JLabel("Họ tên:")); panel.add(txtName);
        panel.add(new JLabel("Ngày sinh:")); panel.add(datePanel);
        panel.add(new JLabel("Giới tính:")); panel.add(genderBox);
        panel.add(new JLabel("Quan hệ:")); panel.add(relationBox);
        panel.add(new JLabel("Nhóm máu:")); panel.add(bloodBox);
        panel.add(new JLabel("Số bảo hiểm:")); panel.add(txtInsurance);
        panel.add(new JLabel("Số điện thoại:")); panel.add(txtPhone); 

        int result = JOptionPane.showConfirmDialog(this, panel, "Thêm thành viên", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalDate dob = LocalDate.of(
                    Integer.parseInt((String)yearBox.getSelectedItem()),
                    Integer.parseInt((String)monthBox.getSelectedItem()),
                    Integer.parseInt((String)dayBox.getSelectedItem())
                );
                
                FamilyMembers fm = new FamilyMembers.Builder(currentUser.getUserId(), txtName.getText(), dob)
                        .gender((FamilyMembers.Gender)genderBox.getSelectedItem())
                        .relationship((FamilyMembers.Relationship)relationBox.getSelectedItem())
                        .bloodType((FamilyMembers.BloodType)bloodBox.getSelectedItem())
                        .insuranceNumber(txtInsurance.getText())
                        .phone(txtPhone.getText()) 
                        .build();
                FamilyMembersDAO.getInstance().insert(fm);
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm thành viên: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openEditDialog() {
        int selectedViewRow = table.getSelectedRow(); 
        if (selectedViewRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 thành viên để sửa.");
            return;
        }
        
        int selectedModelRow = table.convertRowIndexToModel(selectedViewRow);

        int id = (int) model.getValueAt(selectedModelRow, 0); 
        String name = (String) model.getValueAt(selectedModelRow, 1);
        LocalDate dobDate = (LocalDate) model.getValueAt(selectedModelRow, 2); 
        String genderStr = model.getValueAt(selectedModelRow, 3).toString();
        String relationStr = model.getValueAt(selectedModelRow, 4).toString();
        String bloodStr = model.getValueAt(selectedModelRow, 5).toString();
        String insurance = model.getValueAt(selectedModelRow, 6) != null ? model.getValueAt(selectedModelRow, 6).toString() : "";
        String phone = model.getValueAt(selectedModelRow, 7) != null ? model.getValueAt(selectedModelRow, 7).toString() : "";

        JTextField txtName = new JTextField(name, 20);

        JComboBox<String> yearBox = createYearBox(0);
        JComboBox<String> monthBox = createMonthBox();
        yearBox.setSelectedItem(String.valueOf(dobDate.getYear()));
        monthBox.setSelectedItem(String.valueOf(dobDate.getMonthValue()));
        JComboBox<String> dayBox = createDayBox(dobDate.getYear(), dobDate.getMonthValue());
        dayBox.setSelectedItem(String.valueOf(dobDate.getDayOfMonth()));

        ItemListener dateListener = e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                updateDayBox(dayBox, Integer.parseInt((String)yearBox.getSelectedItem()), Integer.parseInt((String)monthBox.getSelectedItem()));
            }
        };
        yearBox.addItemListener(dateListener);
        monthBox.addItemListener(dateListener);
        
        JPanel datePanel = new JPanel();
        datePanel.add(new JLabel("Năm:")); datePanel.add(yearBox);
        datePanel.add(new JLabel("Tháng:")); datePanel.add(monthBox);
        datePanel.add(new JLabel("Ngày:")); datePanel.add(dayBox);
        
        JComboBox<FamilyMembers.Gender> genderBox = new JComboBox<>(FamilyMembers.Gender.values());
        genderBox.setSelectedItem(FamilyMembers.Gender.fromString(genderStr));

        JComboBox<FamilyMembers.Relationship> relationBox = new JComboBox<>(FamilyMembers.Relationship.values());
        relationBox.setSelectedItem(FamilyMembers.Relationship.fromString(relationStr));
        
        if (relationStr.equals(FamilyMembers.Relationship.BAN_THAN.getDisplayName())) {
            relationBox.setEnabled(false); 
        } else {
            relationBox.removeItem(FamilyMembers.Relationship.BAN_THAN); 
        }

        JComboBox<FamilyMembers.BloodType> bloodBox = new JComboBox<>(FamilyMembers.BloodType.values());
        bloodBox.setSelectedItem(FamilyMembers.BloodType.fromString(bloodStr));

        JTextField txtInsurance = new JTextField(insurance, 15);
        
        JTextField txtPhone = new JTextField(phone, 15);

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Họ tên:")); panel.add(txtName);
        panel.add(new JLabel("Ngày sinh:")); panel.add(datePanel);
        panel.add(new JLabel("Giới tính:")); panel.add(genderBox);
        panel.add(new JLabel("Quan hệ:")); panel.add(relationBox);
        panel.add(new JLabel("Nhóm máu:")); panel.add(bloodBox);
        panel.add(new JLabel("Số bảo hiểm:")); panel.add(txtInsurance);
        panel.add(new JLabel("Số điện thoại:")); panel.add(txtPhone);

        int result = JOptionPane.showConfirmDialog(this, panel, "Sửa thành viên", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalDate newDob = LocalDate.of(
                    Integer.parseInt((String)yearBox.getSelectedItem()),
                    Integer.parseInt((String)monthBox.getSelectedItem()),
                    Integer.parseInt((String)dayBox.getSelectedItem())
                );

                FamilyMembers fm = new FamilyMembers.Builder(currentUser.getUserId(), txtName.getText(), newDob)
                        .memberId(id) 
                        .gender((FamilyMembers.Gender)genderBox.getSelectedItem())
                        .relationship((FamilyMembers.Relationship)relationBox.getSelectedItem())
                        .bloodType((FamilyMembers.BloodType)bloodBox.getSelectedItem())
                        .insuranceNumber(txtInsurance.getText())
                        .phone(txtPhone.getText()) 
                        .updatedAt(LocalDateTime.now()) 
                        .build();
                FamilyMembersDAO.getInstance().update(fm);
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelected() {
        int selectedViewRow = table.getSelectedRow(); 
        if (selectedViewRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thành viên cần xóa.");
            return;
        }

        int selectedModelRow = table.convertRowIndexToModel(selectedViewRow);

        int id = (int) model.getValueAt(selectedModelRow, 0); 
        String relationStr = model.getValueAt(selectedModelRow, 4).toString(); 
        
        if (relationStr.equals(FamilyMembers.Relationship.BAN_THAN.getDisplayName())) {
             JOptionPane.showMessageDialog(this, "Không thể xóa hồ sơ 'Bản thân'.", "Lỗi", JOptionPane.WARNING_MESSAGE);
             return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Xóa thành viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            FamilyMembers fm = new FamilyMembers.Builder(currentUser.getUserId(), "", LocalDate.now()).memberId(id).build();
            FamilyMembersDAO.getInstance().delete(fm);
            loadData();
        }
    }
}