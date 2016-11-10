package com.razanPardazesh.supervisor;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.PreferenceHelper;
import com.example.zikey.sarparast.R;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

public class CoveragePercentageActivity extends AppCompatActivity {

    private ImageView imgBack;
    private TextView txtHead;
    private PreferenceHelper preferenceHelper;
    private TextView txtEror;
    private RelativeLayout lyEror;
    private RelativeLayout lyContent;
    private RelativeLayout lyProgress;
    private LinearLayoutManager layoutManager;
    private EditText edtSearch;
    private CoveragePercentAsync coveragePercentAsync = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coverage_percentage);

        initViews();

    }


    private void initViews() {

        txtHead = (TextView) findViewById(R.id.txtHead);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        preferenceHelper = new PreferenceHelper(this);
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        lyContent = (RelativeLayout) findViewById(R.id.lyContent);
        lyEror = (RelativeLayout) findViewById(R.id.lyEror);
        lyProgress = (RelativeLayout) findViewById(R.id.lyProgress);
        txtEror = (TextView) findViewById(R.id.txtEror);
        lyEror.setVisibility(View.GONE);
        lyContent.setVisibility(View.GONE);
        lyProgress.setVisibility(View.VISIBLE);
        txtHead.setText("آمار پوشش روزانه ویزیتورها");

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // row_adapter.getFilter().filter(editable.toString());
            }
        });
    }

    public class CoveragePercentAsync extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    private void runAsync() {
     if (coveragePercentAsync!=null) return;
        coveragePercentAsync = new CoveragePercentAsync();
        coveragePercentAsync.execute();
    }

    public static void start(FragmentActivity context) {
        Intent starter = new Intent(context, CoveragePercentageActivity.class);
        context.startActivity(starter);
    }

}
