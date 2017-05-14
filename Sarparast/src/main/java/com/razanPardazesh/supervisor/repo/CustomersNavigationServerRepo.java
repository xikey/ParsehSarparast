package com.razanPardazesh.supervisor.repo;

import android.content.Context;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;
import com.razanPardazesh.supervisor.model.wrapper.CustomersAroundMeAnswer;
import com.razanPardazesh.supervisor.repo.iRepo.ICustomersNavigation;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Zikey on 06/03/2017.
 */

public class CustomersNavigationServerRepo implements ICustomersNavigation {

    private   final String KEY_GET_ALL_AROUND_CUSTOMERS_WEBMETHOD = "getAroundCustomersLocation";
    private final String Key_TOKEN = "TokenID";
    private final String Key_LONGITUDE = "ln";
    private final String Key_LATITUDE = "lt";
    private PreferenceHelper preferenceHelper;

    @Override
    public CustomersAroundMeAnswer getAroundCustomers(Context context, String lt, String ln) {
        preferenceHelper = new PreferenceHelper(context);
        String token = preferenceHelper.getString(PreferenceHelper.TOKEN_ID);
        String url = "http://" + preferenceHelper.getString(NetworkTools.URL);
        HashMap<String, Object> datas = new HashMap<>();

        datas.put(Key_TOKEN, token);
        datas.put(Key_LONGITUDE, ln);
        datas.put(Key_LATITUDE, lt);

        CustomersAroundMeAnswer answer = new CustomersAroundMeAnswer();

        try {
            String request = NetworkTools.CallSoapMethod(url, KEY_GET_ALL_AROUND_CUSTOMERS_WEBMETHOD, datas).getPropertyAsString(0);
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
