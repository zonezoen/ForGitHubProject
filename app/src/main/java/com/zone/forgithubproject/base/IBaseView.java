package com.zone.forgithubproject.base;

/**
 * Created by john on 2016/7/26.
 */
public interface IBaseView {
    void showToast(String message);
    void showAlertDialog(String title, String message);
    void hideProgressDialog();
    void showProgressDialog(String message);
}
