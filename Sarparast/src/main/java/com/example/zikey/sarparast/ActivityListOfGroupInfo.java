package com.example.zikey.sarparast;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

public class ActivityListOfGroupInfo extends AppCompatActivity {

    private PreferenceHelper preferenceHelper;

   private   ImageView imgBack;
   private  String groupName;
   private TextView txtGroup;

   private TextView txtForoosh;
   private TextView txtBargasht;
   private TextView txtKhales;
   private TextView txtTasvieN;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ist_of_group_info);

        preferenceHelper = new PreferenceHelper(this);

        imgBack= (ImageView) findViewById(R.id.imgBack);
        txtGroup = (TextView) findViewById(R.id.txtNumber);
        txtForoosh= (TextView) findViewById(R.id.txtTForoosh);
        txtBargasht= (TextView) findViewById(R.id.txtBargasht);
        txtKhales= (TextView) findViewById(R.id.txtKhales);
        txtTasvieN= (TextView) findViewById(R.id.txtTasvieN);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        groupName = getIntent().getStringExtra("Group_Asli");

        txtGroup.setText(groupName);

        new GroupVisitorAsync().execute();

    }

    public class GroupVisitorAsync extends AsyncTask<Void,String,String> {
        Boolean isonline= NetworkTools.isOnline(ActivityListOfGroupInfo.this);
        GroupInfo gi = new GroupInfo();

        ProgressDialog dialog;


        @Override
        protected void onPostExecute(String state) {

            if (state.equals("Null")){
                new AlertDialog.Builder(ActivityListOfGroupInfo.this)
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
                txtForoosh.setText(gi.getForoosh());
                txtBargasht.setText(gi.getBargasht());
                txtKhales.setText(gi.getKhales());
                txtTasvieN.setText(gi.getTasvieNashode());

                if (dialog != null)
                    dialog.dismiss();
            }
            else  if (state.equals("NotOnline")){
                new AlertDialog.Builder(ActivityListOfGroupInfo.this)
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

                dialog = ProgressDialog.show(ActivityListOfGroupInfo.this, "", "دریافت اطلاعات");
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

                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_AnalyzeOFForooshGMoshtari", datas).getProperty(0);
                    SoapObject sp = (SoapObject) request2.getProperty(0);

                    gi.setForoosh(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 0).toString())));
                    gi.setBargasht(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 1).toString())));
                    gi.setKhales(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 2).toString())));
                    gi.setTasvieNashode(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 3).toString())));

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



