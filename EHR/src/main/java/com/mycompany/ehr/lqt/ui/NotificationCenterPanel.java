package com.mycompany.ehr.lqt.ui;

import com.mycompany.ehr.lqt.dao.NotificationsDao;
import com.mycompany.ehr.lqt.model.NotificationPriority;
import com.mycompany.ehr.lqt.model.NotificationType;
import com.mycompany.ehr.lqt.model.Notifications;
import net.miginfocom.swing.MigLayout;

// THÊM MỚI: Import User model (từ dtc) và HomeFrame
import com.mycompany.ehr.dtc.model.User;
import com.mycompany.ehr.dtc.ui.HomeFrame;
import com.mycompany.ehr.dtc.dao.FamilyMembersDAO;
import com.mycompany.ehr.dtc.model.FamilyMembers;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

public class NotificationCenterPanel extends JPanel {
    
    private static final Color PRIMARY_COLOR = new Color(13, 71, 161);
    private static final Color ACCENT_COLOR = new Color(0, 121, 107);
    private static final Color ERROR_COLOR = new Color(211, 47, 47);
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private static final Color WARNING_COLOR = new Color(255, 152, 0);
    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(189, 189, 189);
    private static final Color TEXT_PRIMARY = new Color(13, 13, 13);
    private static final Color TEXT_SECONDARY = new Color(66, 66, 66);
    private static final Color UNREAD_BG = new Color(232, 244, 253);
    
    private final JFrame owner;
    private final NotificationsDao dao = new NotificationsDao();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    private final JTextField filterRecipient;
    private final JComboBox<Object> filterType;
    private final JComboBox<String> filterStatus;
    private final JComboBox<String> filterPriority;
    private final JTextField filterTitle;
    private final JLabel unreadBadge;
    private final JLabel totalLabel;
    private final DefaultTableModel model;
    private final JTable table;
    private final TableRowSorter<DefaultTableModel> sorter;
    private final JTextArea detailArea;
    private final JButton btnMarkRead;
    private final JButton btnMarkUnread;
    private final JButton btnDelete;
    private final JButton btnMarkAllRead;
    private final JButton btnRefresh;
    private final JButton btnExport;
    private final JButton btnSettings;
    private final JButton btnResponse;
    
    // SỬA: Thêm biến để lưu User
    private final Integer currentUserId;
    private final User currentUser; // Giữ lại User để quay lại

    // Constructor chính (cho HomeFrame - xem thông báo cá nhân)
    public NotificationCenterPanel(JFrame owner, User currentUser) {
        this.owner = owner;
        this.currentUser = currentUser;
        // Giữ nguyên logic mock ID của bạn nếu currentUser là null
        this.currentUserId = (currentUser != null) ? currentUser.getUserId() : 1; 
        
        // Khởi tạo các component (lấy từ file gốc)
        filterRecipient = new JTextField();
        filterType = new JComboBox<>();
        filterStatus = new JComboBox<>(new String[]{"Tất cả", "Chưa đọc", "Đã đọc"});
        filterPriority = new JComboBox<>();
        filterTitle = new JTextField();
        unreadBadge = new JLabel("0");
        totalLabel = new JLabel("0 thông báo");
        model = new DefaultTableModel(
            new String[]{"ID", "Người nhận", "Tiêu đề", "Loại", "Ưu tiên", "Trạng thái", "Ngày tạo", "Tài liệu", "Cuộc hẹn"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        sorter = new TableRowSorter<>(model);
        detailArea = new JTextArea();
        btnMarkRead = new JButton("Đánh dấu đã đọc");
        btnMarkUnread = new JButton("Đánh dấu chưa đọc");
        btnDelete = new JButton("Xóa");
        btnMarkAllRead = new JButton("Đọc tất cả");
        btnRefresh = new JButton("Tải lại");
        btnExport = new JButton("Xuất CSV");
        btnSettings = new JButton("Cài đặt");
        btnResponse = new JButton("Phản hồi");

        // Code layout
        setLayout(new BorderLayout(0, 0));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(0, 0, 0, 0));
        
        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildStatus(), BorderLayout.SOUTH);
        
        setupFilters();
        
        // GIỮ NGUYÊN: Logic ẩn cột/filter của bạn
        if (this.currentUserId != null && this.currentUserId != 1) { // Giả sử 1 là admin
            filterRecipient.setVisible(false);
            TableColumn recipientCol = table.getColumnModel().getColumn(1);
            recipientCol.setMinWidth(0);
            recipientCol.setMaxWidth(0);
            recipientCol.setPreferredWidth(0);
            recipientCol.setResizable(false);
            
            try {
                Component[] filterComponents = filterRecipient.getParent().getComponents();
                for (int i = 0; i < filterComponents.length; i++) {
                    if (filterComponents[i] == filterRecipient && i > 0) {
                        filterComponents[i-1].setVisible(false);
                        break;
                    }
                }
            } catch (Exception e) { /* Bỏ qua */ }
        }
        
        loadData();
        updateUnreadBadge();
    }

    private JPanel buildHeader() {
        JPanel main = new JPanel(new BorderLayout(0, 12));
        main.setBackground(BACKGROUND_COLOR);
        main.setBorder(new EmptyBorder(12, 12, 0, 12));
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                float[] dist = {0.0f, 0.5f, 1.0f};
                Color[] colors = {PRIMARY_COLOR, ACCENT_COLOR, new Color(103, 58, 183)};
                LinearGradientPaint gradient = new LinearGradientPaint(0, 0, w, 0, dist, colors);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, w, h);
                g2d.dispose();
            }
        };
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(20, 24, 20, 24));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        titlePanel.setOpaque(false);
        JLabel icon = new JLabel("🔔");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        JLabel title = new JLabel("Trung tâm Thông báo");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        JLabel subtitle = new JLabel("Quản lý tất cả thông báo trong hệ thống");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(255, 255, 255, 200));
        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(subtitle);
        titlePanel.add(icon);
        titlePanel.add(textPanel);
        JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        badgePanel.setOpaque(false);
        unreadBadge.setOpaque(true);
        unreadBadge.setBackground(ERROR_COLOR);
        unreadBadge.setForeground(Color.WHITE);
        unreadBadge.setFont(new Font("Segoe UI", Font.BOLD, 18));
        unreadBadge.setBorder(new EmptyBorder(8, 16, 8, 16));
        JPanel badgeContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badgeContainer.setLayout(new BorderLayout());
        badgeContainer.setOpaque(false);
        badgeContainer.setBackground(ERROR_COLOR);
        badgeContainer.add(unreadBadge, BorderLayout.CENTER);
        JLabel badgeLabel = new JLabel("Chưa đọc: ");
        badgeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        badgeLabel.setForeground(Color.WHITE);
        badgePanel.add(badgeLabel);
        badgePanel.add(badgeContainer);
        header.add(titlePanel, BorderLayout.WEST);
        header.add(badgePanel, BorderLayout.EAST);
        JPanel filterPanel = buildFilterPanel();
        main.add(header, BorderLayout.NORTH);
        main.add(filterPanel, BorderLayout.SOUTH);
        return main;
    }

    private JPanel buildFilterPanel() {
        JPanel outerPanel = new JPanel(new BorderLayout(0, 0));
        outerPanel.setBackground(BACKGROUND_COLOR);
        JPanel panel = new JPanel(new BorderLayout(8, 8)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                float[] dist = {0.0f, 0.5f, 1.0f};
                Color[] colors = {PRIMARY_COLOR, ACCENT_COLOR, new Color(103, 58, 183)};
                LinearGradientPaint gradient = new LinearGradientPaint(0, 0, w, 0, dist, colors);
                g2d.setPaint(gradient);
                g2d.setStroke(new BasicStroke(3f));
                g2d.drawRoundRect(1, 1, w - 3, h - 3, 12, 12);
                g2d.dispose();
            }
        };
        panel.setOpaque(true);
        panel.setBackground(new Color(250, 252, 255));
        panel.setBorder(new EmptyBorder(18, 24, 20, 24));
        JPanel filterHeader = new JPanel(new BorderLayout(12, 0));
        filterHeader.setOpaque(false);
        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftHeader.setOpaque(false);
        JPanel filterIconLabel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int size = 36;
                GradientPaint gradient = new GradientPaint(0, 0, PRIMARY_COLOR, size, size, ACCENT_COLOR);
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, size, size, 8, 8);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawOval(10, 10, 12, 12);
                g2.drawLine(20, 20, 26, 26);
                g2.dispose();
            }
        };
        filterIconLabel.setPreferredSize(new Dimension(36, 36));
        filterIconLabel.setOpaque(false);
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        JLabel filterLabel = new JLabel("Bộ lọc & Tìm kiếm");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        filterLabel.setForeground(PRIMARY_COLOR);
        JLabel filterSubLabel = new JLabel("Lọc và tìm kiếm thông báo theo nhiều tiêu chí");
        filterSubLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        filterSubLabel.setForeground(TEXT_SECONDARY);
        textPanel.add(filterLabel);
        textPanel.add(filterSubLabel);
        leftHeader.add(filterIconLabel);
        leftHeader.add(Box.createHorizontalStrut(12));
        leftHeader.add(textPanel);
        filterHeader.add(leftHeader, BorderLayout.WEST);
        JPanel filterInputs = new JPanel(new MigLayout("insets 16 0 8 0", 
            "[110!][fill,220:280:350][110!][fill,180:220:280][110!][fill,180:220:280][110!][fill,220:280:350]"));
        filterInputs.setOpaque(false);
        filterType.addItem("Tất cả loại");
        for (NotificationType type : NotificationType.values()) {
            filterType.addItem(type);
        }
        filterPriority.addItem("Tất cả mức");
        for (NotificationPriority priority : NotificationPriority.values()) {
            filterPriority.addItem(priority.getDisplayName());
        }
        styleFilterTextField(filterRecipient, new Color(33, 150, 243));
        styleFilterTextField(filterTitle, new Color(156, 39, 176));
        styleFilterComboBox(filterType, new Color(0, 150, 136));
        styleFilterComboBox(filterPriority, new Color(255, 152, 0));
        styleFilterComboBox(filterStatus, new Color(76, 175, 80));
        filterInputs.add(createColoredLabel("Nguoi nhan", new Color(33, 150, 243)), "align left");
        filterInputs.add(filterRecipient, "growx");
        filterInputs.add(createColoredLabel("Loai", new Color(0, 150, 136)), "align left");
        filterInputs.add(filterType, "growx");
        filterInputs.add(createColoredLabel("Uu tien", new Color(255, 152, 0)), "align left");
        filterInputs.add(filterPriority, "growx");
        filterInputs.add(createColoredLabel("Tieu de", new Color(156, 39, 176)), "align left");
        filterInputs.add(filterTitle, "growx, wrap 12");
        filterInputs.add(createColoredLabel("Trang thai", new Color(76, 175, 80)), "skip 6, align left");
        filterInputs.add(filterStatus, "growx");
        panel.add(filterHeader, BorderLayout.NORTH);
        panel.add(filterInputs, BorderLayout.CENTER);
        outerPanel.add(panel, BorderLayout.CENTER);
        return outerPanel;
    }
    
    private void styleFilterTextField(JTextField textField, Color accentColor) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(10, 14, 10, 14)
        ));
        textField.setBackground(Color.WHITE);
        textField.setForeground(TEXT_PRIMARY);
        textField.setPreferredSize(new Dimension(textField.getPreferredSize().width, 42));
        textField.setMinimumSize(new Dimension(180, 42));
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor, 2, true),
                    new EmptyBorder(9, 13, 9, 13)
                ));
            }
            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                    new EmptyBorder(10, 14, 10, 14)
                ));
            }
        });
    }
    
    private void styleFilterComboBox(JComboBox<?> comboBox, Color accentColor) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(TEXT_PRIMARY);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        comboBox.setPreferredSize(new Dimension(comboBox.getPreferredSize().width, 42));
        comboBox.setMinimumSize(new Dimension(150, 42));
        comboBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                comboBox.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor, 2, true),
                    new EmptyBorder(7, 11, 7, 11)
                ));
            }
            @Override
            public void focusLost(FocusEvent e) {
                comboBox.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                    new EmptyBorder(8, 12, 8, 12)
                ));
            }
        });
    }
    
    private JLabel createColoredLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(color);
        label.setPreferredSize(new Dimension(110, 24));
        label.setMinimumSize(new Dimension(110, 24));
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, color),
            new EmptyBorder(0, 10, 0, 5)
        ));
        return label;
    }

    private JPanel buildCenter() {
        JPanel main = new JPanel(new BorderLayout(0, 0));
        main.setBackground(BACKGROUND_COLOR);
        main.setBorder(new EmptyBorder(12, 12, 12, 12));
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        toolbar.setBackground(CARD_COLOR);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        stylePrimaryButton(btnMarkRead, SUCCESS_COLOR);
        stylePrimaryButton(btnMarkUnread, WARNING_COLOR);
        stylePrimaryButton(btnDelete, ERROR_COLOR);
        stylePrimaryButton(btnResponse, new Color(103, 58, 183));
        styleSecondaryButton(btnMarkAllRead);
        styleSecondaryButton(btnRefresh);
        styleSecondaryButton(btnExport);
        styleSecondaryButton(btnSettings);
        toolbar.add(btnMarkRead);
        toolbar.add(btnMarkUnread);
        toolbar.add(btnDelete);
        toolbar.add(btnResponse);
        toolbar.add(createVerticalSeparator());
        toolbar.add(btnMarkAllRead);
        toolbar.add(btnRefresh);
        toolbar.add(createVerticalSeparator());
        toolbar.add(btnExport);
        toolbar.add(btnSettings);
        table.setRowSorter(sorter);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setGridColor(new Color(240, 240, 240));
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        sorter.toggleSortOrder(0);
        sorter.toggleSortOrder(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(300);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(150);
        table.getColumnModel().getColumn(7).setPreferredWidth(80);
        table.getColumnModel().getColumn(8).setPreferredWidth(80);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 42));
        header.setBorder(BorderFactory.createEmptyBorder());
        final Color[] COLUMN_COLORS = {
            new Color(33, 150, 243), new Color(156, 39, 176), new Color(244, 67, 54),
            new Color(0, 150, 136), new Color(255, 152, 0), new Color(76, 175, 80),
            new Color(103, 58, 183), new Color(0, 188, 212), new Color(233, 30, 99)
        };
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                final int col = column;
                JLabel label = new JLabel(value != null ? value.toString() : "") {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        Color baseColor = COLUMN_COLORS[col];
                        Color lighterColor = new Color(
                            Math.min(255, baseColor.getRed() + 40),
                            Math.min(255, baseColor.getGreen() + 40),
                            Math.min(255, baseColor.getBlue() + 40)
                        );
                        GradientPaint gradient = new GradientPaint(0, 0, baseColor, 0, getHeight(), lighterColor);
                        g2.setPaint(gradient);
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                label.setOpaque(false);
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                label.setBorder(new EmptyBorder(0, 10, 0, 10));
                label.setHorizontalAlignment(SwingConstants.LEFT);
                return label;
            }
        });
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            final Color[] COLUMN_BG_LIGHT = {
                new Color(227, 242, 253), new Color(243, 229, 245), new Color(255, 235, 238),
                new Color(224, 242, 241), new Color(255, 243, 224), new Color(232, 245, 233),
                new Color(237, 231, 246), new Color(224, 247, 250), new Color(252, 228, 236)
            };
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Object statusObj = table.getValueAt(row, 5);
                boolean isRead = statusObj != null && statusObj.toString().contains("Đã đọc");
                if (isSelected) {
                    setBackground(new Color(179, 229, 252));
                    setForeground(TEXT_PRIMARY);
                    setFont(getFont().deriveFont(Font.BOLD));
                } else {
                    if (isRead) {
                        if (row % 2 == 0) setBackground(Color.WHITE);
                        else setBackground(COLUMN_BG_LIGHT[column]);
                        setFont(getFont().deriveFont(Font.PLAIN));
                    } else {
                        setBackground(COLUMN_BG_LIGHT[column]);
                        setFont(getFont().deriveFont(Font.BOLD));
                    }
                    setForeground(TEXT_PRIMARY);
                }
                Color borderColor = new Color(
                    COLUMN_BG_LIGHT[column].getRed() - 40,
                    COLUMN_BG_LIGHT[column].getGreen() - 40,
                    COLUMN_BG_LIGHT[column].getBlue() - 40
                );
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 3, 0, 0, borderColor),
                    new EmptyBorder(6, 10, 6, 8)
                ));
                return c;
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        scrollPane.getViewport().setBackground(Color.WHITE);
        JPanel detailPanel = createDetailPanel();
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, detailPanel);
        splitPane.setResizeWeight(0.70);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setDividerSize(8);
        main.add(toolbar, BorderLayout.NORTH);
        main.add(splitPane, BorderLayout.CENTER);
        setupListeners();
        return main;
    }
    private JPanel createDetailPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(CARD_COLOR);
        panel.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(248, 250, 252));
        header.setBorder(new EmptyBorder(12, 16, 12, 16));
        JLabel headerLabel = new JLabel("Chi tiết thông báo");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerLabel.setForeground(PRIMARY_COLOR);
        header.add(headerLabel, BorderLayout.WEST);
        detailArea.setEditable(false);
        detailArea.setLineWrap(true);
        detailArea.setWrapStyleWord(true);
        detailArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        detailArea.setBorder(new EmptyBorder(12, 16, 12, 16));
        detailArea.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(detailArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(header, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // SỬA: Thêm nút Quay lại vào thanh Status
    private JPanel buildStatus() {
        JPanel status = new JPanel(new BorderLayout(12, 8)); // Đổi sang BorderLayout
        status.setBackground(CARD_COLOR);
        status.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
            new EmptyBorder(4, 12, 4, 12)
        ));
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        infoPanel.setOpaque(false);
        totalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        totalLabel.setForeground(TEXT_SECONDARY);
        JLabel separator = new JLabel("|");
        separator.setForeground(BORDER_COLOR);
        JLabel timeLabel = new JLabel("Cập nhật: " + LocalDateTime.now().format(dateFormatter));
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        timeLabel.setForeground(TEXT_SECONDARY);
        infoPanel.add(totalLabel);
        infoPanel.add(separator);
        infoPanel.add(timeLabel);
        status.add(infoPanel, BorderLayout.WEST);

        // THÊM MỚI: Nút Quay lại
        // Chỉ thêm nút Quay lại nếu nó được mở từ HomeFrame (currentUser != null)
        if (currentUser != null) {
            JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            backButtonPanel.setOpaque(false);
            
            JButton btnBack = new JButton("Quay lại Trang chủ");
            styleSecondaryButton(btnBack); // Dùng style nút phụ
            
            btnBack.addActionListener(e -> {
                // Mở lại HomeFrame
                HomeFrame home = new HomeFrame(currentUser);
                home.setVisible(true);
                // Đóng cửa sổ Hòm thư hiện tại
                SwingUtilities.getWindowAncestor(this).dispose();
            });
            
            backButtonPanel.add(btnBack);
            status.add(backButtonPanel, BorderLayout.EAST);
        }
        
        return status;
    }

    private void setupListeners() {
        btnMarkRead.addActionListener(e -> markSelectedAsRead(true));
        btnMarkUnread.addActionListener(e -> markSelectedAsRead(false));
        btnDelete.addActionListener(e -> deleteSelected());
        btnMarkAllRead.addActionListener(e -> markAllAsRead());
        btnRefresh.addActionListener(e -> { loadData(); updateUnreadBadge(); });
        btnExport.addActionListener(e -> exportToCSV());
        btnSettings.addActionListener(e -> showSettings());
        btnResponse.addActionListener(e -> showResponseDialog());
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showSelectedNotificationDetail();
            }
        });
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showNotificationDetail();
                }
            }
        });
    }

    private void setupFilters() {
        Runnable applyFilter = this::applyFilters;
        filterRecipient.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { applyFilter.run(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { applyFilter.run(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { applyFilter.run(); }
        });
        filterType.addActionListener(e -> applyFilter.run());
        filterStatus.addActionListener(e -> applyFilter.run());
        filterPriority.addActionListener(e -> applyFilter.run());
        filterTitle.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { applyFilter.run(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { applyFilter.run(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { applyFilter.run(); }
        });
    }

    // SỬA: Cập nhật loadData() để lọc theo currentUserId
    private void loadData() {
        model.setRowCount(0);
        List<Notifications> all;
        if (this.currentUserId != null) {
            all = dao.getByRecipient(this.currentUserId);
        } else {
            all = dao.selectAll();
        }
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < all.size(); i++) {
            Notifications n = all.get(i);
            String typeDisplay = (n.getType() != null) ? n.getType().getDisplayName() : "";
            String priorityDisplay = (n.getPriority() != null) ? n.getPriority().getDisplayName() : "";
            String statusDisplay = n.getIsRead() ? "Đã đọc" : "Chưa đọc";
            LocalDateTime displayTime = (n.getCreatedAt() != null) ? n.getCreatedAt() : LocalDateTime.now();
            int daysAgo = i % 30;
            int hoursOffset = (i * 3) % 24;
            int minutesOffset = (i * 7) % 60;
            displayTime = displayTime.minusDays(daysAgo).minusHours(hoursOffset).minusMinutes(minutesOffset);
            
            // Lấy tên người nhận từ member_id
            String recipientName = getMemberName(n.getMemberId());
            
            model.addRow(new Object[]{
                n.getNotificationId(),
                recipientName,
                n.getTitle(),
                typeDisplay,
                priorityDisplay,
                statusDisplay,
                displayTime.format(dateFormatter),
                n.getRelatedDocumentId() != null ? "#" + n.getRelatedDocumentId() : "",
                n.getRelatedAppointmentId() != null ? "#" + n.getRelatedAppointmentId() : ""
            });
        }
        totalLabel.setText(all.size() + " thông báo");
    }

    // SỬA: Cập nhật applyFilters()
    private void applyFilters() {
        RowFilter<DefaultTableModel, Integer> filter = new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                if (currentUserId == null) { 
                    String recipient = filterRecipient.getText().trim();
                    if (!recipient.isEmpty()) {
                        String cellRecipient = entry.getStringValue(1);
                        if (!cellRecipient.toLowerCase().contains(recipient.toLowerCase())) {
                            return false;
                        }
                    }
                }
                if (filterType.getSelectedIndex() > 0) {
                    NotificationType selectedType = (NotificationType) filterType.getSelectedItem();
                    String cellType = entry.getStringValue(3);
                    if (!cellType.contains(selectedType.getDisplayName())) return false;
                }
                if (filterStatus.getSelectedIndex() > 0) {
                    String selectedStatus = (String) filterStatus.getSelectedItem();
                    String cellStatus = entry.getStringValue(5);
                    if (selectedStatus.equals("Chưa đọc") && !cellStatus.contains("Chưa đọc")) return false;
                    if (selectedStatus.equals("Đã đọc") && !cellStatus.contains("Đã đọc")) return false;
                }
                if (filterPriority.getSelectedIndex() > 0) {
                    String selectedPriorityStr = (String) filterPriority.getSelectedItem();
                    String cellPriority = entry.getStringValue(4);
                    if (!cellPriority.equals(selectedPriorityStr)) return false;
                }
                String title = filterTitle.getText().trim();
                if (!title.isEmpty()) {
                    String cellTitle = entry.getStringValue(2);
                    if (!cellTitle.toLowerCase().contains(title.toLowerCase())) return false;
                }
                return true;
            }
        };
        sorter.setRowFilter(filter);
    }

    // SỬA: Cập nhật updateUnreadBadge()
    private void updateUnreadBadge() {
        int unreadCount = 0;
        try {
            if (this.currentUserId != null) {
                unreadCount = dao.countUnread(this.currentUserId);
            } else {
                List<Notifications> all = dao.selectAll();
                unreadCount = (int) all.stream().filter(n -> n.getIsRead() != null && !n.getIsRead()).count();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        unreadBadge.setText(String.valueOf(unreadCount));
    }

    private void markSelectedAsRead(boolean read) {
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thông báo cần đánh dấu!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        for (int row : selectedRows) {
            int modelRow = table.convertRowIndexToModel(row);
            Integer notifId = (Integer) model.getValueAt(modelRow, 0);
            if (read) {
                dao.markAsRead(notifId);
            } else {
                Notifications n = new Notifications();
                n.setNotificationId(notifId);
                Notifications existing = dao.selectById(n);
                if (existing != null) {
                    existing.setIsRead(false);
                    existing.setReadAt(null);
                    dao.update(existing);
                }
            }
        }
        loadData();
        updateUnreadBadge();
        JOptionPane.showMessageDialog(this, "Đã cập nhật " + selectedRows.length + " thông báo!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteSelected() {
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thông báo cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa " + selectedRows.length + " thông báo?", "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            List<Integer> ids = new ArrayList<>();
            for (int row : selectedRows) {
                int modelRow = table.convertRowIndexToModel(row);
                Integer notifId = (Integer) model.getValueAt(modelRow, 0);
                ids.add(notifId);
            }
            int deleted = dao.deleteByIds(ids);
            loadData();
            updateUnreadBadge();
            JOptionPane.showMessageDialog(this, "Đã xóa " + deleted + " thông báo!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // SỬA: Cập nhật markAllAsRead()
    private void markAllAsRead() {
        // Chỉ đánh dấu đã đọc cho user hiện tại (nếu có)
        if (this.currentUserId == null) {
             // (Giữ nguyên logic cũ của bạn, vì 1 là mock ID)
            if (this.currentUser == null) {
                 JOptionPane.showMessageDialog(this, "Không thể đánh dấu tất cả đã đọc ở chế độ xem tất cả.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                 return;
            }
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Đánh dấu tất cả thông báo là đã đọc?", "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            int updated = dao.markAllAsRead(this.currentUserId);
            loadData();
            updateUnreadBadge();
            JOptionPane.showMessageDialog(this, "Đã đánh dấu " + updated + " thông báo là đã đọc!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showSelectedNotificationDetail() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            detailArea.setText("");
            return;
        }
        int modelRow = table.convertRowIndexToModel(selectedRow);
        Integer notifId = (Integer) model.getValueAt(modelRow, 0);
        Notifications n = new Notifications();
        n.setNotificationId(notifId);
        Notifications notification = dao.selectById(n);
        if (notification != null) {
            StringBuilder detail = new StringBuilder();
            detail.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            detail.append("THÔNG TIN CHI TIẾT THÔNG BÁO\n");
            detail.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
            detail.append("ID: ").append(notification.getNotificationId()).append("\n\n");
            detail.append("Người nhận: ").append(notification.getRecipientName() != null ? notification.getRecipientName() : "User #" + notification.getRecipientUserId()).append("\n\n");
            detail.append("Tiêu đề: ").append(notification.getTitle()).append("\n\n");
            detail.append("Nội dung:\n").append(notification.getMessage() != null ? notification.getMessage() : "(Không có)").append("\n\n");
            detail.append("Loại: ").append(notification.getType() != null ? notification.getType().getDisplayName() : "").append("\n\n");
            detail.append("Ưu tiên: ").append(notification.getPriority() != null ? notification.getPriority().getDisplayName() : "").append("\n\n");
            detail.append("Ngày tạo: ").append(notification.getCreatedAt() != null ? notification.getCreatedAt().format(dateFormatter) : "").append("\n\n");
            detail.append("Trạng thái: ").append(notification.getIsRead() ? "Đã đọc" : "Chưa đọc").append("\n\n");
            if (notification.getReadAt() != null) {
                detail.append("👁️ Đã đọc lúc: ").append(notification.getReadAt().format(dateFormatter)).append("\n\n");
            }
            if (notification.getRelatedDocumentId() != null) {
                detail.append("📄 Tài liệu liên quan: #").append(notification.getRelatedDocumentId()).append("\n\n");
            }
            if (notification.getRelatedAppointmentId() != null) {
                detail.append("📅 Cuộc hẹn liên quan: #").append(notification.getRelatedAppointmentId()).append("\n\n");
            }
            if (notification.getRequiresResponse()) {
                detail.append("⚠️ YÊU CẦU PHẢN HỒI\n\n");
                if (notification.getResponseText() != null) {
                    detail.append("💬 Phản hồi: ").append(notification.getResponseText()).append("\n");
                    detail.append("🕐 Phản hồi lúc: ").append(notification.getRespondedAt() != null ? notification.getRespondedAt().format(dateFormatter) : "").append("\n\n");
                }
            }
            detail.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            detailArea.setText(detail.toString());
            detailArea.setCaretPosition(0);
        }
    }

    private void showNotificationDetail() {
        showSelectedNotificationDetail();
    }

    private void showResponseDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn thông báo cần phản hồi!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(selectedRow);
        Integer notifId = (Integer) model.getValueAt(modelRow, 0);
        String response = JOptionPane.showInputDialog(this, "Nhập phản hồi của bạn:", "Phản hồi thông báo", JOptionPane.PLAIN_MESSAGE);
        if (response != null && !response.trim().isEmpty()) {
            dao.addResponse(notifId, response.trim());
            loadData();
            showSelectedNotificationDetail();
            JOptionPane.showMessageDialog(this, "Đã gửi phản hồi thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Xuất dữ liệu ra file CSV");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));
        fileChooser.setSelectedFile(new java.io.File("notifications_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv"));
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getAbsolutePath().endsWith(".csv")) {
                fileToSave = new java.io.File(fileToSave.getAbsolutePath() + ".csv");
            }
            try (java.io.PrintWriter writer = new java.io.PrintWriter(
                    new java.io.OutputStreamWriter(new java.io.FileOutputStream(fileToSave), java.nio.charset.StandardCharsets.UTF_8))) {
                writer.write('\ufeff'); // BOM
                writer.println("ID,Người nhận,Tiêu đề,Loại,Ưu tiên,Trạng thái,Ngày tạo,Tài liệu,Cuộc hẹn,Nội dung");
                List<Notifications> all = (currentUserId != null) ? dao.getByRecipient(currentUserId) : dao.selectAll();
                for (Notifications n : all) {
                    String recipientName = n.getRecipientName() != null ? n.getRecipientName() : ("User #" + n.getRecipientUserId());
                    String type = n.getType() != null ? n.getType().getDisplayName() : "";
                    String priority = n.getPriority() != null ? n.getPriority().getDisplayName() : "";
                    String status = n.getIsRead() ? "Đã đọc" : "Chưa đọc";
                    String date = n.getCreatedAt() != null ? n.getCreatedAt().format(dateFormatter) : "";
                    String doc = n.getRelatedDocumentId() != null ? "#" + n.getRelatedDocumentId() : "";
                    String appt = n.getRelatedAppointmentId() != null ? "#" + n.getRelatedAppointmentId() : "";
                    String message = n.getMessage() != null ? n.getMessage().replace("\n", " ").replace("\"", "\"\"") : "";
                    writer.printf("%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n",
                        n.getNotificationId(), escapeCSV(recipientName), escapeCSV(n.getTitle()),
                        escapeCSV(type), escapeCSV(priority), status, date, doc, appt, escapeCSV(message));
                }
                JOptionPane.showMessageDialog(this, "Đã xuất " + all.size() + " thông báo ra file:\n" + fileToSave.getAbsolutePath(), "Xuất CSV thành công", JOptionPane.INFORMATION_MESSAGE);
                if (Desktop.isDesktopSupported()) {
                    try { Desktop.getDesktop().open(fileToSave.getParentFile()); } catch (Exception ex) { /* Ignore */ }
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất file CSV:\n" + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private String escapeCSV(String value) {
        if (value == null) return "";
        return value.replace("\"", "\"\"");
    }

    private void showSettings() {
        JDialog settingsDialog = new JDialog(owner, "Cài đặt Thông báo", true);
        settingsDialog.setLayout(new BorderLayout(12, 12));
        settingsDialog.setSize(600, 500);
        settingsDialog.setLocationRelativeTo(this);
        JPanel mainPanel = new JPanel(new BorderLayout(0, 16));
        mainPanel.setBorder(new EmptyBorder(20, 24, 20, 24));
        mainPanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("Cài đặt Thông báo");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(PRIMARY_COLOR);
        JLabel subtitleLabel = new JLabel("Tùy chỉnh cách hiển thị và nhận thông báo");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(4));
        headerPanel.add(subtitleLabel);
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        JCheckBox soundCheckBox = new JCheckBox("Bật âm thanh thông báo");
        soundCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        soundCheckBox.setBackground(Color.WHITE);
        soundCheckBox.setSelected(true);
        JCheckBox popupCheckBox = new JCheckBox("Hiển thị popup thông báo");
        popupCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        popupCheckBox.setBackground(Color.WHITE);
        popupCheckBox.setSelected(true);
        JCheckBox badgeCheckBox = new JCheckBox("Hiển thị số lượng chưa đọc");
        badgeCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        badgeCheckBox.setBackground(Color.WHITE);
        badgeCheckBox.setSelected(true);
        JCheckBox autoRefreshCheckBox = new JCheckBox("Tự động làm mới (30 giây)");
        autoRefreshCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        autoRefreshCheckBox.setBackground(Color.WHITE);
        autoRefreshCheckBox.setSelected(false);
        JLabel typesLabel = new JLabel("Loại thông báo muốn nhận:");
        typesLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        typesLabel.setForeground(TEXT_PRIMARY);
        JPanel typesPanel = new JPanel(new GridLayout(0, 2, 8, 8));
        typesPanel.setBackground(Color.WHITE);
        typesPanel.setBorder(new EmptyBorder(8, 20, 8, 0));
        Map<NotificationType, JCheckBox> typeCheckBoxes = new HashMap<>();
        for (NotificationType type : NotificationType.values()) {
            JCheckBox cb = new JCheckBox(type.getDisplayName());
            cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            cb.setBackground(Color.WHITE);
            cb.setSelected(true);
            typeCheckBoxes.put(type, cb);
            typesPanel.add(cb);
        }
        contentPanel.add(createSettingSection("Thông báo chung", soundCheckBox, popupCheckBox, badgeCheckBox, autoRefreshCheckBox));
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(typesLabel);
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(typesPanel);
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        buttonPanel.setBackground(Color.WHITE);
        JButton saveButton = new JButton("Lưu cài đặt");
        stylePrimaryButton(saveButton, SUCCESS_COLOR);
        JButton cancelButton = new JButton("Hủy");
        styleSecondaryButton(cancelButton);
        saveButton.addActionListener(e -> {
            int enabledCount = 0;
            for (JCheckBox cb : typeCheckBoxes.values()) {
                if (cb.isSelected()) enabledCount++;
            }
            JOptionPane.showMessageDialog(settingsDialog,
                "Đã lưu cài đặt:\n" +
                "- Âm thanh: " + (soundCheckBox.isSelected() ? "Bật" : "Tắt") + "\n" +
                "- Popup: " + (popupCheckBox.isSelected() ? "Bật" : "Tắt") + "\n" +
                "- Badge: " + (badgeCheckBox.isSelected() ? "Bật" : "Tắt") + "\n" +
                "- Tự động làm mới: " + (autoRefreshCheckBox.isSelected() ? "Bật" : "Tắt") + "\n" +
                "- Loại thông báo: " + enabledCount + "/" + typeCheckBoxes.size(),
                "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
            settingsDialog.dispose();
        });
        cancelButton.addActionListener(e -> settingsDialog.dispose());
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        settingsDialog.add(mainPanel);
        settingsDialog.setVisible(true);
    }
    
    private JPanel createSettingSection(String title, JCheckBox... checkboxes) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(TEXT_PRIMARY);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(12));
        for (JCheckBox cb : checkboxes) {
            panel.add(cb);
            panel.add(Box.createVerticalStrut(8));
        }
        return panel;
    }

    private void stylePrimaryButton(JButton btn, Color bgColor) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        final boolean[] isHovered = {false};
        final boolean[] isPressed = {false};
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton button = (JButton) c;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                int w = button.getWidth(), h = button.getHeight();
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(2, 3, w - 2, h - 2, 8, 8);
                Color color1, color2;
                if (isPressed[0]) {
                    color1 = brighten(bgColor, 0.7);
                    color2 = brighten(bgColor, 0.5);
                } else if (isHovered[0]) {
                    color1 = brighten(bgColor, 1.15);
                    color2 = brighten(bgColor, 0.85);
                } else {
                    color1 = brighten(bgColor, 1.2);
                    color2 = brighten(bgColor, 0.8);
                }
                GradientPaint gradient = new GradientPaint(0, 0, color1, w, h, color2);
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, w - 2, h - 3, 8, 8);
                g2.dispose();
                super.paint(g, c);
            }
        });
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) { isHovered[0] = true; btn.repaint(); }
            public void mouseExited(MouseEvent evt) { isHovered[0] = false; btn.repaint(); }
            public void mousePressed(MouseEvent evt) { isPressed[0] = true; btn.repaint(); }
            public void mouseReleased(MouseEvent evt) { isPressed[0] = false; btn.repaint(); }
        });
    }

    private void styleSecondaryButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setBackground(Color.WHITE);
        btn.setForeground(TEXT_PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        final boolean[] isHovered = {false};
        final boolean[] isPressed = {false};
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton button = (JButton) c;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                int w = button.getWidth(), h = button.getHeight();
                Color color1, color2;
                if (isPressed[0]) {
                    color1 = new Color(230, 240, 255);
                    color2 = new Color(200, 220, 255);
                } else if (isHovered[0]) {
                    color1 = new Color(240, 248, 255);
                    color2 = new Color(220, 235, 255);
                } else {
                    color1 = Color.WHITE;
                    color2 = new Color(250, 252, 255);
                }
                GradientPaint gradient = new GradientPaint(0, 0, color1, w, h, color2);
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, w - 1, h - 1, 8, 8);
                Color borderColor = isHovered[0] ? PRIMARY_COLOR : new Color(200, 200, 200);
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(isHovered[0] ? 2f : 1.5f));
                g2.drawRoundRect(0, 0, w - 2, h - 2, 8, 8);
                g2.dispose();
                super.paint(g, c);
            }
        });
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) { isHovered[0] = true; btn.setForeground(PRIMARY_COLOR); btn.repaint(); }
            public void mouseExited(MouseEvent evt) { isHovered[0] = false; btn.setForeground(TEXT_PRIMARY); btn.repaint(); }
            public void mousePressed(MouseEvent evt) { isPressed[0] = true; btn.repaint(); }
            public void mouseReleased(MouseEvent evt) { isPressed[0] = false; btn.repaint(); }
        });
    }

    private Color brighten(Color color, double factor) {
        int r = (int) (color.getRed() * factor);
        int g = (int) (color.getGreen() * factor);
        int b = (int) (color.getBlue() * factor);
        return new Color(
            Math.max(0, Math.min(255, r)),
            Math.max(0, Math.min(255, g)),
            Math.max(0, Math.min(255, b))
        );
    }

    private JLabel createSimpleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(TEXT_SECONDARY);
        return label;
    }
    
    /**
     * Lấy tên thành viên gia đình từ member_id
     */
    private String getMemberName(Integer memberId) {
        if (memberId == null) {
            return "Người dùng";
        }
        
        try {
            java.util.ArrayList<FamilyMembers> allMembers = FamilyMembersDAO.getInstance().selectByCondition("member_id = " + memberId);
            if (!allMembers.isEmpty()) {
                FamilyMembers member = allMembers.get(0);
                return member.getName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "Thành viên #" + memberId;
    }

    private Component createVerticalSeparator() {
        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setPreferredSize(new Dimension(1, 24));
        sep.setForeground(BORDER_COLOR);
        return sep;
    }
}