package com.razanPardazesh.supervisor.repo;

import android.content.Context;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;
import com.razanPardazesh.supervisor.model.wrapper.CustomerEditAnswer;
import com.razanPardazesh.supervisor.model.wrapper.CustomersEditAnswer;
import com.razanPardazesh.supervisor.model.wrapper.ServerAnswer;
import com.razanPardazesh.supervisor.repo.iRepo.ICustomersEdited;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Zikey on 12/02/2017.
 */

public class CustomersEditedServerRepo implements ICustomersEdited {

    private final String EDITED_CUSTOMER_WEBMETHOD = "getCustomerRequestEdited";
    private final String EDITEDLIST_WEBMETHOD = "getCustomerRequestEditedList";
    private final String SET_STATUS_WEBMETHOD = "SetCustomerRequestEdited_StatusCode";
    private final String EDITED_LIST_COUNT_WEBMETHOD = "getCustomerRequestEditedCount";
    private final String Key_TOKEN = "TokenID";
    private final String Key_SEARCH = "key";
    private final String Key_FIRST_INDEX = "firstIndex";
    private final String Key_COUNT = "count";
    private final String ID = "ID";
    private final String DATE_TIME = "dateTime";
    private final String STATUS = "statusCode";

    private PreferenceHelper preferenceHelper;

    @Override
    public CustomersEditAnswer getEditedList(Context context, String key, long firstIndex, long count) {

        if (key == null)
            key = "";

        preferenceHelper = new PreferenceHelper(context);
        String token = preferenceHelper.getString(PreferenceHelper.TOKEN_ID);
        String url = "http://" + preferenceHelper.getString(NetworkTools.URL);
        HashMap<String, Object> datas = new HashMap<>();

        datas.put(Key_TOKEN, token);
        datas.put(Key_SEARCH, key);
        datas.put(Key_FIRST_INDEX, firstIndex);
        datas.put(Key_COUNT, count);

        CustomersEditAnswer answer = new CustomersEditAnswer();

        try {
            String request = NetworkTools.CallSoapMethod(url, EDITEDLIST_WEBMETHOD, datas).getPropertyAsString(0);
            if (request == null) {
                answer.setMessage("خطا در دریافت اطلاعات از سرور answer is empty ");
                return answer;
            }

            JSONObject jsonObject = new JSONObject(request);
            answer.fillByJson(jsonObject);

        } catch (Exception ex) {
            answer.setMessage(" خطا در دریافت اطلاعات از سرور  " + ex.getMessage().toString());
            return answer;

        }

        return answer;
    }

    @Override
    public CustomerEditAnswer getEditedCustomer(Context context, long requestID) {

        preferenceHelper = new PreferenceHelper(context);
        String token = preferenceHelper.getString(PreferenceHelper.TOKEN_ID);
        String url = "http://" + preferenceHelper.getString(NetworkTools.URL);
        CustomerEditAnswer answer = new CustomerEditAnswer();
        HashMap<String, Object> datas = new HashMap<>();
        datas.put(Key_TOKEN, token);
        datas.put(ID, requestID);

        try {

            String request = NetworkTools.CallSoapMethod(url, EDITED_CUSTOMER_WEBMETHOD, datas).getPropertyAsString(0);

            if (request == null) {
                answer.setMessage("خطا در دریافت اطلاعات از سرور answer is empty ");
                return answer;
            }

            JSONObject jsonObject = new JSONObject(request);
            answer.fillByJson(jsonObject);

        } catch (Exception ex) {
            answer.setMessage(" خطا در دریافت اطلاعات از سرور  " + ex.getMessage().toString());
            return answer;

        }

        return answer;
    }

    @Override
    public ServerAnswer setEditedCustomerStatus(Context context, long requestID, String date, int statusCode) {

        preferenceHelper = new PreferenceHelper(context);
        String token = preferenceHelper.getString(PreferenceHelper.TOKEN_ID);
        String url = "http://" + preferenceHelper.getString(NetworkTools.URL);
        HashMap<String, Object> datas = new HashMap<>();

        ServerAnswer answer = new ServerAnswer() {
            @Override
            public ArrayList parseList(JSONArray array) {
                return null;
            }
        };
        datas.put(Key_TOKEN, token);
        datas.put(ID, requestID);
        datas.put(DATE_TIME, date);
        datas.put(STATUS, statusCode);

        try {

            String request = NetworkTools.CallSoapMethod(url,SET_STATUS_WEBMETHOD, datas).getPropertyAsString(0);

            if (request == null) {
                answer.setMessage("خطا در دریافت اطلاعات از سرور answer is empty ");
                return answer;
            }

            JSONObject jsonObject = new JSONObject(request);
            answer.fillByJson(jsonObject);

        } catch (Exception ex) {
            answer.setMessage(" خطا در دریافت اطلاعات از سرور  " + ex.getMessage().toString());
            return answer;
        }
        return answer;
    }

    @Override
    public CustomerEditAnswer getEditedListCount(Context context) {
        preferenceHelper = new PreferenceHelper(context);
        String token = preferenceHelper.getString(PreferenceHelper.TOKEN_ID);
        String url = "http://" + preferenceHelper.getString(NetworkTools.URL);
        HashMap<String, Object> datas = new HashMap<>();
        datas.put(Key_TOKEN, token);

        CustomerEditAnswer answer = new CustomerEditAnswer();

        try {

            String request = NetworkTools.CallSoapMethod(url,EDITED_LIST_COUNT_WEBMETHOD, datas).getPropertyAsString(0);

            if (request == null) {
                answer.setMessage("خطا در دریافت اطلاعات از سرور answer is empty ");
                return answer;
            }

            JSONObject jsonObject = new JSONObject(request);
            answer.fillByJson(jsonObject);

        } catch (Exception ex) {
            answer.setMessage(" خطا در دریافت اطلاعات از سرور  " + ex.getMessage().toString());
            return answer;
        }
        return answer;
    }

}
