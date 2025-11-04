package com.mycompany.ehr.dtc.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.YearMonth;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.mindrot.jbcrypt.BCrypt;

import com.mycompany.ehr.dtc.dao.FamilyMembersDAO;
import com.mycompany.ehr.dtc.dao.UserDAO;
import com.mycompany.ehr.dtc.model.FamilyMembers;
import com.mycompany.ehr.dtc.model.User;
import com.mycompany.ehr.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;


public class RegisterFrame extends JFrame {
    private final JTextField txtCCCD = new JTextField(20);
    private final JTextField txtFullName = new JTextField(20);
    private final JComboBox<String> yearBox;
    private final JComboBox<String> monthBox;
    private final JComboBox<String> dayBox;
    private final JTextField txtPhone = new JTextField(20);
    private final JPasswordField txtPassword = new JPasswordField(20);
    private final JPasswordField txtConfirm = new JPasswordField(20);
    private final JButton btnSubmit = new JButton("Đăng ký");
    private final JButton btnCancel = new JButton("Hủy");
    private final JComboBox<FamilyMembers.Gender> genderBox;
    private final JComboBox<FamilyMembers.BloodType> bloodTypeBox;
    private final JTextField txtInsurance = new JTextField(20);
    private final JLabel pwErrorLabel = new JLabel(" ");
    private static final Pattern PASSWORD_RULE = Pattern.compile("(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{6,}");
    private static final Pattern PHONE_RULE = Pattern.compile("^\\d{9,15}$");
    private final JLabel greetingLabel = new JLabel("Xin chào !", JLabel.CENTER);

    public RegisterFrame() {
        super("EHR - Đăng ký");
        int currentYear = LocalDate.now().getYear();
        int maxYear = currentYear - 16;
        int minYear = maxYear - 100;
        int size = maxYear - minYear + 1;
        String[] years = new String[size];
        for (int i = 0; i < size; i++) {
            years[i] = String.valueOf(maxYear - i);
        }
        yearBox = new JComboBox<>(years);
        String[] months = new String[12];
        for (int i = 0; i < 12; i++) {
            months[i] = String.valueOf(i + 1);
        }
        monthBox = new JComboBox<>(months);
        dayBox = new JComboBox<>();
        genderBox = new JComboBox<>(FamilyMembers.Gender.values());
        genderBox.setSelectedItem(FamilyMembers.Gender.KHAC);
        genderBox.setPreferredSize(new Dimension(100, 25));
        bloodTypeBox = new JComboBox<>(FamilyMembers.BloodType.values());
        bloodTypeBox.setSelectedItem(FamilyMembers.BloodType.UNKNOWN);
        bloodTypeBox.setPreferredSize(new Dimension(100, 25));
        ItemListener dmListener = e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                updateDayCombo();
            }
        };
        yearBox.addItemListener(dmListener);
        monthBox.addItemListener(dmListener);
        yearBox.setSelectedIndex(0);
        monthBox.setSelectedIndex(0);
        updateDayCombo();
        Dimension comboSize = new Dimension(60, 25);
        yearBox.setPreferredSize(new Dimension(80, 25));
        monthBox.setPreferredSize(comboSize);
        dayBox.setPreferredSize(comboSize);
        initUI();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }
    
    private void initUI() {
        pwErrorLabel.setForeground(Color.RED);
        pwErrorLabel.setPreferredSize(new Dimension(360, 18));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("CCCD:"), gbc);
        gbc.gridx = 1;
        panel.add(txtCCCD, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Họ và tên:"), gbc);
        gbc.gridx = 1;
        panel.add(txtFullName, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Ngày sinh:"), gbc);
        JPanel datePanel = new JPanel();
        datePanel.add(new JLabel("Năm:"));
        datePanel.add(yearBox);
        datePanel.add(Box.createHorizontalStrut(10));
        datePanel.add(new JLabel("Tháng:"));
        datePanel.add(monthBox);
        datePanel.add(Box.createHorizontalStrut(10));
        datePanel.add(new JLabel("Ngày:"));
        datePanel.add(dayBox);
        gbc.gridx = 1;
        panel.add(datePanel, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Giới tính:"), gbc);
        gbc.gridx = 1;
        panel.add(genderBox, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Nhóm máu:"), gbc);
        gbc.gridx = 1;
        panel.add(bloodTypeBox, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Số BHYT:"), gbc);
        gbc.gridx = 1;
        panel.add(txtInsurance, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1;
        panel.add(txtPhone, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);
        row++;
        gbc.gridx = 1; gbc.gridy = row;
        JLabel pwdHint = new JLabel("Mật khẩu ≥6 ký tự, chứa chữ hoa, chữ thường, số và ký tự đặc biệt.");
        pwdHint.setForeground(Color.DARK_GRAY);
        Font f = pwdHint.getFont();
        pwdHint.setFont(f.deriveFont(f.getSize2D() - 2f));
        panel.add(pwdHint, gbc);
        row++;
        gbc.gridx = 1; gbc.gridy = row;
        JCheckBox chkShowPassword = new JCheckBox("Hiện mật khẩu");
        final char echoPw = txtPassword.getEchoChar();
        chkShowPassword.addActionListener(ev -> txtPassword.setEchoChar(chkShowPassword.isSelected() ? (char)0 : echoPw));
        panel.add(chkShowPassword, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Xác nhận mật khẩu:"), gbc);
        gbc.gridx = 1;
        panel.add(txtConfirm, gbc);
        row++;
        gbc.gridx = 1; gbc.gridy = row;
        JCheckBox chkShowConfirm = new JCheckBox("Hiện mật khẩu");
        final char echoConf = txtConfirm.getEchoChar();
        chkShowConfirm.addActionListener(ev -> txtConfirm.setEchoChar(chkShowConfirm.isSelected() ? (char)0 : echoConf));
        panel.add(chkShowConfirm, gbc);
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JPanel btns = new JPanel();
        btns.add(btnSubmit);
        btns.add(btnCancel);
        panel.add(btns, gbc);
        add(panel);
        txtPassword.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String pw = new String(txtPassword.getPassword());
                if (!pw.isEmpty() && !PASSWORD_RULE.matcher(pw).matches()) {
                    pwErrorLabel.setText("Mật khẩu chưa đạt yêu cầu.");
                    txtPassword.setText("");
                    txtConfirm.setText("");
                    SwingUtilities.invokeLater(() -> txtPassword.requestFocusInWindow());
                    JOptionPane.showMessageDialog(RegisterFrame.this,
                            "Mật khẩu phải có ít nhất 6 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt.\nVui lòng nhập lại.",
                            "Mật khẩu không hợp lệ", JOptionPane.WARNING_MESSAGE);
                } else {
                    pwErrorLabel.setText(" ");
                }
            }
        });
        txtConfirm.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String pw = new String(txtPassword.getPassword());
                String conf = new String(txtConfirm.getPassword());
                if (!conf.isEmpty() && !conf.equals(pw)) {
                    txtConfirm.setText("");
                    SwingUtilities.invokeLater(() -> txtConfirm.requestFocusInWindow());
                    JOptionPane.showMessageDialog(RegisterFrame.this,
                            "Xác nhận mật khẩu không trùng. Vui lòng nhập lại.", "Lỗi xác nhận", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        btnSubmit.addActionListener(this::onSubmit);
        btnCancel.addActionListener(e -> dispose());
    }

    private String extractLastName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return "";
        String[] parts = fullName.trim().split("\\s+");
        return parts[parts.length - 1];
    }

    private String greetingsByTime() {
        LocalTime t = LocalTime.now();
        int h = t.getHour();
        if (h >= 5 && h < 11) return "buổi sáng";
        if (h >= 11 && h < 13) return "buổi trưa";
        if (h >= 13 && h < 18) return "buổi chiều";
        return "buổi tối";
    }

    private void updateDayCombo() {
        int year = Integer.parseInt((String) yearBox.getSelectedItem());
        int month = Integer.parseInt((String) monthBox.getSelectedItem());
        YearMonth yearMonth = YearMonth.of(year, month);
        int maxDays = yearMonth.lengthOfMonth();
        String[] days = new String[maxDays];
        for (int i = 0; i < maxDays; i++) {
            days[i] = String.valueOf(i + 1);
        }
        dayBox.setModel(new JComboBox<>(days).getModel());
    }
    
    private void onSubmit(ActionEvent e) {
        final String cccd = txtCCCD.getText().trim();
        final String fullName = txtFullName.getText().trim();
        final LocalDate dob;
        try {
            int year = Integer.parseInt((String) yearBox.getSelectedItem());
            int month = Integer.parseInt((String) monthBox.getSelectedItem());
            int day = Integer.parseInt((String) dayBox.getSelectedItem());
            dob = LocalDate.of(year, month, day);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ngày sinh không hợp lệ.", "Lỗi ngày sinh", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int yearsOld = Period.between(dob, LocalDate.now()).getYears();
        if (yearsOld <= 16) {
            JOptionPane.showMessageDialog(this, "Yêu cầu trên 16 tuổi để đăng ký.", "Yêu cầu tuổi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        final String phone = txtPhone.getText().trim();
        final String password = new String(txtPassword.getPassword());
        final String confirm = new String(txtConfirm.getPassword());
        final FamilyMembers.Gender gender = (FamilyMembers.Gender) genderBox.getSelectedItem();
        final FamilyMembers.BloodType bloodType = (FamilyMembers.BloodType) bloodTypeBox.getSelectedItem();
        final String insurance = txtInsurance.getText().trim();
        if (cccd.isEmpty() || fullName.isEmpty() || phone.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền tất cả các trường bắt buộc (có *).", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!PHONE_RULE.matcher(phone).matches()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ. Chỉ gồm chữ số, độ dài 9-15.", "Lỗi số điện thoại", JOptionPane.WARNING_MESSAGE);
            txtPhone.requestFocusInWindow();
            return;
        }
        if (!PASSWORD_RULE.matcher(password).matches()) {
            JOptionPane.showMessageDialog(this, "Mật khẩu chưa đạt yêu cầu. Vui lòng nhập lại.", "Mật khẩu yếu", JOptionPane.WARNING_MESSAGE);
            txtPassword.setText("");
            txtConfirm.setText("");
            txtPassword.requestFocusInWindow();
            return;
        }
        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Xác nhận mật khẩu không khớp. Vui lòng nhập lại.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            txtConfirm.setText("");
            txtConfirm.requestFocusInWindow();
            return;
        }
    
        btnSubmit.setEnabled(false);

        new SwingWorker<Boolean, Void>() {
            private String errorMsg = null;

            @Override
            protected Boolean doInBackground() {
                Connection conn = null; 
                try {
                    conn = JDBCUtil.getConnection();
                    conn.setAutoCommit(false);

                    String hashed = BCrypt.hashpw(cccd + password, BCrypt.gensalt());
                    
                    String userSql = "INSERT INTO Users (username, password_hash, phone, full_name, is_active, created_at) VALUES (?, ?, ?, ?, ?, NOW())";
                    int newUserId = 0;
                    
                    try (PreparedStatement psUser = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                        psUser.setString(1, cccd);
                        psUser.setString(2, hashed); 
                        psUser.setString(3, phone);
                        psUser.setString(4, fullName);
                        psUser.setBoolean(5, true);
                        
                        int affected = psUser.executeUpdate();
                        if (affected == 0) {
                            throw new SQLException("Tạo người dùng thất bại, không có dòng nào được thêm.");
                        }

                        try (ResultSet rs = psUser.getGeneratedKeys()) {
                            if (rs.next()) {
                                newUserId = rs.getInt(1);
                            } else {
                                throw new SQLException("Tạo người dùng thất bại, không lấy được ID.");
                            }
                        }
                    }

                    FamilyMembers selfProfile = new FamilyMembers.Builder(newUserId, fullName, dob)
                            .gender(gender)
                            .bloodType(bloodType)
                            .insuranceNumber(insurance.isEmpty() ? null : insurance)
                            .relationship(FamilyMembers.Relationship.BAN_THAN) 
                            .phone(phone)
                            .build();
                    
                    String profileSql = "INSERT INTO family_members (user_id, name, dob, gender, relationship, blood_type, insurance_number, phone, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement psProfile = conn.prepareStatement(profileSql)) {
                        psProfile.setInt(1, selfProfile.getUserId());
                        psProfile.setString(2, selfProfile.getName());
                        psProfile.setDate(3, java.sql.Date.valueOf(selfProfile.getDob()));
                        psProfile.setString(4, selfProfile.getGender().getDisplayName());
                        psProfile.setString(5, selfProfile.getRelationship().getDisplayName()); 
                        psProfile.setString(6, selfProfile.getBloodType().getDisplayName());
                        psProfile.setString(7, selfProfile.getInsuranceNumber());
                        psProfile.setString(8, selfProfile.getPhone());
                        psProfile.setTimestamp(9, Timestamp.valueOf(selfProfile.getCreatedAt()));
                        psProfile.setTimestamp(10, Timestamp.valueOf(selfProfile.getUpdatedAt()));

                        int profileAffected = psProfile.executeUpdate();
                        if (profileAffected == 0) {
                            throw new SQLException("Tạo hồ sơ người dùng thất bại.");
                        }
                    }
                    
                    conn.commit();
                    return true;

                } catch (SQLException ex) {
                    if (conn != null) {
                        try {
                            conn.rollback();
                        } catch (SQLException e) {
                            e.printStackTrace(); 
                        }
                    }
                    if (ex.getMessage().contains("Duplicate entry") && ex.getMessage().contains("username")) {
                         errorMsg = "username_exists";
                    } else if (ex.getMessage().contains("Duplicate entry") && ex.getMessage().contains("phone")) {
                         errorMsg = "phone_exists";
                    } else {
                        errorMsg = ex.getMessage();
                    }
                    ex.printStackTrace();
                    return false;
                } finally {
                    if (conn != null) {
                        try {
                            conn.setAutoCommit(true); 
                            conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            protected void done() {
                btnSubmit.setEnabled(true);
                boolean ok = false;
                try {
                    ok = get();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (ok) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "Đăng ký thành công.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    String msg;
                    if ("username_exists".equals(errorMsg)) {
                        msg = "Đăng ký thất bại: CCCD (Tên đăng nhập) đã tồn tại.";
                    } else if ("phone_exists".equals(errorMsg)) {
                        msg = "Đăng ký thất bại: Số điện thoại đã tồn tại.";
                    } else if (errorMsg != null) {
                        msg = "Đăng ký thất bại (DB): " + errorMsg;
                    } else {
                        msg = "Đăng ký thất bại. Vui lòng thử lại.";
                    }
                    JOptionPane.showMessageDialog(RegisterFrame.this, msg, "Lỗi đăng ký", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
}