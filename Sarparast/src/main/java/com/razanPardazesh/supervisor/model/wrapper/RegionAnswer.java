package com.razanPardazesh.supervisor.model.wrapper;

import com.google.gson.annotations.SerializedName;
import com.razanPardazesh.supervisor.model.Region;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Zikey on 26/07/2017.
 */

public class RegionAnswer extends ServerAnswer {

    @SerializedName("r")
    Region region;

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public ArrayList parseList(JSONArray array) {
        return null;
    }
}

