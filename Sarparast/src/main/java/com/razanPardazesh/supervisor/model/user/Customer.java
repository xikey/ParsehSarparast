package com.razanPardazesh.supervisor.model.user;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.razanPardazesh.supervisor.model.accounting.AccountData;
import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zikey on 10/05/2017.
 */

public class Customer extends IUser  {

    private static final String KEY_CODE_MARKAZ = "c";
    private static final String KEY_NAME = "n";

    @SerializedName("c")
    private long codeMarkaz;
    @SerializedName("n")
    private String name;
    @SerializedName("aC")
    private AccountData accountData;
    @SerializedName("ln")
    private String ln = null;
    @SerializedName("lt")
    private String lt = null;
    @SerializedName("te")
    private String tel = null;
    @SerializedName("mo")
    private String mobile = null;
    @SerializedName("ad")
    private String address = null;
    @SerializedName("v")
    private Visitor visitor;

    public long getCodeMarkaz() {
        return codeMarkaz;
    }

    public void setCodeMarkaz(long codeMarkaz) {
        this.codeMarkaz = codeMarkaz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Customer() {
        setType(3);
    }

    public AccountData getAccountData() {
        return accountData;
    }

    public void setAccountData(AccountData accountData) {
        this.accountData = accountData;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public String getLn() {
        return ln;
    }

    public void setLn(String ln) {
        this.ln = ln;
    }

    public String getLt() {
        return lt;
    }

    public void setLt(String lt) {
        this.lt = lt;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int getType() {
        return super.getType();
    }


    @Override
    public void setType(int type) {
        super.setType(type);
    }

    @Override
    public void fillByJson(JSONObject jsonObject) {
        super.fillByJson(jsonObject);

        if (jsonObject == null)
            return;

        if (jsonObject.has(KEY_CODE_MARKAZ)) {
            try {
                setCodeMarkaz(jsonObject.getLong(KEY_CODE_MARKAZ));
            } catch (JSONException e) {
                LogWrapper.loge("Question_fillByJson :",e );
            }
        }   if (jsonObject.has(KEY_NAME)) {
            try {
                setName(jsonObject.getString(KEY_NAME));
            } catch (JSONException e) {
                LogWrapper.loge("Question_fillByJson :",e );
            }
        }


    }

    @Override
    public JSONObject writeJson(Context context) {

        JSONObject jsonObject = new JSONObject();

        try {
            if(codeMarkaz!=0)
            jsonObject.put(KEY_CODE_MARKAZ,getCodeMarkaz());
            if (name!=null)
            jsonObject.put(KEY_NAME,getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }

    @Override
    public ArrayList parseList(JSONArray array) {
        return null;
    }
}
