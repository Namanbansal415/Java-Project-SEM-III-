package com.onlinejobportal.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/online_job_portal?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "Naman@415";   // <-- YOUR PASSWORD

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("DB Connected Successfully!");
            return conn;

        } catch (Exception e) {
            System.out.println("DATABASE CONNECTION FAILED:");
            e.printStackTrace();
            return null;
        }
    }
}
