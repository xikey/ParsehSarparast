package com.example.zikey.sarparast;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.zikey.sarparast.Helpers.PreferenceHelper;
import com.google.android.gms.common.api.GoogleApiClient;

public class ActivityVisitorsInMap extends AppCompatActivity    {

//    String mLatitudeText;
//    String mLongitudeText;
   private   Location mLastLocation;
   private GoogleApiClient mGoogleApiClient;

    private PreferenceHelper preferenceHelper;
    private ImageView imgBack;

    private LinearLayout lyBazaryabPath;
    private LinearLayout lySabtSefaresh;
    private LinearLayout lyNearCustomers;
    private LinearLayout lyNotOrdered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitors_in_map);

//        if (mGoogleApiClient == null) {
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(MyLocationServices.API)
//                    .build();
//        }

        preferenceHelper = new PreferenceHelper(this);
        imgBack= (ImageView) findViewById(R.id.imgBack);

        lyBazaryabPath = (LinearLayout) findViewById(R.id.lyBazaryabPath);
        lySabtSefaresh = (LinearLayout) findViewById(R.id.lySabtSefaresh);
        lyNearCustomers = (LinearLayout) findViewById(R.id.lyNearCustomers);
        lyNotOrdered = (LinearLayout) findViewById(R.id.lyNotOrdered);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lyNearCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityVisitorsInMap.this,ActivityGoogleMap.class);
                intent.putExtra("state","MyNearCustomers");
//                intent.putExtra("Lat",mLatitudeText);
//                intent.putExtra("Long",mLongitudeText);
                startActivity(intent);

            }
        });


        lyBazaryabPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityVisitorsInMap.this,ActivityListOfBazaryab.class);
                intent.putExtra("state","BazaryabLists");
                startActivity(intent);

            }
        });


        lySabtSefaresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityVisitorsInMap.this,ActivityListOfBazaryab.class);
                intent.putExtra("state","SabtSefaresh");
                startActivity(intent);

            }
        });

        lyNotOrdered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityVisitorsInMap.this,ActivityListOfBazaryab.class);
                intent.putExtra("state","NotOrdered");
                startActivity(intent);

            }
        });






//        Location mLastLocation = MyLocationServices.FusedLocationApi.getLastLocation( mGoogleApiClient);
//        Log.e("LastLocation","last location is  "+mLastLocation);
//        if (mLastLocation != null) {
//            mLatitudeText=(String.valueOf(mLastLocation.getLatitude()));
//            mLongitudeText=(String.valueOf(mLastLocation.getLongitude()));
//
//            Log.e("latlong","lat and long is "+mLatitudeText+"  "+mLongitudeText);

        }


    }



