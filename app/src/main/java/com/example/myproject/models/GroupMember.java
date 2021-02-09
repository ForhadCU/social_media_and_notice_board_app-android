package com.example.myproject.models;

public class GroupMember {
    String gId;
    String gName;
    String uId;
    String uName;

    public GroupMember() {
    }

    public GroupMember(String gId, String gName, String uId, String uName) {
        this.gId = gId;
        this.gName = gName;
        this.uId = uId;
        this.uName = uName;
    }

    public String getgId() {
        return gId;
    }

    public void setgId(String gId) {
        this.gId = gId;
    }

    public String getgName() {
        return gName;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }
}
