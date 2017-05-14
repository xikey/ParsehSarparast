package com.razanPardazesh.supervisor.repo.iRepo;

import android.content.Context;

import com.razanPardazesh.supervisor.model.wrapper.UnvisitsAnswer;

/**
 * Created by Zikey on 14/05/2017.
 */

public interface IUnvisitRepo  {

    public UnvisitsAnswer getReasons(Context context, long shopCode);
}
