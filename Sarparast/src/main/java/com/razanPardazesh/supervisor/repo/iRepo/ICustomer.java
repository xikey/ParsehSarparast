package com.razanPardazesh.supervisor.repo.iRepo;

import android.content.Context;

import com.razanPardazesh.supervisor.model.wrapper.CustomerAnswer;
import com.razanPardazesh.supervisor.repo.tools.IRepo;
import com.razanPardazesh.supervisor.repo.tools.IRepoCallBack;

/**
 * Created by Zikey on 29/07/2017.
 */

public interface ICustomer  extends IRepo<CustomerAnswer> {

      void getCustomers(Context context,long regionId,int regionLevel,long visitorCode,String keySearch,int rowCount,long lastIndex, IRepoCallBack callBack);
}
