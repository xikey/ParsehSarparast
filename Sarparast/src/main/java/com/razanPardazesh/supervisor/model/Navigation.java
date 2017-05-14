package com.razanPardazesh.supervisor.model;

import android.content.Context;
import android.text.format.DateFormat;

import com.example.zikey.sarparast.Helpers.DeviceInfos;
import com.razanPardazesh.supervisor.model.interfaces.IJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zikey on 13/11/2016.
 */

public class Navigation implements IJson {

    private final String KEY_ID = "id";
    private final String KEY_LATITUDE = "lt";
    private final String KEY_LONGITUDE = "ln";
    private final String KEY_ACCURACY = "ac";
    private final String KEY_DATE = "d";
    private final String KEY_GPSSTATUS = "gs";
    private final String KEY_PROVIDER = "p";
    private final String KEY_SPEED = "s";
    private final String KEY_ISMOCK = "is";
    private final String KEY_DEVICEID = "di";
    private final String KEY_BATTERY = "b";


    @Override
    public void fillByJson(JSONObject jsonObject) {

    }

    @Override
    public JSONObject writeJson(Context context) {
        JSONObject jsonObject = new JSONObject();

        return null;
    }

    @Override
    public ArrayList parseList(JSONArray array) {
        return null;
    }

    public String jsonCreator(ArrayList<LocationData> lastData) {

        JSONArray jsonArray = new JSONArray();

        if (lastData == null || lastData.size() == 0)
            return null;

        for (LocationData locationData : lastData) {
            JSONObject jso = new JSONObject();

            try {
                jso.put(KEY_ID, String.valueOf(locationData.getId()));
                jso.put(KEY_LATITUDE,String.valueOf( locationData.getLatitude()));
                jso.put(KEY_LONGITUDE, String.valueOf(locationData.getLongitude()));
                jso.put(KEY_ACCURACY, String.valueOf(locationData.getAccuracy()));
                jso.put(KEY_DATE, String.valueOf( DateFormat.format("yyyy-MM-dd hh:mms", locationData.getDate()).toString()));
                jso.put(KEY_GPSSTATUS, String.valueOf(locationData.getGpsStatus()));
                jso.put(KEY_PROVIDER,String.valueOf( locationData.getProvider()));
                jso.put(KEY_SPEED,String.valueOf( locationData.getSpeed()));
                jso.put(KEY_ISMOCK, String.valueOf(locationData.getIsMock()));
                jso.put(KEY_DEVICEID,String.valueOf( locationData.getDeviceID()));
                jso.put(KEY_BATTERY, String.valueOf(locationData.getBatteryCharge()));

                jsonArray.put(jso);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return String.valueOf(jsonArray);
    }
}
