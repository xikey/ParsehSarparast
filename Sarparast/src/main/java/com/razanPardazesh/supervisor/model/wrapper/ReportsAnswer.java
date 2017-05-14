package com.razanPardazesh.supervisor.model.wrapper;

import com.razanPardazesh.supervisor.model.Report;
import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zikey on 12/11/2016.
 */

public class ReportsAnswer extends ServerAnswer {
    private final String KEY_REPORT = "rp";


    private Report report;
    ArrayList<Report> reports = new ArrayList<>();

    public ArrayList<Report> getReports() {
        return reports;
    }

    public void setReports(ArrayList<Report> reports) {
        this.reports = reports;
    }


    @Override
    public void fillByJson(JSONObject jsonObject) {
        super.fillByJson(jsonObject);

        if (jsonObject.has(KEY_REPORT)) {
            try {

                report = new Report();
                JSONArray array = jsonObject.getJSONArray(KEY_REPORT);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    Report r = new Report();
                    r.fillByJson(obj);
                    reports.add(r);
                }

            } catch (Exception ex) {
                LogWrapper.loge("ReportAnswer_fillByJson_Exception: ", ex);
            }
        }
    }

    @Override
    public ArrayList parseList(JSONArray array) {
        return null;
    }
}
