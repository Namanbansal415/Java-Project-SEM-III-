
package com.onlinejobportal.ui;

import com.onlinejobportal.dao.JobDAO;
import com.onlinejobportal.model.Job;
import com.onlinejobportal.model.User;
import com.onlinejobportal.util.DBUtil; // (if JobDAO uses DBUtil, keep import set)

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class EmployerDashboardFrame extends JFrame {

    private User currentUser;
    private JTable jobTable;
    private DefaultTableModel model;

    public EmployerDashboardFrame(User user) {
        this.currentUser = user;

        setTitle("Employer Dashboard");
        setSize(950, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        loadJobs();
    }

    private void initUI() {

        // 🌈 Gradient Background
        JPanel bg = new JPanel() {
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

        bg.setLayout(new GridBagLayout());

        // 🌟 Center White Card UI
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(850, 450));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(0, 70, 120), 3));

        // Title
        JLabel lblTitle = new JLabel("Employer Dashboard", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 70, 120));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        card.add(lblTitle, BorderLayout.NORTH);

        // ===============================
        // TABLE MODEL
        // ===============================
        model = new DefaultTableModel(new String[]{
                "Job ID", "Title", "Company", "Location", "Posted", "View Applicants"
        }, 0);

        jobTable = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 5;  // Only View Applicants button column
            }

            @Override
            public Class<?> getColumnClass(int col) {
                return col == 5 ? JButton.class : Object.class;
            }
        };

        jobTable.setRowHeight(35);
        jobTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jobTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(jobTable);
        card.add(scrollPane, BorderLayout.CENTER);

        // ===============================
        // VIEW APPLICANTS BUTTON HANDLER
        // Use safe renderer/editor implementation
        // ===============================
        TableCellRenderer viewRenderer = (table, value, isSelected, hasFocus, row, col) -> {
            JButton btn = new JButton("View");
            btn.setBackground(new Color(0, 153, 204));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            return btn;
        };

        jobTable.getColumnModel().getColumn(5).setCellRenderer(viewRenderer);

        // Editor: use DefaultCellEditor with button; ensure selection row reading is robust
        jobTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JButton("View")));

        // ===============================
        // BOTTOM BUTTONS: POST JOB + REFRESH
        // ===============================
        JPanel bottomPanel = new JPanel();

        JButton btnPostJob = new JButton("Post New Job");
        btnPostJob.setBackground(new Color(52, 152, 219));
        btnPostJob.setForeground(Color.WHITE);
        btnPostJob.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnPostJob.setFocusPainted(false);

        btnPostJob.addActionListener(e -> new PostJobFrame(currentUser).setVisible(true));

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(46, 204, 113));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnRefresh.setFocusPainted(false);

        btnRefresh.addActionListener(e -> loadJobs());

        bottomPanel.add(btnPostJob);
        bottomPanel.add(btnRefresh);

        card.add(bottomPanel, BorderLayout.SOUTH);

        bg.add(card);
        add(bg);
    }

    // =============================================
    // LOAD JOBS THAT BELONG TO THIS EMPLOYER
    // =============================================
    private void loadJobs() {
        model.setRowCount(0);

        try {
            JobDAO jobDao = new JobDAO();
            List<Job> jobs = jobDao.getJobsByEmployer(currentUser.getId());

            if (jobs == null || jobs.isEmpty()) {
                // no rows — keep table empty
                System.out.println("No jobs found for employer id: " + currentUser.getId());
                return;
            }

            for (Job j : jobs) {
                // postedAt could be null or a date — convert to string safely
                Object posted = j.getPostedAt() == null ? "" : j.getPostedAt().toString();
                model.addRow(new Object[]{
                        j.getId(),
                        j.getTitle(),
                        j.getCompany(),
                        j.getLocation(),
                        posted,
                        "View"
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load jobs:\n" + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ========== small inner class for button editing (safer than anonymous DefaultCellEditor hack) ==========
    // Using a dedicated editor avoids tricky anonymous-class compilation/runtime issues.
    private class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JButton button;
        private int currentRow = -1;

        public ButtonEditor(JButton btn) {
            this.button = btn;
            button.setBackground(new Color(0, 153, 204));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);

            button.addActionListener(e -> {
                // when button clicked, row should be determined, then open applicants
                if (currentRow >= 0 && currentRow < model.getRowCount()) {
                    Object idObj = model.getValueAt(currentRow, 0);
                    if (idObj instanceof Number) {
                        int jobId = ((Number) idObj).intValue();
                        // open applicants with jobId (constructor expects jobId)
                        SwingUtilities.invokeLater(() -> new ViewApplicantsFrame(jobId).setVisible(true));
                    } else {
                        // fallback parse
                        try {
                            int jobId = Integer.parseInt(String.valueOf(idObj));
                            SwingUtilities.invokeLater(() -> new ViewApplicantsFrame(jobId).setVisible(true));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(EmployerDashboardFrame.this,
                                    "Unable to determine job id for selected row.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.currentRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "View";
        }
    }
}
