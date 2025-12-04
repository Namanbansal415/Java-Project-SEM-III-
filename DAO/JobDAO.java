package com.onlinejobportal.dao;

import com.onlinejobportal.model.Job;
import com.onlinejobportal.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobDAO {

    public boolean postJob(Job job) {
        String sql = "INSERT INTO jobs(title, company, location, description, posted_by, approved, posted_at) " +
                     "VALUES(?,?,?,?,?,0,CURRENT_TIMESTAMP)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, job.getTitle());
            ps.setString(2, job.getCompany());
            ps.setString(3, job.getLocation());
            ps.setString(4, job.getDescription());
            ps.setInt(5, job.getPostedBy());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet gk = ps.getGeneratedKeys()) {
                    if (gk.next()) job.setId(gk.getInt(1));
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Job> getJobsByEmployer(int employerId) {
        List<Job> list = new ArrayList<>();
        String sql = "SELECT id, title, company, location, description, posted_by, posted_at " +
                     "FROM jobs WHERE posted_by = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, employerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Job j = new Job();
                    j.setId(rs.getInt("id"));
                    j.setTitle(rs.getString("title"));
                    j.setCompany(rs.getString("company"));
                    j.setLocation(rs.getString("location"));
                    j.setDescription(rs.getString("description"));
                    j.setPostedBy(rs.getInt("posted_by"));
                    j.setPostedAt(rs.getTimestamp("posted_at"));
                    list.add(j);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Job> getApprovedJobs() {
        List<Job> list = new ArrayList<>();
        String sql = "SELECT id, title, company, location, description, posted_by, posted_at FROM jobs WHERE approved = 1";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Job j = new Job();
                j.setId(rs.getInt("id"));
                j.setTitle(rs.getString("title"));
                j.setCompany(rs.getString("company"));
                j.setLocation(rs.getString("location"));
                j.setDescription(rs.getString("description"));
                j.setPostedBy(rs.getInt("posted_by"));
                j.setPostedAt(rs.getTimestamp("posted_at"));
                list.add(j);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Job getJobById(int jobId) {
        String sql = "SELECT id, title, company, location, description, posted_by, posted_at FROM jobs WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, jobId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Job j = new Job();
                    j.setId(rs.getInt("id"));
                    j.setTitle(rs.getString("title"));
                    j.setCompany(rs.getString("company"));
                    j.setLocation(rs.getString("location"));
                    j.setDescription(rs.getString("description"));
                    j.setPostedBy(rs.getInt("posted_by"));
                    j.setPostedAt(rs.getTimestamp("posted_at"));
                    return j;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
