package com.razanPardazesh.supervisor.model.user;

import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zikey on 10/11/2016.
 */

public class Visitor extends IUser{

    @Override
    public int getType() {
        return super.getType();
    }

    private final String KEY_ID = "id";
    private final String KEY_CODE = "code";
    private final String KEY_NAME = "name";


    private Long id=0l;
    private Long code=0l;
    private String name="";


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKEY_ID() {
        return KEY_ID;
    }

    public String getKEY_CODE() {
        return KEY_CODE;
    }

    public String getKEY_NAME() {
        return KEY_NAME;
    }

    @Override
    public void fillByJson(JSONObject jsonObject) {
        super.fillByJson(jsonObject);

        if (jsonObject == null)
            return;

        if (jsonObject.has(KEY_ID)) {
            try {
                setId(jsonObject.getLong(KEY_ID));
            } catch (JSONException e) {
                LogWrapper.loge("fillByJson: price1: ", e);
            }
        }
        if (jsonObject.has(KEY_NAME)) {
            try {
                setName(jsonObject.getString(KEY_NAME));
            } catch (JSONException e) {
                LogWrapper.loge("fillByJson: price1: ", e);
            }
        }
        if (jsonObject.has(KEY_CODE)) {
            try {
                setCode(jsonObject.getLong(KEY_CODE));
            } catch (JSONException e) {
                LogWrapper.loge("fillByJson: price1: ", e);
            }
        }
    }

    @Override
    public ArrayList parseList(JSONArray array) {
        return null;
    }
}
