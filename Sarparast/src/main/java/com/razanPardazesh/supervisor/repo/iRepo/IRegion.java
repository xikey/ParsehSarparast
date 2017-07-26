package com.razanPardazesh.supervisor.repo.iRepo;

import android.content.Context;

import com.razanPardazesh.supervisor.model.wrapper.RegionAnswer;
import com.razanPardazesh.supervisor.model.wrapper.RegionsAnswer;
import com.razanPardazesh.supervisor.repo.tools.IRepo;
import com.razanPardazesh.supervisor.repo.tools.IRepoCallBack;

/**
 * Created by Zikey on 26/07/2017.
 */

public interface IRegion extends IRepo<RegionAnswer> {

    void getDailyPaths(Context context, String date,String keySearch, IRepoCallBack callBack);

}
