package com.razanPardazesh.supervisor.model.wrapper;

import com.razanPardazesh.supervisor.model.CustomerInfo;
import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONObject;

/**
 * Created by Zikey on 19/02/2017.
 */

public class CustomerDeptDividedLineAnswer extends ServerAnswer {


    private final String KEY_CUSTOMER_DEPT = "cd";


    private CustomerInfo customerInfo;

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }


    @Override
    public void fillByJson(JSONObject jsonObject) {
        super.fillByJson(jsonObject);

        if (jsonObject == null) return;

        if (!jsonObject.has(KEY_CUSTOMER_DEPT))
            return;

        try {
            CustomerInfo item = new CustomerInfo();
            JSONObject o = (jsonObject.getJSONObject(KEY_CUSTOMER_DEPT));
            item.fillByJson(o);
            setCustomerInfo(item);

        } catch (Exception ex) {

            LogWrapper.loge("CustomerDeptServerRepo_fillByJson :", ex);
        }
    }
}
