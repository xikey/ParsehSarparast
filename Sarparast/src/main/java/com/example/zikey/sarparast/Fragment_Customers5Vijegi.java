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

public class Fragment_Customers5Vijegi extends DialogFragment   {


    private ImageView btnExit;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    private AppCompatActivity activity;

    private  int statee=0;


    RelativeLayout lyContent;
    RelativeLayout lyEror;
    RelativeLayout lyProgress;

    private String num1;
    private String num2;
    private String num3;
    private String num4;
    private String num5;


    private ImageView btnCall1;
    private PreferenceHelper preferenceHelper;

    private  String name;

    public void setCode(String code) {
        this.code = code;
    }

    private String code;

    public void setName(String name) {
        this.name = name;
    }

    private TextView  txtName;
    private TextView  txtNum1;
    private TextView  txtNum2;
    private TextView  txtNum3;
    private TextView  txtNum4;
    private TextView  txtNum5;

    private TextView  txtEror;

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
        view= inflater.inflate(R.layout.fragment_customers_fiveproperty,null);

       btnExit = (ImageView) view.findViewById(R.id.btnExit);
//        btnClose = (ImageView) view.findViewById(R.id.imgCancel);

        txtName= (TextView) view.findViewById(R.id.txtName);
        txtNum1= (TextView) view.findViewById(R.id.txtNum1);
        txtNum2= (TextView) view.findViewById(R.id.txtNum2);
        txtNum3= (TextView) view.findViewById(R.id.txtNum3);
        txtNum4= (TextView) view.findViewById(R.id.txtNum4);
        txtNum5= (TextView) view.findViewById(R.id.txtNum5);

        txtEror= (TextView) view.findViewById(R.id.txtEror);

        btnCall1= (ImageView) view.findViewById(R.id.btnCall1);

        lyEror= (RelativeLayout) view.findViewById(R.id.lyEror);
        lyContent= (RelativeLayout) view.findViewById(R.id.lyContent);
        lyProgress= (RelativeLayout) view.findViewById(R.id.lyProgress);

        lyProgress.setVisibility(View.VISIBLE);
        lyContent.setVisibility(View.GONE);
        lyEror.setVisibility(View.GONE);

        txtName.setText(" (5 ویژگی مشتری) "+name);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog(). setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

        new FivePropertyAsync().execute();

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dismiss();
            }
       });
        return view;
    }

    public boolean isTelephonyEnabled(){
        TelephonyManager tm = (TelephonyManager)activity.getSystemService(activity.TELEPHONY_SERVICE);
        return tm != null && tm.getSimState()== TelephonyManager.SIM_STATE_READY;
    }

    public void startDialActivity(String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phone));
        startActivity(intent);
    }


    public class FivePropertyAsync extends AsyncTask<Void,String,String > {

        ProgressDialog dialog;

        @Override
        protected void onPostExecute(String value) {

            if (statee==0){
                lyProgress.setVisibility(View.GONE);
                lyContent.setVisibility(View.GONE);
                lyEror.setVisibility(View.VISIBLE);
            }


            if (statee==1) {

               txtNum1.setText(num1);
               txtNum2.setText(num2);
               txtNum3.setText(num3);
               txtNum4.setText(num4);
               txtNum5.setText(num5);

                lyProgress.setVisibility(View.GONE);
                lyEror.setVisibility(View.GONE);
                lyContent.setVisibility(View.VISIBLE);

            }
        }


        @Override
        protected void onPreExecute() {

            lyProgress.setVisibility(View.VISIBLE);
            lyEror.setVisibility(View.GONE);
            lyContent.setVisibility(View.GONE);

        }

        @Override
        protected String  doInBackground(Void... voids) {
            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID",preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            datas.put("code",code);

            try {

                SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_GetListOf_5_Vijegi_Moshtari", datas).getProperty(0);

                if (request2.getPropertyCount()<=0)
                {
                    statee=0;
                    return "Null";
                }

                SoapObject sp = (SoapObject) request2.getProperty(0);

                if ((sp.toString()==null)||(sp.toString()=="")){
                    statee=0;
                    return "";
                }
                Log.e("wqqqqqqqqqqq", "info is " + sp);
//                    Log.e("wqqqqqqqqqqq", "info is " + sp);
//                    Log.e("propertyCount", "info is " + request2.getPropertyCount());
               num1 =NetworkTools.getSoapPropertyAsNullableString(sp, 1);
               num2 =NetworkTools.getSoapPropertyAsNullableString(sp, 3);
               num3 =NetworkTools.getSoapPropertyAsNullableString(sp, 5);
               num4 =NetworkTools.getSoapPropertyAsNullableString(sp, 7);
               num5 =NetworkTools.getSoapPropertyAsNullableString(sp, 9);

                statee=1;

            } catch (Exception e) {
                statee=0;
                Log.e("iiiiiii", "connot read Soap");

                e.printStackTrace();
                return "Null";
            }
            return "Online";

        }

    }

}









