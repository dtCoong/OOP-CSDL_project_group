package com.mycompany.ehr;

import com.mycompany.ehr.ui.MedicationPanel;
import com.mycompany.ehr.ui.PrescriptionPanel;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;

public class EHR {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Hệ thống Quản lý Sức khỏe (EHR)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 700);
            frame.setLocationRelativeTo(null); 
            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.addTab("Quản lý Thuốc", new MedicationPanel());
            tabbedPane.addTab("Đơn thuốc & Chi tiết", new PrescriptionPanel());
            frame.add(tabbedPane, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}