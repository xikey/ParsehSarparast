package com.example.zikey.sarparast;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityAcceptRequests extends AppCompatActivity {

    private ImageView btnShowDetails;
    private ImageView btnAccept;
    private ImageView btnCancel;
    private ImageView btnForward;

    private String code;

    private TextView txtKol;
    private TextView txtTaeed;
    private TextView txtLaghv;
    private TextView txtErja;

    private TextView txtRKol;
    private TextView txtRTaeed;
    private TextView txtRLaghv;
    private TextView txtRErja;

    private PreferenceHelper preferenceHelper;
    private ImageView imgBack;

    static String state = "";

    private ArrayList<RequestsInfo> requestsInfos = new ArrayList<>();

    private RequestsInfo countrinfo = new RequestsInfo();

    private RecyclerView row_Requests;
    private RecyclerView.LayoutManager row_manager;
    private RequestsAdapter row_adapter;

    private EditText edtSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView
                (R.layout.activity_accept_requests);


        code = getIntent().getStringExtra("Code");

        row_Requests = (RecyclerView) findViewById(R.id.row_requests);

        btnShowDetails = (ImageView) findViewById(R.id.btnShowDetails);
        btnAccept = (ImageView) findViewById(R.id.btnAccept);
        btnCancel = (ImageView) findViewById(R.id.btnCancel);
        btnForward = (ImageView) findViewById(R.id.btnForward);

        txtKol = (TextView) findViewById(R.id.txtKol);
        txtTaeed = (TextView) findViewById(R.id.txtTaeed);
        txtLaghv = (TextView) findViewById(R.id.txtLaghv);
        txtErja = (TextView) findViewById(R.id.txtErja);

        txtRKol = (TextView) findViewById(R.id.txtRKol);
        txtRTaeed = (TextView) findViewById(R.id.txtRTaeed);
        txtRLaghv = (TextView) findViewById(R.id.txtRLaghv);
        txtRErja = (TextView) findViewById(R.id.txtRErja);

        edtSearch = (EditText) findViewById(R.id.edtSearch);

        state = "negative";


        preferenceHelper = new PreferenceHelper(this);
        imgBack = (ImageView) findViewById(R.id.imgBack);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        new TSFactorHeaderAsync().execute();


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

        row_Requests.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                G.hideSoftKeyboard(ActivityAcceptRequests.this);
                return false;
            }
        });


    }

    @Override
    protected void onResume() {


        if (state.equals("paused")) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
//        state = "paused";
        super.onPause();
    }
    //___________________________________SHOW    DETAILS___________________________________________

    private class TSFactorHeaderAsync extends AsyncTask<Void, String, String> {

        Boolean isonline = NetworkTools.isOnline(ActivityAcceptRequests.this);
        boolean isnull = true;

        ProgressDialog dialog;

        @Override
        protected void onPostExecute(String state) {

            if (state.equals("Null")) {
                new AlertDialog.Builder(ActivityAcceptRequests.this)
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

                txtKol.setText("" + countrinfo.get_TedadKol().toString());
                txtTaeed.setText("" + countrinfo.get_TedadTaeed().toString());
                txtLaghv.setText("" + countrinfo.get_TedadLaghv().toString());
                txtErja.setText("" + countrinfo.get_TedadBargashti().toString());

                txtRKol.setText(("" + countrinfo.get_RialTedadKol().toString()));
                txtRTaeed.setText(("" + countrinfo.get_RialTedadTaeed().toString()));
                txtRLaghv.setText(("" + countrinfo.get_RialTedadLaghv().toString()));
                txtRErja.setText(("" + countrinfo.get_RialTedadBargashti().toString()));

                row_Requests.setFocusable(false);
                row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                row_Requests.setLayoutManager(row_manager);
                row_adapter = new RequestsAdapter(requestsInfos);
                row_adapter.setActivity(ActivityAcceptRequests.this);
                row_adapter.setManager(getFragmentManager());
                row_Requests.setAdapter(row_adapter);

                if (dialog != null)
                    dialog.dismiss();
            } else if (state.equals("NotOnline")) {
                new AlertDialog.Builder(ActivityAcceptRequests.this)
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
                dialog = ProgressDialog.show(ActivityAcceptRequests.this, "", "دریافت اطلاعات");
                super.onPreExecute();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {

            HashMap<String, Object> datas = new HashMap<String, Object>();
            HashMap<String, Object> data = new HashMap<String, Object>();


            String tokenid = preferenceHelper.getString(preferenceHelper.TOKEN_ID);

            datas.put("TokenID", tokenid);
            datas.put("code", code);

            data.put("TokenID", tokenid);
            data.put("Code", code);

            Log.e("code", "Code IS " + code);


            if (isonline) {

                try {

                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_All_Requests", data).getProperty(0);
                    if (request2.getPropertyCount() <= 0) {
                        return "Null";
                    }
                    SoapObject Counter = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Requests_Counter", datas).getProperty(0);

                    SoapObject cr = (SoapObject) Counter.getProperty(0);


                    countrinfo.set_TedadKol(NetworkTools.getSoapPropertyAsNullableString(cr, 0));
                    countrinfo.set_TedadTaeed(NetworkTools.getSoapPropertyAsNullableString(cr, 1));
                    countrinfo.set_TedadBargashti(NetworkTools.getSoapPropertyAsNullableString(cr, 2));
                    countrinfo.set_TedadLaghv(NetworkTools.getSoapPropertyAsNullableString(cr, 3));
                    countrinfo.set_RialTedadKol((String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(cr, 4).toString()))));
                    countrinfo.set_RialTedadTaeed((String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(cr, 5).toString()))));
                    countrinfo.set_RialTedadBargashti((String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(cr, 6).toString()))));
                    countrinfo.set_RialTedadLaghv((String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(cr, 7).toString()))));

                    for (int i = 0; i < request2.getPropertyCount(); i++) {

                        SoapObject sp = (SoapObject) request2.getProperty(i);
                        Log.e("fffffff", "request is " + sp);

                        RequestsInfo requestsInfo = new RequestsInfo();
                        requestsInfo.set_SefareshID(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                        requestsInfo.set_OrderDate(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                        requestsInfo.set_CodeMoshtari(NetworkTools.getSoapPropertyAsNullableString(sp, 2));
                        requestsInfo.set_NameMoshtari(NetworkTools.getSoapPropertyAsNullableString(sp, 3));
                        requestsInfo.set_NameBazaryab(NetworkTools.getSoapPropertyAsNullableString(sp, 5));
                        requestsInfo.set_PKolSefaresh(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 7).toString())));
                        requestsInfo.set_MandeMoshtari(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 6).toString())));
                        requestsInfo.set_CntVBank(NetworkTools.getSoapPropertyAsNullableString(sp, 8));
                        requestsInfo.set_SumVBank(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 9).toString())));
                        requestsInfo.set_CntHoghooghi(NetworkTools.getSoapPropertyAsNullableString(sp, 10));
                        requestsInfo.set_SumHoghooghi(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 11).toString())));
                        requestsInfo.set_VaziatSefarersh(Integer.parseInt(NetworkTools.getSoapPropertyAsNullableString(sp, 12)));
                        requestsInfo.set_Tel(NetworkTools.getSoapPropertyAsNullableString(sp, 13));
                        requestsInfo.set_Mobile(NetworkTools.getSoapPropertyAsNullableString(sp, 14));
                        requestsInfo.set_NahveVosol(NetworkTools.getSoapPropertyAsNullableString(sp, 15));
                        requestsInfo.set_Tozihat(NetworkTools.getSoapPropertyAsNullableString(sp, 16));

                        String mandeHesab = NetworkTools.getSoapPropertyAsNullableString(sp, 6).toString();
                        String chekDarJaryan = NetworkTools.getSoapPropertyAsNullableString(sp, 9).toString();

                        requestsInfo.setMandeEtebar(calculateMandehEtebar(mandeHesab, chekDarJaryan));
                        requestsInfos.add(requestsInfo);
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

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private String calculateMandehEtebar(String mandehHesab, String rialiCheq) {

        if (isStringNullOrEmpty(mandehHesab) || isStringNullOrEmpty(rialiCheq)) return "-";

        long mande = Long.parseLong(mandehHesab);
        long rialChek = Long.parseLong(rialiCheq);

        long output = mande + rialChek;

        return String.format("%,d", output);


    }


    private boolean isStringNullOrEmpty(String s) {

        if (s == null || TextUtils.isEmpty(s))
            return true;

        return false;
    }

}
