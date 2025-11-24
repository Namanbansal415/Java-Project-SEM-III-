package com.onlinejobportal.dao;

import com.onlinejobportal.model.Application;
import com.onlinejobportal.util.DBUtil;
import java.sql.*;

public class ApplicationDAO {
    public boolean apply(Application a){
        try(Connection c=DBUtil.getConnection()){
            PreparedStatement ps=c.prepareStatement("INSERT INTO applications(job_id,user_id,resume) VALUES(?,?,?)");
            ps.setInt(1,a.getJobId());
            ps.setInt(2,a.getUserId());
            ps.setString(3,a.getResume());
            return ps.executeUpdate()>0;
        }catch(Exception e){e.printStackTrace();}
        return false;
    }
}