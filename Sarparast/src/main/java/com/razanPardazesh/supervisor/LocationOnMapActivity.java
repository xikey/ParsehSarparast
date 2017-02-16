package com.razanPardazesh.supervisor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationOnMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String KEY_LONGITUDE = "LONGITUDE";
    private static final String KEY_LATITUDE = "LATITUDE";


    SupportMapFragment mapFragment;
    double longitude;
    double latitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_on_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        initViews();
        parseIntent();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap==null)
            return;

        addMarker(googleMap,longitude,latitude);

    }

    public static void start(FragmentActivity context, double lt, double ln) {
        Intent starter = new Intent(context, LocationOnMapActivity.class);
        starter.putExtra(KEY_LONGITUDE, ln);
        starter.putExtra(KEY_LATITUDE, lt);
        context.startActivity(starter);
    }

    private void parseIntent() {
        Intent data = getIntent();

        if (data == null)
            return;

        if (data.hasExtra(KEY_LONGITUDE)) {

            longitude = data.getDoubleExtra(KEY_LONGITUDE, 0);
            latitude = data.getDoubleExtra(KEY_LATITUDE, 0);

        }
    }

    private void initViews() {

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void addMarker(GoogleMap map, double ln, double lt) {

        if (!NetworkTools.checkNetworkConnection(LocationOnMapActivity.this)) {
            showErorAlertDialog("خطا", "امکان برقراری اتصال به اینترنت نمیباشد");
            return;
        }
        if (lt == 0 || ln == 0)
            return;

        if (map == null)
            return;

        float zoomLevel = 15.0f;
        MarkerOptions options = new MarkerOptions();
        LatLng latLng = new LatLng(lt, ln);
        options.position(latLng);
        map.addMarker(options);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
    }

    private void showErorAlertDialog(String titile, String message) {
        if (message == null) return;

        AlertDialog alertDialog = new AlertDialog.Builder(LocationOnMapActivity.this).create();
        alertDialog.setTitle(titile);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.show();

    }
}
