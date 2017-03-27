package com.zone.forgithubproject.model.bean;

/**
 * Created by john on 2016/12/30.
 */
public class LoginCheckBean {
    private boolean isPlayAd = false;//默认赋值 false ，当没有网络时，也不会 NullPoint

    public LoginCheckBean(boolean isPlayAd) {
        this.isPlayAd = isPlayAd;
    }
    public LoginCheckBean() {
    }


    public boolean isPlayAd() {
        return isPlayAd;
    }

    public void setPlayAd(boolean playAd) {
        isPlayAd = playAd;
    }
}