package com.mycompany.ehr.dtc.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.mycompany.ehr.dtc.model.FamilyMembers;
import com.mycompany.ehr.dtc.model.User;

// Import các Frame khác trong 'dtc'
import com.mycompany.ehr.dtc.ui.FamilyMembersFrame;
import com.mycompany.ehr.dtc.ui.MedicalHistoryFrame;
import com.mycompany.ehr.dtc.ui.AllergiesFrame;

// Import các lớp 'bnta'
import com.mycompany.ehr.bnta.dao.VaccinationRecordsDao;
import com.mycompany.ehr.bnta.dao.VaccinationRecordsDaoImpl;
import com.mycompany.ehr.bnta.dao.VaccineTemplatesDao;
import com.mycompany.ehr.bnta.dao.VaccineTemplatesDaoImpl;
import com.mycompany.ehr.bnta.ui.MainApplicationFrame;

// Import các lớp 'tdh'
import com.mycompany.ehr.tdh.ui.PrescriptionMainFrame;

// THÊM MỚI: Import các lớp Tài liệu (lqt.ui)
import com.mycompany.ehr.lqt.ui.DocumentsPanelVN;

public class MemberDetailsFrame extends JFrame {

    private User currentUser;
    private FamilyMembers selectedMember;

    public MemberDetailsFrame(User user, FamilyMembers member) {
        this.currentUser = user;
        this.selectedMember = member;

        setTitle("Hồ sơ sức khỏe - " + selectedMember.getName());
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        JLabel nameLabel = new JLabel("Thành viên: " + selectedMember.getName(), SwingConstants.CENTER);
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 18f));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(nameLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnMedicalHistory = createFeatureButton("Lịch sử bệnh án"); 
        JButton btnAllergies = createFeatureButton("Dị ứng");
        JButton btnPrescriptions = createFeatureButton("Đơn thuốc");
        JButton btnVaccinations = createFeatureButton("Tiêm chủng");
        JButton btnDocuments = createFeatureButton("Tài liệu"); // Nút này

        buttonPanel.add(btnMedicalHistory);
        buttonPanel.add(btnAllergies);
        buttonPanel.add(btnPrescriptions);
        buttonPanel.add(btnVaccinations);
        buttonPanel.add(btnDocuments); // Nút này

        add(buttonPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnBack = new JButton("Quay lại danh sách");
        bottomPanel.add(btnBack);
        add(bottomPanel, BorderLayout.SOUTH);

        btnBack.addActionListener(e -> {
            FamilyMembersFrame membersFrame = new FamilyMembersFrame(currentUser);
            membersFrame.setVisible(true);
            MemberDetailsFrame.this.dispose();
        });

        btnMedicalHistory.addActionListener(e -> {
            MedicalHistoryFrame historyFrame = new MedicalHistoryFrame(currentUser, selectedMember);
            historyFrame.setVisible(true);
            MemberDetailsFrame.this.dispose();
        });

        btnAllergies.addActionListener(e -> {
            AllergiesFrame allergiesFrame = new AllergiesFrame(currentUser, selectedMember);
            allergiesFrame.setVisible(true);
            MemberDetailsFrame.this.dispose();
        });

        btnPrescriptions.addActionListener(e -> {
            // Giả sử PrescriptionMainFrame đã được sửa tương tự
            PrescriptionMainFrame pFrame = new PrescriptionMainFrame(currentUser, selectedMember);
            pFrame.setVisible(true);
            MemberDetailsFrame.this.dispose();
        });
        
        btnVaccinations.addActionListener(e -> {
            try {
                VaccineTemplatesDao templateDao = new VaccineTemplatesDaoImpl();
                VaccinationRecordsDao recordDao = new VaccinationRecordsDaoImpl();
                // Sửa: Truyền User và Member vào
                MainApplicationFrame vaxFrame = new MainApplicationFrame(templateDao, recordDao, currentUser, selectedMember);
                vaxFrame.setVisible(true);
                MemberDetailsFrame.this.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(MemberDetailsFrame.this,
                    "Không thể mở mô-đun tiêm chủng: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // SỬA: Cập nhật sự kiện cho nút "Tài liệu"
        btnDocuments.addActionListener(e -> {
            JFrame docFrame = new JFrame("Quản lý Tài liệu - " + selectedMember.getName());
            docFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            docFrame.setSize(1400, 850); 
            docFrame.setLocationRelativeTo(this);
            
            // Sửa: Gọi constructor mới và truyền User, Member
            DocumentsPanelVN docPanel = new DocumentsPanelVN(docFrame, currentUser, selectedMember);
            
            docFrame.setContentPane(docPanel);
            docFrame.setVisible(true);
            
            MemberDetailsFrame.this.dispose();
        });
    }

    private JButton createFeatureButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 50));
        return button;
    }

    private void showPlaceholder(String featureName) {
        JOptionPane.showMessageDialog(this,
                "Tính năng '" + featureName + "' chưa được triển khai.",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
    }
}