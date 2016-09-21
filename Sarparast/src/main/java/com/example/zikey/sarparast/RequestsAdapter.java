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

import java.util.ArrayList;

/**
 * Created by Zikey on 12/07/2016.
 */
public class RequestsAdapter  extends RecyclerView.Adapter<RequestsAdapter.productoViewHolder> implements Filterable{
    private ArrayList<RequestsInfo> item;
    private ArrayList<RequestsInfo> itemDump;
    public   int vaziat;
    public   int id;
    private String customerCode;

    FragmentManager manager;
    AppCompatActivity activity;

    public void setManager(FragmentManager manager) {
        this.manager = manager;
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public RequestsAdapter(ArrayList<RequestsInfo> item) {
        this.item = item;
        this.itemDump = item;
    }

    @Override
    public productoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_accept_requests,viewGroup,false);
        productoViewHolder producto= new productoViewHolder(v);
        return producto;
    }

    @Override
    public void onBindViewHolder(productoViewHolder productoViewHolder, final int i) {
        productoViewHolder. txtShomareSefaresh  .setText(""+item.get(i).get_SefareshID());
        productoViewHolder. txtDate             .setText(""+item.get(i).get_OrderDate());
        productoViewHolder. txtCodeMoshtari     .setText(""+item.get(i).get_CodeMoshtari());
        productoViewHolder. txtNameMoshtari     .setText(""+item.get(i).get_NameMoshtari());
        productoViewHolder. txtNameVizitor      .setText(""+item.get(i).get_NameBazaryab());
        productoViewHolder. txtPrice            .setText(""+item.get(i).get_PKolSefaresh());
        productoViewHolder. txtMande            .setText(""+item.get(i).get_MandeMoshtari());
        productoViewHolder. txtJaryanT          .setText(""+item.get(i).get_CntVBank());
        productoViewHolder. txtJaryanR          .setText(""+item.get(i).get_SumVBank());
        productoViewHolder. txtBargashtT        .setText(""+item.get(i).get_CntHoghooghi());
        productoViewHolder. txtBargashtR        .setText(""+item.get(i).get_SumHoghooghi());
        vaziat = item.get(i).get_VaziatSefarersh();
        productoViewHolder.tell = item.get(i).get_Tel();
        productoViewHolder.mobile = item.get(i).get_Mobile();
        productoViewHolder.nahveVosool = item.get(i).get_NahveVosol();
        productoViewHolder.tozihat = item.get(i).get_Tozihat();

        switch (vaziat){
            case 0: productoViewHolder.lyButtom.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case 1: productoViewHolder.lyButtom.setBackgroundColor(Color.parseColor("#81C784"));
                break;
            case 2: productoViewHolder.lyButtom.setBackgroundColor(Color.parseColor("#ff8a80"));
                break;
            case 3: productoViewHolder.lyButtom.setBackgroundColor(Color.parseColor("#FFF176"));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public Filter getFilter() {

        Filter filter = new RequestsFilter();
         return filter;
    }

    public class RequestsFilter extends  Filter{

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<RequestsInfo> temp = new ArrayList<>();

            if (TextUtils.isEmpty(charSequence)){
                FilterResults filterResults = new FilterResults();
                filterResults.values = itemDump;
                filterResults.count = temp.size();

                return  filterResults;
            }
            if ((itemDump!=null)&&(itemDump.size()!=0)){

                for (RequestsInfo item:itemDump  ) {

                    if ((!TextUtils.isEmpty(item.get_NameMoshtari()))&&(item.get_NameMoshtari().contains(charSequence))){

                        temp.add(item);
                    }

                     if ((!TextUtils.isEmpty(item.get_CodeMoshtari()))&&(item.get_CodeMoshtari().contains(charSequence))){

                        temp.add(item);
                    }

                }
            }

            FilterResults fr = new FilterResults();
            fr.values=temp;
            fr.count=temp.size();

            return  fr;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            ArrayList<RequestsInfo > temp = null;

            try {
                temp = (ArrayList<RequestsInfo>) filterResults.values;
            }

            catch (Exception e){
                Log.e("Eror::",""+e);
            }

            if ( temp==null) temp = new ArrayList<>( );

           item = temp;
            notifyDataSetChanged();

        }
    }

    public class productoViewHolder extends RecyclerView.ViewHolder{
        TextView  txtShomareSefaresh,txtDate,txtCodeMoshtari,txtNameMoshtari,txtNameVizitor,
                txtPrice,txtMande,txtJaryanT,txtJaryanR,txtBargashtT,txtBargashtR;

        ImageView btnShowDetails;
        ImageView btnAccept;
        ImageView btnCancel;
        ImageView btnForward;

        ImageView imgOrders;
        ImageView imgInfo;
        ImageView imgCall;


        RelativeLayout lyRootr;
        LinearLayout lyButtom;

        String tell;
        String mobile;
        String nahveVosool;
        String tozihat;

        public productoViewHolder(final View itemView) {
            super(itemView);

         txtShomareSefaresh   = (TextView) itemView.findViewById(R.id.txtShomareSefaresh);
         txtDate              = (TextView) itemView.findViewById(R.id.txtDate     );
         txtCodeMoshtari          = (TextView) itemView.findViewById(R.id.txtCodeMoshtari   );
         txtNameMoshtari         = (TextView) itemView.findViewById(R.id.txtNameMoshtari   );
         txtNameVizitor          = (TextView) itemView.findViewById(R.id.txtNameVizitor    );
         txtPrice             = (TextView) itemView.findViewById(R.id.txtPrice          );
         txtMande            = (TextView) itemView.findViewById(R.id.txtMande          );
         txtJaryanT        = (TextView) itemView.findViewById(R.id.txtJaryanT);
         txtJaryanR            = (TextView) itemView.findViewById(R.id.txtJaryanR);
         txtBargashtT             = (TextView) itemView.findViewById(R.id.txtBargashtT      );
         txtBargashtR         = (TextView) itemView.findViewById(R.id.txtBargashtR      );


            btnShowDetails =(ImageView)  itemView.findViewById(R.id.btnShowDetails);
            btnAccept =     (ImageView)  itemView.findViewById(R.id.btnAccept);
            btnCancel =     (ImageView)  itemView.findViewById(R.id.btnCancel);
            btnForward =    (ImageView)  itemView.findViewById(R.id.btnForward);

            imgOrders =    (ImageView)  itemView.findViewById(R.id.imgOrders);
            imgInfo =    (ImageView)  itemView.findViewById(R.id.imgInfo);
            imgCall =    (ImageView)  itemView.findViewById(R.id.imgCall);


            lyRootr= (RelativeLayout) itemView.findViewById(R.id.lyRootReq);
            lyButtom= (LinearLayout) itemView.findViewById(R.id.lyButtom);



            imgOrders.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Fragment_MandehMoshtarian  showDetails = new Fragment_MandehMoshtarian();
                    showDetails.setID( txtCodeMoshtari.getText().toString());
//                  showDetails.setActivity(activity);
                    showDetails.show(manager,"");

                    showDetails.setCancelable(true);
                }
            });


            imgInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    customerCode= txtCodeMoshtari.getText().toString();
                    Intent intent = new Intent(activity,ActivityCustomersLastOrders.class);
                    intent.putExtra("code",customerCode);
                    activity.startActivity(intent);
                }
            });

            imgCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if ((tell.equals("0"))&&(mobile.equals("0"))){
                        new AlertDialog.Builder(activity)
                                .setTitle("خطا")
                                .setMessage("شماره ای به مشتری اختصاص داده نشده است!")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                    }
                                }).create().show();
                    }

                    else {

                        Fragment_CustomersPhoneNumber  showDetails = new Fragment_CustomersPhoneNumber();
                        showDetails.setName(txtNameMoshtari.getText().toString());
                        showDetails.setPhone(tell);
                        showDetails.setMobile(mobile);
                        showDetails.setActivity(activity);
                        showDetails.show(manager,"");

                        showDetails.setCancelable(true);
                    }
                }
            });

            btnShowDetails.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {
                    G.ID= Integer.parseInt(txtShomareSefaresh.getText().toString());

                    ActivitySefareshDetails.Price = txtPrice.getText().toString();
                    Intent intent = new Intent(activity,ActivitySefareshDetails.class);
                    intent.putExtra("Tozihat",tozihat);
                    intent.putExtra("Nahveh",nahveVosool);
                    ActivityAcceptRequests.state="negative";
                    activity.startActivity(intent);

                }
            });

            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    G.ID= Integer.parseInt(txtShomareSefaresh.getText().toString());
                    if (vaziat==1){

                        // baraye bargardoondan az halate 1 be halate sefr *************************
                        new AlertDialog.Builder(activity)

                                .setMessage("مایل به برگردادن سفارش از حالت تایید شده میباشد ؟")
                                .setNegativeButton(android.R.string.no, null)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(activity,ActivityStateCheker.class);
                        intent.putExtra("Amaliat","Nothing");
                        intent.putExtra("ID",G.ID);

                        //Log.i("FragmentID","Amaliat ID is "+G.ID);
                        ActivityAcceptRequests.state="paused";
                        activity.startActivity(intent);
                                    }
                                }).create().show();

                    }
else {
                        new AlertDialog.Builder(activity)
                                .setTitle("تایید سفارش")
                                .setMessage("مایل به تایید سفارش میباشید؟")
                                .setNegativeButton(android.R.string.no, null)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(activity, ActivityStateCheker.class);
                                        intent.putExtra("Amaliat", "Accept");
                                        intent.putExtra("ID", G.ID);

                                        Log.i("FragmentID", "Amaliat ID is " + G.ID);
                                        ActivityAcceptRequests.state = "paused";
                                        activity.startActivity(intent);
//                                    G.currentActivity.finish();
                                    }
                                }).create().show();
                    }
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    G.ID = Integer.parseInt(txtShomareSefaresh.getText().toString());

                    if (vaziat == 2) {
                        // baraye bargardoondan az halate 1 be halate sefr *************************
                        new AlertDialog.Builder(activity)

                                .setMessage("مایل به برگرداندن سفارش از حال بغو شده میباشید ؟")
                                .setNegativeButton(android.R.string.no, null)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(activity,ActivityStateCheker.class);
                                        intent.putExtra("Amaliat","Nothing");
                                        intent.putExtra("ID",G.ID);

                                        //Log.i("FragmentID","Amaliat ID is "+G.ID);
                                        ActivityAcceptRequests.state="paused";
                                        activity.startActivity(intent);
                                    }
                                }).create().show();
                    } else {

                        new AlertDialog.Builder(activity)
                                .setTitle("لغو سفارش")
                                .setMessage("مایل به لغو سفارش میباشید؟")
                                .setNegativeButton(android.R.string.no, null)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(activity, ActivityStateCheker.class);
                                        intent.putExtra("Amaliat", "Cancel");
                                        intent.putExtra("ID", G.ID);

                                        Log.i("FragmentID", "Amaliat ID is " + G.ID);
                                        ActivityAcceptRequests.state = "paused";
                                        activity.startActivity(intent);
//                                    G.currentActivity.finish();
                                    }
                                }).create().show();
                    }
                }
            });


            btnForward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    G.ID= Integer.parseInt(txtShomareSefaresh.getText().toString());

                    if (vaziat==3){
                        // baraye bargardoondan az halate 1 be halate sefr *************************
                        new AlertDialog.Builder(activity)

                                .setMessage("مایل به برگرداندن سفارش از حالت ارجاع داده شده میباشد؟")
                                .setNegativeButton(android.R.string.no, null)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Intent intent = new Intent(activity,ActivityStateCheker.class);
                                        intent.putExtra("Amaliat","Nothing");
                                        intent.putExtra("ID",G.ID);

                                        //Log.i("FragmentID","Amaliat ID is "+G.ID);
                                        ActivityAcceptRequests.state="paused";
                                        activity.startActivity(intent);
                                    }
                                }).create().show();

                    }

                    else {

                        new AlertDialog.Builder(activity)
                                .setTitle("ارجاع سفارش")
                                .setMessage("مایل به ارجاع سفارش میباشید؟")
                                .setNegativeButton(android.R.string.no, null)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                        Intent intent = new Intent(activity, ActivityStateCheker.class);
                                        intent.putExtra("Amaliat", "Forward");
                                        intent.putExtra("ID", G.ID);

                                        Log.i("FragmentID", "Amaliat ID is " + G.ID);
                                        ActivityAcceptRequests.state = "paused";
                                        activity.startActivity(intent);

//                                    G.currentActivity.finish();
                                    }
                                }).create().show();
                    }
                }
            });


//            lyRootr.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    id = Integer.parseInt( txtShomareSefaresh.getText().toString());
//                    ActivitySefareshDetails.Price=txtPrice.getText().toString();
//
//                    G.ID=id;
//                    Fragment_AcceptRequestsDialog myDialog = new Fragment_AcceptRequestsDialog();
//                    myDialog.show(ActivityAcceptRequests.manager,"انتخواب عملیات ");
//                    myDialog.setCancelable(true);
//
////                    Intent intent = new Intent(G.currentActivity,ActivityListOfGroupInfo.class);
////                   // intent.putExtra("Group_Asli",code);
////                    G.currentActivity.startActivity(intent);
//
//                }
//            });
        }
    }

}