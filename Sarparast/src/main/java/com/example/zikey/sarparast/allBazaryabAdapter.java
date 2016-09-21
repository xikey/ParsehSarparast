package com.example.zikey.sarparast;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Zikey on 31/07/2016.
 */
public class allBazaryabAdapter   extends RecyclerView.Adapter<allBazaryabAdapter.productoViewHolder>{
    private ArrayList<UserInfo> item;

    SelectUserCommunicator communicator;


    public allBazaryabAdapter(ArrayList<UserInfo> item) {
        this.item = item;
    }

    @Override
    public productoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_all_bazaryab,viewGroup,false);
        productoViewHolder producto= new productoViewHolder(v);
        return producto;
    }

    @Override
    public void onBindViewHolder(productoViewHolder productoViewHolder, int i) {
        productoViewHolder.code_user.setText(item.get(i).getCode_user());
        productoViewHolder.name_user.setText(item.get(i).getName_user());
        productoViewHolder.image_user.setImageResource(item.get(i).getImage_user());

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class productoViewHolder extends RecyclerView.ViewHolder{
        TextView code_user,name_user;
        ImageView image_user;
        RelativeLayout lyRoot;

        public productoViewHolder(final View itemView) {
            super(itemView);

            code_user=(TextView)itemView.findViewById(R.id.code_user);
            name_user=(TextView)itemView.findViewById(R.id.name_user);

            image_user=(ImageView)itemView.findViewById(R.id.image_user);
            lyRoot = (RelativeLayout) itemView.findViewById(R.id.lyRoot);

            lyRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    String code = code_user.getText().toString();
                      String name = name_user.getText().toString();

                    if (communicator != null)
                        communicator.onClick(name,code);

//                     communicator.getID(code);
//                    communicator.getName(name);

                    Log.e("Interface","name and id is "+code + name);

// = new FragmentAllBazaryab();
//                 dialogFragment = fragmentAllBazaryab.getDialogFragment();
                 //  dialogFragment.dismiss();

                }
            });

        }
    }

    public void setCommunicator(SelectUserCommunicator communicator) {
        this.communicator = communicator;
    }
}
