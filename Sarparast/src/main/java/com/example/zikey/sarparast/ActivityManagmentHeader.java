package com.example.zikey.sarparast;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zikey.sarparast.Helpers.FontApplier;
import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;

import org.ksoap2.serialization.SoapObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;


public class ActivityManagmentHeader extends AppCompatActivity {

    private RelativeLayout lyProgress;
    private RelativeLayout lyEror;
    private RelativeLayout lyContent;

    private String name;
    private String id;
    private CheckBox chkDistance;
    private RelativeLayout ltDistance;
    private Button btnsave;
    private Button btnTargetSave;
    private EditText edtDistance;
    private EditText edtTarget;

    private TextView txtEror;
    private CheckBox chkCanUse;
    private CheckBox chkAllCustomers;
    private CheckBox chkCanUpdate;

    private ManagmentInfo managmentModify = new ManagmentInfo();
    private ManagmentInfo managmentInfo = new ManagmentInfo();

    private ImageView imgBack;
    private TextView txtHead;
    private PreferenceHelper preferenceHelper;
    private GetSettingAsync getSettingAsync = null;
    private SetManagmentAsync setManagmentAsync = null;
    private GetTargetAsync getTargetAsync = null;
    private SetTargettAsync setTargettAsync = null;

    private TargetInfo targetInfo = new TargetInfo();
    private TargetInfo targetInfoModify = new TargetInfo();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managment_header);

        initViews();
        runGetSettings();
        runGetTargetAsync();

    }


    public static void start(FragmentActivity context, String name, String ID) {
        Intent starter = new Intent(context, ActivityManagmentHeader.class);
        starter.putExtra("name", name);
        starter.putExtra("ID", ID);
        context.startActivity(starter);
    }

    private void checkboxChanger(int value, CheckBox checkBox, RelativeLayout layout) {

        //layout use when check box effects in layout to hide or show like distance box

        if (value == 1) {
            checkBox.setChecked(true);
            if (layout != null) layout.setVisibility(View.GONE);

        } else if (value == 0) {
            checkBox.setChecked(false);
            if (layout != null) ltDistance.setVisibility(View.VISIBLE);
        }
    }

    private void initViews() {

        TextView txtName = (TextView) findViewById(R.id.txtName);
        TextView txtCode = (TextView) findViewById(R.id.txtCode);

        lyProgress = (RelativeLayout) findViewById(R.id.lyProgress);
        lyEror = (RelativeLayout) findViewById(R.id.lyEror);
        lyContent = (RelativeLayout) findViewById(R.id.lyContent);
        txtEror = (TextView) findViewById(R.id.txtEror);
        EditText fakeTarget = (EditText) findViewById(R.id.fakeTarget);
        fakeTarget.findFocus();

        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtHead = (TextView) findViewById(R.id.txtHead);
        txtHead.setText("تنظیمات");

        imgBack = (ImageView) findViewById(R.id.imgBack);
        btnsave = (Button) findViewById(R.id.btnSave);
        btnTargetSave = (Button) findViewById(R.id.btnTargetSave);
        btnsave.setEnabled(false);
        btnsave.setTextColor(Color.parseColor("#EEEEEE"));
        btnTargetSave.setEnabled(false);
        btnTargetSave.setTextColor(Color.parseColor("#EEEEEE"));
        edtDistance = (EditText) findViewById(R.id.edtDistance);
        edtTarget = (EditText) findViewById(R.id.edtTarget);

        chkDistance = (CheckBox) findViewById(R.id.chkDistance);
        chkCanUse = (CheckBox) findViewById(R.id.chkCanUse);
        chkAllCustomers = (CheckBox) findViewById(R.id.chkAllCustomers);
        chkCanUpdate = (CheckBox) findViewById(R.id.chkCanUpdate);

        ltDistance = (RelativeLayout) findViewById(R.id.lyDistance);

        lyProgress.setVisibility(View.VISIBLE);
        lyContent.setVisibility(View.GONE);
        lyEror.setVisibility(View.GONE);

        FontApplier.applyMainFont(getApplicationContext(), lyContent);

        ltDistance.setVisibility(View.GONE);

        initClickListeners();

        edtDistance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    managmentModify.setDistance(Integer.valueOf(s.toString()));
                    isanythingChanged();
                }
            }
        });


        NumberTextWatcher numberTextWatcher = new NumberTextWatcher(edtTarget);

        edtTarget.addTextChangedListener(numberTextWatcher);
        Long m = numberTextWatcher.getNumber();
        Log.e("number", "" + m);


//        edtTarget.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            private int focus = 0;
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//
//                if (hasFocus) {
//                    focus = 1;
//
//                    edtTarget.setText(targetInfoModify.getPriceTarget());
//                    edtTarget.setSelection(edtTarget.getText().length());
//                    InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
//                    imm.showSoftInput(edtTarget, InputMethodManager.SHOW_IMPLICIT);
//
//                    edtTarget.addTextChangedListener(new TextWatcher() {
//                        @Override
//                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//
//                        }
//
//                        @Override
//                        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                        }
//
//                        @Override
//                        public void afterTextChanged(Editable s) {
//
//                            if (focus == 1) {
//                                targetInfoModify.setPriceTarget(s.toString());
//                                targetInfoModify.setPriceTargetSeprated(String.format("%,d", Long.parseLong(s.toString())));
//                                isTargetChanged();
//                            }
//
//                        }
//                    });
//
//                } else {
//
//                    focus = 0;
//                    edtTarget.setText("" + targetInfoModify.getPriceTargetSeprated());
//                }
//            }
//        });


        preferenceHelper = new PreferenceHelper(this);

        name = parseIntent(getIntent(), "name");
        id = parseIntent(getIntent(), "ID");
        txtName.setText(name);
        txtCode.setText(id);

    }

    private String parseIntent(Intent data, String value) {
        if (data == null) return null;
        if (!data.hasExtra(value)) return null;
        return data.getStringExtra(value);

    }

    private void runGetSettings() {

        if (getSettingAsync != null)
            return;

        lyProgress.setVisibility(View.VISIBLE);
        getSettingAsync = new GetSettingAsync();
        getSettingAsync.execute();

    }

    private class GetSettingAsync extends AsyncTask<Void, String, String> {


        Boolean isonline = NetworkTools.isOnline(ActivityManagmentHeader.this);

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            datas.put("visitorCode", id);

            try {
                if (isonline) {
                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Managment_GetInfo", datas).getProperty(0);

                    if (request2.getPropertyCount() <= 0) {
                        return "Null";
                    }

                    managmentInfo.setCode(NetworkTools.getSoapPropertyAsNullableString(request2, 0));
                    managmentInfo.setCanOrder(Integer.parseInt(NetworkTools.getSoapPropertyAsNullableString(request2, 2)));
                    managmentInfo.setCanRegister(Integer.parseInt(NetworkTools.getSoapPropertyAsNullableString(request2, 1)));
                    managmentInfo.setDistance(Integer.parseInt(NetworkTools.getSoapPropertyAsNullableString(request2, 3)));
                    managmentInfo.setCanReadAllCustomer(Integer.parseInt(NetworkTools.getSoapPropertyAsNullableString(request2, 4)));
                    managmentInfo.setCanUpdate(Integer.parseInt(NetworkTools.getSoapPropertyAsNullableString(request2, 5)));
                    managmentInfo.setForceToLoguot(Integer.parseInt(NetworkTools.getSoapPropertyAsNullableString(request2, 6)));

                    managmentModify.setCode(NetworkTools.getSoapPropertyAsNullableString(request2, 0));
                    managmentModify.setCanOrder(Integer.parseInt(NetworkTools.getSoapPropertyAsNullableString(request2, 2)));
                    managmentModify.setCanRegister(Integer.parseInt(NetworkTools.getSoapPropertyAsNullableString(request2, 1)));
                    managmentModify.setDistance(Integer.parseInt(NetworkTools.getSoapPropertyAsNullableString(request2, 3)));
                    managmentModify.setCanReadAllCustomer(Integer.parseInt(NetworkTools.getSoapPropertyAsNullableString(request2, 4)));
                    managmentModify.setCanUpdate(Integer.parseInt(NetworkTools.getSoapPropertyAsNullableString(request2, 5)));
                    managmentModify.setForceToLoguot(Integer.parseInt(NetworkTools.getSoapPropertyAsNullableString(request2, 6)));

//                    Log.e("kkk",""+managmentInfo.getCanOrder()  +"  "+managmentInfo.getDistance());
//                    Log.e("kkk2",""+managmentModify.getCanOrder() +"  "+managmentModify.getDistance());
                    return "OK";

                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Eror";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {


            lyProgress.setVisibility(View.GONE);
            lyContent.setVisibility(View.VISIBLE);

            if (s.equals("OK")) {


                checkboxChanger(managmentModify.getCanOrder(), chkDistance, ltDistance);
                checkboxChanger(managmentModify.getCanRegister(), chkCanUse, null);
                checkboxChanger(managmentModify.getCanUpdate(), chkCanUpdate, null);
                checkboxChanger(managmentModify.getCanReadAllCustomer(), chkAllCustomers, null);

                edtDistance.setText("" + managmentModify.getDistance());

                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (managmentModify.getCanOrder() == 0 && TextUtils.isEmpty(edtDistance.getText().toString())) {

                            edtDistance.setError("فیلد مسافت خالی میباشد");

                        } else {


                            if (Long.parseLong(edtDistance.getText().toString()) > 1000) {

                                new AlertDialog.Builder(ActivityManagmentHeader.this)
                                        .setCancelable(false)
                                        .setTitle("خطا")
                                        .setMessage("مقدار مسافت بیشتر از 1000 متر میباشد")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .setIcon(R.drawable.eror_dialog)
                                        .show();
                            } else {
                                new AlertDialog.Builder(ActivityManagmentHeader.this)
                                        .setTitle("ارسال")
                                        .setMessage("مایل به ذخیره تنظیمات میباشید ؟")
                                        .setNegativeButton(android.R.string.no, null)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface arg0, int arg1) {
                                                runSetManagmentAsync();
                                            }
                                        }).create().show();
                            }
                        }


                    }
                });

            }
            super.onPostExecute(s);
        }
    }

    private class SetManagmentAsync extends AsyncTask<Void, String, String> {

        ProgressDialog dialog;
        Boolean isonline = NetworkTools.isOnline(ActivityManagmentHeader.this);

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ActivityManagmentHeader.this, "", "ارسال اطلاعات");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            datas.put("codeVisitor", id);
            datas.put("canOrder", managmentModify.getCanOrder());
            datas.put("canRegister", managmentModify.getCanRegister());
            datas.put("distance", managmentModify.getDistance());
            datas.put("canReadAllCustomer", managmentModify.getCanReadAllCustomer());
            datas.put("forceToUpdate", managmentModify.getCanUpdate());
            datas.put("forceToLoguot", managmentModify.getForceToLoguot());

            if (isonline) {


                try {
                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Managment_SetInfo", datas);
                    if (request2.getPropertyCount() <= 0) {
                        return "Null";
                    }

                    SoapObject rq = (SoapObject) request2.getProperty(0);
                    String m = NetworkTools.getSoapPropertyAsNullableString(rq, 0);
                    Log.e("myLog", m);
                    return m;

                } catch (Exception e) {
                    e.printStackTrace();
                    return "Eror";
                }
            }
            return "ofline";
        }

        @Override
        protected void onPostExecute(String s) {

            setManagmentAsync = null;

            if (dialog != null)
                dialog.dismiss();

            Log.e("posted", "" + s);

            //s==1 means that successfully
            //s==0 means that supervisor denied to send datas cuse permissions
            //s==-1 means that successfully sent but nothin changes in server

            if (s.equals("ofline")) {
                new AlertDialog.Builder(ActivityManagmentHeader.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("برقراری ارتباط با اینترنت امکان پذیر نمیباشد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }
else {


                if (s.equals("1")||s.equals("2")||s.equals("3")||s.equals("4")||s.equals("5")) {

                    new AlertDialog.Builder(ActivityManagmentHeader.this)
                            .setCancelable(false)
                            .setTitle("موفقیت")
                            .setMessage("ارسال اطلاعات با موفقیت انجام شد")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    managmentInfo.setCode(managmentModify.getCode());
                                    managmentInfo.setCanOrder(managmentModify.getCanOrder());
                                    managmentInfo.setCanRegister(managmentModify.getCanRegister());
                                    managmentInfo.setDistance(managmentModify.getDistance());
                                    managmentInfo.setCanReadAllCustomer(managmentModify.getCanReadAllCustomer());
                                    managmentInfo.setCanUpdate(managmentModify.getCanUpdate());
                                    managmentInfo.setForceToLoguot(managmentModify.getForceToLoguot());

                                    isanythingChanged();

                                }
                            })
                            .setIcon(R.drawable.eror_dialog)
                            .show();
                }


                if (s.equals("0")) {

                    new AlertDialog.Builder(ActivityManagmentHeader.this)
                            .setCancelable(false)
                            .setTitle("موفقیت")
                            .setMessage("اطلاعات با موفقیت ارسال شد اما تغییری در سمت سرور ایجاد نشده است!")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setIcon(R.drawable.eror_dialog)
                            .show();
                }
                if (s.equals("-1")) {

                    new AlertDialog.Builder(ActivityManagmentHeader.this)
                            .setCancelable(false)
                            .setTitle("خطا")
                            .setMessage("متاسفانه خطایی هنگام ارسال اطلاعات به وجود آمده است. لطفا دوباره تلاش کنید در صورت مشکل مجدد با پشتیبانی پارسه تماس بگیرید")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setIcon(R.drawable.eror_dialog)
                            .show();
                }
            }
            super.onPostExecute(s);
        }
    }

    private class GetTargetAsync extends AsyncTask<Void, String, String> {


        Boolean isonline = NetworkTools.isOnline(ActivityManagmentHeader.this);

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            datas.put("visitorCode", id);

            try {
                if (isonline) {
                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Get_Target", datas).getProperty(0);

                    if (request2.getPropertyCount() <= 0) {
                        return "Null";
                    }

                    //  DecimalFormat decimalFormat = new DecimalFormat("#,###");

                    //  Number num = decimalFormat.parse(NetworkTools.getSoapPropertyAsNullableString(request2, 0).toString());
//                    targetInfo.setPriceTargetSeprated(num.toString());
                    targetInfo.setPriceTarget((NetworkTools.getSoapPropertyAsNullableString(request2, 0).toString()));

//                    targetInfoModify.setPriceTargetSeprated(num.toString());
                    targetInfoModify.setPriceTarget(NetworkTools.getSoapPropertyAsNullableString(request2, 0).toString());

                    return "OK";

                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Eror";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            getTargetAsync = null;

            if (s.equals("OK")) {


                edtTarget.setText("" + targetInfoModify.getPriceTarget());

                btnTargetSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtTarget.clearFocus();

                        if (edtTarget.getText().toString() == null || TextUtils.isEmpty(edtTarget.getText().toString())) {

                            edtTarget.setError("فیلد تارگت خالی میباشد");
                        } else {


                            runsetTargetAsync();
                        }
                    }
                });

            }
            super.onPostExecute(s);
        }
    }


    private class SetTargettAsync extends AsyncTask<Void, String, String> {

        ProgressDialog dialog2;

        Boolean isonline = NetworkTools.isOnline(ActivityManagmentHeader.this);

        @Override
        protected void onPreExecute() {
            dialog2 = ProgressDialog.show(ActivityManagmentHeader.this, "", "ارسال اطلاعات");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            datas.put("codeVisitor", id);
            datas.put("target", Long.parseLong(targetInfoModify.getPriceTarget()));

            if (isonline) {


                try {
                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Set_Target", datas);
                    if (request2.getPropertyCount() <= 0) {
                        return "Null";
                    }

                    SoapObject rq = (SoapObject) request2.getProperty(0);
                    String m = NetworkTools.getSoapPropertyAsNullableString(rq, 0);
                    Log.e("myLog", m);
                    return m;

                } catch (Exception e) {
                    e.printStackTrace();
                    return "Eror";
                }
            }
            return "ofline";
        }

        @Override
        protected void onPostExecute(String s) {

            setTargettAsync = null;

            if (dialog2 != null)
                dialog2.dismiss();

            Log.e("posted", "" + s);

            //s==1 means that successfully
            //s==0 means that supervisor denied to send datas cuse permissions
            //s==-1 means that successfully sent but nothin changes in server

            if (s.equals("ofline")) {
                new AlertDialog.Builder(ActivityManagmentHeader.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("برقراری ارتباط با اینترنت امکان پذیر")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                targetInfo.setPriceTarget(targetInfoModify.getPriceTarget());
                                targetInfo.setPriceTargetSeprated(targetInfoModify.getPriceTargetSeprated());
                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }

            if (s.equals("1")) {

                new AlertDialog.Builder(ActivityManagmentHeader.this)
                        .setCancelable(false)
                        .setTitle("موفقیت")
                        .setMessage("ارسال اطلاعات با موفقیت انجام شد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                targetInfo.setPriceTarget(targetInfoModify.getPriceTarget());
                                targetInfo.setPriceTargetSeprated(targetInfoModify.getPriceTargetSeprated());


                                isTargetChanged();

                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }


            if (s.equals("0")) {

                new AlertDialog.Builder(ActivityManagmentHeader.this)
                        .setCancelable(false)
                        .setTitle("موفقیت")
                        .setMessage("اطلاعات با موفقیت ارسال شد اما تغییری در سمت سرور ایجاد نشده است!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }
            if (s.equals("-1")) {

                new AlertDialog.Builder(ActivityManagmentHeader.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("متاسفانه خطایی هنگام ارسال اطلاعات به وجود آمده است. لطفا دوباره تلاش کنید در صورت مشکل مجدد با پشتیبانی پارسه تماس بگیرید")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }
            super.onPostExecute(s);
        }
    }


    private void runSetManagmentAsync() {

        if (setManagmentAsync != null) return;

        setManagmentAsync = new SetManagmentAsync();
        setManagmentAsync.execute();
    }

    private void initClickListeners() {
        chkDistance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (chkDistance.isChecked()) {
                    ltDistance.setVisibility(View.GONE);
                    managmentModify.setCanOrder(1);

                } else if (!chkDistance.isChecked()) {

                    managmentModify.setCanOrder(0);
//                    Log.e("jjj",""+managmentModify.getCanOrder());
//                    Log.e("jjj2",""+managmentInfo.getCanOrder());
                    ltDistance.setVisibility(View.VISIBLE);
                }

                isanythingChanged();
            }
        });

        chkCanUse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                //can register is canUse

                if (chkCanUse.isChecked()) {

                    managmentModify.setCanRegister(1);

                } else if (!chkCanUse.isChecked()) {

                    managmentModify.setCanRegister(0);

                }

                isanythingChanged();
            }
        });
        chkAllCustomers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (chkAllCustomers.isChecked()) {

                    managmentModify.setCanReadAllCustomer(1);

                } else if (!chkAllCustomers.isChecked()) {
                    managmentModify.setCanReadAllCustomer(0);
                }

                isanythingChanged();

            }
        });

        chkCanUpdate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (chkCanUpdate.isChecked()) {

                    managmentModify.setCanUpdate(1);

                } else if (!chkCanUpdate.isChecked()) {

                    managmentModify.setCanUpdate(0);

                }

                isanythingChanged();
            }
        });

    }

    private void isanythingChanged() {

        if (managmentModify.compareTo(managmentInfo) == 0) {

            btnsave.setTextColor(Color.parseColor("#EEEEEE"));
            btnsave.setEnabled(false);
        } else {

            btnsave.setTextColor(Color.parseColor("#01579B"));
            btnsave.setEnabled(true);

        }
    }

    private void runGetTargetAsync() {
        if (getTargetAsync != null) return;

        getTargetAsync = new GetTargetAsync();
        getTargetAsync.execute();

    }

    private void runsetTargetAsync() {
        if (setTargettAsync != null) return;

        setTargettAsync = new SetTargettAsync();
        setTargettAsync.execute();
    }

    private void isTargetChanged() {

        if (targetInfoModify.compareTo(targetInfo) == 0) {

            btnTargetSave.setTextColor(Color.parseColor("#EEEEEE"));
            btnTargetSave.setEnabled(false);

        } else {
            btnTargetSave.setTextColor(Color.parseColor("#01579B"));
            btnTargetSave.setEnabled(true);
        }
    }


    public class NumberTextWatcher implements TextWatcher {

        private DecimalFormat df;
        private DecimalFormat dfnd;
        private boolean hasFractionalPart;


        private EditText et;

        public Long getNumber() {


            try {
                return dfnd.parse(et.getText().toString()).longValue();
            } catch (ParseException e) {
                return null;
            }
        }

        public String getString() {

            return (et.getText().toString());
        }

        public NumberTextWatcher(EditText et) {
            df = new DecimalFormat("#,###.##");
            df.setDecimalSeparatorAlwaysShown(true);
            dfnd = new DecimalFormat("#,###");
            this.et = et;
            hasFractionalPart = false;
        }

        @SuppressWarnings("unused")
        private static final String TAG = "NumberTextWatcher";

        public void afterTextChanged(Editable s) {
            et.removeTextChangedListener(this);

            try {


                int inilen, endlen;
                inilen = et.getText().length();

                String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
//_____________________________get the none Seprated Number_________________________________________
                targetInfoModify.setPriceTarget(v);


                Number n = df.parse(v);
                int cp = et.getSelectionStart();
                if (hasFractionalPart) {
                    et.setText(df.format(n));
                } else {
                    et.setText(dfnd.format(n));
                }
                endlen = et.getText().length();
                int sel = (cp + (endlen - inilen));
                if (sel > 0 && sel <= et.getText().length()) {
                    et.setSelection(sel);
                } else {
                    // place cursor at the end?
                    et.setSelection(et.getText().length() - 1);
                }


            } catch (NumberFormatException nfe) {
                // do nothing?
            } catch (Exception e) {
                // do nothing?
            }

            et.addTextChangedListener(this);

            isTargetChanged();

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()))) {
                hasFractionalPart = true;


            } else {
                hasFractionalPart = false;
            }
        }
    }
}
