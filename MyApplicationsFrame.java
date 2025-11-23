package com.onlinejobportal.ui;

import com.onlinejobportal.dao.ApplicationDAO;
import com.onlinejobportal.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

public class MyApplicationsFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private User currentUser;

    public MyApplicationsFrame(User user) {
        this.currentUser = user;

        setTitle("My Applications");
        setSize(900, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
        loadApplications();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("My Job Applications", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(0, 51, 102));
        panel.add(title, BorderLayout.NORTH);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
                "App ID", "Job Title", "Company", "Location", "Description", "Applied At"
        });

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);

        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> dispose());

        JPanel bottom = new JPanel();
        bottom.add(btnBack);

        panel.add(bottom, BorderLayout.SOUTH);

        add(panel);
    }

    private void loadApplications() {
        ApplicationDAO dao = new ApplicationDAO();

        try {
            ResultSet rs = dao.getUserApplications(currentUser.getId());

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("app_id"),
                        rs.getString("title"),
                        rs.getString("company"),
                        rs.getString("location"),
                        rs.getString("description"),
                        rs.getTimestamp("applied_at")
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Unable to load applications!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
