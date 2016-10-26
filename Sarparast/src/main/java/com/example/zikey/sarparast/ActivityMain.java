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
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zikey.sarparast.Helpers.FontApplier;
import com.example.zikey.sarparast.Helpers.Indicator;
import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.ksoap2.serialization.SoapObject;

import java.lang.reflect.Array;
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
    private LinearLayout lyHadaf;
    private LinearLayout lyAcceptRequests;
    private LinearLayout lyMap;
    private LinearLayout lySarjam;
    private LinearLayout lyHead;
    private LinearLayout lyNotVisited;

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

    private ArrayList<UserInfo> userInfos_khales = new ArrayList<UserInfo>();
    private ArrayList<UserInfo> userInfos_Tasvie = new ArrayList<UserInfo>();
    private ArrayList<UserInfo> userInfos_SaleProducts = new ArrayList<UserInfo>();
    private ArrayList<UserInfo> userInfos_TasvieNashodeProducts = new ArrayList<UserInfo>();

    private ArrayList<ChartInfo> chartInfoArrayList = new ArrayList<>();
    private GetPieChartInfos getPieChartInfos = null;

    private RelativeLayout lyProgress;

    private TextView txtTotal;
    private TextView txtTaeedKol;
    private TextView txtEntezar;

    private String total = "0";
    private String accepted = "0";
    private String notAccepted = "0";

    //PieChart
    private PieChart mChart;
    private RelativeLayout lyProgressPieChart;
    private RelativeLayout lyContentPieChart;
    private boolean isPieChartInited = false;
    private boolean fakePie = false;


    //LineChart
    private RelativeLayout lyLineChart;
    private LineChart cubeChart;

    //bARAYE GHEYRE FAAL KARDANE THREADE ERORHANDELLING
    private int isAFinalRelease = -1;

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferenceHelper = new PreferenceHelper(this);


        isAFinalRelease = 1;

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
            // this will call run() function
        }

//        alarmManager.setInexactRepeating(AlarmManager.RTC,9000,9000,);

        requestPermission();
        initVersionName();
        runGetPiechartAsync();
        initCubeChart();


//        int permiss = ContextCompat.checkSelfPermission(ActivityMain.this,
//                android.Manifest.permission.ACCESS_FINE_LOCATION);

        MyLocationServices.startActionGetLocation(getApplicationContext());


        inflater = (LayoutInflater) getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);

//        Thread.setDefaultUncaughtExceptionHandler();
//;
        lyForoosh = (LinearLayout) findViewById(R.id.lyForoosh);
        lyAnbar = (LinearLayout) findViewById(R.id.lyAnbar);
        lyHadaf = (LinearLayout) findViewById(R.id.lyHadaf);
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

//        lyPieChart = (RelativeLayout) findViewById(R.id.lyPieChart);


        DisplayMetrics metrics = getResources().getDisplayMetrics();



        preferenceHelper = new PreferenceHelper(this);

//txtUserName.setText(""+preferenceHelper.getString(PreferenceHelper.USER_NAME));

        lyNotVisited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityMain.this, ActivityAdamVisit.class);
                startActivity(intent);
            }
        });

        lyHadaf.setOnClickListener(new View.OnClickListener() {
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


//        ArrayList<Integer> imageIDS = new ArrayList<>();
//        for (int i = 1; i < 4; i++) {
//            int imageID = getApplicationContext().getResources().getIdentifier("picture_0" + i, "drawable", getApplicationContext().getPackageName());
//            imageIDS.add(imageID);
//        }


        lyLineChart = (RelativeLayout) findViewById(R.id.lineChart);

//        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(imageIDS);
//        myPagerAdapter.setInflater(inflater);
//        pager.setAdapter(myPagerAdapter);
//        Indicator indicator = new Indicator(getApplicationContext());
//        indicator.setViewPager(pager);

        initAdvertiseBox();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "پیام جدید وجود ندارد", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
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


//        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
//        navigationView.addHeaderView(header);
//        TextView text = (TextView) header.findViewById(R.id.txtuserName);
//        text.setText("HELLO");


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

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow)
//
// {
//
//            if (id == R.id.nav_manage) {
//
//        }
//            else if (id == R.id.nav_share) {
//
//        }
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
        } else if (id == R.id.nav_exit) {
            new AlertDialog.Builder(this)
                    .setTitle("خروج")
                    .setMessage("مایل به بستن برنامه میباشید؟")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            preferenceHelper.putString(PreferenceHelper.TOKEN_ID, "");
                            Log.e("LOG_onDestroyMain", "token Cleaned");
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
                        preferenceHelper.putString(PreferenceHelper.TOKEN_ID, "");
                        Log.e("LOG_onDestroyMain", "token Cleaned");
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
//
//
                for (int i = 0; i < request2.getPropertyCount(); i++) {
                    SoapObject sp = (SoapObject) request2.getProperty(i);


                    UserInfo userInfo = new UserInfo();
                    userInfo.setCode_user(NetworkTools.getSoapPropertyAsNullableString(sp, 0));
                    userInfo.setName_user(NetworkTools.getSoapPropertyAsNullableString(sp, 1));

                    userInfo.setExecute_user(String.format("%,d", Long.parseLong(NetworkTools.getSoapPropertyAsNullableString(sp, 4).toString())));
                    userInfo.setImage_user(R.drawable.user);
                    userInfos_khales.add(userInfo);

//
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

        int height = (int) (width / 1.5);

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

        XAxis x = cubeChart.getXAxis();
        x.setEnabled(false);

        YAxis y = cubeChart.getAxisLeft();


        y.setLabelCount(6, false);
        y.setTextColor(Color.BLACK);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(false);
        y.setTextSize(12);
        y.setAxisLineColor(Color.WHITE);

        cubeChart.getAxisRight().setEnabled(false);

        // add data
        setCubicChartData(45, 100);

        cubeChart.getLegend().setEnabled(false);

        cubeChart.animateXY(2000, 2000);

        // dont forget to refresh the drawing
        cubeChart.invalidate();
    }


    private void setCubicChartData(int count, float range) {
        int[] mColors = new int[]{
                ColorTemplate.COLORFUL_COLORS[0],
                ColorTemplate.PASTEL_COLORS[1],
                ColorTemplate.JOYFUL_COLORS[2]
        };


        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

        for (int z = 0; z < 3; z++) {

            ArrayList<Entry> values = new ArrayList<Entry>();

            for (int i = 0; i < 20; i++) {
                double val = (Math.random() * 20) + 3;
                values.add(new Entry(i, (float) val));
            }




            LineDataSet d = new LineDataSet(values, "DataSet " + (z + 1));

            if (cubeChart.getData() != null &&
                  cubeChart.getData().getDataSetCount() > 0) {
                d = (LineDataSet)cubeChart.getData().getDataSetByIndex(0);
                d.setValues(values);
                cubeChart.getData().notifyDataChanged();
                cubeChart.notifyDataSetChanged();
            }

else {
                d.setLineWidth(2.5f);
                d.setCircleRadius(4f);


                d.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                d.setDrawCircles(true);

                d.setDrawFilled(true);
                d.setFillColor(Color.parseColor("#000000"));

                d.setLineWidth(1.8f);
                d.setCircleRadius(4f);
                d.setCircleColor(Color.WHITE);
                d.setHighLightColor(Color.rgb(244, 117, 117));
                d.setColor(Color.BLACK);
                d.setFillColor(Color.BLACK);
                d.setFillAlpha(1);


                d.disableDashedLine();
                d.setColor(Color.BLUE);
                d.setDrawFilled(true);
                d.setLineWidth(2f);
                d.setDrawCircleHole(true);
                d.setFillColor(Color.BLUE);
                d.setFillAlpha(65);
                d.setDrawHorizontalHighlightIndicator(false);

                d.setFillFormatter(new IFillFormatter() {
                    @Override
                    public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                        return -10;
                    }
                });

                int color = mColors[z % mColors.length];
                d.setColor(color);
                d.setCircleColor(color);

                d.disableDashedLine();
                d.setColor(color);
                d.setDrawFilled(true);
                d.setLineWidth(2f);
                d.setDrawCircleHole(true);
                d.setFillColor(color);
                d.setFillAlpha(65);
                d.setDrawHorizontalHighlightIndicator(false);

                dataSets.add(d);
            }
        }

        // make the first DataSet dashed
        ((LineDataSet) dataSets.get(0)).enableDashedLine(10, 10, 0);
        ((LineDataSet) dataSets.get(0)).setColors(ColorTemplate.VORDIPLOM_COLORS);
        ((LineDataSet) dataSets.get(0)).setCircleColors(ColorTemplate.VORDIPLOM_COLORS);

        LineData data = new LineData(dataSets);
        data.setValueTextSize(9f);
        data.setDrawValues(false);

        data.setHighlightEnabled(false);

        cubeChart.setData(data);
        cubeChart.invalidate();


    }
}


