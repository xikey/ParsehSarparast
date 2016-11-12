package com.razanPardazesh.supervisor.model.user;

import android.content.Context;

import com.razanPardazesh.supervisor.model.interfaces.IJson;
import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Zikey on 10/11/2016.
 */

public abstract class IUser implements IJson{

    // type for VISITOR is 2
    //type for Suoervisor is 1

    private   int type=0;
    private  final String KEY_USER_TYPE = "type";

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void fillByJson(JSONObject jsonObject) {
        if (jsonObject==null)
            return;
        
        if (jsonObject.has(KEY_USER_TYPE)) {
            try {
                setType(jsonObject.getInt(KEY_USER_TYPE));
            } catch (JSONException e) {
                LogWrapper.loge("fillByJson: price1: ", e);
            }
        }
    }

    @Override
    public JSONObject writeJson(Context context) {
        return null;
    }
}
