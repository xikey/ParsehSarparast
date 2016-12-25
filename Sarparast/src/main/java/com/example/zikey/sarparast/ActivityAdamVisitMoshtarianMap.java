package com.example.zikey.sarparast;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActivityAdamVisitMoshtarianMap extends FragmentActivity implements OnMapReadyCallback {

    private static final String KEY_CUSTOMER_LAT = "CLAT";
    private static final String KEY_CUSTOMER_LONG = "CLONG";
    private static final String KEY_ORDER_LAT = "OLAT";
    private static final String KEY_ORDER_LONG = "OLONG";

    private Double customerLat = null;
    private Double customerLong = null;
    private Double orderLat = null;
    private Double orderLong = null;

    private BitmapDescriptor customerPin;
    private BitmapDescriptor orderPin;

    private TextView txtCustomer;
    private TextView txtOrder;
    private TextView txtDistance;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adam_visit_moshtarian_map);

        initViews();
        parseIntent();
        initToolbar();
        initMap();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        pinLocations();

    }

    public static void start(FragmentActivity context, Double customerLat, Double customerLong, Double orderLat, Double orderLong) {
        Intent starter = new Intent(context, ActivityAdamVisitMoshtarianMap.class);
        starter.putExtra(KEY_CUSTOMER_LAT, customerLat);
        starter.putExtra(KEY_CUSTOMER_LONG, customerLong);
        starter.putExtra(KEY_ORDER_LAT, orderLat);
        starter.putExtra(KEY_ORDER_LONG, orderLong);
        context.startActivity(starter);
    }

    private void initMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    private void initToolbar() {
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void parseIntent() {


        Intent intent = getIntent();

        if (intent.hasExtra(KEY_CUSTOMER_LAT)) {
            customerLat = intent.getDoubleExtra(KEY_CUSTOMER_LAT, 0);

        }
        if (intent.hasExtra(KEY_CUSTOMER_LONG)) {
            customerLong = intent.getDoubleExtra(KEY_CUSTOMER_LONG, 0);

        }
        if (intent.hasExtra(KEY_ORDER_LAT)) {
            orderLat = intent.getDoubleExtra(KEY_ORDER_LAT, 0);

        }
        if (intent.hasExtra(KEY_ORDER_LONG)) {
            orderLong = intent.getDoubleExtra(KEY_ORDER_LONG, 0);
        }

    }

    private void pinLocations() {

        RelativeLayout lyProgress = (RelativeLayout) findViewById(R.id.lyProgress);
        lyProgress.setVisibility(View.GONE);

        customerPin = BitmapDescriptorFactory.fromResource(R.drawable.pin_end);
        orderPin = BitmapDescriptorFactory.fromResource(R.drawable.start_pin);
        final float zoomLevel = 16.0f;

        LatLng customerLocation = null;
        LatLng visitorLocation = null;


        if (customerLat != null && customerLat != 0) {

            MarkerOptions marker = new MarkerOptions();
            final LatLng customer = new LatLng(customerLat, customerLong);
            customerLocation = customer;
            marker.position(customer);
            marker.title("مکان مشتری");
            marker.icon(customerPin);
            mMap.addMarker(marker);

            txtCustomer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(customer, zoomLevel));

                }
            });

        }

        if (orderLat != null && orderLat != 0) {

            MarkerOptions marker = new MarkerOptions();
            final LatLng visitor = new LatLng(orderLat, orderLong);
            visitorLocation = visitor;
            marker.position(visitor);
            marker.title("مکان ثبت");
            marker.icon(orderPin);
            mMap.addMarker(marker);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(visitor, zoomLevel));

            txtOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(visitor, zoomLevel));

                }
            });

        }

        calculateDistance(customerLocation,visitorLocation);


    }

    private void initViews() {

        txtCustomer = (TextView) findViewById(R.id.txtCustomer);
        txtOrder = (TextView) findViewById(R.id.txtOrder);
        txtDistance = (TextView) findViewById(R.id.txtDistance);

    }

    private void calculateDistance(LatLng location1, LatLng location2) {
        txtDistance.setText("مسافت: نامشخص");

        if (location1 == null || location2 == null)
            return;

        float[] results = new float[1];
        Double lat1 = location1.latitude;
        Double long1 = location1.longitude;
        Double lat2 = location2.latitude;
        Double long2 = location2.longitude;
        Location.distanceBetween(lat1, long1, lat2, long2, results);

        txtDistance.setText("مسافت: "+String.valueOf(Math.round(results[0])) + "متر");


    }

}
