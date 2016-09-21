package com.example.zikey.sarparast;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Zikey on 13/07/2016.
 */
public class SefareshDetailsAdapter  extends RecyclerView.Adapter<SefareshDetailsAdapter.productoViewHolder>{
    private ArrayList<TsFactorDetail> item;
    private String FI;

    public SefareshDetailsAdapter(ArrayList<TsFactorDetail> item) {
        this.item = item;
    }

    @Override
    public productoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_sefaresh_details,viewGroup,false);
        productoViewHolder producto= new productoViewHolder(v);
        return producto;
    }

    @Override
    public void onBindViewHolder(productoViewHolder productoViewHolder, int i) {
        productoViewHolder.txtCodeKala.setText(item.get(i).getCodeKala());
        productoViewHolder.txtNameKala.setText(item.get(i).getNameKala());
        productoViewHolder.txtTedad.setText(""+item.get(i).getTedad());
        productoViewHolder.txtFi.setText(""+item.get(i).getFi());
        productoViewHolder.txtPriceKol.setText(""+item.get(i).getPrice());
        productoViewHolder.txtRowNumber.setText(""+item.get(i).getRow());


        FI=  ((item.get(i).getFi()).toString());
        Log.e("yyyy","Fi is "+FI);

          if (FI.contains("-")){
              productoViewHolder.lyRoot.setBackgroundColor(Color.parseColor("#ef9a9a"));
          }
          if (!FI.contains("-")){
              productoViewHolder.lyRoot.setBackgroundColor(Color.parseColor("#FFFFFF"));
          }

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class productoViewHolder extends RecyclerView.ViewHolder{
        TextView txtCodeKala,txtNameKala,txtTedad,txtFi,txtPriceKol,txtRowNumber;
        LinearLayout lyRoot;


        public productoViewHolder(final View itemView) {
            super(itemView);


            txtCodeKala=(TextView)itemView.findViewById(R.id.txtCodeKala);
            txtNameKala=(TextView)itemView.findViewById(R.id.txtCode);
            txtTedad=(TextView)itemView.findViewById(R.id.txtTedad);
            txtFi=(TextView)itemView.findViewById(R.id.txtFi);
            txtPriceKol=(TextView)itemView.findViewById(R.id.txtPriceKol);
            txtRowNumber=(TextView)itemView.findViewById(R.id.txtRowNumber);
            lyRoot = (LinearLayout) itemView.findViewById(R.id.lyRoot);







        }
    }

}
