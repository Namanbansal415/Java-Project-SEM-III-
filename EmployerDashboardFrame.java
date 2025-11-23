package com.onlinejobportal.ui;

import com.onlinejobportal.model.User;

import javax.swing.*;
import java.awt.*;

public class EmployerDashboardFrame extends JFrame {

    private User employer;

    public EmployerDashboardFrame(User user) {
        this.employer = user;

        setTitle("Employer Dashboard");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(230, 240, 255));

        JLabel lbl = new JLabel("Employer Dashboard", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lbl.setBounds(120, 20, 400, 40);
        lbl.setForeground(new Color(0, 60, 130));
        panel.add(lbl);

        JButton btnPostJob = new JButton("Post Job");
        btnPostJob.setBounds(200, 100, 200, 40);
        panel.add(btnPostJob);

        JButton btnMyJobs = new JButton("My Jobs");
        btnMyJobs.setBounds(200, 160, 200, 40);
        panel.add(btnMyJobs);

        JButton btnApplications = new JButton("View Applications");
        btnApplications.setBounds(200, 220, 200, 40);
        panel.add(btnApplications);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(200, 280, 200, 40);
        panel.add(btnLogout);

        btnPostJob.addActionListener(e -> new PostJobFrame(employer).setVisible(true));
        btnMyJobs.addActionListener(e -> new EmployerJobsFrame(employer).setVisible(true));
        btnApplications.addActionListener(e -> new ViewApplicantsFrame(employer).setVisible(true));

        btnLogout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        add(panel);
    }
}
