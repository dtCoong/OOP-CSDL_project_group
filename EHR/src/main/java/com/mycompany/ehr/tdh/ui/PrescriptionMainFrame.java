package com.mycompany.ehr.tdh.ui;

import javax.swing.*;
import java.awt.*;
import com.mycompany.ehr.dtc.model.User;
import com.mycompany.ehr.dtc.model.FamilyMembers;
import com.mycompany.ehr.dtc.ui.MemberDetailsFrame;

public class PrescriptionMainFrame extends JFrame {

    private User currentUser;
    private FamilyMembers selectedMember;

    private JTabbedPane tabbedPane;

    public PrescriptionMainFrame(User user, FamilyMembers member) {
        this.currentUser = user;
        this.selectedMember = member;

        setTitle("Quản lý Thuốc & Đơn thuốc - " + selectedMember.getName());
        setSize(1000, 700); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();

        PrescriptionPanel prescriptionPanel = new PrescriptionPanel(currentUser, selectedMember);
        tabbedPane.addTab("Đơn thuốc (của " + selectedMember.getName() + ")", prescriptionPanel);

        // --- Tab 2: Tra cứu thuốc ---
        MedicationPanel medicationPanel = new MedicationPanel();
        tabbedPane.addTab("Tra cứu Thuốc (Danh mục chung)", medicationPanel);

        add(tabbedPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnBack = new JButton("Quay lại Hồ sơ sức khỏe");
        bottomPanel.add(btnBack);
        add(bottomPanel, BorderLayout.SOUTH);

        btnBack.addActionListener(e -> {
            MemberDetailsFrame detailsFrame = new MemberDetailsFrame(currentUser, selectedMember);
            detailsFrame.setVisible(true);
            this.dispose();
        });
    }
}