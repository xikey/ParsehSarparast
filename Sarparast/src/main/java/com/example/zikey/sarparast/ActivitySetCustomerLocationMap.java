package com.example.zikey.sarparast;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.DeviceInfos;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

public class ActivitySetCustomerLocationMap extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    public static final String LT = "LATITUDE";
    public static final String LN = "LONGITUDE";
    public static final String NAME = "NAME";
    public static final String CODE = "CODE";

    private GoogleMap mMap;
    private CardView btnSave;
    private ImageView imgPin;
    private GoogleApiClient mGoogleApiClient;
    private Boolean isToched;
    private RelativeLayout lyMain;
    private FloatingActionButton fabMyLocation;
    private Double latitude = 0.0;
    private Double longitude = 0.0;

    private Double cameraLatitude;
    private Double cameraLongitude;

    private String name = "_";
    private String code = "_";
    private PreferenceHelper preferenceHelper;
    private SetCustomerLocationAsync setCustomerLocationAsync = null;
    private Boolean customerLocated = false;

    private BitmapDescriptor pin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_customer_location_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        MapsInitializer.initialize(getApplicationContext());

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (isOnline()) {

            parseIntent(getIntent());
            initMainViews();


        } else {

            RelativeLayout lyEror = (RelativeLayout) findViewById(R.id.lyEror);
            lyEror.setVisibility(View.VISIBLE);
        }

        // Add a marker in Sydney and move the camera

    }

    private void initMainViews() {
        preferenceHelper = new PreferenceHelper(this);
        btnSave = (CardView) findViewById(R.id.btnSave);
        imgPin = (ImageView) findViewById(R.id.imgPin);
        lyMain = (RelativeLayout) findViewById(R.id.lyRoot5);
        fabMyLocation = (FloatingActionButton) findViewById(R.id.fabMyLocation);
        pin = BitmapDescriptorFactory.fromResource(R.drawable.pin_end);


        MapsInitializer.initialize(getApplicationContext());
        TextView txtName = (TextView) findViewById(R.id.txtName);
        txtName.setText(name);

        isCustomerLocated();

        fabMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyLocation();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = getMiddleCameraPosition();
                if (b) {
                    new AlertDialog.Builder(ActivitySetCustomerLocationMap.this)
                            .setCancelable(false)
                            .setTitle("")
                            .setMessage("آیا مایل به ثبت موقیعت جدید برای مشتری ؟")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    setCustomerLocation();
                                }
                            })
                            .setIcon(R.drawable.eror_dialog)
                            .show();

                } else {
                    new AlertDialog.Builder(ActivitySetCustomerLocationMap.this)
                            .setCancelable(false)
                            .setTitle("خطا")
                            .setMessage("متاسفانه اشکالی در ثبت موقعیت ب وجود  آمده است. از فعال بودن اینترنت و دستگاه موقعیت یاب خود اطمینان حاصل کنید")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(R.drawable.eror_dialog)
                            .show();

                }
            }

        });

    }

    public static void start(FragmentActivity context, Double lt, Double ln, String customerName, String customerCode,int requestCode) {
        Intent starter = new Intent(context, ActivitySetCustomerLocationMap.class);
        starter.putExtra(LT, lt);
        starter.putExtra(LN, ln);
        starter.putExtra(NAME, customerName);
        starter.putExtra(CODE, customerCode);

        context.startActivityForResult(starter,requestCode);
    }

    private Boolean isOnline() {

        return NetworkTools.isOnline(ActivitySetCustomerLocationMap.this);
    }


    private void parseIntent(Intent intet) {
        if (intet.hasExtra(LT)) {
            latitude = intet.getDoubleExtra(LT, 0);

        }

        if (intet.hasExtra(LN)) {
            longitude = intet.getDoubleExtra(LN, 0);
        }

        if (intet.hasExtra(NAME)) {
            name = intet.getStringExtra(NAME);
        }
        if (intet.hasExtra(CODE)) {
            code = intet.getStringExtra(CODE);
        }
    }

    private void isCustomerLocated() {

        // check customer has location on map or not

        if (longitude == 0.0 || latitude == 0.0) {
            customerLocated = false;
            return;
        } else {

            float zoomLevel = 19.0f; //This goes up to 21
            MarkerOptions marker = new MarkerOptions();
            LatLng latLng = new LatLng(latitude, longitude);
            marker.position(latLng);
            marker.title("" + name);
            marker.icon(pin);
            mMap.addMarker(marker);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

            customerLocated = true;

            return;
        }
    }


    private void getMyLocation() {


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            return;
        }

        Location mLastLocation = com.google.android.gms.location.LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            float zoomLevel = 17.0f; //This goes up to 21

            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

        }
    }

    private Boolean getMiddleCameraPosition() {

        LatLng latLng = mMap.getCameraPosition().target;
        if (latLng != null && isOnline()) {

            cameraLatitude = latLng.latitude;

            cameraLongitude = latLng.longitude;

            return true;
        }
        else {
            return false;
        }
    }

    private void setCustomerLocation() {
        runSetLocation();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void runSetLocation() {

        if (setCustomerLocationAsync != null) return;

        setCustomerLocationAsync = new SetCustomerLocationAsync();
        setCustomerLocationAsync.execute();
    }


    public class SetCustomerLocationAsync extends AsyncTask<Void, String, String> {


        private String temp = "";
        private ProgressDialog dialog;
        private String deviceInfo = DeviceInfos.getDeviceModel() + " " + DeviceInfos.getDeviceModel();

        @Override
        protected void onPostExecute(String state) {

            if (dialog != null)
                dialog.dismiss();

            if (state.equals("Null")) {
                new AlertDialog.Builder(ActivitySetCustomerLocationMap.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("خطا در ثبت موقعیت")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }

            if (state.equals("Online")) {

                new AlertDialog.Builder(ActivitySetCustomerLocationMap.this)
                        .setCancelable(false)
                        .setTitle("موفقیت")
                        .setMessage(" موقعیت جدید با موفقیت ثبت شد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                setResult(RESULT_OK);
                                finish();


                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }
        }

        @Override
        protected void onPreExecute() {


            dialog = ProgressDialog.show(ActivitySetCustomerLocationMap.this, "", "دریافت اطلاعات");
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... voids) {

            setCustomerLocationAsync=null;


            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            datas.put("customerID", code);
            datas.put("LT", String.valueOf(cameraLatitude));
            datas.put("LN", String.valueOf(cameraLongitude));
            datas.put("deviceInfo", deviceInfo);

            try {

                SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_SettCustomers_Location", datas);

                temp = (NetworkTools.getSoapPropertyAsNullableString(request2, 0).toString());


                if (temp.equals("")) {
                    return null;
                }

            } catch (Exception e) {
                Log.e("iiiiiii", "connot read Soap");
                e.printStackTrace();
                return "Null";
            }
            return "Online";
        }
    }


}
