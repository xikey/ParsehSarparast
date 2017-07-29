package com.razanPardazesh.supervisor.tools;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zikey.sarparast.R;


/**
 * Created by Zikey on 10/04/2017.
 */

public class SupervisorDialogBuilder extends DialogBuilder {

    public SupervisorDialogBuilder() {

    }

    public Snackbar showMessageLong(FragmentActivity context, String message, ViewGroup viewGroup) {

        Snackbar s = Snackbar.make(viewGroup, message, Snackbar.LENGTH_LONG);
        View sbView = s.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(5);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        s.show();

        return s;
    }

    public Snackbar showMessageLongٌ(FragmentActivity context, String message, ViewGroup viewGroup, String actionName, View.OnClickListener action) {


        Snackbar s = Snackbar.make(viewGroup, message, Snackbar.LENGTH_LONG);
        View sbView = s.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(5);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        if (!TextUtils.isEmpty(actionName) && action != null)
            s.setAction(actionName, action);
        s.show();

        return s;
    }

    public Snackbar showMessageShort(FragmentActivity context, String message, ViewGroup viewGroup) {

        Snackbar s = Snackbar.make(viewGroup, message, Snackbar.LENGTH_SHORT);
        View sbView = s.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(5);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        s.show();

        return s;
    }

//    public AlertDialog showInputTextDialog(AppCompatActivity context, String title, String note, final OnDialogListener onYesClickListener) {
//
//        String output = null;
//
//        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(context);
//        View mView = layoutInflaterAndroid.inflate(R.layout.layout_input_dialog, null);
//        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(context);
//        alertDialogBuilderUserInput.setView(mView);
//
//        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
//        TextView dialogTitle = (TextView) mView.findViewById(R.id.dialogTitle);
//
//        if (!TextUtils.isEmpty(title))
//            dialogTitle.setText(title);
//
//        if (!TextUtils.isEmpty(note)) {
//            userInputDialogEditText.setText(note);
//        }
//
//        alertDialogBuilderUserInput
//                .setCancelable(false)
//                .setPositiveButton("تایید", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialogBox, int id) {
//
//                        if (onYesClickListener != null) {
//
//                            if (TextUtils.isEmpty(userInputDialogEditText.getText().toString())) {
//                                onYesClickListener.onOK(null);
//                            } else
//                                onYesClickListener.onOK(userInputDialogEditText.getText().toString());
//                        }
//
//                    }
//                })
//
//                .setNegativeButton("انصراف",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialogBox, int id) {
//
//                                dialogBox.cancel();
//                            }
//                        });
//
//        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
//        alertDialogAndroid.show();
//
//        return alertDialogAndroid;
//    }

    public AlertDialog showPickerDialog(AppCompatActivity context, String title, final CharSequence items[], final OnPickerDialogListener onPickerDialoglistener) {


        AlertDialog.Builder pickerDialog = new AlertDialog.Builder(context);
        pickerDialog.setTitle(title);
        pickerDialog.setCancelable(true);
        if (items == null || items.length == 0)
            return null;
        pickerDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < items.length; i++) {
                    if (which == i) {
                        if (onPickerDialoglistener != null)

                            onPickerDialoglistener.onPick(which);
                    }
                }
            }
        });

        AlertDialog alertDialogAndroid = pickerDialog.create();
        alertDialogAndroid.show();

        return alertDialogAndroid;
    }


    public interface OnDialogListener {
        public void onOK(String input);

    }

    public interface OnPickerDialogListener {
        void onPick(int position);
    }

}
