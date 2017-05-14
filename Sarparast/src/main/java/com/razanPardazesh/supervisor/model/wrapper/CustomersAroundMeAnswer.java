package com.razanPardazesh.supervisor.model.wrapper;

import com.razanPardazesh.supervisor.model.CustomerInfo;
import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zikey on 05/03/2017.
 */

public class CustomersAroundMeAnswer extends ServerAnswer {

    private final String KEY_EDITED_LIST = "cms";

    private ArrayList<CustomerInfo> customerArray = null;

    public ArrayList<CustomerInfo> getCustomerArray() {
        return customerArray;
    }

    public void setCustomerArray(ArrayList<CustomerInfo> customerArray) {
        this.customerArray = customerArray;
    }

    @Override
    public void fillByJson(JSONObject jsonObject) {
        super.fillByJson(jsonObject);

        if (jsonObject.has(KEY_EDITED_LIST)) {
            try {
                JSONArray arr =  jsonObject.getJSONArray(KEY_EDITED_LIST);
                if (customerArray == null)
                    customerArray = new ArrayList<>();

                for (int j = 0; j <= arr.length(); j++) {
                    CustomerInfo item = new CustomerInfo();
                    JSONObject obj = arr.getJSONObject(j);
                    item.fillByJson(obj);
                    customerArray.add(item);
                }
            } catch (Exception ex) {
                LogWrapper.loge("CustomersAroundMeAnswer_fillByJson :", ex);
            }
        }
    }

    @Override
    public ArrayList parseList(JSONArray array) {
        return null;
    }

    public void clearList() {
        if (customerArray == null || customerArray.size() == 0)
            return;

        customerArray.clear();
    }
}
