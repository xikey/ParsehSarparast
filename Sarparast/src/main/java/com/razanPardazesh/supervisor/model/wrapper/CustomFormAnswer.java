package com.razanPardazesh.supervisor.model.wrapper;

import com.razanPardazesh.supervisor.model.checkCustomer.CustomForm;
import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zikey on 11/05/2017.
 */

public class CustomFormAnswer extends ServerAnswer {

    private static final String KEY_CUSTOM_FORM = "f";

    CustomForm customForm = null;

    public CustomForm getCustomForm() {
        return customForm;
    }

    public void setCustomForm(CustomForm customForm) {
        this.customForm = customForm;
    }

    @Override
    public void fillByJson(JSONObject jsonObject) {
        super.fillByJson(jsonObject);

        if (jsonObject == null)
            return;

        if (jsonObject.has(KEY_CUSTOM_FORM)) {
            try {

                customForm = new CustomForm();
                this.customForm.fillByJson(jsonObject.getJSONObject(KEY_CUSTOM_FORM));
            } catch (Exception ex) {
                LogWrapper.loge("CustomFormAnswer_fillByJson :", ex);
            }
        }

    }

    @Override
    public ArrayList parseList(JSONArray array) {
        return null;
    }
}
