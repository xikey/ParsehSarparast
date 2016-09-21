package com.example.zikey.sarparast;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

public class ActivityChangePass extends AppCompatActivity {

    private PreferenceHelper preferenceHelper;

    private ImageView imgBack;
    private EditText edtOldPass;
    private EditText edtNewPass1;
    private EditText edtNewPass2;
    private Button   save;

    private String oldPAss;
    private String newPass;
    private String mirrorNewPass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        preferenceHelper = new PreferenceHelper(this);

        edtOldPass = (EditText) findViewById(R.id.oldPass);
        edtNewPass1 = (EditText) findViewById(R.id.newPass1);
        edtNewPass2 = (EditText) findViewById(R.id.newPass2);
        save = (Button) findViewById(R.id.btnSave);
        imgBack= (ImageView) findViewById(R.id.imgBack);



        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityChangePass.this,ActivityMain.class);
                startActivity(intent);
                finish();

            }
        });

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                newPass = edtNewPass1.getText().toString();
                mirrorNewPass = edtNewPass2.getText().toString();
                oldPAss = edtOldPass.getText().toString();




                if (edtOldPass.getText().toString().equals("")){
                    edtOldPass.setError("رمز عبور قبلی");
                }

                  if (((edtNewPass1.getText().toString().equals("")))||(newPass.length()<4)){
                    edtNewPass1.setError("رمز عبور جدید");
                }
                  if (edtNewPass2.getText().toString().equals("")){
                    edtNewPass2.setError("رمز عبور جدید");
                }



                if (!newPass.equals(mirrorNewPass)&&(!newPass.equals("")&!mirrorNewPass.equals(""))){

                    new AlertDialog.Builder(ActivityChangePass.this)
                            .setCancelable(false)
                            .setTitle("خطا")
                            .setMessage("عدم تطابق رمز عبور جدید")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(R.drawable.eror_dialog)
                            .show();

                }
                if (newPass.equals(mirrorNewPass)&&!newPass.equals("")){

                   new ChangePassAsync().execute();

                  }
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivityChangePass.this,ActivityMain.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    public class ChangePassAsync extends AsyncTask<Void,String,String> {
        Boolean isonline= NetworkTools.isOnline(ActivityChangePass.this);
        private   String state;
        private   ProgressDialog dialog;

        @Override
        protected void onPostExecute(String state) {

            if (state.equals("lenght")){
                if (dialog != null)
                    dialog.dismiss();

                new AlertDialog.Builder(ActivityChangePass.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("رمز عبور باید بالای چهار عدد باشد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();

            }

            if (state.equals("0")){
                if (dialog != null)
                    dialog.dismiss();

                new AlertDialog.Builder(ActivityChangePass.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("رمز عبور شما اشتباه است")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();

            }

            if (( state.equals("true"))){



                if (dialog != null)
                    dialog.dismiss();

               new AlertDialog.Builder(ActivityChangePass.this)
                        .setCancelable(false)
                        .setTitle("موفقیت")
                        .setMessage("رمز عبور با موفقیت تغییر یافت")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent i = getBaseContext().getPackageManager().
                                        getLaunchIntentForPackage(getBaseContext().getPackageName());
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                System.exit(0);
                                finish();



                            }
                        })
                        .setIcon(R.drawable.success)
                        .show();
            }
            else  if (state.equals("NotOnline")){
                new AlertDialog.Builder(ActivityChangePass.this)
                        .setCancelable(false)
                        .setTitle("خطا")
                        .setMessage("اتصال به اینترنت مقدور نمیباشد")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(R.drawable.eror_dialog)
                        .show();
            }
        }


        @Override
        protected void onPreExecute() {
            if (isonline) {

                dialog = ProgressDialog.show(ActivityChangePass.this, "", "دریافت اطلاعات");
                super.onPreExecute();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            HashMap<String, Object> datas = new HashMap<String, Object>();

            datas.put("TokenID",preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
            datas.put("OldPassword",oldPAss);
            datas.put("NewPassword",newPass);

            if (isonline) {
                try {
                    Log.e("change password", "pass ld "+oldPAss);
                    Log.e("change password", "pass new "+newPass);

                    if (newPass.length()<4){
                        return "lenght";
                    }
                    SoapObject request2 = (SoapObject) NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), "S_ChangePassword", datas);
                      state=  NetworkTools.getSoapPropertyAsNullableString(request2,0);
                        Log.e("change password", "pass changed"+state);

                    if (state.equals("false")){
                        return "0";
                    }
                    else return "true";

                } catch (Exception e) {
                    Log.e("iiiiiii", "connot read Soap");
                    e.printStackTrace();
                }

            }
            return "NotOnline";
        }
    }

}
