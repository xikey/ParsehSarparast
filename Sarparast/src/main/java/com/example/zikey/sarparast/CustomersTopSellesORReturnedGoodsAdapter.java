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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Zikey on 12/07/2016.
 */
public class CustomersTopSellesORReturnedGoodsAdapter extends RecyclerView.Adapter<CustomersTopSellesORReturnedGoodsAdapter.productoViewHolder> implements Filterable {

    private ArrayList<CustomersInfo> item;

    private ArrayList<CustomersInfo> itemDump;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    private AppCompatActivity activity;

    public CustomersTopSellesORReturnedGoodsAdapter(ArrayList<CustomersInfo> item) {
        this.item = item;
        this.itemDump = item;
    }

    @Override
    public productoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_top_returned_or_selles_goods,viewGroup,false);
        productoViewHolder producto= new productoViewHolder(v);
        return producto;
    }

    @Override
    public void onBindViewHolder(productoViewHolder productoViewHolder, final int i) {

        productoViewHolder. txtRowNumber  .setText(""+item.get(i).get_Row());
        productoViewHolder. txtCode             .setText(""+item.get(i).get_IDHeaeder());
        productoViewHolder. txtName     .setText(""+item.get(i).get_Name());
        productoViewHolder. txtPrice     .setText(""+item.get(i).get_Price());

       // productoViewHolder.code = item.get(i).get_CodeBazaryab();

    }

    @Override
    public int getItemCount() {
        return item.size();
    }


    @Override
    public Filter getFilter() {

        Filter filter = new OrdersrFilter();
        return filter;
    }


    public class  OrdersrFilter extends  Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

             ArrayList<CustomersInfo> temp = new ArrayList<>();

            if (TextUtils.isEmpty(charSequence)){
                FilterResults filterResults = new FilterResults();
                filterResults.values = itemDump;
                filterResults.count = temp.size();

                return  filterResults;
            }

            if (itemDump != null && itemDump.size() != 0) {
                for (CustomersInfo item : itemDump) {
                    if (!TextUtils.isEmpty(item.get_IDHeaeder()) && item.get_IDHeaeder().contains(charSequence)) {
                        temp.add(item);
                    }
                  }
                }

            FilterResults filterResults = new FilterResults();

            filterResults.values = temp;
            filterResults.count = temp.size();

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            ArrayList<CustomersInfo>  temp = null;

            try {
                temp = (ArrayList<CustomersInfo>) filterResults.values;
            }
            catch (Exception e)
            {
                Log.e("mohsen: ",e.toString());
            }


            if (temp == null)
                temp = new ArrayList<>();

            item = temp;

            notifyDataSetChanged();
        }
    }

    public class productoViewHolder extends RecyclerView.ViewHolder{
        TextView  txtRowNumber,txtCode,txtName,txtPrice ;

         private  String code;

        RelativeLayout lyRootr;

        public productoViewHolder(final View itemView) {
            super(itemView);

            txtRowNumber    = (TextView) itemView.findViewById(R.id.txtRowNumber );
            txtCode       = (TextView) itemView.findViewById(R.id.txtCode    );
            txtName      = (TextView) itemView.findViewById(R.id.txtName   );
            txtPrice      = (TextView) itemView.findViewById(R.id.txtPrice   );

        }

    }

}