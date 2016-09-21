package com.example.zikey.sarparast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.zikey.sarparast.Helpers.PreferenceHelper;

public class ActivityForoosh extends AppCompatActivity {

    private PreferenceHelper preferenceHelper;
    private ImageView imgBack;

    private  LinearLayout lyMantaghe;
    private  LinearLayout lyTarikh;
    private LinearLayout lyMahane;
    private LinearLayout lyVisitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foroosh);

        lyMantaghe = (LinearLayout) findViewById(R.id.lyMantaghe);
        lyTarikh = (LinearLayout) findViewById(R.id.lyTarikh);
        lyMahane = (LinearLayout) findViewById(R.id.lyMahane);
        lyVisitor = (LinearLayout) findViewById(R.id.lyVisitor);


        preferenceHelper = new PreferenceHelper(this);
        imgBack= (ImageView) findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        lyMahane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityForoosh.this,ActivityAnalyseOfSefareshat.class );
                intent.putExtra("State","Mahane");
                startActivity(intent);

            }
        });
            lyMantaghe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityForoosh.this,ActivityAnalyseOfSefareshat.class );
                intent.putExtra("State","Manategh");
                startActivity(intent);

            }
        });
            lyTarikh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityForoosh.this,ActivityAnalyseOfSefareshat.class );
                intent.putExtra("State","Date");
                startActivity(intent);

            }
        });
            lyVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityForoosh.this,ActivityAnalyseOfSefareshat.class );
                intent.putExtra("State","Visitor");
                startActivity(intent);

            }
        });

    }
}
