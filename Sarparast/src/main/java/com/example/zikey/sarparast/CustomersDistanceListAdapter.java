package com.example.zikey.sarparast;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.Convertor;
import com.example.zikey.sarparast.Helpers.FontApplier;

import java.util.ArrayList;

/**
 * Created by Zikey on 09/10/2016.
 */

public class CustomersDistanceListAdapter extends RecyclerView.Adapter<CustomersDistanceListAdapter.CustomerViewHolder> implements Filterable {
    private ArrayList<BazaryabInfo> item;
    private ArrayList<BazaryabInfo> itemDump;
    private FragmentActivity activity;

    private Double visitorLat = 0.0;
    private Double visitorLong = 0.0;
    private Double customerLat = 0.0;
    private Double customerLong = 0.0;


    public CustomersDistanceListAdapter(FragmentActivity activity) {
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
    public CustomersDistanceListAdapter.CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_customers_distance, parent, false);
        CustomersDistanceListAdapter.CustomerViewHolder customerViewHolder = new CustomersDistanceListAdapter.CustomerViewHolder(v);
        return customerViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomersDistanceListAdapter.CustomerViewHolder holder, int position) {
        holder.txtName.setText("" + item.get(position).get_Name().toString());
        holder.txtCode.setText("" + item.get(position).get_Code().toString());
        holder.txtTell.setText("" + item.get(position).get_Tel().toString());
        holder.txtAddress.setText("" + item.get(position).get_Address().toString());

        holder.txtDistance.setText("" + item.get(position).get_Distance() + " متر ");

        parseNavigationInfo(item.get(position).getWrappers(), (LinearLayout) holder.lyJson);

        visitorLat = Double.valueOf(item.get(position).get_W());
        visitorLong = Double.valueOf(item.get(position).get_L());
        customerLat = Double.valueOf(item.get(position).get_CustomerLat());
        customerLong = Double.valueOf(item.get(position).get_CustomerLong());


        if (item.get(position).get_Distance() == 0.0) {
            holder.txtDistance.setText(" ندارد ");
        }

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
        Filter filter = new CustomersDistanceListAdapter.DistanceFilter();
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
        private TextView txtTell;
        private TextView txtAddress;
        private LinearLayout lyRoot;
        private LinearLayout lyJson;

        public CustomerViewHolder(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtCode = (TextView) itemView.findViewById(R.id.txtCode);
            txtDistance = (TextView) itemView.findViewById(R.id.txtDistance);
            txtTell = (TextView) itemView.findViewById(R.id.txtTell);
            txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);
            lyJson = (LinearLayout) itemView.findViewById(R.id.lyJson);



            lyRoot = (LinearLayout) itemView.findViewById(R.id.lyRoot);
            FontApplier.applyMainFont(activity, lyRoot);

            lyRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ActivityOrdersPointsMap.class);
                    intent.putExtra("visitorLat", visitorLat);
                    intent.putExtra("visitorLong", visitorLong);
                    intent.putExtra("customerLat", customerLat);
                    intent.putExtra("customerLong", customerLong);
                    activity.startActivity(intent);
                }
            });

        }
    }


    private void parseNavigationInfo(ArrayList<ActivityGoogleMap.NavigationWrapper> wrappers, LinearLayout parent) {
       int margin =  Convertor.toPixcel(98f, activity.getApplicationContext());

        parent.removeAllViews();

        for (int i = 0; i < wrappers.size(); i++) {

            LinearLayout row = new LinearLayout(activity.getApplicationContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int padding = Convertor.toPixcel(5f, activity.getApplicationContext());
            int paddingRight = Convertor.toPixcel(10f, activity.getApplicationContext());
            row.setPadding(paddingRight, padding, paddingRight, padding);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(params);

            TextView value = new TextView(activity.getApplicationContext());
            LinearLayout.LayoutParams paramsValue = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            value.setText(wrappers.get(i).value);
            value.setLayoutParams(paramsValue);
            value.setGravity(Gravity.RIGHT);
             value.setTextColor(Color.parseColor("#263238"));
            row.addView(value);

            TextView title = new TextView(activity.getApplicationContext());
            LinearLayout.LayoutParams paramsTitle = new LinearLayout.LayoutParams(margin, LinearLayout.LayoutParams.WRAP_CONTENT, 0f);
            title.setText(wrappers.get(i).title);

            title.setTextColor(Color.parseColor("#263238"));
            title.setLayoutParams(paramsTitle);

            row.addView(title);
            FontApplier.applyMainFont(activity.getApplicationContext(),parent);

            parent.addView(row);


        }

    }
}
