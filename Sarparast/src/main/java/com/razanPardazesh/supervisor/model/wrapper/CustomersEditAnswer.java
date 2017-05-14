package com.razanPardazesh.supervisor.model.wrapper;

import com.razanPardazesh.supervisor.model.CustomerRequestEdit;
import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zikey on 12/02/2017.
 */

public class CustomersEditAnswer extends ServerAnswer {

    private final String KEY_EDITED_LIST = "el";
    private ArrayList<CustomerRequestEdit> editedList = null;

    public ArrayList<CustomerRequestEdit> getEditedList() {
        return editedList;
    }

    public void setEditedList(ArrayList<CustomerRequestEdit> editedList) {
        this.editedList = editedList;
    }

    @Override
    public void fillByJson(JSONObject jsonObject) {
        super.fillByJson(jsonObject);

        if (jsonObject.has(KEY_EDITED_LIST)) {
            try {

                editedList = new ArrayList<>();
                JSONArray array = jsonObject.getJSONArray(KEY_EDITED_LIST);
                for (int i = 0; i <= array.length(); i++) {
                    CustomerRequestEdit item = new CustomerRequestEdit();
                    JSONObject obj = array.getJSONObject(i);
                    item.fillByJson(obj);
                    editedList.add(item);
                }
            } catch (Exception ex) {
                LogWrapper.loge("CustomersEditAnswer_fillByJson_Exception: ", ex);
            }

        }
    }

    @Override
    public ArrayList parseList(JSONArray array) {
        return null;
    }
}
