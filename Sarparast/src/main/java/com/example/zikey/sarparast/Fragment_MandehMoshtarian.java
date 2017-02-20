package com.example.zikey.sarparast;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.FontApplier;
import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

/**
 * Created by Zikey on 24/07/2016.
 */

public class Fragment_MandehMoshtarian extends DialogFragment {

    private int statee = 1;

    private ImageView btnExit;
    private ImageView btnClose;


    private MandehMoshtariInfo mandehMoshtariInfo;

    private TextView Code;
    private TextView Name;
    private TextView Mandeh;
    private TextView Vaset;
    private TextView txtFF;

    private TextView RFrososh;
    private TextView TBargashti;
    private TextView RBargashti;
    private TextView TTasvieNashode;
    private TextView RTasvieNashode;
    private TextView TDarJaryan;
    private TextView RDarJaryan;
    private TextView THoghooghi;
    private TextView RHoghooghi;
    private TextView TVosool;
    private TextView RVosool;

    private TextView PasShode;
    private TextView RPasShode;

    private RelativeLayout lyProgress;
    private RelativeLayout lyBackg;
    private RelativeLayout lyEror;

    private LinearLayout lyHead;

    public void setID(String ID) {
        this.ID = ID;
    }

    private String ID;

    ProgressBar progressBar;

    private PreferenceHelper preferenceHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        preferenceHelper = new PreferenceHelper(getActivity());
        View view;
//        final View v2=inflater.inflate(R.layout.activity_show_dialog,null);
        view = inflater.inflate(R.layout.fragment_mandeh_moshtari, null);

        btnExit = (ImageView) view.findViewById(R.id.btnClose);
        btnClose = (ImageView) view.findViewById(R.id.imgCancel);

        mandehMoshtariInfo = new MandehMoshtariInfo();

        lyProgress = (RelativeLayout) view.findViewById(R.id.lyProgress);
        lyBackg = (RelativeLayout) view.findViewById(R.id.lyBackg);
        lyEror = (RelativeLayout) view.findViewById(R.id.lyEror);

        lyHead = (LinearLayout) view.findViewById(R.id.lyHead);

        lyProgress.setVisibility(View.VISIBLE);
        lyEror.setVisibility(View.GONE);
        lyBackg.setVisibility(View.GONE);


        lyBackg.setVisibility(View.GONE);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        Code = (TextView) view.findViewById(R.id.txtCode);
        Name = (TextView) view.findViewById(R.id.txtName);
        Mandeh = (TextView) view.findViewById(R.id.txtMande);
        Vaset = (TextView) view.findViewById(R.id.txtVaset);
        txtFF = (TextView) view.findViewById(R.id.textView65);
        RFrososh = (TextView) view.findViewById(R.id.txtRForoosh);
        TBargashti = (TextView) view.findViewById(R.id.txtTBargasht);
        RBargashti = (TextView) view.findViewById(R.id.txtRBargasht);
        TTasvieNashode = (TextView) view.findViewById(R.id.txtTTasvie);
        RTasvieNashode = (TextView) view.findViewById(R.id.txtRTasvie);
        TDarJaryan = (TextView) view.findViewById(R.id.txtTJaryan);
        RDarJaryan = (TextView) view.findViewById(R.id.txtRJaryan);
        THoghooghi = (TextView) view.findViewById(R.id.txtTHoghoghi);
        RHoghooghi = (TextView) view.findViewById(R.id.txtRHoghoghi);
        TVosool = (TextView) view.findViewById(R.id.txtTVosol);
        RVosool = (TextView) view.findViewById(R.id.txtRVosol);

        PasShode = (TextView) view.findViewById(R.id.txtPAsShode);
        RPasShode = (TextView) view.findViewById(R.id.txtRPAsShode);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        FontApplier.applyMainFont(getActivity(), lyHead);

        if (statee == 1) {
            new VisitorsInfoAsync().execute();
            lyBackg.setVisibility(View.VISIBLE);
            lyProgress.setVisibility(View.GONE);
        }
        if (statee == 0) {

        }

        return view;
    }


    public class VisitorsInfoAsync extends AsyncTask<Void, String, String> {

        ProgressDialog dialog;

        @Override
        protected void onPostExecute(String value) {

            if (statee == 0) {
                lyProgress.setVisibility(View.GONE);
                lyBackg.setVisibility(View.GONE);
                lyEror.setVisibility(View.VISIBLE);
            }

            if (statee == 1) {

                Code.setText(mandehMoshtariInfo.get_Code().toString());
                Name.setText(mandehMoshtariInfo.get_Name().toString());
                Mandeh.setText(mandehMoshtariInfo.get_Mandeh().toString());
                Vaset.setText(mandehMoshtariInfo.get_Vaset().toString());
                txtFF.setText(mandehMoshtariInfo.get_TFrorosh().toString());
                RFrososh.setText(mandehMoshtariInfo.get_RFrososh().toString());
                TBargashti.setText(mandehMoshtariInfo.get_TBargashti().toString());
                RBargashti.setText(mandehMoshtariInfo.get_RBargashti().toString());
                TTasvieNashode.setText(mandehMoshtariInfo.get_TTasvieNashode().toString());
                RTasvieNashode.setText(mandehMoshtariInfo.get_RTasvieNashode().toString());
                TDarJaryan.setText(mandehMoshtariInfo.get_TDarJaryan().toString());
                RDarJaryan.setText(mandehMoshtariInfo.get_RDarJaryan().toString());
                THoghooghi.setText(mandehMoshtariInfo.get_THoghooghi().toString());
                RHoghooghi.setText(mandehMoshtariInfo.get_RHoghooghi().toString());
                TVosool.setText(mandehMoshtariInfo.get_TVosool().toString());
                RVosool.setText(mandehMoshtariInfo.get_RVosool().toString());

                PasShode.setText(mandehMoshtariInfo.get_TPasShode().toString());
                RPasShode.setText(mandehMoshtariInfo.get_RPasShode().toString());

                lyProgress.setVisibility(View.GONE);
                lyEror.setVisibility(View.GONE);
                lyBackg.setVisibility(View.VISIBLE);
            }
        }


        @Override
        protected void onPreExecute() {

            lyProgress.setVisibility(View.VISIBLE);
            lyEror.setVisibility(View.GONE);
            lyBackg.setVisibility(View.GONE);

        }

        @Override
        protected String doInBackground(Void... voids) {
            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            datas.put("Code", ID);

            try {

                SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_GetListOF_MandehMoshtari_Details", datas).getProperty(0);

                if (request2.getPropertyCount() <= 0) {
                    statee = 0;
                    return "Null";
                }
                SoapObject sp = (SoapObject) request2.getProperty(0);

                if ((sp.toString() == null) || (sp.toString() == "")) {
                    statee = 0;
                    return "";
                }

                mandehMoshtariInfo.set_Code(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                mandehMoshtariInfo.set_Name(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                mandehMoshtariInfo.set_Vaset(NetworkTools.getSoapPropertyAsNullableString(sp, 3));
                mandehMoshtariInfo.set_Mandeh(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 2).toString())));
                mandehMoshtariInfo.set_TFrorosh(NetworkTools.getSoapPropertyAsNullableString(sp, 4));
                mandehMoshtariInfo.set_RFrososh(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 5).toString())));
                mandehMoshtariInfo.set_TBargashti(NetworkTools.getSoapPropertyAsNullableString(sp, 6));
                mandehMoshtariInfo.set_RBargashti(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 7).toString())));
                mandehMoshtariInfo.set_TTasvieNashode(NetworkTools.getSoapPropertyAsNullableString(sp, 8));
                mandehMoshtariInfo.set_RTasvieNashode(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 9).toString())));
                mandehMoshtariInfo.set_TDarJaryan(NetworkTools.getSoapPropertyAsNullableString(sp, 10));
                mandehMoshtariInfo.set_RDarJaryan(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 11).toString())));
                mandehMoshtariInfo.set_THoghooghi(NetworkTools.getSoapPropertyAsNullableString(sp, 12));
                mandehMoshtariInfo.set_RHoghooghi(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 13).toString())));
                mandehMoshtariInfo.set_TVosool(NetworkTools.getSoapPropertyAsNullableString(sp, 14));
                mandehMoshtariInfo.set_RVosool(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 15).toString())));
                mandehMoshtariInfo.set_TPasShode(NetworkTools.getSoapPropertyAsNullableString(sp, 16));
                mandehMoshtariInfo.set_RPasShode(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 17).toString())));

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






