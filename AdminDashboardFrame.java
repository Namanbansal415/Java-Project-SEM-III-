
package com.onlinejobportal.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import com.onlinejobportal.util.DBUtil;

public class AdminDashboardFrame extends JFrame {

    JTable userTable, jobTable;
    DefaultTableModel userModel, jobModel;

    public AdminDashboardFrame() {
        setTitle("Admin Dashboard");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {

        // ===============================
        // GRADIENT BACKGROUND
        // ===============================
        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(0, 90, 160),
                        0, getHeight(), new Color(0, 60, 120)
                );

                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        bgPanel.setLayout(new GridBagLayout());


        // ===============================
        // WHITE CARD PANEL
        // ===============================
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(780, 500));
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(new Color(0, 70, 140), 3));


        // ===============================
        // TITLE
        // ===============================
        JLabel lblTitle = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(0, 60, 120));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        card.add(lblTitle, BorderLayout.NORTH);


        // ===============================
        // TABS
        // ===============================
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 16));

        tabs.addTab("User Management", createUserPanel());
        tabs.addTab("Job Approvals", createJobPanel());
        tabs.addTab("System Settings", createSettingsPanel());

        card.add(tabs, BorderLayout.CENTER);


        // ADD CARD TO CENTER
        bgPanel.add(card);
        add(bgPanel);

        loadUsers();
        loadPendingJobs();
    }

    // ===============================
    // USER MANAGEMENT PANEL
    // ===============================
    private JPanel createUserPanel() {
        JPanel p = new JPanel(new BorderLayout());

        userModel = new DefaultTableModel(new String[]{
                "ID", "Username", "Email", "Full Name", "Role"
        }, 0);

        userTable = new JTable(userModel);
        userTable.setRowHeight(28);
        p.add(new JScrollPane(userTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();

        JButton btnDelete = createButton("Delete User", new Color(231, 76, 60));
        JButton btnRole = createButton("Change Role", new Color(52, 152, 219));
        JButton btnRefresh = createButton("Refresh", new Color(46, 204, 113));

        btnPanel.add(btnDelete);
        btnPanel.add(btnRole);
        btnPanel.add(btnRefresh);

        p.add(btnPanel, BorderLayout.SOUTH);

        btnDelete.addActionListener(e -> deleteUser());
        btnRole.addActionListener(e -> changeRole());
        btnRefresh.addActionListener(e -> loadUsers());

        return p;
    }

    private void loadUsers() {
        try {
            userModel.setRowCount(0);

            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT id, username, email, full_name, role FROM users");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                userModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("full_name"),
                        rs.getString("role")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteUser() {
        int row = userTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a user first!");
            return;
        }

        int id = (int) userModel.getValueAt(row, 0);

        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM users WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();

            loadUsers();
            JOptionPane.showMessageDialog(this, "User deleted!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeRole() {
        int row = userTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a user first!");
            return;
        }

        int id = (int) userModel.getValueAt(row, 0);
        String newRole = JOptionPane.showInputDialog(this, 
                "Enter new role (admin/employer/candidate):");

        if (newRole == null || newRole.trim().isEmpty()) return;

        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE users SET role=? WHERE id=?");
            ps.setString(1, newRole);
            ps.setInt(2, id);
            ps.executeUpdate();

            loadUsers();
            JOptionPane.showMessageDialog(this, "Role updated!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===============================
    // JOB APPROVAL PANEL
    // ===============================
    private JPanel createJobPanel() {
        JPanel p = new JPanel(new BorderLayout());

        jobModel = new DefaultTableModel(new String[]{
                "ID", "Title", "Company", "Location", "Description"
        }, 0);

        jobTable = new JTable(jobModel);
        jobTable.setRowHeight(28);

        p.add(new JScrollPane(jobTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();

        JButton btnApprove = createButton("Approve", new Color(39, 174, 96));
        JButton btnReject = createButton("Reject", new Color(192, 57, 43));
        JButton btnRefresh = createButton("Refresh", new Color(52, 152, 219));

        btnPanel.add(btnApprove);
        btnPanel.add(btnReject);
        btnPanel.add(btnRefresh);

        p.add(btnPanel, BorderLayout.SOUTH);

        btnApprove.addActionListener(e -> approveJob());
        btnReject.addActionListener(e -> rejectJob());
        btnRefresh.addActionListener(e -> loadPendingJobs());

        return p;
    }

    private void loadPendingJobs() {
        try {
            jobModel.setRowCount(0);

            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT id, title, company, location, description FROM jobs WHERE approved = 0"
            );

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                jobModel.addRow(new Object[]{
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

    private void approveJob() {
        int row = jobTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a job!");
            return;
        }

        int id = (int) jobModel.getValueAt(row, 0);

        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE jobs SET approved = 1 WHERE id=?");
            ps.setInt(1, id);
            ps.executeUpdate();

            loadPendingJobs();
            JOptionPane.showMessageDialog(this, "Job Approved!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rejectJob() {
        int row = jobTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a job!");
            return;
        }

        int id = (int) jobModel.getValueAt(row, 0);

        try {
            Connection con = DBUtil.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM jobs WHERE id=?");
            ps.setInt(1, id);
            ps.executeUpdate();

            loadPendingJobs();
            JOptionPane.showMessageDialog(this, "Job Rejected!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===============================
    // SETTINGS PANEL
    // ===============================
    private JPanel createSettingsPanel() {
        JPanel p = new JPanel();
        JLabel label = new JLabel("System Settings Coming Soon...");
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(new Color(80, 80, 80));
        p.add(label);
        return p;
    }

    // ===============================
    // BUTTON STYLING
    // ===============================
    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
