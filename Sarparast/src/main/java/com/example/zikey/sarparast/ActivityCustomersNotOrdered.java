package com.example.zikey.sarparast;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
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

public class ActivityCustomersNotOrdered extends AppCompatActivity {

    private PreferenceHelper preferenceHelper;
    private ImageView imgBack;
    private TextView txtEror;
    private TextView txtTop;

    private RelativeLayout lyEror;
    private RelativeLayout lyContent;
    private RelativeLayout lyProgress;

    private RecyclerView row_customer;
    private RecyclerView.LayoutManager row_manager;
    private CustomersNotOrderedAdapter row_adapter;

    private EditText edtSearch;

    String value;



    ArrayList<BazaryabInfo> bazaryabs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_not_ordered);

        edtSearch = (EditText) findViewById(R.id.edtSearch);

        preferenceHelper = new PreferenceHelper(this);
        imgBack = (ImageView) findViewById(R.id.imgBack);

        row_customer = (RecyclerView) findViewById(R.id.row_customers);

//        row_customer.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                G.hideSoftKeyboard(ActivityCustomersNotOrdered.this);
//                return false;
//            }
//        });

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

        value = getIntent().getStringExtra("value");

      new   GetListOfCustomers().execute();

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


        public class GetListOfCustomers extends AsyncTask<Void, String, String> {

            Boolean isonline = NetworkTools.isOnline(ActivityCustomersNotOrdered.this);

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

                    Log.e("Is Online","Online is ok ");

//                adapter.setAct(ActivityAnalyseOfSefareshat.this);

                    row_adapter = new CustomersNotOrderedAdapter(bazaryabs);
                    row_adapter.setActivity(ActivityCustomersNotOrdered.this);
                    row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    row_customer.setLayoutManager(row_manager);
                    row_customer.setAdapter(row_adapter);

                    lyContent.setVisibility(View.VISIBLE);
                    lyProgress.setVisibility(View.GONE);

                }
                else if (state.equals("NotOnline"))

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
                datas.put("code", value);

                if (isonline) {
                    try {
                        SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Navigation_ListOfCustomers_Not_Ordered", datas).getProperty(0);

                        if (request2.getPropertyCount()<=0){
                            return  "Null" ;
                        }

                        for (int i = 0; i < request2.getPropertyCount(); i++) {
                            SoapObject sp = (SoapObject) request2.getProperty(i);

                            BazaryabInfo bazaryab = new BazaryabInfo();

                            bazaryab.set_Code(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                            bazaryab.set_Name(NetworkTools.getSoapPropertyAsNullableString(sp, 1));

                            bazaryab.set_Mobile(""+NetworkTools.getSoapPropertyAsNullableString(sp, 7));

                            //total is Header with Address
                            bazaryab.set_Total(NetworkTools.getSoapPropertyAsNullableString(sp, 6));
                            if (!TextUtils.isEmpty(NetworkTools.getSoapPropertyAsNullableString(sp, 3))) {
                                bazaryab.set_Tel(NetworkTools.getSoapPropertyAsNullableString(sp, 3));
                            }
                            else {
                                bazaryab.set_Tel("- - -");
                            }
                            bazaryab.set_Address(NetworkTools.getSoapPropertyAsNullableString(sp,2));

                            if (!TextUtils.isEmpty(NetworkTools.getSoapPropertyAsNullableString(sp, 4))) {
                                bazaryab.set_L(NetworkTools.getSoapPropertyAsNullableString(sp, 4));
                                bazaryab.set_W(NetworkTools.getSoapPropertyAsNullableString(sp, 5));

                            }
                            else {
                                bazaryab.set_L("0");
                                bazaryab.set_W("0");
                            }

                            bazaryabs.add(bazaryab);
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

}
