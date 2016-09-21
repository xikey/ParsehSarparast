package com.example.zikey.sarparast;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

public class ActiviyListOfTSVisitorInfoFactorinfo extends AppCompatActivity {
private  ImageView imgBack;


private  TextView txtShFactor;
private  TextView txtCodeMoshtary;
private  TextView txtNameMoshtary;
private  TextView txtShomare;
private  TextView txtDate;
private  TextView txtNameAmel;
private  TextView txtShTimTozi;
private  TextView txtDateTimTozi;
private  TextView txtEtebar;
private  TextView txtCodeForosh;

private  String id_header;

    public PreferenceHelper preferenceHelper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_list_of_tsvisitor_info_factor);

        preferenceHelper = new PreferenceHelper(this);

        imgBack= (ImageView) findViewById(R.id.imgBack);


        id_header = getIntent().getStringExtra("id_header");

        txtShFactor = (TextView) findViewById(R.id.txtNumber);
        txtCodeMoshtary= (TextView) findViewById(R.id.txtPrice);
        txtNameMoshtary= (TextView) findViewById(R.id.txtNameMoshtary);
        txtShomare= (TextView) findViewById(R.id.txtShomare);
        txtDate= (TextView) findViewById(R.id.txtCode);
        txtNameAmel= (TextView) findViewById(R.id.txtNameAmel);
        txtShTimTozi= (TextView) findViewById(R.id.txtShTimTozi);
        txtDateTimTozi= (TextView) findViewById(R.id.txtDateTimTozi);
        txtEtebar= (TextView) findViewById(R.id.txtEtebar);
        txtCodeForosh= (TextView) findViewById(R.id.txtCodeForosh);




        txtShFactor.setText(id_header);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        new FactorInfoAsync().execute();
    }

    public class FactorInfoAsync extends AsyncTask<Void,UserInfo,Void> {
        ProgressDialog dialog;
        TSFactorInfo detail = new TSFactorInfo();


        @Override
        protected void onPostExecute(Void aVoid) {

            txtCodeMoshtary.setText(""+detail.getCodeMoshtari());
            txtNameMoshtary.setText(detail.getNameMoshtari());
            txtShomare.setText(detail.getShomare());
            txtDate.setText(detail.getDate());
            txtNameAmel.setText(detail.getNameAmel());
            txtShTimTozi.setText(detail.getShTimPTozi());
           txtDateTimTozi.setText(detail.getDateTimPTozi() );
           txtEtebar.setText(detail.getDateEtebar());
            txtCodeForosh.setText(detail.getCodeMarkazForosh());

            if (dialog != null)
                dialog.dismiss();

        }


        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ActiviyListOfTSVisitorInfoFactorinfo.this, "", "دریافت اطلاعات");
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            HashMap<String, Object> datas = new HashMap<String, Object>();


            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            datas.put("factor_num", id_header);


            try {

                SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_ListOfTSVisitorFactorInfo", datas).getProperty(0);
                SoapObject sp = (SoapObject) request2.getProperty(0);
                Log.e("wqqqqqqqqqqq", "info is " + sp);

                detail.setCodeMoshtari(NetworkTools.getSoapPropertyAsNullableString(sp, 2));
                detail.setNameMoshtari(NetworkTools.getSoapPropertyAsNullableString(sp, 3));
                detail.setShomare(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                detail.setShFactor(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                detail.setDate(NetworkTools.getSoapPropertyAsNullableString(sp, 4));
                detail.setNameAmel(NetworkTools.getSoapPropertyAsNullableString(sp, 5));
                detail.setShTimPTozi(NetworkTools.getSoapPropertyAsNullableString(sp, 6));
                detail.setDateEtebar(NetworkTools.getSoapPropertyAsNullableString(sp, 7));
                detail.setDateTimPTozi(NetworkTools.getSoapPropertyAsNullableString(sp, 8));
                detail.setCodeMarkazForosh(NetworkTools.getSoapPropertyAsNullableString(sp, 9));

            } catch (Exception e) {
                Log.e("iiiiiii", "connot read Soap");
                e.printStackTrace();
            }
            return null;
        }


    }
    }
