package com.zone.forgithubproject.model.bean;

/**
 * Created by john on 2016/12/30.
 */
public class AdMessageBean {
    int adTime;//播放广告的时常
    String adPictureUrl;//广告图片的地址
    String adUrl;//点击广告图片时，展示详情 webview 的地址

    public AdMessageBean(int adTime, String adPictureUrl, String adUrl) {
        this.adTime = adTime;
        this.adPictureUrl = adPictureUrl;
        this.adUrl = adUrl;
    }

    public int getAdTime() {
        return adTime;
    }

    public void setAdTime(int adTime) {
        this.adTime = adTime;
    }

    public String getAdPictureUrl() {
        return adPictureUrl;
    }

    public void setAdPictureUrl(String adPictureUrl) {
        this.adPictureUrl = adPictureUrl;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }
}
