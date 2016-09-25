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

public class ActivityListOfAdamVisit extends AppCompatActivity {

    private ImageView imgBack;
    private TextView txtHead;
    private String controler;

    private PreferenceHelper preferenceHelper;

    private TextView txtEror;

    private RelativeLayout lyEror;
    private RelativeLayout lyContent;
    private RelativeLayout lyProgress;

    private String methodName;

    private EditText edtSearch;

    ArrayList<AdamVisitInfo> items = new ArrayList<>();

    private RecyclerView row_AdamVisit;
    private RecyclerView.LayoutManager row_manager;
    private AdamVisitInfoAdapter row_adapter;

    private AdamVisitAsync adamVisitAsync = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_list_of_adam_visit);

        row_AdamVisit = (RecyclerView) findViewById(R.id.row_AdamVisit);

        controler = getIntent().getStringExtra("state");


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

        if (controler.equals("Date")){
            txtHead.setText("عدم ویزیت روزانه");
            methodName="S_Adam_Visit_Day";
            runAsync();
        }

        if (controler.equals("Month")){
            txtHead.setText("عدم ویزیت ماهانه");
            methodName="S_Adam_Visit_Month";
            runAsync();
        }


        lyContent = (RelativeLayout) findViewById(R.id.lyContent);
        lyEror = (RelativeLayout) findViewById(R.id.lyEror);
        lyProgress = (RelativeLayout) findViewById(R.id.lyProgress);
        txtEror = (TextView) findViewById(R.id.txtEror);

        lyEror.setVisibility(View.GONE);
        lyContent.setVisibility(View.GONE);
        lyProgress.setVisibility(View.VISIBLE);

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


    public class AdamVisitAsync extends AsyncTask<Void, String, String> {

        Boolean isonline = NetworkTools.isOnline(ActivityListOfAdamVisit.this);

        @Override
        protected void onPostExecute(String state) {

            adamVisitAsync=null;

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


                lyContent.setVisibility(View.VISIBLE);
                lyProgress.setVisibility(View.GONE);
                lyEror.setVisibility(View.GONE);
                row_adapter = new AdamVisitInfoAdapter(items);

                row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                row_AdamVisit.setLayoutManager(row_manager);

                row_AdamVisit.setAdapter(row_adapter);
                row_adapter.setState(1);

                if (controler.equals("Date")){
                  row_adapter.setChecker("date");
                }

                if (controler.equals("Month")){
                     row_adapter.setChecker("month");
                }
                row_adapter.setActivity(ActivityListOfAdamVisit.this);

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

                      if (isonline) {
                try {

                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), methodName, datas).getProperty(0);

                    if (request2.getPropertyCount() <= 0) {
                        return "Null";
                    }

                    for (int i = 0; i < request2.getPropertyCount(); i++) {
                        SoapObject sp = (SoapObject) request2.getProperty(i);
                        Log.e("tttttttttttt", "" + sp);

                        AdamVisitInfo item = new AdamVisitInfo();

                        item.setCode(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                        item.setName(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                        item.setCount(NetworkTools.getSoapPropertyAsNullableString(sp, 2));

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
        if (adamVisitAsync!=null)
        {
            return;
        }
        else {
            adamVisitAsync=new AdamVisitAsync();
            adamVisitAsync.execute();
        }
    }
}
