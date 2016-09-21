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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Zikey on 12/07/2016.
 */
public class RequestsHeaderAdapter extends RecyclerView.Adapter<RequestsHeaderAdapter.productoViewHolder> implements Filterable {

    private ArrayList<RequestsInfo> item;

    private ArrayList<RequestsInfo> itemDump;


    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    private AppCompatActivity activity;


    public RequestsHeaderAdapter(ArrayList<RequestsInfo> item) {
        this.item = item;
        this.itemDump = item;
    }

    @Override
    public productoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_orderedvisitro,viewGroup,false);
        productoViewHolder producto= new productoViewHolder(v);
        return producto;
    }

    @Override
    public void onBindViewHolder(productoViewHolder productoViewHolder, final int i) {
        productoViewHolder. txtCodeName  .setText(""+item.get(i).get_NameBazaryab());
        productoViewHolder. txtKol             .setText(""+item.get(i).get_TedadKol());
        productoViewHolder. txtFactorShode     .setText(""+item.get(i).get_FactorShode());
        productoViewHolder. txtTaeed     .setText(""+item.get(i).get_TedadTaeed());
        productoViewHolder. txtTaeedNashode      .setText(""+item.get(i).get_TedadLaghv());
        productoViewHolder. txtErja            .setText(""+item.get(i).get_TedadBargashti());

        productoViewHolder.code = item.get(i).get_CodeBazaryab();

        productoViewHolder.txtRKol.setText( item.get(i).get_RialTedadKol());
        productoViewHolder.txtRFactorShode.setText(item.get(i).get_RialFactorShode());
        productoViewHolder.txtRTaeedShode.setText(item.get(i).get_RialTedadTaeed());
        productoViewHolder.txtRLaghv.setText(item.get(i).get_RialTedadLaghv());
        productoViewHolder.txtRErja.setText(item.get(i).get_RialTedadBargashti());

    }

    @Override
    public int getItemCount() {
        return item.size();
    }


    @Override
    public Filter getFilter() {

        Filter filter = new RequestHeaderFilter();
        return filter;
    }


    public class  RequestHeaderFilter extends  Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

             ArrayList<RequestsInfo> temp = new ArrayList<>();

            if (TextUtils.isEmpty(charSequence)){
                FilterResults filterResults = new FilterResults();
                filterResults.values = itemDump;
                filterResults.count = temp.size();

                return  filterResults;
            }

            if (itemDump != null && itemDump.size() != 0) {
                for (RequestsInfo item : itemDump) {
                    if (!TextUtils.isEmpty(item.get_NameBazaryab()) && item.get_NameBazaryab().contains(charSequence)) {
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


            ArrayList<RequestsInfo>  temp = null;

            try {
                temp = (ArrayList<RequestsInfo>) filterResults.values;
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
        TextView  txtCodeName,txtKol,txtFactorShode,txtTaeed,txtTaeedNashode,
                  txtErja,txtRKol,txtRFactorShode,txtRTaeedShode,txtRLaghv,txtRErja ;

        private  String code;


        RelativeLayout lyRootr;

        public productoViewHolder(final View itemView) {
            super(itemView);

            txtCodeName   = (TextView) itemView.findViewById(R.id.txtCodeName);
            txtKol              = (TextView) itemView.findViewById(R.id.txtKol     );
            txtFactorShode          = (TextView) itemView.findViewById(R.id.txtFactorShode   );
            txtTaeed         = (TextView) itemView.findViewById(R.id.txtTaeed   );
            txtTaeedNashode          = (TextView) itemView.findViewById(R.id.txtTaeedNashode    );
            txtErja             = (TextView) itemView.findViewById(R.id.txtErja          );

            lyRootr= (RelativeLayout) itemView.findViewById(R.id.lyRoot);

            txtRKol = (TextView) itemView.findViewById(R.id.txtRKol);
            txtRFactorShode = (TextView) itemView.findViewById(R.id.txtRFactorShode);
            txtRTaeedShode = (TextView) itemView.findViewById(R.id.txtRTaeedShode);
            txtRLaghv = (TextView) itemView.findViewById(R.id.txtRLaghv);
            txtRErja = (TextView) itemView.findViewById(R.id.txtRErja);


           lyRootr.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
//
//                    id = Integer.parseInt( txtShomareSefaresh.getText().toString());
//                    ActivitySefareshDetails.Price=txtPrice.getText().toString();
//
//                    G.ID=id;
//                    Fragment_AcceptRequestsDialog myDialog = new Fragment_AcceptRequestsDialog();
//                    myDialog.show(ActivityAcceptRequests.manager,"انتخواب عملیات ");
//                    myDialog.setCancelable(true);
//
                  Intent intent = new Intent(activity,ActivityAcceptRequests.class);
                  intent.putExtra("Code",code);
                   activity.startActivity(intent);
//
                 }
             });


        }
    }

}