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


public class ActivityListOfNewCustomers extends AppCompatActivity {

    private PreferenceHelper preferenceHelper;
    private ImageView imgBack;

    private EditText edtSearch;
    private  int state=0;

    private TextView txtEror;

    private RelativeLayout lyEror;
    private RelativeLayout lyContent;
    private RelativeLayout lyProgress;

    private ArrayList<CustomersInfo> Customers = new ArrayList<>();

    private RecyclerView row_customers;
    private RecyclerView.LayoutManager row_manager;
    private NewCustomersListAdapter row_adapter;


    @Override
    protected void onPause() {
      Customers.clear();
        super.onPause();
        state=2;
    }


    @Override
    protected void onRestart() {

        super.onRestart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_new_customers);

        edtSearch = (EditText) findViewById(R.id.edtSearch);

        state=0;


        row_customers = (RecyclerView) findViewById(R.id.row_customers);

        preferenceHelper = new PreferenceHelper(this);
        imgBack = (ImageView) findViewById(R.id.imgBack);


        lyContent = (RelativeLayout) findViewById(R.id.lyContent);
        lyEror = (RelativeLayout) findViewById(R.id.lyEror);
        lyProgress = (RelativeLayout) findViewById(R.id.lyProgress);
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

    @Override
    protected void onResume() {
        new GetListOfBazaryabAsync().execute();
        Log.e("onCreate","ON Create");
        super.onResume();
    }

    public class GetListOfBazaryabAsync extends AsyncTask<Void, String, String> {

        Boolean isonline = NetworkTools.isOnline(ActivityListOfNewCustomers.this);



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
                NewCustomersListAdapter newCustomersListAdapter = new NewCustomersListAdapter(Customers);
                newCustomersListAdapter.setAct(ActivityListOfNewCustomers.this);

                row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                row_customers.setLayoutManager(row_manager);
                row_adapter = newCustomersListAdapter;
                row_adapter.setActivity(ActivityListOfNewCustomers.this);



                row_customers.setAdapter(row_adapter);


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

            Log.e("tttttttttttt", "" + "Do in background is ok");

            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));

            if (isonline) {
                try {
                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_GetListOF_NewCustomers_Detail", datas).getProperty(0);

                    if (request2.getPropertyCount()<=0){
                    return  "Null" ;
                    }

                    for (int i = 0; i < request2.getPropertyCount(); i++) {
                        SoapObject sp = (SoapObject) request2.getProperty(i);
                       Log.e("tttttttttttt", "" + sp);

                        CustomersInfo customer = new CustomersInfo();



                        customer.set_Name(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                        customer.set_Tel(NetworkTools.getSoapPropertyAsNullableString(sp, 2));
                        customer.set_Mobile(NetworkTools.getSoapPropertyAsNullableString(sp, 3));
                        customer.set_Address(NetworkTools.getSoapPropertyAsNullableString(sp, 4));
                        customer.set_IDHeaeder(NetworkTools.getSoapPropertyAsNullableString(sp, 0));


                        Customers.add(customer);
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