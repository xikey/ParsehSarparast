package com.example.zikey.sarparast;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zikey on 31/07/2016.
 */
public class FragmentAllBazaryab   extends DialogFragment {

    private int statee=1;
    private  String id;
    private ImageView btnExit;
    private ImageView btnClose;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    private AppCompatActivity activity;


    private SelectUserCommunicator communicator;

    ArrayList<UserInfo> userInfos = new ArrayList<>();

    private   RecyclerView rowAllBazaryab;
    private   RecyclerView.LayoutManager row_manager;
    private   allBazaryabAdapter row_adapter;

    RelativeLayout lyProgress;
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

        view= inflater.inflate(R.layout.fragment_add_new_customer,null);

        rowAllBazaryab = (RecyclerView) view.findViewById(R.id.rowAllBazaryab);

        lyProgress = (RelativeLayout) view.findViewById(R.id.lyProgress);
        lyEror= (RelativeLayout) view.findViewById(R.id.lyEror);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog(). setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

//        btnExit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dismiss();
//            }
//        });
//        btnClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dismiss();
//            }
//        });

        if (statee==1){
            new AllBazaryabAsync().execute();

            lyProgress.setVisibility(View.GONE);
        }
        if (statee==0){

        }

        return view;
    }

    //________________________ASYNC TASK __________________________________________________________

    public class AllBazaryabAsync extends AsyncTask<Void,String,String > {

        ProgressDialog dialog;

        @Override
        protected void onPostExecute(String value) {

            if (statee==0){
                lyProgress.setVisibility(View.GONE);
                lyEror.setVisibility(View.VISIBLE);
            }

            if (statee==1) {

                row_manager = new LinearLayoutManager(activity.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                rowAllBazaryab.setLayoutManager(row_manager);
                row_adapter = new allBazaryabAdapter(userInfos);

                row_adapter.setCommunicator(communicator);

                rowAllBazaryab.setAdapter(row_adapter);
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String  doInBackground(Void... voids) {
            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID",preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            try {

                SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_GetListOF_Bazaryab", datas).getProperty(0);

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

                statee=1;

                for (int i = 0; i < request2.getPropertyCount(); i++) {
                    SoapObject sp1 = (SoapObject) request2.getProperty(i);
                    UserInfo userInfo = new UserInfo();
                    userInfo.setCode_user(NetworkTools.getSoapPropertyAsNullableString(sp1, 0));
                    userInfo.setName_user(NetworkTools.getSoapPropertyAsNullableString(sp1, 1));
                    userInfo.setImage_user(R.drawable.userm);

                    userInfos.add(userInfo);
                }

            } catch (Exception e) {
                statee=0;
                Log.e("iiiiiii", "connot read Soap");

                e.printStackTrace();
                return "Null";
            }
            return "Online";

        }

    }

    public void setCommunicator(SelectUserCommunicator communicator) {
        this.communicator = communicator;
    }
}






