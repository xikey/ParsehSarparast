package com.example.zikey.sarparast;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.zikey.sarparast.Helpers.PreferenceHelper;

public class ActivityAdamVisit extends AppCompatActivity {

    private PreferenceHelper preferenceHelper;
    private ImageView imgBack;

    private LinearLayout lyDate;
    private LinearLayout lyMonth;
    private LinearLayout lyGozaresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_adam_visit);

        lyDate = (LinearLayout) findViewById(R.id.lyDate);
        lyMonth = (LinearLayout) findViewById(R.id.lyMonth);

        lyGozaresh = (LinearLayout) findViewById(R.id.lyGozaresh);

        preferenceHelper = new PreferenceHelper(this);
        imgBack = (ImageView) findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lyDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityAdamVisit.this, ActivityListOfAdamVisit.class);
                intent.putExtra("state", "Date");
                //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        lyMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityAdamVisit.this, ActivityListOfAdamVisit.class);
                intent.putExtra("state", "Month");
                //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        lyGozaresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityAdamVisit.this, ActivityReasonsNotVisited.class);

                startActivity(intent);
            }
        });


    }
}
