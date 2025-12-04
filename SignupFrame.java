
package com.onlinejobportal.ui;

import com.onlinejobportal.dao.UserDAO;
import com.onlinejobportal.model.User;

import javax.swing.*;
import java.awt.*;

public class SignupFrame extends JFrame {

    private JTextField txtUsername, txtFullname, txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole;

    public SignupFrame() {
        setTitle("Create Account");
        setSize(520, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {

        // MAIN GRADIENT PANEL
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(255, 153, 102),
                        0, getHeight(), new Color(204, 51, 102)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        panel.setLayout(null);

        // TITLE
        JLabel lblTitle = new JLabel("Create Your Account");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(120, 40, 350, 40);
        panel.add(lblTitle);

        // ROLE LABEL
        JLabel lblRole = new JLabel("Register As:");
        lblRole.setBounds(90, 110, 140, 25);
        lblRole.setForeground(Color.WHITE);
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        panel.add(lblRole);

        // ROLE DROPDOWN
        cmbRole = new JComboBox<>(new String[]{"candidate", "employer", "admin"});
        cmbRole.setBounds(210, 110, 210, 30);
        cmbRole.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        panel.add(cmbRole);

        // USERNAME LABEL
        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(90, 160, 120, 25);
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        panel.add(lblUser);

        // USERNAME FIELD
        txtUsername = new JTextField();
        txtUsername.setBounds(210, 160, 210, 30);
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtUsername.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        panel.add(txtUsername);

        // PASSWORD
        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(90, 210, 120, 25);
        lblPass.setForeground(Color.WHITE);
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        panel.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(210, 210, 210, 30);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtPassword.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        panel.add(txtPassword);

        // FULL NAME
        JLabel lblFull = new JLabel("Full Name:");
        lblFull.setBounds(90, 260, 120, 25);
        lblFull.setForeground(Color.WHITE);
        lblFull.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        panel.add(lblFull);

        txtFullname = new JTextField();
        txtFullname.setBounds(210, 260, 210, 30);
        txtFullname.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtFullname.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        panel.add(txtFullname);

        // EMAIL
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(90, 310, 120, 25);
        lblEmail.setForeground(Color.WHITE);
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        panel.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(210, 310, 210, 30);
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtEmail.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        panel.add(txtEmail);

        // SIGNUP BUTTON
        JButton btnSignup = new JButton("Sign Up");
        btnSignup.setBounds(160, 380, 180, 45);
        btnSignup.setBackground(new Color(0, 204, 102));
        btnSignup.setForeground(Color.WHITE);
        btnSignup.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnSignup.setFocusPainted(false);
        btnSignup.setBorder(BorderFactory.createEmptyBorder());
        panel.add(btnSignup);

        // BACK BUTTON
        JButton btnBack = new JButton("Back");
        btnBack.setBounds(160, 440, 180, 40);
        btnBack.setBackground(Color.WHITE);
        btnBack.setForeground(new Color(120, 30, 50));
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnBack.setFocusPainted(false);
        btnBack.setBorder(BorderFactory.createLineBorder(new Color(120, 30, 50), 2));
        panel.add(btnBack);

        btnSignup.addActionListener(e -> registerAction());
        btnBack.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        add(panel);
    }

    private void registerAction() {

        User u = new User();
        u.setUsername(txtUsername.getText().trim());
        u.setPassword(new String(txtPassword.getPassword()).trim());
        u.setFullName(txtFullname.getText().trim());
        u.setEmail(txtEmail.getText().trim());
        u.setRole(cmbRole.getSelectedItem().toString());

        boolean ok = new UserDAO().registerUser(u);

        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Signup Successful! Please Login.");
            new LoginFrame().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Signup Failed! Try different username.");
        }
    }
}
