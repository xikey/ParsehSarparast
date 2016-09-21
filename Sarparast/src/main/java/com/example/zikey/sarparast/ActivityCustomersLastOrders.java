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

public class ActivityCustomersLastOrders extends AppCompatActivity {

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

    private  String code;

  private ArrayList<CustomersInfo> customersInfos = new ArrayList<>();
//
   private  RecyclerView row_orders;
   private  RecyclerView.LayoutManager row_manager;
   private  CustomersLastOrdersAdapter row_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_last_orders);

        code = getIntent().getStringExtra("code");

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
                G.hideSoftKeyboard(ActivityCustomersLastOrders.this);
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

        new  CustomersLastOrdersAsync().execute();

    }

    public class CustomersLastOrdersAsync extends AsyncTask<Void, String, String> {

        Boolean isonline = NetworkTools.isOnline(ActivityCustomersLastOrders.this);

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
                row_adapter = new CustomersLastOrdersAdapter(customersInfos);
//                adapter.setAct(ActivityAnalyseOfSefareshat.this);
                row_orders = (RecyclerView) findViewById(R.id.row_orders);

                row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                row_orders.setLayoutManager(row_manager);

                row_adapter.setActivity(ActivityCustomersLastOrders.this);
                row_orders.setAdapter(row_adapter);

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
            datas.put("code",code);

            if (isonline) {
                try {
                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_GetListOf_Customers_Last_Orders", datas).getProperty(0);

                    if (request2.getPropertyCount() <= 0) {
                        return "Null";
                    }

                    for (int i = 0; i < request2.getPropertyCount(); i++) {
                        SoapObject sp = (SoapObject) request2.getProperty(i);

                        CustomersInfo customersInfo = new CustomersInfo();

                        customersInfo.set_IDHeaeder(NetworkTools.getSoapPropertyAsNullableString(sp,0));
                        customersInfo.set_Date(NetworkTools.getSoapPropertyAsNullableString(sp,1));
                        customersInfo.set_Price(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp,2).toString())));
                        customersInfo.set_TasvieNashode(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp,3).toString())));
                        customersInfo.set_Naghd(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp,4).toString())));
                        customersInfo.set_Cheque(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp,5).toString())));
                        customersInfo.set_Variz(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp,6).toString())));
                        customersInfo.set_Elamie(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp,7).toString())));
                        customersInfo.set_Takhfif(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp,8).toString())));
                        customersInfo.set_Bargashti(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp,9).toString())));

                        customersInfos.add(customersInfo);
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
