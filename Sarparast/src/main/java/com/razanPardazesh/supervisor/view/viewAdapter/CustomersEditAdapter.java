package com.razanPardazesh.supervisor.view.viewAdapter;

import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zikey.sarparast.R;
import com.razanPardazesh.supervisor.EditedCustomerActivity;
import com.razanPardazesh.supervisor.model.CustomerRequestEdit;

import java.util.ArrayList;

/**
 * Created by Zikey on 12/02/2017.
 */

public class CustomersEditAdapter extends RecyclerView.Adapter<CustomersEditAdapter.EditedListViewHolder> {

    ArrayList<CustomerRequestEdit> item;
    private static final int STATUS_CHANGE_REQUEST_CODE = 14;
    FragmentActivity context;


    public CustomersEditAdapter(FragmentActivity context) {

        this.context = context;
    }

    public void addItem(ArrayList<CustomerRequestEdit> items) {

        if (items == null)
            return;

        if (items.size() == 0)
            return;

        if (this.item == null)
            this.item = new ArrayList<>();

        this.item.addAll(items);
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
    public EditedListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent == null || parent.getContext() == null)
            return null;

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_edited_list_item, parent, false);
        EditedListViewHolder holder = new EditedListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(EditedListViewHolder holder, final int position) {

        final long id = item.get(position).getId();
        String name = item.get(position).getCustomerName();
        String family = item.get(position).getCustomerFamily();

        holder.txtVisitorName.setText(item.get(position).getVisitorName());
        holder.txtCustomerName.setText(customerFullName(name, family));
        int status = item.get(position).getStatus();
        holder.txtStatus.setText(decodeStatusCode(status));
        holder.imgStatus2.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_circle_storke));
        holder.imgStatus1.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_circle_storke));
        if (status == 1) {
            holder.imgStatus1.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_circle_green));
        }
        if (status == 2) {
            holder.imgStatus2.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_circle_red));
        }
        holder.lyRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditedCustomerActivity.start(context, item.get(position), STATUS_CHANGE_REQUEST_CODE);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (item == null)
            return 0;

        return item.size();
    }

    public class EditedListViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgStatus2;
        private ImageView imgStatus1;
        private TextView txtVisitorName;
        private TextView txtCustomerName;
        private TextView txtStatus;
        private RelativeLayout lyRoot;


        public EditedListViewHolder(View v) {
            super(v);

            imgStatus2 = (ImageView) v.findViewById(R.id.imgStatus2);
            imgStatus1 = (ImageView) v.findViewById(R.id.imgStatus1);
            txtVisitorName = (TextView) v.findViewById(R.id.txtVisitorName);
            txtCustomerName = (TextView) v.findViewById(R.id.txtCustomerName);
            txtStatus = (TextView) v.findViewById(R.id.txtStatus);
            lyRoot = (RelativeLayout) v.findViewById(R.id.lyRoot);


        }
    }

    private String decodeStatusCode(int code) {
        if (code == 1)
            return "تایید شده";
        if (code == 2)
            return "تایید نشده";

        return "در انتظار";

    }

    private String customerFullName(String name, String family) {

        if ((name != null || !TextUtils.isEmpty(name)) && (family != null | !TextUtils.isEmpty(family)))
            return (name + " " + family);

        if ((name != null || !TextUtils.isEmpty(name)))
            return name;

        if ((family != null | !TextUtils.isEmpty(family)))
            return family;

        return "";
    }
}
