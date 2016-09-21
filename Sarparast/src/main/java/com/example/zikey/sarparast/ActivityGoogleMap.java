package com.example.zikey.sarparast;

import android.*;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityGoogleMap extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

  private   String mLatitudeText="0.0";
  private   String mLongitudeText="0.0";

    double lastCustomerL;
    double lastCustomerW;

   private  Marker marker;

    private ArrayList<BazaryabInfo> pointha = new ArrayList<>();

   private String TempL;
   private String TempW;

    private int mmapReady=0;
    private int MY_PERMISSIONS_REQUEST_READ_GPS;


   private Location mLastLocation;
   private GoogleApiClient mGoogleApiClient;

    private boolean started=false;

    @Override
    protected void onStart() {

        mGoogleApiClient.connect();
        started = true;


        super.onStart();
    }

    private GoogleMap mMap;
    int ID;

    BitmapDescriptor pinStart;
    BitmapDescriptor pinEnd;
    BitmapDescriptor pin;

    private String state;
    public PreferenceHelper preferenceHelper;

    public ArrayList<BazaryabInfo> points;

    private    Double W;
    private    Double L;

    private    Double myW;
    private    Double myL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_path);

        MapsInitializer.initialize(getApplicationContext());


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }



        points = new ArrayList<>();
        points.clear();

        state = getIntent().getStringExtra("state");

        ID = getIntent().getIntExtra("Code", 0);



        preferenceHelper = new PreferenceHelper(this);

        if (state.equals("MasirHarkat")) {
            new BazaryabMovePath().execute();
        }

        if (state.equals("MasirSabt")) {
            new BazaryabSabtPath().execute();
        }

  if (state.equals("CustomerPastLocation")) {

      TempL = getIntent().getStringExtra("Lat");
      TempW = getIntent().getStringExtra("Long");

      if ((!TempL.equals("0")) && (!TempW.equals("0"))) {

        lastCustomerL = Double.parseDouble(TempL);
        lastCustomerW = Double.parseDouble(TempW);

      }
  }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {


        pinStart = BitmapDescriptorFactory.fromResource(R.drawable.start_pin);
        pinEnd = BitmapDescriptorFactory.fromResource(R.drawable.pin_end);
        pin = BitmapDescriptorFactory.fromResource(R.drawable.pin);


        mMap = googleMap;
        mmapReady = 1;

        if (mmapReady==1){


            mMap.setInfoWindowAdapter(new CustomInfoView());
//            if (state.equals("MyNearCustomers")) {
//
//                Log.e("OnConnected", "on Connected is run");
//
//             new  NearCustomersAsync().execute();
//
//            }




            if (state.equals("CustomerPastLocation")) {

                new LastCustomerLocationAsync().execute();
            }

            if (state.equals("NotOrdered")) {

                lastCustomerL = Double.parseDouble(getIntent().getStringExtra("Lat"));
                lastCustomerW = Double.parseDouble(getIntent().getStringExtra("Long"));

                new LastCustomerLocationAsync().execute();
            }
        }

//        mMap.moveCamera(CameraUpdateFactory.newLatLng(gholipoor));
//        float zoomLevel = 17.0f; //This goes up to 21
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gholipoor, zoomLevel));

        // Add a marker in Sydney and move the camera

//Log.e("Poits size","Poits size si "+points.size());
//        for (int i=0;i<points.size();i++){
//            L= Double.valueOf((((points.get(i)).get_L())));
//            W= Double.valueOf((((points.get(i)).get_W())));
//
//             Log.e("LatLong","L is "+L+"W is "+W);
//
//            MarkerOptions marker = new MarkerOptions();
//
//            marker.position(new LatLng(W,L));
//            mMap.addMarker(marker);
//        }

//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (state.equals("MyNearCustomers")) {

           getMyLocation();


        }
    }


    public class MyLocationAsync extends AsyncTask<Void, String, String> implements GoogleMap.InfoWindowAdapter {



        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }


        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }


        Boolean isonline = NetworkTools.isOnline(ActivityGoogleMap.this);
        ProgressDialog dialog;

        @Override
        protected void onPostExecute(String state) {

            CircleOptions circleOptions = new CircleOptions();



            for (int i = 0; i < points.size(); i++) {
                myW = Double.valueOf((((points.get(i)).get_L())));
                myL = Double.valueOf((((points.get(i)).get_W())));



                MarkerOptions marker = new MarkerOptions();

                LatLng latLng = new LatLng(myW, myL);

                marker.position(latLng);
                marker.icon(pin);
                marker.title("اطلاعاتی برای نمایش وجود ندارد ");

                //   polylineOptions.add(latLng).width(1).color(Color.parseColor("#000000"));

                if (latLng!=null){

                if (i == 0) {
                    marker.icon(pinStart);
                    circleOptions.center(latLng);
                    circleOptions.radius(500);

                    circleOptions.strokeColor(Color.parseColor("#d50000")).strokeWidth(2);
                    circleOptions.fillColor(Color.argb(20, 255, 0, 0));

                    float zoomLevel = 15.0f; //This goes up to 21

                    mMap.addCircle(circleOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                }

//                if (i == (points.size() - 1)) {
//                    marker.icon(pinEnd);
//                }
                //tsinfo  mMap.addPolyline(polylineOptions);
                if (marker!=null) {

                    mMap.addMarker(marker);

                      }
                }

            }
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... voids) {
            HashMap<String, Object> datas = new HashMap<String, Object>();
            HashMap<String, Object> datas2 = new HashMap<String, Object>();

            String w;
            String l;

            l = String.valueOf(myL);
            w = String.valueOf(myW);



            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            datas.put("ID", ID);




            if (isonline) {
                try {

                        BazaryabInfo point = new BazaryabInfo();
                        point.set_L(l);
                        point.set_W(w);
                        points.add(point);




                } catch (Exception e) {

                    e.printStackTrace();
                }
                return "Online";
            }
            return "NotOnline";

        }


    }









    public class BazaryabMovePath extends AsyncTask<Void, String, String> implements GoogleMap.InfoWindowAdapter {

//        private View view;
//
//        public BazaryabMovePath() {
//            view = getLayoutInflater().inflate(R.layout.custom_info_window,
//                    null);
//        }

            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }


            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }


            Boolean isonline = NetworkTools.isOnline(ActivityGoogleMap.this);
            ProgressDialog dialog;

            @Override
            protected void onPostExecute(String state) {

                //PolylineOptions polylineOptions = new PolylineOptions();
                CircleOptions circleOptions = new CircleOptions();

                for (int i = 0; i < points.size(); i++) {

                   L = Double.valueOf((((points.get(i)).get_L())));
                    W = Double.valueOf((((points.get(i)).get_W())));


                    MarkerOptions marker = new MarkerOptions();

                    LatLng latLng = new LatLng(W, L);

                    marker.position(latLng);
                    marker.icon(pin);
                    marker.title("اطلاعاتی برای نمایش وجود ندارد ");

                    //   polylineOptions.add(latLng).width(1).color(Color.parseColor("#000000"));


                    if (i == 0) {
                        marker.icon(pinStart);
                        circleOptions.center(latLng);
                        circleOptions.radius(500);

                        circleOptions.strokeColor(Color.parseColor("#d50000")).strokeWidth(2);
                        circleOptions.fillColor(Color.argb(20, 255, 0, 0));

                        float zoomLevel = 14.0f; //This goes up to 21
                        mMap.addCircle(circleOptions);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

                    }

                    if (i == (points.size() - 1)) {
                        marker.icon(pinEnd);
                    }
                    //tsinfo  mMap.addPolyline(polylineOptions);
                    mMap.addMarker(marker);

                }
            }

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, Object> datas = new HashMap<String, Object>();


                datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
                datas.put("ID", ID);



                if (isonline) {
                    try {
                        SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Navigation_ListOfVisitorPoints", datas).getProperty(0);

                        for (int i = 0; i < request2.getPropertyCount(); i++) {
                            SoapObject sp = (SoapObject) request2.getProperty(i);

                            BazaryabInfo point = new BazaryabInfo();


                            if (!TextUtils.isEmpty(NetworkTools.getSoapPropertyAsNullableString(sp, 0))) {
                                point.set_W(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                                point.set_L(NetworkTools.getSoapPropertyAsNullableString(sp, 1));

                                points.add(point);
                            }
                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                    return "Online";
                }
                return "NotOnline";

            }


        }

        @Override
        public void onBackPressed () {
            mMap.clear();

            super.onBackPressed();
        }

        @Override
        protected void onRestart () {
            mMap.clear();
            super.onRestart();
        }


        //__________________________________ASYNC MASIR SABT ___________________________________________


        public class BazaryabSabtPath extends AsyncTask<Void, String, String> {


            Boolean isonline = NetworkTools.isOnline(ActivityGoogleMap.this);
            ProgressDialog dialog;

            @Override
            protected void onPostExecute(String state) {


                if (state.equals("")){

                    new android.app.AlertDialog.Builder(ActivityGoogleMap.this)
                            .setCancelable(false)
                            .setTitle("خطا")
                            .setMessage("مکانی برای این بازاریاب روی نقشه ثبت نشده است")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setIcon(R.drawable.eror_dialog)
                            .show();

                }
else
                {
                //PolylineOptions polylineOptions = new PolylineOptions();

                for (int i = 0; i < points.size(); i++) {

                    if (points.get(i).get_L()!=null) {
                        L = Double.valueOf((((points.get(i)).get_L())));
                        W = Double.valueOf((((points.get(i)).get_W())));


                        MarkerOptions marker = new MarkerOptions();
                        LatLng latLng = new LatLng(W, L);

                        marker.position(latLng);

                        marker.icon(pin);
                        marker.title("اطلاعاتی برای نمایش وجود ندارد ");

                        //   polylineOptions.add(latLng).width(1).color(Color.parseColor("#000000"));


                        if (i == 0) {
                            marker.icon(pinStart);

                            float zoomLevel = 11.0f; //This goes up to 21
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

                        }

                        if (i == (points.size() - 1)) {
                            marker.icon(pinEnd);
                        }
                        //tsinfo  mMap.addPolyline(polylineOptions);
                        mMap.addMarker(marker);
                    }
                }

                }
            }

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, Object> datas = new HashMap<String, Object>();


                datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
                datas.put("ID", ID);



                if (isonline) {
                    try {
                        SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Navigation_ListOfVisitorMasirSabtPoints", datas).getProperty(0);

                        if (request2.getPropertyCount()<=0)
                        {

                            return "";


                        }
                        for (int i = 0; i < request2.getPropertyCount(); i++) {
                            SoapObject sp = (SoapObject) request2.getProperty(i);

                            Log.e("Points","Point is "+sp);

                            BazaryabInfo point = new BazaryabInfo();

                            if (!TextUtils.isEmpty(NetworkTools.getSoapPropertyAsNullableString(sp, 0))) {

                                   point.set_W(NetworkTools.getSoapPropertyAsNullableString(sp, 0));

                                   point.set_L(NetworkTools.getSoapPropertyAsNullableString(sp, 1));


                                   points.add(point);

                            }
                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                    return "Online";
                }
                return "NotOnline";

            }

        }



//    public class NearCustomersAsync extends AsyncTask<Void, String, String> {
//
//
//        Boolean isonline = NetworkTools.isOnline(ActivityGoogleMap.this);
//        ProgressDialog dialog;
//
//        @Override
//        protected void onPostExecute(String state) {
//
//            //PolylineOptions polylineOptions = new PolylineOptions();
//            Log.e("Poits size", "Poits size si " + points.size());
//            for (int i = 0; i < points.size(); i++) {
//                L = Double.valueOf((((points.get(i)).get_L())));
//                W = Double.valueOf((((points.get(i)).get_W())));
//
//                //  Log.e("LatLong","L is "+L+"W is "+W);
//
//                MarkerOptions marker = new MarkerOptions();
//                LatLng latLng = new LatLng(W, L);
//
//                marker.position(latLng);
//
//                marker.icon(pinStart );
//                marker.title("اطلاعاتی برای نمایش وجود ندارد ");
//
//                mMap.addMarker(marker);
//
//            }
//        }
//
//        @Override
//        protected void onPreExecute() {
//
//        }
//
//        @Override
//        protected String doInBackground(Void... voids) {
//            HashMap<String, Object> datas = new HashMap<String, Object>();
//
//
//            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
//            datas.put("L", myL);
//            datas.put("W", myW);
//
//            Log.e("myLocation","My location is "+myL+"  "+myW);
//
//            Log.e("AsyncCheker", "Async Is Run");
//
//            if (isonline) {
//                try {
//                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Navigation_MyNEar_Customers", datas).getProperty(0);
//
//                    for (int i = 0; i < request2.getPropertyCount(); i++) {
//                        SoapObject sp = (SoapObject) request2.getProperty(i);
//                        //  Log.e("tttttttttttt", "" + sp);
//
//                        BazaryabInfo point = new BazaryabInfo();
//
//                        point.set_W(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
//                        point.set_L(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
//
//                        points.add(point);
//                    }
//
//                } catch (Exception e) {
//                    //    Log.e("iiiiiii", "connot read Soap");
//                    e.printStackTrace();
//                }
//                return "Online";
//            }
//            return "NotOnline";
//
//        }
//
//    }






    public class LastCustomerLocationAsync extends AsyncTask<Void, String, String> implements GoogleMap.InfoWindowAdapter {

        private  BazaryabInfo location = new BazaryabInfo();
        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }


        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }


        Boolean isonline = NetworkTools.isOnline(ActivityGoogleMap.this);


        @Override
        protected void onPostExecute(String state) {
if (mmapReady==1) {


            CircleOptions circleOptions = new CircleOptions();


                MarkerOptions marker = new MarkerOptions();

                LatLng latLng = new LatLng(lastCustomerL, lastCustomerW);

            if (latLng!=null) {
                marker.position(latLng);

                marker.icon(pin);
                marker.title("اطلاعاتی برای نمایش وجود ندارد ");

                //   polylineOptions.add(latLng).width(1).color(Color.parseColor("#000000"));

                marker.icon(pinStart);

                circleOptions.center(latLng);
                circleOptions.radius(100);
                circleOptions.strokeColor(Color.argb(15, 0, 0, 0)).strokeWidth(2);
                circleOptions.fillColor(Color.argb(10, 255, 0, 0));

                float zoomLevel = 15.0f; //This goes up to 21

                mMap.addCircle(circleOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                //
                if (marker != null) {

                    mMap.addMarker(marker);

                }
            }
}
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... voids) {


            if (isonline) {
                try {
//

                } catch (Exception e) {

                    e.printStackTrace();
                }
                return "Online";
            }
            return "NotOnline";

        }


    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    private void getMyLocation(){

        requestPermission();

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

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {

            myL = mLastLocation.getLatitude();
            myW = mLastLocation.getLongitude();


            mLongitudeText = String.valueOf(mLastLocation.getLatitude());
            mLatitudeText = String.valueOf(mLastLocation.getLongitude());


            new MyLocationAsync().execute();

            new  NearCustomersAsync().execute();

        } else {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("خطا")
                    .setIcon(R.drawable.eror_dialog)
                    .setCancelable(false)
                    .setMessage("موقعیت فعلی شما قابل شناسایی نمیباشد. روشن بودن gps خود را کنترل نمایید")
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();

                        }
                    })
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            mGoogleApiClient.connect();
                          getMyLocation();
                        }
                    }).create().show();
        }
    };




    public class NearCustomersAsync extends AsyncTask<Void, String, String> {


        Boolean isonline = NetworkTools.isOnline(ActivityGoogleMap.this);
        ProgressDialog dialog;


        @Override
        protected void onPostExecute(String state) {


            for (int i = 0; i < pointha.size(); i++) {
                L = Double.valueOf((((pointha.get(i)).get_L())));
                W = Double.valueOf((((pointha.get(i)).get_W())));

                MarkerOptions marker = new MarkerOptions();
                LatLng latLng = new LatLng(W, L);

                marker.position(latLng);

                marker.icon(pin);
                marker.title(pointha.get(i).get_Code());

                //   polylineOptions.add(latLng).width(1).color(Color.parseColor("#000000"));


                //tsinfo  mMap.addPolyline(polylineOptions);
                mMap.addMarker(marker);

            }
            mMap.setInfoWindowAdapter(new CustomInfoView());
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... voids) {
            HashMap<String, Object> datas = new HashMap<String, Object>();

            Log.e("Location","L and W is "+mLatitudeText+" ...  "+mLongitudeText);


            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            datas.put("L", mLongitudeText);
            datas.put("W", mLatitudeText);


            if (isonline) {
                try {

                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Navigation_MyNEar_Customers", datas).getProperty(0);

                    Log.e("customers"," "+request2);

                    for (int i = 0; i < request2.getPropertyCount(); i++) {
                        SoapObject sp = (SoapObject) request2.getProperty(i);


                        BazaryabInfo point = new BazaryabInfo();

                        point.set_L(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                        point.set_W(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                        point.set_Code(NetworkTools.getSoapPropertyAsNullableString(sp, 2));
                        point.set_Name(NetworkTools.getSoapPropertyAsNullableString(sp, 3));
                        point.set_Tel(NetworkTools.getSoapPropertyAsNullableString(sp, 4));

                        pointha.add(point);
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }
                return "Online";
            }
            return "NotOnline";

        }

    }

    public   int requestPermission( ){

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(ActivityGoogleMap.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityGoogleMap.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(ActivityGoogleMap.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_GPS);

                Log.e("Permission","Persmisn is "+MY_PERMISSIONS_REQUEST_READ_GPS);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        return  MY_PERMISSIONS_REQUEST_READ_GPS;


    }
private class  CustomInfoView implements GoogleMap.InfoWindowAdapter{

    @Override
    public View getInfoWindow(Marker marker) {

        if (marker == null && TextUtils.isEmpty(marker.getTitle()))
            return null;

        BazaryabInfo currentPoint = null;

        for (int i = 0;i<pointha.size();i++){
            if (marker.getTitle().compareTo(pointha.get(i).get_Code()) == 0)
                currentPoint = pointha.get(i);
        }

        if (currentPoint == null)
            return null;

        View view = getLayoutInflater().inflate(R.layout.custom_pin_info,
                null);

        TextView txtCode =(TextView) view.findViewById(R.id.txtCode);
        TextView txtName =(TextView) view.findViewById(R.id.txtName);
        TextView txtTell =(TextView) view.findViewById(R.id.txtTell);
        txtCode.setText(currentPoint.get_Code());
        txtName.setText(currentPoint.get_Name());
        txtTell.setText(currentPoint.get_Tel());

        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}

}




