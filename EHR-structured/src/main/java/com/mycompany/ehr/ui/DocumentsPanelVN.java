package com.mycompany.ehr.ui;

import com.mycompany.ehr.dao.DocumentsDao;
import com.mycompany.ehr.model.DocumentType;
import com.mycompany.ehr.model.Documents;
import net.miginfocom.swing.MigLayout;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DocumentsPanelVN extends JPanel {
    // Màu sắc chuyên nghiệp cho ứng dụng y tế - Tăng cường độ tương phản
    private static final Color PRIMARY_COLOR = new Color(13, 71, 161);       // Xanh dương đậm hơn
    private static final Color PRIMARY_LIGHT = new Color(25, 118, 210);      // Xanh vừa
    private static final Color PRIMARY_DARK = new Color(1, 87, 155);         // Xanh rất đậm
    private static final Color ACCENT_COLOR = new Color(0, 121, 107);        // Xanh lục đậm hơn
    private static final Color ERROR_COLOR = new Color(211, 47, 47);         // Đỏ đậm
    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);  // Xanh xám nhạt
    private static final Color CARD_COLOR = Color.WHITE;                     // Trắng tinh
    private static final Color BORDER_COLOR = new Color(189, 189, 189);      // Viền xám đậm hơn
    private static final Color TEXT_PRIMARY = new Color(13, 13, 13);         // Text đen đậm
    private static final Color TEXT_SECONDARY = new Color(66, 66, 66);       // Text xám đậm hơn
    
    // Dark mode colors - Tăng độ tương phản
    private static final Color DARK_BG = new Color(15, 15, 15);              // Background đen
    private static final Color DARK_CARD = new Color(28, 28, 30);            // Card tối
    private static final Color DARK_TEXT = new Color(245, 245, 245);         // Text trắng sáng
    private static final Color DARK_BORDER = new Color(70, 70, 70);          // Viền xám tối
    
    private boolean isDarkMode = false;
    
    private final JTextField memberId = new JTextField();
    private final JTextField appointmentId = new JTextField();
    private final JComboBox<DocumentType> type = new JComboBox<>(DocumentType.values());
    private final JTextField title = new JTextField();
    private final JTextField filePath = new JTextField();
    private final JButton browse = new JButton("Chọn file");
    private final JTextArea description = new JTextArea(3, 40);
    private final DatePicker docDate;
    private final JTextField uploadedBy = new JTextField();

    private final JButton btnAdd = new JButton("Thêm", createIconFromText("+", new Color(76, 175, 80)));
    private final JButton btnClear = new JButton("Làm mới", createIconFromText("↻", new Color(100, 100, 100)));
    private final JButton btnOpen = new JButton("Mở thư mục", createIconFromText("📁", new Color(255, 152, 0)));
    private final JButton btnPreview = new JButton("Xem", createIconFromText("👁", new Color(33, 150, 243)));
    private final JButton btnDelete = new JButton("Xoá", createIconFromText("×", new Color(244, 67, 54)));
    private final JButton btnRefresh = new JButton("Tải lại", createIconFromText("⟳", new Color(100, 100, 100)));
    private final JButton btnExport = new JButton("Xuất CSV", createIconFromText("↓", new Color(0, 150, 136)));
    private final JButton btnPrint = new JButton("In", createIconFromText("🖨", new Color(103, 58, 183)));
    private final JToggleButton btnDark = new JToggleButton("Chế độ tối", createIconFromText("☾", new Color(100, 100, 100)));

    private final JTextField filterMember = new JTextField();
    private final JComboBox<Object> filterType = new JComboBox<>();
    private final JTextField filterText = new JTextField();

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID","Member","Appt","Type","Title","File","Desc","Doc Date","Uploaded By","Uploaded At"}, 0) {
        public boolean isCellEditable(int r,int c){return false;}
    };
    private final JTable table = new JTable(model);
    private final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    private final FilePreviewPanel previewPanel = new FilePreviewPanel();
    private final JLabel status = new JLabel("0 rows");
    private final DocumentsDao dao = new DocumentsDao();

    public DocumentsPanelVN(JFrame owner) {
        DatePickerSettings s = new DatePickerSettings(new Locale("vi","VN"));
        s.setAllowEmptyDates(true);
        docDate = new DatePicker(s);
        
        setLayout(new BorderLayout(0, 0));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(0, 0, 0, 0));
        
        // Create main split pane for form and table
        JComponent northPanel = buildNorth();
        
        // Wrap form in ScrollPane to ensure full visibility
        JScrollPane northScroll = new JScrollPane(northPanel);
        northScroll.setBorder(BorderFactory.createEmptyBorder());
        northScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        northScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        northScroll.getVerticalScrollBar().setUnitIncrement(16);
        
        JComponent centerPanel = buildCenter();
        
        // Use split pane to allow resizing - form gets priority, table can be smaller
        JSplitPane mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, northScroll, centerPanel);
        mainSplit.setResizeWeight(0.80); // 80% for form (priority), 20% for table
        mainSplit.setBorder(BorderFactory.createEmptyBorder());
        mainSplit.setDividerSize(8);
        
        add(mainSplit, BorderLayout.CENTER);
        add(buildStatus(), BorderLayout.SOUTH);
        
        styleComponents();
        hookEvents();
        initDnD();
        refreshTable();
    }
    
    private void styleComponents() {
        // Style text fields với viền và padding
        styleTextField(memberId);
        memberId.setToolTipText("VD: 1001");
        styleTextField(appointmentId);
        appointmentId.setToolTipText("Có thể để trống");
        styleTextField(title);
        title.setToolTipText("Tiêu đề tài liệu");
        
        // Style filePath với màu tím nhạt
        filePath.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        filePath.setForeground(new Color(74, 20, 140)); // Tím đậm
        filePath.setCaretColor(new Color(156, 39, 176));
        filePath.setBorder(new CompoundBorder(
            new LineBorder(new Color(156, 39, 176), 1, true),
            new EmptyBorder(10, 14, 10, 14)
        ));
        filePath.setBackground(new Color(250, 245, 252)); // Background tím rất nhạt
        filePath.setToolTipText("Đường dẫn file");
        
        // Focus effect cho filePath
        filePath.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                filePath.setBorder(new CompoundBorder(
                    new LineBorder(new Color(156, 39, 176), 2, true),
                    new EmptyBorder(9, 13, 9, 13)
                ));
                filePath.setBackground(Color.WHITE);
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                filePath.setBorder(new CompoundBorder(
                    new LineBorder(new Color(156, 39, 176), 1, true),
                    new EmptyBorder(10, 14, 10, 14)
                ));
                filePath.setBackground(new Color(250, 245, 252));
            }
        });
        
        styleTextField(uploadedBy);
        uploadedBy.setToolTipText("User ID");
        
        // Filter fields
        filterMember.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filterMember.setForeground(TEXT_PRIMARY);
        filterMember.setCaretColor(PRIMARY_COLOR);
        filterMember.setToolTipText("Lọc Member ID");
        styleTextField(filterMember);
        
        filterText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filterText.setForeground(TEXT_PRIMARY);
        filterText.setCaretColor(PRIMARY_COLOR);
        filterText.setToolTipText("Tìm tiêu đề");
        styleTextField(filterText);
        
        // Style text area với màu xanh lá nhạt và viền đẹp
        description.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        description.setForeground(new Color(27, 94, 32)); // Xanh lá đậm
        description.setCaretColor(new Color(76, 175, 80));
        description.setBackground(new Color(245, 250, 245)); // Background xanh lá rất nhạt
        description.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(76, 175, 80), 1, true),
            new EmptyBorder(10, 14, 10, 14)
        ));
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        
        // Focus effect cho description
        description.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                description.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(76, 175, 80), 2, true),
                    new EmptyBorder(9, 13, 9, 13)
                ));
                description.setBackground(Color.WHITE);
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                description.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(76, 175, 80), 1, true),
                    new EmptyBorder(10, 14, 10, 14)
                ));
                description.setBackground(new Color(245, 250, 245));
            }
        });
        
        // Style combo boxes với màu nổi bật
        styleComboBox(type);
        styleComboBox(filterType);
        
        // Style buttons với màu sắc đẹp
        stylePrimaryButton(btnAdd, new Color(76, 175, 80)); // Xanh lá đậm
        styleSecondaryButton(btnClear);
        styleSecondaryButton(btnRefresh);
        styleSecondaryButton(btnOpen);
        styleSecondaryButton(btnPreview);
        styleDangerButton(btnDelete);
        stylePrimaryButton(btnExport, new Color(0, 150, 136)); // Xanh lục
        stylePrimaryButton(btnPrint, new Color(103, 58, 183)); // Tím
        styleSecondaryButton(browse);
        
        // Style toggle button cho dark mode
        btnDark.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDark.setBackground(Color.WHITE);
        btnDark.setForeground(TEXT_PRIMARY);
        btnDark.setFocusPainted(false);
        btnDark.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(8, 16, 8, 16)
        ));
        btnDark.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDark.addActionListener(e -> {
            if (btnDark.isSelected()) {
                btnDark.setBackground(PRIMARY_DARK);
                btnDark.setForeground(Color.WHITE);
            } else {
                btnDark.setBackground(Color.WHITE);
                btnDark.setForeground(TEXT_PRIMARY);
            }
            btnDark.repaint();
            // Force repaint toàn bộ panel
            revalidate();
            repaint();
        });
    }
    
    private void styleComboBox(JComboBox<?> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(4, 10, 4, 10)
        ));
        combo.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15)); // Font lớn hơn
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(PRIMARY_COLOR);
        
        // Bo góc 10px cho text field
        field.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(10, 14, 10, 14) // Padding lớn hơn
        ));
        
        // Focus effect với gradient border
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                // Border gradient khi focus
                field.setBorder(new CompoundBorder(
                    new LineBorder(PRIMARY_COLOR, 2, true),
                    new EmptyBorder(9, 13, 9, 13)
                ));
                // Background gradient nhẹ khi focus
                field.setBackground(new Color(240, 248, 255));
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                field.setBorder(new CompoundBorder(
                    new LineBorder(BORDER_COLOR, 1, true),
                    new EmptyBorder(10, 14, 10, 14)
                ));
                field.setBackground(Color.WHITE);
            }
        });
    }    private void stylePrimaryButton(JButton btn, Color bgColor) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(12, 24, 12, 24));
        btn.setIconTextGap(8);
        
        // Tạo rounded border với gradient paint
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
                
                // Shadow effect
                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillRoundRect(2, 3, w - 2, h - 2, 10, 10);
                
                // Gradient background với màu chuyển động
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
                g2.fillRoundRect(0, 0, w - 2, h - 3, 10, 10);
                
                g2.dispose();
                
                // Vẽ icon và text
                super.paint(g, c);
            }
        });
        
        // Hover effects
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                isHovered[0] = true;
                btn.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                isHovered[0] = false;
                btn.repaint();
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                isPressed[0] = true;
                btn.repaint();
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                isPressed[0] = false;
                btn.repaint();
            }
        });
    }
    
    // Helper method để làm sáng/tối màu
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
    
    // Tạo icon từ text với font hỗ trợ tốt
    private static Icon createIconFromText(String text, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                // Thử nhiều font để đảm bảo icon hiển thị
                Font[] fonts = {
                    new Font("Segoe UI Emoji", Font.PLAIN, 16),
                    new Font("Apple Color Emoji", Font.PLAIN, 16),
                    new Font("Noto Color Emoji", Font.PLAIN, 16),
                    new Font("Segoe UI Symbol", Font.PLAIN, 16),
                    new Font("Arial Unicode MS", Font.PLAIN, 16),
                    new Font("Segoe UI", Font.BOLD, 18)
                };
                
                Font selectedFont = fonts[0];
                for (Font font : fonts) {
                    if (font.canDisplay(text.charAt(0))) {
                        selectedFont = font;
                        break;
                    }
                }
                
                g2.setFont(selectedFont);
                g2.setColor(color);
                
                // Căn giữa text
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(text);
                int textHeight = fm.getAscent();
                
                g2.drawString(text, x + (getIconWidth() - textWidth) / 2, y + (getIconHeight() + textHeight) / 2 - 2);
                g2.dispose();
            }
            
            @Override
            public int getIconWidth() {
                return 20;
            }
            
            @Override
            public int getIconHeight() {
                return 20;
            }
        };
    }
    
    private void styleSecondaryButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setBackground(Color.WHITE);
        btn.setForeground(TEXT_PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setIconTextGap(8);
        
        final boolean[] isHovered = {false};
        final boolean[] isPressed = {false};
        
        // Custom painting với border tròn và gradient
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton button = (JButton) c;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                int w = button.getWidth();
                int h = button.getHeight();
                
                // Gradient background
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
                g2.fillRoundRect(0, 0, w - 1, h - 1, 10, 10);
                
                // Border với gradient
                Color borderColor = isHovered[0] ? PRIMARY_COLOR : new Color(200, 200, 200);
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(isHovered[0] ? 2f : 1.5f));
                g2.drawRoundRect(0, 0, w - 2, h - 2, 10, 10);
                
                g2.dispose();
                
                // Vẽ text và icon
                super.paint(g, c);
            }
        });
        
        // Hover effects
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                isHovered[0] = true;
                btn.setForeground(PRIMARY_COLOR);
                btn.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                isHovered[0] = false;
                btn.setForeground(TEXT_PRIMARY);
                btn.repaint();
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                isPressed[0] = true;
                btn.repaint();
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                isPressed[0] = false;
                btn.repaint();
            }
        });
    }
    
    private void styleDangerButton(JButton btn) {
        stylePrimaryButton(btn, ERROR_COLOR);
    }

    private JComponent buildNorth() {
        // Main panel với background màu gradient
        JPanel main = new JPanel(new BorderLayout(0, 0));
        main.setBackground(BACKGROUND_COLOR);
        main.setBorder(new EmptyBorder(12, 12, 12, 12));
        
        // Header với gradient effect (xanh dương → tím → hồng)
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                
                // Gradient đa màu: xanh dương → xanh lục → tím
                float[] dist = {0.0f, 0.5f, 1.0f};
                Color[] colors = {
                    PRIMARY_COLOR,        // Xanh dương
                    ACCENT_COLOR,         // Xanh lục
                    new Color(103, 58, 183)  // Tím đậm
                };
                
                LinearGradientPaint gradient = new LinearGradientPaint(
                    0, 0, w, 0, dist, colors
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, w, h);
                g2d.dispose();
            }
        };
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(20, 24, 20, 24));
        
        JLabel icon = new JLabel("⚕");
        icon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 48));
        icon.setForeground(Color.WHITE);
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        
        // Tạo tiêu đề với gradient text (Rainbow effect)
        JLabel h = new JLabel("Quản lý Tài liệu Y tế") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                // Gradient cầu vồng: trắng → vàng → cam → hồng
                int w = getWidth();
                float[] dist = {0.0f, 0.33f, 0.66f, 1.0f};
                Color[] colors = {
                    Color.WHITE,
                    new Color(255, 255, 150),  // Vàng nhạt
                    new Color(255, 200, 150),  // Cam nhạt
                    new Color(255, 180, 200)   // Hồng nhạt
                };
                
                LinearGradientPaint gradient = new LinearGradientPaint(
                    0, 0, w, 0, dist, colors
                );
                g2.setPaint(gradient);
                g2.setFont(getFont());
                
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), 0, fm.getAscent());
                g2.dispose();
            }
        };
        h.setFont(new Font("Segoe UI", Font.BOLD, 28));
        h.setForeground(Color.WHITE); // Fallback color
        
        JLabel subtitle = new JLabel("Hệ thống lưu trữ và quản lý hồ sơ bệnh án điện tử");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(255, 255, 255, 200));
        
        titlePanel.add(h);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(subtitle);
        
        JPanel headerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        headerLeft.setOpaque(false);
        headerLeft.add(icon);
        headerLeft.add(titlePanel);
        
        header.add(headerLeft, BorderLayout.WEST);
        
        // Form panel trong card với shadow - BỌC TRONG SCROLLPANE
        JPanel cardContainer = new JPanel(new BorderLayout());
        cardContainer.setBackground(BACKGROUND_COLOR);
        cardContainer.setBorder(new EmptyBorder(12, 0, 0, 0));
        
        JPanel formCard = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Shadow effect
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, 12, 12);
                g2d.setColor(new Color(0, 0, 0, 15));
                g2d.fillRoundRect(4, 4, getWidth()-8, getHeight()-8, 12, 12);
            }
        };
        formCard.setBackground(CARD_COLOR);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(2, 2, 2, 2),
            BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(24, 24, 24, 24)
            )
        ));
        
        // Sử dụng MigLayout với wrap và minimum size - giảm kích thước
        JPanel p = new JPanel(new MigLayout(
            "insets 0, gapy 14, hidemode 3, wrap 4", 
            "[100!,shrink 0][fill,150:200:,grow][100!,shrink 0][fill,150:200:,grow]",
            "[]"));
        p.setBackground(CARD_COLOR);
        
        // Section 1: Thông tin cơ bản với box màu
        JPanel section1 = createSectionHeader("Thông tin cơ bản", ACCENT_COLOR);
        p.add(section1, "span 4, growx");
        
        p.add(createBoldLabel("Member ID", true), "");
        p.add(memberId, "growx, wmin 100");
        p.add(createBoldLabel("Appointment ID", false), "");
        p.add(appointmentId, "growx, wmin 100");
        
        p.add(createBoldLabel("Loại tài liệu", true), "");
        p.add(type, "growx, wmin 100");
        p.add(createBoldLabel("Tiêu đề", true), "");
        p.add(title, "growx, wmin 100");
        
        // Section 2: File đính kèm với box màu
        JPanel section2 = createSectionHeader("File đính kèm", new Color(156, 39, 176));
        p.add(section2, "newline, span 4, growx, gaptop 16");
        
        // Đường dẫn với highlight box màu tím
        JPanel pathPanel = new JPanel(new BorderLayout(8, 8)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background gradient tím nhạt
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(243, 229, 245),
                    0, getHeight(), new Color(250, 245, 252)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                // Border tím đậm
                g2.setColor(new Color(156, 39, 176));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 10, 10);
                
                g2.dispose();
            }
        };
        pathPanel.setOpaque(false);
        pathPanel.setBorder(new EmptyBorder(12, 14, 12, 14));
        
        JPanel fpPanel = new JPanel(new BorderLayout(8, 8));
        fpPanel.setOpaque(false);
        fpPanel.add(filePath, BorderLayout.CENTER);
        fpPanel.add(browse, BorderLayout.EAST);
        pathPanel.add(fpPanel, BorderLayout.CENTER);
        
        p.add(createBoldLabel("Đường dẫn", false), "newline");
        p.add(pathPanel, "growx, span 3, wmin 200");
        
        // Mô tả với highlight box màu xanh lá
        JPanel descPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background gradient xanh lá nhạt
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(232, 245, 233),
                    0, getHeight(), new Color(245, 250, 245)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                // Border xanh lá đậm
                g2.setColor(new Color(76, 175, 80));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 10, 10);
                
                g2.dispose();
            }
        };
        descPanel.setOpaque(false);
        descPanel.setBorder(new EmptyBorder(12, 14, 12, 14));
        
        JScrollPane descScroll = new JScrollPane(description);
        descScroll.setBorder(BorderFactory.createEmptyBorder());
        descScroll.setOpaque(false);
        descScroll.getViewport().setOpaque(false);
        descPanel.add(descScroll, BorderLayout.CENTER);
        
        p.add(createBoldLabel("Mô tả", false), "newline");
        p.add(descPanel, "span 3, growx, h 100::, wmin 200");
        
        // Section 3: Thông tin bổ sung với box màu
        JPanel section3 = createSectionHeader("Thông tin bổ sung", new Color(255, 152, 0));
        p.add(section3, "newline, span 4, growx, gaptop 16");
        
        p.add(createBoldLabel("Ngày tài liệu", false), "newline");
        p.add(docDate, "growx, wmin 100");
        p.add(createBoldLabel("Uploaded By", false), "");
        p.add(uploadedBy, "growx, wmin 100");
        
        // Action buttons với spacing đẹp
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 16));
        btnPanel.setBackground(CARD_COLOR);
        btnPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(230, 230, 230)),
            new EmptyBorder(4, 0, 0, 0)
        ));
        
        btnPanel.add(btnAdd);
        btnPanel.add(btnClear);
        btnPanel.add(btnRefresh);
        btnPanel.add(createVerticalSeparator());
        btnPanel.add(btnOpen);
        btnPanel.add(btnPreview);
        btnPanel.add(btnDelete);
        btnPanel.add(createVerticalSeparator());
        btnPanel.add(btnExport);
        btnPanel.add(btnPrint);
        btnPanel.add(createVerticalSeparator());
        btnPanel.add(btnDark);
        
        p.add(btnPanel, "newline, span 4, growx, gaptop 12");
        
        formCard.add(p, BorderLayout.CENTER);
        
        // Add formCard directly (outer ScrollPane handles scrolling)
        cardContainer.add(formCard, BorderLayout.CENTER);
        
        main.add(header, BorderLayout.NORTH);
        main.add(cardContainer, BorderLayout.CENTER);
        
        return main;
    }
    
    private JPanel createSectionHeader(String text, Color accentColor) {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int w = getWidth();
                int h = getHeight();
                
                // Gradient nền từ màu nhạt sang màu đậm
                Color color1 = new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 15);
                Color color2 = new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 50);
                GradientPaint gradient = new GradientPaint(0, 0, color1, w, h, color2);
                g2.setPaint(gradient);
                g2.fillRect(0, 0, w, h);
                
                // Vẽ border trái với gradient dọc (màu sáng → đậm)
                GradientPaint borderGradient = new GradientPaint(
                    0, 0, brighten(accentColor, 1.3),
                    0, h, accentColor.darker()
                );
                g2.setPaint(borderGradient);
                g2.fillRect(0, 0, 4, h);
                
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(12, 16, 12, 16));
        
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(accentColor.darker());
        
        panel.add(label, BorderLayout.WEST);
        return panel;
    }
    
    private JLabel createBoldLabel(String text, boolean required) {
        JLabel label = new JLabel(text + (required ? " *" : ""));
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(required ? new Color(211, 47, 47) : TEXT_PRIMARY);
        return label;
    }
    
    private Component createVerticalSeparator() {
        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setPreferredSize(new Dimension(1, 28));
        sep.setForeground(new Color(200, 200, 200));
        return sep;
    }

    private JComponent buildCenter() {
        JPanel center = new JPanel(new BorderLayout(0, 0));
        center.setBackground(BACKGROUND_COLOR);
        center.setBorder(new EmptyBorder(0, 16, 16, 16));
        
        // Style table
        table.setRowSorter(sorter);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(PRIMARY_LIGHT);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(BORDER_COLOR);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        
        // Set column widths for better visibility
        table.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(80);   // Member
        table.getColumnModel().getColumn(2).setPreferredWidth(80);   // Appt
        table.getColumnModel().getColumn(3).setPreferredWidth(100);  // Type
        table.getColumnModel().getColumn(4).setPreferredWidth(200);  // Title
        table.getColumnModel().getColumn(5).setPreferredWidth(150);  // File
        table.getColumnModel().getColumn(6).setPreferredWidth(250);  // Desc
        table.getColumnModel().getColumn(7).setPreferredWidth(100);  // Doc Date
        table.getColumnModel().getColumn(8).setPreferredWidth(100);  // Uploaded By
        table.getColumnModel().getColumn(9).setPreferredWidth(150);  // Uploaded At
        
        // Enable horizontal scrolling
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        // Style table header với gradient
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setBorder(BorderFactory.createEmptyBorder());
        
        // Custom renderer cho header với gradient
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value != null ? value.toString() : "") {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        
                        // Gradient từ xanh đậm sang xanh lục
                        GradientPaint gradient = new GradientPaint(
                            0, 0, PRIMARY_COLOR,
                            getWidth(), getHeight(), ACCENT_COLOR
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
                label.setBorder(new EmptyBorder(0, 8, 0, 8));
                label.setHorizontalAlignment(SwingConstants.LEFT);
                return label;
            }
        });
        
        // Alternating row colors với gradient
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (isSelected) {
                    // Gradient cho dòng được chọn
                    setBackground(new Color(225, 245, 255));
                    setForeground(TEXT_PRIMARY);
                } else {
                    // Gradient nhẹ cho dòng xen kẽ
                    if (row % 2 == 0) {
                        setBackground(Color.WHITE);
                    } else {
                        // Tạo gradient effect cho alternating rows
                        setBackground(new Color(248, 251, 255));
                    }
                    setForeground(TEXT_PRIMARY);
                }
                
                setBorder(new EmptyBorder(4, 8, 4, 8));
                return c;
            }
        });
        
        // Filter panel
        JPanel filterPanel = new JPanel(new BorderLayout(8, 8));
        filterPanel.setBackground(CARD_COLOR);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(12, 16, 12, 16)
        ));
        
        JLabel filterIcon = new JLabel("🔍");
        filterIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        
        JLabel filterLabel = new JLabel("  Bộ lọc tìm kiếm");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filterLabel.setForeground(PRIMARY_COLOR);
        
        JPanel filterHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filterHeader.setBackground(CARD_COLOR);
        filterHeader.add(filterIcon);
        filterHeader.add(filterLabel);
        
        JPanel filterInputs = new JPanel(new MigLayout("insets 8 0 0 0", "[80!]8[fill,grow]16[80!]8[120!]16[80!]8[fill,grow]"));
        filterInputs.setBackground(CARD_COLOR);
        
        filterType.addItem("Tất cả");
        for (DocumentType dt : DocumentType.values()) filterType.addItem(dt);
        
        filterInputs.add(createSimpleLabel("Member ID"), "");
        filterInputs.add(filterMember, "growx");
        filterInputs.add(createSimpleLabel("Loại"), "");
        filterInputs.add(filterType, "growx");
        filterInputs.add(createSimpleLabel("Tiêu đề"), "");
        filterInputs.add(filterText, "growx");
        
        filterPanel.add(filterHeader, BorderLayout.NORTH);
        filterPanel.add(filterInputs, BorderLayout.CENTER);
        
        // Table container - bỏ preview panel
        JPanel tableContainer = new JPanel(new BorderLayout(0, 0));
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        // Không dùng split pane nữa, chỉ hiển thị bảng
        JPanel contentPanel = new JPanel(new BorderLayout(0, 12));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.add(filterPanel, BorderLayout.NORTH);
        contentPanel.add(tableContainer, BorderLayout.CENTER);
        
        center.add(contentPanel, BorderLayout.CENTER);

        JPopupMenu cm = new JPopupMenu();
        JMenuItem miOpen = new JMenuItem("📂 Mở file");
        JMenuItem miFolder = new JMenuItem("📁 Mở thư mục");
        JMenuItem miCopy = new JMenuItem("📋 Copy dòng");
        JMenuItem miDel = new JMenuItem("🗑 Xoá dòng");
        JMenu colMenu = new JMenu("👁 Cột hiển thị");
        
        styleMenuItem(miOpen);
        styleMenuItem(miFolder);
        styleMenuItem(miCopy);
        styleMenuItem(miDel);
        
        cm.add(miOpen); cm.add(miFolder); cm.add(miCopy); cm.add(miDel); 
        cm.addSeparator(); cm.add(colMenu);
        table.setComponentPopupMenu(cm);

        miOpen.addActionListener(e -> previewOrOpen(true));
        miFolder.addActionListener(e -> openFolder());
        miCopy.addActionListener(e -> copySelectionToClipboard());
        miDel.addActionListener(e -> deleteSelected());
        buildColumnChooser(colMenu);

        return center;
    }
    
    private void styleMenuItem(JMenuItem item) {
        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        item.setBorder(new EmptyBorder(6, 12, 6, 12));
    }

    private JComponent buildStatus() {
        JPanel s = new JPanel(new BorderLayout());
        s.setBackground(PRIMARY_DARK);
        s.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        status.setForeground(Color.WHITE);
        status.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel copyright = new JLabel("© 2025 EHR System - Hệ thống quản lý hồ sơ y tế điện tử");
        copyright.setForeground(new Color(200, 200, 200));
        copyright.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        s.add(status, BorderLayout.WEST);
        s.add(copyright, BorderLayout.EAST);
        return s;
    }

    private void hookEvents() {
        browse.addActionListener(this::onBrowse);
        btnAdd.addActionListener(this::onAdd);
        btnClear.addActionListener(e -> clearForm());
        btnRefresh.addActionListener(e -> refreshTable());
        btnOpen.addActionListener(e -> openFolder());
        btnPreview.addActionListener(e -> previewOrOpen(false));
        btnDelete.addActionListener(e -> deleteSelected());
        btnExport.addActionListener(e -> exportCsv());
        btnPrint.addActionListener(e -> printTable());
        btnDark.addActionListener(e -> toggleDarkMode());
        filterMember.getDocument().addDocumentListener(d(this::applyFilters));
        filterText.getDocument().addDocumentListener(d(this::applyFilters));
        filterType.addActionListener(e -> applyFilters());
        table.getSelectionModel().addListSelectionListener(e -> previewFromTable());
        table.registerKeyboardAction(e -> copySelectionToClipboard(), KeyStroke.getKeyStroke("ctrl C"), JComponent.WHEN_FOCUSED);
    }
    
    private void toggleDarkMode() {
        isDarkMode = !isDarkMode;
        applyTheme();
        SwingUtilities.updateComponentTreeUI(SwingUtilities.getWindowAncestor(this));
    }
    
    private void applyTheme() {
        Color bgColor = isDarkMode ? DARK_BG : BACKGROUND_COLOR;
        Color cardColor = isDarkMode ? DARK_CARD : CARD_COLOR;
        Color textColor = isDarkMode ? DARK_TEXT : TEXT_PRIMARY;
        Color borderColor = isDarkMode ? DARK_BORDER : BORDER_COLOR;
        
        // Update panel backgrounds
        setBackground(bgColor);
        updateComponentColors(this, cardColor, textColor, borderColor);
        
        // Update button appearance
        if (isDarkMode) {
            btnDark.setBackground(PRIMARY_COLOR);
            btnDark.setForeground(Color.WHITE);
            btnDark.setText("☀ Sáng");
        } else {
            btnDark.setBackground(Color.WHITE);
            btnDark.setForeground(TEXT_PRIMARY);
            btnDark.setText("🌙 Tối");
        }
        
        // Update table colors
        if (isDarkMode) {
            table.setBackground(DARK_CARD);
            table.setForeground(DARK_TEXT);
            table.setGridColor(DARK_BORDER);
            table.setSelectionBackground(PRIMARY_DARK);
            table.setSelectionForeground(Color.WHITE);
        } else {
            table.setBackground(Color.WHITE);
            table.setForeground(TEXT_PRIMARY);
            table.setGridColor(BORDER_COLOR);
            table.setSelectionBackground(PRIMARY_LIGHT);
            table.setSelectionForeground(Color.WHITE);
        }
        
        revalidate();
        repaint();
    }
    
    private void updateComponentColors(Container container, Color cardColor, Color textColor, Color borderColor) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                // Don't change colored section headers
                if (panel.getBorder() != null && panel.getBackground().getAlpha() < 255) {
                    // Keep section header colors
                } else if (panel.getBackground().equals(CARD_COLOR) || panel.getBackground().equals(DARK_CARD)) {
                    panel.setBackground(cardColor);
                } else if (panel.getBackground().equals(BACKGROUND_COLOR) || panel.getBackground().equals(DARK_BG)) {
                    panel.setBackground(isDarkMode ? DARK_BG : BACKGROUND_COLOR);
                }
                updateComponentColors(panel, cardColor, textColor, borderColor);
            } else if (comp instanceof JTextField) {
                JTextField field = (JTextField) comp;
                field.setBackground(cardColor);
                field.setForeground(textColor);
                field.setCaretColor(textColor);
            } else if (comp instanceof JTextArea) {
                JTextArea area = (JTextArea) comp;
                area.setBackground(cardColor);
                area.setForeground(textColor);
                area.setCaretColor(textColor);
            } else if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                // Keep colored labels (section headers, etc)
                if (!label.getForeground().equals(Color.WHITE) && 
                    !label.getForeground().equals(PRIMARY_COLOR) &&
                    !label.getForeground().equals(ACCENT_COLOR) &&
                    !label.getForeground().equals(new Color(156, 39, 176)) &&
                    !label.getForeground().equals(new Color(255, 152, 0)) &&
                    !label.getForeground().equals(new Color(211, 47, 47))) {
                    label.setForeground(textColor);
                }
            } else if (comp instanceof JComboBox) {
                JComboBox<?> combo = (JComboBox<?>) comp;
                combo.setBackground(cardColor);
                combo.setForeground(textColor);
            } else if (comp instanceof JScrollPane) {
                JScrollPane scroll = (JScrollPane) comp;
                scroll.getViewport().setBackground(cardColor);
                updateComponentColors(scroll.getViewport(), cardColor, textColor, borderColor);
            } else if (comp instanceof Container) {
                updateComponentColors((Container) comp, cardColor, textColor, borderColor);
            }
        }
    }

    private DocumentListener d(Runnable r){
        return new DocumentListener() {
            public void insertUpdate(DocumentEvent e){r.run();}
            public void removeUpdate(DocumentEvent e){r.run();}
            public void changedUpdate(DocumentEvent e){r.run();}
        };
    }

    private void onBrowse(ActionEvent e){
        JFileChooser fc = new JFileChooser();
        int ret = fc.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) filePath.setText(fc.getSelectedFile().getAbsolutePath());
    }

    private void onAdd(ActionEvent e){
        try {
            String mid = memberId.getText().trim();
            String apt = appointmentId.getText().trim();
            String t = title.getText().trim();
            String fp = filePath.getText().trim();
            String desc = description.getText().trim();
            String upBy = uploadedBy.getText().trim();
            LocalDate d = docDate.getDate();
            if (mid.isEmpty() || t.isEmpty()) { JOptionPane.showMessageDialog(this,"Member ID và Tiêu đề là bắt buộc"); return; }
            Documents doc = new Documents();
            doc.setMemberId(Integer.parseInt(mid));
            if (!apt.isEmpty()) doc.setAppointmentId(Integer.parseInt(apt));
            doc.setType((DocumentType) type.getSelectedItem());
            doc.setTitle(t);
            doc.setFilePath(fp.isEmpty()?null:fp);
            doc.setDescription(desc);
            if (d != null) doc.setDocumentDate(d);
            if (!upBy.isEmpty()) doc.setUploadedByUserId(Integer.parseInt(upBy));
            int id = dao.insert(doc);
            if (id > 0) { clearForm(); refreshTable(); JOptionPane.showMessageDialog(this,"Đã thêm ID = "+id); }
            else JOptionPane.showMessageDialog(this,"Không thể thêm");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,"Member ID/Appointment ID/Uploaded By phải là số");
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,"Ngày không hợp lệ");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void clearForm(){
        memberId.setText("");
        appointmentId.setText("");
        type.setSelectedIndex(0);
        title.setText("");
        filePath.setText("");
        description.setText("");
        docDate.clear();
        uploadedBy.setText("");
    }

    private void refreshTable() {
        try {
            List<Documents> list = dao.listAll(null, null);
            model.setRowCount(0);
            for (Documents d : list) {
                model.addRow(new Object[]{
                        d.getId(),
                        d.getMemberId(),
                        d.getAppointmentId(),
                        d.getType()==null ? "" : d.getType().getDisplayName(),
                        d.getTitle(),
                        d.getFilePath(),
                        d.getDescription(),
                        d.getDocumentDate()==null?null:d.getDocumentDate().toString(),
                        d.getUploadedByUserId(),
                        d.getUploadedAt()==null?null:d.getUploadedAt().toString()
                });
            }
            applyFilters();
            updateStatus();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void applyFilters() {
        List<RowFilter<DefaultTableModel, Integer>> filters = new ArrayList<>();
        String mf = filterMember.getText().trim();
        if (!mf.isEmpty()) filters.add(RowFilter.regexFilter("^"+Pattern.quote(mf)+"$", 1));
        Object ft = filterType.getSelectedItem();
        if (ft instanceof DocumentType) filters.add(RowFilter.regexFilter("^"+((DocumentType)ft).name()+"$", 3));
        String txt = filterText.getText().trim();
        if (!txt.isEmpty()) filters.add(RowFilter.regexFilter("(?i)"+Pattern.quote(txt), 4));
        sorter.setRowFilter(filters.isEmpty()?null:RowFilter.andFilter(filters));
        updateStatus();
    }

    private void updateStatus() {
        int visible = table.getRowCount();
        int total = model.getRowCount();
        String icon = visible == total ? "📊" : "🔍";
        status.setText(String.format("%s Hiển thị: %d / Tổng: %d tài liệu", icon, visible, total));
    }

    private void openFolder() {
        int r = table.getSelectedRow();
        if (r < 0) return;
        r = table.convertRowIndexToModel(r);
        String path = String.valueOf(model.getValueAt(r,5));
        if (path == null || path.equals("null") || path.isEmpty()) return;
        try {
            File f = new File(path);
            File dir = f.isDirectory()? f : f.getParentFile();
            if (dir != null && dir.exists()) Desktop.getDesktop().open(dir);
        } catch (Exception ignored) {}
    }

    private void previewOrOpen(boolean forceOpen) {
        int r = table.getSelectedRow();
        if (r < 0) return;
        r = table.convertRowIndexToModel(r);
        String path = String.valueOf(model.getValueAt(r,5));
        if (path == null || path.equals("null") || path.isEmpty()) return;
        File f = new File(path);
        String name = path.toLowerCase();
        if (!forceOpen && (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg"))) previewPanel.showFile(f);
        else try { Desktop.getDesktop().open(f); } catch (Exception ignored) {}
    }

    private void previewFromTable() {
        int r = table.getSelectedRow();
        if (r < 0) { previewPanel.showFile(null); return; }
        r = table.convertRowIndexToModel(r);
        Object p = model.getValueAt(r,5);
        if (p == null) { previewPanel.showFile(null); return; }
        File f = new File(p.toString());
        String name = f.getName().toLowerCase();
        if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg")) previewPanel.showFile(f);
        else previewPanel.showFile(null);
    }

    private void deleteSelected() {
        int[] rows = table.getSelectedRows();
        if (rows.length == 0) return;
        int c = JOptionPane.showConfirmDialog(this,"Xoá "+rows.length+" dòng?","Xác nhận",JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) return;
        try {
            List<Integer> ids = new ArrayList<>();
            for (int vr : rows) {
                int mr = table.convertRowIndexToModel(vr);
                int id = Integer.parseInt(String.valueOf(model.getValueAt(mr,0)));
                ids.add(id);
            }
            dao.deleteByIds(ids);
            refreshTable();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void copySelectionToClipboard() {
        int[] viewRows = table.getSelectedRows();
        if (viewRows.length == 0) return;
        StringBuilder sb = new StringBuilder();
        for (int vr : viewRows) {
            int mr = table.convertRowIndexToModel(vr);
            for (int c = 0; c < model.getColumnCount(); c++) {
                if (c > 0) sb.append('\t');
                Object v = model.getValueAt(mr, c);
                sb.append(v == null ? "" : String.valueOf(v));
            }
            sb.append('\n');
        }
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(sb.toString()), null);
        JOptionPane.showMessageDialog(this,"Đã copy "+viewRows.length+" dòng");
    }

    private void exportCsv() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File("documents.csv"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File f = fc.getSelectedFile();
        try {
            List<String> lines = new ArrayList<>();
            // Header row with semicolon delimiter
            lines.add(Arrays.stream(getCols()).collect(Collectors.joining(";")));
            
            // Data rows
            for (int i = 0; i < table.getRowCount(); i++) {
                int mr = table.convertRowIndexToModel(i);
                List<String> row = new ArrayList<>();
                for (int c = 0; c < model.getColumnCount(); c++) {
                    Object v = model.getValueAt(mr, c);
                    String s = v == null ? "" : String.valueOf(v);
                    // Escape quotes and wrap fields containing semicolon, quotes, or newlines
                    if (s.contains(";") || s.contains("\"") || s.contains("\n")) {
                        s = "\"" + s.replace("\"", "\"\"") + "\"";
                    }
                    row.add(s);
                }
                lines.add(String.join(";", row));
            }
            
            // Write with UTF-8 BOM for Excel compatibility
            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
                 java.io.OutputStreamWriter writer = new java.io.OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
                // Write UTF-8 BOM
                fos.write(0xEF);
                fos.write(0xBB);
                fos.write(0xBF);
                // Write content
                writer.write(String.join("\n", lines));
                writer.flush();
            }
            
            JOptionPane.showMessageDialog(this,"Đã export: "+f.getAbsolutePath());
        } catch (Exception ex) { 
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi xuất file: " + ex.getMessage()); 
        }
    }

    private String[] getCols() {
        String[] a = new String[model.getColumnCount()];
        for (int i=0;i<a.length;i++) a[i]=model.getColumnName(i);
        return a;
    }

    private void printTable() {
        try { table.print(); } catch (Exception ignored) {}
    }

    private void initDnD() {
        new DropTarget(filePath, new DropTargetListener() {
            public void dragEnter(DropTargetDragEvent dtde) {}
            public void dragOver(DropTargetDragEvent dtde) {}
            public void dropActionChanged(DropTargetDragEvent dtde) {}
            public void dragExit(DropTargetEvent dte) {}
            public void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    List<?> list = (List<?>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    if (!list.isEmpty()) filePath.setText(((File) list.get(0)).getAbsolutePath());
                } catch (Exception ignored) {}
            }
        });
    }

    private void buildColumnChooser(JMenu menu) {
        for (int i = 0; i < model.getColumnCount(); i++) {
            int idx = i;
            JCheckBoxMenuItem mi = new JCheckBoxMenuItem(model.getColumnName(i), true);
            mi.addActionListener(e -> setColumnVisible(idx, mi.isSelected()));
            menu.add(mi);
        }
    }

    private void setColumnVisible(int index, boolean visible) {
        TableColumn col = table.getColumnModel().getColumn(index);
        if (visible) col.setMinWidth(15);
        else { col.setMinWidth(0); col.setMaxWidth(0); col.setPreferredWidth(0); }
    }
    
    private JLabel createSimpleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Bold để nét hơn
        label.setForeground(TEXT_SECONDARY);
        return label;
    }
}
