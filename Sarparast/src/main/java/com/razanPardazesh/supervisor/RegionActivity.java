package com.razanPardazesh.supervisor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.zikey.sarparast.R;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.razanPardazesh.supervisor.model.Region;
import com.razanPardazesh.supervisor.model.wrapper.RegionsAnswer;
import com.razanPardazesh.supervisor.model.wrapper.ServerAnswer;
import com.razanPardazesh.supervisor.repo.RegionServerRepo;
import com.razanPardazesh.supervisor.repo.tools.IRepoCallBack;
import com.razanPardazesh.supervisor.tools.DialogBuilder;

import java.util.ArrayList;

public class RegionActivity extends AppCompatActivity {

    //Globals
    private String date = null;

    //Views
    ImageView imgBack;
    ImageView imgDatePicker;
    RecyclerView rvItems;
    RelativeLayout lyProgress;
    LinearLayoutManager linearLayoutManager;

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

        initToolbar();
        initRepo();
        initViews();
        initDatePickerListener();
        initClickListeners();
        initRecycleView();
        getData();

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
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public static void start(FragmentActivity context) {
        Intent starter = new Intent(context, RegionActivity.class);
        context.startActivity(starter);
    }

    private void initDatePickerListener() {

        dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {


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

        regionRepo.getDailyPaths(getApplicationContext(), null, null, new IRepoCallBack() {
            @Override
            public void onAnswer(ServerAnswer answer) {

                if (answer == null) {
                    new DialogBuilder().showAlert(RegionActivity.this, "اطلاعاتی جهت نمایش وجود ندارد");
                    lyProgress.setVisibility(View.GONE);
                    return;
                }

                if (answer.getIsSuccess() == 0) {
                    new DialogBuilder().showAlert(RegionActivity.this, "اطلاعاتی جهت نمایش وجود ندارد");
                    lyProgress.setVisibility(View.GONE);
                    return;
                }

                if (answer.getMessage() != null) {
                    new DialogBuilder().showAlert(RegionActivity.this, answer.getMessage());
                    lyProgress.setVisibility(View.GONE);
                    return;
                }

                ArrayList<Region> regions = null;
                if (!(answer instanceof RegionsAnswer)) {
                    new DialogBuilder().showAlert(RegionActivity.this, "اطلاعاتی جهت نمایش وجود ندارد");
                    lyProgress.setVisibility(View.GONE);
                    return;
                }
                regions = ((RegionsAnswer) answer).getRegions();

                if (regions == null || regions.size() == 0) {
                    new DialogBuilder().showAlert(RegionActivity.this, "اطلاعاتی جهت نمایش وجود ندارد");
                    lyProgress.setVisibility(View.GONE);
                    return;
                }

                adapter.setItems(regions);
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

        }

        @Override
        public int getItemCount() {
            if (items == null || items.size() == 0)
                return 0;
            return items.size();
        }

        public class RegionHolder extends RecyclerView.ViewHolder {


            public RegionHolder(View itemView) {
                super(itemView);
            }
        }
    }

}
