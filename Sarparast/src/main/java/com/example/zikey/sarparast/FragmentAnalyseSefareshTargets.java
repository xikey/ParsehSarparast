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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

/**
 * Created by Zikey on 24/07/2016.
 */
public class FragmentAnalyseSefareshTargets extends DialogFragment {

    private int statee = 1;

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    private String groupCode;





    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    private String groupName;

    private ImageView btnExit;
    private ImageView btnClose;

    private TextView txtName;
    private TextView txtKhales;
    private TextView txtTarget;
    private TextView txtDarsadTarget;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    AppCompatActivity activity;

    RelativeLayout lyProgress;
    RelativeLayout lyBackg;
    RelativeLayout lyEror;

    ProgressBar progressBar;

    private PreferenceHelper preferenceHelper;

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
        view = inflater.inflate(R.layout.fragment_analyseforoosh_target, null);

        btnExit = (ImageView) view.findViewById(R.id.btnClose);
        btnClose = (ImageView) view.findViewById(R.id.imgCancel);

//         mandehMoshtariInfo = new MandehMoshtariInfo();

        lyProgress = (RelativeLayout) view.findViewById(R.id.lyProgress);
        lyBackg = (RelativeLayout) view.findViewById(R.id.lyBackg);
        lyEror = (RelativeLayout) view.findViewById(R.id.lyEror);


        lyBackg.setVisibility(View.GONE);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        txtName = (TextView) view.findViewById(R.id.txtName);
        txtKhales = (TextView) view.findViewById(R.id.txtKhales);
        txtTarget = (TextView) view.findViewById(R.id.txtTarget);
        txtDarsadTarget = (TextView) view.findViewById(R.id.txtDarsadTarget);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        lyProgress.setVisibility(View.VISIBLE);
        lyBackg.setVisibility(View.GONE);
        lyEror.setVisibility(View.GONE);


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

        lyProgress.setVisibility(View.GONE);

        if (statee == 1) {
          //  Log.e("Group&Code", Cgroup + " " + groupCode);
            new GetVisitorTargetAsync().execute();

        }
        if (statee == 0) {

        }

        return view;
    }


    public class GetVisitorTargetAsync extends AsyncTask<Void, String, String> {


        private AnalyseSefareshatInfo analyseSefareshat;
        private ProgressDialog dialog;
        private String name;
        private String code;

        @Override
        protected void onPostExecute(String value) {

            if (statee == 0) {
                lyProgress.setVisibility(View.GONE);
                lyBackg.setVisibility(View.GONE);
                lyEror.setVisibility(View.VISIBLE);
            }

            if (statee == 1) {

//

                txtDarsadTarget.setText(analyseSefareshat.get_DarsadTarget().toString() + "%");
                txtTarget.setText(analyseSefareshat.get_Target().toString());
                txtKhales.setText(analyseSefareshat.get_KhalesForosh().toString());
                txtName.setText(analyseSefareshat.get_Name().toString());


                lyProgress.setVisibility(View.GONE);
                lyEror.setVisibility(View.GONE);
                lyBackg.setVisibility(View.VISIBLE);

            }
        }

        @Override
        protected void onPreExecute() {

        }


        @Override
        protected String doInBackground(Void... voids) {
            HashMap<String, Object> datas = new HashMap<String, Object>();


            analyseSefareshat = new AnalyseSefareshatInfo();

            name = groupName;
            code = groupCode;


            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            datas.put("code", code);


            try {

                SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Visitors_Target", datas).getProperty(0);

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


                analyseSefareshat.set_Target(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 3).toString())));
                analyseSefareshat.set_KhalesForosh(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 2).toString())));
                analyseSefareshat.set_DarsadTarget(NetworkTools.getSoapPropertyAsNullableString(sp, 4));
                analyseSefareshat.set_Name(name);


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






