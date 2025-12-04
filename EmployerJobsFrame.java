
package com.onlinejobportal.ui;

import com.onlinejobportal.model.User;
import com.onlinejobportal.util.DBUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class EmployerJobsFrame extends JFrame {

    private User currentUser;
    private JTable jobTable;
    private DefaultTableModel model;

    public EmployerJobsFrame(User user) {
        this.currentUser = user;

        setTitle("Manage Job Listings");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        loadJobs();
    }

    private void initUI() {

        // BACKGROUND PANEL WITH GRADIENT
        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(0, 168, 204),
                        0, getHeight(), new Color(0, 120, 160)
                );

                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        bgPanel.setLayout(new GridBagLayout());


        // WHITE CARD PANEL
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(750, 420));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(0, 100, 140), 3));


        // TITLE
        JLabel lblTitle = new JLabel("My Job Listings", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 80, 130));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        card.add(lblTitle, BorderLayout.NORTH);


        // TABLE MODEL
        model = new DefaultTableModel(new String[]{
                "ID", "Title", "Company", "Location", "Description", "Posted At"
        }, 0);

        jobTable = new JTable(model);
        jobTable.setRowHeight(28);
        jobTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jobTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        card.add(new JScrollPane(jobTable), BorderLayout.CENTER);


        // BUTTON PANEL
        JPanel btnPanel = new JPanel();

        JButton btnEdit = createButton("Edit Job", new Color(255, 153, 0));
        JButton btnDelete = createButton("Delete Job", new Color(231, 76, 60));
        JButton btnRefresh = createButton("Refresh", new Color(46, 204, 113));

        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        btnPanel.add(btnRefresh);

        card.add(btnPanel, BorderLayout.SOUTH);


        // BUTTON ACTIONS
        btnRefresh.addActionListener(e -> loadJobs());
        btnDelete.addActionListener(e -> deleteJob());
        btnEdit.addActionListener(e -> editJob());


        // Add card to center
        bgPanel.add(card);
        add(bgPanel);
    }

    // ================================================================
    // LOAD JOBS LIST POSTED BY THIS EMPLOYER
    // ================================================================
    private void loadJobs() {
        model.setRowCount(0);

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id, title, company, location, description, posted_at " +
                            "FROM jobs WHERE posted_by = ?"
            );
            ps.setInt(1, currentUser.getId());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("company"),
                        rs.getString("location"),
                        rs.getString("description"),
                        rs.getTimestamp("posted_at")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================================================================
    // DELETE JOB
    // ================================================================
    private void deleteJob() {
        int row = jobTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a job to delete!");
            return;
        }

        int jobId = (int) model.getValueAt(row, 0);

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM jobs WHERE id = ?");
            ps.setInt(1, jobId);
            ps.executeUpdate();

            loadJobs();
            JOptionPane.showMessageDialog(this, "Job Deleted!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================================================================
    // EDIT JOB
    // ================================================================
    private void editJob() {
        int row = jobTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a job to edit!");
            return;
        }

        int jobId = (int) model.getValueAt(row, 0);
        // Use the single-argument constructor and open the PostJobFrame;
        // if PostJobFrame supports setting an existing job for editing, add a setter or update there.
        new PostJobFrame(currentUser).setVisible(true);
    }


    // ================================================================
    // BUTTON STYLING
    // ================================================================
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
