package com.example.zikey.sarparast;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.FontApplier;

import java.util.ArrayList;

/**
 * Created by Zikey on 08/10/2016.
 */

public class CustomersDistancesAdapter extends RecyclerView.Adapter<CustomersDistancesAdapter.CustomerViewHolder> implements Filterable {
    private ArrayList<BazaryabInfo> item;
    private ArrayList<BazaryabInfo> itemDump;
    private FragmentActivity activity;

    public CustomersDistancesAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    public void setItem(ArrayList<BazaryabInfo> item) {
        if (item == null || item.size() == 0)
            return;

        this.item = item;
        this.itemDump = item;

        notifyDataSetChanged();

    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_customer_distance, parent, false);
        CustomerViewHolder customerViewHolder = new CustomerViewHolder(v);
        return customerViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder holder, int position) {
        holder.txtName.setText("" + item.get(position).get_Name().toString());
        holder.txtCode.setText("" + item.get(position).get_Code().toString());
        holder.txtDistance.setText("" + item.get(position).get_Distance() + " متر ");

        double count = (item.get(position).get_Distance());

        if (count > 500) {
            holder.lyRoot.setBackgroundColor(Color.parseColor("#ef9a9a"));
        }
        if (count < 500) {
            holder.lyRoot.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Override
    public int getItemCount() {
        if (item == null)
            return 0;

        return item.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new DistanceFilter();
        return filter;
    }

    public class DistanceFilter extends Filter {


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<BazaryabInfo> temp = new ArrayList<>();

            if (TextUtils.isEmpty(constraint)) {

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemDump;
                filterResults.count = temp.size();

                return filterResults;
            }

            if (itemDump != null || itemDump.size() != 0) {
                for (BazaryabInfo item : itemDump) {

                    if ((!TextUtils.isEmpty(item.get_Name())) && item.get_Name().contains(constraint)) {

                        temp.add(item);
                    }
                    if ((!TextUtils.isEmpty(item.get_Code())) && item.get_Code().contains(constraint)) {

                        temp.add(item);
                    }
                }

            }
            FilterResults fl = new FilterResults();
            fl.values = temp;
            fl.count = temp.size();
            return fl;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            ArrayList<BazaryabInfo> temp = null;

            try {
                temp = (ArrayList<BazaryabInfo>) results.values;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (temp == null) temp = new ArrayList<>();

            item = temp;
            notifyDataSetChanged();
        }
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName;
        private TextView txtCode;
        private TextView txtDistance;
        private LinearLayout lyRoot;


        public CustomerViewHolder(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtCode = (TextView) itemView.findViewById(R.id.txtCode);
            txtDistance = (TextView) itemView.findViewById(R.id.txtDistance);
            lyRoot = (LinearLayout) itemView.findViewById(R.id.lyRoot);


            FontApplier.applyMainFont(activity, lyRoot);


        }
    }
}
