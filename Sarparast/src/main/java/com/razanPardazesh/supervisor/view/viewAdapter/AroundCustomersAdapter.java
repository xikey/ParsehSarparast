package com.razanPardazesh.supervisor.view.viewAdapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zikey.sarparast.R;
import com.razanPardazesh.supervisor.AroundMeCustomerInfoFragment;
import com.razanPardazesh.supervisor.model.CustomerInfo;

import java.util.ArrayList;

/**
 * Created by Zikey on 06/03/2017.
 */

public class AroundCustomersAdapter extends RecyclerView.Adapter<AroundCustomersAdapter.CustomerHolder> {

    private ArrayList<CustomerInfo> item;

    public AroundCustomersAdapter(FragmentActivity context) {
        this.context = context;
    }

    private FragmentActivity context;

    public void setItem(ArrayList<CustomerInfo> item) {

        if (item == null)
            return;
        this.item = item;
        notifyDataSetChanged();
    }


    public void clearList() {

        if (this.item == null)
            return;

        if (this.item.size() == 0)
            return;

        this.item.clear();
        notifyDataSetChanged();
    }

    @Override
    public CustomerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent == null || parent.getContext() == null)
            return null;

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.around_all_customer_item, parent, false);
        CustomerHolder holder = new CustomerHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CustomerHolder holder, final int position) {

        holder.txtName.setText(item.get(position).getCustomerName());

        holder.lyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AroundMeCustomerInfoFragment.Show(context, item.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {

        if (item == null)
            return 0;

        return item.size();
    }


    public class CustomerHolder extends RecyclerView.ViewHolder {

        private TextView txtName;
        private LinearLayout lyDetails;


        public CustomerHolder(View v) {
            super(v);

            txtName = (TextView) v.findViewById(R.id.txtName);
            lyDetails = (LinearLayout) v.findViewById(R.id.lyDetails);

        }
    }
}
