package com.example.bv_mart.bean;

import java.io.Serializable;



public class StoreBean implements Serializable {

    private String ID;
    private String iv_store_pic;
    private String storeName;
    private String storeScore;
    private String storeSell;

    public StoreBean(String ID, String iv_store_pic, String storeName, String storeScore, String storeSell, String storeSign) {
        this.ID = ID;
        this.iv_store_pic = iv_store_pic;
        this.storeName = storeName;
        this.storeScore = storeScore;
        this.storeSell = storeSell;
    }

    public StoreBean() {

    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setIv_store_pic(String iv_store_pic) {
        this.iv_store_pic = iv_store_pic;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreScore() {
        return storeScore;
    }

    public void setStoreScore(String storeScore) {
        this.storeScore = storeScore;
    }

    public String getStoreSell() {
        return storeSell;
    }

    public void setStoreSell(String storeSell) {
        this.storeSell = storeSell;
    }


    @Override
    public String toString() {
        return "StoreBean{" +
                "ID='" + ID + '\'' +
                ", iv_store_pic='" + iv_store_pic + '\'' +
                ", storeName='" + storeName + '\'' +
                ", storeScore='" + storeScore + '\'' +
                ", storeSell='" + storeSell + '\'' + '}';
    }
}
