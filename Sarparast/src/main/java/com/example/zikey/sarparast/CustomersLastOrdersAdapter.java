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
public class CustomersLastOrdersAdapter extends RecyclerView.Adapter<CustomersLastOrdersAdapter.productoViewHolder> implements Filterable {

    private ArrayList<CustomersInfo> item;

    private ArrayList<CustomersInfo> itemDump;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    private AppCompatActivity activity;

    public CustomersLastOrdersAdapter(ArrayList<CustomersInfo> item) {
        this.item = item;
        this.itemDump = item;
    }

    @Override
    public productoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_customers_last_orders,viewGroup,false);
        productoViewHolder producto= new productoViewHolder(v);
        return producto;
    }

    @Override
    public void onBindViewHolder(productoViewHolder productoViewHolder, final int i) {
        productoViewHolder. txtPeygiri  .setText(""+item.get(i).get_IDHeaeder());
        productoViewHolder. txtDate             .setText(""+item.get(i).get_Date());
        productoViewHolder. txtPrice     .setText(""+item.get(i).get_Price());
        productoViewHolder. txtTasvie     .setText(""+item.get(i).get_TasvieNashode());
        productoViewHolder. txtNaghd      .setText(""+item.get(i).get_Naghd());
        productoViewHolder. txtCheque            .setText(""+item.get(i).get_Cheque());
        productoViewHolder. txtVariz            .setText(""+item.get(i).get_Variz());
        productoViewHolder. txtElamie            .setText(""+item.get(i).get_Elamie());
        productoViewHolder. txtTakhfif            .setText(""+item.get(i).get_Takhfif());
        productoViewHolder. txtBargasht            .setText(""+item.get(i).get_Bargashti());

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
        TextView  txtPeygiri,txtDate,txtPrice,txtNaghd,txtVariz,txtTasvie,txtCheque,txtElamie,txtBargasht,txtTakhfif ;

        ImageView imgAghlam;
        ImageView imgfactor;

        private  String code;

        RelativeLayout lyRootr;

        public productoViewHolder(final View itemView) {
            super(itemView);

            txtPeygiri    = (TextView) itemView.findViewById(R.id.txtPeygiri );
            txtDate       = (TextView) itemView.findViewById(R.id.txtDate    );
            txtPrice      = (TextView) itemView.findViewById(R.id.txtPrice   );
            txtNaghd      = (TextView) itemView.findViewById(R.id.txtNaghd   );
            txtVariz      = (TextView) itemView.findViewById(R.id.txtVariz   );
            txtTasvie     = (TextView) itemView.findViewById(R.id.txtTasvie  );
            txtCheque     = (TextView) itemView.findViewById(R.id.txtCheque  );
            txtElamie     = (TextView) itemView.findViewById(R.id.txtElamie  );
            txtBargasht   = (TextView) itemView.findViewById(R.id.txtBargasht);
            txtTakhfif    = (TextView) itemView.findViewById(R.id.txtTakhfif );

            imgAghlam    = (ImageView) itemView.findViewById(R.id.imgAghlam );
            imgfactor    = (ImageView) itemView.findViewById(R.id.imgfactor );

            imgfactor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity,ActiviyListOfTSVisitorInfoFactorinfo.class);
                    intent.putExtra("id_header",txtPeygiri.getText().toString());
                   activity.startActivity(intent);

                }
            });

            imgAghlam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity,ActivityListOfTSVisitorInfoFactorDetails.class);
                    intent.putExtra("id_header",txtPeygiri.getText().toString());
                    intent.putExtra("price_tasvie_nashode",txtTasvie.getText().toString());

                   activity.startActivity(intent);

                }
            });
         //   lyRootr= (RelativeLayout) itemView.findViewById(R.id.lyRoot);

        }
    }

}