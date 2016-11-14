package com.razanPardazesh.supervisor.repo.iRepo;

import android.content.Context;

import com.razanPardazesh.supervisor.model.LocationData;
import com.razanPardazesh.supervisor.model.wrapper.ServerAnswer;

import java.util.ArrayList;

/**
 * Created by Zikey on 13/11/2016.
 */

public interface INavigation {

    public ServerAnswer sendSupervisorsLocations(Context context, ArrayList<LocationData> locationData);

}
