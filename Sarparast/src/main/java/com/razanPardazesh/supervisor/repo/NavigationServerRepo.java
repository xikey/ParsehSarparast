package com.razanPardazesh.supervisor.repo;

import android.content.Context;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;
import com.razanPardazesh.supervisor.model.LocationData;
import com.razanPardazesh.supervisor.model.Navigation;
import com.razanPardazesh.supervisor.model.wrapper.NavigationAnswer;
import com.razanPardazesh.supervisor.model.wrapper.ServerAnswer;
import com.razanPardazesh.supervisor.repo.iRepo.INavigation;
import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zikey on 13/11/2016.
 */

public class NavigationServerRepo implements INavigation {

    private PreferenceHelper preferenceHelper;
    private static final String KEY_SEND_VISITORS_LOCATION = "Create_NavigationsLog";

    @Override
    public ServerAnswer sendSupervisorsLocations(Context context, ArrayList<LocationData> locationData) {

        preferenceHelper = new PreferenceHelper(context);
        HashMap<String, Object> datas = new HashMap<>();
        NavigationAnswer answer = new NavigationAnswer();
        Navigation navigation  = new Navigation();
        String locationJson = navigation.jsonCreator(locationData);



        datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
        datas.put("jsonArrString", locationJson);

        try {

            String request2 = NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), KEY_SEND_VISITORS_LOCATION, datas).getPropertyAsString(0);
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
