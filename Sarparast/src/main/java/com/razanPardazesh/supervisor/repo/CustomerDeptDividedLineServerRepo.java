package com.razanPardazesh.supervisor.repo;

import android.content.Context;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;
import com.razanPardazesh.supervisor.EditedCustomerListActivity;
import com.razanPardazesh.supervisor.model.wrapper.CustomerDeptDividedLineAnswer;
import com.razanPardazesh.supervisor.repo.iRepo.ICustomerInfo;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Zikey on 20/02/2017.
 */

public class CustomerDeptDividedLineServerRepo implements ICustomerInfo {

    private final String CUSTOMER_DEPT_DIVIDED_LINDE_WEBMETOD = "getCustomerDeptDividedLine";
    private final String Key_TOKEN = "TokenID";
    private final String Key_CUSTOMER_CODEMARKAZ = "CustomerCode";
    private PreferenceHelper preferenceHelper;

    @Override
    public CustomerDeptDividedLineAnswer getCustomerDept(Context context, long CustomerCode) {

        preferenceHelper = new PreferenceHelper(context);
        String token = preferenceHelper.getString(PreferenceHelper.TOKEN_ID);
        String url = "http://" + preferenceHelper.getString(NetworkTools.URL);
        HashMap<String, Object> datas = new HashMap<>();

        datas.put(Key_TOKEN, token);
        datas.put(Key_CUSTOMER_CODEMARKAZ, CustomerCode);

        CustomerDeptDividedLineAnswer answer = new CustomerDeptDividedLineAnswer();

        try {

            String request = NetworkTools.CallSoapMethod(url, CUSTOMER_DEPT_DIVIDED_LINDE_WEBMETOD, datas).getPropertyAsString(0);
            if (request == null) {
                answer.setMessage("خطا در دریافت اطلاعات از سرور answer is empty ");
                return answer;
            }

            JSONObject jsonObject = new JSONObject(request);
            answer.fillByJson(jsonObject);

        } catch (Exception ex) {
            answer.setMessage(" خطا در دریافت اطلاعات از سرور  " +ex.getMessage().toString());
            return answer;

        }

        return answer;

    }
}
