package com.example.zikey.sarparast;

import android.app.FragmentManager;
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
 * Created by Zikey
 * on 05/08/2015.
 */
public class SarjamBarAdapter extends RecyclerView.Adapter<SarjamBarAdapter.productoViewHolder> implements Filterable {
    public ArrayList<SarjamInfo> item;
    public ArrayList<SarjamInfo> itemDump;

    //state -1 is VISITOR
    //state 1 is Kala

    private int state;
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

    public SarjamBarAdapter(ArrayList<SarjamInfo> item) {
        this.item = item;
        this.itemDump = item;
    }

    @Override
    public productoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_sarjam, viewGroup, false);
        productoViewHolder producto = new productoViewHolder(v);

        return producto;
    }

    @Override
    public void onBindViewHolder(productoViewHolder productoViewHolder, int i) {

        productoViewHolder.txtName.setText("" + item.get(i).get_Name());

        productoViewHolder.txtCode.setText("" + item.get(i).get_Code());
        productoViewHolder.txtSefaresh.setText("" + item.get(i).get_Sefaresh());
        productoViewHolder.txtFactor.setText("" + item.get(i).get_Factor());
        productoViewHolder.txtRSefaresh.setText("" + item.get(i).get_PriceSefaresh());
        productoViewHolder.txtRFactor.setText("" + item.get(i).get_PriceFactor());


        if (state == -1) {
            productoViewHolder.lyButtomVisitor.setVisibility(View.VISIBLE);
        } else if (state == 1) {
            productoViewHolder.lyButtom.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }


    public class SarjamBarFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<SarjamInfo> temp = new ArrayList<>();

            if (TextUtils.isEmpty(charSequence)) {

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemDump;
                filterResults.count = temp.size();

                return filterResults;
            }

            if (itemDump != null && itemDump.size() != 0) {

                for (SarjamInfo item : itemDump) {

                    if (!TextUtils.isEmpty(item.get_Name()) && item.get_Name().contains(charSequence)) {
                        temp.add(item);
                    }

                    if (!TextUtils.isEmpty(item.get_Code()) && item.get_Code().contains(charSequence)) {
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

            ArrayList<SarjamInfo> temp = null;

            try {
                temp = (ArrayList<SarjamInfo>) filterResults.values;
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
        TextView txtCode, txtName, txtSefaresh, txtFactor, txtRSefaresh, txtRFactor;
        private LinearLayout lyButtom;
        private LinearLayout lyButtomVisitor;
        private ImageView imgVijegiKala;
        private ImageView imgInfo;
        private ImageView imgDetails;
        private ImageView imgSathGheymat;


        public productoViewHolder(final View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtCode = (TextView) itemView.findViewById(R.id.txtCode);
            txtFactor = (TextView) itemView.findViewById(R.id.txtFactor);
            txtSefaresh = (TextView) itemView.findViewById(R.id.txtSefaresh);
            txtRSefaresh = (TextView) itemView.findViewById(R.id.txtRSefaresh);
            txtRFactor = (TextView) itemView.findViewById(R.id.txtRFactor);

            imgVijegiKala = (ImageView) itemView.findViewById(R.id.imgVijegiKala);
            imgInfo = (ImageView) itemView.findViewById(R.id.imgInfo);
            imgSathGheymat = (ImageView) itemView.findViewById(R.id.imgSathGheymat);
            imgDetails = (ImageView) itemView.findViewById(R.id.imgDetails);

            lyButtom = (LinearLayout) itemView.findViewById(R.id.lyButtom);
            lyButtomVisitor = (LinearLayout) itemView.findViewById(R.id.lyButtomVisitor);

            imgVijegiKala.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Fragment_ProductsTenPropertyOrPrice fragment = new Fragment_ProductsTenPropertyOrPrice();
                    fragment.setActivity(activity);
                    fragment.setName(txtName.getText().toString());
                    fragment.setCode(txtCode.getText().toString());
                    fragment.setChecker("Vijegi");
                    fragment.show(manager, "VijegiKala");

                }
            });

            imgInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Fragment_ProductsKalaInDetails fragment = new Fragment_ProductsKalaInDetails();
                    fragment.setActivity(activity);
                    fragment.setName(txtName.getText().toString());
                    fragment.setCode(txtCode.getText().toString());
                    fragment.show(manager, "VijegiKala");
                }
            });

            imgSathGheymat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Fragment_ProductsTenPropertyOrPrice fragment = new Fragment_ProductsTenPropertyOrPrice();
                    fragment.setActivity(activity);
                    fragment.setName(txtName.getText().toString());
                    fragment.setCode(txtCode.getText().toString());
                    fragment.setChecker("Gheymat");
                    fragment.show(manager, "SathGheymat");
                }
            });


            imgDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivitySarjamDetails.start(activity, "Kala", txtCode.getText().toString());
                }
            });


        }
    }
}

