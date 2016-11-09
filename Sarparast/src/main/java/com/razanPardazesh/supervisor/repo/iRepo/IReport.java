package com.razanPardazesh.supervisor.repo.iRepo;

import android.content.Context;

import com.razanPardazesh.supervisor.model.wrapper.ReportAnswer;

/**
 * Created by Zikey on 09/11/2016.
 */

public interface IReport  {
    //key for search

    public ReportAnswer mainCoveragePercent(Context context,String key, long lastIndex, int Count);

    public ReportAnswer visitorsCoveragePercent(Context context,String key, long lastIndex, int Count);
}
