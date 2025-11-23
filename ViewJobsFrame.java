package com.onlinejobportal.ui;

import com.onlinejobportal.dao.ApplicationDAO;
import com.onlinejobportal.model.User;
import com.onlinejobportal.util.DBUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.AbstractCellEditor;
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
        setSize(950, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        loadJobs();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(230, 240, 255));

        JLabel lblTitle = new JLabel("Available Jobs", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(0, 51, 102));
        panel.add(lblTitle, BorderLayout.NORTH);

        // Table setup
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
                "ID", "Title", "Company", "Location", "Description",
                "Posted By", "Posted At", "Apply"
        });

        table = new JTable(model) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 7) return JButton.class;
                return Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 7;
            }
        };

        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);

        // Back button
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setBackground(Color.gray);
        btnBack.setForeground(Color.white);
        btnBack.addActionListener(e -> dispose());

        JPanel bottom = new JPanel();
        bottom.add(btnBack);
        panel.add(bottom, BorderLayout.SOUTH);

        add(panel);

        // APPLY BUTTON COLUMN
        TableColumn applyCol = table.getColumnModel().getColumn(7);
        applyCol.setCellRenderer((tbl, value, isSelected, hasFocus, row, col) -> {
            JButton b = new JButton("Apply");
            b.setBackground(new Color(0, 153, 204));
            b.setForeground(Color.white);
            return b;
        });

        applyCol.setCellEditor(new ApplyButtonEditor());
    }

    // Custom working editor class
    private class ApplyButtonEditor extends AbstractCellEditor implements TableCellEditor {

        private final JButton button;

        public ApplyButtonEditor() {
            button = new JButton("Apply");
            button.setBackground(new Color(0, 153, 204));
            button.setForeground(Color.white);

            button.addActionListener(e -> {
                int row = table.getSelectedRow();
                int jobId = (int) model.getValueAt(row, 0);
                handleApply(jobId);
                stopCellEditing();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Apply";
        }
    }

    private void handleApply(int jobId) {
        ApplicationDAO appDao = new ApplicationDAO();
        int userId = currentUser.getId();

        if (appDao.hasApplied(jobId, userId)) {
            JOptionPane.showMessageDialog(this, "You have already applied!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String resumeText = JOptionPane.showInputDialog(this, "Paste your resume:", "Resume", JOptionPane.PLAIN_MESSAGE);
        if (resumeText == null) return;

        boolean ok = appDao.applyToJob(jobId, userId, resumeText);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Application Submitted!");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to submit!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadJobs() {
        String sql = "SELECT id, title, company, location, description, posted_by, posted_at FROM jobs";

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
            JOptionPane.showMessageDialog(this, "Error loading jobs!", "DB Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
