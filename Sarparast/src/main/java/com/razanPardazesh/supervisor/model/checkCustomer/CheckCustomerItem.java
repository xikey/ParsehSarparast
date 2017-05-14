package com.razanPardazesh.supervisor.model.checkCustomer;

import android.content.Context;

import com.razanPardazesh.supervisor.model.interfaces.IJson;
import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zikey on 10/05/2017.
 */

public class CheckCustomerItem implements IJson<CheckCustomerItem> {

    private final String KEY_ID = "i";
    private final String KEY_ID_HEADER = "ih";
    private final String KEY_QUSETION_ID = "qi";
    private final String KEY_QUSETION_TITLE = "qt";
    private final String KEY_QUSETION_ANSWER = "qa";
    private final String KEY_RATE = "r";

    private long id = 0;
    private long id_header = 0;
    private long question_id = 0;
    private String question_title = null;
    private String question_answer = null;
    private int rate = 0;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId_header() {
        return id_header;
    }

    public void setId_header(long id_header) {
        this.id_header = id_header;
    }

    public long getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(long question_id) {
        this.question_id = question_id;
    }

    public String getQuestion_title() {
        return question_title;
    }

    public void setQuestion_title(String question_title) {
        this.question_title = question_title;
    }

    public String getQuestion_answer() {
        return question_answer;
    }

    public void setQuestion_answer(String question_answer) {
        this.question_answer = question_answer;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    @Override
    public void fillByJson(JSONObject jsonObject) {

        if (jsonObject == null)
            return;

        if (jsonObject.has(KEY_ID)) {
            try {
                setId(jsonObject.getLong(KEY_ID));
            } catch (JSONException e) {
                LogWrapper.loge("CheckCustomerItem_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_ID_HEADER)) {
            try {
                setId_header(jsonObject.getLong(KEY_ID_HEADER));
            } catch (JSONException e) {
                LogWrapper.loge("CheckCustomerItem_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_QUSETION_ID)) {
            try {
                setQuestion_id(jsonObject.getLong(KEY_QUSETION_ID));
            } catch (JSONException e) {
                LogWrapper.loge("CheckCustomerItem_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_QUSETION_TITLE)) {
            try {
                setQuestion_title(jsonObject.getString(KEY_QUSETION_TITLE));
            } catch (JSONException e) {
                LogWrapper.loge("CheckCustomerItem_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_QUSETION_ANSWER)) {
            try {
                setQuestion_answer(jsonObject.getString(KEY_QUSETION_ANSWER));
            } catch (JSONException e) {
                LogWrapper.loge("CheckCustomerItem_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_RATE)) {
            try {
                setRate(jsonObject.getInt(KEY_RATE));
            } catch (JSONException e) {
                LogWrapper.loge("CheckCustomerItem_fillByJson :", e);
            }
        }
    }

    @Override
    public JSONObject writeJson(Context context) {

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(KEY_ID, getId());
            jsonObject.put(KEY_ID_HEADER, getId_header());
            jsonObject.put(KEY_QUSETION_ID, getQuestion_id());
            jsonObject.put(KEY_QUSETION_TITLE, getQuestion_title());
            jsonObject.put(KEY_QUSETION_ANSWER, getQuestion_answer());
            jsonObject.put(KEY_RATE, getRate());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    public ArrayList<CheckCustomerItem> parseList(JSONArray array) {
        ArrayList<CheckCustomerItem> output = new ArrayList<>();

        try {

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                CheckCustomerItem item = new CheckCustomerItem();
                item.fillByJson(obj);
                output.add(item);

            }

        } catch (Exception ex) {
            LogWrapper.loge("CheckCustomerItem_parseList_Exception: ", ex);
        }


        return output;


    }

    public JSONArray serializeList(Context context, List<CheckCustomerItem> checkCustomerItems) {

        JSONArray output = new JSONArray();

        for (CheckCustomerItem c : checkCustomerItems) {

            output.put(c.writeJson(context));
        }

        return output;


    }


}
