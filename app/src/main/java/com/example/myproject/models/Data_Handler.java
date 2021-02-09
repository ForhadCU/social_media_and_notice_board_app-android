package com.example.myproject.models;

import java.io.Serializable;

public class Data_Handler implements Serializable {
    private String currentUid;
    private String postTitle;
    private String desc;
    private String imageUri;
    private String gId;
    private String gName;

    public Data_Handler() {

    }

    public Data_Handler(String currentUid, String gId, String gName, String postTitle, String desc, String imageUri) {
        this.currentUid = currentUid;
        this.gId = gId;
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
}
