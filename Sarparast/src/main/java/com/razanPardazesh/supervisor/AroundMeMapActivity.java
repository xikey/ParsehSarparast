package com.razanPardazesh.supervisor;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.zikey.sarparast.Helpers.FontApplier;
import com.example.zikey.sarparast.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.razanPardazesh.supervisor.model.CustomerInfo;
import com.razanPardazesh.supervisor.model.wrapper.CustomersAroundMeAnswer;
import com.razanPardazesh.supervisor.repo.CustomersNavigationServerRepo;
import com.razanPardazesh.supervisor.repo.iRepo.ICustomersNavigation;
import com.razanPardazesh.supervisor.view.viewAdapter.AroundCustomersAdapter;

import java.util.ArrayList;

import static com.example.zikey.sarparast.R.id.map;


public class AroundMeMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public final int PERMISSIONS_REQUEST_READ_GPS = 14;
    private SupportMapFragment mapFragment;
    private LocationManager locationManager;
    private final long MIN_TIME = 1000;
    private final float MIN_DISTANCE = 400;
    private Marker myMarker;
    private FloatingActionButton fabLookAround;
    private ICustomersNavigation allAroundServerRepo;
    private CustomersAroundMeAnswer customersAroundMeAnswer;
    private RelativeLayout lyProgress;
    private CustomersAroundMeAsynk customersAroundMeAsynk;
    private Location myLocation;
    private BitmapDescriptor customerPin;
    private RecyclerView rvCustomers;
    private LinearLayoutManager layoutManager;
    private AroundCustomersAdapter customersAdapter;
    private ArrayList<MarkerOptions> markerArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_around_me_map);

        requestAccessLocationPermission();
        initViews();
        checkGpsProviderEnable();
        initRepo();
        initRecyclerView();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestAccessLocationPermission();
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        locationListener();

        if (fabLookAround != null)
            fabLookAround.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    runAllAroundCustomersAsync();

                }
            });


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker == null || marker.getSnippet() == null)
                    return false;

                if (markerArrayList != null && markerArrayList.size() != 0) {
                    for (int i = 0; i < markerArrayList.size(); i++) {

                        if (marker.getSnippet().toString().equals(markerArrayList.get(i).getSnippet().toString())) {
                            layoutManager.scrollToPosition(i);
                            marker.showInfoWindow();
                        }
                    }
                }
                return false;
            }
        });

    }

    private void initViews() {

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        fabLookAround = (FloatingActionButton) findViewById(R.id.fabLookAround);

        lyProgress = (RelativeLayout) findViewById(R.id.lyProgress);
        lyProgress.setVisibility(View.VISIBLE);
        customerPin = BitmapDescriptorFactory.fromResource(R.drawable.start_pin);
        rvCustomers = (RecyclerView) findViewById(R.id.rvCustomers);

        FontApplier.applyMainFont(getApplicationContext(), findViewById(R.id.lyMain));

    }

    private void initRecyclerView() {
        if (rvCustomers == null)
            initViews();

        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rvCustomers.setLayoutManager(layoutManager);
        customersAdapter = new AroundCustomersAdapter(AroundMeMapActivity.this);
        rvCustomers.setAdapter(customersAdapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvCustomers);

        rvCustomers.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    View v = rvCustomers.getChildAt(newState);
                    int pos = recyclerView.getChildAdapterPosition(v);
                    moveCameraToPosition(pos);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }


    public static void start(FragmentActivity context) {

        Intent starter = new Intent(context, AroundMeMapActivity.class);
        context.startActivity(starter);

    }

    public int requestAccessLocationPermission() {

        if (ContextCompat.checkSelfPermission(AroundMeMapActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(AroundMeMapActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(AroundMeMapActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_READ_GPS);
            }
        }
        return PERMISSIONS_REQUEST_READ_GPS;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_GPS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mapFragment != null)
                        mapFragment.getMapAsync(this);
                } else {

                }
                return;
            }
        }
    }

    private void locationListener() {

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                if (myMarker != null) {
                    myMarker.remove();
                }

                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                mMap.animateCamera(cameraUpdate);
                myMarker = mMap.addMarker(new MarkerOptions().position(latLng));
                drawCircle(latLng);
                myLocation = location;
                lyProgress.setVisibility(View.GONE);
                if (ActivityCompat.checkSelfPermission(AroundMeMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AroundMeMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
    }

    private void initRepo() {

        if (allAroundServerRepo == null)
            allAroundServerRepo = new CustomersNavigationServerRepo();
    }


    public class CustomersAroundMeAsynk extends AsyncTask<Void, String, String> {

        private String message;

        @Override
        protected void onPreExecute() {
            if (customersAroundMeAnswer != null)
                customersAroundMeAnswer.clearList();
            lyProgress.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            customersAroundMeAsynk = null;

            if (myLocation == null)
                return "3";

            String myLatitude = String.valueOf(myLocation.getLatitude());
            String myLongitude = String.valueOf(myLocation.getLongitude());

            customersAroundMeAnswer = allAroundServerRepo.getAroundCustomers(getApplicationContext(), myLatitude, myLongitude);

            if (customersAroundMeAnswer.getMessage() != null || !TextUtils.isEmpty(customersAroundMeAnswer.getMessage())) {
                message = customersAroundMeAnswer.getMessage();
                return ("-1");
            }

            if (customersAroundMeAnswer.getIsSuccess() == 1) {
                return "1";
            }

            return "0";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("-1")) {
                lyProgress.setVisibility(View.GONE);
                showErorAlertDialog("خطا", message, false);
            }

            if (s.equals("1")) {

                lyProgress.setVisibility(View.GONE);
                initMapMarkers(customersAroundMeAnswer.getCustomerArray());
                customersAdapter.setItem(customersAroundMeAnswer.getCustomerArray());

            }
            if (s.equals("3")) {
                lyProgress.setVisibility(View.GONE);
                showErorAlertDialog("خطا", "خطا در دریافت موقعیت  فعلی شما", false);
            }

            if (s.equals("0")) {
                lyProgress.setVisibility(View.GONE);
                showErorAlertDialog("خطا", "مشتری ای در اطراف شما یافت نشد", false);
            }
        }
    }


    private void runAllAroundCustomersAsync() {
        if (customersAroundMeAsynk != null)
            return;

        customersAroundMeAsynk = new CustomersAroundMeAsynk();
        customersAroundMeAsynk.execute();
    }

    private void showErorAlertDialog(String titile, String message, final boolean closeActivity) {
        if (message == null) return;

        AlertDialog alertDialog = new AlertDialog.Builder(AroundMeMapActivity.this).create();
        alertDialog.setTitle(titile);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (closeActivity) {
                            finish();
                            dialog.dismiss();
                        }
                    }
                });
        alertDialog.show();

    }

    private void checkGpsProviderEnable() {

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            showErorAlertDialog("خطا", "  (gps) دستگاه خود را  روشن نمایید", true);
        }
    }


    private void initMapMarkers(ArrayList<CustomerInfo> points) {

        if (points == null || points.size() == 0)
            return;

        if (mMap == null)
            return;

        if (markerArrayList.size() > 0)
            markerArrayList.clear();


        for (int i = 0; i < points.size(); i++) {

            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(points.get(i).getCustomerLT(), points.get(i).getCustomerLN());
            markerOptions.position(latLng);
            markerOptions.title(points.get(i).getCustomerName());
            markerOptions.icon(customerPin);
            markerOptions.snippet(String.valueOf(points.get(i).getCustomerCode()));

            markerArrayList.add(markerOptions);
            mMap.addMarker(markerOptions);
        }
    }

    private void moveCameraToPosition(int position) {

        if (mMap == null)
            return;

        if (customersAroundMeAnswer == null)
            return;

        ArrayList<CustomerInfo> items;

        items = customersAroundMeAnswer.getCustomerArray();
        if (items == null || items.size() == 0)
            return;

        double lt = items.get(position).getCustomerLT();
        double ln = items.get(position).getCustomerLN();

        LatLng l = new LatLng(lt, ln);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l, 18));

    }

    private void drawCircle(LatLng point) {

        if (point == null)
            return;

        CircleOptions circleOptions = new CircleOptions();

        circleOptions.center(point);

        circleOptions.radius(650);

        circleOptions.strokeColor(Color.parseColor("#33e57373"));

        circleOptions.fillColor(Color.parseColor("#33FFEB3B"));

        circleOptions.strokeWidth(1);

        mMap.addCircle(circleOptions);

    }



}
