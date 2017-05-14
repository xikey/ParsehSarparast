package com.razanPardazesh.supervisor.model.user;

import android.content.Context;

import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zikey on 10/05/2017.
 */

public class Customer extends IUser  {

    private static final String KEY_CODE_MARKAZ = "c";
    private static final String KEY_NAME = "n";

    private long codeMarkaz;
    private String name;

    public long getCodeMarkaz() {
        return codeMarkaz;
    }

    public void setCodeMarkaz(long codeMarkaz) {
        this.codeMarkaz = codeMarkaz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Customer() {
        setType(3);
    }

    @Override
    public int getType() {
        return super.getType();
    }


    @Override
    public void setType(int type) {
        super.setType(type);
    }

    @Override
    public void fillByJson(JSONObject jsonObject) {
        super.fillByJson(jsonObject);

        if (jsonObject == null)
            return;

        if (jsonObject.has(KEY_CODE_MARKAZ)) {
            try {
                setCodeMarkaz(jsonObject.getLong(KEY_CODE_MARKAZ));
            } catch (JSONException e) {
                LogWrapper.loge("Question_fillByJson :",e );
            }
        }   if (jsonObject.has(KEY_NAME)) {
            try {
                setName(jsonObject.getString(KEY_NAME));
            } catch (JSONException e) {
                LogWrapper.loge("Question_fillByJson :",e );
            }
        }


    }

    @Override
    public JSONObject writeJson(Context context) {

        JSONObject jsonObject = new JSONObject();

        try {
            if(codeMarkaz!=0)
            jsonObject.put(KEY_CODE_MARKAZ,getCodeMarkaz());
            if (name!=null)
            jsonObject.put(KEY_NAME,getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }

    @Override
    public ArrayList parseList(JSONArray array) {
        return null;
    }
}
