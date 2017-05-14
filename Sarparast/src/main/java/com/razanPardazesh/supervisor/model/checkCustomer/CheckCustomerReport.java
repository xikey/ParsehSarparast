package com.razanPardazesh.supervisor.model.checkCustomer;

import android.content.Context;

import com.razanPardazesh.supervisor.model.interfaces.IJson;
import com.razanPardazesh.supervisor.model.user.Customer;
import com.razanPardazesh.supervisor.model.user.Supervisor;
import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zikey on 10/05/2017.
 */

public class CheckCustomerReport implements IJson {

    private final String KEY_ID = "i";
    private final String KEY_CUSTOMER = "C";
    private final String KEY_USER = "u";
    private final String KEY_EXPIRE_DTE = "exd";
    private final String KEY_EXPIRED = "ex";
    private final String KEY_CUSTOMER_SATISFACTION_RATE = "csr";
    private final String KEY_LN = "ln";
    private final String KEY_LT = "lt";
    private final String KEY_ITEMS = "its";


    private long id = 0;
    private Customer customer;
    private Supervisor user;
    private String expireDate = null;
    private boolean expired = false;
    private int customer_satisfaction_rate = 0;
    private String ln = null;
    private String lt = null;
    private List<CheckCustomerItem> items = null;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Supervisor getUser() {
        return user;
    }

    public void setUser(Supervisor user) {
        this.user = user;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public int getCustomer_satisfaction_rate() {
        return customer_satisfaction_rate;
    }

    public void setCustomer_satisfaction_rate(int customer_satisfaction_rate) {
        this.customer_satisfaction_rate = customer_satisfaction_rate;
    }

    public String getLn() {
        return ln;
    }

    public void setLn(String ln) {
        this.ln = ln;
    }

    public String getLt() {
        return lt;
    }

    public void setLt(String lt) {
        this.lt = lt;
    }

    public List<CheckCustomerItem> getItems() {
        return items;
    }

    public void setItems(List<CheckCustomerItem> items) {
        this.items = items;
    }

    @Override
    public void fillByJson(JSONObject jsonObject) {

        if (jsonObject == null)
            return;

        if (jsonObject.has(KEY_ID)) {
            try {
                setId(jsonObject.getLong(KEY_ID));
            } catch (JSONException e) {
                LogWrapper.loge("Question_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_CUSTOMER)) {
            try {
                Customer customer = new Customer();
                JSONObject object = new JSONObject(KEY_CUSTOMER);
                customer.fillByJson(object);
                setCustomer(customer);
            } catch (JSONException e) {
                LogWrapper.loge("Question_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_USER)) {
            try {
                Supervisor supervisor = new Supervisor();
                JSONObject o = new JSONObject(KEY_USER);
                supervisor.fillByJson(o);
                setUser(supervisor);
            } catch (JSONException e) {
                LogWrapper.loge("Question_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_EXPIRE_DTE)) {
            try {
                setExpireDate(jsonObject.getString(KEY_EXPIRE_DTE));
            } catch (JSONException e) {
                LogWrapper.loge("Question_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_EXPIRED)) {
            try {
                setExpired(jsonObject.getBoolean(KEY_EXPIRED));
            } catch (JSONException e) {
                LogWrapper.loge("Question_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_CUSTOMER_SATISFACTION_RATE)) {
            try {
                setCustomer_satisfaction_rate(jsonObject.getInt(KEY_CUSTOMER_SATISFACTION_RATE));
            } catch (JSONException e) {
                LogWrapper.loge("Question_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_LN)) {
            try {
                setLn(jsonObject.getString(KEY_LN));
            } catch (JSONException e) {
                LogWrapper.loge("Question_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_LT)) {
            try {
                setLt(jsonObject.getString(KEY_LT));
            } catch (JSONException e) {
                LogWrapper.loge("Question_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_ITEMS)) {
            try {
                JSONArray array = jsonObject.getJSONArray(KEY_ITEMS);
                setItems(new CheckCustomerItem().parseList(array));
            } catch (JSONException e) {
                LogWrapper.loge("Question_fillByJson :", e);
            }
        }


    }

    @Override
    public JSONObject writeJson(Context context) {

        JSONObject object = new JSONObject();

        try {

            object.put(KEY_ID, getId());

            if (customer != null)
                object.put(KEY_CUSTOMER, getCustomer().writeJson(context));


            object.put(KEY_CUSTOMER_SATISFACTION_RATE, getCustomer_satisfaction_rate());

            object.put(KEY_LN, getLn());
            object.put(KEY_LT, getLt());

            if (items != null)
                object.put(KEY_ITEMS, new CheckCustomerItem().serializeList(context, getItems()));


        } catch (Exception ex) {
            LogWrapper.loge("CheckCustomerReport_writeJson_Exception: ", ex);
        }
        return object;

    }

    @Override
    public ArrayList parseList(JSONArray array) {
        return null;
    }
}
