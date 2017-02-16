package com.razanPardazesh.supervisor.repo;

import android.content.Context;

import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;
import com.razanPardazesh.supervisor.model.wrapper.ReportAnswer;
import com.razanPardazesh.supervisor.model.wrapper.ReportsAnswer;
import com.razanPardazesh.supervisor.repo.iRepo.IReport;
import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Zikey on 09/11/2016.
 */

public class ReportServerRepo implements IReport {

    private final String MAIN_COVERAGE_METHOD = "mainCoveragePercent";
    private final String VISITORS_COVERAGE_METHOD = "visitorCoveragePercentage";
    private PreferenceHelper preferenceHelper;

    @Override
    public ReportAnswer mainCoveragePercent(Context context, String key, long lastIndex, int Count) {

        preferenceHelper = new PreferenceHelper(context);
        HashMap<String, Object> datas = new HashMap<>();
        datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));

        ReportAnswer reportAnswer = new ReportAnswer();

        try {
            String request = NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), MAIN_COVERAGE_METHOD, datas).getPropertyAsString(0);

            if (request == null) {
                reportAnswer.setMessage("empty");
            }

            JSONObject jsonObject = new JSONObject(request);
            reportAnswer.fillByJson(jsonObject);


        } catch (Exception e) {
            LogWrapper.loge("ProductServerRepo : priceLevels ", e);
            reportAnswer.setIsSuccess(0);
            return reportAnswer;
        }

        return reportAnswer;
    }

    @Override
    public ReportsAnswer visitorsCoveragePercent(Context context, String key, long lastIndex, int Count) {
        preferenceHelper = new PreferenceHelper(context);
        HashMap<String, Object> datas = new HashMap<>();
        datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));

        ReportsAnswer reportsAnswer = new ReportsAnswer();

        try {
            String request = NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), VISITORS_COVERAGE_METHOD, datas).getPropertyAsString(0);

            if (request == null) {
                reportsAnswer.setMessage("empty");

            }

            JSONObject jsonObject = new JSONObject(request);

            reportsAnswer.fillByJson(jsonObject);


        } catch (Exception e) {
            LogWrapper.loge("ProductServerRepo : priceLevels ", e);
            reportsAnswer.setIsSuccess(0);
            return reportsAnswer;
        }

        return reportsAnswer;

    }
}
