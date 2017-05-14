package com.razanPardazesh.supervisor.model.checkCustomer;

import android.content.Context;

import com.razanPardazesh.supervisor.model.interfaces.IJson;
import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zikey on 10/05/2017.
 */

public class Question implements IJson {

    private final String KEY_ID = "i";
    private final String KEY_TITLE = "t";
    private final String ANSWER_TYPE = "at";
    private final String QUESTION_TYPE = "qt";
    private final String NEED_RATE = "nr";


    private long id ;
    private String title = null;
    private int answerType ;
    private int questionType ;
    private boolean needRate ;
    private String answer;
    private int rate;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

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

    public int getAnswerType() {
        return answerType;
    }

    public void setAnswerType(int answerType) {
        this.answerType = answerType;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public boolean isNeedRate() {
        return needRate;
    }

    public void setNeedRate(boolean needRate) {
        this.needRate = needRate;
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
        if (jsonObject.has(KEY_TITLE)) {
            try {
                setTitle(jsonObject.getString(KEY_TITLE));
            } catch (JSONException e) {
                LogWrapper.loge("Question_fillByJson :", e);
            }
        }
        if (jsonObject.has(ANSWER_TYPE)) {
            try {
                setAnswerType(jsonObject.getInt(ANSWER_TYPE));
            } catch (JSONException e) {
                LogWrapper.loge("Question_fillByJson :", e);
            }
        }
        if (jsonObject.has(QUESTION_TYPE)) {
            try {
                setQuestionType(jsonObject.getInt(QUESTION_TYPE));
            } catch (JSONException e) {
                LogWrapper.loge("Question_fillByJson :", e);
            }
        }
        if (jsonObject.has(NEED_RATE)) {
            try {
                setNeedRate(jsonObject.getBoolean(NEED_RATE));
            } catch (JSONException e) {
                LogWrapper.loge("Question_fillByJson :", e);
            }
        }

    }

    @Override
    public JSONObject writeJson(Context context) {
        return null;
    }

    @Override
    public ArrayList parseList(JSONArray array) {

        ArrayList<Question> output = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Question add = new Question();
                add.fillByJson(obj);
                output.add(add);

            }
        } catch (Exception ex) {
            LogWrapper.loge("Question_parseList :", ex);
        }
        return output;
    }
}
