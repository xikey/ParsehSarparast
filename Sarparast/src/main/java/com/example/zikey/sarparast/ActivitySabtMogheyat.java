package com.example.zikey.sarparast;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivitySabtMogheyat extends AppCompatActivity   implements   GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    Location mLastLocation;
    GoogleApiClient mGoogleApiClient;

    double l=0.0;
    double w=0.0;

    GetMyLocationCommunicator communicator;

    private  int ID;

    private EditText edtSearch;

    private PreferenceHelper preferenceHelper;

    private ImageView imgBack;

    private RecyclerView row_Mandeh;
    private   RecyclerView.LayoutManager row_manager;
    private SabtMogheyatAdapter row_adapter;

    private   ArrayList<MandehMoshtariInfo> MandehMoshtaries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandeh_moshtarian);

        row_Mandeh = (RecyclerView) findViewById(R.id.row_Mandeh);

        edtSearch = (EditText) findViewById( R.id.edtSearch);



        preferenceHelper = new PreferenceHelper(this);

        imgBack= (ImageView) findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                row_adapter.getFilter().filter(editable);

            }
        });

        row_Mandeh.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                G.hideSoftKeyboard(ActivitySabtMogheyat.this);

                return false;
            }
        });


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

                new   MandehMoshtarianAsync().execute();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        gpsOnorOf();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public class MandehMoshtarianAsync extends AsyncTask<Void,String,String> {

        Boolean isonline= NetworkTools.isOnline(ActivitySabtMogheyat.this);

        ProgressDialog dialog;

        @Override
        protected void onPostExecute(String state) {

            if (state.equals("Null")){
                new AlertDialog.Builder(ActivitySabtMogheyat.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("اطلاعاتی جها نمایش وجود ندارد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }


            if (state.equals("Online")) {


                row_Mandeh.setFocusable(false);
                row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                row_Mandeh.setLayoutManager(row_manager);
                row_adapter = new SabtMogheyatAdapter( MandehMoshtaries);
                row_adapter.setActivity(ActivitySabtMogheyat.this);

                    row_adapter.setCommunicator(new GetMyLocationCommunicator() {
                        @Override
                        public void onClick(int listener) {
                            getMyLocation();
                            ID = listener;
                            if (l!=0.0){

                                new SetLocationAsync().execute();

                            }

                            else if (l==0.0)
                            {

                            }
                        }
                    });

                 row_Mandeh.setAdapter(row_adapter);

                if (dialog != null)
                    dialog.dismiss();
            }
            else  if (state.equals("NotOnline")){

                new AlertDialog.Builder(ActivitySabtMogheyat.this)
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
            //show
            if (isonline) {
                dialog = ProgressDialog.show(ActivitySabtMogheyat.this, "", "دریافت اطلاعات");
                super.onPreExecute();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {

            HashMap<String, Object> datas = new HashMap<String, Object>();
            String tokenid =  preferenceHelper.getString(preferenceHelper.TOKEN_ID);

            datas.put("TokenID",tokenid);
            datas.put("search","");

            if (isonline) {

                try {

                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_GetListOF_MandehMoshtaries_Infos", datas).getProperty(0);

                    for (int i = 0; i < request2.getPropertyCount(); i++) {
                        SoapObject sp = (SoapObject) request2.getProperty(i);
                        Log.e("iiiiissdxsdii", "  Soap"+sp);

                        MandehMoshtariInfo mandeh = new MandehMoshtariInfo();
                        mandeh.set_Code(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                        mandeh.set_Name(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                        mandeh.set_Address(NetworkTools.getSoapPropertyAsNullableString(sp, 4));
                        mandeh.set_LastL(NetworkTools.getSoapPropertyAsNullableString(sp, 5));
                        mandeh.set_LastW(NetworkTools.getSoapPropertyAsNullableString(sp, 6));


                        MandehMoshtaries.add(mandeh);
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
    protected void onResume() {

        super.onResume();
    }


    private void getMyLocation(){

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
           w = mLastLocation.getLatitude();
           l = mLastLocation.getLongitude();

      //     new MyNearCustomersAsync().execute();

          //  Log.e("latlong", "lat and long is " + mLatitudeText + "  " + mLongitudeText);
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


    private void gpsOnorOf(){

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
            w = mLastLocation.getLatitude();
            l = mLastLocation.getLongitude();

            //     new MyNearCustomersAsync().execute();

            //  Log.e("latlong", "lat and long is " + mLatitudeText + "  " + mLongitudeText);
        } else {

            new android.app.AlertDialog.Builder(this)
                    .setTitle("خطا")
                    .setIcon(R.drawable.eror_dialog)
                    .setCancelable(false)
                    .setMessage("موقعیت فعلی شما قابل شناسایی نمیباشد. روشن بودن gps خود را کنترل نمایید")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            mGoogleApiClient.connect();
                        }
                    }).create().show();
        }
    };

    @Override
    protected void onStart() {

        mGoogleApiClient.connect();



        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
//____________________________Async Class___________________________________________________________\\

    public class SetLocationAsync extends AsyncTask<Void,String,String> {

        String Latitude;
        String Longitude;


        private Boolean isonline= NetworkTools.isOnline(ActivitySabtMogheyat.this);

        private String temp="";
        private   ProgressDialog dialog;


        @Override
        protected void onPostExecute(String state) {

            if (state.equals("Null")){
                new AlertDialog.Builder(ActivitySabtMogheyat.this)
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

                if (dialog != null)
                    dialog.dismiss();

                new AlertDialog.Builder(ActivitySabtMogheyat.this)
                        .setCancelable(false)
                        .setTitle("موفقیت")
                        .setMessage(" موقعیت جدید با موفقیت ثبت شد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (MandehMoshtaries!=null) {
                                    MandehMoshtaries.clear();
                                    new MandehMoshtarianAsync().execute();
                                }


                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }

            else  if (state.equals("NotOnline")){
                new AlertDialog.Builder(ActivitySabtMogheyat.this)
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

                dialog = ProgressDialog.show(ActivitySabtMogheyat.this, "", "دریافت اطلاعات");
                super.onPreExecute();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            Latitude = String.valueOf(w);
            Longitude = String.valueOf(l);

            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID",preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            datas.put("customerID", ID);
            datas.put("L",Latitude );
            datas.put("W",Longitude );

            if (isonline) {
                try {

                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "Navigation_CreateNavigation", datas);


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




}
