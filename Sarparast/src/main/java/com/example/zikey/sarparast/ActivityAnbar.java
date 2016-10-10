package com.example.zikey.sarparast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.zikey.sarparast.Helpers.PreferenceHelper;

public class ActivityAnbar extends AppCompatActivity {
    private PreferenceHelper preferenceHelper;
    private ImageView imgBack;

    LinearLayout lyKala;
    LinearLayout lyForoshVije;
    LinearLayout lyNews;
    LinearLayout lyPictures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__anbar);

        preferenceHelper = new PreferenceHelper(this);
        imgBack = (ImageView) findViewById(R.id.imgBack);

        lyKala = (LinearLayout) findViewById(R.id.lyMojodi);
        lyForoshVije = (LinearLayout) findViewById(R.id.lyForoshVije);
        lyNews = (LinearLayout) findViewById(R.id.lyNew);
        lyPictures = (LinearLayout) findViewById(R.id.lyPictures);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lyKala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityAnbar.this, ActivityAnbarDetails.class);
                intent.putExtra("State", "MojodiKol");
                startActivity(intent);

            }
        });

        lyForoshVije.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityAnbar.this, ActivityAnbarDetails.class);
                intent.putExtra("State", "ForooshVije");
                startActivity(intent);

            }
        });

        lyNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityAnbar.this, ActivityAnbarDetails.class);
                intent.putExtra("State", "News");

                startActivity(intent);

            }
        });
    }
}
