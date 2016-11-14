package com.razanPardazesh.supervisor.model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Zikey on 13/11/2016.
 */

public class LocationData extends RealmObject {



    private long id=0;
    private double latitude=0.0;
    private double longitude=0.0;
    private double accuracy=0.0;
    private Date date;
    private int gpsStatus=0;
    private String provider="";
    private double speed=0.0;
    private int isMock=0;
    private String deviceID="";
    private long batteryCharge=0l;
    private int isSended=0;

    public int isSended() {
        return isSended;
    }

    public void setSended(int sended) {
        isSended = sended;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getGpsStatus() {
        return gpsStatus;
    }

    public void setGpsStatus(int gpsStatus) {
        this.gpsStatus = gpsStatus;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getIsMock() {
        return isMock;
    }

    public void setIsMock(int isMock) {
        this.isMock = isMock;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public long getBatteryCharge() {
        return batteryCharge;
    }

    public void setBatteryCharge(long batteryCharge) {
        this.batteryCharge = batteryCharge;
    }
}
