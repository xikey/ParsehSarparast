package com.example.zikey.sarparast.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Zikey on 16/05/2016.
 */
public class PreferenceHelper {
    private static final String NAME = "MY_PREFERENCES";
    public  static final String  TOKEN_ID = "TokenID";
    public  static final String  IS_LOGIN = "login_value";
    public  static final String  USER_NAME = "sarparast_name";

    private SharedPreferences sharedPreferences;

    public PreferenceHelper(Context context) {
        this.sharedPreferences = context.getSharedPreferences(NAME,context.MODE_PRIVATE);
    }

    public String getString(String Key){
        return  sharedPreferences.getString(Key,"");
    }

    public void putString(String key,String value){
        SharedPreferences.Editor editor;
        editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }
   }
