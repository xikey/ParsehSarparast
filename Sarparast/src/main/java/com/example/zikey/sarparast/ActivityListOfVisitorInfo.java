package com.example.zikey.sarparast;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class ActivityListOfVisitorInfo extends AppCompatActivity {
    VisitorInfo visitor = new VisitorInfo();
    private TextView btnDarsad;
    private String code;

    private PreferenceHelper preferenceHelper;

 private TextView txtcodeB;
 private TextView txtNameB;
 private TextView txtTFForoosh;
 private TextView txtRFForoosh;
 private TextView txtTFBargasht;
 private TextView txtRFBargasht;
 private TextView txtKhalesT;
 private TextView txtFRGDore;
 private TextView txtAVGsatr;
 private TextView txtAVGForoosh;
 private TextView txtSoodR;
 private TextView txtSoodD;
 private TextView txtKhalesForoosh;

 private RatingBar visitorRate;

 private ImageView imgSMS;
 private ImageView imgCall;

 private ImageView imgBack;

 private String tell;

    public CustomGauge gauge2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_visitor_info);
        preferenceHelper = new PreferenceHelper(this);
        btnDarsad = (TextView) findViewById(R.id.btnDarsad);

        code = getIntent().getStringExtra("Code_Bazaryab");

        imgBack= (ImageView) findViewById(R.id.imgBack);

        txtcodeB = (TextView) findViewById(R.id.txtPrice);
        txtNameB = (TextView) findViewById(R.id.txtNumber);
        txtTFForoosh = (TextView) findViewById(R.id.txtTFForoosh);
        txtRFForoosh = (TextView) findViewById(R.id.txtRFForoosh);
        txtTFBargasht = (TextView) findViewById(R.id.txtTFBargasht);
        txtRFBargasht = (TextView) findViewById(R.id.txtRFBargasht);
        txtKhalesT = (TextView) findViewById(R.id.txtKhalesT);
        txtFRGDore = (TextView) findViewById(R.id.txtFRGDore);
        txtAVGsatr = (TextView) findViewById(R.id.txtAVGsatr);
        txtAVGForoosh = (TextView) findViewById(R.id.txtAVGForoosh);
        txtSoodR = (TextView) findViewById(R.id.txtSoodR);
        txtSoodD = (TextView) findViewById(R.id.txtSoodD);
        txtKhalesForoosh = (TextView) findViewById(R.id.txtKhalesForoosh);

        visitorRate = (RatingBar) findViewById(R.id.ratingBar);

        imgCall= (ImageView) findViewById(R.id.imgCall);
        imgSMS = (ImageView) findViewById(R.id.imgSMS);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        gauge2 = (CustomGauge) findViewById(R.id.gauge2);


        new VisitorsInfoAsync().execute();


    }

    public class VisitorsInfoAsync extends AsyncTask<Void,String,String > {

        ProgressDialog dialog;

        Boolean isonline= NetworkTools.isOnline(ActivityListOfVisitorInfo.this);

        @Override
        protected void onPostExecute(String value) {

            if (value.equals("Null")){
                new AlertDialog.Builder(ActivityListOfVisitorInfo.this)
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

            if (value.equals("Online")) {
                txtcodeB.setText(visitor.get_CodeB());
                txtNameB.setText("" + visitor.get_NameB());
                txtTFForoosh.setText("" + visitor.get_TForoosh());
                txtRFForoosh.setText("" + visitor.get_SumForoosh());
                txtTFBargasht.setText("" + visitor.get_TBargasht());
                txtRFBargasht.setText("" + visitor.get_SumBargasht());
                txtKhalesT.setText("" + visitor.get_KhalesT());
                txtKhalesForoosh.setText("" + visitor.get_KhalesR());
                txtFRGDore.setText("" + visitor.get_FGhablDore());
                txtAVGsatr.setText("" + visitor.get_AvgRowF());
                txtAVGForoosh.setText("" + visitor.get_AvgPriceF());
                txtSoodR.setText("" + visitor.get_SoodR());
                txtSoodD.setText("" + visitor.get_SoodD());

                tell = ""+visitor.get_Tell();

                long starRating = Long.parseLong((""+visitor.get_DarsadF()));
                long gaugeRating = Long.parseLong((""+visitor.get_DarsadTargetF()));

                visitorRate.setRating(starRating / 20);

                gauge2.setValue(((int) gaugeRating));
                btnDarsad.setText("" + gaugeRating + "%");
                if (gaugeRating>50){
                    btnDarsad.setTextColor(Color.parseColor("#424242"));
                }

                imgCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Log.e("uuuuuuuuuuuu", "isTelephonyEnabled" + isTelephonyEnabled());
                        if (isTelephonyEnabled() == true) {
                            if (tell.equals("0")) {
                                new AlertDialog.Builder(ActivityListOfVisitorInfo.this)
                                        .setCancelable(false)
                                        .setTitle("خطا")
                                        .setMessage("شماره ای به ویزیتور اختصاص داده نشده است")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .setIcon(R.drawable.eror_dialog)
                                        .show();

                            } else {
                                startDialActivity(tell);
                            }
                        } else {
                            new AlertDialog.Builder(ActivityListOfVisitorInfo.this)
                                    .setCancelable(false)
                                    .setTitle("خطا")
                                    .setMessage("دستگاه شما قابلیت برقراری تماس ندارد")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .setIcon(R.drawable.eror_dialog)
                                    .show();

                        }
                    }
                });


                imgSMS.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!tell.equals("0")) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", tell, null)));
                        } else {
                            new AlertDialog.Builder(ActivityListOfVisitorInfo.this)
                                    .setCancelable(false)
                                    .setTitle("خطا")
                                    .setMessage("شماره ای به ویزیتور اختصاص داده نشده است")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .setIcon(R.drawable.eror_dialog)
                                    .show();
                        }

                    }
                });

                if (dialog != null)
                    dialog.dismiss();
            }
            else if (value.equals("notOnline")){
                new AlertDialog.Builder(ActivityListOfVisitorInfo.this)
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
                dialog = ProgressDialog.show(ActivityListOfVisitorInfo.this, "", "دریافت اطلاعات");
                super.onPreExecute();
            }
        }



        @Override
        protected String  doInBackground(Void... voids) {
            HashMap<String, Object> datas = new HashMap<String, Object>();


            datas.put("TokenID",preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            datas.put("code_B",code);

            if (isonline) {


                try {

                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_ListOfVisitornfo", datas).getProperty(0);
                    if (request2.getPropertyCount()==0||request2.equals("")){
                        return "Null";
                    }
                    SoapObject sp = (SoapObject) request2.getProperty(0);
                    Log.e("wqqqqqqqqqqq", "info is " + sp);
                    Log.e("propertyCount", "info is " + request2.getPropertyCount());




                    visitor.set_CodeB(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                    visitor.set_NameB(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                    visitor.set_Tell(NetworkTools.getSoapPropertyAsNullableString(sp, 2));
                    visitor.set_Address(NetworkTools.getSoapPropertyAsNullableString(sp, 3));
                    visitor.set_TForoosh(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 4).toString())));
                    visitor.set_SumForoosh(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 5).toString())));
                    visitor.set_TBargasht(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 6).toString())));
                    visitor.set_SumBargasht(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 7).toString())));
                    visitor.set_KhalesT(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 8).toString())));
                    visitor.set_KhalesR(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 9).toString())));
                    visitor.set_FGhablDore(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 10).toString())));
                    visitor.set_AvgRowF(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 11).toString())));
                    visitor.set_AvgPriceF(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 12).toString())));
                    visitor.set_SoodR(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 13).toString())));
                    visitor.set_SoodD(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 14).toString())));
                    visitor.set_DarsadTargetF(NetworkTools.getSoapPropertyAsNullableString(sp, 17));
                    visitor.set_DarsadF(NetworkTools.getSoapPropertyAsNullableString(sp, 16));


//


                } catch (Exception e) {
                    Log.e("iiiiiii", "connot read Soap");

                    e.printStackTrace();
                    return "Null";
                }
                return "Online";
            }
            return "notOnline";

        }


    }

    public boolean isTelephonyEnabled(){
        TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        return tm != null && tm.getSimState()== TelephonyManager.SIM_STATE_READY;
    }
    public void startDialActivity(String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phone));
        startActivity(intent);
    }


}
