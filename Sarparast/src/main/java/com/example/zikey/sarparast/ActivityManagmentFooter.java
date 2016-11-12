package com.example.zikey.sarparast;

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
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityManagmentFooter extends AppCompatActivity {

    private ImageView imgBack;
    private TextView txtHead;
    private PreferenceHelper preferenceHelper;
    private TextView txtEror;
    private RelativeLayout lyEror;
    private RelativeLayout lyContent;
    private RelativeLayout lyProgress;
    private EditText edtSearch;
    private ListOfVisitorsAsync listOfVisitorsAsync = null;

    private RecyclerView visitorsRecycle;
    private ListOfAllVisitorsAdapter row_adapter;
    private LinearLayoutManager layoutManager;

    private ArrayList<BazaryabInfo> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managment_footer);

        initViews();
        initRecycleView();
        runAsync();

    }

    public static void start(FragmentActivity context) {
        Intent starter = new Intent(context, ActivityManagmentFooter.class);
        context.startActivity(starter);
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

        txtHead.setText("مدیریت ویزیتور ها");

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

    public class ListOfVisitorsAsync extends AsyncTask<Void, String, String> {

        FragmentActivity activity;


        Boolean isonline = NetworkTools.isOnline(ActivityManagmentFooter.this);

        @Override
        protected void onPostExecute(String state) {

            listOfVisitorsAsync = null;

            if (state.equals("Null")) {

                lyProgress.setVisibility(View.GONE);
                lyContent.setVisibility(View.GONE);
                lyEror.setVisibility(View.VISIBLE);
            }

            if (state.equals("Eror")) {

                txtEror.setText("خطا در دریافت اطلاعات از سرور");
                lyProgress.setVisibility(View.GONE);
                lyContent.setVisibility(View.GONE);
                lyEror.setVisibility(View.VISIBLE);
            }

            if (state.equals("Online")) {

                row_adapter.setItem(items);

                lyProgress.setVisibility(View.GONE);
                lyContent.setVisibility(View.VISIBLE);
                lyEror.setVisibility(View.GONE);


            } else if (state.equals("NotOnline"))

            {
                txtEror.setText("اتصال به اینترنت خود را چک نمایید");

                lyEror.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... voids) {

            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));

            if (isonline) {
                try {

                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_GetListOF_Bazaryab", datas).getProperty(0);

                    if (request2.getPropertyCount() <= 0) {
                        return "Null";
                    }

                    for (int i = 0; i < request2.getPropertyCount(); i++) {
                        SoapObject sp = (SoapObject) request2.getProperty(i);
                        Log.e("tttttttttttt", "" + sp);

                        BazaryabInfo item = new BazaryabInfo();

                        item.set_Code(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                        item.set_Name(NetworkTools.getSoapPropertyAsNullableString(sp, 0));

                        items.add(item);

                    }

                } catch (Exception e) {

                    Log.e("iiiiiii", "connot read Soap");
                    e.printStackTrace();
                    return "Eror";
                }
                return "Online";
            }
            return "NotOnline";
        }
    }

    private void runAsync() {
        if (listOfVisitorsAsync != null) {
            return;
        } else {
            listOfVisitorsAsync = new ListOfVisitorsAsync();
            listOfVisitorsAsync.execute();
        }
    }

    private void initRecycleView() {

        visitorsRecycle = (RecyclerView) findViewById(R.id.row_AllVisitors);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        visitorsRecycle.setLayoutManager(layoutManager);
        row_adapter = new ListOfAllVisitorsAdapter();
        row_adapter.setItem(items);
        row_adapter.setActivity(ActivityManagmentFooter.this);
        visitorsRecycle.setAdapter(row_adapter);

    }

}
