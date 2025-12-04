package com.onlinejobportal.ui;

import com.onlinejobportal.util.DBUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewApplicantsFrame extends JFrame {

    private int jobId;
    private DefaultTableModel model;
    private JTable table;

    public ViewApplicantsFrame(int jobId) {
        this.jobId = jobId;

        setTitle("Applicants for Job ID: " + jobId);
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        loadApplicants();
    }

    private void initUI() {

        // 🌈 Gradient Background Panel
        JPanel bg = new JPanel() {
            @Override
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

        // 🌟 White Card Panel
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(750, 420));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(0, 80, 120), 3));

        // Title
        JLabel lblTitle = new JLabel("Applicants List", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 70, 120));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        card.add(lblTitle, BorderLayout.NORTH);

        // =====================
        // TABLE
        // =====================
        model = new DefaultTableModel(new String[]{
                "Application ID",
                "Applicant Name",
                "Email",
                "Resume",
                "Applied At"
        }, 0);

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scroll = new JScrollPane(table);
        card.add(scroll, BorderLayout.CENTER);

        // Back Button
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnBack.setBackground(new Color(52, 152, 219));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);

        btnBack.addActionListener(e -> dispose());

        JPanel bottom = new JPanel();
        bottom.add(btnBack);

        card.add(bottom, BorderLayout.SOUTH);

        bg.add(card);
        add(bg);
    }

    // ===============================
    // LOAD APPLICANTS FROM DATABASE
    // ===============================
    private void loadApplicants() {

        String sql = """
            SELECT a.id AS app_id,
                   u.full_name,
                   u.email,
                   a.resume,
                   a.applied_at
            FROM applications a
            JOIN users u ON a.user_id = u.id
            WHERE a.job_id = ?
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("app_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("resume"),
                        rs.getTimestamp("applied_at")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to load applicants!",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}

