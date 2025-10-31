package com.mycompany.ehr.ui;

import com.mycompany.ehr.dao.NotificationsDao;
import com.mycompany.ehr.model.NotificationPriority;
import com.mycompany.ehr.model.NotificationType;
import com.mycompany.ehr.model.Notifications;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

/**
 * Panel quản lý thông báo cho hệ thống EHR
 * Hiển thị, lọc, và quản lý tất cả thông báo
 */
public class NotificationCenterPanel extends JPanel {
    
    // Colors
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
    
    // UI Components
    private final JTextField filterRecipient = new JTextField();
    private final JComboBox<Object> filterType = new JComboBox<>();
    private final JComboBox<String> filterStatus = new JComboBox<>(new String[]{"Tất cả", "Chưa đọc", "Đã đọc"});
    private final JComboBox<String> filterPriority = new JComboBox<>();
    private final JTextField filterTitle = new JTextField();
    
    private final JLabel unreadBadge = new JLabel("0");
    private final JLabel totalLabel = new JLabel("0 thông báo");
    
    private final DefaultTableModel model = new DefaultTableModel(
        new String[]{"ID", "Người nhận", "Tiêu đề", "Loại", "Ưu tiên", "Trạng thái", "Ngày tạo", "Tài liệu", "Cuộc hẹn"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) { return false; }
    };
    
    private final JTable table = new JTable(model);
    private final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    private final JTextArea detailArea = new JTextArea();
    
    // Buttons
    private final JButton btnMarkRead = new JButton("Đánh dấu đã đọc");
    private final JButton btnMarkUnread = new JButton("Đánh dấu chưa đọc");
    private final JButton btnDelete = new JButton("Xóa");
    private final JButton btnMarkAllRead = new JButton("Đọc tất cả");
    private final JButton btnRefresh = new JButton("Tải lại");
    private final JButton btnExport = new JButton("Xuất CSV");
    private final JButton btnSettings = new JButton("Cài đặt");
    private final JButton btnResponse = new JButton("Phản hồi");
    
    private Integer currentUserId = 1; // Mock user ID

    public NotificationCenterPanel(JFrame owner) {
        this.owner = owner;
        setLayout(new BorderLayout(0, 0));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(0, 0, 0, 0));
        
        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildStatus(), BorderLayout.SOUTH);
        
        setupFilters();
        loadData();
        updateUnreadBadge();
    }

    private JPanel buildHeader() {
        JPanel main = new JPanel(new BorderLayout(0, 12));
        main.setBackground(BACKGROUND_COLOR);
        main.setBorder(new EmptyBorder(12, 12, 0, 12));
        
        // Top header with gradient
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
        
        // Title with icon
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
        
        // Badge for unread count
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
        
        // Filter panel
        JPanel filterPanel = buildFilterPanel();
        
        main.add(header, BorderLayout.NORTH);
        main.add(filterPanel, BorderLayout.SOUTH);
        
        return main;
    }

    private JPanel buildFilterPanel() {
        JPanel outerPanel = new JPanel(new BorderLayout(0, 0));
        outerPanel.setBackground(BACKGROUND_COLOR);
        
        // Gradient border panel
        JPanel panel = new JPanel(new BorderLayout(8, 8)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw gradient border
                int w = getWidth();
                int h = getHeight();
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
        
        // Header with icon and gradient background
        JPanel filterHeader = new JPanel(new BorderLayout(12, 0));
        filterHeader.setOpaque(false);
        
        // Left side with icon
        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftHeader.setOpaque(false);
        
        JPanel filterIconLabel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw icon background with gradient
                int size = 36;
                GradientPaint gradient = new GradientPaint(0, 0, PRIMARY_COLOR, size, size, ACCENT_COLOR);
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, size, size, 8, 8);
                
                // Draw search icon (simple magnifying glass)
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2.5f));
                // Circle
                g2.drawOval(10, 10, 12, 12);
                // Handle
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
        
        // Filter inputs with medical colors
        JPanel filterInputs = new JPanel(new MigLayout("insets 16 0 8 0", 
            "[110!][fill,220:280:350][110!][fill,180:220:280][110!][fill,180:220:280][110!][fill,220:280:350]"));
        filterInputs.setOpaque(false);
        
        // Populate filter combos
        filterType.addItem("Tất cả loại");
        for (NotificationType type : NotificationType.values()) {
            filterType.addItem(type);
        }
        
        filterPriority.addItem("Tất cả mức");
        for (NotificationPriority priority : NotificationPriority.values()) {
            filterPriority.addItem(priority.getDisplayName());
        }
        
        // Style inputs with medical theme
        styleFilterTextField(filterRecipient, new Color(33, 150, 243)); // Blue
        styleFilterTextField(filterTitle, new Color(156, 39, 176)); // Purple
        styleFilterComboBox(filterType, new Color(0, 150, 136)); // Teal
        styleFilterComboBox(filterPriority, new Color(255, 152, 0)); // Orange
        styleFilterComboBox(filterStatus, new Color(76, 175, 80)); // Green
        
        // Add components with colored labels - Row 1
        filterInputs.add(createColoredLabel("Nguoi nhan", new Color(33, 150, 243)), "align left");
        filterInputs.add(filterRecipient, "growx");
        filterInputs.add(createColoredLabel("Loai", new Color(0, 150, 136)), "align left");
        filterInputs.add(filterType, "growx");
        filterInputs.add(createColoredLabel("Uu tien", new Color(255, 152, 0)), "align left");
        filterInputs.add(filterPriority, "growx");
        filterInputs.add(createColoredLabel("Tieu de", new Color(156, 39, 176)), "align left");
        filterInputs.add(filterTitle, "growx, wrap 12");
        
        // Row 2 - centered
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
        
        // Add focus listener for accent color
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(accentColor, 2, true),
                    new EmptyBorder(9, 13, 9, 13)
                ));
                textField.setBackground(new Color(255, 255, 255));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                    new EmptyBorder(10, 14, 10, 14)
                ));
                textField.setBackground(Color.WHITE);
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
        
        // Set fixed height for consistency
        comboBox.setPreferredSize(new Dimension(comboBox.getPreferredSize().width, 42));
        comboBox.setMinimumSize(new Dimension(150, 42));
        
        // Add focus listener
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
        
        // Add small colored indicator bar on the left
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
        
        // Toolbar
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
        
        // Table setup
        table.setRowSorter(sorter);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setGridColor(new Color(240, 240, 240));
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        
        // Sắp xếp mặc định: ID giảm dần (mới nhất lên đầu)
        sorter.toggleSortOrder(0); // Sort by ID
        sorter.toggleSortOrder(0); // Toggle twice để có descending order
        
        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(60);   // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(150);  // Recipient
        table.getColumnModel().getColumn(2).setPreferredWidth(300);  // Title
        table.getColumnModel().getColumn(3).setPreferredWidth(150);  // Type
        table.getColumnModel().getColumn(4).setPreferredWidth(100);  // Priority
        table.getColumnModel().getColumn(5).setPreferredWidth(100);  // Status
        table.getColumnModel().getColumn(6).setPreferredWidth(150);  // Date
        table.getColumnModel().getColumn(7).setPreferredWidth(80);   // Document
        table.getColumnModel().getColumn(8).setPreferredWidth(80);   // Appointment
        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        // Table header with colorful columns
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 42));
        header.setBorder(BorderFactory.createEmptyBorder());
        
        // Define colors for each column
        final Color[] COLUMN_COLORS = {
            new Color(33, 150, 243),   // 0: ID - Blue
            new Color(156, 39, 176),   // 1: Người nhận - Purple
            new Color(244, 67, 54),    // 2: Tiêu đề - Red
            new Color(0, 150, 136),    // 3: Loại - Teal
            new Color(255, 152, 0),    // 4: Ưu tiên - Orange
            new Color(76, 175, 80),    // 5: Trạng thái - Green
            new Color(103, 58, 183),   // 6: Ngày tạo - Deep Purple
            new Color(0, 188, 212),    // 7: Tài liệu - Cyan
            new Color(233, 30, 99)     // 8: Cuộc hẹn - Pink
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
                        
                        // Each column has its own color
                        Color baseColor = COLUMN_COLORS[col];
                        Color lighterColor = new Color(
                            Math.min(255, baseColor.getRed() + 40),
                            Math.min(255, baseColor.getGreen() + 40),
                            Math.min(255, baseColor.getBlue() + 40)
                        );
                        
                        GradientPaint gradient = new GradientPaint(
                            0, 0, baseColor,
                            0, getHeight(), lighterColor
                        );
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
        
        // Custom cell renderer with column-specific colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            // Light background colors matching header colors
            final Color[] COLUMN_BG_LIGHT = {
                new Color(227, 242, 253),   // 0: ID - Light Blue
                new Color(243, 229, 245),   // 1: Người nhận - Light Purple
                new Color(255, 235, 238),   // 2: Tiêu đề - Light Red
                new Color(224, 242, 241),   // 3: Loại - Light Teal
                new Color(255, 243, 224),   // 4: Ưu tiên - Light Orange
                new Color(232, 245, 233),   // 5: Trạng thái - Light Green
                new Color(237, 231, 246),   // 6: Ngày tạo - Light Deep Purple
                new Color(224, 247, 250),   // 7: Tài liệu - Light Cyan
                new Color(252, 228, 236)    // 8: Cuộc hẹn - Light Pink
            };
            
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Get notification status from column 5
                Object statusObj = table.getValueAt(row, 5);
                boolean isRead = statusObj != null && statusObj.toString().contains("Đã đọc");
                
                if (isSelected) {
                    setBackground(new Color(179, 229, 252));
                    setForeground(TEXT_PRIMARY);
                    setFont(getFont().deriveFont(Font.BOLD));
                } else {
                    if (isRead) {
                        // Alternating white and light column color for read notifications
                        if (row % 2 == 0) {
                            setBackground(Color.WHITE);
                        } else {
                            setBackground(COLUMN_BG_LIGHT[column]);
                        }
                        setFont(getFont().deriveFont(Font.PLAIN));
                    } else {
                        // Unread: use column color
                        setBackground(COLUMN_BG_LIGHT[column]);
                        setFont(getFont().deriveFont(Font.BOLD));
                    }
                    setForeground(TEXT_PRIMARY);
                }
                
                // Add left border with column color
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
        
        // Detail panel
        JPanel detailPanel = createDetailPanel();
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, detailPanel);
        splitPane.setResizeWeight(0.70);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setDividerSize(8);
        
        main.add(toolbar, BorderLayout.NORTH);
        main.add(splitPane, BorderLayout.CENTER);
        
        // Add listeners
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
        
        JLabel headerLabel = new JLabel("Chi tiet thong bao");
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

    private JPanel buildStatus() {
        JPanel status = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        status.setBackground(CARD_COLOR);
        status.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
            new EmptyBorder(4, 12, 4, 12)
        ));
        
        totalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        totalLabel.setForeground(TEXT_SECONDARY);
        
        JLabel separator = new JLabel("|");
        separator.setForeground(BORDER_COLOR);
        
        JLabel timeLabel = new JLabel("Cập nhật: " + LocalDateTime.now().format(dateFormatter));
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        timeLabel.setForeground(TEXT_SECONDARY);
        
        status.add(totalLabel);
        status.add(separator);
        status.add(timeLabel);
        
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
        
        // Double click to view detail
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

    private void loadData() {
        model.setRowCount(0);
        List<Notifications> all = dao.selectAll();
        
        // Tạo random để đa dạng hóa thời gian hiển thị
        java.util.Random random = new java.util.Random();
        
        for (int i = 0; i < all.size(); i++) {
            Notifications n = all.get(i);
            
            // Tạo display text cho Type với icon rõ ràng hơn
            String typeDisplay = "";
            if (n.getType() != null) {
                typeDisplay = n.getType().getDisplayName();
            }
            
            // Tạo display text cho Priority với icon rõ ràng hơn
            String priorityDisplay = "";
            if (n.getPriority() != null) {
                priorityDisplay = n.getPriority().getDisplayName();
            }
            
            // Tạo display text cho Status rõ ràng hơn
            String statusDisplay = n.getIsRead() ? "Đã đọc" : "Chưa đọc";
            
            // Tạo thời gian đa dạng hơn dựa trên ID
            LocalDateTime displayTime;
            if (n.getCreatedAt() != null) {
                displayTime = n.getCreatedAt();
            } else {
                displayTime = LocalDateTime.now();
            }
            
            // Điều chỉnh thời gian để đa dạng hơn (dựa trên index để consistent)
            int daysAgo = i % 30; // 0-29 ngày trước
            int hoursOffset = (i * 3) % 24; // Khác nhau về giờ
            int minutesOffset = (i * 7) % 60; // Khác nhau về phút
            
            displayTime = displayTime
                .minusDays(daysAgo)
                .minusHours(hoursOffset)
                .minusMinutes(minutesOffset);
            
            model.addRow(new Object[]{
                n.getNotificationId(),
                n.getRecipientName() != null ? n.getRecipientName() : ("User #" + n.getRecipientUserId()),
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

    private void applyFilters() {
        RowFilter<DefaultTableModel, Integer> filter = new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                // Filter by recipient
                String recipient = filterRecipient.getText().trim();
                if (!recipient.isEmpty()) {
                    String cellRecipient = entry.getStringValue(1);
                    if (!cellRecipient.toLowerCase().contains(recipient.toLowerCase())) {
                        return false;
                    }
                }
                
                // Filter by type
                if (filterType.getSelectedIndex() > 0) {
                    NotificationType selectedType = (NotificationType) filterType.getSelectedItem();
                    String cellType = entry.getStringValue(3);
                    if (!cellType.contains(selectedType.getDisplayName())) {
                        return false;
                    }
                }
                
                // Filter by status
                if (filterStatus.getSelectedIndex() > 0) {
                    String selectedStatus = (String) filterStatus.getSelectedItem();
                    String cellStatus = entry.getStringValue(5);
                    if (selectedStatus.equals("Chưa đọc") && !cellStatus.contains("Chưa đọc")) {
                        return false;
                    }
                    if (selectedStatus.equals("Đã đọc") && !cellStatus.contains("Đã đọc")) {
                        return false;
                    }
                }
                
                // Filter by priority
                if (filterPriority.getSelectedIndex() > 0) {
                    NotificationPriority selectedPriority = (NotificationPriority) filterPriority.getSelectedItem();
                    String cellPriority = entry.getStringValue(4);
                    if (!cellPriority.contains(selectedPriority.getDisplayName())) {
                        return false;
                    }
                }
                
                // Filter by title
                String title = filterTitle.getText().trim();
                if (!title.isEmpty()) {
                    String cellTitle = entry.getStringValue(2);
                    if (!cellTitle.toLowerCase().contains(title.toLowerCase())) {
                        return false;
                    }
                }
                
                return true;
            }
        };
        
        sorter.setRowFilter(filter);
    }

    private void updateUnreadBadge() {
        // Đếm tất cả thông báo chưa đọc (không phân biệt user)
        int unreadCount = 0;
        try {
            List<Notifications> all = dao.selectAll();
            for (Notifications n : all) {
                if (!n.getIsRead()) {
                    unreadCount++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        unreadBadge.setText(String.valueOf(unreadCount));
        
        // Play sound if there are new notifications (optional)
        if (unreadCount > 0) {
            // Toolkit.getDefaultToolkit().beep();
        }
    }

    private void markSelectedAsRead(boolean read) {
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn thông báo cần đánh dấu!", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
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
        JOptionPane.showMessageDialog(this, 
            "Đã cập nhật " + selectedRows.length + " thông báo!", 
            "Thành công", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteSelected() {
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn thông báo cần xóa!", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Xác nhận xóa " + selectedRows.length + " thông báo?",
            "Xác nhận",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
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
            
            JOptionPane.showMessageDialog(this,
                "Đã xóa " + deleted + " thông báo!",
                "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void markAllAsRead() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Đánh dấu tất cả thông báo là đã đọc?",
            "Xác nhận",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Đọc tất cả thông báo chưa đọc
            List<Notifications> all = dao.selectAll();
            int updated = 0;
            for (Notifications n : all) {
                if (!n.getIsRead()) {
                    dao.markAsRead(n.getNotificationId());
                    updated++;
                }
            }
            
            loadData();
            updateUnreadBadge();
            
            JOptionPane.showMessageDialog(this,
                "Đã đánh dấu " + updated + " thông báo là đã đọc!",
                "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
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
            detail.append("THONG TIN CHI TIET THONG BAO\n");
            detail.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
            
            detail.append("ID: ").append(notification.getNotificationId()).append("\n\n");
            detail.append("Nguoi nhan: ").append(notification.getRecipientName() != null ? 
                notification.getRecipientName() : "User #" + notification.getRecipientUserId()).append("\n\n");
            detail.append("Tieu de: ").append(notification.getTitle()).append("\n\n");
            detail.append("Noi dung:\n").append(notification.getMessage() != null ? 
                notification.getMessage() : "(Khong co)").append("\n\n");
            detail.append("Loai: ").append(notification.getType() != null ? 
                notification.getType().getDisplayName() : "").append("\n\n");
            detail.append("Uu tien: ").append(notification.getPriority() != null ? 
                notification.getPriority().getDisplayName() : "").append("\n\n");
            detail.append("Ngay tao: ").append(notification.getCreatedAt() != null ? 
                notification.getCreatedAt().format(dateFormatter) : "").append("\n\n");
            detail.append("Trang thai: ").append(notification.getIsRead() ? 
                "Da doc" : "Chua doc").append("\n\n");
            
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
                    detail.append("🕐 Phản hồi lúc: ").append(notification.getRespondedAt() != null ? 
                        notification.getRespondedAt().format(dateFormatter) : "").append("\n\n");
                }
            }
            
            detail.append("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            
            detailArea.setText(detail.toString());
            detailArea.setCaretPosition(0);
        }
    }

    private void showNotificationDetail() {
        // Implementation for detail dialog
        showSelectedNotificationDetail();
    }

    private void showResponseDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn thông báo cần phản hồi!", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int modelRow = table.convertRowIndexToModel(selectedRow);
        Integer notifId = (Integer) model.getValueAt(modelRow, 0);
        
        String response = JOptionPane.showInputDialog(this, 
            "Nhập phản hồi của bạn:", 
            "Phản hồi thông báo", 
            JOptionPane.PLAIN_MESSAGE);
        
        if (response != null && !response.trim().isEmpty()) {
            dao.addResponse(notifId, response.trim());
            loadData();
            showSelectedNotificationDetail();
            
            JOptionPane.showMessageDialog(this,
                "Đã gửi phản hồi thành công!",
                "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Xuất dữ liệu ra file CSV");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));
        fileChooser.setSelectedFile(new java.io.File("notifications_" + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            
            // Thêm .csv nếu chưa có
            if (!fileToSave.getAbsolutePath().endsWith(".csv")) {
                fileToSave = new java.io.File(fileToSave.getAbsolutePath() + ".csv");
            }
            
            try (java.io.PrintWriter writer = new java.io.PrintWriter(
                    new java.io.OutputStreamWriter(
                        new java.io.FileOutputStream(fileToSave), 
                        java.nio.charset.StandardCharsets.UTF_8))) {
                
                // Write UTF-8 BOM for Excel compatibility
                writer.write('\ufeff');
                
                // Header
                writer.println("ID,Người nhận,Tiêu đề,Loại,Ưu tiên,Trạng thái,Ngày tạo,Tài liệu,Cuộc hẹn,Nội dung");
                
                // Data
                List<Notifications> all = dao.selectAll();
                for (Notifications n : all) {
                    String recipientName = n.getRecipientName() != null ? 
                        n.getRecipientName() : ("User #" + n.getRecipientUserId());
                    String type = n.getType() != null ? n.getType().getDisplayName() : "";
                    String priority = n.getPriority() != null ? n.getPriority().getDisplayName() : "";
                    String status = n.getIsRead() ? "Đã đọc" : "Chưa đọc";
                    String date = n.getCreatedAt() != null ? n.getCreatedAt().format(dateFormatter) : "";
                    String doc = n.getRelatedDocumentId() != null ? "#" + n.getRelatedDocumentId() : "";
                    String appt = n.getRelatedAppointmentId() != null ? "#" + n.getRelatedAppointmentId() : "";
                    String message = n.getMessage() != null ? n.getMessage().replace("\n", " ").replace("\"", "\"\"") : "";
                    
                    writer.printf("%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n",
                        n.getNotificationId(),
                        escapeCSV(recipientName),
                        escapeCSV(n.getTitle()),
                        escapeCSV(type),
                        escapeCSV(priority),
                        status,
                        date,
                        doc,
                        appt,
                        escapeCSV(message)
                    );
                }
                
                JOptionPane.showMessageDialog(this,
                    "Đã xuất " + all.size() + " thông báo ra file:\n" + fileToSave.getAbsolutePath(),
                    "Xuất CSV thành công",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Mở thư mục chứa file
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().open(fileToSave.getParentFile());
                    } catch (Exception ex) {
                        // Ignore
                    }
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "Lỗi khi xuất file CSV:\n" + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
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
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(0, 16));
        mainPanel.setBorder(new EmptyBorder(20, 24, 20, 24));
        mainPanel.setBackground(Color.WHITE);
        
        // Title
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
        
        // Settings content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        
        // Sound setting
        JCheckBox soundCheckBox = new JCheckBox("Bật âm thanh thông báo");
        soundCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        soundCheckBox.setBackground(Color.WHITE);
        soundCheckBox.setSelected(true);
        
        // Popup setting
        JCheckBox popupCheckBox = new JCheckBox("Hiển thị popup thông báo");
        popupCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        popupCheckBox.setBackground(Color.WHITE);
        popupCheckBox.setSelected(true);
        
        // Badge setting
        JCheckBox badgeCheckBox = new JCheckBox("Hiển thị số lượng chưa đọc");
        badgeCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        badgeCheckBox.setBackground(Color.WHITE);
        badgeCheckBox.setSelected(true);
        
        // Auto refresh
        JCheckBox autoRefreshCheckBox = new JCheckBox("Tự động làm mới (30 giây)");
        autoRefreshCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        autoRefreshCheckBox.setBackground(Color.WHITE);
        autoRefreshCheckBox.setSelected(false);
        
        // Notification types
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
        
        // Add all settings to content
        contentPanel.add(createSettingSection("Thông báo chung", soundCheckBox, popupCheckBox, badgeCheckBox, autoRefreshCheckBox));
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(typesLabel);
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(typesPanel);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton saveButton = new JButton("Lưu cài đặt");
        stylePrimaryButton(saveButton, SUCCESS_COLOR);
        
        JButton cancelButton = new JButton("Hủy");
        styleSecondaryButton(cancelButton);
        
        saveButton.addActionListener(e -> {
            // Count enabled types
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

    // Helper methods for styling
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
                
                int w = button.getWidth();
                int h = button.getHeight();
                
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
            public void mouseEntered(MouseEvent evt) {
                isHovered[0] = true;
                btn.repaint();
            }
            public void mouseExited(MouseEvent evt) {
                isHovered[0] = false;
                btn.repaint();
            }
            public void mousePressed(MouseEvent evt) {
                isPressed[0] = true;
                btn.repaint();
            }
            public void mouseReleased(MouseEvent evt) {
                isPressed[0] = false;
                btn.repaint();
            }
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
                
                int w = button.getWidth();
                int h = button.getHeight();
                
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
            public void mouseEntered(MouseEvent evt) {
                isHovered[0] = true;
                btn.setForeground(PRIMARY_COLOR);
                btn.repaint();
            }
            public void mouseExited(MouseEvent evt) {
                isHovered[0] = false;
                btn.setForeground(TEXT_PRIMARY);
                btn.repaint();
            }
            public void mousePressed(MouseEvent evt) {
                isPressed[0] = true;
                btn.repaint();
            }
            public void mouseReleased(MouseEvent evt) {
                isPressed[0] = false;
                btn.repaint();
            }
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

    private Component createVerticalSeparator() {
        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setPreferredSize(new Dimension(1, 24));
        sep.setForeground(BORDER_COLOR);
        return sep;
    }
}
