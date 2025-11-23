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
        setTitle("Online Job Portal - Signup");
        setSize(420, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        initUI();
    }

    private void initUI() {
        JPanel p = new JPanel(null);
        p.setBackground(new Color(245, 245, 245));

        JLabel lblTitle = new JLabel("Create Account");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBounds(130, 20, 250, 30);
        p.add(lblTitle);

        JLabel l1 = new JLabel("Username:");
        l1.setBounds(60, 80, 100, 25);
        p.add(l1);
        txtUsername = new JTextField();
        txtUsername.setBounds(160, 80, 180, 28);
        p.add(txtUsername);

        JLabel l2 = new JLabel("Password:");
        l2.setBounds(60, 130, 100, 25);
        p.add(l2);
        txtPassword = new JPasswordField();
        txtPassword.setBounds(160, 130, 180, 28);
        p.add(txtPassword);

        JLabel l3 = new JLabel("Full Name:");
        l3.setBounds(60, 180, 100, 25);
        p.add(l3);
        txtFullname = new JTextField();
        txtFullname.setBounds(160, 180, 180, 28);
        p.add(txtFullname);

        JLabel l4 = new JLabel("Email:");
        l4.setBounds(60, 230, 100, 25);
        p.add(l4);
        txtEmail = new JTextField();
        txtEmail.setBounds(160, 230, 180, 28);
        p.add(txtEmail);

        JLabel l5 = new JLabel("Role:");
        l5.setBounds(60, 280, 100, 25);
        p.add(l5);

        cmbRole = new JComboBox<>(new String[]{"candidate", "employer", "admin"});
        cmbRole.setBounds(160, 280, 180, 30);
        p.add(cmbRole);

        JButton btnSignup = new JButton("Sign Up");
        btnSignup.setBounds(140, 350, 120, 35);
        btnSignup.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSignup.setBackground(new Color(51, 153, 255));
        btnSignup.setForeground(Color.white);
        btnSignup.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        p.add(btnSignup);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(140, 400, 120, 35);
        btnBack.setBackground(Color.gray);
        btnBack.setForeground(Color.white);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        p.add(btnBack);

        btnSignup.addActionListener(e -> registerAction());
        btnBack.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        add(p);
    }

    private void registerAction() {
        User u = new User();
        u.setUsername(txtUsername.getText().trim());
        u.setPassword(new String(txtPassword.getPassword()).trim());
        u.setFullName(txtFullname.getText().trim());
        u.setEmail(txtEmail.getText().trim());
        u.setRole(cmbRole.getSelectedItem().toString()); // SELECTED ROLE SET HOGA

        boolean ok = new UserDAO().registerUser(u);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Signup successful! Please login.");
            new LoginFrame().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Signup failed! Username may already exist.");
        }
    }
}
