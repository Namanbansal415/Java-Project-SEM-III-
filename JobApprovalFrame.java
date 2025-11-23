package com.onlinejobportal.ui;

import com.onlinejobportal.dao.JobDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class JobApprovalFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JobDAO dao = new JobDAO();

    public JobApprovalFrame() {
        setTitle("Pending Job Approvals");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        loadPendingJobs();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel lbl = new JLabel("Pending Job Approvals", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lbl.setForeground(new Color(0, 80, 160));

        panel.add(lbl, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Title", "Company", "Location", "Description"});

        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane sp = new JScrollPane(table);
        panel.add(sp, BorderLayout.CENTER);

        // Approve / Reject buttons
        JPanel btnPanel = new JPanel();

        JButton btnApprove = new JButton("Approve");
        JButton btnReject = new JButton("Reject");

        btnApprove.setBackground(new Color(0, 128, 0));
        btnApprove.setForeground(Color.WHITE);

        btnReject.setBackground(new Color(200, 0, 0));
        btnReject.setForeground(Color.WHITE);

        btnPanel.add(btnApprove);
        btnPanel.add(btnReject);

        panel.add(btnPanel, BorderLayout.SOUTH);

        add(panel);

        btnApprove.addActionListener(e -> approveSelectedJob());
        btnReject.addActionListener(e -> rejectSelectedJob());
    }

    private void loadPendingJobs() {
        model.setRowCount(0); 

        for (Object[] row : dao.getPendingJobs()) {
            model.addRow(row);
        }
    }

    private void approveSelectedJob() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job!");
            return;
        }

        int jobId = (int) model.getValueAt(row, 0);

        if (dao.approveJob(jobId)) {
            JOptionPane.showMessageDialog(this, "Job Approved!");
            loadPendingJobs();
        }
    }

    private void rejectSelectedJob() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job!");
            return;
        }

        int jobId = (int) model.getValueAt(row, 0);

        if (dao.rejectJob(jobId)) {
            JOptionPane.showMessageDialog(this, "Job Rejected!");
            loadPendingJobs();
        }
    }
}
