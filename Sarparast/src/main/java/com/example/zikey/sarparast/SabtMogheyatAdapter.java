package com.example.zikey.sarparast;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import com.example.zikey.sarparast.Helpers.PreferenceHelper;

import java.util.ArrayList;

/**
 * Created by Zikey on 24/07/2016.
 */

public class SabtMogheyatAdapter extends RecyclerView.Adapter<SabtMogheyatAdapter.productoViewHolder> implements Filterable {
    private ArrayList<MandehMoshtariInfo> item;
    private ArrayList<MandehMoshtariInfo> itemDump;

    public int id;

    public void setCommunicator(GetMyLocationCommunicator communicator) {
        this.communicator = communicator;
    }

    GetMyLocationCommunicator communicator;

    private AppCompatActivity activity;

    public void setL(double l) {
        L = l;
    }

    private double L;

    public void setW(double w) {
        W = w;
    }

    private double W;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setCustomer(ArrayList<MandehMoshtariInfo> item) {
        this.item = item;
        notifyDataSetChanged();
        //  this.itemDump = item;
    }

    public void addCustomer(ArrayList<MandehMoshtariInfo> item) {
        this.item.addAll(item);
        // this.itemDump.addAll(item);
        notifyDataSetChanged();
    }

    @Override
    public productoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_hame_moshtarian, viewGroup, false);
        productoViewHolder producto = new productoViewHolder(v);
        return producto;
    }

    @Override
    public void onBindViewHolder(productoViewHolder productoViewHolder, final int i) {

        productoViewHolder.txtCode.setText("" + item.get(i).get_Code());
        productoViewHolder.txtName.setText("" + item.get(i).get_Name());
        productoViewHolder.txtAddress.setText("" + item.get(i).get_Address());


        if (item.get(i).get_LastL().toString() != null) {

            productoViewHolder.lastL = item.get(i).get_LastL().toString();

            if (productoViewHolder.lastL.equals("0")) {

                productoViewHolder.lyRootReq.setBackgroundColor(Color.parseColor("#5fFFEB3B"));
            }

            if (!productoViewHolder.lastL.equals("0")) {

                productoViewHolder.lyRootReq.setBackgroundColor(Color.parseColor("#ffffff"));
            }

        }

        if (item.get(i).get_LastW().toString() != null) {

            productoViewHolder.lastW = item.get(i).get_LastW().toString();
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public Filter getFilter() {

        Filter filter = new SabtMogheyatFilter();
        return filter;
    }

    public class SabtMogheyatFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<MandehMoshtariInfo> temp = new ArrayList<>();

            if (TextUtils.isEmpty(charSequence)) {

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemDump;
                filterResults.count = temp.size();

                return filterResults;
            }

            if (itemDump != null && itemDump.size() != 0) {
                for (MandehMoshtariInfo item : itemDump) {
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

            ArrayList<MandehMoshtariInfo> temp = null;

            try {
                temp = (ArrayList<MandehMoshtariInfo>) filterResults.values;
            } catch (Exception e) {

                Log.e("Eror: ", e.toString());
            }

            if (temp == null) temp = new ArrayList<>();

            item = temp;

            notifyDataSetChanged();

        }
    }


    public class productoViewHolder extends RecyclerView.ViewHolder {
        TextView txtCode, txtName, txtAddress;

        private String lastL;
        private String lastW;

//        ImageView btnShowDetails;
        ImageView imgSetLocation;
//        ImageView imgPastLocation;

        RelativeLayout lyRootReq;


//        RelativeLayout lyRootr;

        public productoViewHolder(final View itemView) {
            super(itemView);

            txtCode = (TextView) itemView.findViewById(R.id.txtCode);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);

            lyRootReq = (RelativeLayout) itemView.findViewById(R.id.lyRootReq);

//            btnShowDetails = (ImageView) itemView.findViewById(R.id.btnShowDetails);
            imgSetLocation = (ImageView) itemView.findViewById(R.id.imgSetLocation);
//            imgPastLocation = (ImageView) itemView.findViewById(R.id.imgPastLocation);

//            lyRootr = (RelativeLayout) itemView.findViewById(R.id.lyRootReq);


//            btnShowDetails.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//
//                    Intent intent = new Intent(activity, ActivityGoogleMap.class);
//                    intent.putExtra("state", "MyNearCustomers");
//                    activity.startActivity(intent);
//
//                }
//            });


//            imgPastLocation.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//
//                    if (!lastL.equals("0") || !lastW.equals("0")) {
//                        Intent intent = new Intent(activity, ActivityGoogleMap.class);
//                        intent.putExtra("state", "CustomerPastLocation");
//                        intent.putExtra("Lat", lastW);
//                        intent.putExtra("Long", lastL);
//
//                        activity.startActivity(intent);
//                    } else {
//
//                        new AlertDialog.Builder(activity)
//                                .setCancelable(false)
//                                .setTitle("خطا")
//                                .setMessage("موقعیت این مشتری ثبت نشده است")
//                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//
//                                    }
//                                })
//                                .setIcon(R.drawable.eror_dialog)
//                                .show();
//                    }
//                }
//            });

            imgSetLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                     ActivitySetCustomerLocationMap.start(activity,Double.valueOf(lastW), Double.valueOf(lastL),txtName.getText().toString(),txtCode.getText().toString(),1200);
                }
            });


        }


    }


}