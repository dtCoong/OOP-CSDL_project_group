package com.mycompany.ehr.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class FilePreviewPanel extends JPanel {
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210);
    private static final Color BORDER_COLOR = new Color(224, 224, 224);
    private static final Color BACKGROUND_COLOR = new Color(250, 250, 250);
    
    private final JLabel title = new JLabel();
    private final JLabel image = new JLabel();
    private final JLabel fileInfo = new JLabel();
    private File current;

    public FilePreviewPanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(Color.WHITE);
        setBorder(new LineBorder(BORDER_COLOR, 1, true));
        setPreferredSize(new Dimension(380, 10));
        
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BACKGROUND_COLOR);
        header.setBorder(new EmptyBorder(12, 16, 12, 16));
        
        JLabel headerIcon = new JLabel("👁");
        headerIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        
        JLabel headerLabel = new JLabel("  Xem trước");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerLabel.setForeground(PRIMARY_COLOR);
        
        JPanel headerContent = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerContent.setBackground(BACKGROUND_COLOR);
        headerContent.add(headerIcon);
        headerContent.add(headerLabel);
        
        header.add(headerContent, BorderLayout.WEST);
        
        // Title
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        title.setForeground(new Color(117, 117, 117));
        title.setBorder(new EmptyBorder(8, 12, 8, 12));
        
        // Image
        image.setHorizontalAlignment(SwingConstants.CENTER);
        image.setVerticalAlignment(SwingConstants.CENTER);
        
        JScrollPane scrollPane = new JScrollPane(image);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        // File info
        fileInfo.setHorizontalAlignment(SwingConstants.CENTER);
        fileInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        fileInfo.setForeground(new Color(117, 117, 117));
        fileInfo.setBorder(new EmptyBorder(8, 12, 12, 12));
        
        add(header, BorderLayout.NORTH);
        add(title, BorderLayout.CENTER);
        add(fileInfo, BorderLayout.SOUTH);
        
        showFile(null);
    }

    public void showFile(File f) {
        current = f;
        if (f == null || !f.exists()) {
            title.setText("<html><div style='text-align:center; padding:40px;'>"
                + "<div style='font-size:48px; margin-bottom:16px;'>📄</div>"
                + "<div style='color:#757575;'>Không có file để xem trước</div>"
                + "</div></html>");
            image.setIcon(null);
            fileInfo.setText("");
            
            // Move title back to center when no preview
            remove(image);
            if (getComponentCount() == 2) {
                add(title, BorderLayout.CENTER);
            }
            revalidate();
            repaint();
            return;
        }
        
        String name = f.getName().toLowerCase();
        title.setText("<html><div style='padding:8px;'><b>📎 " + f.getName() + "</b></div></html>");
        
        long size = f.length();
        String sizeStr = size < 1024 ? size + " B" 
            : size < 1024*1024 ? String.format("%.1f KB", size/1024.0)
            : String.format("%.1f MB", size/(1024.0*1024.0));
        fileInfo.setText("Kích thước: " + sizeStr);
        
        if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg")) {
            try {
                BufferedImage src = ImageIO.read(f);
                if (src == null) { 
                    image.setIcon(null);
                    showNoPreview();
                    return; 
                }
                
                // Scale image to fit
                int maxWidth = getWidth() - 40;
                int maxHeight = getHeight() - 150;
                if (maxWidth <= 0) maxWidth = 340;
                if (maxHeight <= 0) maxHeight = 400;
                
                double scale = Math.min(
                    (double)maxWidth / src.getWidth(),
                    (double)maxHeight / src.getHeight()
                );
                if (scale > 1) scale = 1; // Don't upscale
                
                int newW = (int)(src.getWidth() * scale);
                int newH = (int)(src.getHeight() * scale);
                
                Image scaled = src.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
                image.setIcon(new ImageIcon(scaled));
                
                // Move image to center when showing preview
                if (getComponentCount() == 2 || 
                    ((BorderLayout)getLayout()).getLayoutComponent(BorderLayout.CENTER) != new JScrollPane(image)) {
                    remove(title);
                    JScrollPane sp = new JScrollPane(image);
                    sp.setBorder(BorderFactory.createEmptyBorder());
                    sp.getViewport().setBackground(Color.WHITE);
                    add(sp, BorderLayout.CENTER);
                }
                
                fileInfo.setText(String.format("Kích thước: %s | Độ phân giải: %dx%d", 
                    sizeStr, src.getWidth(), src.getHeight()));
                    
            } catch (Exception e) { 
                image.setIcon(null);
                showNoPreview();
            }
        } else {
            image.setIcon(null);
            showNoPreview();
        }
        
        revalidate();
        repaint();
    }
    
    private void showNoPreview() {
        remove(image);
        if (getComponentCount() == 2) {
            title.setText("<html><div style='text-align:center; padding:40px;'>"
                + "<div style='font-size:48px; margin-bottom:16px;'>📄</div>"
                + "<div style='color:#757575;'>Không hỗ trợ xem trước<br>định dạng này</div>"
                + "</div></html>");
            add(title, BorderLayout.CENTER);
        }
    }

    public File getCurrentFile() { return current; }
}
