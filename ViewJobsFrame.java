package com.onlinejobportal.ui;

import com.onlinejobportal.dao.ApplicationDAO;
import com.onlinejobportal.model.User;
import com.onlinejobportal.util.DBUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewJobsFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private User currentUser;

    public ViewJobsFrame(User user) {
        this.currentUser = user;

        setTitle("Available Jobs");
        setSize(950, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        loadJobs();
    }

    private void initUI() {

        // 🌈 MAIN GRADIENT BACKGROUND
        JPanel bg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(0, 160, 200),
                        0, getHeight(), new Color(0, 100, 140)
                );

                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        bg.setLayout(new GridBagLayout()); // to center the card


        // 🌟 WHITE CARD PANEL
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(850, 450));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(0, 80, 120), 3));


        // TITLE
        JLabel lblTitle = new JLabel("Available Job Listings", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 70, 120));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        card.add(lblTitle, BorderLayout.NORTH);


        // ===============================
        // JOB TABLE
        // ===============================
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
                "ID", "Title", "Company", "Location", "Description",
                "Posted By", "Posted At", "Apply"
        });

        table = new JTable(model) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;  // Only "Apply" button column is editable
            }

            @Override
            public Class<?> getColumnClass(int col) {
                return (col == 7) ? JButton.class : Object.class;
            }
        };

        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scroll = new JScrollPane(table);
        card.add(scroll, BorderLayout.CENTER);


        // ===============================
        // APPLY BUTTON RENDERER
        // ===============================
        TableColumn applyCol = table.getColumnModel().getColumn(7);

        applyCol.setCellRenderer((tbl, value, isSelected, hasFocus, row, col) -> {

            JButton btn = new JButton("Apply");
            btn.setBackground(new Color(0, 153, 204));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            return btn;
        });

        applyCol.setCellEditor(new DefaultCellEditor(new JCheckBox()) {

            JButton btn = new JButton("Apply");

            {
                btn.setBackground(new Color(0, 153, 204));
                btn.setForeground(Color.WHITE);
                btn.setFocusPainted(false);

                btn.addActionListener(e -> {
                    int row = table.getSelectedRow();
                    int jobId = (int) model.getValueAt(row, 0);
                    handleApply(jobId);
                });
            }

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value,
                                                         boolean isSelected, int row, int column) {
                return btn;
            }
        });


        // BACK BUTTON
        JButton btnBack = new JButton("Back");
        btnBack.setBackground(new Color(52, 152, 219));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(e -> dispose());

        JPanel bottom = new JPanel();
        bottom.add(btnBack);

        card.add(bottom, BorderLayout.SOUTH);


        bg.add(card);
        add(bg);
    }


    // =====================================================
    // APPLY LOGIC
    // =====================================================
    private void handleApply(int jobId) {

        ApplicationDAO appDao = new ApplicationDAO();
        int userId = currentUser.getId();

        // Duplicate check
        if (appDao.hasApplied(jobId, userId)) {
            JOptionPane.showMessageDialog(this,
                    "You have already applied for this job!",
                    "Duplicate Application",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String resumeText = JOptionPane.showInputDialog(
                this,
                "Enter your resume/summary:",
                "Apply for Job",
                JOptionPane.PLAIN_MESSAGE
        );

        if (resumeText == null || resumeText.isEmpty()) return;

        boolean ok = appDao.applyToJob(jobId, userId, resumeText);

        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Application submitted successfully!");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to apply!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    // =====================================================
    // LOAD JOBS
    // =====================================================
    private void loadJobs() {
        String sql =
                "SELECT id, title, company, location, description, posted_by, posted_at " +
                "FROM jobs WHERE approved = 1";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("company"),
                        rs.getString("location"),
                        rs.getString("description"),
                        rs.getInt("posted_by"),
                        rs.getTimestamp("posted_at"),
                        "Apply"
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Failed to load jobs!",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
