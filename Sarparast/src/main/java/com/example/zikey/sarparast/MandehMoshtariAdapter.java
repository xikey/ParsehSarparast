package com.example.zikey.sarparast;

import android.app.AlertDialog;
import android.app.FragmentManager;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;
import com.razanPardazesh.supervisor.CustomerDeptDividedLineFragment;

import java.util.ArrayList;

/**
 * Created by Zikey on 24/07/2016.
 */

public class MandehMoshtariAdapter extends RecyclerView.Adapter<MandehMoshtariAdapter.productoViewHolder> implements Filterable {
    private ArrayList<MandehMoshtariInfo> item;
    private ArrayList<MandehMoshtariInfo> itemDump;

    public int id;
    private String customerCode;
    private String temp;

    public void setManager(FragmentManager manager) {
        this.manager = manager;
    }

    private FragmentManager manager;


    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    private AppCompatActivity activity;


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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_mande_moshtarian, viewGroup, false);
        productoViewHolder producto = new productoViewHolder(v);
        return producto;
    }

    @Override
    public void onBindViewHolder(productoViewHolder productoViewHolder, final int i) {

        productoViewHolder.txtCode.setText(item.get(i).get_Code());
        productoViewHolder.txtName.setText(item.get(i).get_Name());
        productoViewHolder.txtMande.setText(item.get(i).get_Mandeh());
        productoViewHolder.txtVaset.setText(item.get(i).get_Vaset());
        productoViewHolder.txtAddress.setText(item.get(i).get_Address());

        productoViewHolder.L = item.get(i).get_LastL();
        productoViewHolder.W = item.get(i).get_LastW();

        productoViewHolder.tell = item.get(i).get_Tell();
        productoViewHolder.mobile = item.get(i).get_Mobile();

        String count = (productoViewHolder.txtMande.getText().toString());
        if (!count.contains("-")) {
            productoViewHolder.lyButtom.setBackgroundColor(Color.parseColor("#ef9a9a"));
        }
        if (count.contains("-")) {
            productoViewHolder.lyButtom.setBackgroundColor(Color.parseColor("#8f8BC34A"));
        }
        if (count.equals("0")) {
            productoViewHolder.lyButtom.setBackgroundColor(Color.parseColor("#ffffffff"));
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new MandehFilter();
        return filter;
    }

    public class MandehFilter extends Filter {

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
                    if ((!TextUtils.isEmpty(item.get_Vaset())) && item.get_Vaset().contains(charSequence)) {

                        temp.add(item);
                    }
                    if ((!TextUtils.isEmpty(item.get_Address())) && item.get_Address().contains(charSequence)) {

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
        TextView txtCode, txtName, txtMande, txtVaset, txtAddress;

        ImageView btnShowDetails;
        ImageView imgInfo;
        ImageView imgNavigate;
        ImageView imgCall;
        ImageView img5Vijegi;
        ImageView imgLike;
        ImageView imgDislike;
        ImageView imgMandehLine;


        LinearLayout lyButtom;

        String L;
        String W;

        String tell;
        String mobile;

        String ID;

        public productoViewHolder(final View itemView) {
            super(itemView);

            txtCode = (TextView) itemView.findViewById(R.id.txtCode);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtMande = (TextView) itemView.findViewById(R.id.txtMande);
            txtVaset = (TextView) itemView.findViewById(R.id.txtVaset);
            txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);
            lyButtom = (LinearLayout) itemView.findViewById(R.id.lyButtom);
            btnShowDetails = (ImageView) itemView.findViewById(R.id.btnShowDetails);
            imgInfo = (ImageView) itemView.findViewById(R.id.imgInfo);
            imgNavigate = (ImageView) itemView.findViewById(R.id.imgNavigate);
            imgCall = (ImageView) itemView.findViewById(R.id.imgCall);
            img5Vijegi = (ImageView) itemView.findViewById(R.id.img5Vijegi);
            imgLike = (ImageView) itemView.findViewById(R.id.imgLike);
            imgDislike = (ImageView) itemView.findViewById(R.id.imgDislike);
            imgMandehLine = (ImageView) itemView.findViewById(R.id.imgMandehLine);

            imgLike.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {

                    customerCode = txtCode.getText().toString();
                    Intent intent = new Intent(activity, ActivityTopSelledOrReturnedGoods.class);
                    intent.putExtra("code", customerCode);
                    intent.putExtra("Method", "S_GetListOf_Top_Selled_Goods");
                    activity.startActivity(intent);
                }
            });

            imgDislike.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {

                    customerCode = txtCode.getText().toString();
                    Intent intent = new Intent(activity, ActivityTopSelledOrReturnedGoods.class);
                    intent.putExtra("code", customerCode);
                    intent.putExtra("Method", "S_GetListOf_Top_Returned_Goods");
                    activity.startActivity(intent);
                }
            });


            img5Vijegi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Fragment_Customers5Vijegi fragment = new Fragment_Customers5Vijegi();
                    fragment.setActivity(activity);
                    fragment.setName(txtName.getText().toString());
                    fragment.setCode(txtCode.getText().toString());
                    fragment.show(manager, "FiveProperty");

                }
            });

            imgCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tell != null && mobile != null) {

                        if ((tell.equals("0")) && (mobile.equals("0"))) {
                            new AlertDialog.Builder(activity)
                                    .setTitle("خطا")
                                    .setMessage("شماره ای به مشتری اختصاص داده نشده است!")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface arg0, int arg1) {
                                        }
                                    }).create().show();
                        } else {

                            Fragment_CustomersPhoneNumber showDetails = new Fragment_CustomersPhoneNumber();
                            showDetails.setName(txtName.getText().toString());
                            showDetails.setPhone(tell);
                            showDetails.setMobile(mobile);
                            showDetails.setActivity(activity);
                            showDetails.show(manager, "CustomerPhoneNumber");

                            showDetails.setCancelable(true);
                        }
                    }
                }
            });


            btnShowDetails.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    Fragment_MandehMoshtarian showDetails = new Fragment_MandehMoshtarian();
                    showDetails.setID(txtCode.getText().toString());
                    showDetails.show(manager, "Details");

                    showDetails.setCancelable(true);
                }
            });


            imgInfo.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {

                    customerCode = txtCode.getText().toString();
                    Intent intent = new Intent(activity, ActivityCustomersLastOrders.class);
                    intent.putExtra("code", customerCode);
                    activity.startActivity(intent);
                }
            });


            imgNavigate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    temp = L;
                    if (temp.equals("0")) {

                        new AlertDialog.Builder(activity)
                                .setTitle("خطا")
                                .setMessage("مکانی برای این مشتری روی نقشه ثبت نشده است!")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                    }
                                }).create().show();
                    } else {

                        Intent intent = new Intent(activity, ActivityGoogleMap.class);
                        intent.putExtra("state", "NotOrdered");
                        intent.putExtra("Lat", W);
                        intent.putExtra("Long", L);
                        activity.startActivity(intent);

                    }
                }
            });

            imgMandehLine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String code = txtCode.getText().toString();
                    String name = txtName.getText().toString();
                    String mandeh = txtMande.getText().toString();
                    CustomerDeptDividedLineFragment.Show(activity, name, code, mandeh);
                }
            });

        }
    }


}