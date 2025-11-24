package com.onlinejobportal.dao;

import com.onlinejobportal.model.User;
import com.onlinejobportal.util.DBUtil;
import java.sql.*;

public class UserDAO {
    public User authenticate(String u,String p){
        try(Connection c=DBUtil.getConnection()){
            PreparedStatement ps=c.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            ps.setString(1,u); ps.setString(2,p);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                User x=new User();
                x.setId(rs.getInt("id"));
                x.setUsername(u);
                x.setPassword(p);
                x.setFullName(rs.getString("full_name"));
                x.setEmail(rs.getString("email"));
                x.setRole(rs.getString("role"));
                return x;
            }
        }catch(Exception e){e.printStackTrace();}
        return null;
    }
}