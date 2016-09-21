package com.example.zikey.sarparast;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Zikey on 25/05/2016.
 */
public class MyPagerAdapter extends PagerAdapter {
    public  ArrayList<Integer> imageIDS;

    public void setInflater(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    private LayoutInflater inflater;
    // یک کانستراکت.ر برای گرفتن نام پیج
    public MyPagerAdapter(ArrayList<Integer> imageIDS) {
        this.imageIDS=imageIDS;
    }

    @Override
    public int getCount() {
        return imageIDS.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = inflater.inflate(R.layout.pager_gallery,null);

     ImageView image= (ImageView) view.findViewById(R.id.imgView);
        image.setImageResource(imageIDS.get(position));

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        Log.e("remove_Log","view #"+position+"deleted");
    }
}
