package com.example.zikey.sarparast;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ActivityCustomersDistance extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private PreferenceHelper preferenceHelper;
    private TextView txtHeader;
    private EditText edtSearch;
    private TextView txtAverage;
    private TextView txtEror;
    private ImageView imgBack;
    private ImageView imgCalendar;
    private RelativeLayout lyContent;
    private RelativeLayout lyEror;
    private RelativeLayout lyProgress;
    private RecyclerView customerRecycle;
    private PersianCalendar persianCalendar = null;
    private DatePickerDialog datePickerDialog = null;
    private GetCustomerAsync getCustomerAsync = null;
    private LinearLayoutManager layoutManager;

    private CustomersDistanceListAdapter adapter;
    private ArrayList<BazaryabInfo> points = new ArrayList<>();
    private String date = "";
    private int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_distance);

        initViews();
        initRecycleView();
        ID = parseIntent(getIntent());

        runAsunc("");

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {


        String date = changeDate(year, monthOfYear, dayOfMonth);
        runAsunc(date);

        txtHeader.setText("" + " لیست مشتریان ـ " + date);

    }


    private void initViews() {
        txtHeader = (TextView) findViewById(R.id.txtTop);
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        txtAverage = (TextView) findViewById(R.id.txtAverage);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgCalendar = (ImageView) findViewById(R.id.imgCalendar);
        lyContent = (RelativeLayout) findViewById(R.id.lyContent);
        lyEror = (RelativeLayout) findViewById(R.id.lyEror);
        lyProgress = (RelativeLayout) findViewById(R.id.lyProgress);
        preferenceHelper = new PreferenceHelper(this);
        txtEror = (TextView) findViewById(R.id.txtEror);

        lyContent.setVisibility(View.GONE);
        lyEror.setVisibility(View.GONE);
        lyProgress.setVisibility(View.VISIBLE);

        txtHeader.setText("لیست مشتریان ـ امروز");

        imgCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPersianCalender(persianCalendar, datePickerDialog);
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                adapter.getFilter().filter(editable.toString());
            }
        });
    }

    private void runAsunc(String date) {

        if (getCustomerAsync == null) {
            points.clear();

            getCustomerAsync = new GetCustomerAsync(date);
            getCustomerAsync.execute();

        } else return;

    }

    private void showPersianCalender(PersianCalendar calender, DatePickerDialog dialog) {

        calender = new PersianCalendar();
        dialog = DatePickerDialog.newInstance(ActivityCustomersDistance.this, calender.getPersianYear(), calender.getPersianMonth(),
                calender.getPersianDay());

        dialog.setThemeDark(false);
        dialog.show(getFragmentManager(), "Datepickerdialog");


    }

    private String changeDate(int year, int monthOfYear, int dayOfMonth) {
        String month;
        String day;
        if (monthOfYear < 10) month = "0" + (monthOfYear + 1);
        else month = "" + (monthOfYear + 1);
        if (dayOfMonth < 10) day = "0" + (dayOfMonth);
        else day = "" + (dayOfMonth);
        return (year + "/" + month + "/" + day);
    }


    private void initRecycleView() {

        customerRecycle = (RecyclerView) findViewById(R.id.row_Customers);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        customerRecycle.setLayoutManager(layoutManager);
        adapter = new CustomersDistanceListAdapter(ActivityCustomersDistance.this);
        adapter.setItem(points);
        customerRecycle.setAdapter(adapter);

    }

    private double getDistanceAverage(ArrayList<BazaryabInfo> array) {

        if (array == null || array.size() == 0)
            return 0;
        double average = 0;
        int i = array.size();
        for (int j = 0; j < i; j++) {
            average += array.get(j).get_Distance();
        }
        return Math.round(average / i);

    }


    public class GetCustomerAsync extends AsyncTask<Void, String, String> {
        Boolean isonline = NetworkTools.isOnline(ActivityCustomersDistance.this);

        public GetCustomerAsync(String date) {
            this.date = date;
        }

        private String date = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {


            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            datas.put("ID", ID);
            datas.put("thisDate", date);

            if (isonline) {
                try {
                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_Navigation_ListOfVisitorMasirSabtPoints", datas).getProperty(0);

                    if (request2.getPropertyCount() <= 0) return "null";

                    for (int i = 0; i < request2.getPropertyCount(); i++) {
                        SoapObject sp = (SoapObject) request2.getProperty(i);

                        Log.e("Points", "Point is " + sp);

                        BazaryabInfo point = new BazaryabInfo();

                        if (!TextUtils.isEmpty(NetworkTools.getSoapPropertyAsNullableString(sp, 0))) {
                            String value = (NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                            if (!value.equals("0")) {

                                point.set_W(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                                point.set_L(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                                point.set_Code(NetworkTools.getSoapPropertyAsNullableString(sp, 2));
                                point.set_Name(NetworkTools.getSoapPropertyAsNullableString(sp, 3));
                                point.set_Tel(NetworkTools.getSoapPropertyAsNullableString(sp, 4));
                                point.set_Address(NetworkTools.getSoapPropertyAsNullableString(sp, 6));
                                point.setWrappers(ActivityCustomersDistance.NavigationWrapper.getNavigationWrappers(NetworkTools.getSoapPropertyAsNullableString(sp, 7)));
                                point.set_CustomerLat(NetworkTools.getSoapPropertyAsNullableString(sp, 8));
                                point.set_CustomerLong(NetworkTools.getSoapPropertyAsNullableString(sp, 9));


                                if (!NetworkTools.getSoapPropertyAsNullableString(sp, 8).equals("0")) {
                                    float[] results = new float[1];
                                    Double lat1 = Double.valueOf(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                                    Double long1 = Double.valueOf(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                                    Double lat2 = Double.valueOf(NetworkTools.getSoapPropertyAsNullableString(sp, 8));
                                    Double long2 = Double.valueOf(NetworkTools.getSoapPropertyAsNullableString(sp, 9));

                                    Location.distanceBetween(lat1, long1, lat2, long2, results);

                                    point.set_Distance(Double.valueOf(Math.round(results[0])));
                                }

                                points.add(point);
                            }
                        }
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
                return "Online";
            }
            return "NotOnline";
        }


        @Override
        protected void onPostExecute(String s) {
            getCustomerAsync = null;

            if (points.size()==0||points==null){
                lyProgress.setVisibility(View.GONE);
                lyEror.setVisibility(View.VISIBLE);
                txtEror.setText("اطلاعاتی جهت نمایش وجود ندارد");
                return;
            }

            if (s.equals("null")) {

                lyProgress.setVisibility(View.GONE);
                lyEror.setVisibility(View.VISIBLE);
            }

            if (s.equals("Online")) {

                Collections.sort(points);
                adapter.setItem(points);
                txtAverage.setText(" میانگین مسافت ویزیتور: " + getDistanceAverage(points) + " متر ");
                lyContent.setVisibility(View.VISIBLE);
                lyProgress.setVisibility(View.GONE);

            }

            if (s.equals("NotOnline")) {
                lyProgress.setVisibility(View.GONE);
                lyEror.setVisibility(View.VISIBLE);
                txtEror.setText("خطا در اتصال به اینترنت");
            }
            super.onPostExecute(s);
        }
    }


    private Integer parseIntent(Intent data) {
        if (data == null) return null;

        if (!data.hasExtra("Code")) return null;

        return data.getIntExtra("Code", 0);

    }

    public static class NavigationWrapper {
        public String title;
        public String value;

        public NavigationWrapper(String jsonStr) throws JSONException {

            JSONObject obj = new JSONObject(jsonStr);

            this.title = obj.getString("t");
            this.value = obj.getString("v");
        }

        public static ArrayList<ActivityGoogleMap.NavigationWrapper> getNavigationWrappers(String jsonArr) throws JSONException {
            ArrayList<ActivityGoogleMap.NavigationWrapper> output = new ArrayList<>();

            JSONArray arr = new JSONArray(jsonArr);

            for (int i = 0; i < arr.length(); i++) {
                ActivityGoogleMap.NavigationWrapper n = new ActivityGoogleMap.NavigationWrapper(arr.getString(i));
                output.add(n);
            }

            return output;
        }
    }

}
