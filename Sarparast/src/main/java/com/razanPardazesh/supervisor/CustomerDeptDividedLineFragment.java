package com.razanPardazesh.supervisor;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.FontApplier;
import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.R;
import com.razanPardazesh.supervisor.model.CustomerInfo;
import com.razanPardazesh.supervisor.model.wrapper.CustomerDeptDividedLineAnswer;
import com.razanPardazesh.supervisor.model.wrapper.ServerAnswer;
import com.razanPardazesh.supervisor.repo.CustomerDeptDividedLineServerRepo;
import com.razanPardazesh.supervisor.repo.iRepo.ICustomerInfo;

/**
 * Created by Zikey on 20/02/2017.
 */

public class CustomerDeptDividedLineFragment extends DialogFragment {

    private static final String KEY_FRAGMENT_LABEL = "CustomerDeptDividedLine";
    private ViewGroup root;
    private ICustomerInfo serverRepo = null;
    private CustomerDeptAsync customerDeptAsync = null;

    private ImageView btnClose;
    private TextView txtCode;
    private TextView txtName;
    private TextView txtMande;
    private TextView txtMandeh1;
    private TextView txtMandeh2;
    private TextView txtMandeh3;
    private TextView txtMandeh4;
    private TextView txtMandeh5;
    private TextView txtMandeh6;
    private TextView txtMandeh7;
    private TextView txtMandeh8;
    private TextView txtMandeh9;
    private TextView txtMandeh10;

    private LinearLayout lyProgress;
    private LinearLayout lyEror;
    private TextView txtEror;

    private String code;
    private String name;
    private String mandeh;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMandeh() {
        return mandeh;
    }

    public void setMandeh(String mandeh) {
        this.mandeh = mandeh;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_FRAME,
                android.R.style.Theme_Black_NoTitleBar);
        super.onCreate(savedInstanceState);


    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.WHITE));

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.fragment_customer_dept_divided_line, null);
        initDialog();
        initView();
        initRepo();
        startAsync();

        return root;
    }


    private void initDialog() {
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
    }

    private void initView() {
        if (root == null)
            return;

        RelativeLayout lyContainer = (RelativeLayout) root.findViewById(R.id.lyContainer);
        FontApplier.applyMainFont(getActivity(), lyContainer);

        btnClose = (ImageView) root.findViewById(R.id.btnClose);
        txtCode = (TextView) root.findViewById(R.id.txtCode);
        txtName = (TextView) root.findViewById(R.id.txtName);
        txtMande = (TextView) root.findViewById(R.id.txtMande);
        txtMandeh1 = (TextView) root.findViewById(R.id.txtMandeh1);
        txtMandeh2 = (TextView) root.findViewById(R.id.txtMandeh2);
        txtMandeh3 = (TextView) root.findViewById(R.id.txtMandeh3);
        txtMandeh4 = (TextView) root.findViewById(R.id.txtMandeh4);
        txtMandeh5 = (TextView) root.findViewById(R.id.txtMandeh5);
        txtMandeh6 = (TextView) root.findViewById(R.id.txtMandeh6);
        txtMandeh7 = (TextView) root.findViewById(R.id.txtMandeh7);
        txtMandeh8 = (TextView) root.findViewById(R.id.txtMandeh8);
        txtMandeh9 = (TextView) root.findViewById(R.id.txtMandeh9);
        txtMandeh10 = (TextView) root.findViewById(R.id.txtMandeh10);

        lyProgress = (LinearLayout) root.findViewById(R.id.lyProgress);
        lyEror = (LinearLayout) root.findViewById(R.id.lyEror);
        txtEror = (TextView) root.findViewById(R.id.txtEror);

        String code = getCode();
        String name = getName();
        String mandeh = getMandeh();

        if (code != null && !TextUtils.isEmpty(code))
            txtCode.setText(code);

        if (name != null && !TextUtils.isEmpty(name))
            txtName.setText(name);

        if (mandeh != null && !TextUtils.isEmpty(mandeh))
            txtMande.setText(mandeh);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    private void initRepo() {

        if (serverRepo == null)
            serverRepo = new CustomerDeptDividedLineServerRepo();
    }


    public static CustomerDeptDividedLineFragment Show(FragmentActivity act, String name, String code, String mandeh) {

        android.app.FragmentManager manager = act.getFragmentManager();
        CustomerDeptDividedLineFragment fragment = new CustomerDeptDividedLineFragment();
        if (code != null && !TextUtils.isEmpty(code))
            fragment.setCode(code);
        if (name != null && !TextUtils.isEmpty(name))
            fragment.setName(name);
        if (mandeh != null && !TextUtils.isEmpty(mandeh))
            fragment.setMandeh(mandeh);
        fragment.show(manager, KEY_FRAGMENT_LABEL);

        return fragment;
    }


    public class CustomerDeptAsync extends AsyncTask<Void, String, String> {

        private CustomerDeptDividedLineAnswer answer;
        private String message;

        @Override
        protected void onPreExecute() {
            lyProgress.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            customerDeptAsync = null;
            answer = serverRepo.getCustomerDept(getActivity(), Long.parseLong(getCode()));

            if (answer.getMessage() != null || !TextUtils.isEmpty(answer.getMessage())) {
                message = answer.getMessage();
                return ("-1");
            }

            if (answer.getIsSuccess() == 1) {
                return "1";
            }

            return "0";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("-1")) {
                lyProgress.setVisibility(View.GONE);
                lyEror.setVisibility(View.VISIBLE);
                if (message != null)
                    txtEror.setText(message);
            }

            if (s.equals("0"))
                lyProgress.setVisibility(View.GONE);

            if (s.equals("1")) {
                FillCustomerDept(answer.getCustomerInfo());
                lyProgress.setVisibility(View.GONE);

            }
        }
    }

    private void startAsync() {

        if (customerDeptAsync != null)
            return;

        customerDeptAsync = new CustomerDeptAsync();
        customerDeptAsync.execute();

    }

    private void FillCustomerDept(CustomerInfo customerInfo) {
        if (customerInfo == null)
            return;

        if (txtMandeh1 == null)
            initView();

        String line1 = String.valueOf(customerInfo.getMandeh_line1());
        String line2 = String.valueOf(customerInfo.getMandeh_line2());
        String line3 = String.valueOf(customerInfo.getMandeh_line3());
        String line4 = String.valueOf(customerInfo.getMandeh_line4());
        String line5 = String.valueOf(customerInfo.getMandeh_line5());
        String line6 = String.valueOf(customerInfo.getMandeh_line6());
        String line7 = String.valueOf(customerInfo.getMandeh_line7());
        String line8 = String.valueOf(customerInfo.getMandeh_line8());
        String line9 = String.valueOf(customerInfo.getMandeh_line9());
        String line10 = String.valueOf(customerInfo.getMandeh_line10());

        if (!TextUtils.isEmpty(line1) || line1 != null)
            txtMandeh1.setText(String.format("%,d", Long.parseLong(line1.toString())));
        if (!TextUtils.isEmpty(line2) || line2 != null)
            txtMandeh2.setText(String.format("%,d", Long.parseLong(line2.toString())));
        if (!TextUtils.isEmpty(line3) || line3 != null)
            txtMandeh3.setText(String.format("%,d", Long.parseLong(line3.toString())));
        if (!TextUtils.isEmpty(line4) || line4 != null)
            txtMandeh4.setText(String.format("%,d", Long.parseLong(line4.toString())));
        if (!TextUtils.isEmpty(line5) || line5 != null)
            txtMandeh5.setText(String.format("%,d", Long.parseLong(line5.toString())));
        if (!TextUtils.isEmpty(line6) || line6 != null)
            txtMandeh6.setText(String.format("%,d", Long.parseLong(line6.toString())));
        if (!TextUtils.isEmpty(line7) || line7 != null)
            txtMandeh7.setText(String.format("%,d", Long.parseLong(line7.toString())));
        if (!TextUtils.isEmpty(line8) || line8 != null)
            txtMandeh8.setText(String.format("%,d", Long.parseLong(line8.toString())));
        if (!TextUtils.isEmpty(line9) || line9 != null)
            txtMandeh9.setText(String.format("%,d", Long.parseLong(line9.toString())));
        if (!TextUtils.isEmpty(line10) || line10 != null)
            txtMandeh10.setText(String.format("%,d", Long.parseLong(line10.toString())));

    }
}
