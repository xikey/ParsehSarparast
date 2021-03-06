package com.example.zikey.sarparast.Helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.example.zikey.sarparast.MyLocationServices;

/**
 * Created by Zikey on 17/09/2016.
 */
public class SendLocationBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager pm = (PowerManager) context.getSystemService(context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SARPARST");
        try {
            wl.acquire();
            if (intent.getAction().equals("com.sarparast.sendlocation")) {
                if (intent.hasExtra("MTG"))
                    Log.e("MTG","mtg");
                MyLocationServices.startActionGetLocation(context);
                //   Toast.makeText(context, "Location is seted", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Log.e("sarparast", "onConnected: "+ex.toString() );
        } finally {

            wl.release();
        }


    }
}
