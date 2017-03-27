package com.zone.forgithubproject.myApplication;

import android.app.Application;

/**
 * Created by john on 2016/5/8.
 */
public class MyApplication extends Application {
    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        MyApplication.token = token;
    }

    private static String token = "";




    @Override
    public void onCreate() {
        super.onCreate();

    }

}
