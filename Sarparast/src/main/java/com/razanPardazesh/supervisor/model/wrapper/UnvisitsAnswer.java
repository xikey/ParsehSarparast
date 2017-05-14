package com.razanPardazesh.supervisor.model.wrapper;

import com.razanPardazesh.supervisor.model.Unvisit;
import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zikey on 14/05/2017.
 */

public class UnvisitsAnswer extends ServerAnswer {

    private static final String KEY_UNVISIT_ARRAY = "ua";

    private ArrayList<Unvisit> unvisits = null;

    public ArrayList<Unvisit> getUnvisits() {
        return unvisits;
    }


    public void setUnvisits(ArrayList<Unvisit> unvisits) {
        this.unvisits = unvisits;
    }

    @Override
    public void fillByJson(JSONObject jsonObject) {
        super.fillByJson(jsonObject);

        try {

            unvisits = new ArrayList<>();
            JSONArray array = jsonObject.getJSONArray(KEY_UNVISIT_ARRAY);
            for (int i = 0; i <= array.length(); i++) {
                Unvisit item = new Unvisit();
                JSONObject obj = array.getJSONObject(i);
                item.fillByJson(obj);
                unvisits.add(item);
            }
        } catch (Exception ex) {
            LogWrapper.loge("UnvisitAnswer_fillByJson :", ex);
        }
    }

    @Override
    public ArrayList parseList(JSONArray array) {
        return null;
    }
}
