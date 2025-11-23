package com.onlinejobportal.ui;

import javax.swing.*;
import java.awt.*;
import com.onlinejobportal.model.User;

public class JobPortalHomeFrame extends JFrame {

    private User user;

    public JobPortalHomeFrame(User user) {
        this.user = user;

        setTitle("Online Job Portal - Dashboard");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(225, 240, 255));

        JLabel lblWelcome = new JLabel("Welcome, " + user.getFullName());
        lblWelcome.setBounds(50, 30, 500, 40);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblWelcome.setForeground(new Color(0, 51, 102));
        panel.add(lblWelcome);

        // VIEW JOBS BUTTON
        JButton btnViewJobs = new JButton("View Available Jobs");
        btnViewJobs.setBounds(180, 110, 230, 45);
        btnViewJobs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnViewJobs.setBackground(new Color(0, 153, 255));
        btnViewJobs.setForeground(Color.white);
        panel.add(btnViewJobs);

        // MY APPLICATIONS BUTTON
        JButton btnMyApps = new JButton("My Applications");
        btnMyApps.setBounds(180, 180, 230, 45);
        btnMyApps.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnMyApps.setBackground(new Color(0, 153, 102));
        btnMyApps.setForeground(Color.white);
        panel.add(btnMyApps);

        // LOGOUT BUTTON
        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(180, 250, 230, 45);
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogout.setBackground(new Color(102, 0, 0));
        btnLogout.setForeground(Color.white);
        panel.add(btnLogout);

        // ACTIONS
        btnViewJobs.addActionListener(e -> {
    new ViewJobsFrame(user).setVisible(true);
});


        btnMyApps.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "My Applications coming soon!");
        });

        btnLogout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        btnMyApps.addActionListener(e -> {
    new MyApplicationsFrame(user).setVisible(true);
});


        add(panel);
    }
}
