package com.example.zikey.sarparast;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

public class Fragment_CustomersPhoneNumber extends DialogFragment   {


    private ImageView btnExit;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    private AppCompatActivity activity;

    RelativeLayout lyContent;
    RelativeLayout lyEror;


    private ImageView btnCall1;


    private ImageView btnCall2;
    private ImageView btnSmS2;

    private  String mobile;
    private  String phone;
    private  String name;

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    private TextView  txtName;
    private TextView  txtPhone;
    private TextView  txtMobile;

    private TextView  txtEror;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view;
//        final View v2=inflater.inflate(R.layout.activity_show_dialog,null);
        view= inflater.inflate(R.layout.fragment_customers_phonenumber,null);

       btnExit = (ImageView) view.findViewById(R.id.btnExit);
//        btnClose = (ImageView) view.findViewById(R.id.imgCancel);

        txtName= (TextView) view.findViewById(R.id.txtName);
        txtPhone= (TextView) view.findViewById(R.id.txtPhone);
        txtMobile= (TextView) view.findViewById(R.id.txtMobile);
        txtEror= (TextView) view.findViewById(R.id.txtEror);

        btnCall1= (ImageView) view.findViewById(R.id.btnCall1);

        btnCall2= (ImageView) view.findViewById(R.id.btnCall2);
        btnSmS2= (ImageView) view.findViewById(R.id.btnSmS2);

        lyEror= (RelativeLayout) view.findViewById(R.id.lyEror);
        lyContent= (RelativeLayout) view.findViewById(R.id.lyContent);


        txtName.setText(name);
        txtMobile.setText(mobile);
        txtPhone.setText(phone);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        getDialog(). setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

        btnCall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTelephonyEnabled() == true) {
                    if (phone.equals("0")) {

                        lyContent.setVisibility(View.GONE);
                        lyEror.setVisibility(View.VISIBLE);
                        txtEror.setText("شماره تلفن مشتری صحیح نمیباشد");

                    } else {
                        startDialActivity(phone);
                    }
                } else {
                    lyContent.setVisibility(View.GONE);
                    lyEror.setVisibility(View.VISIBLE);
                    txtEror.setText("دستگاه شما قابلیت برقراری تماس ندارد!");
                }
            }
        });

        btnCall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTelephonyEnabled() == true) {
                    if (mobile.equals("0")) {

                        lyContent.setVisibility(View.GONE);
                        lyEror.setVisibility(View.VISIBLE);
                        txtEror.setText("شماره تلفن مشتری صحیح نمیباشد");

                    } else {
                        startDialActivity(mobile);
                    }
                } else {
                    lyContent.setVisibility(View.GONE);
                    lyEror.setVisibility(View.VISIBLE);
                    txtEror.setText("دستگاه شما قابلیت برقراری تماس ندارد!");
                }
            }
        });


        btnSmS2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mobile.equals("0")) {
                 //   startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", mobile, null)));

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("sms:"+mobile));
                    startActivity(intent);
                } else {
                    lyContent.setVisibility(View.GONE);
                    lyEror.setVisibility(View.VISIBLE);
                    txtEror.setText("شماره تلفن مشتری صحیح نمیباشد");


                }

            }
        });

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


 }






