package com.razanPardazesh.supervisor.repo;

import android.content.Context;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;
import com.razanPardazesh.supervisor.model.wrapper.UnvisitsAnswer;
import com.razanPardazesh.supervisor.repo.iRepo.IUnvisitRepo;
import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.WeakHashMap;

/**
 * Created by Zikey on 14/05/2017.
 */

public class UnvisitServerRepo implements IUnvisitRepo {

    private final String WEBMETHOD = "getCustomerUnvisitReasons";
    private PreferenceHelper preferenceHelper;

    @Override
    public UnvisitsAnswer getReasons(Context context, long shopCode) {

        preferenceHelper = new PreferenceHelper(context);
        HashMap<String, Object> datas = new HashMap<>();
        datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
        datas.put("shpeCode", shopCode);

        UnvisitsAnswer answer = new UnvisitsAnswer();

        try {
            String request = NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), WEBMETHOD, datas).getPropertyAsString(0);

            if (request == null) {
                answer.setMessage("empty");
            }

            JSONObject jsonObject = new JSONObject(request);
            answer.fillByJson(jsonObject);


        } catch (Exception e) {
            LogWrapper.loge("ProductServerRepo : priceLevels ", e);
            answer.setIsSuccess(0);
            return answer;
        }

        return answer;


    }
}
