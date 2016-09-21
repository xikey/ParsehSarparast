package com.example.zikey.sarparast.Helpers;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.example.zikey.sarparast.R;


/**
 * Created by Torabi on 9/8/2016.
 */

public class Indicator extends LinearLayout {

    private ViewPager pager;
    private int pageNumber = 0;
    private int selectedPage = 0;


    public Indicator(Context context) {
        super(context);
        init();
    }

    public Indicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Indicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Indicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        if (isInEditMode())
            return;

        setOrientation(LinearLayout.HORIZONTAL);
    }

    public void setViewPager(ViewPager pager) {
        if (pager == null)
            return;

        this.pager = pager;
        this.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedPage = position;
                for (int i = 0; i < getChildCount(); i++) {
                    getChildAt(i).setAlpha(0.5f);
                }

                getChildAt(position).setAlpha(1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        notifyDatabasChange();
    }

    public void notifyDatabasChange() {
        if (pager == null || pager.getAdapter() == null) {
            selectedPage = 0;
            pageNumber = 0;
            return;
        }

        selectedPage = pager.getCurrentItem();
        pageNumber = pager.getAdapter().getCount();
        initView();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        removeAllViews();

        for (int i = 0; i < pageNumber; i++) {
            View child = new View(getContext());
            LayoutParams params = new LayoutParams(Convertor.toPixcel(10, getContext()), Convertor.toPixcel(10, getContext()));

            if (i != 0) {
                int left = Convertor.toPixcel(5, getContext());
                params.setMargins(left, 0, 0, 0);
            }

            child.setLayoutParams(params);
            child.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.bg_circle_white, null));

            if (selectedPage == i)
                child.setAlpha(1);
            else
                child.setAlpha(0.5f);
            addView(child);
        }
    }

}
