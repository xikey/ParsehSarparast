package com.razanPardazesh.supervisor.model.wrapper;

import com.google.gson.annotations.SerializedName;
import com.razanPardazesh.supervisor.model.user.Customer;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Zikey on 29/07/2017.
 */

public class CustomersAnswer extends ServerAnswer {

    @SerializedName("c")
    ArrayList<Customer> customers ;

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }

    @Override
    public ArrayList parseList(JSONArray array) {
        return null;
    }

}
