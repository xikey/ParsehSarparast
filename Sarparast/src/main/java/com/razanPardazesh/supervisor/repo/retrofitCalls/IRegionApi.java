package com.razanPardazesh.supervisor.repo.retrofitCalls;

import com.razanPardazesh.supervisor.model.wrapper.RegionsAnswer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Zikey on 26/07/2017.
 */

public interface IRegionApi {

    @GET("targets/visitors/path")
    Call<RegionsAnswer> getRegions(@Query("key")
                                           String key,
                                   @Query("date")
                                           String date
    );

}
