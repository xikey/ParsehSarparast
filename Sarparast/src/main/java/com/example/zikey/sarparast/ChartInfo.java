package com.example.zikey.sarparast;

/**
 * Created by Zikey on 24/10/2016.
 */

public class ChartInfo {

    private  String sarparast_id;
    private  String groupName;
    private Long khalesR;
    private String groupID;
    private double sharePercent;


    public String getSarparast_id() {
        return sarparast_id;
    }

    public void setSarparast_id(String sarparast_id) {
        this.sarparast_id = sarparast_id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getKhalesR() {
        return khalesR;
    }

    public void setKhalesR(Long khalesR) {
        this.khalesR = khalesR;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public double getSharePercent() {
        return sharePercent;
    }

    public void setSharePercent(double sharePercent) {
        this.sharePercent = sharePercent;
    }
}
