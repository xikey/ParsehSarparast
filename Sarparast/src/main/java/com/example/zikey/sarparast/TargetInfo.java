package com.example.zikey.sarparast;

/**
 * Created by Zikey on 22/10/2016.
 */

public class TargetInfo implements Comparable<TargetInfo> {

    private String priceTargetSeprated;
    private  String  priceTarget;


    public String getPriceTargetSeprated() {
        return priceTargetSeprated;
    }

    public void setPriceTargetSeprated(String priceTargetSeprated) {
        this.priceTargetSeprated = priceTargetSeprated;
    }

    public String getPriceTarget() {
        return priceTarget;
    }

    public void setPriceTarget(String priceTarget) {
        this.priceTarget = priceTarget;
    }

    @Override
    public int compareTo(TargetInfo another) {

        if (getPriceTarget().equals(another.getPriceTarget())) {
            return 0;
        }
        return 1;
    }
}
