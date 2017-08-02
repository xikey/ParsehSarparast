package com.razanPardazesh.supervisor;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zikey.sarparast.ActivityCustomersLastOrders;
import com.example.zikey.sarparast.ActivityGoogleMap;
import com.example.zikey.sarparast.ActivityTopSelledOrReturnedGoods;
import com.example.zikey.sarparast.Fragment_Customers5Vijegi;
import com.example.zikey.sarparast.Fragment_CustomersPhoneNumber;
import com.example.zikey.sarparast.Fragment_MandehMoshtarian;
import com.example.zikey.sarparast.R;
import com.razanPardazesh.supervisor.model.user.Customer;
import com.razanPardazesh.supervisor.model.wrapper.CustomersAnswer;
import com.razanPardazesh.supervisor.model.wrapper.ServerAnswer;
import com.razanPardazesh.supervisor.repo.CustomerServerRepo;
import com.razanPardazesh.supervisor.repo.iRepo.ICustomer;
import com.razanPardazesh.supervisor.repo.tools.IRepoCallBack;
import com.razanPardazesh.supervisor.tools.LogWrapper;
import com.razanPardazesh.supervisor.tools.NumberSeperator;
import com.razanPardazesh.supervisor.tools.RxSearch;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.zikey.sarparast.R.id.code;

public class CustomerActivity extends AppCompatActivity {

    //Globals
    public static final String KEY_REGION_CUSTOMERS = "REGION_CUSTOMERS";
    private static final String KEY_REGION_ID = "REGION_ID";
    private static final String KEY_REGION_LEVEL = "REGION_LEVEL";
    private static final String KEY_VISITOR_CODE = "VISITOR_CODE";
    private static final String KEY_STARTER = "STARTER";

    private String keySearch;
    private long regionID;
    private long visitorCode;
    private int regionLevel;
    private long lastIndex;
    private int count = 50;
    private boolean hasMore;

    private int lastVisibleItem, totalItemCount;
    private int visibleThreshold = 2;
    private boolean isLoading;

    //Views
    private RecyclerView rvItems;
    private ImageView imgBack;
    private SearchView searchView;
    private RelativeLayout lyProgress;
    private TextView txtError;
    private LinearLayoutManager layoutManager;

    //adapter
    private CustomerAdapter adapter;
    //Repo
    private ICustomer customerRepo;

    //   use for loadMore
    private ArrayList<Customer> customers = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        initRepo();
        parseIntent();
        initViews();
        initToolbar();
        initRecycleView();
        initClickListeners();

    }

    private void parseIntent() {

        Intent data = getIntent();
        if (data == null)
            return;

        if (data.hasExtra(KEY_VISITOR_CODE))
            visitorCode = data.getLongExtra(KEY_VISITOR_CODE, 0);

        if (data.hasExtra(KEY_REGION_ID))
            regionID = data.getLongExtra(KEY_REGION_ID, 0);

        if (data.hasExtra(KEY_REGION_LEVEL))
            regionLevel = data.getIntExtra(KEY_REGION_LEVEL, 0);


    }


    private void initRepo() {
        if (customerRepo == null)
            customerRepo = new CustomerServerRepo();
    }

    private void initClickListeners() {

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initRecycleView() {

        if (adapter == null)
            adapter = new CustomerAdapter();

        if (layoutManager == null)
            layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        rvItems.setLayoutManager(layoutManager);

        rvItems.setAdapter(adapter);

        rvItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (adapter == null)
                    return;


                if (layoutManager != null && customers != null) {

                    totalItemCount = layoutManager.getItemCount();
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (customers.size() <= count && hasMore) {
                            customers.add(null);
                            adapter.setIsLoading(true);
                            adapter.notifyItemInserted(customers.size() - 1);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    customers.remove(customers.size() - 1);
                                    adapter.notifyItemRemoved(customers.size());

                                    isLoading = true;
                                    getData();

                                }
                            }, 500);
                        } else {

                        }
                    }

                }
            }
        });

    }

    private void doSearch(String keySearch) {

        if (adapter != null)
            adapter.clearAdapter();


        this.keySearch = keySearch;

        lastIndex=0;
        lyProgress.setVisibility(View.VISIBLE);
        getData();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        searchEditText.setHint(("جستجو منطقه و بازاریاب"));


        RxSearch.fromSearchView(searchView)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(String s) {
                        doSearch(s);
                    }
                });

        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setQuery("", false);
                searchView.onActionViewCollapsed();

            }
        });
    }

    private void initViews() {

        rvItems = (RecyclerView) findViewById(R.id.rvItems);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        searchView = (SearchView) findViewById(R.id.searchView);
        lyProgress = (RelativeLayout) findViewById(R.id.lyProgress);
        txtError = (TextView) findViewById(R.id.txtError);
    }

    private void getData() {

        if (customerRepo == null)
            initRepo();

        customerRepo.getCustomers(getApplicationContext(), regionID, regionLevel, visitorCode, keySearch, count, lastIndex, new IRepoCallBack() {
            @Override
            public void onAnswer(ServerAnswer answer) {

                hasMore = false;

                if (answer == null) {
                    txtError.setVisibility(View.VISIBLE);
                    lyProgress.setVisibility(View.GONE);
                    return;
                }

                if (answer.getIsSuccess() == 0) {
                    txtError.setVisibility(View.VISIBLE);
                    lyProgress.setVisibility(View.GONE);
                    return;
                }

                if (answer.getMessage() != null) {
                    txtError.setVisibility(View.VISIBLE);
                    lyProgress.setVisibility(View.GONE);
                    return;
                }

                if (!(answer instanceof CustomersAnswer)) {
                    txtError.setVisibility(View.VISIBLE);
                    lyProgress.setVisibility(View.GONE);
                    return;
                }

                customers = ((CustomersAnswer) answer).getCustomers();


                if (customers == null || customers.size() == 0) {
                    txtError.setVisibility(View.VISIBLE);
                    lyProgress.setVisibility(View.GONE);
                    return;
                }

                adapter.addItems(customers);
                lastIndex = answer.getLastIndex();
                if (answer.getHasMore() == 1)
                    hasMore = true;
                isLoading = false;
                adapter.setIsLoading(false);
                txtError.setVisibility(View.GONE);
                lyProgress.setVisibility(View.GONE);

            }

            @Override
            public void onError(Throwable error) {
                txtError.setVisibility(View.VISIBLE);
                lyProgress.setVisibility(View.GONE);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onProgress(int p) {

            }
        });

    }

    public static void start(FragmentActivity context, String key) {
        Intent starter = new Intent(context, CustomerActivity.class);
        starter.putExtra(KEY_STARTER, key);
        context.startActivity(starter);
    }


    public static void start_forRegionCustomers(FragmentActivity context, long regionId, int regionLevel, long visitorCode) {
        Intent starter = new Intent(context, CustomerActivity.class);
        starter.putExtra(KEY_STARTER, KEY_REGION_CUSTOMERS);
        starter.putExtra(KEY_REGION_ID, regionId);
        starter.putExtra(KEY_REGION_LEVEL, regionLevel);
        starter.putExtra(KEY_VISITOR_CODE, visitorCode);
        context.startActivity(starter);

    }


    public class CustomerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public ArrayList<Customer> items;
        private boolean isLoading = false;
        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;

        public void setItems(ArrayList<Customer> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        public void addItems(ArrayList<Customer> items) {

            if (items == null || items.size() == 0)
                return;

            if (this.items == null) {
                this.items = new ArrayList<>();
            }

            this.items.addAll(items);
            notifyDataSetChanged();
        }


        public void clearAdapter() {
            items = null;
            notifyDataSetChanged();

        }

        public void setIsLoading(boolean isLoading) {
            if (isLoading) {
                this.isLoading = true;
            } else {
                this.isLoading = false;
            }
        }

        public ArrayList<Customer> getCustomers() {

            if (items == null || items.size() == 0)
                return null;

            return items;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (parent == null || parent.getContext() == null)
                return null;
            if (viewType == VIEW_TYPE_ITEM) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_mande_moshtarian, parent, false);
                return new CustomerHolder(view);
            } else if (viewType == VIEW_TYPE_LOADING) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.row_load_more, parent, false);
                return new LoadingViewHolder(view);
            }

            return null;

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder == null)
                return;

            if (items.get(position) == null)
                return;

            if (holder instanceof CustomerHolder) {
                CustomerHolder cHolder = (CustomerHolder) holder;

                Customer customer = items.get(position);
                cHolder.customer = customer;

                String customerAddress = customer.getAddress();
                String customerName = customer.getName();
                long customerCode = customer.getCodeMarkaz();

                cHolder.txtCode.setText(String.valueOf(customerCode));

                if (!TextUtils.isEmpty(customerName))
                    cHolder.txtName.setText(customerName);

                if (!TextUtils.isEmpty(customerAddress))
                    cHolder.txtAddress.setText(customerAddress);

                if (customer.getAccountData() != null) {
                    try {
                        String mandeh = customer.getAccountData().getBalance();
                        if (!TextUtils.isEmpty(mandeh)) {
                            double val = Double.parseDouble(mandeh);
                            cHolder.txtMande.setText(NumberSeperator.separate(val));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (customer.getVisitor() != null) {

                    String nameVaset = customer.getVisitor().getName();

                    if (TextUtils.isEmpty(nameVaset))
                        cHolder.txtVaset.setText(String.valueOf(visitorCode));
                    cHolder.txtVaset.setText(nameVaset + " - " + visitorCode);
                }

            } else if (holder instanceof LoadingViewHolder) {

                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;


            }
        }

        @Override
        public int getItemViewType(int position) {

            if (isLoading && position == getItemCount() - 1)
                return 1;
            else
                return 0;
        }

        @Override
        public int getItemCount() {
            if (items == null || items.size() == 0)
                return 0;
            return items.size();
        }


        public class LoadingViewHolder extends RecyclerView.ViewHolder {


            public LoadingViewHolder(View view) {
                super(view);

            }
        }


        public class CustomerHolder extends RecyclerView.ViewHolder {

            private TextView txtCode;
            private TextView txtName;
            private TextView txtMande;
            private TextView txtVaset;
            private TextView txtAddress;
            private ImageView btnShowDetails;
            private ImageView imgInfo;
            private ImageView imgNavigate;
            private ImageView imgCall;
            private ImageView img5Vijegi;
            private ImageView imgLike;
            private ImageView imgDislike;
            private ImageView imgMandehLine;
            private ImageView imgQuestions;
            private ImageView unvisited;
            private LinearLayout lyButtom;
            public Customer customer;

            public CustomerHolder(View itemView) {
                super(itemView);

                txtCode = (TextView) itemView.findViewById(R.id.txtCode);
                txtName = (TextView) itemView.findViewById(R.id.txtName);
                txtMande = (TextView) itemView.findViewById(R.id.txtMande);
                txtVaset = (TextView) itemView.findViewById(R.id.txtVaset);
                txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);
                lyButtom = (LinearLayout) itemView.findViewById(R.id.lyButtom);
                btnShowDetails = (ImageView) itemView.findViewById(R.id.btnShowDetails);
                imgInfo = (ImageView) itemView.findViewById(R.id.imgInfo);
                imgNavigate = (ImageView) itemView.findViewById(R.id.imgNavigate);
                imgCall = (ImageView) itemView.findViewById(R.id.imgCall);
                img5Vijegi = (ImageView) itemView.findViewById(R.id.img5Vijegi);
                imgLike = (ImageView) itemView.findViewById(R.id.imgLike);
                imgDislike = (ImageView) itemView.findViewById(R.id.imgDislike);
                imgMandehLine = (ImageView) itemView.findViewById(R.id.imgMandehLine);
                imgQuestions = (ImageView) itemView.findViewById(R.id.imgQuestions);
                unvisited = (ImageView) itemView.findViewById(R.id.unvisited);

                imgLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (customer == null)
                            return;

                        Intent intent = new Intent(CustomerActivity.this, ActivityTopSelledOrReturnedGoods.class);
                        intent.putExtra("code", customer.getCodeMarkaz());
                        intent.putExtra("Method", "S_GetListOf_Top_Selled_Goods");
                        startActivity(intent);
                    }
                });

                imgDislike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (customer == null)
                            return;

                        Intent intent = new Intent(CustomerActivity.this, ActivityTopSelledOrReturnedGoods.class);
                        intent.putExtra("code", customer.getCodeMarkaz());
                        intent.putExtra("Method", "S_GetListOf_Top_Returned_Goods");
                        startActivity(intent);
                    }
                });

                img5Vijegi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        android.app.FragmentManager manager = getFragmentManager();
                        // TODO: 30/07/2017 fragment must change becuase the set activity not necessary

                        try {
                            Fragment_Customers5Vijegi fragment = new Fragment_Customers5Vijegi();
                            fragment.setActivity(CustomerActivity.this);
                            fragment.setName(customer.getName());
                            fragment.setCode(String.valueOf(customer.getCodeMarkaz()));
                            fragment.show(manager, "FiveProperty");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

                imgCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (customer == null)
                            return;

                        if (customer.getTel() != null && customer.getMobile() != null) {

                            if ((customer.getTel().equals("0")) && (customer.getMobile().equals("0"))) {
                                new AlertDialog.Builder(CustomerActivity.this)
                                        .setTitle("خطا")
                                        .setMessage("شماره ای به مشتری اختصاص داده نشده است!")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface arg0, int arg1) {
                                            }
                                        }).create().show();
                            } else {

                                try {
                                    android.app.FragmentManager manager = getFragmentManager();
                                    // TODO: 30/07/2017 fragment must change becuase the set activity not necessary try catch must be delete
                                    Fragment_CustomersPhoneNumber showDetails = new Fragment_CustomersPhoneNumber();
                                    showDetails.setName(customer.getName());
                                    showDetails.setPhone(customer.getTel());
                                    showDetails.setMobile(customer.getMobile());
                                    showDetails.setActivity(CustomerActivity.this);
                                    showDetails.show(manager, "CustomerPhoneNumber");

                                    showDetails.setCancelable(true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                });


                btnShowDetails.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if (customer == null)
                            return;
                        // TODO: 30/07/2017 fragment must change becuase the set activity not necessary try catch must be delete
                        try {
                            android.app.FragmentManager manager = getFragmentManager();
                            Fragment_MandehMoshtarian showDetails = new Fragment_MandehMoshtarian();
                            showDetails.setID(String.valueOf(customer.getCodeMarkaz()));
                            showDetails.show(manager, "Details");

                            showDetails.setCancelable(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });

                imgInfo.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        if (customer == null)
                            return;

                        Intent intent = new Intent(CustomerActivity.this, ActivityCustomersLastOrders.class);
                        intent.putExtra("code", customer.getCodeMarkaz());
                        startActivity(intent);
                    }
                });

                imgNavigate.setOnClickListener(new View.OnClickListener() {
                    // TODO: 30/07/2017 fragment must change becuase the set activity not necessary try catch must be delete
                    @Override
                    public void onClick(View view) {

                        if (customer == null)
                            return;

                        if (customer.getLt() == null || customer.getLt() == null)
                            return;

                        if (customer.getLn().equals("0") || customer.getLt().equals("0")) {

                            new AlertDialog.Builder(CustomerActivity.this)
                                    .setTitle("خطا")
                                    .setMessage("مکانی برای این مشتری روی نقشه ثبت نشده است!")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface arg0, int arg1) {
                                        }
                                    }).create().show();
                        } else {

                            try {
                                Intent intent = new Intent(CustomerActivity.this, ActivityGoogleMap.class);
                                intent.putExtra("state", "NotOrdered");
                                intent.putExtra("Lat", customer.getLt());
                                intent.putExtra("Long", customer.getLn());
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });

                imgMandehLine.setOnClickListener(new View.OnClickListener() {
                    // TODO: 30/07/2017 fragment must change becuase the set activity not necessary try catch must be delete
                    @Override
                    public void onClick(View v) {

                        try {
                            String code = String.valueOf(customer.getCodeMarkaz());
                            String name = customer.getName();
                            String mandeh = customer.getAccountData().getBalance();
                            CustomerDeptDividedLineFragment.Show(CustomerActivity.this, name, code, mandeh);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

                imgQuestions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (customer == null)
                            return;

                        String name = customer.getName();


                        try {
                            double lt = Double.parseDouble(customer.getLt());
                            double lng = Double.parseDouble(customer.getLn());
                            SatisfactionFromActivity.start(CustomerActivity.this, code, name, lt, lng);
                        } catch (Exception ex) {
                            LogWrapper.loge("productoViewHolder_onClick_Exception: ", ex);
                        }


                    }
                });

                unvisited.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (customer == null)
                            return;
                        String name = customer.getName();
                        if (TextUtils.isEmpty(name))
                            return;
                        CustomerUnvisitedReasonsActivity.start(CustomerActivity.this, customer.getCodeMarkaz(), name);
                    }
                });


            }
        }
    }


}
