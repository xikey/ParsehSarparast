package com.razanPardazesh.supervisor.tools;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zikey.sarparast.Helpers.FontApplier;


/**
 * Created by Zikey on 19/06/2017.
 */

public class FontChanger extends FontApplier {

    private static final String mainFontAddress = "fonts/IRANSansMobile.ttf";
    private static final String titleFontAddress = "fonts/IRANSansMobile_Bold.ttf";
    private static final String yekanFontAddress = "fonts/IRYekan.ttf";
    private static Typeface mainFont = null;
    private static Typeface titleFont = null;
    private static Typeface yekanFont = null;

    public FontChanger() {
    }

    public static Typeface getMainFont(Context context) {
        if(mainFont == null) {
            mainFont = Typeface.createFromAsset(context.getAssets(), mainFontAddress);
        }

        return mainFont;
    }

    public static Typeface getTitleFont(Context context) {
        if(titleFont == null) {
            titleFont = Typeface.createFromAsset(context.getAssets(), titleFontAddress);
        }

        return titleFont;
    }

    public static Typeface getYekanFont(Context context) {
        if(yekanFont == null) {
            yekanFont = Typeface.createFromAsset(context.getAssets(), yekanFontAddress);
        }

        return yekanFont;
    }

    public static void applyMainFont(View view) {
        if(view != null && view.getContext() != null) {
            if(view instanceof TextView) {
                ((TextView)view).setTypeface(getMainFont(view.getContext()));
            } else {
                if(view instanceof ViewGroup) {
                    for(int i = 0; i < ((ViewGroup)view).getChildCount(); ++i) {
                        View child = ((ViewGroup)view).getChildAt(i);
                        applyMainFont(child);
                    }
                }

            }
        }
    }

    public static void applyTitleFont(View view) {
        if(view != null && view.getContext() != null) {
            if(view instanceof TextView) {
                ((TextView)view).setTypeface(getTitleFont(view.getContext()));
            } else {
                if(view instanceof ViewGroup) {
                    for(int i = 0; i < ((ViewGroup)view).getChildCount(); ++i) {
                        View child = ((ViewGroup)view).getChildAt(i);
                        applyTitleFont(child);
                    }
                }

            }
        }
    }

    public static void applyYekanFont(View view) {
        if(view != null && view.getContext() != null) {
            if(view instanceof TextView) {
                ((TextView)view).setTypeface(getYekanFont(view.getContext()));
            } else {
                if(view instanceof ViewGroup) {
                    for(int i = 0; i < ((ViewGroup)view).getChildCount(); ++i) {
                        View child = ((ViewGroup)view).getChildAt(i);
                        applyYekanFont(child);
                    }
                }

            }
        }
    }
}
