package com.example.zikey.sarparast;

/**
 * Created by Zikey on 17/10/2016.
 */

public class ManagmentInfo implements Comparable<ManagmentInfo> {
    private String code;
    private int canRegister;
    private int canOrder;
    private int distance;
    private int canReadAllCustomer;
    private int CanUpdate;
    private int forceToLoguot;
    private int turner;

    public int getTurner() {
        return turner;
    }

    public String getCode() {
        return code;
    }

    public int getCanRegister() {
        return canRegister;
    }

    public int getCanOrder() {
        return canOrder;
    }

    public int getCanReadAllCustomer() {
        return canReadAllCustomer;
    }

    public int getCanUpdate() {
        return CanUpdate;
    }

    public int getForceToLoguot() {
        return forceToLoguot;
    }

    public int getDistance() {
        return distance;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCanReadAllCustomer(int canReadAllCustomer) {
        this.canReadAllCustomer = canReadAllCustomer;
    }

    public void setCanUpdate(int canUpdate) {
        CanUpdate = canUpdate;
    }

    public void setForceToLoguot(int forceToLoguot) {
        this.forceToLoguot = forceToLoguot;
    }

    public void setCanRegister(int canRegister) {
        this.canRegister = canRegister;
    }

    public void setTurner(int turner) {
        this.turner = turner;
    }

    public void setCanOrder(int canOrder) {
        this.canOrder = canOrder;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(ManagmentInfo another) {
        if (getCanRegister() < another.getCanRegister())
            return -1;

        if (getCanRegister() > another.getCanRegister())
            return 1;

        if (getDistance() < another.getDistance())
            return -1;

        if (getDistance() > another.getDistance())
            return 1;

        if (getCanOrder() < another.getCanOrder())
            return -1;

        if (getCanOrder() > another.getCanOrder())
            return 1;

        if (getCanUpdate() < another.getCanUpdate())
            return -1;

        if (getCanUpdate() > another.getCanUpdate())
            return 1;

        if (getCanReadAllCustomer() < another.getCanReadAllCustomer())
            return -1;

        if (getCanReadAllCustomer() > another.getCanReadAllCustomer())
            return 1;

        if (getForceToLoguot() < another.getForceToLoguot())
            return -1;

          if (getForceToLoguot() > another.getForceToLoguot())
            return 1;



        return 0;
    }
}
