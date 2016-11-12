package com.razanPardazesh.supervisor.view.viewAdapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
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

import com.example.zikey.sarparast.AdamVisitInfo;
import com.example.zikey.sarparast.Helpers.FontApplier;
import com.example.zikey.sarparast.R;
import com.razanPardazesh.supervisor.model.Report;
import com.razanPardazesh.supervisor.model.user.Visitor;

import java.util.ArrayList;

/**
 * Created by Zikey on 09/11/2016.
 */

public class CoveragePercentAdapter extends RecyclerView.Adapter<CoveragePercentAdapter.VisitorsViewHolder> implements Filterable {
    public ArrayList<Report> item;
    public ArrayList<Report> itemDump;
    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private Visitor user = new Visitor();


    public void setItem(ArrayList<Report> item) {

        if (item == null) return;
        this.item = item;
        this.itemDump = item;
        notifyDataSetChanged();
    }


    @Override
    public VisitorsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_coverage_percentage, viewGroup, false);
        VisitorsViewHolder visitors = new VisitorsViewHolder(v);

        return visitors;
    }

    @Override
    public void onBindViewHolder(VisitorsViewHolder productoViewHolder, int i) {

        if (item.get(i).getUser() instanceof Visitor) {
            user = (Visitor) item.get(i).getUser();

            productoViewHolder.txtName.setText("" + user.getName().toString());
            productoViewHolder.txtCode.setText("" + user.getCode().toString());
        }
        productoViewHolder.txtTotal.setText("" + item.get(i).getTotalCustomers());
        productoViewHolder.txtAdamSefaresh.setText("" + item.get(i).getCantOrderCount());
        productoViewHolder.txtCovered.setText("" + item.get(i).getOrderCount());
        productoViewHolder.txtnotVisited.setText("" + item.get(i).getNotVisited());
        long covered =   (item.get(i).getTotalCustomers()-item.get(i).getNotVisited());
        productoViewHolder.txtpercent.setText(""+(coverageProceed(item.get(i).getTotalCustomers(),item.get(i).getNotVisited()))+"%");
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class VisitorsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<Report> temp = new ArrayList<>();

            if (TextUtils.isEmpty(charSequence)) {

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemDump;
                filterResults.count = temp.size();

                return filterResults;
            }

            if (itemDump != null && itemDump.size() != 0) {

                for (Report item : itemDump) {

                    Visitor visitor = new Visitor();
                    visitor = (Visitor) item.getUser();

                   if (!TextUtils.isEmpty(visitor.getName()) && visitor.getName().contains(charSequence)) {
                        temp.add(item);
                    }
                    if (!TextUtils.isEmpty(visitor.getCode().toString()) && visitor.getCode().toString().contains(charSequence)) {
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

            ArrayList<Report> temp = null;

            try {
                temp = (ArrayList<Report>) filterResults.values;
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

        Filter filter = new CoveragePercentAdapter.VisitorsFilter();
        return filter;
    }

    public class VisitorsViewHolder extends RecyclerView.ViewHolder {
        TextView txtCode, txtName, txtTotal, txtCovered, txtAdamSefaresh, txtnotVisited,txtpercent;
        RelativeLayout lyHead;


        public VisitorsViewHolder(final View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtCode = (TextView) itemView.findViewById(R.id.txtCode);
            txtTotal = (TextView) itemView.findViewById(R.id.txtTotal);
            txtCovered = (TextView) itemView.findViewById(R.id.txtCovered);
            txtAdamSefaresh = (TextView) itemView.findViewById(R.id.txtAdamSefaresh);
            txtnotVisited = (TextView) itemView.findViewById(R.id.txtnotVisited);
            txtpercent = (TextView) itemView.findViewById(R.id.txtpercent);
            lyHead = (RelativeLayout) itemView.findViewById(R.id.lyHead);

            FontApplier.applyMainFont(context,lyHead);


        }
    }

    private int coverageProceed(Long total, Long visited) {

        if (total == 0) return 0;
        return (int) ((((float) (total - visited)) / total) * 100);

    }


}

