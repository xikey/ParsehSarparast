package com.razanPardazesh.supervisor.repo.iRepo;

import android.content.Context;

import com.razanPardazesh.supervisor.model.wrapper.CustomFormAnswer;
import com.razanPardazesh.supervisor.model.wrapper.ServerAnswer;

import org.json.JSONObject;

/**
 * Created by Zikey on 11/05/2017.
 */

public interface ICustomForm {

    public CustomFormAnswer getCustomForm(Context context);
    public ServerAnswer sendCustomForm(Context context, JSONObject json);
}
