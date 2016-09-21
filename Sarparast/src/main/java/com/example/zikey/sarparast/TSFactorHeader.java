package com.example.zikey.sarparast;

/**
 * Created by Zikey on 29/06/2016.
 */
public class TSFactorHeader {
    private String sh_factro;
    private String date;
    private String name_moshtari;
    private String ts_price;
    private int imgFactorDetail;
    private int imgAghlam;


    public String getSh_factro() {
        return sh_factro;
    }

    public String getTs_price() {
        return ts_price;
    }

    public String getName_moshtari() {
        return name_moshtari;
    }

    public String getDate() {
        return date;
    }

    public int getImgFactorDetail() {
        return imgFactorDetail;
    }

    public int getImgAghlam() {
        return imgAghlam;
    }

    public void setSh_factro(String sh_factro) {
        this.sh_factro = sh_factro;
    }

    public void setTs_price(String ts_price) {
        this.ts_price = ts_price;
    }

    public void setName_moshtari(String name_moshtari) {
        this.name_moshtari = name_moshtari;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImgFactorDetail(int imgFactorDetail) {
        this.imgFactorDetail = imgFactorDetail;
    }

    public void setImgAghlam(int imgAghlam) {
        this.imgAghlam = imgAghlam;
    }
}
