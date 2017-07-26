package com.razanPardazesh.supervisor.model;

import com.google.gson.annotations.SerializedName;
import com.razanPardazesh.supervisor.model.user.Customer;
import com.razanPardazesh.supervisor.model.user.Visitor;

import java.util.List;

/**
 * Created by Zikey on 26/07/2017.
 */

public class Region {

    @SerializedName("i")
    private long id = 0;
    @SerializedName("n")
    private String name = null;
    @SerializedName("vs")
    private List<Visitor> visitors = null;
    @SerializedName("vc")
    private long visitorsCount = 0;
    @SerializedName("cc")
    private long customerCount = 0;
    @SerializedName("cs")
    private List<Customer> customers = null;
    @SerializedName("l")
    private int level = 0;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Visitor> getVisitors() {
        return visitors;
    }

    public void setVisitors(List<Visitor> visitors) {
        this.visitors = visitors;
    }

    public long getVisitorsCount() {
        return visitorsCount;
    }

    public void setVisitorsCount(long visitorsCount) {
        this.visitorsCount = visitorsCount;
    }

    public long getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(long customerCount) {
        this.customerCount = customerCount;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
