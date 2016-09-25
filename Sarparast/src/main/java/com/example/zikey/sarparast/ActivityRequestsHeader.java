package com.example.zikey.sarparast;

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

public class ActivityRequestsHeader extends AppCompatActivity {

    private PreferenceHelper preferenceHelper;
    private ImageView imgBack;
    private TextView txtEror;
    private TextView txtTop;

    private String searchString = null;

    private EditText edtSearch;

    private RelativeLayout lyEror;
    private android.widget.RelativeLayout lyContent;
    private RelativeLayout lyProgress;
    private RelativeLayout lyView;


    private ArrayList<RequestsInfo> requestsInfos = new ArrayList<>();

    private RecyclerView row_sefaresh;
    private RecyclerView.LayoutManager row_manager;
    private RequestsHeaderAdapter row_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_request_header);

        preferenceHelper = new PreferenceHelper(this);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        txtTop = (TextView) findViewById(R.id.txtTop);

        lyContent = (RelativeLayout) findViewById(R.id.lyContent);
        lyEror = (RelativeLayout) findViewById(R.id.lyEror);
        lyProgress = (RelativeLayout) findViewById(R.id.lyProgress);
        lyView = (RelativeLayout) findViewById(R.id.lyView);
        txtEror = (TextView) findViewById(R.id.txtEror);

        lyEror.setVisibility(View.GONE);
        lyContent.setVisibility(View.GONE);
        lyProgress.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                G.hideSoftKeyboard(ActivityRequestsHeader.this);
            }
        });
        edtSearch = (EditText) findViewById(R.id.edtSearch);

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

    public class BazaryabHaveOrderAsync extends AsyncTask<Void, String, String> {

        Boolean isonline = NetworkTools.isOnline(ActivityRequestsHeader.this);

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
                row_adapter = new RequestsHeaderAdapter(requestsInfos);
//                adapter.setAct(ActivityAnalyseOfSefareshat.this);
                row_sefaresh = (RecyclerView) findViewById(R.id.row_sefareshat);

                row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                row_sefaresh.setLayoutManager(row_manager);

                row_adapter.setActivity(ActivityRequestsHeader.this);
                row_sefaresh.setAdapter(row_adapter);

                lyContent.setVisibility(View.VISIBLE);
                lyProgress.setVisibility(View.GONE);


            } else if (state.equals("NotOnline"))

            {
                txtEror.setText("اتصال به اینترنت خود را چک نمایید");
                lyProgress.setVisibility(View.GONE);
                lyContent.setVisibility(View.GONE);
                lyEror.setVisibility(View.VISIBLE);
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

            if (isonline) {
                try {
                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_GetListOf_Visitors_Have_Order", datas).getProperty(0);

                    if (request2.getPropertyCount() <= 0) {
                        return "Null";
                    }
                    for (int i = 0; i < request2.getPropertyCount(); i++) {
                        SoapObject sp = (SoapObject) request2.getProperty(i);

                        RequestsInfo requestsInfo = new RequestsInfo();

                        requestsInfo.set_SefareshID(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                        requestsInfo.set_NameBazaryab(NetworkTools.getSoapPropertyAsNullableString(sp, 9));
                        requestsInfo.set_CodeBazaryab(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                        requestsInfo.set_TedadKol(NetworkTools.getSoapPropertyAsNullableString(sp, 3));
                        requestsInfo.set_FactorShode(NetworkTools.getSoapPropertyAsNullableString(sp, 4));
                        requestsInfo.set_TedadTaeed(NetworkTools.getSoapPropertyAsNullableString(sp, 6));
                        requestsInfo.set_TedadLaghv(NetworkTools.getSoapPropertyAsNullableString(sp, 7));
                        requestsInfo.set_TedadBargashti(NetworkTools.getSoapPropertyAsNullableString(sp, 8));

                        requestsInfo.set_RialTedadKol((String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 10).toString()))));
                        requestsInfo.set_RialFactorShode((String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 12).toString()))));
                        requestsInfo.set_RialTedadTaeed((String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 14).toString()))));
                        requestsInfo.set_RialTedadLaghv((String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 15).toString()))));
                        requestsInfo.set_RialTedadBargashti((String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 16).toString()))));

                        requestsInfos.add(requestsInfo);
                    }

                } catch (Exception e) {

                    Log.e("iiiiiii", "connot read Soap");
                    e.printStackTrace();

                }
                return "Online";
            }
            return "NotOnline";

        }
    }

//    @Override
//    protected void onRestart() {
//
//        requestsInfos.clear();
//        new BazaryabHaveOrderAsync().execute();
//        super.onRestart();
//    }


    @Override
    protected void onResume() {
        lyProgress.setVisibility(View.VISIBLE);
        lyContent.setVisibility(View.GONE);
        requestsInfos.clear();
        new BazaryabHaveOrderAsync().execute();

        super.onResume();
    }
}

