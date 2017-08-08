package com.example.zikey.sarparast;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
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

public class ActivitySefareshDetails extends AppCompatActivity {



    public static  String Price;

    private TextView txtPrice;
    private TextView txtNahveDaryaft;
    private TextView txtTozihat;

    private  String id_header;
    private  String tsPrice;

    private  String tozihat;
    private  String nahveVosol;

    public PreferenceHelper preferenceHelper;

    private ImageView imgBack;

    private TextView txtNumber;


    SelectUserCommunicator communicator;

    private  int vaziat;


    private RecyclerView row_TSFactorDetail;
    private   android.support.v7.widget.RecyclerView.LayoutManager row_manager;
    private   RecyclerView.Adapter row_adapter;

    ArrayList<TsFactorDetail> factors = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sefaresh_details);

        nahveVosol = getIntent().getStringExtra("Nahveh");
        tozihat = getIntent().getStringExtra("Tozihat");

        txtPrice = (TextView) findViewById(R.id.txtPrice);
        txtNahveDaryaft = (TextView) findViewById(R.id.txtNahveDaryaft);
        txtTozihat = (TextView) findViewById(R.id.txtTozihat);

        imgBack= (ImageView) findViewById(R.id.imgBack);


        txtNumber = (TextView) findViewById(R.id.txtNumber);
        txtNumber.setText(""+G.ID);
        txtPrice.setText(""+Price);


        if (!TextUtils.isEmpty(nahveVosol)){
            txtNahveDaryaft.setText(nahveVosol);
        }

 if (!TextUtils.isEmpty(tozihat)){
            txtTozihat.setText(tozihat);
        }

        preferenceHelper = new PreferenceHelper(this);
//        id_header = getIntent().getStringExtra("id_header");
//        tsPrice = getIntent().getStringExtra("price_tasvie_nashode");





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
            row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            row_TSFactorDetail.setLayoutManager(row_manager);
            row_adapter = new SefareshDetailsAdapter(factors);
            row_TSFactorDetail.setAdapter(row_adapter);

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
            dialog = ProgressDialog.show(ActivitySefareshDetails.this,"",ss1);
            super.onPreExecute();
        }



        @Override
        protected Void doInBackground(Void... voids) {
            HashMap<String, Object> datas = new HashMap<String, Object>();
            String tokenid =  preferenceHelper.getString(preferenceHelper.TOKEN_ID);

            datas.put("sefareshID",G.ID);
            Log.e("mmm","sefaresh id is "+ G.ID);
            datas.put("TokenID",tokenid);

            try {

                SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://"+preferenceHelper.getString(NetworkTools.URL),"S_ListOfSefareshDetails",datas).getProperty(0);

                for (int i=0;i<request2.getPropertyCount();i++){
                    SoapObject sp= (SoapObject) request2.getProperty(i);

                    TsFactorDetail factor = new TsFactorDetail();
                    factor.setRow(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp,0).toString())));
                    factor.setCodeKala(NetworkTools.getSoapPropertyAsNullableString(sp,1));
                    factor.setNameKala(NetworkTools.getSoapPropertyAsNullableString(sp,2));
                    factor.setTedad(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp,3).toString())));
                    factor.setFi(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp,4).toString())));
                    factor.setPrice(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp,5).toString())));

                    factors.add(factor);

                }

            } catch (Exception e) {
                Log.e("iiiiiii","connot read Soap");
                e.printStackTrace();
            }
            return null;
        }

    }


}
