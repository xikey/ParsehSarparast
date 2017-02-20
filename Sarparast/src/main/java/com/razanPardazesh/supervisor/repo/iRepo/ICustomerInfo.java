package com.razanPardazesh.supervisor.repo.iRepo;

import android.content.Context;

import com.razanPardazesh.supervisor.model.wrapper.CustomerDeptDividedLineAnswer;

/**
 * Created by Zikey on 19/02/2017.
 */

public interface ICustomerInfo {
    CustomerDeptDividedLineAnswer getCustomerDept(Context context, long CustomerCode);
}
