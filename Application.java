package com.onlinejobportal.model;

import java.sql.Timestamp;

public class Application {
    private int id;
    private int jobId;
    private int userId;
    private String resume;
    private String status;
    private Timestamp appliedAt;
    public Application(){}
    public int getId(){return id;}
    public void setId(int i){this.id=i;}
    public int getJobId(){return jobId;}
    public void setJobId(int j){this.jobId=j;}
    public int getUserId(){return userId;}
    public void setUserId(int u){this.userId=u;}
    public String getResume(){return resume;}
    public void setResume(String r){this.resume=r;}
    public String getStatus(){return status;}
    public void setStatus(String s){this.status=s;}
    public Timestamp getAppliedAt(){return appliedAt;}
    public void setAppliedAt(Timestamp a){this.appliedAt=a;}
}