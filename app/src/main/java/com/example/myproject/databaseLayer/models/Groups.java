package com.example.myproject.databaseLayer.models;

public class Groups {
    private String gId;
    private String gName;
    private String gDesc;

    public Groups() {
    }

    public Groups(String gId, String gName, String gDesc) {
        this.gId = gId;
        this.gName = gName;
        this.gDesc = gDesc;
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
