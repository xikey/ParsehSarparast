package com.example.zikey.sarparast;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

/**
 * Created by Zikey on 24/07/2016.
 */

public class Fragment_ProductsTenPropertyOrPrice extends DialogFragment {

    private ImageView btnExit;

    //checker for check 10 Property Or ten Price

    public void setChecker(String checker) {
        this.checker = checker;
    }

    private String checker;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    private AppCompatActivity activity;

    private int statee = 0;


    RelativeLayout lyRoot;
    RelativeLayout lyEror;
    RelativeLayout lyProgress;

    private String num1;
    private String num2;
    private String num3;
    private String num4;
    private String num5;
    private String num6;
    private String num7;
    private String num8;
    private String num9;
    private String num10;


    private PreferenceHelper preferenceHelper;

    private String name;

    public void setCode(String code) {
        this.code = code;
    }

    private String code;

    public void setName(String name) {
        this.name = name;
    }

    private TextView txtName;
    private TextView txtNum1;
    private TextView txtNum2;
    private TextView txtNum3;
    private TextView txtNum4;
    private TextView txtNum5;
    private TextView txtNum6;
    private TextView txtNum7;
    private TextView txtNum8;
    private TextView txtNum9;
    private TextView txtNum10;

    private TextView txtEror;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        preferenceHelper = new PreferenceHelper(activity.getApplicationContext());
        View view;
//        final View v2=inflater.inflate(R.layout.activity_show_dialog,null);
        view = inflater.inflate(R.layout.fragment_product_tenproperty, null);

        btnExit = (ImageView) view.findViewById(R.id.btnExit);
//        btnClose = (ImageView) view.findViewById(R.id.imgCancel);

        txtName = (TextView) view.findViewById(R.id.txtName);
        txtNum1 = (TextView) view.findViewById(R.id.txtNum1);
        txtNum2 = (TextView) view.findViewById(R.id.txtNum2);
        txtNum3 = (TextView) view.findViewById(R.id.txtNum3);
        txtNum4 = (TextView) view.findViewById(R.id.txtNum4);
        txtNum5 = (TextView) view.findViewById(R.id.txtNum5);
        txtNum6 = (TextView) view.findViewById(R.id.txtNum6);
        txtNum7 = (TextView) view.findViewById(R.id.txtNum7);
        txtNum8 = (TextView) view.findViewById(R.id.txtNum8);
        txtNum9 = (TextView) view.findViewById(R.id.txtNum9);
        txtNum10 = (TextView) view.findViewById(R.id.txtNum10);

        txtEror = (TextView) view.findViewById(R.id.txtEror);


        lyEror = (RelativeLayout) view.findViewById(R.id.lyEror);
        lyRoot = (RelativeLayout) view.findViewById(R.id.lyRoot);
        lyProgress = (RelativeLayout) view.findViewById(R.id.lyProgress);

        lyProgress.setVisibility(View.VISIBLE);
        lyRoot.setVisibility(View.GONE);
        lyEror.setVisibility(View.GONE);


        txtName.setText(name);


        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

        if (checker.equals("Vijegi")) {
            new TenVijegiKalaAsync().execute();

        }

        if (checker.equals("Gheymat")) {
            new TenPriceKalaAsync().execute();

        }

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }

    public boolean isTelephonyEnabled() {
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(activity.TELEPHONY_SERVICE);
        return tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    public void startDialActivity(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }


    public class TenVijegiKalaAsync extends AsyncTask<Void, String, String> {

        ProgressDialog dialog;

        @Override
        protected void onPostExecute(String value) {

            if (statee == 0) {
                lyProgress.setVisibility(View.GONE);
                lyRoot.setVisibility(View.GONE);
                lyEror.setVisibility(View.VISIBLE);
            }


            if (statee == 1) {

                txtNum1.setText(num1);
                txtNum2.setText(num2);
                txtNum3.setText(num3);
                txtNum4.setText(num4);
                txtNum5.setText(num5);
                txtNum6.setText(num6);
                txtNum7.setText(num7);
                txtNum8.setText(num8);
                txtNum9.setText(num9);
                txtNum10.setText(num10);

                lyProgress.setVisibility(View.GONE);
                lyEror.setVisibility(View.GONE);
                lyRoot.setVisibility(View.VISIBLE);

            }
        }


        @Override
        protected void onPreExecute() {

            lyProgress.setVisibility(View.VISIBLE);
            lyEror.setVisibility(View.GONE);
            lyRoot.setVisibility(View.GONE);

        }

        @Override
        protected String doInBackground(Void... voids) {
            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            datas.put("code", code);

            try {

                SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_GetListOf_10_Property_Kala", datas).getProperty(0);

                if (request2.getPropertyCount() <= 0) {
                    statee = 0;
                    return "Null";
                }

                SoapObject sp = (SoapObject) request2.getProperty(0);

                if ((sp.toString() == null) || (sp.toString() == "")) {
                    statee = 0;
                    return "";
                }
                Log.e("wqqqqqqqqqqq", "info is " + sp);
//                    Log.e("wqqqqqqqqqqq", "info is " + sp);
//                    Log.e("propertyCount", "info is " + request2.getPropertyCount());
                num1 = NetworkTools.getSoapPropertyAsNullableString(sp, 0);
                num2 = NetworkTools.getSoapPropertyAsNullableString(sp, 1);
                num3 = NetworkTools.getSoapPropertyAsNullableString(sp, 2);
                num4 = NetworkTools.getSoapPropertyAsNullableString(sp, 3);
                num5 = NetworkTools.getSoapPropertyAsNullableString(sp, 4);
                num6 = NetworkTools.getSoapPropertyAsNullableString(sp, 5);
                num7 = NetworkTools.getSoapPropertyAsNullableString(sp, 6);
                num8 = NetworkTools.getSoapPropertyAsNullableString(sp, 7);
                num9 = NetworkTools.getSoapPropertyAsNullableString(sp, 8);
                num10 = NetworkTools.getSoapPropertyAsNullableString(sp, 9);

                statee = 1;

            } catch (Exception e) {
                statee = 0;
                Log.e("iiiiiii", "connot read Soap");

                e.printStackTrace();
                return "Null";
            }
            return "Online";

        }

    }


    public class TenPriceKalaAsync extends AsyncTask<Void, String, String> {

        ProgressDialog dialog;

        @Override
        protected void onPostExecute(String value) {

            if (statee == 0) {
                lyProgress.setVisibility(View.GONE);
                lyRoot.setVisibility(View.GONE);
                lyEror.setVisibility(View.VISIBLE);
            }


            if (statee == 1) {

                txtNum1.setText(num1);
                txtNum2.setText(num2);
                txtNum3.setText(num3);
                txtNum4.setText(num4);
                txtNum5.setText(num5);
                txtNum6.setText(num6);
                txtNum7.setText(num7);
                txtNum8.setText(num8);
                txtNum9.setText(num9);
                txtNum10.setText(num10);

                lyProgress.setVisibility(View.GONE);
                lyEror.setVisibility(View.GONE);
                lyRoot.setVisibility(View.VISIBLE);

            }
        }


        @Override
        protected void onPreExecute() {

            lyProgress.setVisibility(View.VISIBLE);
            lyEror.setVisibility(View.GONE);
            lyRoot.setVisibility(View.GONE);

        }

        @Override
        protected String doInBackground(Void... voids) {
            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            datas.put("code", code);

            try {

                SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_GetListOf_10_Price_Kala", datas).getProperty(0);

                if (request2.getPropertyCount() <= 0) {
                    statee = 0;
                    return "Null";
                }

                SoapObject sp = (SoapObject) request2.getProperty(0);

                if ((sp.toString() == null) || (sp.toString() == "")) {
                    statee = 0;
                    return "";
                }
                Log.e("wqqqqqqqqqqq", "info is " + sp);
//                    Log.e("wqqqqqqqqqqq", "info is " + sp);
//                    Log.e("propertyCount", "info is " + request2.getPropertyCount());
                num1 = String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 0).toString()));
                num2 = String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 1).toString()));
                num3 = String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 2).toString()));
                num4 = String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 3).toString()));
                num5 = String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 4).toString()));
                num6 = String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 5).toString()));
                num7 = String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 6).toString()));
                num8 = String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 7).toString()));
                num9 = String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 8).toString()));
                num10 = String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 9).toString()));

                statee = 1;

            } catch (Exception e) {
                statee = 0;
                Log.e("iiiiiii", "connot read Soap");

                e.printStackTrace();
                return "Null";
            }
            return "Online";

        }

    }

}









