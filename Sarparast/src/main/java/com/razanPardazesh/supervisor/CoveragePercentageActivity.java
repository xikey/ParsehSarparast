package com.razanPardazesh.supervisor;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zikey.sarparast.ActivityManagmentFooter;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;
import com.example.zikey.sarparast.ListOfAllVisitorsAdapter;
import com.example.zikey.sarparast.R;
import com.razanPardazesh.supervisor.model.Report;
import com.razanPardazesh.supervisor.model.wrapper.ReportsAnswer;
import com.razanPardazesh.supervisor.repo.ReportServerRepo;
import com.razanPardazesh.supervisor.view.viewAdapter.CoveragePercentAdapter;

import java.util.ArrayList;


public class CoveragePercentageActivity extends AppCompatActivity {

    private ImageView imgBack;
    private TextView txtHead;
    private PreferenceHelper preferenceHelper;
    private TextView txtEror;
    private RelativeLayout lyEror;
    private RelativeLayout lyContent;
    private RelativeLayout lyProgress;
    private EditText edtSearch;
    private CoveragePercentAsync coveragePercentAsync = null;
    private ReportServerRepo reportServerRepo = null;
    private ReportsAnswer reportsAnswer = null;
    private ArrayList<Report> reports = new ArrayList<>();

    private RecyclerView visitorsRecycle;
    private CoveragePercentAdapter row_adapter;
    private LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coverage_percentage);

        initRecycleView();
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


        runAsync();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                row_adapter.getFilter().filter(editable.toString());
            }
        });
    }


    private void initRecycleView() {

        visitorsRecycle = (RecyclerView) findViewById(R.id.row_AllVisitors);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        visitorsRecycle.setLayoutManager(layoutManager);
        row_adapter = new CoveragePercentAdapter();
        row_adapter.setItem(reports);
        row_adapter.setContext(getApplicationContext());
        visitorsRecycle.setAdapter(row_adapter);

    }

    public class CoveragePercentAsync extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {

            reportsAnswer = new ReportsAnswer();
            reportServerRepo = new ReportServerRepo();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            reportsAnswer = reportServerRepo.visitorsCoveragePercent(getApplicationContext(), "", 0, 0);
            if (reportsAnswer.getIsSuccess() == 0) {
                return "0";

            }
            if (reportsAnswer.getMessage()!=null){
                return "-1";
            }

            return "1";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("1")) {
                lyProgress.setVisibility(View.GONE);
                lyContent.setVisibility(View.VISIBLE);

                reports = reportsAnswer.getReports();
                if (reports!=null){
                    row_adapter.setItem(reports);

                }
            }
            if (s.equals("-1")){

                lyProgress.setVisibility(View.GONE);
                lyContent.setVisibility(View.GONE);
                lyEror.setVisibility(View.VISIBLE);
                txtEror.setText("خطا در دریافت اطلاعات");
            }

            if (s.equals("0")){
                lyProgress.setVisibility(View.GONE);
                lyContent.setVisibility(View.GONE);
                lyEror.setVisibility(View.VISIBLE);
                txtEror.setText("اطلاعاتی جهت نمایش وجود ندارد");
            }


        }

    }


    private void runAsync() {
        if (coveragePercentAsync != null) return;
        coveragePercentAsync = new CoveragePercentAsync();
        coveragePercentAsync.execute();
    }

    public static void start(FragmentActivity context) {
        Intent starter = new Intent(context, CoveragePercentageActivity.class);
        context.startActivity(starter);
    }

}
