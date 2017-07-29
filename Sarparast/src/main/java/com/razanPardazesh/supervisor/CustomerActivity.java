package com.razanPardazesh.supervisor;


import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zikey.sarparast.R;
import com.razanPardazesh.supervisor.model.user.Customer;
import com.razanPardazesh.supervisor.model.wrapper.CustomersAnswer;
import com.razanPardazesh.supervisor.model.wrapper.ServerAnswer;
import com.razanPardazesh.supervisor.repo.CustomerServerRepo;
import com.razanPardazesh.supervisor.repo.iRepo.ICustomer;
import com.razanPardazesh.supervisor.repo.tools.IRepoCallBack;

import java.util.ArrayList;

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

    //Views
    private RecyclerView rvItems;
    private ImageView imgBack;
    private SearchView searchView;
    RelativeLayout lyProgress;
    TextView txtError;

    //adapter
    private CustomerAdapter adapter;
    //Repo
    private ICustomer customerRepo;


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
        getData();

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

        rvItems.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        rvItems.setAdapter(adapter);

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

                ArrayList<Customer> customers = null;
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

                adapter.setItems(customers);
                txtError.setVisibility(View.GONE);
                lyProgress.setVisibility(View.GONE);

            }

            @Override
            public void onError(Throwable error) {

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


    public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerHolder> {

        public ArrayList<Customer> items;

        public void setItems(ArrayList<Customer> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        public void clearAdapter() {
            items = null;
            notifyDataSetChanged();

        }

        @Override
        public CustomerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (parent == null || parent.getContext() == null)
                return null;

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_mande_moshtarian, parent, false);
            return new CustomerHolder(view);
        }

        @Override
        public void onBindViewHolder(CustomerHolder holder, int position) {
            if (holder == null)
                return;


        }

        @Override
        public int getItemCount() {
            if (items == null || items.size() == 0)
                return 0;
            return items.size();
        }

        public class CustomerHolder extends RecyclerView.ViewHolder {
            TextView txtCode;
            TextView txtName;
            TextView txtMande;
            TextView txtVaset;
            TextView txtAddress;
            ImageView btnShowDetails;
            ImageView imgInfo;
            ImageView imgNavigate;
            ImageView imgCall;
            ImageView img5Vijegi;
            ImageView imgLike;
            ImageView imgDislike;
            ImageView imgMandehLine;
            ImageView imgQuestions;
            ImageView unvisited;
            LinearLayout lyButtom;
            String L;
            String W;
            String tell;
            String mobile;
            String ID;


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

            }
        }
    }


}
