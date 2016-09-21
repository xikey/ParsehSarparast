package com.example.zikey.sarparast;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityMandehMoshtarian extends AppCompatActivity {

    public static String ID;

    private int firstIndex = 0;
    private int lastIndex = 100;
    private String search = "";

    private MandehMoshtarianAsync moshtarianAsync = null;

    private boolean isAsyncRun = false;

    private boolean isLoading = false;

    private android.app.FragmentManager manager;

    private MandehMoshtarianAsync mandehMoshtarianAsync;

    private PreferenceHelper preferenceHelper;

    private ImageView imgBack;

    private EditText edtSearch;
    private TextView txtHeader;
    private ImageView btnSearch;

    private RecyclerView row_Mandeh;
    private RecyclerView.LayoutManager row_manager;
    private MandehMoshtariAdapter row_adapter;

    private ArrayList<MandehMoshtariInfo> MandehMoshtaries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandeh_moshtarian);

        edtSearch = (EditText) findViewById(R.id.edtSearch);
        txtHeader = (TextView) findViewById(R.id.txtHeader);
        row_Mandeh = (RecyclerView) findViewById(R.id.row_Mandeh);
        btnSearch = (ImageView) findViewById(R.id.btnSearch);

        //   row_Mandeh.setLayoutManager( new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        manager = getFragmentManager();

        preferenceHelper = new PreferenceHelper(this);

        imgBack = (ImageView) findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txtHeader.setText("اطلاعات مشتریان");
//        edtSearch.setVisibility(View.INVISIBLE);

//


        row_Mandeh.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                G.hideSoftKeyboard(ActivityMandehMoshtarian.this);
                return false;
            }
        });


        row_adapter = new MandehMoshtariAdapter();

        row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        row_Mandeh.setLayoutManager(row_manager);

        row_adapter.setCustomer(MandehMoshtaries);
        row_adapter.setActivity(ActivityMandehMoshtarian.this);

        row_adapter.setManager(manager);

        row_Mandeh.setAdapter(row_adapter);

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                  if (TextUtils.isEmpty(edtSearch.getText().toString())) {
                    MandehMoshtaries.clear();
                    firstIndex=0;
                    lastIndex=100;
                    isLoading = false;
                    search = edtSearch.getText().toString();
                    runMandehMoshtarianAsync();

                } else {
                    MandehMoshtaries.clear();
                    search = edtSearch.getText().toString();
                    isLoading = true;
                    firstIndex=0;
                    lastIndex=100;
                    runMandehMoshtarianAsync();
                
                    return true;
                }
                return false;
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtSearch.getText().toString())) {
                    MandehMoshtaries.clear();
                    firstIndex=0;
                    lastIndex=100;
                    isLoading = false;
                    search = edtSearch.getText().toString();
                    runMandehMoshtarianAsync();

                } else {
                    MandehMoshtaries.clear();
                    search = edtSearch.getText().toString();
                    isLoading = true;
                    firstIndex=0;
                    lastIndex=100;
                    runMandehMoshtarianAsync();
                }
            }
        });

        final LinearLayoutManager layoutManager = (LinearLayoutManager) row_Mandeh.getLayoutManager();

        row_Mandeh.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (layoutManager != null) {

                    Log.e("Log", "last index is .." + layoutManager.findLastVisibleItemPosition());

                    if (!isLoading && layoutManager.getItemCount() - 1 == layoutManager.findLastVisibleItemPosition()) {

                        firstIndex = lastIndex + 1;
                        lastIndex += 100;

                        Log.e("FirstIndex", "FirsiIndex us " + firstIndex);
                        Log.e("LasttIndex", "LastIndex us " + lastIndex);

                        runMandehMoshtarianAsync();

//                                 row_adapter.notifyDataSetChanged();
                    }
                }
            }
        });
        runMandehMoshtarianAsync();
    }

    public void runMandehMoshtarianAsync() {
        if (mandehMoshtarianAsync != null)
            return;

        mandehMoshtarianAsync = new MandehMoshtarianAsync();
        mandehMoshtarianAsync.execute();
    }

    public class MandehMoshtarianAsync extends AsyncTask<Void, String, String> {

        ArrayList<MandehMoshtariInfo> newData = new ArrayList<>();

        Boolean isonline = NetworkTools.isOnline(ActivityMandehMoshtarian.this);

        ProgressDialog dialog;

        @Override
        protected void onPostExecute(String state) {

            mandehMoshtarianAsync = null;

            if (state.equals("Null")) {
                new AlertDialog.Builder(ActivityMandehMoshtarian.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("اطلاعاتی جها نمایش وجود ندارد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }

            if (state.equals("Online")) {

//                newData.size();
                row_adapter.addCustomer(newData);


                if (dialog != null)
                    dialog.dismiss();

                isAsyncRun = true;
            } else if (state.equals("NotOnline")) {
                new AlertDialog.Builder(ActivityMandehMoshtarian.this)
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
                dialog = ProgressDialog.show(ActivityMandehMoshtarian.this, "", "دریافت اطلاعات");
                super.onPreExecute();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {

            HashMap<String, Object> datas = new HashMap<String, Object>();
            String tokenid = preferenceHelper.getString(preferenceHelper.TOKEN_ID);

            datas.put("TokenID", tokenid);
            datas.put("search", search);
            datas.put("firstIndex", firstIndex);
            datas.put("lastIndex", lastIndex);

            Log.i("Indexes", "First " + firstIndex + " Lasts " + lastIndex);

            if (isonline) {

                try {

                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_GetListOF_MandehMoshtaries_Infos", datas).getProperty(0);

                    if (request2.getPropertyCount() <= 0) {
                        isLoading = true;
                        return "Online";
                    }
                    for (int i = 0; i < request2.getPropertyCount(); i++) {

                        SoapObject sp = (SoapObject) request2.getProperty(i);

                        MandehMoshtariInfo mandeh = new MandehMoshtariInfo();

                        mandeh.set_Code(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                        mandeh.set_Name(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                        mandeh.set_Vaset(NetworkTools.getSoapPropertyAsNullableString(sp, 3));
                        mandeh.set_Address(NetworkTools.getSoapPropertyAsNullableString(sp, 4));
                        mandeh.set_LastL(NetworkTools.getSoapPropertyAsNullableString(sp, 5));
                        mandeh.set_LastW(NetworkTools.getSoapPropertyAsNullableString(sp, 6));
                        mandeh.set_Tell(NetworkTools.getSoapPropertyAsNullableString(sp, 7));
                        mandeh.set_Mobile(NetworkTools.getSoapPropertyAsNullableString(sp, 8));
                        mandeh.set_Mandeh(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 2).toString())));

                        newData.add(mandeh);
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




}
