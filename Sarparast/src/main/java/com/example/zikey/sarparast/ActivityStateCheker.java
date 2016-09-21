package com.example.zikey.sarparast;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

public class ActivityStateCheker extends AppCompatActivity {

    private String vaizat;
    private Long ID;
    private String ID_Header;
    private PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_cheker);

        preferenceHelper = new PreferenceHelper(this);
        vaizat = getIntent().getStringExtra("Amaliat");


        if (vaizat.equals("AcceptNewCustomer")){
//            Log.e("sadasdsa","ID IS "+Integer.parseInt(getIntent().getStringExtra("ID")));
            ID_Header =  getIntent().getStringExtra("ID");

            new AcceptNewCustomerAsync().execute();


        }
        if (vaizat.equals("DeclinetNewCustomer")){
//            Log.e("sadasdsa","ID IS "+Integer.parseInt(getIntent().getStringExtra("ID")));
            ID_Header =  getIntent().getStringExtra("ID");

            new DeclineNewCustomerAsync().execute();


        }

        if (vaizat.equals("Nothing")){
            //Taeed sefaresh visitor
//            Log.e("sadasdsa","ID IS "+Integer.parseInt(getIntent().getStringExtra("ID")));
            ID = Long.valueOf(getIntent().getIntExtra("ID",0));

           new NothingRequestAsync().execute();


        }
        if (vaizat.equals("Accept")){
            //Taeed sefaresh visitor
//            Log.e("sadasdsa","ID IS "+Integer.parseInt(getIntent().getStringExtra("ID")));
            ID = Long.valueOf(getIntent().getIntExtra("ID",0));

           new AcceptRequestAsync().execute();


        }

        if (vaizat.equals("Cancel")){
//            Log.e("sadasdsa","ID IS "+Integer.parseInt(getIntent().getStringExtra("ID")));
            ID = Long.valueOf(getIntent().getIntExtra("ID",0));

           new CancelRequestAsync().execute();


        }

 if (vaizat.equals("Forward")){
//            Log.e("sadasdsa","ID IS "+Integer.parseInt(getIntent().getStringExtra("ID")));
            ID = Long.valueOf(getIntent().getIntExtra("ID",0));

           new ForwardRequestAsync().execute();


        }

    }

    private class AcceptRequestAsync extends AsyncTask<Void,String,String> {
        private  String state;

        Boolean isonline= NetworkTools.isOnline(ActivityStateCheker.this);

        ProgressDialog dialog;

        @Override
        protected void onPostExecute(String state) {

            if (state.equals("true")) {
                Log.e("onPostExecute","post staete is "+state);
                if (dialog != null)
                    dialog.dismiss();


                new AlertDialog.Builder(ActivityStateCheker.this)
                        .setCancelable(false)
                        .setTitle("موفقیت")
                        .setMessage("سفارش با موفقیت تایید شد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.success)
                        .show();
            }

            if (state.equals("KhataSQL")) {
                Log.e("onPostExecute","post staete is "+state);
                if (dialog != null)
                    dialog.dismiss();

                new AlertDialog.Builder(ActivityStateCheker.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage(" این سفارش قبلا به فاکتور تبدیل شده است. امکان تایید این سفارش وجود ندارد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.decline)
                        .show();
            }

            if (state.equals("false")) {

                if (dialog != null)
                    dialog.dismiss();


                new AlertDialog.Builder(ActivityStateCheker.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("خطا در ارسال اطلاعات")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.decline)
                        .show();
            }



            else  if (state.equals("NotOnline")){
                new AlertDialog.Builder(ActivityStateCheker.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("اتصال به اینترنت مقدور نمیباشد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }
        }

        @Override
        protected void onPreExecute() {
            //show
            if (isonline) {
                dialog = ProgressDialog.show(ActivityStateCheker.this, "", "ارسال اطلاعات");
                super.onPreExecute();
            }
        }


        @Override
        protected String doInBackground(Void... voids) {

            HashMap<String, Object> datas = new HashMap<String, Object>();
            String tokenid =  preferenceHelper.getString(preferenceHelper.TOKEN_ID);

            datas.put("TokenID",tokenid);
            datas.put("ID_Sefaresh",ID);
            datas.put("vaziat",1);

            if (isonline) {

                try {

                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Accept_Request", datas);
                    state=  NetworkTools.getSoapPropertyAsNullableString(request2,0);
                    if (state.equals("1")){
                        return  "true";
                    }

                    if (state.equals("0")){
                        return  "KhataSQL";
                    }


                    if (state.equals("3")){
                        return  "false";
                    }

                } catch (Exception e) {

                    Log.e("iiiiiii", "connot read Soap");
                    e.printStackTrace();
                    return "false";
                }

            }
            return "NotOnline";
        }


    }


 private class CancelRequestAsync extends AsyncTask<Void,String,String> {
     private  String state;

        Boolean isonline= NetworkTools.isOnline(ActivityStateCheker.this);

        ProgressDialog dialog;

        @Override
        protected void onPostExecute(String state) {

            if (state.equals("true")) {
                Log.e("onPostExecute","post staete is "+state);
                if (dialog != null)
                    dialog.dismiss();


                new AlertDialog.Builder(ActivityStateCheker.this)
                        .setCancelable(false)
                        .setTitle("موفقیت")
                        .setMessage("سفارش با موفقیت لغو شد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.success)
                        .show();
            }

            if (state.equals("KhataSQL")) {
                Log.e("onPostExecute","post staete is "+state);
                if (dialog != null)
                    dialog.dismiss();


                new AlertDialog.Builder(ActivityStateCheker.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("این سفارش قبلا به فاکتور تبدیل شده است. امکان لغو این سفارش وجود ندارد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.decline)
                        .show();
            }

            if (state.equals("false")) {

                if (dialog != null)
                    dialog.dismiss();


                new AlertDialog.Builder(ActivityStateCheker.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("خطا در ارسال اطلاعات")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.decline)
                        .show();
            }



            else  if (state.equals("NotOnline")){
                new AlertDialog.Builder(ActivityStateCheker.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("اتصال به اینترنت مقدور نمیباشد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }
        }

        @Override
        protected void onPreExecute() {
            //show
            if (isonline) {
                dialog = ProgressDialog.show(ActivityStateCheker.this, "", "ارسال اطلاعات");
                super.onPreExecute();
            }
        }


        @Override
        protected String doInBackground(Void... voids) {

            HashMap<String, Object> datas = new HashMap<String, Object>();
            String tokenid =  preferenceHelper.getString(preferenceHelper.TOKEN_ID);

            datas.put("TokenID",tokenid);
            datas.put("ID_Sefaresh",ID);
            datas.put("vaziat",2);

            if (isonline) {

                try {

                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Accept_Request", datas);
                    state=  NetworkTools.getSoapPropertyAsNullableString(request2,0);
                    if (state.equals("1")){
                        return  "true";
                    }

                    if (state.equals("0")){
                        return  "KhataSQL";
                    }


                    if (state.equals("3")){
                        return  "false";
                    }

                } catch (Exception e) {

                    Log.e("iiiiiii", "connot read Soap");
                    e.printStackTrace();
                    return "false";
                }

            }
            return "NotOnline";
        }


    }





    private class ForwardRequestAsync extends AsyncTask<Void,String,String> {
        private  String state;

        Boolean isonline= NetworkTools.isOnline(ActivityStateCheker.this);

        ProgressDialog dialog;

        @Override
        protected void onPostExecute(String state) {


            if (state.equals("true")) {
                Log.e("onPostExecute","post staete is "+state);
                if (dialog != null)
                    dialog.dismiss();


                new AlertDialog.Builder(ActivityStateCheker.this)
                        .setCancelable(false)
                        .setTitle("موفقیت")
                        .setMessage("سفارش با موفقیت ارجاع داده شد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.success)
                        .show();
            }

            if (state.equals("KhataSQL")) {
                Log.e("onPostExecute","post staete is "+state);
                if (dialog != null)
                    dialog.dismiss();


                new AlertDialog.Builder(ActivityStateCheker.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("این سفارش قبلا به فاکتور تبدیل شده است. امکان ارجاع این سفارش وجود ندارد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.decline)
                        .show();
            }

            if (state.equals("false")) {

                if (dialog != null)
                    dialog.dismiss();


                new AlertDialog.Builder(ActivityStateCheker.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("خطا در ارسال اطلاعات")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.decline)
                        .show();
            }



            else  if (state.equals("NotOnline")){
                new AlertDialog.Builder(ActivityStateCheker.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("اتصال به اینترنت مقدور نمیباشد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }
        }

        @Override
        protected void onPreExecute() {
            //show
            if (isonline) {
                dialog = ProgressDialog.show(ActivityStateCheker.this, "", "ارسال اطلاعات");
                super.onPreExecute();
            }
        }


        @Override
        protected String doInBackground(Void... voids) {

            HashMap<String, Object> datas = new HashMap<String, Object>();
            String tokenid =  preferenceHelper.getString(preferenceHelper.TOKEN_ID);

            datas.put("TokenID",tokenid);
            datas.put("ID_Sefaresh",ID);
            datas.put("vaziat",3);

            if (isonline) {

                    try {

                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Accept_Request", datas);
                    state=  NetworkTools.getSoapPropertyAsNullableString(request2,0);
                    Log.e("state   ", "state is   "+state);
                    if (state.equals("1")){
                        return  "true";
                    }

                         if (state.equals("0")){
                            return  "KhataSQL";
                        }


                        if (state.equals("3")){
                        return  "false";
                    }

                } catch (Exception e) {

                    Log.e("iiiiiii", "connot read Soap");
                       e.printStackTrace();
                        return "false";
                }

            }
            return "NotOnline";
        }


    }





    private class AcceptNewCustomerAsync extends AsyncTask<Void,String,String> {
        private  String state;

        Boolean isonline= NetworkTools.isOnline(ActivityStateCheker.this);

        ProgressDialog dialog;

        @Override
        protected void onPostExecute(String state) {


            if (state.equals("true")) {
                Log.e("onPostExecute","post staete is "+state);
                if (dialog != null)
                    dialog.dismiss();


                new AlertDialog.Builder(ActivityStateCheker.this)
                        .setCancelable(false)
                        .setTitle("موفقیت")
                        .setMessage("مشتری جدید با موفقیت ثبت شد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.success)
                        .show();
            }

            if (state.equals("KhataSQL")) {
                Log.e("onPostExecute","post staete is "+state);
                if (dialog != null)
                    dialog.dismiss();


                new AlertDialog.Builder(ActivityStateCheker.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("این سفارش قبلا به فاکتور تبدیل شده است. امکان ارجاع این سفارش وجود ندارد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.decline)
                        .show();
            }

            if (state.equals("false")) {

                if (dialog != null)
                    dialog.dismiss();


                new AlertDialog.Builder(ActivityStateCheker.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("خطا در ارسال اطلاعات")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.decline)
                        .show();
            }



            else  if (state.equals("NotOnline")){
                new AlertDialog.Builder(ActivityStateCheker.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("اتصال به اینترنت مقدور نمیباشد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }
        }

        @Override
        protected void onPreExecute() {
            //show
            if (isonline) {
                dialog = ProgressDialog.show(ActivityStateCheker.this, "", "ارسال اطلاعات");
                super.onPreExecute();
            }
        }


        @Override
        protected String doInBackground(Void... voids) {

            HashMap<String, Object> datas = new HashMap<String, Object>();
            String tokenid =  preferenceHelper.getString(preferenceHelper.TOKEN_ID);

            datas.put("TokenID",tokenid);
            datas.put("ID_Header",ID_Header);
            datas.put("Vaziat",1);

            if (isonline) {

                try {

                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Accept_New_Customer", datas);
                    state=  NetworkTools.getSoapPropertyAsNullableString(request2,0);
                    Log.e("state   ", "state is   "+state);
                    if (state.equals("1")){
                        return  "true";
                    }

                    if (state.equals("0")){
                        return  "KhataSQL";
                    }


                    if (state.equals("3")){
                        return  "false";
                    }

                } catch (Exception e) {

                    Log.e("iiiiiii", "connot read Soap");
                    e.printStackTrace();
                    return "false";
                }

            }
            return "NotOnline";
        }


    }



    private class DeclineNewCustomerAsync extends AsyncTask<Void,String,String> {
        private  String state;

        Boolean isonline= NetworkTools.isOnline(ActivityStateCheker.this);

        ProgressDialog dialog;

        @Override
        protected void onPostExecute(String state) {


            if (state.equals("true")) {
                Log.e("onPostExecute","post staete is "+state);
                if (dialog != null)
                    dialog.dismiss();


                new AlertDialog.Builder(ActivityStateCheker.this)
                        .setCancelable(false)
                        .setTitle("موفقیت")
                        .setMessage("مشتری جدید با موفقیت لغو شد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.success)
                        .show();
            }

            if (state.equals("KhataSQL")) {
                Log.e("onPostExecute","post staete is "+state);
                if (dialog != null)
                    dialog.dismiss();


                new AlertDialog.Builder(ActivityStateCheker.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("این سفارش قبلا به فاکتور تبدیل شده است. امکان ارجاع این سفارش وجود ندارد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.decline)
                        .show();
            }

            if (state.equals("false")) {

                if (dialog != null)
                    dialog.dismiss();


                new AlertDialog.Builder(ActivityStateCheker.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("خطا در ارسال اطلاعات")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.decline)
                        .show();
            }



            else  if (state.equals("NotOnline")){
                new AlertDialog.Builder(ActivityStateCheker.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("اتصال به اینترنت مقدور نمیباشد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }
        }

        @Override
        protected void onPreExecute() {
            //show
            if (isonline) {
                dialog = ProgressDialog.show(ActivityStateCheker.this, "", "ارسال اطلاعات");
                super.onPreExecute();
            }
        }


        @Override
        protected String doInBackground(Void... voids) {

            HashMap<String, Object> datas = new HashMap<String, Object>();
            String tokenid =  preferenceHelper.getString(preferenceHelper.TOKEN_ID);
            int vc = 2;

            datas.put("TokenID",tokenid);
            datas.put("ID_Header",ID_Header);
            datas.put("Vaziat",vc);

            if (isonline) {

                try {

                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Accept_New_Customer", datas);
                    state=  NetworkTools.getSoapPropertyAsNullableString(request2,0);
                    Log.e("state   ", "state is   "+state);
                    if (state.equals("1")){
                        return  "true";
                    }

                    if (state.equals("0")){
                        return  "KhataSQL";
                    }


                    if (state.equals("3")){
                        return  "false";
                    }

                } catch (Exception e) {

                    Log.e("iiiiiii", "connot read Soap");
                    e.printStackTrace();
                    return "false";
                }

            }
            return "NotOnline";
        }


    }


    private class NothingRequestAsync extends AsyncTask<Void,String,String> {
        private  String state;

        Boolean isonline= NetworkTools.isOnline(ActivityStateCheker.this);

        ProgressDialog dialog;

        @Override
        protected void onPostExecute(String state) {

            if (state.equals("true")) {
                Log.e("onPostExecute","post staete is "+state);
                if (dialog != null)
                    dialog.dismiss();


                new AlertDialog.Builder(ActivityStateCheker.this)
                        .setCancelable(false)
                        .setTitle("موفقیت")
                        .setMessage("عملیات با موفقیت انجام شد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.success)
                        .show();
            }

            if (state.equals("KhataSQL")) {
                Log.e("onPostExecute","post staete is "+state);
                if (dialog != null)
                    dialog.dismiss();

                new AlertDialog.Builder(ActivityStateCheker.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage(" این سفارش قبلا به فاکتور تبدیل شده است. امکان تایید این سفارش وجود ندارد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.decline)
                        .show();
            }

            if (state.equals("false")) {

                if (dialog != null)
                    dialog.dismiss();


                new AlertDialog.Builder(ActivityStateCheker.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("خطا در ارسال اطلاعات")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.decline)
                        .show();
            }



            else  if (state.equals("NotOnline")){
                new AlertDialog.Builder(ActivityStateCheker.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("اتصال به اینترنت مقدور نمیباشد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }
        }

        @Override
        protected void onPreExecute() {
            //show
            if (isonline) {
                dialog = ProgressDialog.show(ActivityStateCheker.this, "", "ارسال اطلاعات");
                super.onPreExecute();
            }
        }


        @Override
        protected String doInBackground(Void... voids) {

            HashMap<String, Object> datas = new HashMap<String, Object>();
            String tokenid =  preferenceHelper.getString(preferenceHelper.TOKEN_ID);

            datas.put("TokenID",tokenid);
            datas.put("ID_Sefaresh",ID);
            datas.put("vaziat",0);

            if (isonline) {

                try {

                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Accept_Request", datas);
                    state=  NetworkTools.getSoapPropertyAsNullableString(request2,0);
                    if (state.equals("1")){
                        return  "true";
                    }

                    if (state.equals("0")){
                        return  "KhataSQL";
                    }


                    if (state.equals("3")){
                        return  "false";
                    }

                } catch (Exception e) {

                    Log.e("iiiiiii", "connot read Soap");
                    e.printStackTrace();
                    return "false";
                }

            }
            return "NotOnline";
        }


    }


}
