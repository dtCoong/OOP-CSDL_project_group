package com.mycompany.ehr.ui;

import com.mycompany.ehr.dao.VaccinationRecordsDao;
import com.mycompany.ehr.dao.VaccineTemplatesDao;
import com.mycompany.ehr.model.VaccinationRecords;
import com.mycompany.ehr.model.VaccineTemplates;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainApplicationFrame extends JFrame {

    private VaccineTemplatesDao templateDao;
    private VaccinationRecordsDao recordDao;

    private JTable templateTable, recordTable;
    private DefaultTableModel templateTableModel, recordTableModel;
    
    private final Color SIDEBAR_BACKGROUND = new Color(41, 128, 185); 
    private final Color SIDEBAR_TEXT_COLOR = new Color(255, 255, 255); 
    private final Color SIDEBAR_HOVER_COLOR = new Color(31, 97, 141);  

    private final Color MAIN_BACKGROUND = new Color(245, 245, 245);
    private final Color HEADER_BACKGROUND = new Color(255, 255, 255);
    private final Color TABLE_HEADER_BACKGROUND = new Color(230, 230, 230);
    private final Color GRID_COLOR = new Color(224, 224, 224);
    private final Color TEXT_COLOR = new Color(55, 71, 79);
    private final Color ZEBRA_STRIPE_COLOR = new Color(255, 255, 255);
    private final Color BUTTON_HOVER_BACKGROUND_TEXT = new Color(210, 230, 250);

    
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JLabel headerLabel; 
    
  
    private ImageIcon headerTemplateIcon, headerRecordIcon; 


    public MainApplicationFrame(VaccineTemplatesDao templateDao, VaccinationRecordsDao recordDao) {
        this.templateDao = templateDao;
        this.recordDao = recordDao;

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }
        
        loadIcons(); 

        setTitle("Hệ thống quản lý tiêm chủng EHR");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        getContentPane().setLayout(new BorderLayout());

        JPanel sidebarPanel = createSidebarPanel();
        getContentPane().add(sidebarPanel, BorderLayout.WEST);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(MAIN_BACKGROUND);
        
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(MAIN_BACKGROUND);
        
        JPanel templateView = createTemplatePanel();
        JPanel recordView = createRecordPanel();
        
        contentPanel.add(templateView, "TEMPLATES");
        contentPanel.add(recordView, "RECORDS");
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        
        loadTemplateData();
        loadRecordData();
        
        
        cardLayout.show(contentPanel, "TEMPLATES");
        headerLabel.setText("Quản lý Vaccine Templates");
        headerLabel.setIcon(headerTemplateIcon); 
    }
    

    private void loadIcons() {
        try {
            headerTemplateIcon = resizeIcon(new ImageIcon(getClass().getResource("/icons/vaccine.png")), 24, 24);
            headerRecordIcon   = resizeIcon(new ImageIcon(getClass().getResource("/icons/hoso.png")), 24, 24);
        } catch (Exception e) {
            System.err.println("Lỗi khi tải icon. Đảm bảo file icon (vaccine.png, hoso.png) tồn tại trong src/main/resources/icons/");
            headerTemplateIcon = null; 
            headerRecordIcon = null;
        }
    }
    
   
    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        if (icon != null && icon.getImage() != null) {
            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        return null;
    }


    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_BACKGROUND);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, GRID_COLOR));
        header.setPreferredSize(new Dimension(0, 60));
        
        headerLabel = new JLabel("Tiêu đề"); 
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(TEXT_COLOR);
        headerLabel.setBorder(new EmptyBorder(10, 20, 10, 10));
        headerLabel.setIconTextGap(10); 
        header.add(headerLabel, BorderLayout.WEST);
        
        return header;
    }
    

    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(2, 1)); 
        sidebar.setBackground(SIDEBAR_BACKGROUND);
        sidebar.setPreferredSize(new Dimension(240, 0));
        
        JButton templatesButton = createSidebarButton("Quản lý Vaccine Templates", null);
        templatesButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "TEMPLATES");
            headerLabel.setText("Quản lý Vaccine Templates");
            headerLabel.setIcon(headerTemplateIcon); 
        });
        
        JButton recordsButton = createSidebarButton("Quản lý Hồ sơ Tiêm chủng", null);
        recordsButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "RECORDS");
            headerLabel.setText("Quản lý Hồ sơ Tiêm chủng");
            headerLabel.setIcon(headerRecordIcon); 
        });
        
        sidebar.add(templatesButton);
        sidebar.add(recordsButton);
        
        return sidebar;
    }
    
    private JPanel createTemplatePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(MAIN_BACKGROUND); 

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(HEADER_BACKGROUND);
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, GRID_COLOR));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        buttonPanel.setOpaque(false);

        JButton reloadButton = createTextButton("Tải lại", "Tải lại danh sách");
        JButton addButton = createTextButton("Thêm", "Thêm mới Template...");
        JButton editButton = createTextButton("Sửa", "Chỉnh sửa Template đang chọn");
        JButton deleteButton = createTextButton("Xóa", "Xóa Template đang chọn");

        buttonPanel.add(reloadButton);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 10));
        searchPanel.setOpaque(false);
        JTextField searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(200, 30));
        JButton searchButton = createTextButton("Tìm", "Tìm kiếm");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        topPanel.add(buttonPanel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        String[] templateColumnNames = {"ID", "Tên Vaccine", "Mô tả", "Từ (ngày tuổi)", "Đến (ngày tuổi)", "Khoảng cách (ngày)", "Tổng liều", "Ghi chú", "Ngày tạo"};
        templateTableModel = new DefaultTableModel(templateColumnNames, 0) { @Override public boolean isCellEditable(int row, int column) { return false; }};
        templateTable = new JTable(templateTableModel);
        templateTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        templateTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        templateTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        templateTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        templateTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        templateTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        templateTable.getColumnModel().getColumn(5).setPreferredWidth(120);
        templateTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        templateTable.getColumnModel().getColumn(7).setPreferredWidth(150);
        templateTable.getColumnModel().getColumn(8).setPreferredWidth(180);
        
        setupTableStyle(templateTable);
        
        JScrollPane scrollPane = new JScrollPane(templateTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(GRID_COLOR));
        scrollPane.setBackground(MAIN_BACKGROUND);

        reloadButton.addActionListener(e -> loadTemplateData());
        addButton.addActionListener(e -> addNewTemplate());
        editButton.addActionListener(e -> editSelectedTemplate());
        deleteButton.addActionListener(e -> deleteSelectedTemplate());
        searchButton.addActionListener(e -> searchTemplates(searchField.getText()));

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createRecordPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(MAIN_BACKGROUND);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(HEADER_BACKGROUND);
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, GRID_COLOR));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        buttonPanel.setOpaque(false);

        JButton reloadButton = createTextButton("Tải lại", "Tải lại danh sách");
        JButton addButton = createTextButton("Thêm", "Thêm mới Hồ sơ...");
        JButton editButton = createTextButton("Sửa", "Chỉnh sửa Hồ sơ đang chọn");
        JButton deleteButton = createTextButton("Xóa", "Xóa Hồ sơ đang chọn");
        
        buttonPanel.add(reloadButton);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 10));
        searchPanel.setOpaque(false);
        JTextField searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(200, 30));
        JButton searchButton = createTextButton("Tìm", "Tìm kiếm");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        topPanel.add(buttonPanel, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        String[] recordColumnNames = {"Vaccine ID", "Member ID", "Template ID", "Tên Vaccine", "Mũi số", "Ngày tiêm", "Ngày hẹn tiếp", "Số lô", "Trạng thái", "Ghi chú", "Ngày tạo"};
        recordTableModel = new DefaultTableModel(recordColumnNames, 0) { @Override public boolean isCellEditable(int row, int column) { return false; }};
        recordTable = new JTable(recordTableModel);
        recordTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        recordTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        recordTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        recordTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        recordTable.getColumnModel().getColumn(3).setPreferredWidth(180);
        recordTable.getColumnModel().getColumn(4).setPreferredWidth(70);
        recordTable.getColumnModel().getColumn(5).setPreferredWidth(130);
        recordTable.getColumnModel().getColumn(6).setPreferredWidth(130);
        recordTable.getColumnModel().getColumn(7).setPreferredWidth(100);
        recordTable.getColumnModel().getColumn(8).setPreferredWidth(100);
        recordTable.getColumnModel().getColumn(9).setPreferredWidth(130);
        recordTable.getColumnModel().getColumn(10).setPreferredWidth(180);
        
        setupTableStyle(recordTable);
        
        JScrollPane scrollPane = new JScrollPane(recordTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(GRID_COLOR));
        scrollPane.setBackground(MAIN_BACKGROUND);

        reloadButton.addActionListener(e -> loadRecordData());
        addButton.addActionListener(e -> addNewRecord());
        editButton.addActionListener(e -> editSelectedRecord());
        deleteButton.addActionListener(e -> deleteSelectedRecord());
        searchButton.addActionListener(e -> searchRecords(searchField.getText()));

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }



    private void loadTemplateData() {
        try {
            templateTableModel.setRowCount(0);
            ArrayList<VaccineTemplates> templates = templateDao.selectAll();
            for (VaccineTemplates t : templates) {
                templateTableModel.addRow(new Object[] {
                    t.getVaccineTemplateId(), t.getVaccineName(), t.getDescription(),
                    t.getAgeFromDays(), t.getAgeToDays(), t.getIntervalDays(),
                    t.getTotalDoses(), t.getNotes(), t.getCreatedAt()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu Templates: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadRecordData() {
        try {
            recordTableModel.setRowCount(0);
            ArrayList<VaccinationRecords> records = recordDao.selectAll();
            for (VaccinationRecords r : records) {
                recordTableModel.addRow(new Object[] {
                    r.getVaccinationId(), r.getMemberId(), r.getTemplateId(),
                    r.getVaccineName(), r.getDoseNumber(), r.getVaccinationDate(),
                    r.getNextDueDate(), r.getBatchNumber(), r.getStatus(),
                    r.getNotes(), r.getCreatedAt()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu Records: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addNewTemplate() {
        try {
            JTextField nameField = new JTextField();
            JTextField descField = new JTextField();
            JTextField fromDaysField = new JTextField("0");
            JTextField toDaysField = new JTextField("99999");
            JTextField intervalField = new JTextField("0");
            JTextField totalDosesField = new JTextField("1");
            JTextField notesField = new JTextField();
            Object[] message = { "Tên Vaccine:", nameField, "Mô tả:", descField, "Từ ngày tuổi:", fromDaysField, "Đến ngày tuổi:", toDaysField, "Khoảng cách (ngày):", intervalField, "Tổng liều:", totalDosesField, "Ghi chú:", notesField };
            int option = JOptionPane.showConfirmDialog(this, message, "Thêm Vaccine mới", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (option == JOptionPane.OK_OPTION) {
                VaccineTemplates newTemplate = new VaccineTemplates();
                newTemplate.setVaccineName(nameField.getText());
                newTemplate.setDescription(descField.getText());
                newTemplate.setAgeFromDays(Integer.parseInt(fromDaysField.getText()));
                newTemplate.setAgeToDays(Integer.parseInt(toDaysField.getText()));
                newTemplate.setIntervalDays(Integer.parseInt(intervalField.getText()));
                newTemplate.setTotalDoses(Integer.parseInt(totalDosesField.getText()));
                newTemplate.setNotes(notesField.getText());
                newTemplate.setCreatedAt(LocalDateTime.now());
                templateDao.insert(newTemplate);
                loadTemplateData();
                JOptionPane.showMessageDialog(this, "Thêm mới thành công!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editSelectedTemplate() {
        try {
            int selectedRow = templateTable.getSelectedRow();
            if (selectedRow == -1) { JOptionPane.showMessageDialog(this, "Vui lòng chọn một mục để chỉnh sửa!"); return; }
            Integer templateId = (Integer)templateTable.getValueAt(selectedRow, 0);
            
            VaccineTemplates tempQuery = new VaccineTemplates();
            tempQuery.setVaccineTemplateId(templateId);
            VaccineTemplates template = templateDao.selectById(tempQuery);

            if (template == null) { JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu."); loadTemplateData(); return; }

            JTextField nameField = new JTextField(template.getVaccineName());
            JTextField descField = new JTextField(template.getDescription());
            JTextField fromDaysField = new JTextField(String.valueOf(template.getAgeFromDays()));
            JTextField toDaysField = new JTextField(String.valueOf(template.getAgeToDays()));
            JTextField intervalField = new JTextField(String.valueOf(template.getIntervalDays()));
            JTextField totalDosesField = new JTextField(String.valueOf(template.getTotalDoses()));
            JTextField notesField = new JTextField(template.getNotes());
            Object[] message = { "Tên Vaccine:", nameField, "Mô tả:", descField, "Từ ngày tuổi:", fromDaysField, "Đến ngày tuổi:", toDaysField, "Khoảng cách (ngày):", intervalField, "Tổng liều:", totalDosesField, "Ghi chú:", notesField };
            
            int option = JOptionPane.showConfirmDialog(this, message, "Chỉnh sửa Vaccine (ID: " + templateId + ")", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (option == JOptionPane.OK_OPTION) {
                template.setVaccineName(nameField.getText());
                template.setDescription(descField.getText());
                template.setAgeFromDays(Integer.parseInt(fromDaysField.getText()));
                template.setAgeToDays(Integer.parseInt(toDaysField.getText()));
                template.setIntervalDays(Integer.parseInt(intervalField.getText()));
                template.setTotalDoses(Integer.parseInt(totalDosesField.getText()));
                template.setNotes(notesField.getText());

                templateDao.update(template);
                loadTemplateData();
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedTemplate() {
        try {
            int[] selectedRows = templateTable.getSelectedRows();
            if (selectedRows.length == 0) { JOptionPane.showMessageDialog(this, "Vui lòng chọn một hoặc nhiều mục để xóa!"); return; }
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa " + selectedRows.length + " mục đã chọn không?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) { return; }

            List<Integer> idsToDelete = new ArrayList<>();
            for (int row : selectedRows) {
                idsToDelete.add((Integer) templateTable.getValueAt(row, 0));
            }

            for (Integer templateId : idsToDelete) {
                VaccineTemplates tempDelete = new VaccineTemplates();
                tempDelete.setVaccineTemplateId(templateId);
                templateDao.delete(tempDelete);
            }
            loadTemplateData();
            JOptionPane.showMessageDialog(this, "Đã xóa " + idsToDelete.size() + " mục thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchTemplates(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) { loadTemplateData(); return; }
        try {
            keyword = keyword.toLowerCase().trim();
            ArrayList<VaccineTemplates> allTemplates = templateDao.selectAll();
            
            templateTableModel.setRowCount(0);
            for (VaccineTemplates t : allTemplates) {
                if (matchesKeyword(t, keyword)) {
                    templateTableModel.addRow(new Object[] {
                        t.getVaccineTemplateId(), t.getVaccineName(), t.getDescription(),
                        t.getAgeFromDays(), t.getAgeToDays(), t.getIntervalDays(),
                        t.getTotalDoses(), t.getNotes(), t.getCreatedAt()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean matchesKeyword(VaccineTemplates template, String keyword) {
        try {
            int searchId = Integer.parseInt(keyword);
            VaccineTemplates tempQuery = new VaccineTemplates();
            tempQuery.setVaccineTemplateId(searchId);
            VaccineTemplates templateById = templateDao.selectById(tempQuery);
            if (templateById != null && template.getVaccineTemplateId() == templateById.getVaccineTemplateId()) {
                return true;
            }
        } catch (NumberFormatException e) { /* Ignore */ }
        
        return (template.getVaccineName() != null && template.getVaccineName().toLowerCase().contains(keyword)) ||
               (template.getDescription() != null && template.getDescription().toLowerCase().contains(keyword)) ||
               (template.getNotes() != null && template.getNotes().toLowerCase().contains(keyword));
    }

    private void addNewRecord() {
        try {
            JTextField memberIdField = new JTextField();
            JTextField templateIdField = new JTextField();
            JTextField vaccineNameField = new JTextField();
            JTextField doseNumberField = new JTextField("1");
            JTextField vaxDateField = new JTextField(LocalDate.now().toString());
            JTextField nextDueDateField = new JTextField();
            JTextField batchNumberField = new JTextField();
            JTextField statusField = new JTextField("Đã tiêm");
            JTextField notesField = new JTextField();
            Object[] message = { "Member ID:", memberIdField, "Template ID:", templateIdField, "Tên Vaccine:", vaccineNameField, "Mũi số:", doseNumberField, "Ngày tiêm (YYYY-MM-DD):", vaxDateField, "Ngày hẹn tiếp (YYYY-MM-DD):", nextDueDateField, "Số lô:", batchNumberField, "Trạng thái:", statusField, "Ghi chú:", notesField };
            
            int option = JOptionPane.showConfirmDialog(this, message, "Thêm Hồ sơ Tiêm chủng mới", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (option == JOptionPane.OK_OPTION) {
                VaccinationRecords newRecord = new VaccinationRecords();
                newRecord.setMemberId(Integer.parseInt(memberIdField.getText()));
                String templateIdStr = templateIdField.getText();
                newRecord.setTemplateId((templateIdStr != null && !templateIdStr.trim().isEmpty()) ? Integer.parseInt(templateIdStr) : null);
                newRecord.setVaccineName(vaccineNameField.getText());
                newRecord.setDoseNumber(Integer.parseInt(doseNumberField.getText()));
                newRecord.setVaccinationDate(LocalDate.parse(vaxDateField.getText()));
                String nextDueDateStr = nextDueDateField.getText();
                newRecord.setNextDueDate((nextDueDateStr != null && !nextDueDateStr.trim().isEmpty()) ? LocalDate.parse(nextDueDateStr) : null);
                newRecord.setBatchNumber(batchNumberField.getText());
                newRecord.setStatus(statusField.getText());
                newRecord.setNotes(notesField.getText());
                newRecord.setCreatedAt(LocalDateTime.now());
                
                recordDao.insert(newRecord);
                
                loadRecordData();
                JOptionPane.showMessageDialog(this, "Thêm hồ sơ thành công!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editSelectedRecord() {
        try {
            int selectedRow = recordTable.getSelectedRow();
            if (selectedRow == -1) { JOptionPane.showMessageDialog(this, "Vui lòng chọn một hồ sơ để chỉnh sửa!"); return; }
            Integer recordId = (Integer)recordTable.getValueAt(selectedRow, 0);
            
            VaccinationRecords tempQuery = new VaccinationRecords();
            tempQuery.setVaccinationId(recordId);
            VaccinationRecords record = recordDao.selectById(tempQuery);

            if (record == null) { JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu."); loadRecordData(); return; }
            
            JTextField memberIdField = new JTextField(String.valueOf(record.getMemberId()));
            JTextField templateIdField = new JTextField(record.getTemplateId() != null ? String.valueOf(record.getTemplateId()) : "");
            JTextField vaccineNameField = new JTextField(record.getVaccineName());
            JTextField doseNumberField = new JTextField(String.valueOf(record.getDoseNumber()));
            JTextField vaxDateField = new JTextField(record.getVaccinationDate() != null ? record.getVaccinationDate().toString() : "");
            JTextField nextDueDateField = new JTextField(record.getNextDueDate() != null ? record.getNextDueDate().toString() : "");
            JTextField batchNumberField = new JTextField(record.getBatchNumber());
            JTextField statusField = new JTextField(record.getStatus());
            JTextField notesField = new JTextField(record.getNotes());
            Object[] message = { "Member ID:", memberIdField, "Template ID:", templateIdField, "Tên Vaccine:", vaccineNameField, "Mũi số:", doseNumberField, "Ngày tiêm (YYYY-MM-DD):", vaxDateField, "Ngày hẹn tiếp (YYYY-MM-DD):", nextDueDateField, "Số lô:", batchNumberField, "Trạng thái:", statusField, "Ghi chú:", notesField };
            
            int option = JOptionPane.showConfirmDialog(this, message, "Chỉnh sửa Hồ sơ (ID: " + recordId + ")", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (option == JOptionPane.OK_OPTION) {
                record.setMemberId(Integer.parseInt(memberIdField.getText()));
                String templateIdStr = templateIdField.getText();
                record.setTemplateId((templateIdStr != null && !templateIdStr.trim().isEmpty()) ? Integer.parseInt(templateIdStr) : null);
                record.setVaccineName(vaccineNameField.getText());
                record.setDoseNumber(Integer.parseInt(doseNumberField.getText()));
                record.setVaccinationDate(LocalDate.parse(vaxDateField.getText()));
                String nextDueDateStr = nextDueDateField.getText();
                record.setNextDueDate((nextDueDateStr != null && !nextDueDateStr.trim().isEmpty()) ? LocalDate.parse(nextDueDateStr) : null);
                record.setBatchNumber(batchNumberField.getText());
                record.setStatus(statusField.getText());
                record.setNotes(notesField.getText());

                recordDao.update(record);
                loadRecordData();
                JOptionPane.showMessageDialog(this, "Cập nhật hồ sơ thành công!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedRecord() {
        try {
            int[] selectedRows = recordTable.getSelectedRows();
            if (selectedRows.length == 0) { JOptionPane.showMessageDialog(this, "Vui lòng chọn một hoặc nhiều hồ sơ để xóa!"); return; }
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa " + selectedRows.length + " hồ sơ đã chọn không?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) { return; }
            
            List<Integer> idsToDelete = new ArrayList<>();
            for (int row : selectedRows) {
                idsToDelete.add((Integer) recordTable.getValueAt(row, 0));
            }
            
            for (Integer recordId : idsToDelete) {
                VaccinationRecords tempDelete = new VaccinationRecords();
                tempDelete.setVaccinationId(recordId);
                recordDao.delete(tempDelete);
            }
            loadRecordData();
            JOptionPane.showMessageDialog(this, "Đã xóa " + idsToDelete.size() + " hồ sơ thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchRecords(String keyword) {
         if (keyword == null || keyword.trim().isEmpty()) { loadRecordData(); return; }
        try {
            keyword = keyword.toLowerCase().trim();
            ArrayList<VaccinationRecords> allRecords = recordDao.selectAll();
            recordTableModel.setRowCount(0);
            for (VaccinationRecords r : allRecords) {
                if (matchesKeyword(r, keyword)) {
                    recordTableModel.addRow(new Object[] {
                        r.getVaccinationId(), r.getMemberId(), r.getTemplateId(),
                        r.getVaccineName(), r.getDoseNumber(), r.getVaccinationDate(),
                        r.getNextDueDate(), r.getBatchNumber(), r.getStatus(),
                        r.getNotes(), r.getCreatedAt()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean matchesKeyword(VaccinationRecords record, String keyword) {
        try {
            int searchId = Integer.parseInt(keyword);
            VaccinationRecords tempQuery = new VaccinationRecords();
            tempQuery.setVaccinationId(searchId);
            if(recordDao.selectById(tempQuery) != null) return true;
        } catch (NumberFormatException e) { /* Ignore */ }
        
        return (record.getVaccineName() != null && record.getVaccineName().toLowerCase().contains(keyword)) ||
               (record.getBatchNumber() != null && record.getBatchNumber().toLowerCase().contains(keyword)) ||
               (record.getStatus() != null && record.getStatus().toLowerCase().contains(keyword)) ||
               (record.getNotes() != null && record.getNotes().toLowerCase().contains(keyword));
    }

 
    private JButton createSidebarButton(String text, Icon icon) { 
        JButton button = new JButton(text); 
        button.setForeground(SIDEBAR_TEXT_COLOR); 
        button.setBackground(SIDEBAR_BACKGROUND); 
        button.setOpaque(true);                   
        button.setBorderPainted(false);           
        button.setFocusPainted(false);
        
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(SIDEBAR_HOVER_COLOR); 
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SIDEBAR_BACKGROUND); 
            }
        });
        return button;
    }
    
 
    private JButton createTextButton(String text, String toolTipText) {
        JButton button = new JButton(text);
        button.setToolTipText(toolTipText);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setForeground(TEXT_COLOR);
        button.setPreferredSize(new Dimension(80, 30)); 
        button.setOpaque(true);
        button.setBackground(new Color(230, 230, 230));
        button.setBorder(BorderFactory.createLineBorder(GRID_COLOR, 1)); 
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_HOVER_BACKGROUND_TEXT);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(230, 230, 230));
            }
        });
        return button;
    }


    private void setupTableStyle(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setBackground(TABLE_HEADER_BACKGROUND);
        header.setForeground(TEXT_COLOR);
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBorder(BorderFactory.createLineBorder(GRID_COLOR));
        header.setPreferredSize(new Dimension(100, 35));
        table.setShowGrid(true);
        table.setGridColor(GRID_COLOR);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setSelectionBackground(new Color(232, 242, 254));
        table.setSelectionForeground(TEXT_COLOR);
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? MAIN_BACKGROUND : ZEBRA_STRIPE_COLOR);
                    c.setForeground(TEXT_COLOR);
                }
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                return c;
            }
        });
    }
}