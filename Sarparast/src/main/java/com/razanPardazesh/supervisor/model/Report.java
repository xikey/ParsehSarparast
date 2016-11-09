package com.razanPardazesh.supervisor.model;

import android.content.Context;

import com.razanPardazesh.supervisor.model.interfaces.IJson;
import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Zikey on 09/11/2016.
 */

public class Report implements IJson {

    private final String KEY_TotalCustomer = "tc";
    private final String KEY_notVisited = "nv";
    private final String KEY_orderCount = "oc";
    private final String KEY_cantOrderCount = "coc";

    private Long totalCustomers = 0l;
    private Long notVisited = 0l;
    private Long orderCount = 0l;
    private Long cantOrderCount = 0l;


    public Long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(Long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public Long getNotVisited() {
        return notVisited;
    }

    public void setNotVisited(Long notVisited) {
        this.notVisited = notVisited;
    }

    public Long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }

    public Long getCantOrderCount() {
        return cantOrderCount;
    }

    public void setCantOrderCount(Long cantOrderCount) {
        this.cantOrderCount = cantOrderCount;
    }

    @Override
    public void fillByJson(JSONObject jsonObject) {

        if (jsonObject == null)
            return;

        if (jsonObject.has(KEY_TotalCustomer)) {
            try {
                setTotalCustomers(jsonObject.getLong(KEY_TotalCustomer));
            } catch (JSONException e) {
                LogWrapper.loge("fillByJson: price1: ", e);
            }
        }

        if (jsonObject.has(KEY_notVisited)) {
            try {
                setNotVisited(jsonObject.getLong(KEY_notVisited));
            } catch (JSONException e) {
                LogWrapper.loge("fillByJson: price1: ", e);
            }
        }
        if (jsonObject.has(KEY_orderCount)) {
            try {
                setOrderCount(jsonObject.getLong(KEY_orderCount));
            } catch (JSONException e) {
                LogWrapper.loge("fillByJson: price1: ", e);
            }
        }
        if (jsonObject.has(KEY_cantOrderCount)) {
            try {
                setCantOrderCount(jsonObject.getLong(KEY_cantOrderCount));
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
