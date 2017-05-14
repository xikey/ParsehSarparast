package com.razanPardazesh.supervisor.model.interfaces;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zikey on 02/11/2016.
 */

public interface IJson<T> {
    public void fillByJson(JSONObject jsonObject);
    public JSONObject writeJson(Context context);
    public ArrayList<T> parseList(JSONArray array);
}
