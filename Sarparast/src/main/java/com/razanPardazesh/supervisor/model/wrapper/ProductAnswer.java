package com.razanPardazesh.supervisor.model.wrapper;

import com.razanPardazesh.supervisor.model.Product;
import com.razanPardazesh.supervisor.tools.LogWrapper;

import org.json.JSONObject;

/**
 * Created by Zikey on 06/11/2016.
 */

public class ProductAnswer extends ServerAnswer {

    private static final String KEY_prices = "pr";

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    private Product product;

    @Override
    public void fillByJson(JSONObject jsonObject) {
        super.fillByJson(jsonObject);

        if (jsonObject.has(KEY_prices)) {

            try {

                product = new Product();
                this.product.fillByJson(jsonObject.getJSONObject(KEY_prices));

            } catch (Exception ex) {
                LogWrapper.loge("ProductAnswer_fillByJson_Exception: ", ex);
            }

        }


    }
}
