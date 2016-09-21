package com.example.zikey.sarparast;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityListOfTSVisitorInfoFactorDetails extends AppCompatActivity {

     private  String id_header;
     private  String tsPrice;
     public PreferenceHelper preferenceHelper;

     private ImageView imgBack;

     private TextView txtShFactor;
     private TextView txtPrice;
     private TextView txtTasvieNashode;


     private RecyclerView row_TSFactorDetail;
     private   android.support.v7.widget.RecyclerView.LayoutManager row_manager;
     private   RecyclerView.Adapter row_adapter;

   ArrayList<TsFactorDetail> factors = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_tsvisitor_info_factor_details);

        imgBack= (ImageView) findViewById(R.id.imgBack);



        preferenceHelper = new PreferenceHelper(this);
        id_header = getIntent().getStringExtra("id_header");
        tsPrice = getIntent().getStringExtra("price_tasvie_nashode");



        txtShFactor = (TextView) findViewById(R.id.txtNumber);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        txtTasvieNashode =(TextView) findViewById(R.id.txtTasvieNashode);

        row_TSFactorDetail = (RecyclerView) findViewById(R.id.row_TSFactorDetails);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        new TSFactorDetailAsync().execute();
    }



      class TSFactorDetailAsync extends AsyncTask<Void,UserInfo,Void> {

        ProgressDialog dialog;
          String factorPrice;

        @Override
        protected void onPostExecute(Void aVoid) {
            //hide


            row_TSFactorDetail   = (RecyclerView) findViewById(R.id.row_TSFactorDetails);
            row_TSFactorDetail.setFocusable(false);
            row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            row_TSFactorDetail.setLayoutManager(row_manager);
            row_adapter = new TSFactorDetailsAdapter(factors);
            row_TSFactorDetail.setAdapter(row_adapter);

            txtShFactor.setText(id_header);
            txtTasvieNashode.setText(tsPrice);
            txtPrice.setText(factorPrice);

            if (dialog != null)
                dialog.dismiss();
        }


        @Override
        protected void onPreExecute() {

            String s= "دریافت اطلاعات";
            String title="ورود";
            SpannableString ss1=  new SpannableString(s);
            ss1.setSpan(new RelativeSizeSpan(1f), 0, ss1.length(), 0);

            //show
            dialog = ProgressDialog.show(ActivityListOfTSVisitorInfoFactorDetails.this,"",ss1);
            super.onPreExecute();
        }



        @Override
        protected Void doInBackground(Void... voids) {
            HashMap<String, Object> datas = new HashMap<String, Object>();
            String tokenid =  preferenceHelper.getString(preferenceHelper.TOKEN_ID);

            datas.put("factor_num",id_header);
            datas.put("TokenID",tokenid);

            try {

                SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://"+preferenceHelper.getString(NetworkTools.URL),"S_ListOfTSVisitorFactorDetails",datas).getProperty(0);



                for (int i=0;i<request2.getPropertyCount();i++){
                    SoapObject sp= (SoapObject) request2.getProperty(i);

                    TsFactorDetail factor = new TsFactorDetail();
                    factor.setCodeKala(NetworkTools.getSoapPropertyAsNullableString(sp,0));
                    factor.setNameKala(NetworkTools.getSoapPropertyAsNullableString(sp,1));
                    factor.setTedad(NetworkTools.getSoapPropertyAsNullableString(sp,2));
                    factor.setFi(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp,3).toString())));
                    factor.setPrice(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp,9).toString())));
                    factor.setTakhfifD(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp,4).toString())));
                    factor.setPriceWTakhfif(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp,5).toString())));

                    factor.setRow(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp,8).toString())));


                    factor.setPriceTakhsisNayafte(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp,7).toString())));

                    if (i==0){
                        factorPrice = String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp,6).toString()));
                    }
                    factors.add(factor);

                }

            } catch (Exception e) {
                Log.e("iiiiiii","connot read Soap");
                e.printStackTrace();

            }
            return null;
        }


    }

//
//    public static void hideSoftKeyboard(Activity activity) {
//        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
//    }
//
//
//


}
