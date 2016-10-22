package com.example.zikey.sarparast;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;

import org.ksoap2.serialization.SoapObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.HashMap;

public class ActivityAddNewCustomer extends AppCompatActivity implements   GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private   String mLatitudeText;
    private   String mLongitudeText;
    private   Location mLastLocation;
    private   GoogleApiClient mGoogleApiClient;


    private   String L = "0.0";
    private   String W = "0.0";

    private PreferenceHelper preferenceHelper;
    private ImageView imgBack;
    private Button btnAdd;

    private boolean started=false;
    private ConnectionResult connectionState;

    private int permisionCheck = 0;


    private Button btnSend;

    private String name;
    private String family;
    private String tel;
    private String mobile;
    private String address;
    private String visitorName;
    private String visiroID;

    private EditText txtName;
    private EditText txtFamily;
    private EditText txtTell;
    private EditText txtMobile;
    private EditText txtAddress;
    private TextView txtVisitorName;

   public static final int MY_PERMISSIONS_REQUEST_READ_GPS = 12;

    public  int permissionCheck;
    ImageView imageView;

    public   int permiss;

//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_customer);






    permiss = ContextCompat.checkSelfPermission(ActivityAddNewCustomer.this,
                Manifest.permission.ACCESS_FINE_LOCATION);

    requestPermission();


//        Log.e("Permission","Permison is "+permissionCheck);
        Log.e("API LEVEL Version","API IS "+ Build.VERSION.SDK_INT );
        imageView = (ImageView) findViewById(R.id.imgGPS);

        txtName= (EditText) findViewById(R.id.edtName);
        txtFamily= (EditText) findViewById(R.id.edtFamily);
        txtTell= (EditText) findViewById(R.id.edtTell);
        txtMobile= (EditText) findViewById(R.id.edtMobile);
        txtAddress= (EditText) findViewById(R.id.edtAddress);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        txtVisitorName = (TextView) findViewById(R.id.txtVisitorName);

        btnSend = (Button) findViewById(R.id.btnSend);
        btnAdd = (Button) findViewById(R.id.btnAdd);

        preferenceHelper = new PreferenceHelper(this);
        imgBack= (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                FragmentAllBazaryab fragmentAllBazaryab = new FragmentAllBazaryab();
                fragmentAllBazaryab.show(manager,"fragmentAllBazaryab");
                fragmentAllBazaryab.setActivity(ActivityAddNewCustomer.this);
                fragmentAllBazaryab.setCommunicator(new SelectUserCommunicator() {
                    @Override
                    public void onClick(String cCode, String cName) {

                        if (txtVisitorName != null)
                            txtVisitorName.setText(cName);
                        name=cName;
                        visiroID = cCode;

                        FragmentManager manager = getFragmentManager();
                        DialogFragment f = (DialogFragment) manager.findFragmentByTag("fragmentAllBazaryab");
                        if (f != null)
                            f.dismiss();
                    }
                });
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            getMyLocation();

                  }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = txtVisitorName.getText().toString();


                if (temp.equals("...")){
                    new AlertDialog.Builder(ActivityAddNewCustomer.this)
                            .setCancelable(false)
                            .setTitle("خطا")
                            .setMessage("ویزیتور مربوط به مشتری را انتخاب نمایید")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(R.drawable.decline)
                            .show();

                }
else {
                    name = txtName.getText().toString();
                    family = txtFamily.getText().toString();
                    tel = txtTell.getText().toString();
                    mobile = txtMobile.getText().toString();
                    address = txtAddress.getText().toString();
                    visitorName = txtVisitorName.getText().toString();

                    new AddNewCustomerAsync().execute();
                }

            }
        });




    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
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

            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Log.e("LastLocation", "last location is  " + mLastLocation);
            if (mLastLocation != null) {
                L = String.valueOf(mLastLocation.getLatitude());
                W = String.valueOf(mLastLocation.getLongitude());

                Log.e("latlong", "lat and long is " + mLatitudeText + "  " + mLongitudeText);
            } else {
                new android.app.AlertDialog.Builder(this)
                        .setTitle("خطا")
                        .setMessage("موقعیت فعلی شما قابل شناسایی نمیباشد. روشن بودن gps خود را کنترل نمایید")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                mGoogleApiClient.connect();
                            }
                        }).create().show();
            }

        }

    @Override
    //vase zamani ke google play service Stop beshe hast
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    connectionState =   connectionResult;

        Log.e("ConnectionStatus","connection status  ..."+connectionResult);
    }

    @Override
    protected void onStart() {

        mGoogleApiClient.connect();
        started = true;


        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    public class AddNewCustomerAsync extends AsyncTask<Void,String,String> {

        private Boolean isonline= NetworkTools.isOnline(ActivityAddNewCustomer.this);

        private String temp="";
         private   ProgressDialog dialog;


        @Override
        protected void onPostExecute(String state) {

            if (state.equals("Null")){
                new AlertDialog.Builder(ActivityAddNewCustomer.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("خطا در ثبت مشتری ")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }

            if (state.equals("Online")) {

                if (dialog != null)
                    dialog.dismiss();

                new AlertDialog.Builder(ActivityAddNewCustomer.this)
                        .setCancelable(false)
                        .setTitle("موفقیت")
                        .setMessage("مشتری جدید با موفقیت ثبت شد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }

            else  if (state.equals("NotOnline")){
                new AlertDialog.Builder(ActivityAddNewCustomer.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("اتصال به اینترنت مقدور نمیباشد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }
        }


        @Override
        protected void onPreExecute() {
            if (isonline) {

                dialog = ProgressDialog.show(ActivityAddNewCustomer.this, "", "دریافت اطلاعات");
                super.onPreExecute();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID",preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            datas.put("name", name);
            datas.put("family",family );
            datas.put("address",address );
            datas.put("tel",tel );
            datas.put("mobile",mobile );
            datas.put("l",L);
            datas.put("w", W);
            datas.put("visitorID",visiroID );


            if (isonline) {
                try {

                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_ADD_new_Customer", datas);


                   temp=(NetworkTools.getSoapPropertyAsNullableString(request2, 0).toString());


                    if (temp.equals("")){
                        return null;
                    }

                } catch (Exception e) {
                    Log.e("iiiiiii", "connot read Soap");
                    e.printStackTrace();
                    return "Null";
                }
                return "Online";
            }
            return "NotOnline";
        }
    }


    @Override
    protected void onDestroy() {
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }


    public   int requestPermission( ){

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(ActivityAddNewCustomer.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityAddNewCustomer.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(ActivityAddNewCustomer.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_GPS);

                Log.e("Permission","Persmisn is "+MY_PERMISSIONS_REQUEST_READ_GPS);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        return  MY_PERMISSIONS_REQUEST_READ_GPS;
    }


        @Override
        public void onRequestPermissionsResult(int requestCode,
                                               String permissions[], int[] grantResults) {
            Log.e("RequestCode","Request Code Is "+requestCode);
            switch (requestCode) {

                case MY_PERMISSIONS_REQUEST_READ_GPS:  {

                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(ActivityAddNewCustomer.this, "PermissionAccepted", Toast.LENGTH_SHORT).show();
                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.

                        permissionCheck =1;

                        getMyLocation();

                    } else {

                        Toast.makeText(ActivityAddNewCustomer.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.

                        new android.app.AlertDialog.Builder(this)
                                .setTitle("خطا")
                                .setIcon(R.drawable.eror_dialog)
                                .setCancelable(false)
                                .setMessage("مجوز استفاده از موقعیت داده نشده است")
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();

                                    }
                                });
                    }
                    return;
                }

                // other 'case' lines to check for other
                // permissions this app might request
            }
        }





    private  void  getMyLocation(){
        if (MY_PERMISSIONS_REQUEST_READ_GPS==-1){
            new android.app.AlertDialog.Builder(ActivityAddNewCustomer.this)
                    .setTitle("خطا")
                    .setCancelable(false)
                    .setMessage("اجازه دسترسی به موقیعت داده نشده است!")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    }).create().show();

        }

        else {

            if (ActivityCompat.checkSelfPermission(ActivityAddNewCustomer.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ActivityAddNewCustomer.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            Log.e("LastLocation", "last location is  " + mLastLocation);
            if (mLastLocation != null) {
                L = String.valueOf(mLastLocation.getLatitude());
                W = String.valueOf(mLastLocation.getLongitude());

                new android.app.AlertDialog.Builder(ActivityAddNewCustomer.this)
                        .setTitle("موفقیت")
                        .setMessage("موقعیت شما با موفقیت ثبت شد")
                        .setIcon(R.drawable.successs)

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                Log.e("Location is ", " L and W is " + L + " " + W);

                            }
                        }).create().show();

                Log.e("latlong", "lat and long is " + mLatitudeText + "  " + mLongitudeText);
            } else {
                new android.app.AlertDialog.Builder(ActivityAddNewCustomer.this)
                        .setTitle("خطا")
                        .setCancelable(false)
                        .setMessage("موقعیت فعلی شما قابل شناسایی نمیباشد. روشن بودن gps خود را کنترل نمایید")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                mGoogleApiClient.connect();
                            }
                        }).create().show();
            }


        }
    }

}
