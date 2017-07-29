package com.razanPardazesh.supervisor.repo.retrofitCalls;

import com.razanPardazesh.supervisor.model.wrapper.CustomersAnswer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Zikey on 29/07/2017.
 */

public interface ICustomerApi {

    @GET("regions/4/{region_id}/customers")
    Call<CustomersAnswer> getRegions(@Path("region_id")
                                             long regionID,
                                     @Query("visitor_codeMarkaz")
                                             long visitorCode,
                                     @Query("lvl")
                                             int level,
                                     @Query("key")
                                             String key,
                                     @Query("lastIndex")
                                             long lastIndex,
                                     @Query("count")
                                             int count

    );
}
