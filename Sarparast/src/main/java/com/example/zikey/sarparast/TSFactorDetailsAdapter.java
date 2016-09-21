package com.example.zikey.sarparast;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Zikey on 03/07/2016.
 */
public class TSFactorDetailsAdapter extends RecyclerView.Adapter<TSFactorDetailsAdapter.productoViewHolder>{
    private ArrayList<TsFactorDetail> item;

    public TSFactorDetailsAdapter(ArrayList<TsFactorDetail> item) {
        this.item = item;
    }

    @Override
    public productoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_ts_factor_details,viewGroup,false);
        productoViewHolder producto= new productoViewHolder(v);
        return producto;
    }

    @Override
    public void onBindViewHolder(productoViewHolder productoViewHolder, int i) {
        productoViewHolder.txtCodeKala.setText(item.get(i).getCodeKala());
        productoViewHolder.txtNameKala.setText(item.get(i).getNameKala());
        productoViewHolder.txtTedad.setText(""+item.get(i).getTedad());
        productoViewHolder.txtFi.setText(""+item.get(i).getFi());
        productoViewHolder.txtTakhfifD.setText(""+item.get(i).getTakhfifD());
        productoViewHolder.txtPriceKol.setText(""+item.get(i).getPrice());
        productoViewHolder.txtRowNumber.setText(""+item.get(i).getRow());
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class productoViewHolder extends RecyclerView.ViewHolder{
        TextView txtCodeKala,txtNameKala,txtTedad,txtFi,txtTakhfifD,txtPriceKol,txtRowNumber;


        public productoViewHolder(final View itemView) {
            super(itemView);

            txtCodeKala=(TextView)itemView.findViewById(R.id.txtCodeKala);
            txtNameKala=(TextView)itemView.findViewById(R.id.txtCode);
            txtTedad=(TextView)itemView.findViewById(R.id.txtTedad);
            txtFi=(TextView)itemView.findViewById(R.id.txtFi);
            txtTakhfifD=(TextView)itemView.findViewById(R.id.txtTakhfifD);
            txtPriceKol=(TextView)itemView.findViewById(R.id.txtPriceKol);
            txtRowNumber=(TextView)itemView.findViewById(R.id.txtRowNumber);


        }
    }

}
