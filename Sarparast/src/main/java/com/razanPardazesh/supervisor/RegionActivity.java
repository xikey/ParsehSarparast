package com.razanPardazesh.supervisor;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.zikey.sarparast.Helpers.FontApplier;
import com.example.zikey.sarparast.R;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.razanPardazesh.supervisor.model.Region;
import com.razanPardazesh.supervisor.model.user.Visitor;
import com.razanPardazesh.supervisor.model.wrapper.RegionsAnswer;
import com.razanPardazesh.supervisor.model.wrapper.ServerAnswer;
import com.razanPardazesh.supervisor.repo.RegionServerRepo;
import com.razanPardazesh.supervisor.repo.tools.IRepoCallBack;
import com.razanPardazesh.supervisor.tools.DialogBuilder;
import com.razanPardazesh.supervisor.tools.FontChanger;
import com.razanPardazesh.supervisor.tools.RxSearch;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegionActivity extends AppCompatActivity {

    //Globals
    private String date = null;
    private String keySearch = null;

    //Views
    ImageView imgBack;
    ImageView imgDatePicker;
    RecyclerView rvItems;
    RelativeLayout lyProgress;
    LinearLayoutManager linearLayoutManager;
    TextView txtError;
    private SearchView searchView;

    //Calendar
    DatePickerDialog.OnDateSetListener dateSetListener;
    private PersianCalendar persianCalendar = null;
    private DatePickerDialog datePickerDialog = null;

    //Repo
    RegionServerRepo regionRepo;

    //Adapter
    RegionAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);

        initRepo();
        initViews();
        initToolbar();
        initDatePickerListener();
        initClickListeners();
        initRecycleView();

    }

    private void initRepo() {

        if (regionRepo == null)
            regionRepo = new RegionServerRepo();
    }


    private void initRecycleView() {

        if (adapter == null)
            adapter = new RegionAdapter();

        if (linearLayoutManager == null)
            linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        rvItems.setLayoutManager(linearLayoutManager);
        rvItems.setAdapter(adapter);


    }


    private void initClickListeners() {

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPersianCalender(persianCalendar, datePickerDialog);
            }
        });

    }

    private void initViews() {

        imgBack = (ImageView) findViewById(R.id.imgBack);
        rvItems = (RecyclerView) findViewById(R.id.rvItems);
        lyProgress = (RelativeLayout) findViewById(R.id.lyProgress);
        imgDatePicker = (ImageView) findViewById(R.id.imgDatePicker);
        txtError = (TextView) findViewById(R.id.txtError);
        searchView = (SearchView) findViewById(R.id.searchView);

        FontApplier.applyMainFont(getApplicationContext(), txtError);
    }

    private void doSearch(String keySearch) {

        this.keySearch = keySearch;

        getData();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FontApplier.applyMainFont(getApplicationContext(), toolbar);


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

    public static void start(FragmentActivity context) {
        Intent starter = new Intent(context, RegionActivity.class);
        context.startActivity(starter);
    }

    private void initDatePickerListener() {

        dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                date = changeDateFormat(year, monthOfYear, dayOfMonth);
                getData();
            }
        };
    }


    private void showPersianCalender(PersianCalendar calender, DatePickerDialog dialog) {

        if (dateSetListener == null)
            return;

        if (calender == null)
            calender = new PersianCalendar();

        calender = new PersianCalendar();
        dialog = DatePickerDialog.newInstance(dateSetListener, calender.getPersianYear(), calender.getPersianMonth(),
                calender.getPersianDay());

        dialog.setThemeDark(false);
        dialog.show(getFragmentManager(), "DatePickerDialog");

    }

    private String changeDateFormat(int year, int monthOfYear, int dayOfMonth) {

        String month;
        String day;
        if (monthOfYear < 9) month = "0" + (monthOfYear + 1);
        else month = "" + (monthOfYear + 1);
        if (dayOfMonth < 9) day = "0" + (dayOfMonth);
        else day = "" + (dayOfMonth);
        return (year + "/" + month + "/" + day);

    }

    private void getData() {

        if (adapter != null)
            adapter.clearAdapter();

        txtError.setVisibility(View.GONE);
        lyProgress.setVisibility(View.VISIBLE);

        regionRepo.getDailyPaths(getApplicationContext(), date, keySearch, new IRepoCallBack() {
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

                ArrayList<Region> regions = null;
                if (!(answer instanceof RegionsAnswer)) {
                    txtError.setVisibility(View.VISIBLE);
                    lyProgress.setVisibility(View.GONE);
                    return;
                }
                regions = ((RegionsAnswer) answer).getRegions();

                if (regions == null || regions.size() == 0) {
                    txtError.setVisibility(View.VISIBLE);
                    lyProgress.setVisibility(View.GONE);
                    return;
                }

                adapter.setItems(regions);
                txtError.setVisibility(View.GONE);
                lyProgress.setVisibility(View.GONE);


            }

            @Override
            public void onError(Throwable error) {

                if (error != null) {
                    new DialogBuilder().showAlert(RegionActivity.this, error);
                    lyProgress.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onProgress(int p) {

            }
        });
    }


    public class RegionAdapter extends RecyclerView.Adapter<RegionAdapter.RegionHolder> {

        ArrayList<Region> items = null;

        public void setItems(ArrayList<Region> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        public void clearAdapter() {
            items = null;
            notifyDataSetChanged();
        }

        @Override
        public RegionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (parent == null || parent.getContext() == null)
                return null;

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_visitor_dayli_path_item, parent, false);
            return new RegionHolder(view);
        }

        @Override
        public void onBindViewHolder(RegionHolder holder, int position) {

            if (holder == null)
                return;

            if (items.get(position) == null)
                return;

            holder.region = items.get(position);

            Visitor thisVisitor = items.get(position).getVisitors().get(0);

            String visitorName = thisVisitor.getName();
            String visitorId = String.valueOf(thisVisitor.getId());
            if (!TextUtils.isEmpty(visitorName)) {

                holder.txtName.setText(visitorId + " - " + visitorName);
            } else {
                holder.txtName.setText(visitorId);
            }


            String regionName = items.get(position).getName();
            if (!TextUtils.isEmpty(regionName)) {
                holder.txtRegionName.setText(regionName);
            }


            if (position == 0) {
                holder.lyHeader.setVisibility(View.VISIBLE);
            } else if (position < getItemCount()) {

                try {
                    long thisId = thisVisitor.getId();
                    long previousId = items.get(position - 1).getVisitors().get(0).getId();

                    if (thisId == previousId) {
                        holder.lyHeader.setVisibility(View.GONE);
                    } else {
                        holder.lyHeader.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

        @Override
        public int getItemCount() {
            if (items == null || items.size() == 0)
                return 0;
            return items.size();
        }

        public class RegionHolder extends RecyclerView.ViewHolder {

            public Region region;
            private LinearLayout lyHeader;
            private TextView txtName;
            private TextView txtRegionName;
            private LinearLayout lyRoot;
            private LinearLayout lyRegion;


            public RegionHolder(View v) {
                super(v);

                lyHeader = (LinearLayout) v.findViewById(R.id.lyHeader);
                txtName = (TextView) v.findViewById(R.id.txtName);
                txtRegionName = (TextView) v.findViewById(R.id.txtRegionName);
                lyRoot = (LinearLayout) v.findViewById(R.id.lyRoot);
                lyRegion = (LinearLayout) v.findViewById(R.id.lyRegion);

                FontApplier.applyMainFont(getApplicationContext(), lyRoot);
                FontChanger.applyTitleFont(txtName);

                lyRegion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (region==null)
                            return;

                        CustomerActivity.start(RegionActivity.this,CustomerActivity.KEY_REGION_CUSTOMERS);

                    }
                });

            }
        }
    }

}
