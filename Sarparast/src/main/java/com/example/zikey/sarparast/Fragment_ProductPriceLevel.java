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
import com.razanPardazesh.supervisor.model.Product;
import com.razanPardazesh.supervisor.model.wrapper.ProductAnswer;
import com.razanPardazesh.supervisor.repo.ProductServerRepo;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

/**
 * Created by Zikey on 24/07/2016.
 */

public class Fragment_ProductPriceLevel extends DialogFragment {

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

    RelativeLayout lyRoot;
    RelativeLayout lyEror;
    RelativeLayout lyProgress;

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
    private TextView txtprofit;
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
    private TextView txtNum11;
    private TextView txtNum12;
    private TextView txtNum13;
    private TextView txtNum14;
    private TextView txtNum15;

    private TextView txtEror;

    private PriceLevelAsync priceLevelAsync = null;

    ProductServerRepo serverRepo = null;
    ProductAnswer productAnswer = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_product_pricelevel, null);
        initView(view);
        initDialog();
        runPriceLevelAsync();
        return view;

    }


    public class PriceLevelAsync extends AsyncTask<Void, String, String> {


        private String message;

        @Override
        protected void onPreExecute() {

            lyProgress.setVisibility(View.VISIBLE);
            lyEror.setVisibility(View.GONE);
            lyRoot.setVisibility(View.GONE);

        }

        @Override
        protected String doInBackground(Void... voids) {


            productAnswer = serverRepo.priceLevels(getActivity().getApplicationContext(), code, "", 0, 0);

            if (productAnswer.getIsSuccess() == 1) {
                return "1";
            }

            message = productAnswer.getMessage();
            return "0";
        }

        @Override
        protected void onPostExecute(String value) {


            if (value.equals("1")) {
                lyProgress.setVisibility(View.GONE);
                lyEror.setVisibility(View.GONE);
                lyRoot.setVisibility(View.VISIBLE);

                txtNum1.setText(productAnswer.getProduct().getPrice1().toString());
                txtNum2.setText(productAnswer.getProduct().getPrice2().toString());
                txtNum3.setText(productAnswer.getProduct().getPrice3().toString());
                txtNum4.setText(productAnswer.getProduct().getPrice4().toString());
                txtNum5.setText(productAnswer.getProduct().getPrice5().toString());
                txtNum6.setText(productAnswer.getProduct().getPrice6().toString());
                txtNum7.setText(productAnswer.getProduct().getPrice7().toString());
                txtNum8.setText(productAnswer.getProduct().getPrice8().toString());
                txtNum9.setText(productAnswer.getProduct().getPrice9().toString());
                txtNum10.setText(productAnswer.getProduct().getPrice10().toString());
                txtNum11.setText(productAnswer.getProduct().getPrice11().toString());
                txtNum12.setText(productAnswer.getProduct().getPrice12().toString());
                txtNum13.setText(productAnswer.getProduct().getPrice13().toString());
                txtNum14.setText(productAnswer.getProduct().getPrice14().toString());
                txtNum15.setText(productAnswer.getProduct().getPrice15().toString());

              Long profit =  profitBorder(productAnswer.getProduct().getPrice15(), productAnswer.getProduct().getPrice1());



                if (profit!=0l){
                    txtprofit.setText(profit.toString());
                    txtprofit.setVisibility(View.VISIBLE);
                }



            }

            if (value.equals("0")) {
                lyEror.setVisibility(View.VISIBLE);
                lyRoot.setVisibility(View.GONE);
                lyProgress.setVisibility(View.GONE);
            }
        }


    }

    private void initView(View view) {

        preferenceHelper = new PreferenceHelper(activity.getApplicationContext());

        btnExit = (ImageView) view.findViewById(R.id.btnExit);
        txtName = (TextView) view.findViewById(R.id.txtName);
        txtprofit = (TextView) view.findViewById(R.id.txtprofit);
        txtprofit.setVisibility(View.GONE);
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
        txtNum11 = (TextView) view.findViewById(R.id.txtNum11);
        txtNum12 = (TextView) view.findViewById(R.id.txtNum12);
        txtNum13 = (TextView) view.findViewById(R.id.txtNum13);
        txtNum14 = (TextView) view.findViewById(R.id.txtNum14);
        txtNum15 = (TextView) view.findViewById(R.id.txtNum15);

        txtEror = (TextView) view.findViewById(R.id.txtEror);
        lyEror = (RelativeLayout) view.findViewById(R.id.lyEror);
        lyRoot = (RelativeLayout) view.findViewById(R.id.lyRoot);
        lyProgress = (RelativeLayout) view.findViewById(R.id.lyProgress);
        lyProgress.setVisibility(View.VISIBLE);
        lyRoot.setVisibility(View.GONE);
        lyEror.setVisibility(View.GONE);
        txtName.setText(name);

        serverRepo = new ProductServerRepo();
        productAnswer = new ProductAnswer();

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void initDialog() {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

    }

    private void runPriceLevelAsync() {
        if (priceLevelAsync != null)
            return;

        priceLevelAsync = new PriceLevelAsync();
        priceLevelAsync.execute();
    }

    private Long profitBorder(Long val1, Long val2) {

        if (val2 == 0l || val2 == null)
            return 0l;

        if (val1 == 0l || val1 == null)
            return 0l;

        return val2 - val1;
    }

}









