package com.onlinejobportal.dao;

import com.onlinejobportal.model.Job;
import com.onlinejobportal.util.DBUtil;
import java.sql.*;
import java.util.*;

public class JobDAO {
    public List<Job> getAllJobs(){
        List<Job> L=new ArrayList<>();
        try(Connection c=DBUtil.getConnection()){
            ResultSet rs=c.prepareStatement("SELECT * FROM jobs").executeQuery();
            while(rs.next()){
                Job j=new Job();
                j.setId(rs.getInt("id"));
                j.setTitle(rs.getString("title"));
                j.setCompany(rs.getString("company"));
                j.setLocation(rs.getString("location"));
                j.setDescription(rs.getString("description"));
                j.setPostedBy(rs.getInt("posted_by"));
                L.add(j);
            }
        }catch(Exception e){e.printStackTrace();}
        return L;
    }
}