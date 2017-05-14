package com.razanPardazesh.supervisor.model.wrapper;

import com.razanPardazesh.supervisor.model.CustomerRequestEdit;
import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zikey on 13/02/2017.
 */

public class CustomerEditAnswer extends ServerAnswer {

    private final String KEY_EDITED_CUSTOMER = "ec";
    private CustomerRequestEdit customerRequestEdit;

    public CustomerRequestEdit getCustomerRequestEdit() {
        return customerRequestEdit;
    }

    public void setCustomerRequestEdit(CustomerRequestEdit customerRequestEdit) {
        this.customerRequestEdit = customerRequestEdit;
    }

    @Override
    public void fillByJson(JSONObject jsonObject) {
        super.fillByJson(jsonObject);

        if (jsonObject == null) return;

        if (!jsonObject.has(KEY_EDITED_CUSTOMER))
            return;

        try {

            CustomerRequestEdit item = new CustomerRequestEdit();
            JSONObject o = (jsonObject.getJSONObject(KEY_EDITED_CUSTOMER));
            item.fillByJson(o);
            setCustomerRequestEdit(item);

        } catch (Exception ex) {

            LogWrapper.loge("UserAnswer_fillByJson :", ex);
        }
    }

    @Override
    public ArrayList parseList(JSONArray array) {
        return null;
    }
}

