package com.example.zikey.sarparast;

import android.app.FragmentManager;
import android.os.AsyncTask;

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

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivitySarjamDetails extends AppCompatActivity {

    private ImageView imgBack;
    private TextView txtHead;
    private String state;

    private FragmentManager manager;

    private PreferenceHelper preferenceHelper;

    private TextView txtEror;

    private RelativeLayout lyEror;
    private RelativeLayout lyContent;
    private RelativeLayout lyProgress;

    private ArrayList<SarjamInfo> sarjamInfos = new ArrayList<>();

    private RecyclerView row_sarjam;
    private RecyclerView.LayoutManager row_manager;
    private SarjamBarAdapter row_adapter;

    private EditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sarjam_details);

        row_sarjam = (RecyclerView) findViewById(R.id.row_sarjam);

        manager = getFragmentManager();


        state = getIntent().getStringExtra("State");

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


        if (state.equals("Vizitor")) {
            txtHead.setText("سرجمع بار (ویزیتور)");

            new SarjamVisitorAsync().execute();
        }


        if (state.equals("Kala")) {
            txtHead.setText("سرجمع بار (کالا)");

            new SarjamKalaAsync().execute();
        }

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

    public class SarjamVisitorAsync extends AsyncTask<Void, String, String> {

        Boolean isonline = NetworkTools.isOnline(ActivitySarjamDetails.this);


        @Override
        protected void onPostExecute(String state) {


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

                Log.e("Is Online", "Online is ok ");
                SarjamBarAdapter sarjamAdapter = new SarjamBarAdapter(sarjamInfos);

                row_adapter = sarjamAdapter;
                row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                row_sarjam.setLayoutManager(row_manager);
                row_sarjam.setAdapter(row_adapter);
                row_adapter.setState(-1);
                row_adapter.setActivity(ActivitySarjamDetails.this);
                row_adapter.setManager(manager);


                lyContent.setVisibility(View.VISIBLE);
                lyProgress.setVisibility(View.GONE);


            } else if (state.equals("NotOnline"))

            {
                txtEror.setText("اتصال به اینترنت خود را چک نمایید");

            }
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... voids) {

//            Log.e("tttttttttttt", "" + "Do in background is ok");

            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            datas.put("group", "Visitor");

            if (isonline) {
                try {
                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_GetListOF_SarjamBar", datas).getProperty(0);

                    if (request2.getPropertyCount() <= 0) {
                        return "Null";
                    }

                    for (int i = 0; i < request2.getPropertyCount(); i++) {
                        SoapObject sp = (SoapObject) request2.getProperty(i);
                        Log.e("tttttttttttt", "" + sp);

                        SarjamInfo sarjamInfo = new SarjamInfo();

                        sarjamInfo.set_Code(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                        sarjamInfo.set_Name(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                        sarjamInfo.set_Factor(NetworkTools.getSoapPropertyAsNullableString(sp, 3));
                        sarjamInfo.set_Sefaresh(NetworkTools.getSoapPropertyAsNullableString(sp, 2));
                        sarjamInfo.set_PriceFactor(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 5).toString())));
                        sarjamInfo.set_PriceSefaresh(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 4).toString())));

                        sarjamInfos.add(sarjamInfo);
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


    public class SarjamKalaAsync extends AsyncTask<Void, String, String> {

        Boolean isonline = NetworkTools.isOnline(ActivitySarjamDetails.this);


        @Override
        protected void onPostExecute(String state) {


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

                Log.e("Is Online", "Online is ok ");
                SarjamBarAdapter sarjamAdapter = new SarjamBarAdapter(sarjamInfos);

                lyContent.setVisibility(View.VISIBLE);
                lyProgress.setVisibility(View.GONE);
                lyEror.setVisibility(View.GONE);

                row_adapter = sarjamAdapter;
                row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                row_sarjam.setLayoutManager(row_manager);

                row_sarjam.setAdapter(row_adapter);
                row_adapter.setState(1);
                row_adapter.setActivity(ActivitySarjamDetails.this);
                row_adapter.setManager(manager);


            } else if (state.equals("NotOnline"))

            {
                txtEror.setText("اتصال به اینترنت خود را چک نمایید");

            }
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... voids) {

            Log.e("tttttttttttt", "" + "Do in background is ok");

            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            datas.put("group", "Kala");

            if (isonline) {
                try {
                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_GetListOF_SarjamBar", datas).getProperty(0);

                    if (request2.getPropertyCount() <= 0) {
                        return "Null";
                    }

                    for (int i = 0; i < request2.getPropertyCount(); i++) {
                        SoapObject sp = (SoapObject) request2.getProperty(i);
                        Log.e("tttttttttttt", "" + sp);

                        SarjamInfo sarjamInfo = new SarjamInfo();

                        sarjamInfo.set_Code(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                        sarjamInfo.set_Name(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                        sarjamInfo.set_Factor(NetworkTools.getSoapPropertyAsNullableString(sp, 2));
                        sarjamInfo.set_Sefaresh(NetworkTools.getSoapPropertyAsNullableString(sp, 3));
                        sarjamInfo.set_PriceFactor(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 5).toString())));
                        sarjamInfo.set_PriceSefaresh(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 4).toString())));

                        sarjamInfos.add(sarjamInfo);
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


}
