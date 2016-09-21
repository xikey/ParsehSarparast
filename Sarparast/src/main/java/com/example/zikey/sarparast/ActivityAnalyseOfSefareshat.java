package com.example.zikey.sarparast;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityAnalyseOfSefareshat extends AppCompatActivity {

    private PreferenceHelper preferenceHelper;
    private ImageView imgBack;
    private TextView txtEror;
    private TextView txtTop;

    private RelativeLayout lyEror;
    private RelativeLayout lyContent;
    private RelativeLayout lyProgress;



    private ArrayList<AnalyseSefareshatInfo> sefareshatInfos = new ArrayList<>();

    private RecyclerView row_sefaresh;
    private RecyclerView.LayoutManager row_manager;
    private AnalyseSefareshatAdapter row_adapter;

    private EditText edtSearch;

    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse_of_sefareshat);

        edtSearch = (EditText) findViewById(R.id.edtSearch);

        row_sefaresh = (RecyclerView) findViewById(R.id.row_sefareshat);

  edtSearch.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        row_adapter.getFilter().filter(editable);

    }
});

        if (getIntent().getExtras()!=null){

            value = getIntent().getStringExtra("State");

        }

        preferenceHelper = new PreferenceHelper(this);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        txtTop = (TextView) findViewById(R.id.txtTop);

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

        row_sefaresh.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
              // hideSoftKeyboard(ActivityAnalyseOfSefareshat.this);
                return false;
            }
        });


        if (value.equals("Mahane")){
            txtTop.setText("ماهانه");


        }

       if (value.equals("Manategh")){
                  txtTop.setText("مناطق");

              }
       if (value.equals("Date")){
                  txtTop.setText("تاریخ");
              }
       if (value.equals("Visitor")){
            txtTop.setText("ویزیتور");
              }


        new  GetListOfSefareshatASYNC().execute();

    }

    public class GetListOfSefareshatASYNC extends AsyncTask<Void, String, String> {

        Boolean isonline = NetworkTools.isOnline(ActivityAnalyseOfSefareshat.this);



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
                AnalyseSefareshatAdapter adapter = new AnalyseSefareshatAdapter(sefareshatInfos);
//                adapter.setAct(ActivityAnalyseOfSefareshat.this);


                row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                row_sefaresh.setLayoutManager(row_manager);
                row_adapter = adapter;
                row_adapter.setAct(ActivityAnalyseOfSefareshat.this);
                row_adapter.setCgroup(value);
                if (value.equals("Visitor")){
                     row_adapter.setState(1);
                }
                row_sefaresh.setAdapter(row_adapter);

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
            datas.put("Group", value);

            if (isonline) {
                try {
                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Analyse_OF_Sefareshat", datas).getProperty(0);

                    if (request2.getPropertyCount()<=0){
                        return  "Null" ;
                    }

                    for (int i = 0; i < request2.getPropertyCount(); i++) {
                        SoapObject sp = (SoapObject) request2.getProperty(i);

                        AnalyseSefareshatInfo sefaresh = new AnalyseSefareshatInfo();

                       sefaresh.set_Name(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                       sefaresh.set_TForosh(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                       sefaresh.set_RForoosh(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 2).toString())));
                       sefaresh.set_TBargasht(NetworkTools.getSoapPropertyAsNullableString(sp, 3));
                       sefaresh.set_RBargasht(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 4).toString())));
                       sefaresh.set_KhalesForosh(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 5).toString())));

                       sefaresh.set_GroupCode(NetworkTools.getSoapPropertyAsNullableString(sp,19));



                        sefareshatInfos.add(sefaresh);
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


    public    void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }



}