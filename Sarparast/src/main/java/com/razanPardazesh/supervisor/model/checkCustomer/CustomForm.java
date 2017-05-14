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
 * Created by Zikey on 11/05/2017.
 */

public class CustomForm implements IJson {

    private static final String KEY_ID = "i";
    private static final String KEY_TITLE = "t";
    private static final String KEY_QUESTION = "q";

    private long id = 0;
    private String title = null;
    private List<Question> questions = null;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public void fillByJson(JSONObject jsonObject) {

        if (jsonObject == null)
            return;

        if (jsonObject.has(KEY_ID)) {
            try {
                setId(jsonObject.getLong(KEY_ID));
            } catch (JSONException e) {
                LogWrapper.loge("CustomForm_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_TITLE)) {
            try {
                setTitle(jsonObject.getString(KEY_TITLE));
            } catch (JSONException e) {
                LogWrapper.loge("CustomForm_fillByJson :", e);
            }
        } if (jsonObject.has(KEY_QUESTION)) {
            try {
                JSONArray array = jsonObject.getJSONArray(KEY_QUESTION);
                setQuestions(new Question().parseList(array));
            } catch (JSONException e) {
                LogWrapper.loge("CustomForm_fillByJson :", e);
            }
        }
    }

    @Override
    public JSONObject writeJson(Context context) {
        return null;
    }

    @Override
    public ArrayList parseList(JSONArray array) {
        return null;
    }
}
