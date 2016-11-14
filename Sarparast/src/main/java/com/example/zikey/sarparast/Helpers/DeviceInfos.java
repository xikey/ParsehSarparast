package com.example.zikey.sarparast.Helpers;

import android.os.Build;
import android.text.format.DateFormat;

/**
 * Created by Zikey on 01/11/2016.
 */

public class DeviceInfos {

    public static String getDeviceModel(){
        return Build.BRAND+" "+Build.MODEL;
    }

    public static String getAndroidVersion(){

        return String.valueOf("Android "+Build.VERSION.RELEASE);
    }



}


