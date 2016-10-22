package com.example.zikey.sarparast;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Zikey
 * on 05/08/2015.
 */
public class AdamVisitMoshtarianInfoAdapter extends RecyclerView.Adapter<AdamVisitMoshtarianInfoAdapter.productoViewHolder> implements Filterable {
    public ArrayList<AdamVisitInfo> item;
    public ArrayList<AdamVisitInfo> itemDump;


    //state -1 is VISITOR
    //state 1 is Kala

    private int state;
    private String checker = "0";

    public void setChecker(String checker) {
        this.checker = checker;
    }

    private AppCompatActivity activity;
    private FragmentManager manager;

    public void setManager(FragmentManager manager) {
        this.manager = manager;
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setState(int state) {
        this.state = state;
    }
//     public void setItem(ArrayList<SarjamInfo> item) {
//         this.item = item;
//     }

    public AdamVisitMoshtarianInfoAdapter(ArrayList<AdamVisitInfo> item) {
        this.item = item;
        this.itemDump = item;
    }

    @Override
    public productoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_adamvisit_moshtarian, viewGroup, false);
        productoViewHolder producto = new productoViewHolder(v);

        return producto;
    }

    @Override
    public void onBindViewHolder(productoViewHolder productoViewHolder, int i) {

        productoViewHolder.txtName.setText("" + item.get(i).getName());
        productoViewHolder.txtCode.setText("" + item.get(i).getCode());
        productoViewHolder.txtTime.setText("" + item.get(i).getTime());
        productoViewHolder.txtTell.setText("" + item.get(i).getTell());
        productoViewHolder.txtMobile.setText("" + item.get(i).getMobile());
        productoViewHolder.txtAddress.setText("" + item.get(i).getAddress());

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class SarjamBarFilter extends Filter {

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

                for (AdamVisitInfo item : itemDump) {

                    if (!TextUtils.isEmpty(item.getName()) && item.getName().contains(charSequence)) {
                        temp.add(item);
                    }

                    if (!TextUtils.isEmpty(item.getCode()) && item.getCode().contains(charSequence)) {
                        temp.add(item);
                    }
                }
            }

            FilterResults filter = new FilterResults();
            filter.values = temp;
            filter.count = temp.size();

            return filter;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            ArrayList<AdamVisitInfo> temp = null;

            try {
                temp = (ArrayList<AdamVisitInfo>) filterResults.values;
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

        Filter filter = new SarjamBarFilter();
        return filter;
    }

    public class productoViewHolder extends RecyclerView.ViewHolder {
        TextView txtCode, txtName, txtTime, txtTell, txtMobile, txtAddress;
        ImageView imgDetails;

        public productoViewHolder(final View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtCode = (TextView) itemView.findViewById(R.id.txtCode);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            txtTell = (TextView) itemView.findViewById(R.id.txtTel);
            txtMobile = (TextView) itemView.findViewById(R.id.txtMobile);
            txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);

//            imgDetails = (ImageView) itemView.findViewById(R.id.imgDetails);


//            imgDetails.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    Intent intent = new Intent(activity, ActivityListOfAdamVisitMoshjtarian.class);
//                    if (checker != null) {
//
//                        intent.putExtra("checker", checker);
//                        intent.putExtra("hedare", txtName.getText().toString());
//                        intent.putExtra("ID",txtCode.getText().toString());
//                        activity.startActivity(intent);
//                    }
//                }
//            });

        }
    }


}
