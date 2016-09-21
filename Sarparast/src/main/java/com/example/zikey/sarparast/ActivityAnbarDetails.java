package com.example.zikey.sarparast;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityAnbarDetails extends AppCompatActivity {
    private PreferenceHelper preferenceHelper;
    private ImageView imgBack;
    private ArrayList<KalasInfo> Kalas = new ArrayList<>();

    private EditText edtSearch;

    private   RecyclerView row_Kala;
    private   RecyclerView.LayoutManager row_manager;
    private   AnbarInfoAdapter row_adapter;

    private FragmentManager manager;

    TextView txtHeader;

    private String state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anbar_details);

        row_Kala = (RecyclerView) findViewById(R.id.row_anbar);

        txtHeader = (TextView) findViewById(R.id.txtUserName);
        edtSearch = (EditText) findViewById(R.id.edtSearch);

        manager = getFragmentManager();

        row_Kala.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                G.hideSoftKeyboard(ActivityAnbarDetails.this);
                return false;
            }
        });

        state =getIntent().getStringExtra("State");

        preferenceHelper = new PreferenceHelper(this);
        imgBack= (ImageView) findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (state.equals("MojodiKol")){

            new MojoodiKolAsync().execute();
            Log.e("asynkTest","OK");

        }

        if (state.equals("News")){

            new TopNewsAsync().execute();
            Log.e("asynkTest","OK");

        }
        if (state.equals("ForooshVije")){

            new MojoodiJashnvareAsync().execute();
            Log.e("asynkTest","OK");

        }

     edtSearch   .addTextChangedListener(new TextWatcher() {
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

    public class MojoodiKolAsync extends AsyncTask<Void,String,String> {
       private int counter;

        Boolean isonline= NetworkTools.isOnline(ActivityAnbarDetails.this);
        ProgressDialog dialog;
        @Override
        protected void onPostExecute(String state) {

            if (state.equals("Online")) {

                row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                row_Kala.setLayoutManager(row_manager);
                row_adapter = new AnbarInfoAdapter(Kalas);
                row_adapter.setManager(manager);
                row_adapter.setActivity(ActivityAnbarDetails.this);
                row_Kala.setAdapter(row_adapter);

                txtHeader.setText(""+counter+" کالا ");

                if (dialog != null)
                    dialog.dismiss();
            }
            else  if (state.equals("NotOnline")){
                new AlertDialog.Builder(ActivityAnbarDetails.this)
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
                dialog = ProgressDialog.show(ActivityAnbarDetails.this, "", "دریافت اطلاعات");
                super.onPreExecute();
            }
        }
        @Override
        protected String doInBackground(Void... voids) {

            HashMap<String, Object> datas = new HashMap<String, Object>();
            String tokenid =  preferenceHelper.getString(preferenceHelper.TOKEN_ID);

            datas.put("TokenID",tokenid);
            datas.put("Firstindex","");
            datas.put("Lastindex","");

            if (isonline) {

                try {

                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Anbar_GetMojoodi", datas).getProperty(0);
//                    Log.e("testsoap","soap is "+request2);

                    counter = request2.getPropertyCount();

                    for (int i = 0; i < request2.getPropertyCount(); i++) {
                        SoapObject sp = (SoapObject) request2.getProperty(i);

                        KalasInfo kala = new KalasInfo();
                        kala.set_Code(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                        kala.set_Name(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                        kala.set_MojodiFiziki(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 2).toString())));
                        kala.set_GhabelForosh(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 3).toString())));
                        //kala.set_Row(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 4).toString())));
                        Log.i("KALa","Kala is "+sp);

                        Kalas.add(kala);

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




    public class TopNewsAsync extends AsyncTask<Void,String,String> {

        Boolean isonline= NetworkTools.isOnline(ActivityAnbarDetails.this);
        ProgressDialog dialog;
        private int counter;
        @Override
        protected void onPostExecute(String state) {

            if (state.equals("Online")) {

                row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                row_Kala.setLayoutManager(row_manager);
                row_adapter = new AnbarInfoAdapter(Kalas);
                row_adapter.setManager(manager);
                row_adapter.setActivity(ActivityAnbarDetails.this);
                row_Kala.setAdapter(row_adapter);


                txtHeader.setText(""+counter+" کالا ");

                if (dialog != null)
                    dialog.dismiss();
            }
            else  if (state.equals("NotOnline")){
                new AlertDialog.Builder(ActivityAnbarDetails.this)
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
                dialog = ProgressDialog.show(ActivityAnbarDetails.this, "", "دریافت اطلاعات");
                super.onPreExecute();
            }
        }
        @Override
        protected String doInBackground(Void... voids) {

            HashMap<String, Object> datas = new HashMap<String, Object>();
            String tokenid =  preferenceHelper.getString(preferenceHelper.TOKEN_ID);

            datas.put("TokenID",tokenid);
            datas.put("Firstindex","0");
            datas.put("Lastindex","50");

            if (isonline) {

                try {

                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Anbar_GetMojoodi_News", datas).getProperty(0);
//                    Log.e("testsoap","soap is "+request2);

                    for (int i = 0; i < request2.getPropertyCount(); i++) {
                        SoapObject sp = (SoapObject) request2.getProperty(i);

                        counter = request2.getPropertyCount();

                        KalasInfo kala = new KalasInfo();
                        kala.set_Code(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                        kala.set_Name(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                        kala.set_MojodiFiziki(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 2).toString())));
                        kala.set_GhabelForosh(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 3).toString())));
                       // kala.set_Row(NetworkTools.getSoapPropertyAsNullableString(sp, 4));

                        Kalas.add(kala);
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




    public class MojoodiJashnvareAsync extends AsyncTask<Void,String,String> {
        private int counter;

        Boolean isonline= NetworkTools.isOnline(ActivityAnbarDetails.this);
        ProgressDialog dialog;
        @Override
        protected void onPostExecute(String state) {

            if (state.equals("Online")) {

                row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                row_Kala.setLayoutManager(row_manager);
                row_adapter = new AnbarInfoAdapter(Kalas);
                row_adapter.setManager(manager);
                row_adapter.setActivity(ActivityAnbarDetails.this);
                row_Kala.setAdapter(row_adapter);

                txtHeader.setText(""+counter+" کالا ");

                if (dialog != null)
                    dialog.dismiss();
            }
            else  if (state.equals("NotOnline")){
                new AlertDialog.Builder(ActivityAnbarDetails.this)
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
                dialog = ProgressDialog.show(ActivityAnbarDetails.this, "", "دریافت اطلاعات");
                super.onPreExecute();
            }
        }
        @Override
        protected String doInBackground(Void... voids) {

            HashMap<String, Object> datas = new HashMap<String, Object>();
            String tokenid =  preferenceHelper.getString(preferenceHelper.TOKEN_ID);

            datas.put("TokenID",tokenid);
            datas.put("Firstindex","");
            datas.put("Lastindex","");

            if (isonline) {

                try {

                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Anbar_GetMojoodi_Jashnvare", datas).getProperty(0);
//                    Log.e("testsoap","soap is "+request2);

                    counter = request2.getPropertyCount();

                    for (int i = 0; i < request2.getPropertyCount(); i++) {
                        SoapObject sp = (SoapObject) request2.getProperty(i);

                        KalasInfo kala = new KalasInfo();
                        kala.set_Code(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                        kala.set_Name(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                        kala.set_MojodiFiziki(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 2).toString())));
                        kala.set_GhabelForosh(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 3).toString())));
//                        kala.set_Row(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 4).toString())));
//                        Log.i("KALa","Kala is "+sp);

                        Kalas.add(kala);

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
