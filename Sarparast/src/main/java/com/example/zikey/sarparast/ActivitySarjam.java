package com.example.zikey.sarparast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ActivitySarjam extends AppCompatActivity {

    private ImageView imgBack;

    LinearLayout lyKala;
    LinearLayout lyVizitor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sarjam);

        imgBack= (ImageView) findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lyKala = (LinearLayout) findViewById(R.id.lyKala);
        lyVizitor = (LinearLayout) findViewById(R.id.lyVizitor);


        lyVizitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivitySarjam.this,ActivitySarjamDetails.class);
                intent.putExtra("State","Vizitor");
                startActivity(intent);

            }
        });


         lyKala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivitySarjam.this,ActivitySarjamDetails.class);
                intent.putExtra("State","Kala");
                startActivity(intent);

            }
        });



    }
}
