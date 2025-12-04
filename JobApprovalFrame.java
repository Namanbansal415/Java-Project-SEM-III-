package com.onlinejobportal.ui;

import com.onlinejobportal.util.DBUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class JobApprovalFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public JobApprovalFrame() {

        setTitle("Job Approval Panel");
        setSize(900, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        loadPendingJobs();
    }

    private void initUI() {

        // BACKGROUND GRADIENT PANEL
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


        // WHITE CARD PANEL (CENTER)
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(750, 420));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(0, 80, 120), 3));


        // TITLE
        JLabel lblTitle = new JLabel("Pending Job Approvals", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 70, 120));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        card.add(lblTitle, BorderLayout.NORTH);


        // TABLE MODEL
        model = new DefaultTableModel(new String[]{
                "ID", "Title", "Company", "Location", "Description"
        }, 0);

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        card.add(new JScrollPane(table), BorderLayout.CENTER);


        // BUTTON PANEL
        JPanel btnPanel = new JPanel();

        JButton btnApprove = createButton("Approve", new Color(46, 204, 113));
        JButton btnReject = createButton("Reject", new Color(231, 76, 60));
        JButton btnRefresh = createButton("Refresh", new Color(52, 152, 219));

        btnPanel.add(btnApprove);
        btnPanel.add(btnReject);
        btnPanel.add(btnRefresh);

        card.add(btnPanel, BorderLayout.SOUTH);


        // ACTION LISTENERS
        btnApprove.addActionListener(e -> approveJob());
        btnReject.addActionListener(e -> rejectJob());
        btnRefresh.addActionListener(e -> loadPendingJobs());


        // Add card inside center of gradient panel
        bg.add(card);

        add(bg);
    }

    // ===========================================================
    // LOAD PENDING JOBS
    // ===========================================================
    private void loadPendingJobs() {
        model.setRowCount(0);

        try (Connection con = DBUtil.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                    "SELECT id, title, company, location, description FROM jobs WHERE approved = 0"
            );

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("company"),
                        rs.getString("location"),
                        rs.getString("description")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===========================================================
    // APPROVE JOB
    // ===========================================================
    private void approveJob() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a job to approve!");
            return;
        }

        int jobId = (int) model.getValueAt(row, 0);

        try (Connection con = DBUtil.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE jobs SET approved = 1 WHERE id = ?"
            );
            ps.setInt(1, jobId);
            ps.executeUpdate();

            loadPendingJobs();
            JOptionPane.showMessageDialog(this, "Job Approved Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===========================================================
    // REJECT JOB
    // ===========================================================
    private void rejectJob() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a job to reject!");
            return;
        }

        int jobId = (int) model.getValueAt(row, 0);

        try (Connection con = DBUtil.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM jobs WHERE id = ?"
            );
            ps.setInt(1, jobId);
            ps.executeUpdate();

            loadPendingJobs();
            JOptionPane.showMessageDialog(this, "Job Rejected & Deleted!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ===========================================================
    // BUTTON STYLING
    // ===========================================================
    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBackground(bgColor);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}

