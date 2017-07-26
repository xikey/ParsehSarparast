package com.razanPardazesh.supervisor.repo.tools;


import com.razanPardazesh.supervisor.model.wrapper.ServerAnswer;

/**
 * Created by Zikey on 18/07/2017.
 */

public interface IRepoCallBack<T extends ServerAnswer> {
    public void onAnswer(T answer);
    public void onError(Throwable error);
    public void onCancel();
    public void onProgress(int p);
}
