package com.mycompany.ehr.dtc.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.JCheckBox;

import com.mycompany.ehr.dtc.dao.UserDAO;
import com.mycompany.ehr.dtc.model.User;

public class LoginFrame extends JFrame {
    private final JTextField txtUsername = new JTextField(20);
    private final JPasswordField txtPassword = new JPasswordField(20);
    private final JButton btnLogin = new JButton("Đăng nhập");

    private static final String USER_PLACEHOLDER = "CCCD của bạn";

    public LoginFrame() {
        super("EHR - Đăng nhập");
        initUI();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void initUI() {
        JPanel main = new JPanel(new BorderLayout(10,10));
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 1;
        txtUsername.setText(USER_PLACEHOLDER);
        txtUsername.setForeground(Color.GRAY);
        txtUsername.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (USER_PLACEHOLDER.equals(txtUsername.getText())) {
                    txtUsername.setText("");
                    txtUsername.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (txtUsername.getText().trim().isEmpty()) {
                    txtUsername.setText(USER_PLACEHOLDER);
                    txtUsername.setForeground(Color.GRAY);
                }
            }
        });
        form.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1;
        form.add(txtPassword, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        JCheckBox chkShow = new JCheckBox("Hiện mật khẩu");
        final char pwdEcho = txtPassword.getEchoChar();
        chkShow.addActionListener(ev -> txtPassword.setEchoChar(chkShow.isSelected() ? (char)0 : pwdEcho));
        form.add(chkShow, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        JLabel forgotLabel = new JLabel("<html><u>Quên mật khẩu?</u></html>");
        forgotLabel.setForeground(Color.BLUE);
        forgotLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(LoginFrame.this,
                        "Vui lòng liên hệ quản trị để đặt lại mật khẩu.", "Quên mật khẩu", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        form.add(forgotLabel, gbc);

        main.add(form, BorderLayout.CENTER);

        JPanel buttonsBox = new JPanel();
        buttonsBox.add(btnLogin);
        main.add(buttonsBox, BorderLayout.SOUTH);

        JLabel registerLabel = new JLabel("<html>Chưa có tài khoản? <u><font color='blue'>Đăng ký</font></u></html>", SwingConstants.CENTER);
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    new RegisterFrame().setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(LoginFrame.this, "Không thể mở màn hình đăng ký: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel southContainer = new JPanel(new BorderLayout());
        southContainer.add(buttonsBox, BorderLayout.CENTER);
        southContainer.add(registerLabel, BorderLayout.SOUTH);
        main.add(southContainer, BorderLayout.SOUTH);

        add(main);

        btnLogin.addActionListener(this::onLogin);
        getRootPane().setDefaultButton(btnLogin);
    }

    private void onLogin(ActionEvent e) {
        final String username = (txtUsername.getForeground() == Color.GRAY || USER_PLACEHOLDER.equals(txtUsername.getText()))
                ? ""
                : txtUsername.getText().trim();
        final String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập và mật khẩu.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnLogin.setEnabled(false);

        new SwingWorker<Boolean, Void>() {
            private String errorMsg = null;
            @Override
            protected Boolean doInBackground() {
                try {
                    return UserDAO.getInstance().login(username, password);
                } catch (Exception ex) {
                    errorMsg = ex.getMessage();
                    ex.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void done() {
                btnLogin.setEnabled(true);
                boolean ok = false;
                try {
                    ok = get();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (ok) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Đăng nhập thành công.", "Thành công", JOptionPane.INFORMATION_MESSAGE);

                    User user = UserDAO.getInstance().findByUsername(username);
                    
                    if (user == null) {
                        JOptionPane.showMessageDialog(LoginFrame.this, "Không thể tải thông tin người dùng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    SwingUtilities.invokeLater(() -> {
                        HomeFrame home = new HomeFrame(user);
                        home.setVisible(true);
                    });
                    LoginFrame.this.dispose();
                } else {
                    String msg = (errorMsg != null) ? ("Lỗi: " + errorMsg) : "Tên đăng nhập hoặc mật khẩu không đúng.";
                    JOptionPane.showMessageDialog(LoginFrame.this, msg, "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
                    txtPassword.setText("");
                    txtPassword.requestFocusInWindow();
                }
            }
        }.execute();
    }

    private String lastToken(String fullName) {
        if (fullName == null) return "";
        String s = fullName.trim();
        if (s.isEmpty()) return "";
        String[] parts = s.split("\\s+");
        return parts[parts.length - 1];
    }
    
}