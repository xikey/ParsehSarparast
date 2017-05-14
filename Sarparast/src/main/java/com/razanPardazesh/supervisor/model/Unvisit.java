package com.razanPardazesh.supervisor.model;

import android.content.Context;

import com.razanPardazesh.supervisor.model.interfaces.IJson;
import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zikey on 14/05/2017.
 */

public class Unvisit implements IJson<Unvisit>{

    private static final String KEY_DATE = "od";
    private static final String KEY_TIME = "ot";
    private static final String KEY_REASON = "rt";
    private static final String KEY_COMMENT = "c";
    private static final String KEY_NAME = "n";

    private String orderDate;
    private String orderTime;
    private String reasonTitle;
    private String comment;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getReasonTitle() {
        return reasonTitle;
    }

    public void setReasonTitle(String reasonTitle) {
        this.reasonTitle = reasonTitle;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public void fillByJson(JSONObject jsonObject) {

        if (jsonObject==null)
            return;

        if (jsonObject.has(KEY_DATE)) {
            try {
                setOrderDate(jsonObject.getString(KEY_DATE));
            } catch (JSONException e) {
                LogWrapper.loge("Unvisit_fillByJson :",e );
            }
        }
           if (jsonObject.has(KEY_TIME)) {
            try {
                setOrderTime(jsonObject.getString(KEY_TIME));
            } catch (JSONException e) {
                LogWrapper.loge("Unvisit_fillByJson :",e );
            }
        }if (jsonObject.has(KEY_COMMENT)) {
            try {
                setComment(jsonObject.getString(KEY_COMMENT));
            } catch (JSONException e) {
                LogWrapper.loge("Unvisit_fillByJson :",e );
            }
        }if (jsonObject.has(KEY_REASON)) {
            try {
                setReasonTitle(jsonObject.getString(KEY_REASON));
            } catch (JSONException e) {
                LogWrapper.loge("Unvisit_fillByJson :",e );
            }
        }if (jsonObject.has(KEY_NAME)) {
            try {
                setName(jsonObject.getString(KEY_NAME));
            } catch (JSONException e) {
                LogWrapper.loge("Unvisit_fillByJson :",e );
            }
        }

    }

    @Override
    public JSONObject writeJson(Context context) {
        return null;
    }

    @Override
    public ArrayList<Unvisit> parseList(JSONArray array) {
        return null;
    }
}
