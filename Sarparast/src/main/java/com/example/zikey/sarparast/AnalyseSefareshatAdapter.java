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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Zikey on 01/08/2016.
 */
public class AnalyseSefareshatAdapter extends RecyclerView.Adapter<AnalyseSefareshatAdapter.productoViewHolder> implements Filterable {

    public void setAct(AppCompatActivity act) {
        this.act = act;
    }

    public void setCgroup(String cgroup) {
        Cgroup = cgroup;
    }

    String Cgroup;

    private AppCompatActivity act;
    private FragmentManager manager;
    private Fragment_NahveVosool fragmentNahveVosool;
    private FragmentAnalyseSefareshDetails fragmentAnalyseSefareshDetails;

    public void setState(int state) {
        this.state = state;
    }

    // state for get layout name.  cus VISITOR has 3 buttron
    private int state = 0;

    private ArrayList<AnalyseSefareshatInfo> item;
    private ArrayList<AnalyseSefareshatInfo> itemDump;


    public AnalyseSefareshatAdapter(ArrayList<AnalyseSefareshatInfo> item) {
        this.item = item;
        this.itemDump = item;
    }

    @Override
    public productoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_foroosh_analyse, viewGroup, false);
        productoViewHolder producto = new productoViewHolder(v);
        return producto;
    }

    @Override
    public void onBindViewHolder(productoViewHolder productoViewHolder, int i) {

        productoViewHolder.txtName.setText(item.get(i).get_Name());
        productoViewHolder.txtTForosh.setText(item.get(i).get_TForosh());
        productoViewHolder.txtRForosh.setText(item.get(i).get_RForoosh());
        productoViewHolder.txtTBargasht.setText(item.get(i).get_TBargasht());
        productoViewHolder.txtKhales.setText(item.get(i).get_KhalesForosh());
        productoViewHolder.txtRBargasht.setText(item.get(i).get_RBargasht());
        productoViewHolder.groupCode = item.get(i).get_GroupCode().toString();

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public Filter getFilter() {

        Filter filter = new AnalyseSefareshatFilter();
        return filter;


    }

    public class AnalyseSefareshatFilter extends Filter {


        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<AnalyseSefareshatInfo> temp = new ArrayList<>();

            if (TextUtils.isEmpty(charSequence)) {

                FilterResults filterResults = new FilterResults();
                filterResults.values = itemDump;
                filterResults.count = temp.size();

                return filterResults;
            }

            if (itemDump != null && itemDump.size() != 0) {
                for (AnalyseSefareshatInfo item : itemDump) {
                    if ((!TextUtils.isEmpty(item.get_Name())) && item.get_Name().contains(charSequence)) {

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

            ArrayList<AnalyseSefareshatInfo> temp = null;

            try {
                temp = (ArrayList<AnalyseSefareshatInfo>) filterResults.values;

            } catch (Exception e) {

                Log.e("Eror: ", e.toString());
            }

            if (temp == null) temp = new ArrayList<>();

            item = temp;

            notifyDataSetChanged();
        }
    }

    public class productoViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtTForosh, txtRForosh, txtTBargasht, txtRBargasht, txtKhales;
        ImageView imgNahve, btnShowDetails, btnTarget;
        RelativeLayout lyRoot;
        String groupCode;

        public productoViewHolder(final View itemView) {
            super(itemView);


            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtTForosh = (TextView) itemView.findViewById(R.id.txtTForosh);
            txtRForosh = (TextView) itemView.findViewById(R.id.txtRForosh);
            txtTBargasht = (TextView) itemView.findViewById(R.id.txtTBargasht);
            txtRBargasht = (TextView) itemView.findViewById(R.id.txtRBargasht);
            txtKhales = (TextView) itemView.findViewById(R.id.txtKhales);

            imgNahve = (ImageView) itemView.findViewById(R.id.imgNahve);
            btnShowDetails = (ImageView) itemView.findViewById(R.id.btnShowDetails);
            btnTarget = (ImageView) itemView.findViewById(R.id.imgTarget);

            if (state == 1) {
                btnTarget.setVisibility(View.VISIBLE);
            }
            lyRoot = (RelativeLayout) itemView.findViewById(R.id.lyRoot);


            imgNahve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    manager = act.getFragmentManager();
                    fragmentNahveVosool = new Fragment_NahveVosool();
                    fragmentNahveVosool.setGroupCode(groupCode);
                    fragmentNahveVosool.setGroupName("نحوه وصول ـ "+txtName.getText().toString());
                    fragmentNahveVosool.setCgroup(Cgroup);
                    fragmentNahveVosool.setActivity(act);

                    fragmentNahveVosool.show(manager, "NahveVosol");
                }
            });

            btnTarget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    manager = act.getFragmentManager();
                    FragmentAnalyseSefareshTargets analyseSefareshTargets = new FragmentAnalyseSefareshTargets();
                    analyseSefareshTargets.setActivity(act);
                    analyseSefareshTargets.setGroupCode(groupCode);
                    analyseSefareshTargets.setGroupName(" تارگت ـ "+txtName.getText().toString());
                    analyseSefareshTargets.show(manager,"targets");


                }
            });

            btnShowDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    manager = act.getFragmentManager();
                    fragmentAnalyseSefareshDetails = new FragmentAnalyseSefareshDetails();
                    fragmentAnalyseSefareshDetails.setGroupCode(groupCode);
                    fragmentAnalyseSefareshDetails.setGroupName("گزارش فروش ـ"+txtName.getText().toString());
                    fragmentAnalyseSefareshDetails.setCgroup(Cgroup);
                    fragmentAnalyseSefareshDetails.setActivity(act);

                    fragmentAnalyseSefareshDetails.show(manager, "Details");

                }
            });

        }
    }


}
