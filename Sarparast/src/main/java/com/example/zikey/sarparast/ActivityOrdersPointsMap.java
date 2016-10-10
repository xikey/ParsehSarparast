package com.example.zikey.sarparast;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActivityOrdersPointsMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double visitorLat = 0.0;
    private Double visitorLong = 0.0;
    private Double customerLat = 0.0;
    private Double customerLong = 0.0;
    private BitmapDescriptor pinStart;
    private BitmapDescriptor pinEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_points_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        parseIntent(getIntent());
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

        mMap = googleMap;

        pinStart = BitmapDescriptorFactory.fromResource(R.drawable.start_pin);
        pinEnd = BitmapDescriptorFactory.fromResource(R.drawable.pin_end);

        initLocations();

    }


    private void parseIntent(Intent data) {
        if (data == null) return;
        if (!data.hasExtra("visitorLat")) return;
        visitorLat = data.getDoubleExtra("visitorLat", 0.0);
        visitorLong = data.getDoubleExtra("visitorLong", 0.0);
        customerLat = data.getDoubleExtra("customerLat", 0.0);
        customerLong = data.getDoubleExtra("customerLong", 0.0);
    }

    private void initLocations() {
        float zoomLevel = 15.0f; //This goes up to 21
        MarkerOptions visitorMarker = new MarkerOptions();
        MarkerOptions customerMarker = new MarkerOptions();

        if (visitorLat != 0.0) {
            LatLng visitorLatLng = new LatLng(visitorLat, visitorLong);
            visitorMarker.position(visitorLatLng);
            visitorMarker.title("ویزیتور");
            visitorMarker.icon(pinStart);
            mMap.addMarker(visitorMarker);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(visitorLatLng, zoomLevel));
        }

        if (customerLat != 0.0) {
            LatLng customerLatlong = new LatLng(customerLat, customerLong);
            customerMarker.position(customerLatlong);
            customerMarker.title("مشتری");
            customerMarker.icon(pinEnd);
            mMap.addMarker(customerMarker);
        }
    }
}
