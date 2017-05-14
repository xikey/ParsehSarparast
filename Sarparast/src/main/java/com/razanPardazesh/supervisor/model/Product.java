package com.razanPardazesh.supervisor.model;

import android.content.Context;

import com.razanPardazesh.supervisor.model.interfaces.IJson;
import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zikey on 06/11/2016.
 */

public class Product implements IJson {

    private  final String KEY_p1 = "p1";
    private  final String KEY_p2 = "p2";
    private  final String KEY_p3 = "p3";
    private  final String KEY_p4 = "p4";
    private  final String KEY_p5 = "p5";
    private  final String KEY_p6 = "p6";
    private  final String KEY_p7 = "p7";
    private  final String KEY_p8 = "p8";
    private  final String KEY_p9 = "p9";
    private  final String KEY_p10 = "p10";
    private  final String KEY_p11 = "p11";
    private  final String KEY_p12 = "p12";
    private  final String KEY_p13 = "p13";
    private  final String KEY_p14 = "p14";
    private  final String KEY_p15 = "p15";

    private Long price1=0l;
    private Long price2=0l;
    private Long price3=0l;
    private Long price4=0l;
    private Long price5=0l;
    private Long price6=0l;
    private Long price7=0l;
    private Long price8=0l;
    private Long price9=0l;
    private Long price10=0l;
    private Long price11=0l;
    private Long price12=0l;
    private Long price13=0l;
    private Long price14=0l;
    private Long price15=0l;


    public Long getPrice1() {
        return price1;
    }

    public void setPrice1(Long price1) {
        this.price1 = price1;
    }

    public Long getPrice2() {
        return price2;
    }

    public void setPrice2(Long price2) {
        this.price2 = price2;
    }

    public Long getPrice3() {
        return price3;
    }

    public void setPrice3(Long price3) {
        this.price3 = price3;
    }

    public Long getPrice4() {
        return price4;
    }

    public void setPrice4(Long price4) {
        this.price4 = price4;
    }

    public Long getPrice5() {
        return price5;
    }

    public void setPrice5(Long price5) {
        this.price5 = price5;
    }

    public Long getPrice6() {
        return price6;
    }

    public void setPrice6(Long price6) {
        this.price6 = price6;
    }

    public Long getPrice7() {
        return price7;
    }

    public void setPrice7(Long price7) {
        this.price7 = price7;
    }

    public Long getPrice8() {
        return price8;
    }

    public void setPrice8(Long price8) {
        this.price8 = price8;
    }

    public Long getPrice9() {
        return price9;
    }

    public void setPrice9(Long price9) {
        this.price9 = price9;
    }

    public Long getPrice10() {
        return price10;
    }

    public void setPrice10(Long price10) {
        this.price10 = price10;
    }

    public Long getPrice11() {
        return price11;
    }

    public void setPrice11(Long price11) {
        this.price11 = price11;
    }

    public Long getPrice12() {
        return price12;
    }

    public void setPrice12(Long price12) {
        this.price12 = price12;
    }

    public Long getPrice13() {
        return price13;
    }

    public void setPrice13(Long price13) {
        this.price13 = price13;
    }

    public Long getPrice14() {
        return price14;
    }

    public void setPrice14(Long price14) {
        this.price14 = price14;
    }

    public Long getPrice15() {
        return price15;
    }

    public void setPrice15(Long price15) {
        this.price15 = price15;
    }

    @Override
    public void fillByJson(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }

        if (jsonObject.has(KEY_p1)) {
            try {
                setPrice1(jsonObject.getLong(KEY_p1));
            } catch (JSONException e) {
                LogWrapper.loge("fillByJson: price1: ", e);
            }
        }

        if (jsonObject.has(KEY_p2)){
            try {
                setPrice2(jsonObject.getLong(KEY_p2));
            } catch (Exception ex) {
                LogWrapper.loge("fillByJson: price1 ", ex);
            }

        }
        if (jsonObject.has(KEY_p3)){
            try {
                setPrice3(jsonObject.getLong(KEY_p3));
            } catch (Exception ex) {
                LogWrapper.loge("fillByJson: price3", ex);
            }

        }  if (jsonObject.has(KEY_p4)){
            try {
                setPrice4(jsonObject.getLong(KEY_p4));
            } catch (Exception ex) {
                LogWrapper.loge("fillByJson: price4", ex);
            }

        }  if (jsonObject.has(KEY_p5)){
            try {
                setPrice5(jsonObject.getLong(KEY_p5));
            } catch (Exception ex) {
                LogWrapper.loge("fillByJson: price5", ex);
            }

        }  if (jsonObject.has(KEY_p6)){
            try {
                setPrice6(jsonObject.getLong(KEY_p6));
            } catch (Exception ex) {
                LogWrapper.loge("fillByJson: price6", ex);
            }

        }  if (jsonObject.has(KEY_p7)){
            try {
                setPrice7(jsonObject.getLong(KEY_p7));
            } catch (Exception ex) {
                LogWrapper.loge("fillByJson: price7", ex);
            }

        }  if (jsonObject.has(KEY_p8)){
            try {
                setPrice8(jsonObject.getLong(KEY_p8));
            } catch (Exception ex) {
                LogWrapper.loge("fillByJson: price8", ex);
            }

        }  if (jsonObject.has(KEY_p9)){
            try {
                setPrice9(jsonObject.getLong(KEY_p9));
            } catch (Exception ex) {
                LogWrapper.loge("fillByJson: price9", ex);
            }

        }  if (jsonObject.has(KEY_p10)){
            try {
                setPrice10(jsonObject.getLong(KEY_p10));
            } catch (Exception ex) {
                LogWrapper.loge("fillByJson: price10", ex);
            }

        }  if (jsonObject.has(KEY_p11)){
            try {
                setPrice11(jsonObject.getLong(KEY_p11));
            } catch (Exception ex) {
                LogWrapper.loge("fillByJson: price11", ex);
            }

        }  if (jsonObject.has(KEY_p12)){
            try {
                setPrice12(jsonObject.getLong(KEY_p12));
            } catch (Exception ex) {
                LogWrapper.loge("fillByJson: price12", ex);
            }

        }  if (jsonObject.has(KEY_p13)){
            try {
                setPrice13(jsonObject.getLong(KEY_p13));
            } catch (Exception ex) {
                LogWrapper.loge("fillByJson: price13", ex);
            }

        }  if (jsonObject.has(KEY_p14)){
            try {
                setPrice14(jsonObject.getLong(KEY_p14));
            } catch (Exception ex) {
                LogWrapper.loge("fillByJson: price14", ex);
            }

        }  if (jsonObject.has(KEY_p15)){
            try {
                setPrice15(jsonObject.getLong(KEY_p15));
            } catch (Exception ex) {
                LogWrapper.loge("fillByJson: price15", ex);
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
}
