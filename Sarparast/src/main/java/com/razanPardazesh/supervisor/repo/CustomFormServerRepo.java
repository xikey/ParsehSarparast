package com.razanPardazesh.supervisor.repo;

import android.content.Context;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;
import com.razanPardazesh.supervisor.model.wrapper.CustomFormAnswer;
import com.razanPardazesh.supervisor.model.wrapper.ServerAnswer;
import com.razanPardazesh.supervisor.repo.iRepo.ICustomForm;
import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zikey on 11/05/2017.
 */

public class CustomFormServerRepo implements ICustomForm {

    private final String KEY_WEBMETHOD = "getCheckCustomers_formQuestions";
    private final String KEY_WEBMETHOD_SET = "newCheckCustomersForm";
    private PreferenceHelper preferenceHelper;

    @Override
    public CustomFormAnswer getCustomForm(Context context) {
        preferenceHelper = new PreferenceHelper(context);
        HashMap<String, Object> datas = new HashMap<>();
        datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));

        CustomFormAnswer answer = new CustomFormAnswer();

        try {
            String request2 = NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), KEY_WEBMETHOD, datas).getPropertyAsString(0);
            if (request2 == null) {
                answer.setMessage("خطا در دریافت اطلاعات از سرور");
                return answer;
            }
            JSONObject jsonObject = new JSONObject(request2);
            answer.fillByJson(jsonObject);

        } catch (Exception ex) {
            LogWrapper.loge("ProductServerRepo : priceLevels ", ex);
            answer.setMessage("خطا در دریافت اطلاعات");
            return answer;
        }

        return answer;
    }

    @Override
    public ServerAnswer sendCustomForm(Context context, JSONObject json) {
        preferenceHelper = new PreferenceHelper(context);
        HashMap<String, Object> datas = new HashMap<>();

        String output = null;
        if (json != null)
            output = json.toString();

        datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
        datas.put("form", output);

        ServerAnswer answer = new ServerAnswer() {
            @Override
            public ArrayList parseList(JSONArray array) {
                return null;
            }
        };

        try {
            String request2 = NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), KEY_WEBMETHOD_SET, datas).getPropertyAsString(0);
            if (request2 == null) {
                answer.setMessage("خطا در دریافت اطلاعات از سرور");
                return answer;
            }
            JSONObject jsonObject = new JSONObject(request2);
            answer.fillByJson(jsonObject);

        } catch (Exception ex) {
            LogWrapper.loge("ProductServerRepo : priceLevels ", ex);
            answer.setMessage("خطا در دریافت اطلاعات");
            return answer;
        }

        return answer;

    }
}
