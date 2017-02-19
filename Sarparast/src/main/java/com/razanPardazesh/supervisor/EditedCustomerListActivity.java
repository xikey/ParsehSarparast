package com.razanPardazesh.supervisor;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.R;

import com.razanPardazesh.supervisor.model.wrapper.CustomersEditAnswer;
import com.razanPardazesh.supervisor.repo.CustomersEditedServerRepo;
import com.razanPardazesh.supervisor.repo.iRepo.ICustomersEdited;
import com.razanPardazesh.supervisor.view.viewAdapter.CustomersEditAdapter;


public class EditedCustomerListActivity extends AppCompatActivity {

    private final long ROW_COUNT = 20;
    private long firstIndex = 0;
    private static final int STATUS_CHANGE_REQUEST_CODE = 14;
    private TextView txtHead;
    private EditText edtSearch;
    private RecyclerView rvCustomers;
    private RelativeLayout lyProgress;
    private LinearLayoutManager layoutManager;


    private CustomersEditAdapter adapter;
    private ICustomersEdited serverRepo;
    private CustomersRequestEditAsynk customersRequestAsynk;
    private String keySearch = "";

    private boolean hasMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edited_customer_list);

        initRepo();
        initViews();
        initRecycleView();
        startAsync();

    }

    public static void start(FragmentActivity context) {
        Intent starter = new Intent(context, EditedCustomerListActivity.class);
        context.startActivity(starter);
    }

    private void initViews() {

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtHead = (TextView) findViewById(R.id.txtHead);
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        rvCustomers = (RecyclerView) findViewById(R.id.rvCustomers);
        lyProgress = (RelativeLayout) findViewById(R.id.lyProgress);

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if (!keySearch.equals(v.getText().toString())) {
                        keySearch = v.getText().toString();

                        if (adapter != null)
                            adapter.clearList();
                        firstIndex = 0;
                        startAsync();
                    }

                }

                return false;
            }
        });

    }

    private void initRepo() {

        if (serverRepo == null)
            serverRepo = new CustomersEditedServerRepo();
    }

    private void initRecycleView() {
        if (rvCustomers == null)
            initViews();

        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rvCustomers.setLayoutManager(layoutManager);
        adapter = new CustomersEditAdapter(EditedCustomerListActivity.this);
        rvCustomers.setAdapter(adapter);


        rvCustomers.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (layoutManager != null) {

                    if (hasMore && (layoutManager.getItemCount()-1  == layoutManager.findLastVisibleItemPosition())) {

                        firstIndex += ROW_COUNT;
                        startAsync();
                    }
                }
            }
        });


    }

    public class CustomersRequestEditAsynk extends AsyncTask<Void, String, String> {

        private CustomersEditAnswer answer;
        private String message;

        @Override
        protected void onPreExecute() {
            lyProgress.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            customersRequestAsynk = null;
            hasMore = false;

            answer = serverRepo.getEditedList(getApplicationContext(), keySearch, firstIndex, ROW_COUNT);

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
                showErorAlertDialog("خطا", message);
            }

            if (s.equals("1")) {
                adapter.addItem(answer.getEditedList());
                lyProgress.setVisibility(View.GONE);

                if (answer.getHasMore() == 1) {
                    hasMore = true;

                }
            }
            if (s.equals("0"))
                showErorAlertDialog("خطا", "اطلاعاتی جهت نمایش وجود ندارد");
        }
    }

    private void startAsync() {

        if (customersRequestAsynk != null) return;

        if (NetworkTools.checkNetworkConnection(EditedCustomerListActivity.this) == false)
            finish();

        customersRequestAsynk = new CustomersRequestEditAsynk();
        customersRequestAsynk.execute();
    }

    private void showErorAlertDialog(String titile, String message) {
        if (message == null) return;

        AlertDialog alertDialog = new AlertDialog.Builder(EditedCustomerListActivity.this).create();
        alertDialog.setTitle(titile);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == STATUS_CHANGE_REQUEST_CODE && resultCode == RESULT_OK) {


            adapter.clearList();
            firstIndex = 0;
            startAsync();

        }
    }
}
