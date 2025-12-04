package com.onlinejobportal.dao;

import com.onlinejobportal.model.Application;
import com.onlinejobportal.util.DBUtil;

import java.sql.*;

public class ApplicationDAO {

    // =============================
    // CHECK IF USER ALREADY APPLIED
    // =============================
    public boolean hasApplied(int jobId, int userId) {
        String sql = "SELECT id FROM applications WHERE job_id = ? AND user_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ps.setInt(2, userId);

            ResultSet rs = ps.executeQuery();
            return rs.next(); // if row exists → already applied

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    // =============================
    // APPLY TO A JOB
    // =============================
    public boolean applyToJob(int jobId, int userId, String resumeText) {

        String sql = "INSERT INTO applications(job_id, user_id, resume) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, jobId);
            ps.setInt(2, userId);
            ps.setString(3, resumeText);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ============================================
    // GET ALL JOB APPLICATIONS OF CURRENT USER
    // ============================================
    public ResultSet getUserApplications(int userId) throws Exception {

        Connection conn = DBUtil.getConnection();

        String sql =
                "SELECT a.id AS app_id, j.title, j.company, j.location, j.description, a.applied_at " +
                "FROM applications a " +
                "JOIN jobs j ON a.job_id = j.id " +
                "WHERE a.user_id = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);

        return ps.executeQuery();
    }
}
