package com.zone.forgithubproject.base;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;


import com.zhy.autolayout.AutoLayoutActivity;
import com.zone.forgithubproject.utils.T;


/**
 * Created by john on 2016/7/26.
 */
public abstract class BaseActivity extends AutoLayoutActivity {

    private AlertDialog dialog;
    private ProgressDialog mProgressDialog = null;

    public interface Convert {
        void transaction(View dialogView, AlertDialog alertDialog);
    }

    public interface DialogOnClickListener {
        void positiveListener(DialogInterface dialog, int which);

        void negativeListener(DialogInterface dialog, int which);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void iniBase() {
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//创建返回键，并实现打开关/闭监听
        getSupportActionBar().setDisplayShowTitleEnabled(false);//隐藏系统默认的Title
    }

    protected void backhome(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
    }

    public void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }


    public void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.hide();
        }
    }


    public void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    public void showToast(String message) {
        T.showShort(this, message);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
