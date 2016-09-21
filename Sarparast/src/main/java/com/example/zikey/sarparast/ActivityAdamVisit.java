package com.example.zikey.sarparast;

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
    private  LinearLayout lyMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_adam_visit);

        lyDate = (LinearLayout) findViewById(R.id.lyMantaghe);
        lyMonth = (LinearLayout) findViewById(R.id.lyTarikh);



        preferenceHelper = new PreferenceHelper(this);
        imgBack= (ImageView) findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
