package com.example.zikey.sarparast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityErorHandelling extends AppCompatActivity {

    private TextView txtEror;
    private String eror;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eror_handeleing);

        imgBack= (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txtEror= (TextView) findViewById(R.id.txtEror);
        if (getIntent().getStringExtra("Eror")!=null){
            eror=getIntent().getStringExtra("Eror");

            txtEror.setText(eror.toString());
        }


    }
}
