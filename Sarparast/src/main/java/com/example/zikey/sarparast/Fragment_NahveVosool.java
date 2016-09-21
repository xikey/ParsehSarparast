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
public class Fragment_NahveVosool extends DialogFragment   {

    private int statee=1;

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    private  String groupCode;

    public void setCgroup(String cgroup) {
        Cgroup = cgroup;
    }

    private  String Cgroup;

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    private  String groupName;

    private ImageView btnExit;
    private ImageView btnClose;

    private TextView  txtName;
    private TextView  txtNaghd;
    private TextView  txtVariz;
    private TextView  txtChek;
    private TextView  txtTakhfif;
    private TextView  txtElamie;
    private TextView  txtBestankar;
    private TextView  txtBargasht;

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
        view= inflater.inflate(R.layout.fragment_analyseforosh_nahvevosool,null);

        btnExit = (ImageView) view.findViewById(R.id.btnClose);
        btnClose = (ImageView) view.findViewById(R.id.imgCancel);

//         mandehMoshtariInfo = new MandehMoshtariInfo();

        lyProgress = (RelativeLayout) view.findViewById(R.id.lyProgress);
        lyBackg= (RelativeLayout) view.findViewById(R.id.lyBackg);
        lyEror= (RelativeLayout) view.findViewById(R.id.lyEror);


        lyBackg.setVisibility(View.GONE);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

      txtName = (TextView) view.findViewById(R.id. txtName);
      txtNaghd = (TextView) view.findViewById(R.id. txtNaghd);
      txtVariz = (TextView) view.findViewById(R.id. txtVariz);
      txtChek = (TextView) view.findViewById(R.id. txtChek);
      txtTakhfif = (TextView) view.findViewById(R.id. txtTakhfif);
      txtElamie = (TextView) view.findViewById(R.id. txtElamie);
      txtBestankar = (TextView) view.findViewById(R.id. txtBestankar);
      txtBargasht = (TextView) view.findViewById(R.id. txtBargasht);


     getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

       getDialog(). setCancelable(true);
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

if (statee==1){
    Log.e("Group&Code",Cgroup+" "+groupCode);
    new VisitorsInfoAsync().execute();

}
        if (statee==0){

        }

        return view;
    }


    public class VisitorsInfoAsync extends AsyncTask<Void,String,String > {

        private  AnalyseSefareshatInfo analyseSefareshat;
        private ProgressDialog dialog;
        private  String name;
        private  String code;

        @Override
        protected void onPostExecute(String value) {

            if (statee==0){
                lyProgress.setVisibility(View.GONE);
                lyBackg.setVisibility(View.GONE);
                lyEror.setVisibility(View.VISIBLE);
            }

if (statee==1) {

//
    txtNaghd.setText(analyseSefareshat.get_Naghd().toString());
    txtVariz.setText(analyseSefareshat.get_Variz().toString());
    txtChek.setText(analyseSefareshat.get_Check().toString());
    txtTakhfif.setText(analyseSefareshat.get_Takhfif().toString());
    txtElamie.setText(analyseSefareshat.get_Elamie().toString());
    txtBestankar.setText(analyseSefareshat.get_Sayer().toString());
    txtBargasht.setText(analyseSefareshat.get_Bargashti().toString());
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
        protected String  doInBackground(Void... voids) {
            HashMap<String, Object> datas = new HashMap<String, Object>();

                    lyProgress.setVisibility(View.VISIBLE);
                    lyBackg.setVisibility(View.GONE);
                    lyEror.setVisibility(View.GONE);

                    analyseSefareshat = new AnalyseSefareshatInfo();

                    name = groupName;
                    code = groupCode;


            datas.put("TokenID",preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            datas.put("Group",Cgroup);
            datas.put("Code",code);




                try {

                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Analyse_OF_Sefareshat", datas).getProperty(0);

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


                    analyseSefareshat.set_Naghd(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp,6).toString())));
                    analyseSefareshat.set_Variz(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 7).toString())));
                    analyseSefareshat.set_Check(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp,8).toString())));
                    analyseSefareshat.set_Takhfif(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 9).toString())));
                    analyseSefareshat.set_Elamie(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 10).toString())));

                   // Log.i("TForoosh","T Foroosh is "+(NetworkTools.getSoapPropertyAsNullableString(sp, 4)));

                    analyseSefareshat.set_Sayer(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 11).toString())));
                    analyseSefareshat.set_Bargashti(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp,12).toString())));
                    analyseSefareshat.set_Name(name);

//                    mandehMoshtariInfo.set_RDarJaryan(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 11).toString())));
//                    mandehMoshtariInfo.set_THoghooghi(NetworkTools.getSoapPropertyAsNullableString(sp, 12));
//                    mandehMoshtariInfo.set_RHoghooghi(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 13).toString())));
//                    mandehMoshtariInfo.set_TVosool(NetworkTools.getSoapPropertyAsNullableString(sp, 14));
//                    mandehMoshtariInfo.set_RVosool(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 15).toString())));


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






