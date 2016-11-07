package com.razanPardazesh.supervisor.model.interfaces;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by Zikey on 02/11/2016.
 */

public interface IJson {
    public void fillByJson(JSONObject jsonObject);
    public JSONObject writeJson(Context context);
}
