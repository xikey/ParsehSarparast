package com.razanPardazesh.supervisor.tools;

import android.util.Log;

/**
 * Created by Torabi on 10/9/2016.
 */

public class LogWrapper {

    private static String DEBUG_TAG = "com.razanpardazesh";
    private static boolean isDebugable = true;

    public static void loge(String call, Throwable e) {
        if (!isDebugable)
            return;
        Log.e(DEBUG_TAG, call, e);
    }

}
