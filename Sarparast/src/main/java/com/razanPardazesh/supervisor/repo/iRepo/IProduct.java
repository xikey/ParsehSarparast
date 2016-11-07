package com.razanPardazesh.supervisor.repo.iRepo;

import android.content.Context;

import com.razanPardazesh.supervisor.model.wrapper.ProductAnswer;

/**
 * Created by Zikey on 06/11/2016.
 */

public interface IProduct {

    public ProductAnswer priceLevels(Context context,String productID,String key, long lastIndex, int Count);
}
