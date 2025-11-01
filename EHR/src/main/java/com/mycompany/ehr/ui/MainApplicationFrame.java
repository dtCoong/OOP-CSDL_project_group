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
import java.util.ArrayList;


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
        
        JButton templatesButton = createSidebarButton("Các loại vaccine hiện có và Lịch tiêm chuẩn", null);
        templatesButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "TEMPLATES");
            headerLabel.setText("Các loại vaccine hiện có và Lịch tiêm chuẩn");
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

        buttonPanel.add(reloadButton);

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
        buttonPanel.add(reloadButton);

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
        recordTableModel = new DefaultTableModel(recordColumnNames, 0) { 
            @Override 
            public boolean isCellEditable(int row, int column) { 
                return false; 
            }
        };
        recordTable = new JTable(recordTableModel);
        recordTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        // --- CẤU HÌNH ĐỘ RỘNG CỘT MỚI ---
        recordTable.getColumnModel().getColumn(0).setPreferredWidth(80); // Vaccine ID

        // === ẨN CỘT MEMBER ID (INDEX 1) ===
        recordTable.getColumnModel().getColumn(1).setMinWidth(0);
        recordTable.getColumnModel().getColumn(1).setMaxWidth(0);
        recordTable.getColumnModel().getColumn(1).setPreferredWidth(0);

        // === ẨN CỘT TEMPLATE ID (INDEX 2) ===
        recordTable.getColumnModel().getColumn(2).setMinWidth(0);
        recordTable.getColumnModel().getColumn(2).setMaxWidth(0);
        recordTable.getColumnModel().getColumn(2).setPreferredWidth(0);

        // === CÁC CỘT CÒN LẠI HIỂN THỊ BÌNH THƯỜNG (Bắt đầu từ index 3) ===
        recordTable.getColumnModel().getColumn(3).setPreferredWidth(180); // Tên Vaccine
        recordTable.getColumnModel().getColumn(4).setPreferredWidth(70);  // Mũi số
        recordTable.getColumnModel().getColumn(5).setPreferredWidth(130); // Ngày tiêm
        recordTable.getColumnModel().getColumn(6).setPreferredWidth(130); // Ngày hẹn tiếp
        recordTable.getColumnModel().getColumn(7).setPreferredWidth(100); // Số lô
        recordTable.getColumnModel().getColumn(8).setPreferredWidth(100); // Trạng thái
        recordTable.getColumnModel().getColumn(9).setPreferredWidth(130); // Ghi chú
        recordTable.getColumnModel().getColumn(10).setPreferredWidth(180); // Ngày tạo
        // --- HẾT CẤU HÌNH ĐỘ RỘNG CỘT ---
        setupTableStyle(recordTable);
        
        JScrollPane scrollPane = new JScrollPane(recordTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(GRID_COLOR));
        scrollPane.setBackground(MAIN_BACKGROUND);

        reloadButton.addActionListener(e -> loadRecordData());
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