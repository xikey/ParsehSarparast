package com.razanPardazesh.supervisor;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.R;
import com.razanPardazesh.supervisor.model.CustomerRequestEdit;
import com.razanPardazesh.supervisor.model.wrapper.ServerAnswer;
import com.razanPardazesh.supervisor.repo.CustomersEditedServerRepo;
import com.razanPardazesh.supervisor.repo.iRepo.ICustomersEdited;

import java.text.DateFormat;
import java.util.Date;

public class EditedCustomerActivity extends AppCompatActivity {


    private static final String KEY_CUSTOMER_EDITED_EXTRA = "CUSTOMER_EDITED_EXTRA";
    private final int ACCEPT_CUSTOMER = 1;
    private final int REJECT_CUSTOMER = 2;
    private CustomerRequestEdit customerData = null;

    private RelativeLayout lyProgress;
    private ImageView imgBack;
    private TextView txtHead;
    private TextView txtName;
    private TextView txtShop;
    private TextView txtCodeMelli;
    private TextView txtTel;
    private TextView txtMobile;
    private TextView txtAddress;
    private LinearLayout lyLocation;
    private TextView txtRejection;
    private TextView txtAccept;

    private ICustomersEdited serverRepo = null;
    CustomerRequestEditChangeStatusAsynk changeCustomerStatusAsync;
    private double latitude = 0;
    private double longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edited_customer);

        initRepo();
        parseIntent();
        initViews();
        fillCustomerData();

    }

    public static void start(FragmentActivity context, CustomerRequestEdit customerRequestEdit, int requestCode) {

        Intent starter = new Intent(context, EditedCustomerActivity.class);
        starter.putExtra(KEY_CUSTOMER_EDITED_EXTRA, customerRequestEdit);
        context.startActivityForResult(starter, requestCode);
    }

    private void initViews() {

        lyProgress = (RelativeLayout) findViewById(R.id.lyProgress);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        txtHead = (TextView) findViewById(R.id.txtHead);
        txtName = (TextView) findViewById(R.id.txtName);
        txtShop = (TextView) findViewById(R.id.txtShop);
        txtCodeMelli = (TextView) findViewById(R.id.txtCodeMelli);
        txtTel = (TextView) findViewById(R.id.txtTel);
        txtMobile = (TextView) findViewById(R.id.txtMobile);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        lyLocation = (LinearLayout) findViewById(R.id.lyLocation);
        txtRejection = (TextView) findViewById(R.id.txtRejection);
        txtAccept = (TextView) findViewById(R.id.txtAccept);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkUserLocationSetting();
            }
        });

        txtAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new android.app.AlertDialog.Builder(EditedCustomerActivity.this)
                        .setMessage("مایل به تایید اطلاعات ویرایش شده میباشید؟")
                        .setNegativeButton("خیر", null)
                        .setPositiveButton("بلی", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                startChangeCustomerStatusAsync(ACCEPT_CUSTOMER);
                            }
                        }).create().show();
            }
        });
        txtRejection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new android.app.AlertDialog.Builder(EditedCustomerActivity.this)
                        .setMessage("مایل به رد اطلاعات ویرایش شده میباشید؟")
                        .setNegativeButton("خیر", null)
                        .setPositiveButton("بلی", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                startChangeCustomerStatusAsync(REJECT_CUSTOMER);
                            }
                        }).create().show();

            }
        });


    }

    private void parseIntent() {

        Intent data = getIntent();

        if (data == null)
            return;

        if (data.hasExtra(KEY_CUSTOMER_EDITED_EXTRA))
            customerData = data.getParcelableExtra(KEY_CUSTOMER_EDITED_EXTRA);

    }

    private void initRepo() {

        if (serverRepo == null)
            serverRepo = new CustomersEditedServerRepo();
    }

    private void showErorAlertDialog(String titile, String message, final boolean finishActivity) {
        if (message == null) return;

        AlertDialog alertDialog = new AlertDialog.Builder(EditedCustomerActivity.this).create();
        alertDialog.setTitle(titile);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        if (finishActivity)
                            finish();
                    }
                });
        alertDialog.show();

    }

    private void fillCustomerData() {

        if (customerData == null)
            return;

        if (txtName == null)
            initViews();


        String name = customerData.getCustomerName();
        if (name != null || !TextUtils.isEmpty(name))
            txtName.setText(name);

        String stroeName = customerData.getStoreName();
        if (stroeName != null || !TextUtils.isEmpty(stroeName))
            txtShop.setText(stroeName);

        String tell = customerData.getCustomerTel();
        if (tell != null || !TextUtils.isEmpty(tell))
            txtTel.setText(tell);

        String mobile = customerData.getCustomerMobile();
        if (mobile != null || !TextUtils.isEmpty(mobile))
            txtMobile.setText(mobile);

        String address = customerData.getCustomerAddress();
        if (address != null || !TextUtils.isEmpty(address))
            txtAddress.setText(address);

        long codeMelli = customerData.getCodeMelli();
        if (codeMelli != 0) {
            txtCodeMelli.setText("" + codeMelli);
        }


        latitude = customerData.getCustomerLT();
        longitude = customerData.getCustomerLN();

        if (latitude == 0 || longitude == 0) {
            lyLocation.setVisibility(View.GONE);
        }

        lyProgress.setVisibility(View.GONE);

    }

    private void checkUserLocationSetting() {
        if (latitude == 0 || longitude == 0) {
            showErorAlertDialog(null, "موقعیتی برای این مشتری روی نقشه ثبت نشده است", false);
            return;

        }

        LocationOnMapActivity.start(EditedCustomerActivity.this, latitude, longitude);
    }


    public class CustomerRequestEditChangeStatusAsynk extends AsyncTask<Void, String, String> {

        int statusCode;

        public CustomerRequestEditChangeStatusAsynk(int statusCode) {
            this.statusCode = statusCode;
        }

        private ServerAnswer answer;
        private String message;

        @Override
        protected void onPreExecute() {
            lyProgress.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            changeCustomerStatusAsync = null;
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            answer = serverRepo.setEditedCustomerStatus(getApplicationContext(), customerData.getId(), currentDateTimeString, statusCode);

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
                showErorAlertDialog("خطا", message, true);
                lyProgress.setVisibility(View.GONE);
            }

            if (s.equals("0")) {
                showErorAlertDialog("خطا", "خطا در ارسال اطلاعات به سمت سرور", true);
                lyProgress.setVisibility(View.GONE);
            }


            if (s.equals("1")) {
                setResult(RESULT_OK);
                showErorAlertDialog("موفقیت", "موفقیت در انجام عملیات", true);
                lyProgress.setVisibility(View.GONE);
            }
        }
    }


    private void startChangeCustomerStatusAsync(int status) {
        if (changeCustomerStatusAsync != null)
            return;

        if (NetworkTools.checkNetworkConnection(EditedCustomerActivity.this) == false)
            finish();

        changeCustomerStatusAsync = new CustomerRequestEditChangeStatusAsynk(status);
        changeCustomerStatusAsync.execute();
    }


}
