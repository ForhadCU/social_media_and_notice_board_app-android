package com.example.myproject.databaseLayer.models;

import java.io.Serializable;

public class Data_Handler implements Serializable {
    private String currentUid;
    private String postTitle;
    private String desc;
    private String imageUri;
    private String gDocId;
    private String gName;

    public Data_Handler() {

    }

    public Data_Handler(String currentUid, String gDocId, String gName, String postTitle, String desc, String imageUri) {
        this.currentUid = currentUid;
        this.gDocId = gDocId;
        this.postTitle = postTitle;
        this.desc = desc;
        this.imageUri = imageUri;
        this.gName = gName;
    }

    public String getCurrentUid() {
        return currentUid;
    }

    public void setCurrentUid(String currentUid) {
        this.currentUid = currentUid;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getgDocId() {
        return gDocId;
    }

    public void setgDocId(String gDocId) {
        this.gDocId = gDocId;
    }

    public String getgName() {
        return gName;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }
}
