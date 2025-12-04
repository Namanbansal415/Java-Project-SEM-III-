
package com.onlinejobportal.ui;

import com.onlinejobportal.dao.UserDAO;
import com.onlinejobportal.model.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole;

    public LoginFrame() {
        setTitle("Online Job Portal - Login");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {

        // MAIN BACKGROUND PANEL
        JPanel background = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(51, 105, 232),
                        0, getHeight(), new Color(25, 118, 210)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        background.setLayout(new GridBagLayout()); // CENTER align everything

        // ---------- LOGIN PANEL (The actual box) ----------
        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(380, 340));
        panel.setBackground(new Color(255, 255, 255, 40)); // transparent-ish panel
        panel.setOpaque(true);
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        // TITLE
        JLabel lblTitle = new JLabel("Login to Job Portal");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(90, 20, 300, 40);
        panel.add(lblTitle);

        // ROLE LABEL
        JLabel lblRole = new JLabel("Login As:");
        lblRole.setBounds(50, 90, 120, 25);
        lblRole.setForeground(Color.WHITE);
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(lblRole);

        // ROLE DROPDOWN
        cmbRole = new JComboBox<>(new String[]{"candidate", "employer", "admin"});
        cmbRole.setBounds(150, 90, 180, 30);
        panel.add(cmbRole);

        // USERNAME LABEL
        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(50, 140, 120, 25);
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(lblUser);

        // USERNAME FIELD
        txtUsername = new JTextField();
        txtUsername.setBounds(150, 140, 180, 30);
        panel.add(txtUsername);

        // PASSWORD LABEL
        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(50, 190, 120, 25);
        lblPass.setForeground(Color.WHITE);
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        panel.add(lblPass);

        // PASSWORD FIELD
        txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 190, 180, 30);
        panel.add(txtPassword);

        // LOGIN BUTTON
        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(120, 240, 140, 35);
        btnLogin.setBackground(new Color(0, 153, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setFocusPainted(false);
        panel.add(btnLogin);

        // SIGNUP BUTTON
        JButton btnSignup = new JButton("Signup");
        btnSignup.setBounds(120, 285, 140, 35);
        btnSignup.setBackground(Color.WHITE);
        btnSignup.setForeground(new Color(30, 60, 90));
        btnSignup.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnSignup.setFocusPainted(false);
        btnSignup.setBorder(BorderFactory.createLineBorder(new Color(30, 60, 90), 2));
        panel.add(btnSignup);

        // ADD panel to background CENTER
        background.add(panel);

        // ADD background to frame
        add(background);

        // BUTTON ACTIONS
        btnLogin.addActionListener(e -> loginAction());
        btnSignup.addActionListener(e -> {
            new SignupFrame().setVisible(true);
            dispose();
        });
    }

    private void loginAction() {
        String role = cmbRole.getSelectedItem().toString().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        UserDAO dao = new UserDAO();
        User user = dao.authenticate(username, password);

        if (user == null) {
            JOptionPane.showMessageDialog(this, "Invalid username or password!");
            return;
        }

        if (!user.getRole().equalsIgnoreCase(role)) {
            JOptionPane.showMessageDialog(this,
                    "Wrong Role Selected!\nYour actual role: " + user.getRole());
            return;
        }

        JOptionPane.showMessageDialog(this, "Login Successful!");

        switch (role) {
            case "admin":
                new AdminDashboardFrame().setVisible(true);
                break;
            case "employer":
                new EmployerDashboardFrame(user).setVisible(true);
                break;
            case "candidate":
                new JobPortalHomeFrame(user).setVisible(true);
                break;
        }

        dispose();
    }

    public static void launch() {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
