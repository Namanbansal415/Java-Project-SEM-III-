package com.onlinejobportal.ui;

import com.onlinejobportal.dao.JobDAO;
import com.onlinejobportal.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EmployerJobsFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private User employer;

    public EmployerJobsFrame(User user) {
        this.employer = user;

        setTitle("My Posted Jobs");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        loadMyJobs();
    }

    private void initUI() {
        JPanel p = new JPanel(new BorderLayout());

        model = new DefaultTableModel(new String[]{
                "ID", "Title", "Company", "Location", "Approved"
        }, 0);

        table = new JTable(model);
        table.setRowHeight(28);

        p.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnDelete = new JButton("Delete Job");
        p.add(btnDelete, BorderLayout.SOUTH);

        btnDelete.addActionListener(e -> deleteJob());

        add(p);
    }

    private void loadMyJobs() {
        model.setRowCount(0);

        for (Object[] row : new JobDAO().getJobsByEmployer(employer.getId())) {
            model.addRow(row);
        }
    }

    private void deleteJob() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a job first!");
            return;
        }

        int jobId = (int) model.getValueAt(row, 0);

        if (new JobDAO().deleteJob(jobId)) {
            JOptionPane.showMessageDialog(this, "Job deleted!");
            loadMyJobs();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete!");
        }
    }
}
