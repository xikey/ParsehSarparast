package com.example.zikey.sarparast;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Zikey on 25/05/2016.
 */
public class G {

    public static int ID;


    public  static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }


    //____________________________GPS and NETWORK cheker____________________________________________
    public static  boolean gpsCheker(LocationManager lm){
        boolean gps_enabled = false;
        boolean network_enabled = false;

        boolean state = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if (gps_enabled&&network_enabled){
            return  state;
        }

        return state;
    }

//public static void  getLocation(LocationManager lm){
//
//    lm = (LocationManager)  G.context.getSystemService(LOCATION_SERVICE);
//
//    Criteria criteria = new Criteria();
//    String bestProvider = locationManager.getBestProvider(criteria, false);
//    Location location = locationManager.getLastKnownLocation(bestProvider);
//}




}
