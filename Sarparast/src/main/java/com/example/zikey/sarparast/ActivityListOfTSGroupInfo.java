package com.example.zikey.sarparast;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class ActivityListOfTSGroupInfo extends AppCompatActivity {

    private PreferenceHelper preferenceHelper;
    private   ImageView imgBack;
    private EditText edtSearch;

    private TextView username;

    private  String groupName;

    private String searchString = null;

    private RecyclerView row_TSGroup;
    private   RecyclerView.LayoutManager row_manager;
    private   UserTSInfoAdapter row_adapter;

   private ArrayList<TSFactorHeader> factors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_ts_group_info);

        groupName = getIntent().getStringExtra("Group_Asli");

//        Log.e("rrrrrrrrrrrrrrrrr",""+groupName);

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

        preferenceHelper = new PreferenceHelper(this);
        row_TSGroup= (RecyclerView) findViewById(R.id.row_TSGroup);

        username = (TextView) findViewById(R.id.txtUserName);


        imgBack= (ImageView) findViewById(R.id.imgBack);

        row_TSGroup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                G.hideSoftKeyboard(ActivityListOfTSGroupInfo.this);
                return false;
            }
        });


        username.setText("تسویه نشده گروه "+groupName);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

      new  TSGroupHeaderAsync().execute();
    }


    public class TSGroupHeaderAsync extends AsyncTask<Void,String,String> {

        Boolean isonline= NetworkTools.isOnline(ActivityListOfTSGroupInfo.this);
        ProgressDialog dialog;

        @Override

        protected void onPostExecute(String state) {
            if (state.equals("Null")){
                new AlertDialog.Builder(ActivityListOfTSGroupInfo.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("طلاعاتی جهت نمایش وجود ندارد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }

            if (state.equals("Online")) {

                row_TSGroup = (RecyclerView) findViewById(R.id.row_TSGroup);
                row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                row_TSGroup.setLayoutManager(row_manager);
                row_adapter = new UserTSInfoAdapter(factors);
                row_adapter.setActivity(ActivityListOfTSGroupInfo.this);
                row_TSGroup.setAdapter(row_adapter);

                if (dialog != null)
                    dialog.dismiss();
            }
            else  if (state.equals("NotOnline")) {
                new AlertDialog.Builder(ActivityListOfTSGroupInfo.this)
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
            if (isonline) {

                dialog = ProgressDialog.show(ActivityListOfTSGroupInfo.this, "", "دریافت اطلاعات");
                super.onPreExecute();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID",preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            datas.put("group",groupName);

            if (isonline) {
                try {
                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_GetListOfTSGroupMInfo", datas).getProperty(0);
if (request2.getPropertyCount()<=0){
    return "Null";
}
                    for (int i = 0; i < request2.getPropertyCount(); i++) {
                        SoapObject sp = (SoapObject) request2.getProperty(i);
                        Log.e("tttttttttttt", "" + sp);

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
