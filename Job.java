package com.onlinejobportal.model;

import java.sql.Timestamp;

public class Job {
    private int id;
    private String title;
    private String company;
    private String location;
    private String description;
    private int postedBy;
    private Timestamp postedAt;

    public Job(){}
    public int getId(){return id;}
    public void setId(int id){this.id=id;}
    public String getTitle(){return title;}
    public void setTitle(String t){this.title=t;}
    public String getCompany(){return company;}
    public void setCompany(String c){this.company=c;}
    public String getLocation(){return location;}
    public void setLocation(String l){this.location=l;}
    public String getDescription(){return description;}
    public void setDescription(String d){this.description=d;}
    public int getPostedBy(){return postedBy;}
    public void setPostedBy(int p){this.postedBy=p;}
    public Timestamp getPostedAt(){return postedAt;}
    public void setPostedAt(Timestamp t){this.postedAt=t;}
}