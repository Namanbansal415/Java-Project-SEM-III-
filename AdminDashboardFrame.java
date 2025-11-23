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
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(0, 50, 100));
        add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("User Management", userPanel());
        tabs.add("Job Approvals", jobPanel());
        tabs.add("System Settings", settingsPanel());

        add(tabs, BorderLayout.CENTER);

        loadUsers();
        loadPendingJobs();
    }

    // ============================
    //  USER MANAGEMENT PANEL
    // ============================
    private JPanel userPanel() {
        JPanel p = new JPanel(new BorderLayout());

        userModel = new DefaultTableModel(new String[]{
                "ID", "Username", "Email", "Full Name", "Role"
        }, 0);

        userTable = new JTable(userModel);
        userTable.setRowHeight(28);

        JScrollPane scroll = new JScrollPane(userTable);
        p.add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();

        JButton btnDelete = new JButton("Delete User");
        JButton btnRole = new JButton("Change Role");
        JButton btnRefresh = new JButton("Refresh");

        btnPanel.add(btnDelete);
        btnPanel.add(btnRole);
        btnPanel.add(btnRefresh);

        p.add(btnPanel, BorderLayout.SOUTH);

        btnDelete.addActionListener(e -> deleteUser());
        btnRole.addActionListener(e -> changeRole());
        btnRefresh.addActionListener(e -> loadUsers());

        return p;
    }

    // Load users from database
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

        if (newRole == null || newRole.isEmpty()) return;

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

    // ============================
    //  JOB APPROVAL PANEL
    // ============================
    private JPanel jobPanel() {
        JPanel p = new JPanel(new BorderLayout());

        jobModel = new DefaultTableModel(new String[]{
                "ID", "Title", "Company", "Location", "Description"
        }, 0);

        jobTable = new JTable(jobModel);
        jobTable.setRowHeight(28);

        JScrollPane scroll = new JScrollPane(jobTable);
        p.add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();

        JButton btnApprove = new JButton("Approve");
        JButton btnReject = new JButton("Reject");
        JButton btnRefresh = new JButton("Refresh");

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
            JOptionPane.showMessageDialog(this, "Job Rejected & Deleted!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ============================
    //  SETTINGS PANEL
    // ============================
    private JPanel settingsPanel() {
        JPanel p = new JPanel();
        p.add(new JLabel("System settings will come here."));
        return p;
    }
}
