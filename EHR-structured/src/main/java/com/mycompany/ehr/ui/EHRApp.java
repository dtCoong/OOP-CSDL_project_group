package com.mycompany.ehr.ui;

import javax.swing.*;
import java.awt.*;

public class EHRApp {
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210);
    
    public static void main(String[] args) {
        try { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Button.select", new Color(66, 165, 245));
            UIManager.put("Table.selectionBackground", new Color(66, 165, 245));
            UIManager.put("Table.selectionForeground", Color.WHITE);
        } catch (Exception ignored) {}
        
        SwingUtilities.invokeLater(() -> {
            JWindow splash = createSplashScreen();
            splash.setVisible(true);
            
            Timer timer = new Timer(2000, e -> {
                splash.dispose();
                showMainWindow();
            });
            timer.setRepeats(false);
            timer.start();
        });
    }
    
    private static JWindow createSplashScreen() {
        JWindow splash = new JWindow();
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 3));
        
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Color.WHITE);
        center.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        
        JLabel logo = new JLabel("HOSPITAL");
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel title = new JLabel("EHR System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(PRIMARY_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitle = new JLabel("He thong Quan ly Ho so Y te Dien tu");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(117, 117, 117));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel version = new JLabel("Version 1.0.0");
        version.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        version.setForeground(new Color(158, 158, 158));
        version.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        center.add(logo);
        center.add(Box.createVerticalStrut(20));
        center.add(title);
        center.add(Box.createVerticalStrut(8));
        center.add(subtitle);
        center.add(Box.createVerticalStrut(20));
        center.add(version);
        
        JProgressBar progress = new JProgressBar();
        progress.setIndeterminate(true);
        progress.setPreferredSize(new Dimension(300, 8));
        progress.setBackground(Color.WHITE);
        progress.setForeground(PRIMARY_COLOR);
        progress.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        content.add(center, BorderLayout.CENTER);
        content.add(progress, BorderLayout.SOUTH);
        
        splash.setContentPane(content);
        splash.setSize(500, 400);
        splash.setLocationRelativeTo(null);
        
        return splash;
    }
    
    private static void showMainWindow() {
        JFrame f = new JFrame("EHR System - Quản lý Tài liệu & Thông báo Y tế");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabs.addTab("📄 Quản lý Tài liệu", new DocumentsPanelVN(f));
        tabs.addTab("🔔 Trung tâm Thông báo", new NotificationCenterPanel(f));
        
        f.setContentPane(tabs);
        f.setSize(1400, 850);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public static void switchTheme(boolean dark) {
        try { 
            if (dark) {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } else {
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            }
            for (Window w : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(w);
            }
        } catch (Exception ignored) {}
    }
}
