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
    public CustomersEditAnswer getEditedList(Context context,String key,long firstIndex,long count);
    public CustomerEditAnswer getEditedCustomer(Context context, long requestID);
    public ServerAnswer setEditedCustomerStatus(Context context, long requestID, String date,int statusCode);
}
