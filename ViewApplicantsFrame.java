package com.onlinejobportal.ui;

import com.onlinejobportal.dao.ApplicationDAO;
import com.onlinejobportal.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ViewApplicantsFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private User employer;

    public ViewApplicantsFrame(User user) {
        this.employer = user;

        setTitle("Applicants");
        setSize(900, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        loadApplicants();
    }

    private void initUI() {
        JPanel p = new JPanel(new BorderLayout());

        model = new DefaultTableModel(
                new String[]{"Application ID", "Job Title", "Candidate", "Resume", "Applied At"}, 0
        );

        table = new JTable(model);
        table.setRowHeight(30);

        p.add(new JScrollPane(table), BorderLayout.CENTER);

        add(p);
    }

    private void loadApplicants() {
        model.setRowCount(0);

        for (Object[] r : new ApplicationDAO().getApplicantsForEmployer(employer.getId())) {
            model.addRow(r);
        }
    }
}
