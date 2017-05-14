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
import com.razanPardazesh.supervisor.AroundMeMapActivity;

public class ActivityVisitorsInMap extends AppCompatActivity {

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

        preferenceHelper = new PreferenceHelper(this);
        imgBack = (ImageView) findViewById(R.id.imgBack);

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

                AroundMeMapActivity.start(ActivityVisitorsInMap.this);

            }
        });


        lyBazaryabPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityVisitorsInMap.this, ActivityListOfBazaryab.class);
                intent.putExtra("state", "BazaryabLists");
                startActivity(intent);

            }
        });


        lySabtSefaresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityVisitorsInMap.this, ActivityListOfBazaryab.class);
                intent.putExtra("state", "SabtSefaresh");
                startActivity(intent);

            }
        });

        lyNotOrdered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityVisitorsInMap.this, ActivityListOfBazaryab.class);
                intent.putExtra("state", "NotOrdered");
                startActivity(intent);

            }
        });

    }

}



