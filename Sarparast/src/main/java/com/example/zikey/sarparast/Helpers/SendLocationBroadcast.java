package com.example.zikey.sarparast.Helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.zikey.sarparast.MyLocationServices;

/**
 * Created by Zikey on 17/09/2016.
 */
public class SendLocationBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.sarparast.sendlocation")) {
            if (intent.hasExtra("MTG"))
                Log.e("MTG","mtg");
            MyLocationServices.startActionGetLocation(context);
         //   Toast.makeText(context, "Location is seted", Toast.LENGTH_SHORT).show();
        }
    }
}
