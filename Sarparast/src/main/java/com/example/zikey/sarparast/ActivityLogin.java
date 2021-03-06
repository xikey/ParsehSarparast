package com.example.zikey.sarparast;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.zikey.sarparast.Helpers.Convertor;
import com.example.zikey.sarparast.Helpers.DeviceInfos;
import com.example.zikey.sarparast.Helpers.FontApplier;
import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendarConstants;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendarUtils;
import com.razanPardazesh.supervisor.tools.FontChanger;

import org.ksoap2.serialization.SoapObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;


public class ActivityLogin extends AppCompatActivity {
    public PreferenceHelper preferenceHelper;
    private AutoCompleteTextView userID;
    private AutoCompleteTextView pass;
    private AutoCompleteTextView edtUrl;
    private String deviceInfo;


    //   FloatingActionButton login;
    Button login;


    private LinearLayout lyHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login5);


        userID = (AutoCompleteTextView) findViewById(R.id.username);
        pass = (AutoCompleteTextView) findViewById(R.id.password);
        edtUrl = (AutoCompleteTextView) findViewById(R.id.edtUrl);


        //  login = (FloatingActionButton) findViewById(R.id.btnLogin);
        login = (Button) findViewById(R.id.btnLogin);
        lyHead = (LinearLayout) findViewById(R.id.lyrt);

        FontChanger.applyTitleFont(login);
        initVersionName();
        preferenceHelper = new PreferenceHelper(this);

        deviceInfo = DeviceInfos.getDeviceModel() + " " + DeviceInfos.getAndroidVersion();


        FontApplier.applyMainFont(getApplicationContext(), lyHead);


        if (!preferenceHelper.getString("UserName").equals("")) {
            userID.setText(preferenceHelper.getString("UserName"));
        }


        if (!preferenceHelper.getString(NetworkTools.URL).isEmpty()) {
            edtUrl.setText(preferenceHelper.getString(NetworkTools.URL));

        } else {
            edtUrl.setText("");
        }

        userID.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {

                    return true;
                }
                return false;
            }
        });

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                G.hideSoftKeyboard(ActivityLogin.this);

                preferenceHelper.putString("UserName", userID.getText().toString());


                if (userID.getText().toString().equals("")) {
                    userID.setError("please insert userID");


                }
                if (pass.getText().toString().equals("")) {
                    pass.setError("please insert PassWord");


                } else if ((!pass.getText().toString().equals("")) && (!userID.getText().toString().equals(""))) {
                    Log.e("kkkkkkk", "userid is " + userID.getText().toString());

                    String url = edtUrl.getText().toString();

                    preferenceHelper.putString(NetworkTools.URL, url);
                    new LoginAsync().execute();
                }

            }
        });


    }




    public class LoginAsync extends AsyncTask<Void, String, String> {
        ProgressDialog dialog;
        private String eror;

        @Override
        protected String doInBackground(Void... params) {

            Object ID = userID.getText().toString();
            Object password = pass.getText().toString();
            Log.i("MMMMMMM", "user pass is " + ID + " " + password);


            Boolean isonline = NetworkTools.isOnline(ActivityLogin.this);

            if (isonline == false) {
                return "internet";
            }


            if (isonline == true) {
                try {

                    URL myurl = new URL("http://" + preferenceHelper.getString(NetworkTools.URL));
                    HttpURLConnection connection = (HttpURLConnection) myurl
                            .openConnection();
                    connection.setRequestMethod("GET");
                    connection.setDoOutput(true);
                    int response = connection.getResponseCode();
                    Log.e("ggggggggggggggg", "response" + response + "___" + myurl);
                } catch (MalformedURLException e) {
                    Log.e("URL EROR", "URL is not Valid");
                    e.printStackTrace();
                    return "url";
                } catch (IOException e) {
                    Log.e("URL EROR", "connot connect to URL");
                    e.printStackTrace();
                    return "url";
                }


                HashMap<String, Object> datas = new HashMap<String, Object>();
                datas.put("id", ID);
                datas.put("password", password);
                datas.put("deviceInfo", deviceInfo);

                try {
                    SoapObject request = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), NetworkTools.METHOD_NAME, datas).getProperty(0);

                    if (request.getPropertyCount() == 1) {
                        eror = "رمز عبور یا نام کاربری اشتباه میباشد";
                        return "eror";
                    }

                    String tokenId = NetworkTools.getSoapPropertyAsNullableString(request, 0);

                    String userName = NetworkTools.getSoapPropertyAsNullableString(request, 2);

                    preferenceHelper.putString(preferenceHelper.TOKEN_ID, tokenId);
                    preferenceHelper.putString(preferenceHelper.USER_NAME, userName);
                    return "1";


                } catch (Exception e) {
                    eror = "  امکان برقراری ارتباط با آدرس سرور وارد شده نمیباشد.  " + (e.toString());
                    return "eror";
                }
            }
            return "0";
        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ActivityLogin.this, "ورود", "دریافت اطلاعات");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String state) {
            super.onPostExecute(state);


            if (state.equals("0")) {
                new AlertDialog.Builder(ActivityLogin.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("نام کاربری یا کلمه عبور صحیح نمیباشد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //...
                            }
                        })
//                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            // do nothing
//                        }
//                    })
                        .setIcon(R.drawable.eror_dialog)
                        .show();

            }
            if (state.equals("url")) {
                new AlertDialog.Builder(ActivityLogin.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("امکان برقراری اتصال با سرور نمیباشد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }
            if (state.equals("internet")) {
                new AlertDialog.Builder(ActivityLogin.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("اتصال به اینترنت مقدور نمیباشد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }

            if (state.equals("eror")) {
                new AlertDialog.Builder(ActivityLogin.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage(eror)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }

            if (state.equals("1")) {
                preferenceHelper.putString(preferenceHelper.IS_LOGIN, "yes");
                Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
                startActivity(intent);
                finish();
            }
            if (dialog != null)
                dialog.dismiss();
        }

    }

    public void initVersionName() {
        TextView txtParseh = (TextView) findViewById(R.id.textView2);
        PackageManager manager = getApplicationContext().getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(getApplicationContext().getPackageName(), 0);
            String version = info.versionName;
            txtParseh.setText(txtParseh.getText() + " - v." + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }


}
