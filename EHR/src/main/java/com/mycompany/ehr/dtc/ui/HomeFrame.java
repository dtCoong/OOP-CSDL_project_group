package com.mycompany.ehr.dtc.ui;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import java.util.List;
import java.time.LocalTime;
import com.mycompany.ehr.dtc.model.User;

import com.mycompany.ehr.lqt.ui.NotificationCenterPanel; 

import com.mycompany.ehr.npd.gui.BookingSystemGUI;

public class HomeFrame extends JFrame {

    private User currentUser;
    private final DefaultListModel<NewsItem> listModel = new DefaultListModel<>();
    private final JList<NewsItem> newsList = new JList<>(listModel);
    private final String NEWS_URL = "https://moh.gov.vn/tin-noi-bat";
    private final JLabel greetingLabel = new JLabel("Xin chào !", SwingConstants.CENTER);
    private final JButton mailButton = new JButton("\u2709");

    public HomeFrame(User user) {
        this.currentUser = user;
        setTitle("EHR - Trang chủ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 620);
        setLocationRelativeTo(null);
        initUI();
        loadNews();
        greetingLabel.setFont(greetingLabel.getFont().deriveFont(Font.BOLD, 28f));
        greetingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mailButton.setToolTipText("Hòm thư");
        mailButton.setFocusable(false);
        mailButton.setPreferredSize(new java.awt.Dimension(50, 40));
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(greetingLabel, BorderLayout.CENTER);
        topPanel.add(mailButton, BorderLayout.EAST);
        getContentPane().add(topPanel, BorderLayout.NORTH);
        
        mailButton.addActionListener(e -> {
            openNotificationCenter();
            HomeFrame.this.dispose(); 
        });

        setGreeting(currentUser.getFullName());
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setContentPane(root);

        JPanel centerButtons = new JPanel();
        centerButtons.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        JButton familyBtn = new JButton("<html><center>Thành viên<br/>gia đình</center></html>");
        familyBtn.setPreferredSize(new Dimension(170, 60));
        familyBtn.addActionListener(e -> {
            FamilyMembersFrame membersFrame = new FamilyMembersFrame(currentUser);
            membersFrame.setVisible(true);
            HomeFrame.this.dispose(); 
        });

        JButton appointmentBtn = new JButton("Đặt lịch hẹn");
        appointmentBtn.setPreferredSize(new Dimension(170, 60));

        appointmentBtn.addActionListener(e -> {
            BookingSystemGUI bookingFrame = new BookingSystemGUI(currentUser);
            bookingFrame.setVisible(true);
            HomeFrame.this.dispose(); 
        });

        gbc.gridx = 0; gbc.gridy = 0;
        centerButtons.add(familyBtn, gbc);
        gbc.gridx = 1;
        centerButtons.add(appointmentBtn, gbc);

        root.add(centerButtons, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(new TitledBorder("Tin nổi bật - Bộ Y Tế"));
        newsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(newsList);
        scroll.setPreferredSize(new Dimension(480, 260));
        bottom.add(scroll, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        newsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() >= 1) {
                    NewsItem sel = newsList.getSelectedValue();
                    if (sel != null && sel.url != null && !sel.url.isEmpty()) {
                        openInBrowser(sel.url);
                    }
                }
            }
        });
    }

    private void loadNews() {
        listModel.clear();
        listModel.addElement(new NewsItem("Đang tải tin, vui lòng chờ...", ""));

        SwingWorker<List<NewsItem>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<NewsItem> doInBackground() throws Exception {
                List<NewsItem> items = new ArrayList<>();
                try {
                    Document doc = Jsoup.connect(NEWS_URL)
                                        .userAgent("Mozilla/5.0")
                                        .timeout(20000)
                                        .get();
                    Elements links = doc.select("div.page-list-news h3 > a");
                    System.out.println("Using selector: div.page-list-news h3 > a");
                    System.out.println("Found " + links.size() + " news links.");
                    for (Element link : links) {
                        String title = link.text();
                        String url = link.absUrl("href");
                        if (!title.isEmpty() && !url.isEmpty()) {
                            items.add(new NewsItem(title, url));
                            System.out.println("-> Added: " + title);
                        }
                        if (items.size() >= 15) break; 
                    }
                    if (items.isEmpty()) {
                         System.out.println("Selector 1 failed, trying selector 2: h3.news-title > a");
                         links = doc.select("h3.news-title > a"); 
                         System.out.println("Found " + links.size() + " news links with selector 2.");
                         for (Element link : links) {
                             String title = link.text();
                             String url = link.absUrl("href");
                             if (!title.isEmpty() && !url.isEmpty()) {
                                 items.add(new NewsItem(title, url));
                                 System.out.println("-> Added (s2): " + title);
                             }
                             if (items.size() >= 15) break;
                         }
                    }
                } catch (Exception e) {
                    System.err.println("Error fetching/parsing news: " + e.getMessage());
                    e.printStackTrace();
                }
                return items;
            }
            @Override
            protected void done() {
                try {
                    List<NewsItem> result = get();
                    listModel.clear();
                    if (result.isEmpty()) {
                        listModel.addElement(new NewsItem("Không lấy được tin. Kiểm tra kết nối hoặc cấu trúc web đã thay đổi.", ""));
                    } else {
                        for (NewsItem ni : result) {
                            listModel.addElement(ni);
                        }
                    }
                } catch (Exception e) {
                    listModel.clear();
                    listModel.addElement(new NewsItem("Lỗi hiển thị tin tức: " + e.getMessage(), ""));
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }
    private void openInBrowser(String url) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
            } else {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể mở liên kết: " + e.getMessage());
        }
    }
    private static class NewsItem {
        final String title;
        final String url;
        NewsItem(String t, String u) { title = t; url = u; }
        @Override public String toString() { return "<html><p style='padding-bottom: 5px;'>" + title + "</p></html>"; }
    }

    public void setGreeting(String fullNameOrLastName) {
        SwingUtilities.invokeLater(() -> {
            String last = extractLastName(fullNameOrLastName);
            String period = greetingsByTime();
            greetingLabel.setText(String.format("Chào %s %s!", period, last.isEmpty() ? "" : last));
            greetingLabel.revalidate();
            greetingLabel.repaint();
        });
    }
    private String extractLastName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return "";
        String[] p = fullName.trim().split("\\s+");
        return p[p.length - 1];
    }
    private String greetingsByTime() {
        int h = LocalTime.now().getHour();
        if (h >= 5 && h < 11) return "buổi sáng";
        if (h >= 11 && h < 13) return "buổi trưa";
        if (h >= 13 && h < 18) return "buổi chiều";
        return "buổi tối";
    }
    
    private void openNotificationCenter() {
        JFrame notificationFrame = new JFrame("Hòm thư - " + currentUser.getFullName());
        notificationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        notificationFrame.setSize(1200, 700); 
        notificationFrame.setLocationRelativeTo(this); 
        NotificationCenterPanel notificationPanel = new NotificationCenterPanel(notificationFrame, currentUser);
        notificationFrame.setContentPane(notificationPanel);
        notificationFrame.setVisible(true);
        HomeFrame.this.dispose();
    }
}