package com.mycompany.ehr.dtc.ui;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import com.mycompany.ehr.dtc.model.Allergies;

public class AllergyDetailDialog extends JDialog {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy"); 

    public AllergyDetailDialog(Frame owner, Allergies allergy) {
        super(owner, "Chi tiết Dị ứng", true); 

        setLayout(new BorderLayout(10, 10));
        setSize(450, 400); 
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel detailPanel = new JPanel(new GridBagLayout());
        detailPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        Font boldFont = UIManager.getFont("Label.font").deriveFont(Font.BOLD);

        gbc.gridx = 0; gbc.gridy = row; detailPanel.add(createBoldLabel("Tác nhân:"), gbc);
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 1.0;
        JTextField allergenField = createReadOnlyTextField(allergy.getAllergen());
        allergenField.setFont(boldFont);
        detailPanel.add(allergenField, gbc);
        gbc.weightx = 0.0;

        row++;
        gbc.gridx = 0; gbc.gridy = row; detailPanel.add(new JLabel("Loại dị ứng:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        detailPanel.add(createReadOnlyTextField(allergy.getAllergyType().getDisplayName()), gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; detailPanel.add(createBoldLabel("Mức độ:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        JTextField severityField = createReadOnlyTextField(allergy.getSeverity().getDisplayName());
        severityField.setFont(boldFont);
        Color severityColor = Color.WHITE;
         if (allergy.getSeverity() == Allergies.AllergySeverity.NGUY_HIEM) {
             severityColor = new Color(255, 100, 100);
         } else if (allergy.getSeverity() == Allergies.AllergySeverity.NANG) {
              severityColor = new Color(255, 182, 193);
         } else if (allergy.getSeverity() == Allergies.AllergySeverity.TRUNG_BINH) {
             severityColor = new Color(255, 223, 186);
         }
        severityField.setBackground(severityColor);
        detailPanel.add(severityField, gbc);


        row++;
        gbc.gridx = 0; gbc.gridy = row; detailPanel.add(new JLabel("Ngày phát hiện:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        String dateStr = allergy.getDiscoveredDate() != null ? allergy.getDiscoveredDate().format(DATE_FORMATTER) : "N/A";
        detailPanel.add(createReadOnlyTextField(dateStr), gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.NORTHWEST; detailPanel.add(new JLabel("Triệu chứng:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 0.5;
        JTextArea symptomsArea = createReadOnlyTextArea(allergy.getSymptoms(), 4);
        detailPanel.add(new JScrollPane(symptomsArea), gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weighty = 0.0;

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.anchor = GridBagConstraints.NORTHWEST; detailPanel.add(new JLabel("Ghi chú:"), gbc);
        gbc.gridx = 1; gbc.gridy = row;
        gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 0.5;
        JTextArea notesArea = createReadOnlyTextArea(allergy.getNotes(), 4);
        detailPanel.add(new JScrollPane(notesArea), gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weighty = 0.0;


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

    private JTextArea createReadOnlyTextArea(String text, int rows) {
         JTextArea textArea = new JTextArea(text != null ? text : "", rows, 20);
         textArea.setEditable(false);
         textArea.setLineWrap(true);
         textArea.setWrapStyleWord(true);
         textArea.setBackground(UIManager.getColor("Label.background"));
         textArea.setFont(UIManager.getFont("Label.font"));
         textArea.setBorder(BorderFactory.createEtchedBorder());
         return textArea;
    }

    private JLabel createBoldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        return label;
    }
}