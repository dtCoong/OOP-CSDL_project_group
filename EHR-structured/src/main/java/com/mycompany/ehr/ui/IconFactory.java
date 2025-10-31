package com.mycompany.ehr.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

/**
 * Factory class để tạo icon vector cho ứng dụng
 * Icon được vẽ bằng Graphics2D nên sẽ luôn hiển thị đẹp
 */
public class IconFactory {
    
    private static final Color ICON_COLOR = Color.WHITE;
    private static final int DEFAULT_SIZE = 20;
    
    /**
     * Tạo icon "+" (Plus/Add) - Cải tiến
     */
    public static ImageIcon createAddIcon(Color color, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setStroke(new BasicStroke(2.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(color);
        
        int center = size / 2;
        int length = (int)(size * 0.55);
        
        // Vẽ dấu +
        g2.drawLine(center, center - length/2, center, center + length/2); // Vertical
        g2.drawLine(center - length/2, center, center + length/2, center); // Horizontal
        
        g2.dispose();
        return new ImageIcon(img);
    }
    
    /**
     * Tạo icon refresh (circular arrow) - Cải tiến
     */
    public static ImageIcon createRefreshIcon(Color color, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(color);
        
        int center = size / 2;
        int radius = (int)(size * 0.38);
        
        // Vẽ cung tròn
        Arc2D arc = new Arc2D.Double(center - radius, center - radius, 
                                     radius * 2, radius * 2, 40, 280, Arc2D.OPEN);
        g2.draw(arc);
        
        // Vẽ mũi tên đẹp hơn
        int arrowX = (int)(center + radius * Math.cos(Math.toRadians(320)));
        int arrowY = (int)(center - radius * Math.sin(Math.toRadians(320)));
        
        Polygon arrow = new Polygon();
        arrow.addPoint(arrowX, arrowY);
        arrow.addPoint(arrowX - 6, arrowY - 6);
        arrow.addPoint(arrowX + 4, arrowY - 4);
        g2.fill(arrow);
        
        g2.dispose();
        return new ImageIcon(img);
    }
    
    /**
     * Tạo icon trash/delete - Cải tiến
     */
    public static ImageIcon createTrashIcon(Color color, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(color);
        
        int w = (int)(size * 0.55);
        int h = (int)(size * 0.6);
        int x = (size - w) / 2;
        int y = (size - h) / 2 + 2;
        
        // Vẽ nắp thùng rác
        g2.drawLine(x - 2, y, x + w + 2, y);
        g2.drawLine(x + w/3, y - 3, x + 2*w/3, y - 3);
        
        // Vẽ thân thùng rác bo tròn
        Path2D bin = new Path2D.Double();
        bin.moveTo(x + 2, y + 2);
        bin.lineTo(x + 3, y + h - 2);
        bin.quadTo(x + 3, y + h, x + 5, y + h);
        bin.lineTo(x + w - 5, y + h);
        bin.quadTo(x + w - 3, y + h, x + w - 3, y + h - 2);
        bin.lineTo(x + w - 2, y + 2);
        g2.draw(bin);
        
        // Vẽ các đường gạch trong thùng
        g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(x + w/3, y + 5, x + w/3, y + h - 4);
        g2.drawLine(x + 2*w/3, y + 5, x + 2*w/3, y + h - 4);
        
        g2.dispose();
        return new ImageIcon(img);
    }
    
    /**
     * Tạo icon folder - Cải tiến
     */
    public static ImageIcon createFolderIcon(Color color, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setColor(color);
        
        int w = (int)(size * 0.72);
        int h = (int)(size * 0.58);
        int x = (size - w) / 2;
        int y = (size - h) / 2 + 2;
        
        // Vẽ folder với góc bo tròn
        Path2D folder = new Path2D.Double();
        folder.moveTo(x, y + 4);
        folder.lineTo(x, y + h - 2);
        folder.quadTo(x, y + h, x + 2, y + h);
        folder.lineTo(x + w - 2, y + h);
        folder.quadTo(x + w, y + h, x + w, y + h - 2);
        folder.lineTo(x + w, y + 4);
        folder.quadTo(x + w, y + 2, x + w - 2, y + 2);
        folder.lineTo(x + w/2 + 4, y + 2);
        folder.lineTo(x + w/2 + 1, y - 1);
        folder.quadTo(x + w/2, y - 2, x + w/2 - 1, y - 1);
        folder.lineTo(x + w/2 - 4, y + 2);
        folder.lineTo(x + 2, y + 2);
        folder.quadTo(x, y + 2, x, y + 4);
        folder.closePath();
        
        g2.fill(folder);
        
        g2.dispose();
        return new ImageIcon(img);
    }
    
    /**
     * Tạo icon eye (preview) - Cải tiến
     */
    public static ImageIcon createEyeIcon(Color color, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(color);
        
        int center = size / 2;
        int w = (int)(size * 0.68);
        int h = (int)(size * 0.42);
        
        // Vẽ đường viền mắt mượt hơn
        Path2D eye = new Path2D.Double();
        eye.moveTo(center - w/2, center);
        eye.curveTo(center - w/2, center - h/2 - 1, center - w/4, center - h/2 - 2, center, center - h/2 - 2);
        eye.curveTo(center + w/4, center - h/2 - 2, center + w/2, center - h/2 - 1, center + w/2, center);
        eye.curveTo(center + w/2, center + h/2 + 1, center + w/4, center + h/2 + 2, center, center + h/2 + 2);
        eye.curveTo(center - w/4, center + h/2 + 2, center - w/2, center + h/2 + 1, center - w/2, center);
        g2.draw(eye);
        
        // Vẽ đồng tử
        int pupilSize = (int)(size * 0.22);
        g2.fillOval(center - pupilSize/2, center - pupilSize/2, pupilSize, pupilSize);
        
        g2.dispose();
        return new ImageIcon(img);
    }
    
    /**
     * Tạo icon printer
     */
    public static ImageIcon createPrinterIcon(Color color, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(color);
        
        int w = (int)(size * 0.65);
        int h = (int)(size * 0.7);
        int x = (size - w) / 2;
        int y = (size - h) / 2;
        
        // Vẽ thân máy in
        g2.drawRoundRect(x, y + h/4, w, h/2, 3, 3);
        
        // Vẽ giấy trên
        g2.drawLine(x + 3, y, x + w - 3, y);
        g2.drawLine(x + 3, y, x + 3, y + h/4);
        g2.drawLine(x + w - 3, y, x + w - 3, y + h/4);
        
        // Vẽ giấy dưới
        g2.fillRect(x + 4, y + 3*h/4 - 2, w - 8, h/4 + 2);
        
        g2.dispose();
        return new ImageIcon(img);
    }
    
    /**
     * Tạo icon save/export
     */
    public static ImageIcon createSaveIcon(Color color, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        
        int w = (int)(size * 0.65);
        int h = (int)(size * 0.7);
        int x = (size - w) / 2;
        int y = (size - h) / 2;
        
        // Vẽ đĩa mềm
        Path2D disk = new Path2D.Double();
        disk.moveTo(x, y);
        disk.lineTo(x + w - 4, y);
        disk.lineTo(x + w, y + 4);
        disk.lineTo(x + w, y + h);
        disk.lineTo(x, y + h);
        disk.closePath();
        g2.fill(disk);
        
        // Vẽ nhãn trắng
        g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
        g2.fillRect(x + 3, y + 3, w - 6, h/3);
        
        g2.dispose();
        return new ImageIcon(img);
    }
    
    /**
     * Tạo icon search
     */
    public static ImageIcon createSearchIcon(Color color, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(color);
        
        int circleSize = (int)(size * 0.5);
        int x = (size - circleSize) / 2 - 1;
        int y = (size - circleSize) / 2 - 1;
        
        // Vẽ kính lúp
        g2.drawOval(x, y, circleSize, circleSize);
        
        // Vẽ tay cầm
        int handleX = x + circleSize + 1;
        int handleY = y + circleSize + 1;
        g2.drawLine(handleX, handleY, handleX + 5, handleY + 5);
        
        g2.dispose();
        return new ImageIcon(img);
    }
    
    /**
     * Tạo icon moon (dark mode)
     */
    public static ImageIcon createMoonIcon(Color color, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        
        int moonSize = (int)(size * 0.7);
        int x = (size - moonSize) / 2;
        int y = (size - moonSize) / 2;
        
        // Vẽ trăng khuyết
        Ellipse2D moon = new Ellipse2D.Double(x, y, moonSize, moonSize);
        Ellipse2D shadow = new Ellipse2D.Double(x + moonSize/4, y - moonSize/8, moonSize, moonSize);
        
        Area moonArea = new Area(moon);
        moonArea.subtract(new Area(shadow));
        g2.fill(moonArea);
        
        g2.dispose();
        return new ImageIcon(img);
    }
    
    /**
     * Tạo icon sun (light mode)
     */
    public static ImageIcon createSunIcon(Color color, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(color);
        
        int center = size / 2;
        int sunSize = (int)(size * 0.35);
        
        // Vẽ hình tròn mặt trời
        g2.fillOval(center - sunSize/2, center - sunSize/2, sunSize, sunSize);
        
        // Vẽ các tia
        int rayLength = (int)(size * 0.15);
        int outerRadius = sunSize/2 + rayLength;
        for (int i = 0; i < 8; i++) {
            double angle = Math.toRadians(i * 45);
            int x1 = (int)(center + (sunSize/2 + 2) * Math.cos(angle));
            int y1 = (int)(center + (sunSize/2 + 2) * Math.sin(angle));
            int x2 = (int)(center + outerRadius * Math.cos(angle));
            int y2 = (int)(center + outerRadius * Math.sin(angle));
            g2.drawLine(x1, y1, x2, y2);
        }
        
        g2.dispose();
        return new ImageIcon(img);
    }
    
    /**
     * Tạo icon medical/hospital
     */
    public static ImageIcon createMedicalIcon(Color color, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        
        int crossSize = (int)(size * 0.65);
        int x = (size - crossSize) / 2;
        int y = (size - crossSize) / 2;
        int thickness = crossSize / 3;
        
        // Vẽ chữ thập y tế
        // Vertical
        g2.fillRect(x + thickness, y, thickness, crossSize);
        // Horizontal
        g2.fillRect(x, y + thickness, crossSize, thickness);
        
        g2.dispose();
        return new ImageIcon(img);
    }
    
    /**
     * Tạo icon clear/reset
     */
    public static ImageIcon createClearIcon(Color color, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setStroke(new BasicStroke(2.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(color);
        
        int pad = (int)(size * 0.28);
        
        // Vẽ dấu X
        g2.drawLine(pad, pad, size - pad, size - pad);
        g2.drawLine(size - pad, pad, pad, size - pad);
        
        g2.dispose();
        return new ImageIcon(img);
    }
    
    /**
     * Tạo icon info/document (cho section headers)
     */
    public static ImageIcon createInfoIcon(Color color, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setColor(color);
        
        int center = size / 2;
        int radius = (int)(size * 0.4);
        
        // Vẽ vòng tròn
        g2.setStroke(new BasicStroke(2.2f));
        g2.drawOval(center - radius, center - radius, radius * 2, radius * 2);
        
        // Vẽ chữ i
        g2.fillOval(center - 2, center - radius + 4, 4, 4); // Dot
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(center, center - radius/2 + 2, center, center + radius - 4); // Line
        
        g2.dispose();
        return new ImageIcon(img);
    }
    
    /**
     * Tạo icon attachment/paperclip (cho file đính kèm)
     */
    public static ImageIcon createAttachmentIcon(Color color, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(color);
        
        int w = (int)(size * 0.35);
        int h = (int)(size * 0.65);
        int x = (size - w) / 2 + 3;
        int y = (size - h) / 2;
        
        // Vẽ paperclip
        Path2D clip = new Path2D.Double();
        clip.moveTo(x + w, y + h/4);
        clip.lineTo(x + w, y + h - 4);
        clip.quadTo(x + w, y + h, x + w - 4, y + h);
        clip.quadTo(x, y + h, x, y + h - 4);
        clip.lineTo(x, y + 4);
        clip.quadTo(x, y, x + 4, y);
        clip.quadTo(x + w - 2, y, x + w - 2, y + 4);
        clip.lineTo(x + w - 2, y + h - 6);
        
        g2.draw(clip);
        
        g2.dispose();
        return new ImageIcon(img);
    }
    
    /**
     * Tạo icon upload (cho nút chọn file)
     */
    public static ImageIcon createUploadIcon(Color color, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(color);
        
        int center = size / 2;
        int arrowHeight = (int)(size * 0.5);
        
        // Vẽ mũi tên lên
        g2.drawLine(center, center - arrowHeight/2, center, center + arrowHeight/2);
        
        // Đầu mũi tên
        Polygon arrow = new Polygon();
        arrow.addPoint(center, center - arrowHeight/2);
        arrow.addPoint(center - 5, center - arrowHeight/2 + 6);
        arrow.addPoint(center + 5, center - arrowHeight/2 + 6);
        g2.fill(arrow);
        
        // Vẽ đế
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(center - 7, center + arrowHeight/2 + 3, center + 7, center + arrowHeight/2 + 3);
        
        g2.dispose();
        return new ImageIcon(img);
    }
    
    /**
     * Tạo icon note/description (cho thông tin bổ sung)
     */
    public static ImageIcon createNoteIcon(Color color, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setColor(color);
        
        int w = (int)(size * 0.55);
        int h = (int)(size * 0.68);
        int x = (size - w) / 2;
        int y = (size - h) / 2;
        
        // Vẽ tờ giấy với góc gấp
        Path2D note = new Path2D.Double();
        note.moveTo(x, y);
        note.lineTo(x + w - 6, y);
        note.lineTo(x + w, y + 6);
        note.lineTo(x + w, y + h);
        note.lineTo(x, y + h);
        note.closePath();
        
        g2.fill(note);
        
        // Vẽ góc gấp
        g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 120));
        Path2D corner = new Path2D.Double();
        corner.moveTo(x + w - 6, y);
        corner.lineTo(x + w - 6, y + 6);
        corner.lineTo(x + w, y + 6);
        corner.closePath();
        g2.fill(corner);
        
        // Vẽ các đường text
        g2.setColor(new Color(255, 255, 255, 180));
        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(x + 4, y + 12, x + w - 8, y + 12);
        g2.drawLine(x + 4, y + 18, x + w - 4, y + 18);
        g2.drawLine(x + 4, y + 24, x + w - 10, y + 24);
        
        g2.dispose();
        return new ImageIcon(img);
    }
    
    /**
     * Tạo icon clear/reset
     */
    public static ImageIcon createClearIcon_OLD(Color color, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(color);
        
        int pad = (int)(size * 0.25);
        
        // Vẽ dấu X
        g2.drawLine(pad, pad, size - pad, size - pad);
        g2.drawLine(size - pad, pad, pad, size - pad);
        
        g2.dispose();
        return new ImageIcon(img);
    }
    
    // Convenience methods với màu mặc định
    public static ImageIcon createAddIcon() { return createAddIcon(ICON_COLOR, DEFAULT_SIZE); }
    public static ImageIcon createRefreshIcon() { return createRefreshIcon(ICON_COLOR, DEFAULT_SIZE); }
    public static ImageIcon createTrashIcon() { return createTrashIcon(ICON_COLOR, DEFAULT_SIZE); }
    public static ImageIcon createFolderIcon() { return createFolderIcon(ICON_COLOR, DEFAULT_SIZE); }
    public static ImageIcon createEyeIcon() { return createEyeIcon(ICON_COLOR, DEFAULT_SIZE); }
    public static ImageIcon createPrinterIcon() { return createPrinterIcon(ICON_COLOR, DEFAULT_SIZE); }
    public static ImageIcon createSaveIcon() { return createSaveIcon(ICON_COLOR, DEFAULT_SIZE); }
    public static ImageIcon createSearchIcon() { return createSearchIcon(ICON_COLOR, DEFAULT_SIZE); }
    public static ImageIcon createMoonIcon() { return createMoonIcon(ICON_COLOR, DEFAULT_SIZE); }
    public static ImageIcon createSunIcon() { return createSunIcon(ICON_COLOR, DEFAULT_SIZE); }
    public static ImageIcon createMedicalIcon() { return createMedicalIcon(ICON_COLOR, DEFAULT_SIZE); }
    public static ImageIcon createClearIcon() { return createClearIcon(ICON_COLOR, DEFAULT_SIZE); }
}
