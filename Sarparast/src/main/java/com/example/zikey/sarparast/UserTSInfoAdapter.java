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
 * Created by Trujas on 05/08/2015.
 */
public class UserTSInfoAdapter extends RecyclerView.Adapter<UserTSInfoAdapter.productoViewHolder> implements Filterable{
    private ArrayList<TSFactorHeader> item;
    private ArrayList<TSFactorHeader> itemDump;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    private AppCompatActivity activity;
//    public  static  int counter=1;

    public UserTSInfoAdapter(ArrayList<TSFactorHeader> item) {
        this.item = item;
        this.itemDump = item;
    }

    @Override
    public productoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_ts_visitorinfo,viewGroup,false);
        productoViewHolder producto= new productoViewHolder(v);

        return producto;
           }

    @Override
    public void onBindViewHolder(productoViewHolder productoViewHolder, int i) {
        productoViewHolder.txtFactorNumber.setText(item.get(i).getSh_factro());
        productoViewHolder.txtDate.setText(item.get(i).getDate());
        productoViewHolder.txtName.setText(""+item.get(i).getName_moshtari());
        productoViewHolder.txtTSPrice.setText(""+item.get(i).getTs_price());
        productoViewHolder.imgFactorDetail.setImageResource(item.get(i).getImgFactorDetail());
        productoViewHolder.imgAghlam.setImageResource(item.get(i).getImgAghlam());
//
//        Log.e("countera","counter is "+counter%2);
//        Log.e("countera","counter is "+i);
//       counter=i;
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public Filter getFilter() {
        Filter f= new UserTsInfoFilter();
        return f;
    }

    public class UserTsInfoFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<TSFactorHeader> temp = new ArrayList<>();

            if (TextUtils.isEmpty(charSequence))
            {
                FilterResults filter = new FilterResults();
                filter.values = itemDump;
                filter.count = temp.size();

                return filter;
            }

            if (itemDump != null && itemDump.size() != 0) {
                for (TSFactorHeader item : itemDump) {

                    if (!TextUtils.isEmpty(item.getName_moshtari()) && item.getName_moshtari().contains(charSequence)) {
                        temp.add(item);
                    }

                    if (!TextUtils.isEmpty(item.getSh_factro()) && item.getSh_factro().contains(charSequence)) {
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

            ArrayList<TSFactorHeader>  temp = null;

            try {
                temp = (ArrayList<TSFactorHeader>) filterResults.values;
            }
            catch (Exception e)
            {
                Log.e("mohsen: ",e.toString());
            }

            if (temp == null)
                temp = new ArrayList<TSFactorHeader>();

            item = temp;

            notifyDataSetChanged();
        }
    }

    public class productoViewHolder extends RecyclerView.ViewHolder{
        TextView txtFactorNumber,txtDate,txtName,txtTSPrice;
        ImageView imgFactorDetail,imgAghlam;
        RelativeLayout lyRoot02;

        public productoViewHolder(final View itemView) {
            super(itemView);

            txtFactorNumber=(TextView)itemView.findViewById(R.id.txtRow);
            txtDate=(TextView)itemView.findViewById(R.id.txtCode);
            txtName=(TextView)itemView.findViewById(R.id.txtName);
            txtTSPrice=(TextView)itemView.findViewById(R.id.txtTSPrice);
            imgFactorDetail = (ImageView) itemView.findViewById(R.id.imgFactorDetail);
            imgAghlam = (ImageView) itemView.findViewById(R.id.imgAghlam);
            lyRoot02= (RelativeLayout) itemView.findViewById(R.id.lyRoot02);

//             if ((counter%2)==0){
////                Log.e("counter","counter is "+counter%2);
//                lyRoot02.setBackgroundColor(0x0f00f000 );
//            }
//            else if ((counter%2)==1){
//                lyRoot02.setBackgroundColor(0x0f000000 );
//            }

            imgFactorDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity,ActiviyListOfTSVisitorInfoFactorinfo.class);
                    intent.putExtra("id_header",txtFactorNumber.getText().toString());
                    activity.startActivity(intent);

                }
            });

            imgAghlam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity,ActivityListOfTSVisitorInfoFactorDetails.class);
                    intent.putExtra("id_header",txtFactorNumber.getText().toString());
                    intent.putExtra("price_tasvie_nashode",txtTSPrice.getText().toString());

                    activity.startActivity(intent);

                }
            });

                    }
    }

}
