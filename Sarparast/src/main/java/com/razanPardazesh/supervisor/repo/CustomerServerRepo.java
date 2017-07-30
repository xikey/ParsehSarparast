package com.razanPardazesh.supervisor.repo;

import android.content.Context;

import com.razanPardazesh.supervisor.model.user.Customer;
import com.razanPardazesh.supervisor.model.wrapper.CustomerAnswer;
import com.razanPardazesh.supervisor.model.wrapper.CustomersAnswer;
import com.razanPardazesh.supervisor.repo.apiClient.ServerApiClient;
import com.razanPardazesh.supervisor.repo.iRepo.ICustomer;
import com.razanPardazesh.supervisor.repo.retrofitCalls.ICustomerApi;
import com.razanPardazesh.supervisor.repo.tools.IRepoCallBack;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Zikey on 29/07/2017.
 */

public class CustomerServerRepo implements ICustomer {
    @Override
    public void cancel(Context c) {

    }

    @Override
    public void get(Context context, Object id, IRepoCallBack<CustomerAnswer> callBack) {

    }

    @Override
    public void insert(Context context, CustomerAnswer newObject, IRepoCallBack<CustomerAnswer> callBack) {

    }

    @Override
    public void update(Context context, Object id, CustomerAnswer updateObject, IRepoCallBack callBack) {

    }

    @Override
    public void delete(Context context, Object id, IRepoCallBack<CustomerAnswer> callBack) {

    }

    @Override
    public void getCustomers(Context context, final long regionId, int regionLevel, long visitorCode, String keySearch, int rowCount, long lastIndex, final IRepoCallBack callBack) {
        ICustomerApi customerApi = ServerApiClient.getClientWithHeader(context).create(ICustomerApi.class);
        Call<CustomersAnswer> customersAnswerCall = customerApi.getRegions(regionId, visitorCode, regionLevel, keySearch, lastIndex, rowCount);
        customersAnswerCall.enqueue(new Callback<CustomersAnswer>() {
            @Override
            public void onResponse(Call<CustomersAnswer> call, Response<CustomersAnswer> response) {

                CustomersAnswer answer = new CustomersAnswer();

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
                } else if (response.body().getIsSuccess() != 1) {
                    answer.setIsSuccess(0);
                    answer.setMessage("اطلاعاتی جهت نمایش وجود ندارد");
                    callBack.onAnswer(answer);
                    return;
                }

                ArrayList<Customer> customers = response.body().getCustomers();
                if (customers == null || customers.size() == 0) {
                    answer.setIsSuccess(0);
                    answer.setMessage("اطلاعاتی جهت نمایش وجود ندارد");
                    callBack.onAnswer(answer);
                    return;
                }

                answer.setCustomers(customers);
                answer.setIsSuccess(1);
                answer.setHasMore(response.body().getHasMore());

               callBack.onAnswer(answer);

            }

            @Override
            public void onFailure(Call<CustomersAnswer> call, Throwable throwable) {
                callBack.onError(throwable);
            }
        });
    }
}
