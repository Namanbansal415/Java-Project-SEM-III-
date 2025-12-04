package com.onlinejobportal.ui;

import com.onlinejobportal.model.User;
import com.onlinejobportal.util.DBUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MyApplicationsFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private User currentUser;

    public MyApplicationsFrame(User user) {
        this.currentUser = user;

        setTitle("My Job Applications");
        setSize(900, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        loadApplications();
    }

    private void initUI() {

        // MAIN BACKGROUND PANEL WITH GRADIENT
        JPanel bg = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(0, 180, 200),
                        0, getHeight(), new Color(0, 120, 150)
                );

                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(new GridBagLayout());


        // WHITE CARD PANEL
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(750, 420));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(0, 80, 120), 3));


        // PAGE TITLE
        JLabel lblTitle = new JLabel("My Applications", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 70, 120));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        card.add(lblTitle, BorderLayout.NORTH);


        // TABLE MODEL
        model = new DefaultTableModel(new String[]{
                "Application ID", "Job Title", "Company", "Location",
                "Description", "Applied At"
        }, 0);

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scroll = new JScrollPane(table);
        card.add(scroll, BorderLayout.CENTER);


        // BACK BUTTON
        JButton btnBack = new JButton("Back to Dashboard");
        btnBack.setBackground(new Color(52, 152, 219));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnBack.setFocusPainted(false);
        btnBack.setBorder(BorderFactory.createEmptyBorder());
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnBack.addActionListener(e -> dispose());

        JPanel bottom = new JPanel();
        bottom.add(btnBack);
        card.add(bottom, BorderLayout.SOUTH);


        // ADD CARD TO BACKGROUND
        bg.add(card);

        add(bg);
    }

    // ========================================================
    // LOAD APPLICATIONS
    // ========================================================
    private void loadApplications() {
        model.setRowCount(0);

        String sql =
                "SELECT a.id AS app_id, j.title, j.company, j.location, j.description, a.applied_at " +
                "FROM applications a " +
                "JOIN jobs j ON a.job_id = j.id " +
                "WHERE a.user_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, currentUser.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("app_id"),
                        rs.getString("title"),
                        rs.getString("company"),
                        rs.getString("location"),
                        rs.getString("description"),
                        rs.getTimestamp("applied_at")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to load applications!",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}

