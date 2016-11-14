package com.example.zikey.sarparast;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ActivityListOfTSvisitorInfo extends AppCompatActivity {


 private  String code;
 private    String name;
 private   ImageView imgBack;
 private   TextView username;
 private   EditText edtSearch;

    public PreferenceHelper preferenceHelper;

    private   RecyclerView row_TSFactor;
    private   RecyclerView.LayoutManager row_manager;
    private   UserTSInfoAdapter row_adapter;

  private   ArrayList<TSFactorHeader> factors = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_tsvisitor_info);
        username = (TextView) findViewById(R.id.txtUserName);
        edtSearch = (EditText) findViewById(R.id.edtSearch);


        preferenceHelper = new PreferenceHelper(this);

        imgBack= (ImageView) findViewById(R.id.imgBack);
        row_TSFactor= (RecyclerView) findViewById(R.id.row_TSFactors);

        code = getIntent().getStringExtra("Code_Bazaryab");
        name = getIntent().getStringExtra("Name_Bazaryab");

        username.setText("تسویه نشده :"+name +"(از اول سال)");
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


        row_TSFactor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                G.hideSoftKeyboard(ActivityListOfTSvisitorInfo.this);
                return false;
            }
        });


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        new TSFactorHeaderAsync().execute();
    }

    public class TSFactorHeaderAsync extends AsyncTask<Void,String,String> {

        Boolean isonline= NetworkTools.isOnline(ActivityListOfTSvisitorInfo.this);

        ProgressDialog dialog;

        @Override
        protected void onPostExecute(String state) {

            if (state.equals("Null")){
                new AlertDialog.Builder(ActivityListOfTSvisitorInfo.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("اطلاعاتی جهت نمایش وجود ندارد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }

            if (state.equals("Online")) {
                row_TSFactor = (RecyclerView) findViewById(R.id.row_TSFactors);
                row_TSFactor.setFocusable(false);
                row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                row_TSFactor.setLayoutManager(row_manager);
                row_adapter = new UserTSInfoAdapter(factors);
                row_adapter.setActivity(ActivityListOfTSvisitorInfo.this);
                row_TSFactor.setAdapter(row_adapter);

                if (dialog != null)
                    dialog.dismiss();
            }
            else  if (state.equals("NotOnline")){
                new AlertDialog.Builder(ActivityListOfTSvisitorInfo.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("اتصال به اینترنت مقدور نمیباشد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }
        }

        @Override
        protected void onPreExecute() {
            //show
            if (isonline) {
                dialog = ProgressDialog.show(ActivityListOfTSvisitorInfo.this, "", "دریافت اطلاعات");
                super.onPreExecute();
            }
        }


        @Override
        protected String doInBackground(Void... voids) {

            HashMap<String, Object> datas = new HashMap<String, Object>();
            String tokenid =  preferenceHelper.getString(preferenceHelper.TOKEN_ID);

            datas.put("code",code);
            datas.put("TokenID",tokenid);


            if (isonline) {

                try {

                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_TS_AnalyseOfForoosh", datas).getProperty(0);

                    if (request2.getPropertyCount()<=0){
                        return "Null";

                    }


                    for (int i = 0; i < request2.getPropertyCount(); i++) {
                        SoapObject sp = (SoapObject) request2.getProperty(i);
                        if (sp.getPropertyCount()<=0){
                            return "Null";
                        }

                        TSFactorHeader factor = new TSFactorHeader();
                        factor.setSh_factro(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                        factor.setDate(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                        factor.setName_moshtari(NetworkTools.getSoapPropertyAsNullableString(sp, 3));
                        factor.setTs_price(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 2).toString())));
                        factor.setImgFactorDetail(R.drawable.lisst);

                        factor.setImgAghlam(R.drawable.aghlam);
                        factors.add(factor);
                    }

                } catch (Exception e) {
                    Log.e("iiiiiii", "connot read Soap");
                    e.printStackTrace();
                    return "Null";
                }
                return "Online";
            }
            return "NotOnline";
        }

    }

}
