package com.example.zikey.sarparast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.zikey.sarparast.Helpers.PreferenceHelper;

public class ActivityMoshtarian extends AppCompatActivity {

    private PreferenceHelper preferenceHelper;
    private ImageView imgBack;

    private LinearLayout ADD;
    private LinearLayout Mandeh;
    private LinearLayout  ltTaeed;
    private LinearLayout  lySabtMogheyat;

    public static  android.app.FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moshtarian);

        manager = getFragmentManager();

        preferenceHelper = new PreferenceHelper(this);
        imgBack= (ImageView) findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ADD = (LinearLayout) findViewById(R.id.lyAddNewUser);
        Mandeh = (LinearLayout) findViewById(R.id.LyMandeh);
        ltTaeed = (LinearLayout) findViewById(R.id.ltTaeed);
        lySabtMogheyat = (LinearLayout) findViewById(R.id.lySabtM);


        ADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityMoshtarian.this,ActivityAddNewCustomer.class);
                startActivity(intent);
            }
        });

        lySabtMogheyat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityMoshtarian.this,ActivitySabtMogheyat.class);
                startActivity(intent);
            }
        });

        Mandeh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityMoshtarian.this,ActivityMandehMoshtarian.class);
                startActivity(intent);
            }
        });

        ltTaeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityMoshtarian.this,ActivityListOfNewCustomers.class);
                startActivity(intent);
            }
        });

    }
}
