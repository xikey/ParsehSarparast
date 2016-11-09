package com.razanPardazesh.supervisor.view.viewAdapter;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.example.zikey.sarparast.AdamVisitInfo;
import com.example.zikey.sarparast.R;
import com.razanPardazesh.supervisor.model.Report;
import java.util.ArrayList;

/**
 * Created by Zikey on 09/11/2016.
 */

public class CoveragePercentAdapter extends RecyclerView.Adapter<CoveragePercentAdapter.productoViewHolder> implements Filterable {
    public ArrayList<Report> item;
    public ArrayList<Report> itemDump;


    private AppCompatActivity activity;
    private FragmentManager manager;

    public void setManager(FragmentManager manager) {
        this.manager = manager;
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }


    public CoveragePercentAdapter(ArrayList<Report> item) {
        this.item = item;
        this.itemDump = item;
    }

    @Override
    public CoveragePercentAdapter.productoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_coverage_percentage, viewGroup, false);
        CoveragePercentAdapter.productoViewHolder producto = new CoveragePercentAdapter.productoViewHolder(v);

        return producto;
    }

    @Override
    public void onBindViewHolder(CoveragePercentAdapter.productoViewHolder productoViewHolder, int i) {

//        productoViewHolder.txtName.setText("" + item.get(i).getName());


    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class VisitorsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<AdamVisitInfo> temp = new ArrayList<>();

            if (TextUtils.isEmpty(charSequence)) {

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemDump;
                filterResults.count = temp.size();

                return filterResults;
            }

            if (itemDump != null && itemDump.size() != 0) {

                for (Report item : itemDump) {

//                    if (!TextUtils.isEmpty(item.getName()) && item.getName().contains(charSequence)) {
//                        temp.add(item);
//                    }

                }
            }

            FilterResults filter = new FilterResults();
            filter.values = temp;
            filter.count = temp.size();

            return filter;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            ArrayList<Report> temp = null;

            try {
                temp = (ArrayList<Report>) filterResults.values;
            } catch (Exception e) {
                Log.e("mohsen: ", e.toString());
            }


            if (temp == null)
                temp = new ArrayList<>();

            item = temp;

            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {

        Filter filter = new CoveragePercentAdapter.VisitorsFilter();
        return filter;
    }

    public class productoViewHolder extends RecyclerView.ViewHolder {
        TextView txtCode, txtName;


        public productoViewHolder(final View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtCode = (TextView) itemView.findViewById(R.id.txtCode);



        }
    }


}

