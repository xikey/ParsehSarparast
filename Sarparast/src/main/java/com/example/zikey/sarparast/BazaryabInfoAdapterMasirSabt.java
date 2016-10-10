package com.example.zikey.sarparast;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Zikey on 18/07/2016.
 */
public class BazaryabInfoAdapterMasirSabt extends RecyclerView.Adapter<BazaryabInfoAdapterMasirSabt.productoViewHolder> implements Filterable {
    private ArrayList<BazaryabInfo> item;
    private ArrayList<BazaryabInfo> itemDump;
    //    public  static  int counter=1;
    private AppCompatActivity activity;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public BazaryabInfoAdapterMasirSabt(ArrayList<BazaryabInfo> item) {
        this.item = item;
        this.itemDump = item;
    }

    @Override
    public productoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_bazaryab_info, viewGroup, false);
        productoViewHolder producto = new productoViewHolder(v);

        return producto;
    }

    @Override
    public void onBindViewHolder(productoViewHolder productoViewHolder, int i) {
        productoViewHolder.txtName.setText(item.get(i).get_Name());
        productoViewHolder.txtCode.setText(item.get(i).get_Code());
        productoViewHolder.txtTime.setText("" + item.get(i).get_Time());
        productoViewHolder.ID = Integer.parseInt(item.get(i).get_ID());


//        if ((Integer.parseInt(m.toString()))%2==0){
//            productoViewHolder.lyRoot02.setBackgroundColor(Color.parseColor("#ffffff"));
//        }
//        if ((Integer.parseInt(m.toString()))%2>0){
//            productoViewHolder.lyRoot02.setBackgroundColor(Color.parseColor("#E8EAF6"));
//        }

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new MasirSabtSefareshFilter();
        return filter;
    }


    public class MasirSabtSefareshFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<BazaryabInfo> temp = new ArrayList<>();

            if (TextUtils.isEmpty(charSequence)) {

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemDump;
                filterResults.count = temp.size();

                return filterResults;
            }

            if (itemDump != null && itemDump.size() != 0) {
                for (BazaryabInfo item : itemDump) {
                    if ((!TextUtils.isEmpty(item.get_Name())) && item.get_Name().contains(charSequence)) {

                        temp.add(item);
                    }
                    if ((!TextUtils.isEmpty(item.get_Code())) && item.get_Code().contains(charSequence)) {

                        temp.add(item);
                    }
                }
            }
            FilterResults FR = new FilterResults();
            FR.values = temp;
            FR.count = temp.size();

            return FR;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            ArrayList<BazaryabInfo> temp = null;

            try {
                temp = (ArrayList<BazaryabInfo>) filterResults.values;

            } catch (Exception e) {

                Log.e("Eror: ", e.toString());
            }

            if (temp == null) temp = new ArrayList<>();

            item = temp;

            notifyDataSetChanged();

        }
    }


    public class productoViewHolder extends RecyclerView.ViewHolder {
        TextView txtCode, txtName, txtTime;
        LinearLayout lyRoot;
        ImageView imgBazaryab;
        int ID;


        public productoViewHolder(final View itemView) {
            super(itemView);

            txtCode = (TextView) itemView.findViewById(R.id.txtCode);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            lyRoot = (LinearLayout) itemView.findViewById(R.id.lyRoot);


            lyRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("ID BAzaryab", "Bazaryab ID " + ID);

                    Intent intent = new Intent(activity, ActivityCustomersDistance.class);
                    intent.putExtra("Code", ID);
                    activity.startActivity(intent);
                }
            });


        }
    }

}
