package com.example.zikey.sarparast;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.FontApplier;
import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.razanPardazesh.supervisor.CoveragePercentageActivity;
import com.razanPardazesh.supervisor.RegionActivity;
import com.razanPardazesh.supervisor.EditedCustomerListActivity;
import com.razanPardazesh.supervisor.model.wrapper.CustomerEditAnswer;
import com.razanPardazesh.supervisor.model.wrapper.ReportAnswer;
import com.razanPardazesh.supervisor.repo.CustomersEditedServerRepo;
import com.razanPardazesh.supervisor.repo.ReportServerRepo;
import com.razanPardazesh.supervisor.repo.iRepo.ICustomersEdited;

import org.ksoap2.serialization.SoapObject;


import java.util.ArrayList;
import java.util.HashMap;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public PreferenceHelper preferenceHelper;

    private ScrollView scrollView;
    private ViewPager pager;
    private Button btnNews;
    private LayoutInflater inflater;

    private LinearLayout lyForoosh;
    private LinearLayout lyAnbar;
    private LinearLayout lyMoshtarian;
    private LinearLayout lyAcceptRequests;
    private LinearLayout lyMap;
    private LinearLayout lySarjam;
    private LinearLayout lyHead;
    private LinearLayout lyNotVisited;
    RelativeLayout lyReload;

    private RecyclerView row_khales;

    private RecyclerView row_TasvieNashode;
    private RecyclerView row_GroupProducts;
    private RecyclerView row_TasvieNashodeGroups;
    private RelativeLayout lyPieChart;

    private MainGroupAsync mainGroupAsync = null;

    public static final int MY_PERMISSIONS_REQUEST_READ_GPS = 14;

    private RecyclerView.LayoutManager row_manager;

    private UserInfoAdapter useradapter;
    private UserInfoAdapterTS userInfoAdapterTS;
    private GroupInfoAdapter groupInfoAdapter;
    private GroupTSInfoAdapter groupTSInfoAdapter;

    private ArrayList<UserInfo> userInfos_khales = new ArrayList<>();
    private ArrayList<UserInfo> userInfos_Tasvie = new ArrayList<>();
    private ArrayList<UserInfo> userInfos_SaleProducts = new ArrayList<>();
    private ArrayList<UserInfo> userInfos_TasvieNashodeProducts = new ArrayList<>();

    private ArrayList<ChartInfo> chartInfoArrayList = new ArrayList<>();
    private GetPieChartInfos getPieChartInfos = null;

    private RelativeLayout lyProgress;

    private TextView txtTotal;
    private TextView txtTaeedKol;
    private TextView txtEntezar;

    private String total = "0";
    private String accepted = "0";
    private String notAccepted = "0";

    //khalesForoosh
    private Double netSaleDay = null;
    private Double netSaleMonth = null;
    private TextView txtNetSaleDay;
    private TextView txtNetSaleMonth;

    //PieChart
    private PieChart mChart;
    private RelativeLayout lyProgressPieChart;
    private RelativeLayout lyContentPieChart;
    private boolean isPieChartInited = false;
    private boolean fakePie = false;


    //LineChart
    private RelativeLayout lyLineChart;
    private LineChart cubeChart;
    private ArrayList<CubicChartInfo> cubicChartInfoArrayList = new ArrayList<>();
    private GetCubicChartInfos getCubicChartInfos = null;
    int[] mColors = new int[]{
            ColorTemplate.COLORFUL_COLORS[0],
            ColorTemplate.PASTEL_COLORS[1],
            ColorTemplate.JOYFUL_COLORS[2]
    };

    //CoveragePercent
    private ReportServerRepo reportServerRepo = null;
    private ReportAnswer reportAnswer = null;
    private GetCoverageReportAsync getCoverageReportAsync = null;
    private TextView txtDayCustomers;
    private TextView txtDayProceed;
    private TextView txtNotProceed;
    private TextView txtNotProceedPercen;
    private TextView txtDayProceedPercent;
    private RelativeLayout lyReloadCoverage;
    private LinearLayout lyCoverageProgress;

    //CustomerEditedRequest
    private CustomersRequestEditCountAsynk customersRequestEditCountAsynk;
    private ICustomersEdited customersEditedServerRepo;
    private TextView txtEditedCustomersCount;
    private ImageView btnEditedCustomerList;
    private ImageView btnRefreshEditedcustomerCount;
    private RelativeLayout lyProgressEditedCustomerCunt;


    //bARAYE GHEYRE FAAL KARDANE THREADE ERORHANDELLING
    private int isAFinalRelease = 0;

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferenceHelper = new PreferenceHelper(this);

        if (isAFinalRelease == 1) {
            Thread t = new Thread(new adminThread());
            t.setDefaultUncaughtExceptionHandler(new Thread.
                    UncaughtExceptionHandler() {
                public void uncaughtException(Thread t, Throwable e) {
                    System.out.println(t + " throws exception: " + e);

                    Intent intent = new Intent(ActivityMain.this, ActivityErorHandelling.class);
                    intent.putExtra("Eror", e.toString() + " " + e.getMessage() + t.toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    System.exit(0);
                    t.start();
                }
            });

        }

        btnEditedCustomerList = (ImageView) findViewById(R.id.btnEditedCustomerList);
        inflater = (LayoutInflater) getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
        txtEditedCustomersCount = (TextView) findViewById(R.id.txtEditedCustomersCount);
        lyForoosh = (LinearLayout) findViewById(R.id.lyForoosh);
        lyAnbar = (LinearLayout) findViewById(R.id.lyAnbar);
        lyMoshtarian = (LinearLayout) findViewById(R.id.lyHadaf);
        lyAcceptRequests = (LinearLayout) findViewById(R.id.lyAcceptRequests);
        lySarjam = (LinearLayout) findViewById(R.id.lySarjam);
        lyMap = (LinearLayout) findViewById(R.id.lyMap);
        lyHead = (LinearLayout) findViewById(R.id.lyHead);
        lyNotVisited = (LinearLayout) findViewById(R.id.lyNotVisited);
        lyProgress = (RelativeLayout) findViewById(R.id.lyprogress2);
        lyProgress.setVisibility(View.VISIBLE);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtTaeedKol = (TextView) findViewById(R.id.txtTaeedKol);
        txtEntezar = (TextView) findViewById(R.id.txtEntezar);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        lyReload = (RelativeLayout) findViewById(R.id.lyReload);

        btnRefreshEditedcustomerCount = (ImageView) findViewById(R.id.btnRefreshEditedcustomerCount);
        lyProgressEditedCustomerCunt = (RelativeLayout) findViewById(R.id.lyProgressEditedCustomerCunt);

        requestPermission();
        initVersionName();
        runGetPiechartAsync();
        runGetCubicChartInfos();
        initCoveragePercentReport();
        initKhalesForosh();
        initCustomersRequestEditerdReport();
        MyLocationServices.startActionGetLocation(getApplicationContext());

        lyReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runReportAsync();
            }
        });

        preferenceHelper = new PreferenceHelper(this);

        lyNotVisited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityMain.this, ActivityAdamVisit.class);
                startActivity(intent);
            }
        });

        lyMoshtarian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityMain.this, ActivityMoshtarian.class);
                startActivity(intent);
            }
        });

        lySarjam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityMain.this, ActivitySarjam.class);
                startActivity(intent);
            }
        });

        lyMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityMain.this, ActivityVisitorsInMap.class);
                startActivity(intent);
            }
        });


        lyAnbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityMain.this, ActivityAnbar.class);
                startActivity(intent);

            }
        });


        lyForoosh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityMain.this, ActivityForoosh.class);
                startActivity(intent);

            }
        });

        lyAcceptRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityMain.this, ActivityRequestsHeader.class);
                intent.putExtra("Amaliat", "");
                startActivity(intent);

            }
        });


        scrollView = (ScrollView) findViewById(R.id.scrollView);

        lyLineChart = (RelativeLayout) findViewById(R.id.lineChart);

        initAdvertiseBox();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                startActivity(getIntent());
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        TextView txtUserName = (TextView) hView.findViewById(R.id.txtUerN);
        txtUserName.setText("" + preferenceHelper.getString(PreferenceHelper.USER_NAME));

        FontApplier.applyMainFont(getApplicationContext(), lyHead);

        runReportAsync();

//____________________________________EXECUTE ASYNC TASK____________________________________________
        new SaleAnalyzeAsynk().execute();

        new GroupsAnalyzeAsynk().execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_bar) {
            return true;
        }

        if (id == R.id.action_settings) {
            ActivityManagmentFooter.start(ActivityMain.this);
            return true;
        }
//        if (id == R.id.action_settings2) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_changepass) {

            Intent intent = new Intent(ActivityMain.this, ActivityChangePass.class);
            startActivityForResult(intent, 1403);
            finish();

        } else if (id == R.id.nav_home) {

            String url = "http://razanpardazesh.com";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        } else if (id == R.id.nav_refresh) {
            finish();
            startActivity(getIntent());

        } else if (id == R.id.nav_managment) {

            ActivityManagmentFooter.start(ActivityMain.this);
        } else if (id == R.id.nav_customers_edit) {
            EditedCustomerListActivity.start(ActivityMain.this);

        } else if (id == R.id.nav_daily_path) {

            RegionActivity.start(ActivityMain.this);

        } else if (id == R.id.nav_exit) {

            new AlertDialog.Builder(this)
                    .setTitle("خروج")
                    .setMessage("مایل به بستن برنامه میباشید؟")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {

                            finish();
                        }
                    }).create().show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("خروج")
                .setMessage("مایل به بستن برنامه میباشید؟")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {

                        finish();
                    }
                }).create().show();
    }


    public class SaleAnalyzeAsynk extends AsyncTask<Void, UserInfo, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPostExecute(Void aVoid) {

            row_khales = (RecyclerView) findViewById(R.id.row_khales);
            row_khales.setFocusable(false);
            row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            row_khales.setLayoutManager(row_manager);
            useradapter = new UserInfoAdapter(userInfos_khales);
            useradapter.setActivity(ActivityMain.this);
            row_khales.setAdapter(useradapter);

            row_TasvieNashode = (RecyclerView) findViewById(R.id.row_TasvieNashode);
            row_TasvieNashode.setFocusable(false);
            row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            row_TasvieNashode.setLayoutManager(row_manager);
            userInfoAdapterTS = new UserInfoAdapterTS(userInfos_Tasvie);
            userInfoAdapterTS.setActivity(ActivityMain.this);
            row_TasvieNashode.setAdapter(userInfoAdapterTS);

            if (dialog != null) {
                dialog.dismiss();
            }
        }


        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ActivityMain.this, "", "دریافت اطلاعات");
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            HashMap<String, Object> datas = new HashMap<String, Object>();
            String tokenid = preferenceHelper.getString(preferenceHelper.TOKEN_ID);

            Log.e("ffffi", "token id is " + tokenid);

            datas.put("TokenID", tokenid);

            try {
                Log.e("jkkjjj", "url is " + "http://" + preferenceHelper.getString(NetworkTools.URL));
                SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_AnalyseOfForoosh", datas).getProperty(0);

                for (int i = 0; i < request2.getPropertyCount(); i++) {
                    SoapObject sp = (SoapObject) request2.getProperty(i);

                    UserInfo userInfo = new UserInfo();
                    userInfo.setCode_user(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                    userInfo.setName_user(NetworkTools.getSoapPropertyAsNullableString(sp, 1));

                    userInfo.setExecute_user(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 4).toString())));
                    userInfo.setImage_user(R.drawable.user);
                    userInfos_khales.add(userInfo);

                }


                for (int i = 0; i < request2.getPropertyCount(); i++) {
                    SoapObject sp = (SoapObject) request2.getProperty(i);


                    UserInfo userInfo = new UserInfo();
                    userInfo.setCode_user(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                    userInfo.setName_user(NetworkTools.getSoapPropertyAsNullableString(sp, 1));

                    userInfo.setExecute_user(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 5).toString())));
                    userInfo.setImage_user(R.drawable.user_red);
                    userInfos_Tasvie.add(userInfo);


                }


            } catch (Exception e) {

                e.printStackTrace();
            }
            return null;
        }
    }


    public class GroupsAnalyzeAsynk extends AsyncTask<Void, UserInfo, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {

            row_GroupProducts = (RecyclerView) findViewById(R.id.row_saleProducts);
            row_GroupProducts.setFocusable(false);
            row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            row_GroupProducts.setLayoutManager(row_manager);
            groupInfoAdapter = new GroupInfoAdapter(userInfos_SaleProducts);
            groupInfoAdapter.setActivity(ActivityMain.this);
            row_GroupProducts.setAdapter(groupInfoAdapter);


            row_TasvieNashodeGroups = (RecyclerView) findViewById(R.id.List_Product_TasvieNashode);
            row_TasvieNashodeGroups.setFocusable(false);
            row_manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            row_TasvieNashodeGroups.setLayoutManager(row_manager);
            groupTSInfoAdapter = new GroupTSInfoAdapter(userInfos_TasvieNashodeProducts);
            groupTSInfoAdapter.setActivity(ActivityMain.this);
            row_TasvieNashodeGroups.setAdapter(groupTSInfoAdapter);


        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HashMap<String, Object> datas = new HashMap<String, Object>();
            String tokenid = preferenceHelper.getString(preferenceHelper.TOKEN_ID);


            datas.put("TokenID", tokenid);

            try {

                SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_AnalyseOfProducts", datas).getProperty(0);

                for (int i = 0; i < request2.getPropertyCount(); i++) {
                    SoapObject sp = (SoapObject) request2.getProperty(i);

                    UserInfo userInfo = new UserInfo();
                    userInfo.setCode_user("گروه");
                    userInfo.setName_user(NetworkTools.getSoapPropertyAsNullableString(sp, 0));

                    userInfo.setExecute_user(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 3).toString())));
                    userInfo.setImage_user(R.drawable.group_blue);
                    userInfos_SaleProducts.add(userInfo);

                }

                for (int i = 0; i < request2.getPropertyCount(); i++) {
                    SoapObject sp = (SoapObject) request2.getProperty(i);


                    UserInfo userInfo = new UserInfo();
                    userInfo.setCode_user("گروه");
                    userInfo.setName_user(NetworkTools.getSoapPropertyAsNullableString(sp, 0));

                    userInfo.setExecute_user(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 4).toString())));
                    userInfo.setImage_user(R.drawable.group_red);
                    userInfos_TasvieNashodeProducts.add(userInfo);

                }

            } catch (Exception e) {
                Log.e("iiiiiii", "connot read Soap");
                e.printStackTrace();
            }
            return null;
        }

    }


    private void initAdvertiseBox() {
        View advertiseHeaderBox = lyLineChart;

        int width = getResources().getDisplayMetrics().widthPixels;

        int height = (int) (width / 2);

        ViewGroup.LayoutParams params = advertiseHeaderBox.getLayoutParams();
        params.height = height;
        advertiseHeaderBox.setLayoutParams(params);

//        final Indicator indicator = (Indicator) findViewById(R.id.indicatorBox);
//        indicator.setViewPager(pager);

    }


    public int requestPermission() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(ActivityMain.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityMain.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(ActivityMain.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_GPS);

                Log.e("Permission", "Persmisn is " + MY_PERMISSIONS_REQUEST_READ_GPS);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        return MY_PERMISSIONS_REQUEST_READ_GPS;
    }


    public void initVersionName() {
        TextView txtParseh = (TextView) findViewById(R.id.txtCopyRight);
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

    public class adminThread implements Runnable {

        public void run() {
            throw new RuntimeException();
        }
    }


    public void runReportAsync() {
        if (mainGroupAsync == null) {
            lyProgress.setVisibility(View.VISIBLE);
            mainGroupAsync = new MainGroupAsync();
            mainGroupAsync.execute();
        } else {
            return;
        }
    }

    public class MainGroupAsync extends AsyncTask<Void, String, String> {

        Boolean isonline = NetworkTools.isOnline(ActivityMain.this);

        @Override
        protected String doInBackground(Void... voids) {

            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));

            if (isonline) {
                try {
                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_MainGroup_Report", datas).getProperty(0);

                    if (request2.getPropertyCount() <= 0) {
                        return "Null";
                    }
                    SoapObject rq = (SoapObject) request2.getProperty(0);
                    if (request2 != null) {
                        total = (NetworkTools.getSoapPropertyAsNullableString(rq, 0));
                        accepted = (NetworkTools.getSoapPropertyAsNullableString(rq, 1));
                        notAccepted = (NetworkTools.getSoapPropertyAsNullableString(rq, 2));
                    }

                } catch (Exception e) {

                    Log.e("iiiiiii", "connot read Soap");
                    e.printStackTrace();
                    return "Eror";
                }
                return "Online";
            }
            return "NotOnline";

        }

        @Override
        protected void onPostExecute(String s) {

            if (s.equals("Online")) {

                txtTotal.setText(total.toString());
                txtTaeedKol.setText(accepted);
                txtEntezar.setText(notAccepted);
                mainGroupAsync = null;
                lyProgress.setVisibility(View.GONE);
            }

            super.onPostExecute(s);
        }

    }

    private void runGetPiechartAsync() {


        lyProgressPieChart = (RelativeLayout) findViewById(R.id.lyProgressPieChart);
        lyContentPieChart = (RelativeLayout) findViewById(R.id.lyContentPieChart);
        lyContentPieChart.setVisibility(View.GONE);
        lyProgressPieChart.setVisibility(View.VISIBLE);


        if (getPieChartInfos != null) return;

        getPieChartInfos = new GetPieChartInfos();
        getPieChartInfos.execute();

    }


    private void initPieChart() {

        isPieChartInited = true;

        lyContentPieChart.setVisibility(View.VISIBLE);
        lyProgressPieChart.setVisibility(View.GONE);

        mChart = (PieChart) findViewById(R.id.chart1);
        mChart.setDrawingCacheBackgroundColor(Color.parseColor("#ffffff"));
        mChart.setBackgroundColor(Color.parseColor("#ffffff"));
        mChart.setTransparentCircleColor(Color.parseColor("#ffffff"));
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        // mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.TRANSPARENT);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(100);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);
        mChart.setCenterText("فروش گروه های اصلی");
        mChart.setCenterTextSize(16f);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
//        mChart.setOnChartValueSelectedListener(this);

        int count = chartInfoArrayList.size();
        setData(count, 0);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        //mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();

        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setTextSize(12);

        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
//        mChart.setEntryLabelTypeface(mTfRegular);
        mChart.setEntryLabelTextSize(14f);

        mChart.setDrawEntryLabels(false);
    }

    private void setData(int count, float range) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count; i++) {
            entries.add(new PieEntry((float) chartInfoArrayList.get(i).getSharePercent(), chartInfoArrayList.get(i).getGroupName()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

//        dataSet.set

        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();


        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        // colors.add(Color.rgb(51, 181, 229));


        dataSet.setColors(colors);


//        dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);

        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);

        data.setValueTextColor(Color.WHITE);
        //data.setValueTypeface(50);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    //Async For Get Percent of Pie Chart from Server

    private class GetPieChartInfos extends AsyncTask<Void, String, String> {

        private boolean fake = false;
        public ChartInfo chartInfo;

        Boolean isonline = NetworkTools.isOnline(ActivityMain.this);

        @Override
        protected String doInBackground(Void... params) {

            getPieChartInfos = null;

            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));

            try {
                if (isonline) {
                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_PieChart_Android", datas).getProperty(0);

                    if (request2.getPropertyCount() <= 0) {
                        return "Null";
                    }

                    for (int i = 0; i < request2.getPropertyCount(); i++) {
                        SoapObject sp = (SoapObject) request2.getProperty(i);

                        chartInfo = new ChartInfo();

                        chartInfo.setSarparast_id(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                        chartInfo.setGroupName(NetworkTools.getSoapPropertyAsNullableString(sp, 1));
                        chartInfo.setKhalesR(Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 2).toString()));
                        chartInfo.setGroupID(NetworkTools.getSoapPropertyAsNullableString(sp, 3));
                        chartInfo.setSharePercent(Double.parseDouble(NetworkTools.getSoapPropertyAsNullableString(sp, 4).toString()));

                        chartInfoArrayList.add(chartInfo);
                    }

                    return "OK";

                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Eror";
            }
            return "ofline";
        }

        @Override
        protected void onPostExecute(String s) {

            getPieChartInfos = null;


//            lyProgress.setVisibility(View.GONE);
//            lyContent.setVisibility(View.VISIBLE);

            if (s.equals("OK")) {


                scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        Log.e("scrollview", "" + scrollView.getScrollY());

                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lyHead);
                        if (linearLayout.getMeasuredHeight() <= scrollView.getScrollY() +
                                scrollView.getHeight()) {
                            //do something
                            if (isPieChartInited == false) {
                                initPieChart();

                            }
                        } else {
                            //do nothing
                        }

                    }
                });

                super.onPostExecute(s);
            }
        }

    }


    private void initCubeChart() {


        cubeChart = (LineChart) findViewById(R.id.cubeChart);
        cubeChart.setViewPortOffsets(0, 0, 0, 0);
//        cubeChart.setBackgroundColor(Color.rgb(104, 241, 175));
        cubeChart.setBackgroundColor(Color.parseColor("#ffffff"));

        // no description text
        cubeChart.getDescription().setEnabled(false);

        // enable touch gestures
        cubeChart.setTouchEnabled(true);
        cubeChart.setNoDataTextColor(Color.parseColor("#000000"));
        cubeChart.setGridBackgroundColor(Color.parseColor("#000000"));
        cubeChart.setBorderColor(Color.parseColor("#000000"));

        // enable scaling and dragging
        cubeChart.setDragEnabled(true);
        cubeChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        cubeChart.setPinchZoom(false);

        cubeChart.setDrawGridBackground(false);
        cubeChart.setMaxHighlightDistance(300);

        cubeChart.setDragEnabled(true);

        XAxis x = cubeChart.getXAxis();
        x.setEnabled(true);
        x.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        x.setTextSize(10f);
        x.setTextColor(Color.BLACK);
        x.setGridColor(Color.parseColor("#CFD8DC"));
        x.setDrawAxisLine(true);
        x.setDrawGridLines(true);
        x.setDrawLabels(true);
        // x.setLabelCount(10);
        x.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int temp = (int) value;
                float remind = (temp == 0) ? 0 : value % temp;

                if (remind != 0f)
                    return "";

                switch (temp) {
                    case 0:
                        return cubicChartInfoArrayList.get(0).getDate();
                    case 1:
                        return cubicChartInfoArrayList.get(1).getDate();
                    case 2:
                        return cubicChartInfoArrayList.get(2).getDate();
                    case 3:
                        return cubicChartInfoArrayList.get(3).getDate();
                    case 4:
                        return cubicChartInfoArrayList.get(4).getDate();
                    case 5:
                        return cubicChartInfoArrayList.get(5).getDate();
                    case 6:
                        return cubicChartInfoArrayList.get(6).getDate();
                    case 7:
                        return cubicChartInfoArrayList.get(7).getDate();
                    case 8:
                        return cubicChartInfoArrayList.get(8).getDate();
                    case 9:
                        return cubicChartInfoArrayList.get(9).getDate();
                }
                return "";
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        YAxis y = cubeChart.getAxisLeft();
        //float f = cubicChartInfoArrayList.size();
        // cubeChart.setVisibleXRange(0,f);

        y.setLabelCount(6, false);
        y.setEnabled(true);
        y.setGridColor(Color.parseColor("#CFD8DC"));
        y.setTextColor(Color.BLACK);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(true);
        y.setTextSize(12);
        y.setAxisLineColor(Color.WHITE);

        cubeChart.getAxisRight().setEnabled(false);
        // add data
        setCubicChartData();


        Legend l = cubeChart.getLegend();

        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setTextSize(14);
        l.setXEntrySpace(25f);
        l.setYEntrySpace(10f);
        l.setYOffset(1f);
        l.setXOffset(40f);


        cubeChart.getLegend().setEnabled(true);

        cubeChart.animateXY(2000, 2000);

        // dont forget to refresh the drawing
        cubeChart.invalidate();
    }


    private void setCubicChartData() {


//        ArrayList<Entry> lineSalesValues = new ArrayList<>();
        ArrayList<Entry> lineReturnsValues = new ArrayList<>();
        ArrayList<Entry> lineKhalesRsValues = new ArrayList<>();

        for (int i = 0; i < cubicChartInfoArrayList.size(); i++) {
//            lineSalesValues.add(new Entry(i, cubicChartInfoArrayList.get(i).getSales()));
            lineReturnsValues.add(new Entry(i, cubicChartInfoArrayList.get(i).getReturned()));
            lineKhalesRsValues.add(new Entry(i, cubicChartInfoArrayList.get(i).getKhalesR()));
        }

//        LineDataSet lineSalesDataSet = new LineDataSet(lineSalesValues, " فروش ");
        LineDataSet lineReturnsDataSet = new LineDataSet(lineReturnsValues, " بازگشتی");
        LineDataSet lineKhalesRsDataSet = new LineDataSet(lineKhalesRsValues, " خالص فروش ");

//        setDataSetSeting(lineSalesDataSet,Color.parseColor("#00E5FF"));
        setDataSetSeting(lineReturnsDataSet, Color.parseColor("#FF3D00"), false);
        setDataSetSeting(lineKhalesRsDataSet, Color.parseColor("#FFFF00"), true);


//        LineData data = new LineData(lineSalesDataSet);
        LineData data = new LineData(lineKhalesRsDataSet);

        data.addDataSet(lineReturnsDataSet);
//        data.addDataSet(lineKhalesRsDataSet);
        cubeChart.setData(data);
        cubeChart.invalidate();
    }

    private void setDataSetSeting(LineDataSet dataSet, int color, Boolean fillable) {


        dataSet.setMode(LineDataSet.Mode.LINEAR);
        dataSet.setDrawCircles(true);
        dataSet.setDrawFilled(fillable);
        dataSet.setDrawValues(false);
        dataSet.setCubicIntensity(0f);
        dataSet.setCircleRadius(4f);
        dataSet.setHighLightColor(Color.rgb(244, 117, 117));
        dataSet.setCircleColor(color);
        dataSet.disableDashedLine();
        dataSet.setCircleHoleRadius(3f);
        dataSet.setColor(color);
        dataSet.setLineWidth(1f);
        dataSet.setDrawCircleHole(true);
        dataSet.setFillColor(color);
        dataSet.setFillAlpha(95);
//        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setDrawHorizontalHighlightIndicator(true);
    }

    private void runGetCubicChartInfos() {
        if (getCubicChartInfos != null) return;

        getCubicChartInfos = new GetCubicChartInfos();
        getCubicChartInfos.execute();

    }

    private class GetCubicChartInfos extends AsyncTask<Void, String, String> {

        private boolean fake = false;
        public CubicChartInfo cubicChartInfo;

        Boolean isonline = NetworkTools.isOnline(ActivityMain.this);

        @Override
        protected String doInBackground(Void... params) {

            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));

            try {
                if (isonline) {
                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_CubicChart_Android", datas).getProperty(0);

                    if (request2.getPropertyCount() <= 0) {
                        return "Null";
                    }

                    for (int i = request2.getPropertyCount() - 1; i >= 0; i--) {
                        SoapObject sp = (SoapObject) request2.getProperty(i);

                        cubicChartInfo = new CubicChartInfo();

                        cubicChartInfo.setDate(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                        cubicChartInfo.setSales(Long.valueOf(NetworkTools.getSoapPropertyAsNullableString(sp, 1).toString()));
                        cubicChartInfo.setReturned(Long.valueOf(NetworkTools.getSoapPropertyAsNullableString(sp, 2).toString()));
                        cubicChartInfo.setKhalesR(Long.valueOf(NetworkTools.getSoapPropertyAsNullableString(sp, 3).toString()));

                        cubicChartInfoArrayList.add(cubicChartInfo);
                    }

                    return "OK";

                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Eror";
            }
            return "ofline";
        }

        @Override
        protected void onPostExecute(String s) {

            getPieChartInfos = null;

            if (s.equals("OK")) {

                initCubeChart();
            }

            super.onPostExecute(s);
        }
    }

    private void initCoveragePercentReport() {
        txtDayCustomers = (TextView) findViewById(R.id.txtDayCustomers);
        txtDayProceed = (TextView) findViewById(R.id.txtDayProceed);
        txtNotProceed = (TextView) findViewById(R.id.txtNotProceed);
        txtNotProceedPercen = (TextView) findViewById(R.id.txtNotProceedPercen);
        txtDayProceedPercent = (TextView) findViewById(R.id.txtDayProceedPercent);
        lyReloadCoverage = (RelativeLayout) findViewById(R.id.lyReloadCoverage);
        lyCoverageProgress = (LinearLayout) findViewById(R.id.lyCoverageProgress);
        lyReloadCoverage.setVisibility(View.VISIBLE);
        ImageView imgInfo = (ImageView) findViewById(R.id.imgInfo);
        ImageView lyCoveragePercent = (ImageView) findViewById(R.id.lyCoveragePercent);
        imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ActivityMain.this)
                        .setTitle("آنالیز پوشش")
                        .setMessage(R.string.coverage_proceed_info)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        }).create().show();
            }
        });
        lyCoveragePercent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoveragePercentageActivity.start(ActivityMain.this);
            }
        });

        reportServerRepo = new ReportServerRepo();
        reportAnswer = new ReportAnswer();

        runGetCoverageReportAsync();

        lyReloadCoverage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runGetCoverageReportAsync();
            }
        });
    }

    private void runGetCoverageReportAsync() {

        if (getCoverageReportAsync != null)
            return;
        getCoverageReportAsync = new GetCoverageReportAsync();
        getCoverageReportAsync.execute();
    }

    public class GetCoverageReportAsync extends AsyncTask<Void, String, String> {

        private String message;

        @Override
        protected void onPreExecute() {
            lyCoverageProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {

            reportAnswer = reportServerRepo.mainCoveragePercent(getApplicationContext(), "", 0, 0);

            if (reportAnswer.getIsSuccess() == 0) {

                return "0";
            }
            return "1";
        }

        @Override
        protected void onPostExecute(String value) {
            getCoverageReportAsync = null;


            if (value.equals("1")) {

                Long dayCovered = reportAnswer.getReport().getTotalCustomers() - reportAnswer.getReport().getNotVisited();
                txtDayCustomers.setText(reportAnswer.getReport().getTotalCustomers().toString());
                txtNotProceed.setText(reportAnswer.getReport().getNotVisited().toString());
                txtNotProceedPercen.setText(coverageProceed(reportAnswer.getReport().getTotalCustomers(), dayCovered) + " % ");
                txtDayProceed.setText(dayCovered.toString());
                txtDayProceedPercent.setText(coverageProceed(reportAnswer.getReport().getTotalCustomers(), reportAnswer.getReport().getNotVisited()) + " % ");
                lyCoverageProgress.setVisibility(View.GONE);

            }
            if (value.equals("0")) {
                txtDayCustomers.setText("خطا");
                txtNotProceed.setText("خطا");
                txtDayProceed.setText("خطا");
            }
        }

        private String coverageProceed(Long total, Long visited) {

            if (total == 0) return "0";
            float val = ((((float) (total - visited)) / total) * 100);
            if (String.valueOf(val).length() > 5) {
                return String.valueOf(val).substring(0, 5);
            }
            return String.valueOf(val);

        }

    }

    private void initKhalesForosh() {

        txtNetSaleDay = (TextView) findViewById(R.id.txtNetSaleDay);
        txtNetSaleMonth = (TextView) findViewById(R.id.txtNetSaleMonth);

    }


    private void startEditedRequestCustomerCountAsync() {

        if (customersRequestEditCountAsynk != null) return;

        customersRequestEditCountAsynk = new CustomersRequestEditCountAsynk();
        customersRequestEditCountAsynk.execute();
    }


    public class CustomersRequestEditCountAsynk extends AsyncTask<Void, String, String> {

        private CustomerEditAnswer answer;
        String message;

        @Override
        protected void onPreExecute() {
            lyProgressEditedCustomerCunt.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            customersRequestEditCountAsynk = null;

            answer = customersEditedServerRepo.getEditedListCount(getApplicationContext());

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
                txtEditedCustomersCount.setText(message);
            }

            if (s.equals("1")) {

                int count = (int) answer.getCustomerRequestEdit().getCount();

                if (count != 0)
                    txtEditedCustomersCount.setText(count + "");
                lyProgressEditedCustomerCunt.setVisibility(View.GONE);
            }
            if (s.equals("0")) {

            }
        }

    }

    private void initCustomersRequestEditerdReport() {

        if (customersEditedServerRepo == null)
            customersEditedServerRepo = new CustomersEditedServerRepo();


        startEditedRequestCustomerCountAsync();

        btnEditedCustomerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditedCustomerListActivity.start(ActivityMain.this);
            }
        });

        btnRefreshEditedcustomerCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startEditedRequestCustomerCountAsync();
            }
        });
    }
}


