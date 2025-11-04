package com.mycompany.ehr.dtc.ui;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import com.mycompany.ehr.dtc.model.MedicalHistory;

public class MedicalHistoryDetailDialog extends JDialog {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public MedicalHistoryDetailDialog(Frame owner, MedicalHistory history) {
        super(owner, "Chi tiết Lịch sử Bệnh án", true);

        setLayout(new BorderLayout(10, 10));
        setSize(500, 450); 
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel detailPanel = new JPanel(new GridBagLayout());
        detailPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        Font boldFont = UIManager.getFont("Label.font").deriveFont(Font.BOLD);

        gbc.gridx = 0; gbc.gridy = row; detailPanel.add(createBoldLabel("Tên bệnh/Tình trạng:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 1.0;
        JTextField conditionField = createReadOnlyTextField(history.getConditionName());
        conditionField.setFont(boldFont);
        detailPanel.add(conditionField, gbc);
        gbc.weightx = 0.0;

        row++;
        gbc.gridx = 0; gbc.gridy = row; detailPanel.add(new JLabel("Ngày chẩn đoán:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        String dateStr = history.getDiagnosisDate() != null ? history.getDiagnosisDate().format(DATE_FORMATTER) : "N/A";
        detailPanel.add(createReadOnlyTextField(dateStr), gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; detailPanel.add(new JLabel("Đặc tính:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        detailPanel.add(createReadOnlyTextField(history.isChronic() ? "Bệnh mãn tính" : "Không phải bệnh mãn tính"), gbc);


        row++;
        gbc.gridx = 0; gbc.gridy = row; detailPanel.add(createBoldLabel("Mức độ:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        JTextField severityField = createReadOnlyTextField(history.getSeverity().getDisplayName());
        severityField.setFont(boldFont); 
        detailPanel.add(severityField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; detailPanel.add(createBoldLabel("Tình trạng:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        JTextField statusField = createReadOnlyTextField(history.getStatus().getDisplayName());
        statusField.setFont(boldFont); 
        detailPanel.add(statusField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; detailPanel.add(new JLabel("Bệnh viện:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        detailPanel.add(createReadOnlyTextField(history.getHospitalName() != null ? history.getHospitalName() : "N/A"), gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; detailPanel.add(new JLabel("Địa chỉ BV:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        detailPanel.add(createReadOnlyTextField(history.getHospitalAddress() != null ? history.getHospitalAddress() : "N/A"), gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; detailPanel.add(new JLabel("Ngày vào viện:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        String admissionDateStr = history.getHospitalAdmissionDate() != null ? history.getHospitalAdmissionDate().format(DATE_FORMATTER) : "N/A";
        detailPanel.add(createReadOnlyTextField(admissionDateStr), gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; detailPanel.add(new JLabel("Ngày ra viện:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        String dischargeDateStr = history.getHospitalDischargeDate() != null ? history.getHospitalDischargeDate().format(DATE_FORMATTER) : "N/A";
        detailPanel.add(createReadOnlyTextField(dischargeDateStr), gbc);


        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.NORTHWEST; detailPanel.add(new JLabel("Ghi chú:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        JTextArea notesArea = new JTextArea(history.getNotes() != null ? history.getNotes() : "", 5, 20); // Handle null notes
        notesArea.setEditable(false);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setBackground(UIManager.getColor("Label.background"));
        notesArea.setFont(UIManager.getFont("Label.font"));
        notesArea.setBorder(BorderFactory.createEtchedBorder());
        detailPanel.add(new JScrollPane(notesArea), gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0.0;

        add(detailPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton closeButton = new JButton("Đóng");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JTextField createReadOnlyTextField(String text) {
        JTextField textField = new JTextField(text != null ? text : "");
        textField.setEditable(false);
        textField.setBorder(null);
        textField.setBackground(UIManager.getColor("Label.background"));
        return textField;
    }
    
    private JLabel createBoldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        return label;
    }
}