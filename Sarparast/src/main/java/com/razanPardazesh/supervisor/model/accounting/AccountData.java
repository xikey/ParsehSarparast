package com.razanPardazesh.supervisor.model.accounting;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Zikey on 29/07/2017.
 */

public class AccountData implements IAccounting {


    /**
     * تعداد فروش
     */
    @SerializedName("sc")
    private long salesCount = 0;

    /**
     * تعداد فروش
     */
    @SerializedName("sa")
    private String salesAmount = null;

    /**
     *تعداد برگشتی
     */
    @SerializedName("rc")
    private long returnedCount = 0;

    /**
     *ریال برگشتی
     */
    @SerializedName("ra")
    private String returnedAmount = null;

    /**
     *تعدا ناخالص
     */
    @SerializedName("ncc")
    private long notClearCount = 0;

    /**
     *ریال نا خالص
     */
    @SerializedName("nca")
    private String notClearAmount = null;

    /**
     *تعداد در جریان
     */
    @SerializedName("ipc")
    private long inProgressCount = 0;

    /**
     *ریال در جریان
     */
    @SerializedName("ipa")
    private String inProgressAmount = null;

    /**
     *تعداد حقوقی شده
     */
    @SerializedName("lc")
    private long legalizedCount = 0;

    /**
     *ریال حقوقی شده
     */
    @SerializedName("la")
    private String legalizedAmount = null;

    /**
     *تعداد وصول
     */
    @SerializedName("icc")
    private long inCommingCount = 0;

    /**
     *ریال وصول
     */
    @SerializedName("ica")
    private String inCommingAmount = null;

    /**
     *تعدا پاس شده
     */
    @SerializedName("pc")
    private long passedCount = 0;

    /**
     *ریال پاس شده
     */
    @SerializedName("pa")
    private String passedAmount = null;

    /**
     * مانده حساب
     */
    @SerializedName("b")
    private String balance = null;


    public String getBalance() {
        return balance;
    }

    public long getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(long salesCount) {
        this.salesCount = salesCount;
    }

    public String getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(String salesAmount) {
        this.salesAmount = salesAmount;
    }

    public long getReturnedCount() {
        return returnedCount;
    }

    public void setReturnedCount(long returnedCount) {
        this.returnedCount = returnedCount;
    }

    public String getReturnedAmount() {
        return returnedAmount;
    }

    public void setReturnedAmount(String returnedAmount) {
        this.returnedAmount = returnedAmount;
    }

    public long getNotClearCount() {
        return notClearCount;
    }

    public void setNotClearCount(long notClearCount) {
        this.notClearCount = notClearCount;
    }

    public String getNotClearAmount() {
        return notClearAmount;
    }

    public void setNotClearAmount(String notClearAmount) {
        this.notClearAmount = notClearAmount;
    }

    public long getInProgressCount() {
        return inProgressCount;
    }

    public void setInProgressCount(long inProgressCount) {
        this.inProgressCount = inProgressCount;
    }

    public String getInProgressAmount() {
        return inProgressAmount;
    }

    public void setInProgressAmount(String inProgressAmount) {
        this.inProgressAmount = inProgressAmount;
    }

    public long getLegalizedCount() {
        return legalizedCount;
    }

    public void setLegalizedCount(long legalizedCount) {
        this.legalizedCount = legalizedCount;
    }

    public String getLegalizedAmount() {
        return legalizedAmount;
    }

    public void setLegalizedAmount(String legalizedAmount) {
        this.legalizedAmount = legalizedAmount;
    }

    public long getInCommingCount() {
        return inCommingCount;
    }

    public void setInCommingCount(long inCommingCount) {
        this.inCommingCount = inCommingCount;
    }

    public String getInCommingAmount() {
        return inCommingAmount;
    }

    public void setInCommingAmount(String inCommingAmount) {
        this.inCommingAmount = inCommingAmount;
    }

    public long getPassedCount() {
        return passedCount;
    }

    public void setPassedCount(long passedCount) {
        this.passedCount = passedCount;
    }

    public String getPassedAmount() {
        return passedAmount;
    }

    public void setPassedAmount(String passedAmount) {
        this.passedAmount = passedAmount;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public Double calculateBalance() {
        return null;
    }
}
