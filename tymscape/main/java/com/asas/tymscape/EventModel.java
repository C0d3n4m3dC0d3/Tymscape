package com.asas.tymscape;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class EventModel implements  Serializable{
    private String category;
    private String date;
    private String description;
    private String eid;
    private String ename;
    private String priority;
    private String time;
    private String uid;

    //default constructor
    public EventModel(){

    }

    //Getter Methods
    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getEid() {
        return eid;
    }

    public String getEname() {
        return ename;
    }

    public String getPriority() {
        return priority;
    }

    public String getTime() {
        return time;
    }

    public String getUid() {
        return uid;
    }


    //Setter methods
    public void setCategory(String category) {
        this.category = category;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}