package com.example.zikey.sarparast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Trujas on 05/08/2015.
 */
public class UserInfoAdapterTS extends RecyclerView.Adapter<UserInfoAdapterTS.productoViewHolder>{
    private ArrayList<UserInfo> item;

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    private AppCompatActivity activity;

    public UserInfoAdapterTS(ArrayList<UserInfo> item) {
        this.item = item;
    }

    @Override
    public productoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_user_info,viewGroup,false);
        productoViewHolder producto= new productoViewHolder(v);
        return producto;
    }

    @Override
    public void onBindViewHolder(productoViewHolder productoViewHolder, int i) {
        productoViewHolder.code_user.setText(item.get(i).getCode_user());
        productoViewHolder.name_user.setText(item.get(i).getName_user());
        productoViewHolder.execute_user.setText(""+item.get(i).getExecute_user());
        productoViewHolder.image_user.setImageResource(item.get(i).getImage_user());

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class productoViewHolder extends RecyclerView.ViewHolder{
        TextView code_user,name_user,execute_user;
        ImageView image_user;
        RelativeLayout lyRoot;

        public productoViewHolder(final View itemView) {
            super(itemView);

            code_user=(TextView)itemView.findViewById(R.id.code_user);
            name_user=(TextView)itemView.findViewById(R.id.name_user);
            execute_user=(TextView)itemView.findViewById(R.id.execute_user);
            image_user=(ImageView)itemView.findViewById(R.id.image_user);

            lyRoot = (RelativeLayout) itemView.findViewById(R.id.lyRoot);

            lyRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String code = code_user.getText().toString();
                    String name = name_user.getText().toString();

                     Intent intent = new Intent(activity,ActivityListOfTSvisitorInfo.class);
                    intent.putExtra("Code_Bazaryab",code);
                    intent.putExtra("Name_Bazaryab",name);

                   activity.startActivity(intent);

                }
            });

        }
    }

}
