package com.example.zikey.sarparast;

import android.*;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.Convertor;
import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;
import com.google.android.gms.ads.formats.NativeAd;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class ActivityGoogleMap extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, DatePickerDialog.OnDateSetListener {

    private String mLatitudeText = "0.0";
    private String mLongitudeText = "0.0";

    double lastCustomerL;
    double lastCustomerW;
    private TextView txtAverage;

    private Button btnDate;
    private ImageView imgDetails;

    private int swich;

    private Marker marker;

    private ArrayList<BazaryabInfo> pointha = new ArrayList<>();
    private ArrayList<BazaryabInfo> customersDistances = null;

    private String TempL;
    private String TempW;

    private PersianCalendar persianCalendar = null;
    private DatePickerDialog datePickerDialog = null;
    private BazaryabSabtPath bazaryabSabtPathAsync = null;

    private int mmapReady = 0;
    private int MY_PERMISSIONS_REQUEST_READ_GPS;


    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private String thisDate = "";

    private RelativeLayout lyAdapter;

    private RecyclerView recyclerView;
    private CustomersDistancesAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private boolean started = false;

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
    BitmapDescriptor customerPin;

    private String state;
    public PreferenceHelper preferenceHelper;

    public ArrayList<BazaryabInfo> points;

    private Double W;
    private Double L;

    private Double myW;
    private Double myL;

    private RelativeLayout lyProgress;
    private RelativeLayout lyMap;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_path);

        btnDate = (Button) findViewById(R.id.btnDate);
        lyProgress = (RelativeLayout) findViewById(R.id.lyProgress);
        lyMap = (RelativeLayout) findViewById(R.id.lyMap);

        btnDate = (Button) findViewById(R.id.btnDate);
        imgDetails = (ImageView) findViewById(R.id.imgDetails);

        btnDate.setVisibility(View.GONE);
        imgDetails.setVisibility(View.GONE);

        imgBack = (ImageView) findViewById(R.id.imgBack);

        lyAdapter = (RelativeLayout) findViewById(R.id.lyAdapter);
        lyAdapter.setVisibility(View.GONE);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyAdapter.setVisibility(View.GONE);
                lyMap.setVisibility(View.VISIBLE);
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPersianCalender(persianCalendar, datePickerDialog);
            }
        });

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
            initRecycleView();
            initSearchAction();

            lyProgress.setVisibility(View.VISIBLE);
            runBazaryabSabtPathAsync(thisDate);

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

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        customersDistances.clear();
        String month;
        String day;
        if (monthOfYear < 10) month = "0" + (monthOfYear + 1);
        else month = "" + (monthOfYear + 1);

        if (dayOfMonth < 10) day = "0" + (dayOfMonth);
        else day = "" + (dayOfMonth);


        thisDate = year + "/" + month + "/" + day;
        mMap.clear();
        runBazaryabSabtPathAsync(thisDate);

        btnDate.setText(thisDate);

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
        customerPin = BitmapDescriptorFactory.fromResource(R.drawable.customerpin);


        mMap = googleMap;
        mmapReady = 1;

        if (mmapReady == 1) {


            mMap.setInfoWindowAdapter(new CustomInfoView());


            if (state.equals("CustomerPastLocation")) {

                new LastCustomerLocationAsync().execute();
            }

            if (state.equals("NotOrdered")) {

                lastCustomerL = Double.parseDouble(getIntent().getStringExtra("Lat"));
                lastCustomerW = Double.parseDouble(getIntent().getStringExtra("Long"));

                new LastCustomerLocationAsync().execute();
            }
        }


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

                if (latLng != null) {

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
            HashMap<String, Object> datas = new HashMap<String, Object>();
            HashMap<String, Object> datas2 = new HashMap<String, Object>();

            if (points != null && points.size() > 0) {
                points.clear();
            }

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
    public void onBackPressed() {
        mMap.clear();

        super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        mMap.clear();
        super.onRestart();
    }


    //__________________________________ASYNC MASIR SABT ___________________________________________


    public class BazaryabSabtPath extends AsyncTask<Void, String, String> {


        MarkerOptions marker = new MarkerOptions();
        MarkerOptions customerMarker = new MarkerOptions();

        PolylineOptions options;

        Double customerLat;
        Double customerLong;


        private ArrayList<BazaryabInfo> customersPoints = new ArrayList<>();


        Boolean isonline = NetworkTools.isOnline(ActivityGoogleMap.this);
        ProgressDialog dialog;

        @Override
        protected void onPostExecute(String state) {

            btnDate.setVisibility(View.VISIBLE);
            imgDetails.setVisibility(View.VISIBLE);

            imgDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lyProgress.setVisibility(View.GONE);
                    lyMap.setVisibility(View.GONE);
                    lyAdapter.setVisibility(View.VISIBLE);
                }
            });

            bazaryabSabtPathAsync = null;

            if (points.size() <= 0) {
                new android.app.AlertDialog.Builder(ActivityGoogleMap.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("در این تاریخ مکانی برای این بازاریاب ثبت نشده است")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }

            if (state.equals("")) {

                new android.app.AlertDialog.Builder(ActivityGoogleMap.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("در این تاریخ مکانی برای این بازاریاب ثبت نشده است")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();

            } else {


                for (int i = 0; i < points.size(); i++) {

                    BazaryabInfo distanc = new BazaryabInfo();
                    if (points.get(i).get_L() != null) {
                        L = Double.valueOf((((points.get(i)).get_L())));
                        W = Double.valueOf((((points.get(i)).get_W())));

                        customerLong = Double.valueOf(points.get(i).get_CustomerLat());
                        customerLat = Double.valueOf(points.get(i).get_CustomerLong());

                        LatLng latLng = new LatLng(W, L);

                        if (customerLong != 0) {
                            LatLng customerLatLng = new LatLng(customerLat, customerLong);
                            LatLng latLng1 = new LatLng(W, L);
                            customerMarker.position(customerLatLng);
                            options = new PolylineOptions();

                            options.width(2).color(Color.BLACK).geodesic(true);
                            options.add(latLng1);
                            options.add(customerLatLng);
                            mMap.addPolyline(options);
                            float[] results = new float[1];
                            Location.distanceBetween(customerLat, customerLong, W, L, results);

                            distanc.set_Name(points.get(i).get_Name());
                            distanc.set_Code(points.get(i).get_Code());
                            //    Double distanceMeter = CalculationByDistance(latLng1, customerLatLng);

//                            DecimalFormat twoDForm = new DecimalFormat("#");


                            distanc.set_Distance(Double.valueOf((Math.round(results[0]))));

                            customersDistances.add(distanc);

                        }

                        marker.position(latLng);

                        marker.icon(pin);
                        marker.title(points.get(i).get_Code());

                        customerMarker.icon(customerPin);
                        customerMarker.title(points.get(i).get_Code());

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
                        if (customerMarker.getPosition() != null) {
                            mMap.addMarker(customerMarker);
                        }
                    }

                    if (adapter != null || customersDistances != null) {

                        Collections.sort(customersDistances);
                        adapter.setItem(customersDistances);
                        txtAverage.setText("  میانگین مسافت: " + (int) getDistanceAverage(customersDistances) + " متر ");
                    }
                    swich = 2;
                    mMap.setInfoWindowAdapter(new CustomInfoView());
                    lyMap.setVisibility(View.VISIBLE);
                    lyProgress.setVisibility(View.VISIBLE);
                }

            }
        }

        @Override
        protected void onPreExecute() {

            lyMap.setVisibility(View.GONE);
            customersDistances = new ArrayList<>();
        }

        @Override
        protected String doInBackground(Void... voids) {

            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            datas.put("ID", ID);
            datas.put("thisDate", thisDate);

            if (isonline) {
                try {
                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Navigation_ListOfVisitorMasirSabtPoints", datas).getProperty(0);

                    if (request2.getPropertyCount() <= 0) {

                        return "";

                    }
                    for (int i = 0; i < request2.getPropertyCount(); i++) {
                        SoapObject sp = (SoapObject) request2.getProperty(i);

                        Log.e("Points", "Point is " + sp);

                        BazaryabInfo point = new BazaryabInfo();
                        BazaryabInfo customerPoint = new BazaryabInfo();

                        if (!TextUtils.isEmpty(NetworkTools.getSoapPropertyAsNullableString(sp, 0))) {
                            String value = (NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                            if (!value.equals("0")) {

                                point.set_W(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                                point.set_L(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                                point.set_Code(NetworkTools.getSoapPropertyAsNullableString(sp, 2));
                                point.set_Name(NetworkTools.getSoapPropertyAsNullableString(sp, 3));
                                point.set_Tel(NetworkTools.getSoapPropertyAsNullableString(sp, 4));
                                point.setWrappers(NavigationWrapper.getNavigationWrappers(NetworkTools.getSoapPropertyAsNullableString(sp, 7)));
                                point.set_CustomerLat(NetworkTools.getSoapPropertyAsNullableString(sp, 8));
                                point.set_CustomerLong(NetworkTools.getSoapPropertyAsNullableString(sp, 9));

                                points.add(point);

                            }

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

    public static class NavigationWrapper {
        public String title;
        public String value;

        public NavigationWrapper(String jsonStr) throws JSONException {

            JSONObject obj = new JSONObject(jsonStr);

            this.title = obj.getString("t");
            this.value = obj.getString("v");
        }

        public static ArrayList<NavigationWrapper> getNavigationWrappers(String jsonArr) throws JSONException {
            ArrayList<NavigationWrapper> output = new ArrayList<>();

            JSONArray arr = new JSONArray(jsonArr);

            for (int i = 0; i < arr.length(); i++) {
                NavigationWrapper n = new NavigationWrapper(arr.getString(i));
                output.add(n);
            }

            return output;
        }
    }


    public class LastCustomerLocationAsync extends AsyncTask<Void, String, String> implements GoogleMap.InfoWindowAdapter {

        private BazaryabInfo location = new BazaryabInfo();

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
            if (mmapReady == 1) {


                CircleOptions circleOptions = new CircleOptions();


                MarkerOptions marker = new MarkerOptions();

                LatLng latLng = new LatLng(lastCustomerL, lastCustomerW);

                if (latLng != null) {
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


    private void getMyLocation() {

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

            new NearCustomersAsync().execute();

        } else {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("خطا")
                    .setIcon(R.drawable.eror_dialog)
                    .setCancelable(false)
                    .setMessage("موقعیت فعلی شما قابل شناسایی نمیباشد. روشن بودن gps خود را کنترل نمایید")
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            mGoogleApiClient.connect();
                            getMyLocation();
                        }
                    }).create().show();
        }
    }

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

                mMap.addMarker(marker);

            }
            swich = 1;
            mMap.setInfoWindowAdapter(new CustomInfoView());
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... voids) {
            HashMap<String, Object> datas = new HashMap<String, Object>();

            Log.e("Location", "L and W is " + mLatitudeText + " ...  " + mLongitudeText);


            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            datas.put("L", mLongitudeText);
            datas.put("W", mLatitudeText);


            if (isonline) {
                try {

                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Navigation_MyNEar_Customers", datas).getProperty(0);

                    Log.e("customers", " " + request2);

                    for (int i = 0; i < request2.getPropertyCount(); i++) {
                        SoapObject sp = (SoapObject) request2.getProperty(i);


                        BazaryabInfo point = new BazaryabInfo();

                        point.set_L(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                        point.set_W(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                        point.set_Code(NetworkTools.getSoapPropertyAsNullableString(sp, 2));
                        point.set_Name(NetworkTools.getSoapPropertyAsNullableString(sp, 3));
                        point.set_Tel(NetworkTools.getSoapPropertyAsNullableString(sp, 4));
                        point.setWrappers(NavigationWrapper.getNavigationWrappers(NetworkTools.getSoapPropertyAsNullableString(sp, 7)));

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

    public int requestPermission() {

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

                Log.e("Permission", "Persmisn is " + MY_PERMISSIONS_REQUEST_READ_GPS);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        return MY_PERMISSIONS_REQUEST_READ_GPS;


    }

    private class CustomInfoView implements GoogleMap.InfoWindowAdapter {

        @Override
        public View getInfoWindow(Marker marker) {

            if (marker == null && TextUtils.isEmpty(marker.getTitle()))
                return null;

            BazaryabInfo currentPoint = null;
            if (swich == 1) {
                for (int i = 0; i < pointha.size(); i++) {
                    if (marker.getTitle().compareTo(pointha.get(i).get_Code()) == 0)
                        currentPoint = pointha.get(i);
                }
            }
            if (swich == 2) {
                if (points.size() > 0) {
                    for (int i = 0; i < points.size(); i++) {
                        if (marker.getTitle().compareTo(points.get(i).get_Code()) == 0)
                            currentPoint = points.get(i);
                    }
                }
            }

            if (currentPoint == null)
                return null;

            View view = getLayoutInflater().inflate(R.layout.custom_pin_info,
                    null);

            TextView txtCode = (TextView) view.findViewById(R.id.txtCode);
            TextView txtName = (TextView) view.findViewById(R.id.txtName);
            txtCode.setText(currentPoint.get_Code());
            txtName.setText(currentPoint.get_Name());
            parseNavigationInfo(currentPoint.getWrappers(), (LinearLayout) view);
//            txtTell.setText( currentPoint.get_Tel());
//            txtTell.set(parseNavigationInfo(currentPoint.getWrappers()));

            return view;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

    private void parseNavigationInfo(ArrayList<NavigationWrapper> wrappers, LinearLayout parent) {

        for (int i = 0; i < wrappers.size(); i++) {

            LinearLayout row = new LinearLayout(getApplicationContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int padding = Convertor.toPixcel(10f, getApplicationContext());
            row.setPadding(padding, padding, padding, padding);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(params);


            TextView value = new TextView(getApplicationContext());
            LinearLayout.LayoutParams paramsValue = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
            value.setText(wrappers.get(i).value);
            value.setLayoutParams(paramsValue);
            value.setGravity(Gravity.RIGHT);
            value.setTextColor(Color.BLACK);
            row.addView(value);

            TextView title = new TextView(getApplicationContext());
            LinearLayout.LayoutParams paramsTitle = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
            title.setText(wrappers.get(i).title);
            title.setTextColor(Color.BLACK);
            title.setLayoutParams(paramsTitle);

            row.addView(title);

            parent.addView(row);
        }


    }

    private void showPersianCalender(PersianCalendar calender, DatePickerDialog dialog) {

        calender = new PersianCalendar();
        dialog = DatePickerDialog.newInstance(ActivityGoogleMap.this, calender.getPersianYear(), calender.getPersianMonth(),
                calender.getPersianDay());
        dialog.setThemeDark(false);
        dialog.show(getFragmentManager(), "Datepickerdialog");


    }

    private void runBazaryabSabtPathAsync(String date) {

        if (bazaryabSabtPathAsync == null) {
            points.clear();

            bazaryabSabtPathAsync = new BazaryabSabtPath();
            bazaryabSabtPathAsync.execute();
        } else {
            return;
        }

    }

    private void initRecycleView() {

        recyclerView = (RecyclerView) findViewById(R.id.row_customer);
        txtAverage = (TextView) findViewById(R.id.txtAverage);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CustomersDistancesAdapter(ActivityGoogleMap.this);
        adapter.setItem(customersDistances);
        recyclerView.setAdapter(adapter);

    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return meter;
    }

    private double getDistanceAverage(ArrayList<BazaryabInfo> array) {

        if (array == null || array.size() == 0)
            return 0;
        double average = 0;
        int i = array.size();
        for (int j = 0; j < i; j++) {
            average += array.get(j).get_Distance();
        }
        return average / i;

    }

    private void initSearchAction() {

        EditText edtSearch = (EditText) findViewById(R.id.edtSearch);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                adapter.getFilter().filter(editable.toString());
            }
        });
    }


}




