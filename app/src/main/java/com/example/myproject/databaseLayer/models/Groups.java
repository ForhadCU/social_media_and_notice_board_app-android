package com.example.myproject.databaseLayer.models;

public class Groups {
    private String gId;
    private String gName;
    private String gDesc;
    private String gDocId;

    public Groups() {
    }

    public Groups(String gId, String gName, String gDesc, String gDocId) {
        this.gId = gId;
        this.gName = gName;
        this.gDesc = gDesc;
        this.gDocId = gDocId;
    }

    public void setgDocId(String gDocId) {
        this.gDocId = gDocId;
    }

    public String getgDocId() {
        return gDocId;
    }

    public String getgId() {
        return gId;
    }

    public String getgName() {
        return gName;
    }

    public String getgDesc() {
        return gDesc;
    }
}
