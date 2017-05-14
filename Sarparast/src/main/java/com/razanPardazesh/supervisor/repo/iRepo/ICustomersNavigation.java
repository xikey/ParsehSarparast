package com.razanPardazesh.supervisor.repo.iRepo;

import android.content.Context;

import com.razanPardazesh.supervisor.model.wrapper.CustomersAroundMeAnswer;

/**
 * Created by Zikey on 06/03/2017.
 */

public interface ICustomersNavigation {

    CustomersAroundMeAnswer getAroundCustomers(Context context, String lt, String  ln);
}
