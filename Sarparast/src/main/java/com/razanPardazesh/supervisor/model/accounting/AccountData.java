package com.razanPardazesh.supervisor.model.accounting;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Zikey on 29/07/2017.
 */

public class AccountData implements IAccounting {

    @SerializedName("sc")
    /**
     * تعداد فروش
     */
    private long salesCount = 0;
    @SerializedName("sa")
    /**
     * تعداد فروش
     */
    private String salesAmount = null;
    @SerializedName("rc")
    /**
     *تعداد برگشتی
     */
    private long returnedCount = 0;
    @SerializedName("ra")
    /**
     *ریال برگشتی
     */
    private String returnedAmount = null;
    @SerializedName("ncc")
    /**
     *تعدا ناخالص
     */
    private long notClearCount = 0;
    @SerializedName("nca")
    /**
     *ریال نا خالص
     */
    private String notClearAmount = null;
    @SerializedName("ipc")
    /**
     *تعداد در جریان
     */
    private long inProgressCount = 0;
    @SerializedName("ipa")
    /**
     *ریال در جریان
     */
    private String inProgressAmount = null;
    @SerializedName("lc")
    /**
     *تعداد حقوقی شده
     */
    private long legalizedCount = 0;
    @SerializedName("la")
    /**
     *ریال حقوقی شده
     */
    private String legalizedAmount = null;
    @SerializedName("icc")
    /**
     *تعداد وصول
     */
    private long inCommingCount = 0;
    @SerializedName("ica")
    /**
     *ریال وصول
     */
    private String inCommingAmount = null;
    @SerializedName("pc")
    /**
     *تعدا پاس شده
     */
    private long passedCount = 0;
    @SerializedName("pa")
    /**
     *ریال پاس شده
     */
    private String passedAmount = null;
    @SerializedName("b")
    /**
     * مانده حساب
     */
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
