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

public class Fragment_ProductsKalaInDetails extends DialogFragment   {

    private ImageView btnExit;

    //checker for check 10 Property Or ten Price

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    private AppCompatActivity activity;

    private  int statee=0;

    RelativeLayout lyRoot;
    RelativeLayout lyEror;
    RelativeLayout lyProgress;

    private String barcode;
    private String modat;
    private String tedad;
    private String gAsli;
    private String gFarei;

    private PreferenceHelper preferenceHelper;

    private  String name;

    public void setCode(String code) {
        this.code = code;
    }

    private String code;

    public void setName(String name) {
        this.name = name;
    }

    private TextView  txtCode;
    private TextView  txtModat1;
    private TextView  txtTedad1;
    private TextView  txtGAsli1;
    private TextView  txtGFarei1;
    private TextView  txtName;

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
        view= inflater.inflate(R.layout.fragment_product_details,null);

       btnExit = (ImageView) view.findViewById(R.id.btnExit);
//        btnClose = (ImageView) view.findViewById(R.id.imgCancel);

        txtCode= (TextView) view.findViewById(R.id.txtCode);
        txtModat1= (TextView) view.findViewById(R.id.txtModat1);
        txtTedad1= (TextView) view.findViewById(R.id.txtTedad1);
        txtGAsli1= (TextView) view.findViewById(R.id.txtGAsli1);
        txtGFarei1= (TextView) view.findViewById(R.id.txtGFarei1);
        txtName= (TextView) view.findViewById(R.id.txtName);

        txtEror= (TextView) view.findViewById(R.id.txtEror);

        lyEror= (RelativeLayout) view.findViewById(R.id.lyEror);
        lyRoot= (RelativeLayout) view.findViewById(R.id.lyRoot);
        lyProgress= (RelativeLayout) view.findViewById(R.id.lyProgress);

        lyProgress.setVisibility(View.VISIBLE);
        lyRoot.setVisibility(View.GONE);
        lyEror.setVisibility(View.GONE);

        txtName.setText(name);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        getDialog(). setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

           new KalaDetailsAsync().execute();

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

    public class KalaDetailsAsync extends AsyncTask<Void,String,String > {

        ProgressDialog dialog;

        @Override
        protected void onPostExecute(String value) {

            if (statee==0){
                lyProgress.setVisibility(View.GONE);
                lyRoot.setVisibility(View.GONE);
                lyEror.setVisibility(View.VISIBLE);
            }

            if (statee==1) {

               txtCode.setText(barcode);
               txtModat1.setText(modat);
               txtTedad1.setText(tedad);
               txtGAsli1.setText(gAsli);
               txtGFarei1.setText(gFarei);

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
        protected String  doInBackground(Void... voids) {
            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID",preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            datas.put("code",code);

            try {

                SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_GetListOf_Kala_Details_Fragment", datas).getProperty(0);

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
               barcode =NetworkTools.getSoapPropertyAsNullableString(sp, 0);
               modat =NetworkTools.getSoapPropertyAsNullableString(sp, 1);
               tedad =NetworkTools.getSoapPropertyAsNullableString(sp, 2);
               gAsli =NetworkTools.getSoapPropertyAsNullableString(sp, 3);
               gFarei =NetworkTools.getSoapPropertyAsNullableString(sp, 4);

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









