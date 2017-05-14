package com.razanPardazesh.supervisor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.PreferenceHelper;
import com.example.zikey.sarparast.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.razanPardazesh.supervisor.model.checkCustomer.CheckCustomerItem;
import com.razanPardazesh.supervisor.model.checkCustomer.CheckCustomerReport;
import com.razanPardazesh.supervisor.model.checkCustomer.CustomForm;
import com.razanPardazesh.supervisor.model.checkCustomer.Question;
import com.razanPardazesh.supervisor.model.user.Customer;
import com.razanPardazesh.supervisor.model.wrapper.CustomFormAnswer;
import com.razanPardazesh.supervisor.model.wrapper.ServerAnswer;
import com.razanPardazesh.supervisor.repo.CustomFormServerRepo;
import com.razanPardazesh.supervisor.repo.iRepo.ICustomForm;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SatisfactionFromActivity extends AppCompatActivity {

    private static final String KEY_CODE = "CODE";
    private static final String KEY_NAME = "NAME";
    private static final String KEY_CUSTOMER_LAT = "LATITUDE";
    private static final String KEY_CUSTOMER_LONG = "LONGITUDE";
    public final int PERMISSIONS_REQUEST_READ_GPS = 14;

    private ImageView imgBack;
    private RecyclerView rvItems;
    private Button btnSend;
    private RelativeLayout lyProgress;
    private RatingBar rtbTotalRate;

    private GoogleApiClient mGoogleApiClient;
    private Location userLocation;
    private Location customerLocation;

    private PreferenceHelper preferenceHelper;
    private SatisfactionAdapter adapter;
    private long customerCode;
    private String customerName;
    private double customerLat;
    private double customerLong;

    private CustomFormAnswer answer;
    private ICustomForm formRepo;

    private SatisfactionAsync satisfactionAsync = null;
    private SendFormAsync sendFormAsync = null;
    //filled data with customer answers
    private CheckCustomerReport customerReport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satisfaction_from);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mhztoolbar);
        setSupportActionBar(toolbar);

        requestAccessLocationPermission();
        parseIntent();
        initViews();
        initRepo();
        initRecycleView();
        runAsync();
    }

    public int requestAccessLocationPermission() {

        if (ContextCompat.checkSelfPermission(SatisfactionFromActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(SatisfactionFromActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(SatisfactionFromActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_READ_GPS);
            }
        }
        return PERMISSIONS_REQUEST_READ_GPS;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_GPS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("خطا")
                            .setMessage("مجوز استفاده از موقعیت مکانی داده نشده است.سرویس بدون مجوز قادر به شناسایی موقیت مکانی شما نخواهد بود")
                            .setPositiveButton("بستن", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {

                                    finish();
                                }
                            }).create().show();
                }
                return;
            }
        }
    }

    private void initViews() {

        if (preferenceHelper == null)
            preferenceHelper = new PreferenceHelper(this);

        imgBack = (ImageView) findViewById(R.id.imgBack);
        rvItems = (RecyclerView) findViewById(R.id.rvItems);
        lyProgress = (RelativeLayout) findViewById(R.id.lyProgress);
        rtbTotalRate = (RatingBar) findViewById(R.id.rtbTotalRate);
        btnSend = (Button) findViewById(R.id.btnSend);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null) {
                    new AlertDialog.Builder(SatisfactionFromActivity.this)
                            .setTitle("ارسال")
                            .setMessage("مایل به ارسال اطلاعات به سمت سرور می باشید؟")
                            .setNegativeButton("خیر", null)
                            .setPositiveButton("بلی", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {

                                    checkUserLocation();
                                }
                            }).create().show();


                }
            }
        });
        rtbTotalRate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float touchPositionX = event.getX();
                    float width = v.getWidth();
                    float starsf = (touchPositionX / width) * 5.0f;
                    int stars = (int) starsf + 1;
                    rtbTotalRate.setRating(stars);
                    v.setPressed(false);
                }
                return true;
            }
        });
    }

    private void initRecycleView() {

        if (rvItems == null)
            initViews();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvItems.setLayoutManager(layoutManager);

        if (adapter == null)
            adapter = new SatisfactionAdapter();

        rvItems.setAdapter(adapter);

    }

    private void initRepo() {
        if (formRepo == null)
            formRepo = new CustomFormServerRepo();
    }

    private void parseIntent() {

        Intent data = getIntent();
        if (data == null)
            return;

        if (data.hasExtra(KEY_CODE))
            customerCode = data.getLongExtra(KEY_CODE, 0);


        if (data.hasExtra(KEY_NAME)) {
            customerName = data.getStringExtra(KEY_NAME);
        }

        if (data.hasExtra(KEY_CUSTOMER_LAT)) {
            customerLat = data.getDoubleExtra(KEY_CUSTOMER_LAT, 0);
        }
        if (data.hasExtra(KEY_CUSTOMER_LONG)) {
            customerLong = data.getDoubleExtra(KEY_CUSTOMER_LONG, 0);
        }
        customerLocation = new Location("customer");

        customerLocation.setLatitude(customerLat);
        customerLocation.setLongitude(customerLong);

        if (customerLat == 0 || customerLong == 0)
            customerLocation = null;


    }


    public static void start(FragmentActivity context, long code, String name, double lt, double lng) {

        Intent starter = new Intent(context, SatisfactionFromActivity.class);
        starter.putExtra(KEY_CODE, code);
        starter.putExtra(KEY_NAME, name);
        starter.putExtra(KEY_CUSTOMER_LAT, lt);
        starter.putExtra(KEY_CUSTOMER_LONG, lng);
        context.startActivity(starter);
    }

    private void generateFormDataAnswer(CustomForm customForm) {

        if (customForm == null)
            return;

        List<Question> questions = customForm.getQuestions();

        if (questions == null || questions.size() == 0)
            return;

        customerReport = new CheckCustomerReport();

        customerReport.setLt(String.valueOf(userLocation.getLatitude()));
        customerReport.setLn(String.valueOf(userLocation.getLongitude()));

        Customer customer = new Customer();
        customer.setCodeMarkaz(customerCode);
        customer.setName(customerName);

        customerReport.setCustomer(customer);


        customerReport.setCustomer_satisfaction_rate((int) rtbTotalRate.getRating());

        ArrayList<CheckCustomerItem> checkCustomerItems = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {

            CheckCustomerItem item = new CheckCustomerItem();
            item.setRate(questions.get(i).getRate());
            item.setQuestion_answer(questions.get(i).getAnswer());
            item.setId_header(questions.get(i).getId());
            item.setId(questions.get(i).getId());
            item.setQuestion_id(questions.get(i).getId());
            item.setQuestion_title(questions.get(i).getTitle());

            checkCustomerItems.add(item);

        }

        customerReport.setItems(checkCustomerItems);
        JSONObject object = customerReport.writeJson(getApplicationContext());

        runSendAsync(object);
    }

    private void checkUserLocation() {


        GoogleApiClient.ConnectionCallbacks connectionCallbacks;
        GoogleApiClient.OnConnectionFailedListener connectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }
        };


        connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                if (ActivityCompat.checkSelfPermission(SatisfactionFromActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SatisfactionFromActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                if (location != null) {
                    userLocation = location;
                    checkDistance(location, customerLocation);
                } else {
                    new android.app.AlertDialog.Builder(SatisfactionFromActivity.this)
                            .setTitle("خطا")
                            .setMessage("موقعیت فعلی شما قابل شناسایی نمیباشد. روشن بودن gps خود را کنترل نمایید.بدون موقعیت مکانی قادر به ارسال اظلاعات نخواهید بود")
                            .setPositiveButton("باشه", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            }).create().show();
                }
            }

            @Override
            public void onConnectionSuspended(int i) {

                int k = 0;
            }
        };

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(connectionFailedListener)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();


    }

    private void checkDistance(Location userLocation, Location customerLocation) {


        //در صورتی که مشتری موقعیت مکانی نداشته باشد نیازی به چک کردن مسافت نیست و سرورست قادر به ارسال خواهد بود
        if (customerLocation == null && userLocation != null) {
            generateFormDataAnswer(answer.getCustomForm());
            return;
        }

        if (userLocation == null)
            return;

        float[] results = new float[1];

        Double customerLat = customerLocation.getLatitude();
        Double customerLong = customerLocation.getLongitude();
        Double userLat = userLocation.getLatitude();
        Double userLong = userLocation.getLongitude();

        Location.distanceBetween(customerLat, customerLong, userLat, userLong, results);

        Double distance = Double.valueOf(Math.round(results[0]));
        if (distance < 999) {
            generateFormDataAnswer(answer.getCustomForm());
            return;
        } else {

            new AlertDialog.Builder(this)
                    .setTitle("خطا")
                    .setMessage("موقیعت مکانی شما خارج از محدوده تعیین شده برای این مشتری میباشد.لطفا در محدوده موقیعت مکانی مشتری اقدام نمایید ")
                    .setPositiveButton("باشه", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    }).create().show();

            return;
        }

    }

    private void runAsync() {
        if (satisfactionAsync != null) return;
        satisfactionAsync = new SatisfactionAsync();
        satisfactionAsync.execute();
    }


    public class SatisfactionAsync extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {

            lyProgress.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            satisfactionAsync = null;

            answer = formRepo.getCustomForm(getApplicationContext());
            if (answer.getIsSuccess() == 0) {
                return "0";

            }
            if (answer.getMessage() != null) {
                return "-1";
            }

            return "1";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("1")) {
                lyProgress.setVisibility(View.GONE);

                if (answer != null) {
                    adapter.setCustomForm(answer.getCustomForm());

                }
            }
            if (s.equals("-1")) {

                lyProgress.setVisibility(View.GONE);

                new AlertDialog.Builder(SatisfactionFromActivity.this)
                        .setTitle("خطا")
                        .setMessage("خطا در دریافت اطلاعات")
                        .setPositiveButton("باشه", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                                finish();
                            }
                        }).create().show();
            }

            if (s.equals("0")) {
                lyProgress.setVisibility(View.GONE);
                new AlertDialog.Builder(SatisfactionFromActivity.this)
                        .setTitle("خطا")
                        .setMessage("اطلاعاتی جهت نمایش وجود ندارد")
                        .setPositiveButton("باشه", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                                finish();
                            }
                        }).create().show();
            }


        }

    }

    private void runSendAsync(JSONObject object) {
        if (object == null)
            return;
        if (sendFormAsync != null) return;
        sendFormAsync = new SendFormAsync(object);
        sendFormAsync.execute();
    }


    public class SendFormAsync extends AsyncTask<Void, String, String> {

        private ServerAnswer sendAnswer;
        private JSONObject form;

        public SendFormAsync(JSONObject form) {
            this.form = form;
        }

        @Override
        protected void onPreExecute() {

            lyProgress.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            sendFormAsync = null;

            sendAnswer = formRepo.sendCustomForm(getApplicationContext(), form);
            if (sendAnswer.getMessage() != null) {
                return "-1";
            }
            return "1";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("1")) {
                lyProgress.setVisibility(View.GONE);

                if (sendAnswer != null) {

                    new AlertDialog.Builder(SatisfactionFromActivity.this)
                            .setTitle("موفقیت")
                            .setMessage("اطلاعات با موفقیت ارسال شد")
                            .setPositiveButton("بستن", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                    finish();
                                }
                            }).create().show();

                }
            }
            if (s.equals("-1")) {

                lyProgress.setVisibility(View.GONE);

                new AlertDialog.Builder(SatisfactionFromActivity.this)
                        .setTitle("خطا")
                        .setMessage(sendAnswer.getMessage())
                        .setPositiveButton("باشه", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        }).create().show();
            }

        }

    }


    public class SatisfactionAdapter extends RecyclerView.Adapter<SatisfactionAdapter.SatisfactionViewHolder> {

        private CustomForm customForm = null;

        public void setCustomForm(CustomForm customForm) {
            this.customForm = customForm;
            notifyDataSetChanged();
        }

        public CustomForm getCustomForm() {
            return customForm;
        }

        @Override
        public SatisfactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (parent == null || parent.getContext() == null)
                return null;

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_satisfaction_item, parent, false);
            SatisfactionViewHolder holder = new SatisfactionViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final SatisfactionViewHolder holder, int position) {

            holder.rtbItems.setVisibility(View.VISIBLE);

            List<Question> questions = customForm.getQuestions();
            final Question question = questions.get(position);

            if (!TextUtils.isEmpty(question.getAnswer())) {
                holder.edtanswer.setText(question.getAnswer());
            }

            holder.txtQuestion.setText(questions.get(position).getTitle());
            holder.id = questions.get(position).getId();
            if (!questions.get(position).isNeedRate())
                holder.rtbItems.setVisibility(View.GONE);

            holder.edtanswer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    question.setAnswer(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            holder.rtbItems.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        float touchPositionX = event.getX();
                        float width = v.getWidth();
                        float starsf = (touchPositionX / width) * 5.0f;
                        int stars = (int) starsf + 1;
                        if (stars>=5)
                            stars=5;
                        if (stars<=0)
                            stars=0;
                        holder.rtbItems.setRating(stars);
                        question.setRate(stars);
                        v.setPressed(false);
                    }
                    return true;
                }
            });

        }


        @Override
        public int getItemCount() {
            if (customForm == null)
                return 0;

            if (customForm.getQuestions() == null || customForm.getQuestions().size() == 0)
                return 0;

            return customForm.getQuestions().size();
        }

        public class SatisfactionViewHolder extends RecyclerView.ViewHolder {

            private RatingBar rtbItems;
            private TextView txtQuestion;
            private EditText edtanswer;
            private long id;

            public SatisfactionViewHolder(View itemView) {
                super(itemView);

                rtbItems = (RatingBar) itemView.findViewById(R.id.rtbItems);
                txtQuestion = (TextView) itemView.findViewById(R.id.txtQuestion);
                edtanswer = (EditText) itemView.findViewById(R.id.edtAnswer);
//
            }
        }
    }

}
