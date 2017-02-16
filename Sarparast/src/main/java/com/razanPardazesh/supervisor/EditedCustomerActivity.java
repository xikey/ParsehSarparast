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
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.R;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.razanPardazesh.supervisor.model.CustomerRequestEdit;
import com.razanPardazesh.supervisor.model.wrapper.CustomerEditAnswer;
import com.razanPardazesh.supervisor.model.wrapper.ServerAnswer;
import com.razanPardazesh.supervisor.repo.CustomersEditedServerRepo;
import com.razanPardazesh.supervisor.repo.iRepo.ICustomersEdited;

import java.text.DateFormat;
import java.util.Date;

public class EditedCustomerActivity extends AppCompatActivity {



    private static final String KEY_ID = "ID";
    private final int ACCEPT_CUSTOMER = 1;
    private final int REJECT_CUSTOMER = 2;
    private long ID = -1;

    private RelativeLayout lyProgress;
    private ImageView imgBack;
    private TextView txtHead;
    private AutoCompleteTextView txtName;
    private AutoCompleteTextView txtFamily;
    private AutoCompleteTextView txtTel;
    private AutoCompleteTextView txtMobile;
    private AutoCompleteTextView txtAddress;
    private LinearLayout lyLocation;
    private TextView txtRejection;
    private TextView txtAccept;

    private ICustomersEdited serverRepo = null;
    private CustomerRequestEditAsynk customerAsynk;
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
        startAsync();

    }

    public static void start(FragmentActivity context, long id,int requestCode) {

        Intent starter = new Intent(context, EditedCustomerActivity.class);
        starter.putExtra(KEY_ID, id);
        context.startActivityForResult(starter,requestCode);
    }

    private void initViews() {

        lyProgress = (RelativeLayout) findViewById(R.id.lyProgress);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        txtHead = (TextView) findViewById(R.id.txtHead);
        txtName = (AutoCompleteTextView) findViewById(R.id.txtName);
        txtFamily = (AutoCompleteTextView) findViewById(R.id.txtFamily);
        txtTel = (AutoCompleteTextView) findViewById(R.id.txtTel);
        txtMobile = (AutoCompleteTextView) findViewById(R.id.txtMobile);
        txtAddress = (AutoCompleteTextView) findViewById(R.id.txtAddress);
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
                startChangeCustomerStatusAsync(ACCEPT_CUSTOMER);
            }
        });
        txtRejection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChangeCustomerStatusAsync(REJECT_CUSTOMER);
            }
        });

    }

    private void parseIntent() {

        Intent data = getIntent();

        if (data == null)
            return;

        if (data.hasExtra(KEY_ID))
            ID = data.getLongExtra(KEY_ID, -1);

    }

    private void initRepo() {

        if (serverRepo == null)
            serverRepo = new CustomersEditedServerRepo();
    }


    public class CustomerRequestEditAsynk extends AsyncTask<Void, String, String> {

        private CustomerEditAnswer answer;
        private String message;

        @Override
        protected void onPreExecute() {
            lyProgress.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            customerAsynk = null;
            answer = serverRepo.getEditedCustomer(getApplicationContext(), ID);

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
                showErorAlertDialog("خطا", message);
            }

            if (s.equals("0"))
                showErorAlertDialog("خطا", "اطلاعاتی جهت نمایش وجود ندارد");

            if (s.equals("1")) {
                fillCustomerData(answer);
            }
        }
    }


    private void startAsync() {

        if (customerAsynk != null)
            return;

        if (NetworkTools.checkNetworkConnection(EditedCustomerActivity.this) == false)
            finish();

        customerAsynk = new CustomerRequestEditAsynk();
        customerAsynk.execute();

    }

    private void showErorAlertDialog(String titile, String message) {
        if (message == null) return;

        AlertDialog alertDialog = new AlertDialog.Builder(EditedCustomerActivity.this).create();
        alertDialog.setTitle(titile);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.show();

    }

    private void fillCustomerData(CustomerEditAnswer data) {

        if (data == null)
            return;

        if (data.getCustomerRequestEdit() == null)
            return;

        if (txtName == null)
            initViews();

        CustomerRequestEdit customer = data.getCustomerRequestEdit();

        String name = customer.getCustomerName();
        if (name != null || !TextUtils.isEmpty(name))
            txtName.setText(name);

        String family = customer.getCustomerFamily();
        if (family != null || !TextUtils.isEmpty(family))
            txtFamily.setText(family);

        String tell = customer.getCustomerTel();
        if (tell != null || !TextUtils.isEmpty(tell))
            txtTel.setText(tell);

        String mobile = customer.getCustomerMobile();
        if (mobile != null || !TextUtils.isEmpty(mobile))
            txtMobile.setText(mobile);

        String address = customer.getCustomerAddress();
        if (address != null || !TextUtils.isEmpty(address))
            txtAddress.setText(address);

        latitude = customer.getCustomerLT();
        longitude = customer.getCustomerLN();

        lyProgress.setVisibility(View.GONE);

    }

    private void checkUserLocationSetting() {
        if (latitude == 0 || longitude == 0)
            return;

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
            answer = serverRepo.setEditedCustomerStatus(getApplicationContext(), ID,currentDateTimeString, statusCode);

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
                showErorAlertDialog("خطا", message);
                lyProgress.setVisibility(View.GONE);

            }

            if (s.equals("0"))
                showErorAlertDialog("خطا", "خطا در ارسال اطلاعات به سمت سرور");
            lyProgress.setVisibility(View.GONE);

            if (s.equals("1")) {
                setResult(RESULT_OK);
                showErorAlertDialog("موفقیت", "موفقیت در انجام عملیات");
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
