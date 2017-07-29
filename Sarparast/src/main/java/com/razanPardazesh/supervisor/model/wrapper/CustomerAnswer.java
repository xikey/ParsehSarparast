package com.razanPardazesh.supervisor.model.wrapper;

import com.google.gson.annotations.SerializedName;
import com.razanPardazesh.supervisor.model.user.Customer;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Zikey on 29/07/2017.
 */

public class CustomerAnswer extends ServerAnswer {


    @SerializedName("c")
    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public ArrayList parseList(JSONArray array) {
        return null;
    }
}
