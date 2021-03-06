package com.razanPardazesh.supervisor.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.razanPardazesh.supervisor.model.interfaces.IJson;
import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zikey on 12/02/2017.
 */

public class CustomerRequestEdit implements IJson, Parcelable {

    private final String KEY_ID = "id";
    private final String KEY_CUSTOMER_NAME = "cn";
    private final String KEY_CUSTOMER_ID = "ci";
    private final String KEY_CUSTOMER_LN = "ln";
    private final String KEY_CUSTOMER_LT = "lt";
    private final String KEY_CUSTOMER_TEL = "ct";
    private final String KEY_CUSTOMER_MOBILE = "cm";
    private final String KEY_CUSTOMER_ADDRESS = "ca";
    private final String KEY_VISITOR_CODE = "vc";
    private final String KEY_VISITOR_NAME = "vn";
    private final String KEY_UPDATE_DATE = "ud";
    private final String KEY_STATUS = "s";
    private final String KEY_APPROVE_DATE = "ad";
    private final String KEY_APPROVE_CODEMARKAZ = "ac";
    private final String KEY_REJECT_DATE = "rd";
    private final String KEY_REJECTOR_CODEMARKAZ = "rc";
    private final String KEY_CUSTOMER_FAMILY = "cf";
    private final String KEY_STORE_NAME = "sn";
    private final String KEY_CODE_MELLI = "cml";
    private final String KEY_COUNT="count";

    private long id;
    private String customerName = null;
    private String customerFamily = null;
    private long customerId;
    private double customerLN;
    private double customerLT;
    private String customerTel;
    private String customerMobile;
    private String customerAddress;
    private long visitorCodeMarkaz;
    private String updateDate;
    private int status;
    private String approveDate;
    private long approveCodeMarkaz;
    private String visitorName;
    private String rejectDate;
    private long rejectorCodeMarkaz;
    private String storeName;
    private long codeMelli;
    private long count;



    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public double getCustomerLN() {
        return customerLN;
    }

    public void setCustomerLN(double customerLN) {
        this.customerLN = customerLN;
    }

    public double getCustomerLT() {
        return customerLT;
    }

    public void setCustomerLT(double customerLT) {
        this.customerLT = customerLT;
    }

    public String getCustomerTel() {
        return customerTel;
    }

    public void setCustomerTel(String customerTel) {
        this.customerTel = customerTel;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public long getVisitorCodeMarkaz() {
        return visitorCodeMarkaz;
    }

    public void setVisitorCodeMarkaz(long visitorCodeMarkaz) {
        this.visitorCodeMarkaz = visitorCodeMarkaz;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(String approveDate) {
        this.approveDate = approveDate;
    }

    public long getApproveCodeMarkaz() {
        return approveCodeMarkaz;
    }

    public void setApproveCodeMarkaz(long approveCodeMarkaz) {
        this.approveCodeMarkaz = approveCodeMarkaz;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getRejectDate() {
        return rejectDate;
    }

    public void setRejectDate(String rejectDate) {
        this.rejectDate = rejectDate;
    }

    public long getRejectorCodeMarkaz() {
        return rejectorCodeMarkaz;
    }

    public void setRejectorCodeMarkaz(long rejectorCodeMarkaz) {
        this.rejectorCodeMarkaz = rejectorCodeMarkaz;
    }

    public String getCustomerFamily() {
        return customerFamily;
    }

    public void setCustomerFamily(String customerFamily) {
        this.customerFamily = customerFamily;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public long getCodeMelli() {
        return codeMelli;
    }

    public void setCodeMelli(long codeMelli) {
        this.codeMelli = codeMelli;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public void fillByJson(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }

        if (jsonObject.has(KEY_CUSTOMER_NAME)) {
            try {
                setCustomerName(jsonObject.getString(KEY_CUSTOMER_NAME));
            } catch (JSONException e) {
                LogWrapper.loge("CustomerRequestEdit_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_CUSTOMER_ID)) {
            try {
                setCustomerId(jsonObject.getLong(KEY_CUSTOMER_ID));
            } catch (JSONException e) {
                LogWrapper.loge("CustomerRequestEdit_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_CUSTOMER_LN)) {
            try {
                setCustomerLN(jsonObject.getLong(KEY_CUSTOMER_LN));
            } catch (JSONException e) {
                LogWrapper.loge("CustomerRequestEdit_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_CUSTOMER_LT)) {
            try {
                setCustomerLT(jsonObject.getLong(KEY_CUSTOMER_LT));
            } catch (JSONException e) {
                LogWrapper.loge("CustomerRequestEdit_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_CUSTOMER_LT)) {
            try {
                setCustomerLT(jsonObject.getLong(KEY_CUSTOMER_LT));
            } catch (JSONException e) {
                LogWrapper.loge("CustomerRequestEdit_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_CUSTOMER_TEL)) {
            try {
                setCustomerTel(jsonObject.getString(KEY_CUSTOMER_TEL));
            } catch (JSONException e) {
                LogWrapper.loge("CustomerRequestEdit_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_CUSTOMER_MOBILE)) {
            try {
                setCustomerMobile(jsonObject.getString(KEY_CUSTOMER_MOBILE));
            } catch (JSONException e) {
                LogWrapper.loge("CustomerRequestEdit_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_CUSTOMER_ADDRESS)) {
            try {
                setCustomerAddress(jsonObject.getString(KEY_CUSTOMER_ADDRESS));
            } catch (JSONException e) {
                LogWrapper.loge("CustomerRequestEdit_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_VISITOR_CODE)) {
            try {
                setVisitorCodeMarkaz(jsonObject.getLong(KEY_VISITOR_CODE));
            } catch (JSONException e) {
                LogWrapper.loge("CustomerRequestEdit_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_VISITOR_NAME)) {
            try {
                setVisitorName(jsonObject.getString(KEY_VISITOR_NAME));
            } catch (JSONException e) {
                LogWrapper.loge("CustomerRequestEdit_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_UPDATE_DATE)) {
            try {
                setUpdateDate(jsonObject.getString(KEY_UPDATE_DATE));
            } catch (JSONException e) {
                LogWrapper.loge("CustomerRequestEdit_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_STATUS)) {
            try {
                setStatus(jsonObject.getInt(KEY_STATUS));
            } catch (JSONException e) {
                LogWrapper.loge("CustomerRequestEdit_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_APPROVE_DATE)) {
            try {
                setApproveDate(jsonObject.getString(KEY_APPROVE_DATE));
            } catch (JSONException e) {
                LogWrapper.loge("CustomerRequestEdit_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_APPROVE_CODEMARKAZ)) {
            try {
                setApproveCodeMarkaz(jsonObject.getLong(KEY_APPROVE_CODEMARKAZ));
            } catch (JSONException e) {
                LogWrapper.loge("CustomerRequestEdit_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_REJECT_DATE)) {
            try {
                setRejectDate(jsonObject.getString(KEY_REJECT_DATE));
            } catch (JSONException e) {
                LogWrapper.loge("CustomerRequestEdit_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_REJECTOR_CODEMARKAZ)) {
            try {
                setRejectorCodeMarkaz(jsonObject.getLong(KEY_REJECTOR_CODEMARKAZ));
            } catch (JSONException e) {
                LogWrapper.loge("CustomerRequestEdit_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_ID)) {
            try {
                setId(jsonObject.getLong(KEY_ID));
            } catch (JSONException e) {
                LogWrapper.loge("CustomerRequestEdit_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_CUSTOMER_FAMILY)) {
            try {
                setCustomerFamily(jsonObject.getString(KEY_CUSTOMER_FAMILY));
            } catch (JSONException e) {
                LogWrapper.loge("CustomerRequestEdit_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_CODE_MELLI)) {
            try {
                setCodeMelli(jsonObject.getLong(KEY_CODE_MELLI));
            } catch (JSONException e) {
                LogWrapper.loge("CustomerRequestEdit_fillByJson :", e);
            }
        }
        if (jsonObject.has(KEY_STORE_NAME)) {
            try {
                setStoreName(jsonObject.getString(KEY_STORE_NAME));
            } catch (JSONException e) {
                LogWrapper.loge("CustomerRequestEdit_fillByJson :", e);
            }
        }

        if (jsonObject.has(KEY_COUNT)) {
            try {
                setCount(jsonObject.getLong(KEY_COUNT));
            } catch (JSONException e) {
                LogWrapper.loge("CustomerRequestEdit_fillByJson :", e);
            }
        }

    }

    @Override
    public JSONObject writeJson(Context context) {
        return null;
    }

    @Override
    public ArrayList parseList(JSONArray array) {
        return null;
    }


    @Override
    public int describeContents() {
        return 0;
    }


    public CustomerRequestEdit(Parcel in) {

        this.id = in.readLong();
        this.customerName = in.readString();
        this.customerFamily = in.readString();
        this.customerId = in.readLong();
        this.customerLN = in.readDouble();
        this.customerLT = in.readDouble();
        this.customerTel = in.readString();
        this.customerMobile = in.readString();
        this.customerAddress = in.readString();
        this.visitorCodeMarkaz = in.readLong();
        this.updateDate = in.readString();
        this.status = in.readInt();
        this.storeName = in.readString();
        this.codeMelli = in.readLong();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(id);
        dest.writeString(customerName);
        dest.writeString(customerFamily);
        dest.writeLong(customerId);
        dest.writeDouble(customerLN);
        dest.writeDouble(customerLT);
        dest.writeString(customerTel);
        dest.writeString(customerMobile);
        dest.writeString(customerAddress);
        dest.writeLong(visitorCodeMarkaz);
        dest.writeString(updateDate);
        dest.writeInt(status);
        dest.writeString(storeName);
        dest.writeLong(codeMelli);
    }


    public CustomerRequestEdit() {

    }


    public static final Creator<CustomerRequestEdit> CREATOR = new Creator<CustomerRequestEdit>() {

        @Override
        public CustomerRequestEdit createFromParcel(Parcel in) {
            return new CustomerRequestEdit(in);
        }


        @Override
        public CustomerRequestEdit[] newArray(int size) {
            return new CustomerRequestEdit[size];
        }
    };
}
