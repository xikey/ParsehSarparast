package com.example.zikey.sarparast;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityListOfBazaryab extends AppCompatActivity {
    private PreferenceHelper preferenceHelper;
    private ImageView imgBack;

    private RecyclerView row_bazaryab;
    private BazaryabInfoAdapterMasirSabt row_masirsabt;
    private BazaryabInfoAdapterMasirHarkat row_masirharkat;
    private VisitorsNotOrderedAdapter row_notOrdered;

    private RecyclerView.LayoutManager row_manager;
    private RecyclerView.Adapter row_adapter;
    private EditText edtSearch;

    private GetListOfBazaryabAsync getListOfBazaryabAsync = null;

    private TextView txtToolbar;

    private String state;

    /**
     * its param for fake bind
     */
    private final Boolean isfakeBind = false;

    ArrayList<BazaryabInfo> bazaryabs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_bazaryab);

        edtSearch = (EditText) findViewById(R.id.edtSearch);

        state = getIntent().getStringExtra("state");

        preferenceHelper = new PreferenceHelper(this);
        imgBack = (ImageView) findViewById(R.id.imgBack);

        txtToolbar = (TextView) findViewById(R.id.txtToolbar);

        row_bazaryab = (RecyclerView) findViewById(R.id.row_bazaryab);


        row_bazaryab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                G.hideSoftKeyboard(ActivityListOfBazaryab.this);
                return false;
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (state.equals("BazaryabLists")) {

            //Masir Harkat
            edtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    row_masirharkat.getFilter().filter(editable.toString());
                }
            });
            runGetListOfBazaryabAsync();

        }

        if (state.equals("NotOrdered")) {

            edtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    row_notOrdered.getFilter().filter(editable.toString());
                }
            });
            txtToolbar.setText("مشتریانی ک خرید نکرده اند");
            new ListOFCustomersNotOrdered().execute();
        }


        if (state.equals("SabtSefaresh")) {

            txtToolbar.setText("مسیر ثبت سفارش");
            edtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    row_masirsabt.getFilter().filter(editable.toString());
                }
            });


            new GetListOfBazaryabMasirSabtAsync().execute();
        }

    }

    private void runGetListOfBazaryabAsync() {
        if (getListOfBazaryabAsync != null)
            return;

        //if is online?

        getListOfBazaryabAsync = new GetListOfBazaryabAsync();
        getListOfBazaryabAsync.execute();

    }


    public class GetListOfBazaryabAsync extends AsyncTask<Void, String, String> {

        Boolean isonline = NetworkTools.isOnline(ActivityListOfBazaryab.this);
        ProgressDialog dialog;

        private Throwable error = null;

        @Override
        protected void onPostExecute(String state) {

            getListOfBazaryabAsync = null;

            if (isfakeBind) {
                row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

                row_bazaryab.setLayoutManager(row_manager);

                bazaryabs.clear();

                for (int i = 0; i < 2; i++) {
                    BazaryabInfo info = new BazaryabInfo();

                    info.set_Name("test " + i);
                    info.set_Code("1000" + i);
                    info.set_ID("" + i);
                    info.set_Time("1395/12/12");
                    bazaryabs.add(info);
                }


                row_bazaryab.setAdapter(row_adapter);

                if (dialog != null)
                    dialog.dismiss();

                return;
            }

            if (state.equals("Null")) {
                new AlertDialog.Builder(ActivityListOfBazaryab.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("اطلاعاتی جهت نمایش وجود ندارد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }

            if (state.equals("Online")) {

                row_masirharkat = new BazaryabInfoAdapterMasirHarkat(bazaryabs);
                row_masirharkat.setActivity(ActivityListOfBazaryab.this);
                row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                row_bazaryab.setLayoutManager(row_manager);

                row_bazaryab.setAdapter(row_masirharkat);

                if (dialog != null)
                    dialog.dismiss();
            } else if (state.equals("NotOnline")) {
                new AlertDialog.Builder(ActivityListOfBazaryab.this)
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
            if (isonline) {

                dialog = ProgressDialog.show(ActivityListOfBazaryab.this, "", "دریافت اطلاعات");
                super.onPreExecute();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {

            if (isfakeBind) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    error = e;
                }
                return "";
            }

            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));

            if (isonline) {
                try {
                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Navigation_ListOfVisitors", datas).getProperty(0);

                    if (request2.getPropertyCount() <= 0) {
                        return "Null";
                    }

                    for (int i = 0; i < request2.getPropertyCount(); i++) {
                        SoapObject sp = (SoapObject) request2.getProperty(i);
//                        Log.e("tttttttttttt", "" + sp);

                        BazaryabInfo bazaryab = new BazaryabInfo();
                        bazaryab.set_Name(NetworkTools.getSoapPropertyAsNullableString(sp, 2));
                        bazaryab.set_Code(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                        bazaryab.set_ID(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                        bazaryab.set_Time(NetworkTools.getSoapPropertyAsNullableString(sp, 3));

                        bazaryabs.add(bazaryab);
                    }

                } catch (Exception e) {
                    Log.e("iiiiiii", "connot read Soap");
                    e.printStackTrace();
                }
                return "Online";
            }
            return "NotOnline";

        }

    }

    public class GetListOfBazaryabMasirSabtAsync extends AsyncTask<Void, String, String> {

        Boolean isonline = NetworkTools.isOnline(ActivityListOfBazaryab.this);
        ProgressDialog dialog;

        @Override
        protected void onPostExecute(String state) {

            if (state.equals("Null")) {
                new AlertDialog.Builder(ActivityListOfBazaryab.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("اطلاعاتی جهت نمایش وجود ندارد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }

            if (state.equals("Online")) {

                row_masirsabt = new BazaryabInfoAdapterMasirSabt(bazaryabs);
                row_masirsabt.setActivity(ActivityListOfBazaryab.this);
                row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                row_bazaryab.setLayoutManager(row_manager);
                row_bazaryab.setAdapter(row_masirsabt);

                if (dialog != null)
                    dialog.dismiss();
            } else if (state.equals("NotOnline")) {
                new AlertDialog.Builder(ActivityListOfBazaryab.this)
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
            if (isonline) {

                dialog = ProgressDialog.show(ActivityListOfBazaryab.this, "", "دریافت اطلاعات");
                super.onPreExecute();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));

            if (isonline) {
                try {
                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Navigation_ListOfVisitors_MasirSabt", datas).getProperty(0);

                    if (request2.getPropertyCount() <= 0) {
                        return "Null";
                    }

                    for (int i = 0; i < request2.getPropertyCount(); i++) {
                        SoapObject sp = (SoapObject) request2.getProperty(i);
//                        Log.e("tttttttttttt", "" + sp);

                        BazaryabInfo bazaryab = new BazaryabInfo();

                        bazaryab.set_Name(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                        bazaryab.set_Code(NetworkTools.getSoapPropertyAsNullableString(sp, 3));
                        bazaryab.set_ID(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                        bazaryab.set_Time(NetworkTools.getSoapPropertyAsNullableString(sp, 2));

                        bazaryabs.add(bazaryab);
                    }

                } catch (Exception e) {
                    Log.e("iiiiiii", "connot read Soap");
                    e.printStackTrace();
                    return "Null";
                }
                return "Online";
            }
            return "NotOnline";
        }
    }


    public class ListOFCustomersNotOrdered extends AsyncTask<Void, String, String> {

        Boolean isonline = NetworkTools.isOnline(ActivityListOfBazaryab.this);
        ProgressDialog dialog;

        private Throwable error = null;

        @Override
        protected void onPostExecute(String state) {

            getListOfBazaryabAsync = null;

//            if (isfakeBind)
//            {
//                row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
//
//                row_bazaryab.setLayoutManager(row_manager);
//
//                bazaryabs.clear();
//
//                for (int i = 0 ; i< 2 ; i++)
//                {
//                    BazaryabInfo info = new BazaryabInfo();
//
//                    info.set_Name("test "+ i);
//                    info.set_Code("1000"+i);
//                    info.set_ID(""+i);
//                    info.set_Time("1395/12/12");
//                    bazaryabs.add(info);
//                }
//
//                row_masirharkat = new BazaryabInfoAdapterMasirHarkat(bazaryabs);
//                row_bazaryab.setAdapter(row_masirharkat);
//
//                if (dialog != null)
//                    dialog.dismiss();
//
//                return;
//            }

            if (state.equals("Null")) {
                new AlertDialog.Builder(ActivityListOfBazaryab.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("اطلاعاتی جهت نمایش وجود ندارد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }

            if (state.equals("Online")) {

                row_notOrdered = new VisitorsNotOrderedAdapter(bazaryabs);
                row_notOrdered.setActivity(ActivityListOfBazaryab.this);
                row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                row_bazaryab.setLayoutManager(row_manager);
                row_notOrdered.setActivity(ActivityListOfBazaryab.this);

                row_bazaryab.setAdapter(row_notOrdered);

                if (dialog != null)
                    dialog.dismiss();
            } else if (state.equals("NotOnline")) {
                new AlertDialog.Builder(ActivityListOfBazaryab.this)
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
            if (isonline) {

                dialog = ProgressDialog.show(ActivityListOfBazaryab.this, "", "دریافت اطلاعات");
                super.onPreExecute();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {

            if (isfakeBind) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    error = e;
                }
                return "";
            }

            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));

            if (isonline) {
                try {
                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Navigation_ListOfVisitor_Not_Ordered", datas).getProperty(0);

                    if (request2.getPropertyCount() <= 0) {
                        return "Null";
                    }
                    for (int i = 0; i < request2.getPropertyCount(); i++) {
                        SoapObject sp = (SoapObject) request2.getProperty(i);
//                        Log.e("tttttttttttt", "" + sp);

                        BazaryabInfo bazaryab = new BazaryabInfo();

                        bazaryab.set_Name(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                        bazaryab.set_Code(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                        bazaryab.set_Total(NetworkTools.getSoapPropertyAsNullableString(sp, 2));
                        bazaryab.set_NotOrdered(NetworkTools.getSoapPropertyAsNullableString(sp, 3));

                        bazaryabs.add(bazaryab);
                    }

                } catch (Exception e) {
                    Log.e("iiiiiii", "connot read Soap");
                    e.printStackTrace();
                }
                return "Online";
            }
            return "NotOnline";

        }

    }

}
