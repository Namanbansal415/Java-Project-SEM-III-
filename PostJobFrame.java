package com.onlinejobportal.ui;

import com.onlinejobportal.dao.JobDAO;
import com.onlinejobportal.model.User;

import javax.swing.*;
import java.awt.*;

public class PostJobFrame extends JFrame {

    private JTextField txtTitle, txtCompany, txtLocation;
    private JTextArea txtDescription;

    private User employer;

    public PostJobFrame(User user) {
        this.employer = user;

        setTitle("Post New Job");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel p = new JPanel(null);

        JLabel l1 = new JLabel("Job Title:");
        l1.setBounds(50, 40, 120, 25);
        p.add(l1);

        txtTitle = new JTextField();
        txtTitle.setBounds(180, 40, 220, 28);
        p.add(txtTitle);

        JLabel l2 = new JLabel("Company:");
        l2.setBounds(50, 90, 120, 25);
        p.add(l2);

        txtCompany = new JTextField();
        txtCompany.setBounds(180, 90, 220, 28);
        p.add(txtCompany);

        JLabel l3 = new JLabel("Location:");
        l3.setBounds(50, 140, 120, 25);
        p.add(l3);

        txtLocation = new JTextField();
        txtLocation.setBounds(180, 140, 220, 28);
        p.add(txtLocation);

        JLabel l4 = new JLabel("Description:");
        l4.setBounds(50, 200, 120, 25);
        p.add(l4);

        txtDescription = new JTextArea();
        txtDescription.setBounds(180, 200, 220, 120);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        p.add(txtDescription);

        JButton btnPost = new JButton("Post Job");
        btnPost.setBounds(180, 350, 150, 40);
        p.add(btnPost);

        btnPost.addActionListener(e -> submitJob());

        add(p);
    }

    private void submitJob() {
        String title = txtTitle.getText().trim();
        String comp = txtCompany.getText().trim();
        String loc = txtLocation.getText().trim();
        String desc = txtDescription.getText().trim();

        if (title.isEmpty() || comp.isEmpty() || loc.isEmpty() || desc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        if (new JobDAO().addJob(employer.getId(), title, comp, loc, desc)) {
            JOptionPane.showMessageDialog(this, "Job Posted! (Pending Admin Approval)");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to post job!");
        }
    }
}
