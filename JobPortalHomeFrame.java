package com.onlinejobportal.ui;

import com.onlinejobportal.model.User;
import javax.swing.*;
import java.awt.*;

public class JobPortalHomeFrame extends JFrame {

    private User currentUser;

    public JobPortalHomeFrame(User user) {
        this.currentUser = user;

        setTitle("Candidate Dashboard");
        setSize(650, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {

        // MAIN GRADIENT BACKGROUND
        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(72, 201, 176),
                        0, getHeight(), new Color(19, 141, 117)
                );

                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        bgPanel.setLayout(new GridBagLayout()); // center the card

        // WHITE CARD PANEL
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(400, 350));
        card.setBackground(Color.WHITE);
        card.setLayout(null);
        card.setBorder(BorderFactory.createLineBorder(new Color(0, 100, 80), 3));

        // TITLE
        JLabel lblTitle = new JLabel("Candidate Dashboard", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(0, 90, 70));
        lblTitle.setBounds(60, 20, 280, 40);
        card.add(lblTitle);

        // VIEW JOBS BUTTON
        JButton btnViewJobs = new JButton("View Available Jobs");
        btnViewJobs.setBounds(90, 100, 220, 45);
        styleButton(btnViewJobs, new Color(0, 153, 204));
        card.add(btnViewJobs);

        // MY APPLICATIONS BUTTON
        JButton btnMyApp = new JButton("My Applications");
        btnMyApp.setBounds(90, 160, 220, 45);
        styleButton(btnMyApp, new Color(255, 153, 0));
        card.add(btnMyApp);

        // LOGOUT BUTTON
        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(90, 220, 220, 45);
        styleButton(btnLogout, new Color(220, 53, 69));
        card.add(btnLogout);

        // ACTIONS
        btnViewJobs.addActionListener(e -> new ViewJobsFrame(currentUser).setVisible(true));
        btnMyApp.addActionListener(e -> new MyApplicationsFrame(currentUser).setVisible(true));
        btnLogout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        // ADD CARD TO CENTER
        bgPanel.add(card);

        add(bgPanel);
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}

