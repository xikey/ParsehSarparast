package com.razanPardazesh.supervisor.repo.tools;

import android.content.Context;

import com.razanPardazesh.supervisor.model.wrapper.ServerAnswer;


/**
 * Created by Zikey on 18/07/2017.
 */

public interface IRepo<T extends ServerAnswer> {

    public void cancel(Context c);
    public void get(Context context, Object id, IRepoCallBack<T> callBack);
    public void insert(Context context, T newObject, IRepoCallBack<T> callBack);
    public void update(Context context, Object id, T updateObject, IRepoCallBack callBack);
    public void delete(Context context, Object id, IRepoCallBack<T> callBack);
}
