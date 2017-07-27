package com.razanPardazesh.supervisor;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.zikey.sarparast.R;

public class CustomerActivity extends AppCompatActivity {

    public static final String KEY_REGION_CUSTOMERS = "REGION_CUSTOMERS";
    private static final String KEY_STARTER = "STARTER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public static void start(FragmentActivity context,String key) {
        Intent starter = new Intent(context, CustomerActivity.class);
        starter.putExtra(KEY_STARTER,key);
        context.startActivity(starter);
    }

}
