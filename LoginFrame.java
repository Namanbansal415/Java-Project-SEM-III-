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
        setSize(400, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel lblTitle = new JLabel("Login");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setBounds(160, 20, 200, 30);
        panel.add(lblTitle);

        JLabel lblRole = new JLabel("Login As:");
        lblRole.setBounds(50, 70, 100, 25);
        panel.add(lblRole);

        cmbRole = new JComboBox<>(new String[]{"candidate", "employer", "admin"});
        cmbRole.setBounds(150, 70, 180, 25);
        panel.add(cmbRole);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(50, 120, 100, 25);
        panel.add(lblUser);

        txtUsername = new JTextField();
        txtUsername.setBounds(150, 120, 180, 25);
        panel.add(txtUsername);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(50, 160, 100, 25);
        panel.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 160, 180, 25);
        panel.add(txtPassword);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(150, 210, 120, 30);
        panel.add(btnLogin);

        JButton btnSignup = new JButton("Signup");
        btnSignup.setBounds(150, 250, 120, 30);
        panel.add(btnSignup);

        btnLogin.addActionListener(e -> loginAction());
        btnSignup.addActionListener(e -> {
            new SignupFrame().setVisible(true);
            dispose();
        });

        add(panel);
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

        // ROLE CHECK
        if (!user.getRole().equalsIgnoreCase(role)) {
            JOptionPane.showMessageDialog(this,
                    "Wrong role selected!\nYour actual role: " + user.getRole());
            return;
        }

        JOptionPane.showMessageDialog(this, "Login Successful!");

        // OPEN DASHBOARD AS PER ROLE
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

        dispose(); // close login window
    }

    public static void launch() {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
