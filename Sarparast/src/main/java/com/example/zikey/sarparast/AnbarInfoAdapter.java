package com.example.zikey.sarparast;

import android.app.FragmentManager;
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
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Trujas on 05/08/2015.
 */
public class AnbarInfoAdapter extends RecyclerView.Adapter<AnbarInfoAdapter.productoViewHolder> implements Filterable{
    private ArrayList<KalasInfo> item;
    private ArrayList<KalasInfo> itemDump;
//    public  static  int counter=1;
    private AppCompatActivity activity;
    private FragmentManager manager;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setManager(FragmentManager manager) {
        this.manager = manager;
    }

    public AnbarInfoAdapter(ArrayList<KalasInfo> item) {
        this.item = item;
        this.itemDump = item;
    }

    @Override
    public productoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_anbar,viewGroup,false);
        productoViewHolder producto= new productoViewHolder(v);

        return producto;
           }

    @Override
    public void onBindViewHolder(productoViewHolder productoViewHolder, int i) {
        productoViewHolder.txtCode.setText(item.get(i).get_Code());
        productoViewHolder.txtName.setText(item.get(i).get_Name());
        productoViewHolder.txtMojodi.setText(""+item.get(i).get_MojodiFiziki());
        productoViewHolder.txtForosh.setText(""+item.get(i).get_GhabelForosh());

//         if ((Integer.parseInt(m.toString()))%2==0){
//             productoViewHolder.lyRoot02.setBackgroundColor(Color.parseColor("#ffffff"));
//         }
//         if ((Integer.parseInt(m.toString()))%2>0){
//             productoViewHolder.lyRoot02.setBackgroundColor(Color.parseColor("#ECEFF1"));
//         }

        if (item.get(i).get_MojodiFiziki().contains("-")){
            productoViewHolder.lyButtom.setBackgroundColor(Color.parseColor("#e57373"));
        }

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new AnbarInfoFilter();
        return  filter;
    }


    public  class  AnbarInfoFilter extends  Filter{

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<KalasInfo> temp = new ArrayList<>();

            if (TextUtils.isEmpty(charSequence)) {
                FilterResults filterResults = new FilterResults();
                filterResults.values = itemDump;
                filterResults.count = temp.size();

                return filterResults;
            }

            if (itemDump != null && itemDump.size() != 0) {

                for (KalasInfo kala : itemDump) {
                    if (!TextUtils.isEmpty(kala.get_Code()) && kala.get_Code().contains(charSequence)) {
                        temp.add(kala);
                    }


                    if (!TextUtils.isEmpty(kala.get_Name()) && kala.get_Name().contains(charSequence)) {
                        temp.add(kala);
                    }
                }
            }

            FilterResults fR = new FilterResults();
            fR.values=temp;
            fR.count=temp.size();

              return  fR;

            }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ArrayList<KalasInfo> temp = null;

            try {
                temp = (ArrayList<KalasInfo>) filterResults.values;

            }
            catch (Exception E){
                Log.e("Eror","Eror Is "+E);
            }

            if (temp ==null)   temp = new ArrayList<>();

            item = temp;
            notifyDataSetChanged();


        }
    }

    public class productoViewHolder extends RecyclerView.ViewHolder{
        TextView txtCode,txtName,txtMojodi,txtForosh;
        LinearLayout lyButtom;

        ImageView imgVijegiKala;
        ImageView imgInfo;
        ImageView imgSathGheymat;



        public productoViewHolder(final View itemView) {
            super(itemView);

            txtCode=(TextView)itemView.findViewById(R.id.txtCode);
            txtName=(TextView)itemView.findViewById(R.id.txtName);
            txtMojodi=(TextView)itemView.findViewById(R.id.txtMojoodi);
            txtForosh=(TextView)itemView.findViewById(R.id.txtTForoosh);

            imgVijegiKala = (ImageView) itemView.findViewById(R.id.imgVijegiKala);
            imgInfo = (ImageView) itemView.findViewById(R.id.imgInfo);
            imgSathGheymat = (ImageView) itemView.findViewById(R.id.imgSathGheymat);


            lyButtom = (LinearLayout) itemView.findViewById(R.id.lyButtom);


            imgVijegiKala.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Fragment_ProductsTenPropertyOrPrice fragment = new Fragment_ProductsTenPropertyOrPrice();
                    fragment.setActivity(activity);
                    fragment.setName(txtName.getText().toString());
                    fragment.setCode(txtCode.getText().toString());
                    fragment.setChecker("Vijegi");
                    fragment.show(manager,"VijegiKala");

                }
            });

            imgInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Fragment_ProductsKalaInDetails fragment = new Fragment_ProductsKalaInDetails();
                    fragment.setActivity(activity);
                    fragment.setName(txtName.getText().toString());
                    fragment.setCode(txtCode.getText().toString());
                    fragment.show(manager,"VijegiKala");
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
                    fragment.show(manager,"SathGheymat");
                }
            });


        }
    }

}
