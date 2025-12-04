package com.onlinejobportal.dao;

import com.onlinejobportal.model.User;
import com.onlinejobportal.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    public boolean registerUser(User user) {
        String sql = "INSERT INTO users(username, password, full_name, email, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getRole());

            System.out.println("Executing Signup Query...");
            int rows = ps.executeUpdate();
            System.out.println("Rows inserted: " + rows);

            return rows > 0;

        } catch (Exception e) {
            System.out.println("Signup Error:");
            e.printStackTrace();
            return false;
        }
    }


    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            System.out.println("Executing Login Query...");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setFullName(rs.getString("full_name"));
                u.setEmail(rs.getString("email"));
                u.setRole(rs.getString("role"));
                return u;
            }

        } catch (Exception e) {
            System.out.println("Login Error:");
            e.printStackTrace();
        }

        return null;
    }
}
