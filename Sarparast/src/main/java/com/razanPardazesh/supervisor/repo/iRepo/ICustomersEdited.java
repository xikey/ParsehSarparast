package com.razanPardazesh.supervisor.repo.iRepo;

import android.content.Context;

import com.razanPardazesh.supervisor.model.wrapper.CustomerEditAnswer;
import com.razanPardazesh.supervisor.model.wrapper.CustomersEditAnswer;
import com.razanPardazesh.supervisor.model.wrapper.ServerAnswer;

import java.util.Date;

/**
 * Created by Zikey on 12/02/2017.
 */

public interface ICustomersEdited {
   CustomersEditAnswer getEditedList(Context context,String key,long firstIndex,long count);
   CustomerEditAnswer getEditedCustomer(Context context, long requestID);
   ServerAnswer setEditedCustomerStatus(Context context, long requestID, String date,int statusCode);
    CustomerEditAnswer getEditedListCount(Context context);

}
