package com.razanPardazesh.supervisor;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.FontApplier;
import com.example.zikey.sarparast.R;
import com.razanPardazesh.supervisor.model.CustomerInfo;

/**
 * Created by Zikey on 20/02/2017.
 */

public class AroundMeCustomerInfoFragment extends DialogFragment {

    private static final String KEY_FRAGMENT_LABEL = "CustomerDeptDividedLine";
    private ViewGroup root;
    private TextView txtName;
    private TextView txtTel;
    private TextView txtMobile;
    private TextView txtAddress;
    private ImageView imgBack;

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfos) {
        this.customerInfo = customerInfos;
    }

    private CustomerInfo customerInfo;


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

        root = (ViewGroup) inflater.inflate(R.layout.around_me_customesr_info_fragment, null);
        initDialog();
        initView();
        fillCustomerData();

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

        txtName = (TextView) root.findViewById(R.id.txtName);
        txtTel = (TextView) root.findViewById(R.id.txtTel);
        txtMobile = (TextView) root.findViewById(R.id.txtMobile);
        txtAddress = (TextView) root.findViewById(R.id.txtAddress);
        imgBack = (ImageView) root.findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    public static AroundMeCustomerInfoFragment Show(FragmentActivity act, CustomerInfo customerInfo) {

        android.app.FragmentManager manager = act.getFragmentManager();
        AroundMeCustomerInfoFragment fragment = new AroundMeCustomerInfoFragment();
        fragment.setCustomerInfo(customerInfo);
        fragment.show(manager, KEY_FRAGMENT_LABEL);

        return fragment;
    }


    private void fillCustomerData() {

        CustomerInfo customerInfo = getCustomerInfo();

        if (customerInfo == null)
            return;

        if (txtName == null)
            initView();

        String name = customerInfo.getCustomerName();
        if (name != null && !TextUtils.isEmpty(name))
            txtName.setText(name);

        String tell = customerInfo.getCustomerTel();
        if (tell != null && !TextUtils.isEmpty(tell))
            txtTel.setText(tell);

        String mobile = customerInfo.getCustomerMobile();
        if (mobile != null && !TextUtils.isEmpty(mobile))
            txtMobile.setText(mobile);

        String address = customerInfo.getCustomerAddress();
        if (address != null && !TextUtils.isEmpty(address))
            txtAddress.setText(address);

    }
}
