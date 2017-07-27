package com.razanPardazesh.supervisor.tools;


import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;

import rx.subjects.BehaviorSubject;


/**
 * Created by Zikey on 30/05/2017.
 */

public class RxSearch {

    public static BehaviorSubject<String> fromSearchView(@NonNull final SearchView searchView) {
        final BehaviorSubject<String> subject = BehaviorSubject.create("");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                subject.onCompleted();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                                 subject.onNext(newText);

                return true;
            }
        });

        return subject;
    }
}