package com.example.zikey.sarparast;

import java.util.ArrayList;

/**
 * Created by Zikey on 18/07/2016.
 */
public class BazaryabInfo {
    private  String _Name;
    private  String _Code;
    private  String _ID;
    private  String _Time;

    private  String _W;
    private  String _L;

    private String _CustomerCode;
    private String _CustomerName;
    private String _Tel;
    private String _Mobile;
    private String _Address;

    private String _Total;
    private String _NotOrdered;

    private String _CustomerLat;
    private String _CustomerLong;

    public String get_CustomerLat() {
        return _CustomerLat;
    }

    public void set_CustomerLat(String _CustomerLat) {
        this._CustomerLat = _CustomerLat;
    }

    public String get_CustomerLong() {
        return _CustomerLong;
    }

    public void set_CustomerLong(String _CustomerLong) {
        this._CustomerLong = _CustomerLong;
    }

    private ArrayList<ActivityGoogleMap.NavigationWrapper> wrappers = new ArrayList<>();

    public String get_Name() {
        return _Name;
    }

    public String get_Code() {
        return _Code;
    }

    public String get_ID() {
        return _ID;
    }

    public String get_Time() {
        return _Time;
    }

    public String get_W() {
        return _W;
    }

    public String get_L() {
        return _L;
    }

    public String get_CustomerCode() {
        return _CustomerCode;
    }

    public String get_CustomerName() {
        return _CustomerName;
    }

    public String get_Tel() {
        return _Tel;
    }

    public String get_Mobile() {
        return _Mobile;
    }

    public String get_Address() {
        return _Address;
    }

    public String get_Total() {
        return _Total;
    }

    public String get_NotOrdered() {
        return _NotOrdered;
    }

    public void set_Name(String _Name) {
        this._Name = _Name;
    }

    public void set_Code(String _Code) {
        this._Code = _Code;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public void set_Time(String _Time) {
        this._Time = _Time;
    }

    public void set_W(String _W) {
        this._W = _W;
    }

    public void set_L(String _L) {
        this._L = _L;
    }

    public void set_CustomerCode(String _CustomerCode) {
        this._CustomerCode = _CustomerCode;
    }

    public void set_CustomerName(String _CustomerName) {
        this._CustomerName = _CustomerName;
    }

    public void set_Tel(String _Tel) {
        this._Tel = _Tel;
    }

    public void set_Mobile(String _Mobile) {
        this._Mobile = _Mobile;
    }

    public void set_Address(String _Address) {
        this._Address = _Address;
    }

    public void set_Total(String _Total) {
        this._Total = _Total;
    }

    public void set_NotOrdered(String _NotOrdered) {
        this._NotOrdered = _NotOrdered;
    }

    public ArrayList<ActivityGoogleMap.NavigationWrapper> getWrappers() {
        return wrappers;
    }

    public void setWrappers(ArrayList<ActivityGoogleMap.NavigationWrapper> wrappers) {
        this.wrappers = wrappers;
    }
}
