package com.razanPardazesh.supervisor.model.wrapper;

import com.razanPardazesh.supervisor.model.Report;
import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zikey on 09/11/2016.
 */

public class ReportAnswer extends ServerAnswer {
    private final String KEY_REPORT = "rp";

    private Report report;

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    @Override
    public void fillByJson(JSONObject jsonObject) {
        super.fillByJson(jsonObject);

        if (jsonObject.has(KEY_REPORT)) {
            try {

                report = new Report();
                this.report.fillByJson(jsonObject.getJSONArray(KEY_REPORT).getJSONObject(0));
            }
            catch (Exception ex) {
                LogWrapper.loge("ReportAnswer_fillByJson_Exception: ", ex);
            }
        }
    }

    @Override
    public ArrayList parseList(JSONArray array) {
        return null;
    }
}
