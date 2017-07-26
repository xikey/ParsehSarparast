package com.razanPardazesh.supervisor.model.wrapper;

import com.google.gson.annotations.SerializedName;
import com.razanPardazesh.supervisor.model.Region;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Zikey on 26/07/2017.
 */

public class RegionsAnswer extends ServerAnswer {

    @SerializedName("r")
    ArrayList<Region> regions;


    public ArrayList<Region> getRegions() {
        return regions;
    }

    public void setRegions(ArrayList<Region> regions) {
        this.regions = regions;
    }

    @Override
    public ArrayList parseList(JSONArray array) {
        return null;
    }
}
