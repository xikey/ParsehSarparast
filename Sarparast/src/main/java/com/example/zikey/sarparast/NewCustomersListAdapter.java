package com.example.zikey.sarparast;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Trujas on 05/08/2015.
 */
public class NewCustomersListAdapter extends RecyclerView.Adapter<NewCustomersListAdapter.productoViewHolder> implements Filterable{
    private ArrayList<CustomersInfo> item;
    private ArrayList<CustomersInfo> itemDump;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    private AppCompatActivity activity;
//    public  static  int counter=1;

    public void setItem(ArrayList<CustomersInfo> item) {
        this.item = item;

    }


    AppCompatActivity act ;

    public NewCustomersListAdapter(ArrayList<CustomersInfo> item) {
        this.item = item;
        this.itemDump = item;
    }

    @Override
    public productoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_new_customers_info,viewGroup,false);
        productoViewHolder producto= new productoViewHolder(v);

        return producto;
           }

    @Override
    public void onBindViewHolder(productoViewHolder productoViewHolder, int i) {
       productoViewHolder.iD=item.get(i).get_IDHeaeder();

        productoViewHolder.txtName.setText(""+item.get(i).get_Name());

        productoViewHolder.txtTel.setText(""+item.get(i).get_Tel());
        productoViewHolder.txtMobile.setText(""+item.get(i).get_Mobile());
        productoViewHolder.txtAddress.setText(""+item.get(i).get_Address());

//        if ((Integer.parseInt(m.toString()))%2==0){
//            productoViewHolder.lyRoot02.setBackgroundColor(Color.parseColor("#ffffff"));
//        }
//        if ((Integer.parseInt(m.toString()))%2>0){
//            productoViewHolder.lyRoot02.setBackgroundColor(Color.parseColor("#ECEFF1"));
//        }

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public Filter getFilter() {
        Filter f= new NewCustomersFilter();
        return f;
    }


    public  class NewCustomersFilter extends  Filter{

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<CustomersInfo> temp = new ArrayList<>();

            if (TextUtils.isEmpty(charSequence))
            {
                FilterResults filter = new FilterResults();
                filter.values = itemDump;
                filter.count = temp.size();

                return filter;
            }

            if (itemDump != null && itemDump.size() != 0) {
                for (CustomersInfo item : itemDump) {

                    if (!TextUtils.isEmpty(item.get_Name()) && item.get_Name().contains(charSequence)) {
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

            ArrayList<CustomersInfo>  temp = null;

            try {
                temp = (ArrayList<CustomersInfo>) filterResults.values;
            }
            catch (Exception e)
            {
                Log.e("mohsen: ",e.toString());
            }

            if (temp == null)
                temp = new ArrayList<CustomersInfo>();

            item = temp;

            notifyDataSetChanged();
        }
    }

    public class productoViewHolder extends RecyclerView.ViewHolder{
        TextView txtName,txtTel,txtMobile,txtAddress;
        String iD;
        ImageView imgAccept,imgCancel;
//        RelativeLayout lyRoot02;

        public productoViewHolder(final View itemView) {
            super(itemView);

                    txtName    = (TextView) itemView.findViewById(R.id. txtName    );
                    txtTel      = (TextView) itemView.findViewById(R.id. txtTel     );
                    txtMobile    = (TextView) itemView.findViewById(R.id. txtMobile  );
                    txtAddress   = (TextView) itemView.findViewById(R.id. txtAddress );

            imgAccept= (ImageView) itemView.findViewById(R.id.imgAccept);
            imgCancel= (ImageView) itemView.findViewById(R.id.imgCancel);


            imgAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    Toast.makeText(G.context, "Id is "+iD, Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(act)
                            .setTitle("تایید مشتری")
                            .setMessage("مایل به تایید مشتری"+"("+txtName.getText().toString()+")"+"میباشید؟")
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.drawable.successs)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {


                                public void onClick(DialogInterface arg0, int arg1) {

                                    Intent intent = new Intent(act,ActivityStateCheker.class);
                                    intent.putExtra("Amaliat","AcceptNewCustomer");
                                    intent.putExtra("ID",iD);
                                    act.startActivity(intent);

                                }
                            }).create().show();

                }
            });

            imgCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    Toast.makeText(G.context, "Id is "+iD, Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(act)
                            .setTitle("لغو مشتری")
                            .setMessage("مایل به لغو مشتری"+"("+txtName.getText().toString()+")"+"میباشید؟")
                            .setNegativeButton(android.R.string.no, null)
                           .setIcon(R.drawable.eror_dialog)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {


                                    Intent intent = new Intent(act,ActivityStateCheker.class);
                                    intent.putExtra("Amaliat","DeclinetNewCustomer");
                                    intent.putExtra("ID",iD);
                                    act.startActivity(intent);
                                }
                            }).create().show();

                }
            });

        }
    }

    public void setAct(AppCompatActivity act) {
        this.act = act;

    }
}
