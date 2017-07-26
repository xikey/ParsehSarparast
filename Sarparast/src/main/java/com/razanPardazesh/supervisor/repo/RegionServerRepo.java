package com.razanPardazesh.supervisor.repo;

import android.content.Context;

import com.razanPardazesh.supervisor.model.Region;
import com.razanPardazesh.supervisor.model.wrapper.RegionAnswer;
import com.razanPardazesh.supervisor.model.wrapper.RegionsAnswer;
import com.razanPardazesh.supervisor.repo.apiClient.ServerApiClient;
import com.razanPardazesh.supervisor.repo.iRepo.IRegion;
import com.razanPardazesh.supervisor.repo.retrofitCalls.IRegionApi;
import com.razanPardazesh.supervisor.repo.tools.IRepoCallBack;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Zikey on 26/07/2017.
 */

public class RegionServerRepo implements IRegion {
    @Override
    public void cancel(Context c) {

    }

    @Override
    public void get(Context context, Object id, IRepoCallBack<RegionAnswer> callBack) {

    }

    @Override
    public void insert(Context context, RegionAnswer newObject, IRepoCallBack<RegionAnswer> callBack) {

    }

    @Override
    public void update(Context context, Object id, RegionAnswer updateObject, IRepoCallBack callBack) {

    }

    @Override
    public void delete(Context context, Object id, IRepoCallBack<RegionAnswer> callBack) {

    }


    @Override
    public void getDailyPaths(Context context, String date, String keySearch, final IRepoCallBack callBack) {

        IRegionApi regionApi = ServerApiClient.getClientWithHeader(context).create(IRegionApi.class);
        Call<RegionsAnswer> regionsAnswerCall = regionApi.getRegions(keySearch, date);
        regionsAnswerCall.enqueue(new Callback<RegionsAnswer>() {
            @Override
            public void onResponse(Call<RegionsAnswer> call, Response<RegionsAnswer> response) {

                RegionsAnswer answer = new RegionsAnswer();

                // TODO: 26/07/2017 ERROR FOR ALL RETURN
                if (response == null) {
                    answer.setIsSuccess(0);
                    answer.setMessage("اطلاعاتی جهت نمایش وجود ندارد");
                    callBack.onAnswer(answer);
                    return;
                } else if (response.body() == null) {
                    answer.setIsSuccess(0);
                    answer.setMessage("اطلاعاتی جهت نمایش وجود ندارد");
                    callBack.onAnswer(answer);
                    return;
                } else if (response.body().getMessage() != null) {
                    answer.setIsSuccess(0);
                    answer.setMessage("اطلاعاتی جهت نمایش وجود ندارد");
                    callBack.onAnswer(answer);
                    return;
                } else if (response.body().getIsSuccess()!=1) {
                    answer.setIsSuccess(0);
                    answer.setMessage("اطلاعاتی جهت نمایش وجود ندارد");
                    callBack.onAnswer(answer);
                    return;
                }

                ArrayList<Region> regions = response.body().getRegions();

                if (regions == null || regions.size() == 0) {
                    answer.setIsSuccess(0);
                    answer.setMessage("اطلاعاتی جهت نمایش وجود ندارد");
                    callBack.onAnswer(answer);
                    return;
                }


                answer.setRegions(regions);
                answer.setIsSuccess(1);

                callBack.onAnswer(answer);


            }

            @Override
            public void onFailure(Call<RegionsAnswer> call, Throwable throwable) {

                callBack.onError(throwable);
            }
        });

    }
}
