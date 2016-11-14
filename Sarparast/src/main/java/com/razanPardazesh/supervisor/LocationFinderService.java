package com.razanPardazesh.supervisor;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.razanPardazesh.supervisor.model.LocationData;
import com.razanPardazesh.supervisor.tools.LogWrapper;


import io.realm.Realm;

/**
 * Created by Zikey on 13/11/2016.
 */

public class LocationFinderService extends Service {

    private int currentButteryCharge = -1;
    private GoogleApiClient mGoogleApiClient;


    GoogleApiClient.ConnectionCallbacks conectionCallback = new GoogleApiClient.ConnectionCallbacks() {
        @Override
        public void onConnected(@Nullable Bundle bundle) {

        }

        @Override
        public void onConnectionSuspended(int i) {

        }
    };


    GoogleApiClient.OnConnectionFailedListener conectionFaildListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }
    };

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(conectionCallback)
                .addOnConnectionFailedListener(conectionFaildListener)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();

        butteryInfoReceiver.goAsync();

        return START_STICKY;
    }

    private void saveLocation(Location location) {
        if (location == null) return;


        try {


            Realm r = Realm.getDefaultInstance();
            try {

                r.beginTransaction();

                LocationData l = r.createObject(LocationData.class);
                l.setId(System.currentTimeMillis());
                l.setLatitude(location.getLatitude());
                l.setLongitude(location.getLongitude());
                l.setAccuracy(location.getAccuracy());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Calendar c = Calendar.getInstance();
                    l.setDate(c.getTime());
                }
                l.setSended(0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    l.setIsMock(location.isFromMockProvider() ? 1 : 0);
                } else {
                    l.setIsMock(0);
                }
                l.setSpeed(location.getSpeed());
                l.setProvider(location.getProvider());
                l.setGpsStatus(checkGPSstatus() ? 1 : 0);
                l.setDeviceID(Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
                l.setBatteryCharge(currentButteryCharge);
            } catch (Exception ex) {
                LogWrapper.loge("LocationFinderService_saveLocation :", ex);
            } finally {
                if (r != null && r.isClosed())
                    r.close();
            }
        }
        catch (Exception ex){
            LogWrapper.loge("LocationFinderService_getDefaultInstance :", ex);
        }


    }

    private Boolean checkGPSstatus() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private BroadcastReceiver butteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            currentButteryCharge = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        }
    };
}
