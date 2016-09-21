package com.example.zikey.sarparast;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.ksoap2.serialization.SoapObject;

import java.util.Calendar;
import java.util.HashMap;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyLocationServices extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_GETLOCATION = "getLocation";
//    private static final String ACTION_BAZ = "com.example.zikey.sarparast.action.BAZ";

    // TODO: Rename parameters
    private String EXTRA_LATITUDE = "0";
    private String EXTRA_LONGITUDE = "0";
    private String tokenID;
    SendUserLocationAsync locationAsync = null;
    private Boolean isonline = false;

    private Location mCurrentLocation;
    private LocationRequest locationRequest;
    private Location mLastLocation;


    private GoogleApiClient mGoogleApiClient;

    public PreferenceHelper preferenceHelper;

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;


    public MyLocationServices() {
        super("MyLocationServices");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionGetLocation(Context context) {
        Intent intent = new Intent(context, MyLocationServices.class);

        intent.setAction(ACTION_GETLOCATION);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
//    public static void startActionBaz(Context context, String param1, String param2) {
//        Intent intent = new Intent(context, MyLocationServices.class);
//        intent.setAction(ACTION_BAZ);
//        intent.putExtra(EXTRA_LATITUDE, param1);
//        intent.putExtra(EXTRA_LONGITUDE, param2);
//        context.startService(intent);
//    }
    @Override
    protected void onHandleIntent(Intent intent) {

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(com.google.android.gms.location.LocationServices.API)
                .build();

        mGoogleApiClient.connect();

//        final String param1 = intent.getStringExtra(EXTRA_LATITUDE);
//        final String param2 = intent.getStringExtra(EXTRA_LONGITUDE);

        preferenceHelper = new PreferenceHelper(getApplicationContext());
        isonline = NetworkTools.isOnline(getApplicationContext());
        tokenID = preferenceHelper.getString(preferenceHelper.TOKEN_ID);

        handleActionGetLocation();

    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionGetLocation() {


        getLocation();

        // TODO: Handle action Foo
        //   throw new UnsupportedOperationException("Not yet implemented");
    }


    private void runAsync() {

        if (locationAsync == null) {
            locationAsync = new SendUserLocationAsync();
            locationAsync.execute();

        } else {
            return;
        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
//        startLocationUpdate();
    }


    private void getLocation() {
        Log.e("OnConnected", "on Connected is run");

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }

        mLastLocation = com.google.android.gms.location.LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        com.google.android.gms.location.MyLocationServices.FusedLocationApi.requestLocationUpdates()
        Log.e("LastLocation", "last location is  " + mLastLocation);
        if (mLastLocation != null) {


            EXTRA_LATITUDE = String.valueOf(mLastLocation.getLatitude());
            EXTRA_LONGITUDE = String.valueOf(mLastLocation.getLongitude());

            setAlarmManager(getApplicationContext(), 5);


            runAsync();


        } else {

            EXTRA_LATITUDE = "0";
            EXTRA_LONGITUDE = "0";

            setAlarmManager(getApplicationContext(), 1);


        }

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

//        mCurrentLocation = location;
//        notifyLocationChanged(mLastLocation, mCurrentLocation);

    }

    private class SendUserLocationAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            HashMap<String, Object> datas = new HashMap<>();

            datas.put("TokenID", tokenID);
            datas.put("L", EXTRA_LATITUDE);
            datas.put("W", EXTRA_LONGITUDE);


            if (isonline) {

                try {
                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Sarparast_Navigattion", datas);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            return null;
        }
    }

    public void setAlarmManager(Context context, int repeatTime) {

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent();
        intent.putExtra("MTG", "MTG");
        intent.setAction("com.sarparast.sendlocation");

        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, repeatTime);

        alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);


    }

//    private  void startLocationUpdate(){
//        MyLocationServices.FusedLocationApi.requestLocationUpdates(
//                mGoogleApiClient, mLocationRequest, this);
//    }

//    private void notifyLocationChanged(Location pastLocation, Location lastLocation) {
//        float distance = pastLocation.distanceTo(lastLocation);
//        if (distance < 10) {
//            Log.e("Distance", "distance is " + distance);
//        }
//    }

//    private void startLocationUpdate() {
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//locationRequest = new LocationRequest();
//        locationRequest.setPriority()
// LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
//    }

}
