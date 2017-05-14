package com.razanPardazesh.supervisor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zikey.sarparast.R;
import com.razanPardazesh.supervisor.model.Unvisit;
import com.razanPardazesh.supervisor.model.wrapper.UnvisitsAnswer;
import com.razanPardazesh.supervisor.repo.UnvisitServerRepo;
import com.razanPardazesh.supervisor.repo.iRepo.IUnvisitRepo;

import java.util.ArrayList;

public class CustomerUnvisitedReasonsActivity extends AppCompatActivity {

    private static final String KEY_SHOP_CODE = "SHOP_CODE";
    private static final String KEY_NAME = "SHOP_NAME";

    private ImageView imgBack;
    private TextView txtHead;
    private RecyclerView rvItems;
    private LinearLayout lyProgress;

    private long shopCode;
    private String name;

    private CustomerUnvisitAdapter adapter;
    private IUnvisitRepo unvisitRepo;
    private CustomerUnvisitedAsync unvisitedAsync = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_unvisited_resons);
        parseIntent();
        initRepo();
        initViews();
        initRecycleView();
        statAsync();


    }


    private void initRecycleView() {

        if (rvItems == null)
            rvItems = (RecyclerView) findViewById(R.id.rvItems);

        if (adapter == null)
            adapter = new CustomerUnvisitAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvItems.setLayoutManager(layoutManager);

        rvItems.setAdapter(adapter);

    }

    private void initRepo() {
        if (unvisitRepo == null)
            unvisitRepo = new UnvisitServerRepo();
    }

    private void initViews() {

        imgBack = (ImageView) findViewById(R.id.imgBack);
        txtHead = (TextView) findViewById(R.id.txtHead);
        lyProgress = (LinearLayout) findViewById(R.id.lyProgress);
        if (!TextUtils.isEmpty(name))
            name=name+"(عدم سفارش)";
            txtHead.setText(name);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void parseIntent() {

        Intent intent = getIntent();
        if (intent == null)
            return;

        if (intent.hasExtra(KEY_SHOP_CODE))
            this.shopCode = intent.getLongExtra(KEY_SHOP_CODE, 0);

        if(intent.hasExtra(KEY_NAME))
            this.name=intent.getStringExtra(KEY_NAME);



    }

    private void statAsync() {

        if (unvisitedAsync != null)
            return;

        unvisitedAsync = new CustomerUnvisitedAsync();
        unvisitedAsync.execute();


    }

    public static void start(FragmentActivity context, long shopCode,String name) {

        Intent starter = new Intent(context, CustomerUnvisitedReasonsActivity.class);
        starter.putExtra(KEY_SHOP_CODE, shopCode);
        starter.putExtra(KEY_NAME,name);
        context.startActivity(starter);

    }


    public class CustomerUnvisitedAsync extends AsyncTask<Void, String, String> {

        private UnvisitsAnswer answer;

        @Override
        protected void onPreExecute() {


            lyProgress.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            unvisitedAsync = null;

            answer = unvisitRepo.getReasons(getApplicationContext(), shopCode);
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
                    if (adapter != null)
                        adapter.setUnvisits(answer.getUnvisits());

                }
            }
            if (s.equals("-1")) {

                lyProgress.setVisibility(View.GONE);

                new AlertDialog.Builder(CustomerUnvisitedReasonsActivity.this)
                        .setTitle("خطا")
                        .setMessage(answer.getMessage())
                        .setPositiveButton("باشه", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                                finish();
                            }
                        }).create().show();
            }

        }

    }


    public class CustomerUnvisitAdapter extends RecyclerView.Adapter<CustomerUnvisitAdapter.UnvisitHolder> {

        private ArrayList<Unvisit> unvisits = null;

        public ArrayList<Unvisit> getUnvisits() {
            return unvisits;
        }

        public void setUnvisits(ArrayList<Unvisit> unvisits) {
            this.unvisits = unvisits;
            notifyDataSetChanged();
        }

        @Override
        public UnvisitHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (parent == null || parent.getContext() == null)
                return null;

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_customer_unvisit_item, parent, false);
            UnvisitHolder holder = new UnvisitHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final UnvisitHolder holder, int position) {

            holder.txtDate.setText(unvisits.get(position).getOrderDate());
            holder.txtTime.setText(unvisits.get(position).getOrderTime());
            holder.txtComment.setText(unvisits.get(position).getComment());
            holder.txtReason.setText(unvisits.get(position).getReasonTitle());
            holder.txtName.setText(unvisits.get(position).getName());

        }


        @Override
        public int getItemCount() {

            if (unvisits == null || unvisits.size() == 0)
                return 0;

            return unvisits.size();

        }

        public class UnvisitHolder extends RecyclerView.ViewHolder {

            private TextView txtDate;
            private TextView txtTime;
            private TextView txtReason;
            private TextView txtComment;
            private TextView txtName;

            public UnvisitHolder(View itemView) {
                super(itemView);

                txtDate = (TextView) itemView.findViewById(R.id.txtDate);
                txtTime = (TextView) itemView.findViewById(R.id.txtTime);
                txtReason = (TextView) itemView.findViewById(R.id.txtReason);
                txtComment = (TextView) itemView.findViewById(R.id.txtComment);
                txtName = (TextView) itemView.findViewById(R.id.txtName);

//
            }
        }
    }

}
