package com.zone.forgithubproject.base;

import com.zone.forgithubproject.utils.L;


/**
 * Created by zone on 2016/9/13.
 */
public class PBase<V extends IBaseView> implements IPBase<V> {
    public V view;

    @Override
    public void attachView(V view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public boolean isAttachView() {
        return view != null;
    }

    public V getMyView() {
        return view;
    }

    public void checkAttachView() {
        if (!isAttachView()) {
            L.d("====>View没有连接！！！");
        }
    }




}
