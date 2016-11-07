package com.razanPardazesh.supervisor.repo;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.support.v4.util.ArrayMap;


import com.example.zikey.sarparast.Helpers.NetworkTools;
import com.example.zikey.sarparast.Helpers.PreferenceHelper;
import com.razanPardazesh.supervisor.model.Product;
import com.razanPardazesh.supervisor.model.wrapper.ProductAnswer;
import com.razanPardazesh.supervisor.repo.iRepo.IProduct;
import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

/**
 * Created by Zikey on 06/11/2016.
 */

public class ProductServerRepo implements IProduct {

    private final String KEY_WEBMETHOD = "priceLevels";
    private PreferenceHelper preferenceHelper;

    @Override
    public ProductAnswer priceLevels(Context context, String productID, String key, long lastIndex, int Count) {

        preferenceHelper = new PreferenceHelper(context);
        HashMap<String, Object> datas = new HashMap<>();

        datas.put("TokenID", preferenceHelper.getString(PreferenceHelper.TOKEN_ID));
        datas.put("productID", productID);

        ProductAnswer product = new ProductAnswer();
        try {

            String request2 =  NetworkTools.CallSoapMethod("http://" + preferenceHelper.getString(NetworkTools.URL), KEY_WEBMETHOD, datas).getPropertyAsString(0);
            if (request2 == null) {
                product.setMessage("خطا در دریافت اطلاعات از سرور");
                return product;
            }
            JSONObject jsonObject = new JSONObject(request2);
            product.fillByJson(jsonObject);


        } catch (Exception ex) {
            LogWrapper.loge("ProductServerRepo : priceLevels ", ex);
            product.setMessage("خطا در دریافت اطلاعات");
            return product;
        }

        return product;
    }
}
