package com.razanPardazesh.supervisor.tools;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.example.zikey.sarparast.R;


/**
 * Created by Torabi on 9/17/2016.
 */

public class DialogBuilder {

    public AlertDialog showYesNOAlert(AppCompatActivity context, String title, String question, DialogInterface.OnClickListener yesAction, DialogInterface.OnClickListener noAction) {
        if (TextUtils.isEmpty(title))
            title = context.getString(R.string.attention);

        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(question)
                .setPositiveButton(context.getString(R.string.yes), yesAction)
                .setNegativeButton(context.getString(R.string.no), noAction)
                .show();
    }

    public AlertDialog showAlert(AppCompatActivity context, String message) {
        return new AlertDialog.Builder(context)
                .setMessage(message)
                .setNeutralButton(context.getString(R.string.ok), null)
                .show();
    }

    public AlertDialog showAlert(AppCompatActivity context, Throwable error) {
        if (error == null)
            return null;

        if (TextUtils.isEmpty(error.getMessage()))
            return new AlertDialog.Builder(context)
                    .setMessage(error.toString())
                    .setNeutralButton(context.getString(R.string.ok), null)
                    .show();

        return new AlertDialog.Builder(context)
                .setMessage(error.getMessage())
                .setNeutralButton(context.getString(R.string.ok), null)
                .show();
    }

    public AlertDialog showAlert(AppCompatActivity context, String message, DialogInterface.OnClickListener okAction) {
        return new AlertDialog.Builder(context)
                .setMessage(message)
                .setNeutralButton(context.getString(R.string.ok), okAction)
                .show();
    }
}
