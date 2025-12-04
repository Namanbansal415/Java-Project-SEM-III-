package com.onlinejobportal.ui;

import com.onlinejobportal.dao.JobDAO;
import com.onlinejobportal.model.Job;
import com.onlinejobportal.model.User;

import javax.swing.*;
import java.awt.*;

public class PostJobFrame extends JFrame {

    private JTextField txtTitle, txtCompany, txtLocation;
    private JTextArea txtDescription;
    private User currentUser;

    public PostJobFrame(User user) {
        this.currentUser = user;

        setTitle("Post a Job");
        setSize(650, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {

        // 🌈 MAIN GRADIENT BACKGROUND PANEL
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

        bg.setLayout(new GridBagLayout()); // center card

        // 🌟 WHITE CARD PANEL
        JPanel card = new JPanel(null);
        card.setPreferredSize(new Dimension(500, 500));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(0, 70, 120), 3));

        int xLabel = 40;
        int xField = 160;
        int width = 280;

        // 🏷️ TITLE
        JLabel header = new JLabel("Post New Job", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(new Color(0, 70, 120));
        header.setBounds(150, 20, 200, 40);
        card.add(header);

        // Job Title
        JLabel lblTitle = new JLabel("Job Title:");
        lblTitle.setBounds(xLabel, 90, 120, 25);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        card.add(lblTitle);

        txtTitle = new JTextField();
        txtTitle.setBounds(xField, 90, width, 30);
        txtTitle.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        card.add(txtTitle);

        // Company
        JLabel lblCompany = new JLabel("Company:");
        lblCompany.setBounds(xLabel, 140, 120, 25);
        lblCompany.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        card.add(lblCompany);

        txtCompany = new JTextField();
        txtCompany.setBounds(xField, 140, width, 30);
        txtCompany.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        card.add(txtCompany);

        // Location
        JLabel lblLocation = new JLabel("Location:");
        lblLocation.setBounds(xLabel, 190, 120, 25);
        lblLocation.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        card.add(lblLocation);

        txtLocation = new JTextField();
        txtLocation.setBounds(xField, 190, width, 30);
        txtLocation.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        card.add(txtLocation);

        // Description
        JLabel lblDesc = new JLabel("Description:");
        lblDesc.setBounds(xLabel, 240, 120, 25);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        card.add(lblDesc);

        txtDescription = new JTextArea();
        txtDescription.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);

        JScrollPane descScroll = new JScrollPane(txtDescription);
        descScroll.setBounds(xField, 240, width, 120);
        card.add(descScroll);

        // Post Button
        JButton btnPost = new JButton("Post Job");
        btnPost.setBounds(180, 390, 150, 40);

        btnPost.setBackground(new Color(0, 120, 200));
        btnPost.setForeground(Color.WHITE);
        btnPost.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnPost.setFocusPainted(false);
        btnPost.setBorder(BorderFactory.createEmptyBorder());

        card.add(btnPost);

        btnPost.addActionListener(e -> postJob());

        // Add card to gradient panel
        bg.add(card);
        add(bg);
    }

    private void postJob() {
        String title = txtTitle.getText().trim();
        String company = txtCompany.getText().trim();
        String location = txtLocation.getText().trim();
        String description = txtDescription.getText().trim();

        if (title.isEmpty() || company.isEmpty() || location.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "All fields are required!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Job job = new Job();
        job.setTitle(title);
        job.setCompany(company);
        job.setLocation(location);
        job.setDescription(description);
        job.setPostedBy(currentUser.getId());

        boolean ok = new JobDAO().postJob(job);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Job Posted Successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to post job!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

