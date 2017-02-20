package com.razanPardazesh.supervisor.model;

import android.content.Context;

import com.razanPardazesh.supervisor.model.interfaces.IJson;
import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Zikey on 19/02/2017.
 */

public class CustomerInfo implements IJson {

    private final String KEY_MANDEH_LINE1 = "ml1";
    private final String KEY_MANDEH_LINE2 = "ml2";
    private final String KEY_MANDEH_LINE3 = "ml3";
    private final String KEY_MANDEH_LINE4 = "ml4";
    private final String KEY_MANDEH_LINE5 = "ml5";
    private final String KEY_MANDEH_LINE6 = "ml6";
    private final String KEY_MANDEH_LINE7 = "ml7";
    private final String KEY_MANDEH_LINE8 = "ml8";
    private final String KEY_MANDEH_LINE9 = "ml9";
    private final String KEY_MANDEH_LINE10 = "ml10";

    private long mandeh_line1=0;
    private long mandeh_line2=0;
    private long mandeh_line3=0;
    private long mandeh_line4=0;
    private long mandeh_line5=0;
    private long mandeh_line6=0;
    private long mandeh_line7=0;
    private long mandeh_line8=0;
    private long mandeh_line9=0;
    private long mandeh_line10=0;

    public long getMandeh_line1() {
        return mandeh_line1;
    }

    public void setMandeh_line1(long mandeh_line1) {
        this.mandeh_line1 = mandeh_line1;
    }

    public long getMandeh_line2() {
        return mandeh_line2;
    }

    public void setMandeh_line2(long mandeh_line2) {
        this.mandeh_line2 = mandeh_line2;
    }

    public long getMandeh_line3() {
        return mandeh_line3;
    }

    public void setMandeh_line3(long mandeh_line3) {
        this.mandeh_line3 = mandeh_line3;
    }

    public long getMandeh_line4() {
        return mandeh_line4;
    }

    public void setMandeh_line4(long mandeh_line4) {
        this.mandeh_line4 = mandeh_line4;
    }

    public long getMandeh_line5() {
        return mandeh_line5;
    }

    public void setMandeh_line5(long mandeh_line5) {
        this.mandeh_line5 = mandeh_line5;
    }

    public long getMandeh_line6() {
        return mandeh_line6;
    }

    public void setMandeh_line6(long mandeh_line6) {
        this.mandeh_line6 = mandeh_line6;
    }

    public long getMandeh_line7() {
        return mandeh_line7;
    }

    public void setMandeh_line7(long mandeh_line7) {
        this.mandeh_line7 = mandeh_line7;
    }

    public long getMandeh_line8() {
        return mandeh_line8;
    }

    public void setMandeh_line8(long mandeh_line8) {
        this.mandeh_line8 = mandeh_line8;
    }

    public long getMandeh_line9() {
        return mandeh_line9;
    }

    public void setMandeh_line9(long mandeh_line9) {
        this.mandeh_line9 = mandeh_line9;
    }

    public long getMandeh_line10() {
        return mandeh_line10;
    }

    public void setMandeh_line10(long mandeh_line10) {
        this.mandeh_line10 = mandeh_line10;
    }

    @Override
    public void fillByJson(JSONObject jsonObject) {

        if (jsonObject == null) {
            return;
        }

        if (jsonObject.has(KEY_MANDEH_LINE1)) {
            try {
                setMandeh_line1(jsonObject.getLong(KEY_MANDEH_LINE1));
            } catch (JSONException e) {
                LogWrapper.loge("fillByJson: price1: ", e);
            }
        }
        if (jsonObject.has(KEY_MANDEH_LINE2)) {
            try {
                setMandeh_line2(jsonObject.getLong(KEY_MANDEH_LINE2));
            } catch (JSONException e) {
                LogWrapper.loge("fillByJson: price1: ", e);
            }
        }
        if (jsonObject.has(KEY_MANDEH_LINE3)) {
            try {
                setMandeh_line3(jsonObject.getLong(KEY_MANDEH_LINE3));
            } catch (JSONException e) {
                LogWrapper.loge("fillByJson: price1: ", e);
            }
        }
        if (jsonObject.has(KEY_MANDEH_LINE4)) {
            try {
                setMandeh_line4(jsonObject.getLong(KEY_MANDEH_LINE4));
            } catch (JSONException e) {
                LogWrapper.loge("fillByJson: price1: ", e);
            }
        }
        if (jsonObject.has(KEY_MANDEH_LINE5)) {
            try {
                setMandeh_line5(jsonObject.getLong(KEY_MANDEH_LINE5));
            } catch (JSONException e) {
                LogWrapper.loge("fillByJson: price1: ", e);
            }
        }
        if (jsonObject.has(KEY_MANDEH_LINE6)) {
            try {
                setMandeh_line6(jsonObject.getLong(KEY_MANDEH_LINE6));
            } catch (JSONException e) {
                LogWrapper.loge("fillByJson: price1: ", e);
            }
        }
        if (jsonObject.has(KEY_MANDEH_LINE7)) {
            try {
                setMandeh_line7(jsonObject.getLong(KEY_MANDEH_LINE7));
            } catch (JSONException e) {
                LogWrapper.loge("fillByJson: price1: ", e);
            }
        }
        if (jsonObject.has(KEY_MANDEH_LINE8)) {
            try {
                setMandeh_line8(jsonObject.getLong(KEY_MANDEH_LINE8));
            } catch (JSONException e) {
                LogWrapper.loge("fillByJson: price1: ", e);
            }
        }
        if (jsonObject.has(KEY_MANDEH_LINE9)) {
            try {
                setMandeh_line9(jsonObject.getLong(KEY_MANDEH_LINE9));
            } catch (JSONException e) {
                LogWrapper.loge("fillByJson: price1: ", e);
            }
        }
        if (jsonObject.has(KEY_MANDEH_LINE10)) {
            try {
                setMandeh_line10(jsonObject.getLong(KEY_MANDEH_LINE10));
            } catch (JSONException e) {
                LogWrapper.loge("fillByJson: price1: ", e);
            }

        }
    }

    @Override
    public JSONObject writeJson(Context context) {
        return null;
    }

}
