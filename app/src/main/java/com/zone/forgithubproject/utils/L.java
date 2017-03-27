package com.zone.forgithubproject.utils;

import android.util.Log;

/**
 * Created by john on 2016/7/25.
 */
public class L {
    private L()
    {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化
    private static final String TAG = "zoneLog";

    // 下面四个是默认tag的函数
    public static void i(String msg)
    {
        if (!isDebug) return;
        StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
        Log.i(TAG, "(" + targetStackTraceElement.getFileName() + ":"
                + targetStackTraceElement.getLineNumber() + ")");
        Log.i(TAG, msg);
    }

    public static void d(String msg)
    {
        if (!isDebug) return;
        StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
        Log.d(TAG, "(" + targetStackTraceElement.getFileName() + ":"
                + targetStackTraceElement.getLineNumber() + ")");
        Log.d(TAG, msg);
    }

    public static void e(String msg)
    {
        if (!isDebug) return;
        StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
        Log.e(TAG, "(" + targetStackTraceElement.getFileName() + ":"
                + targetStackTraceElement.getLineNumber() + ")");
        Log.e(TAG, msg);
    }

    public static void v(String msg)
    {
        if (!isDebug) return;
        StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
        Log.v(TAG, "(" + targetStackTraceElement.getFileName() + ":"
                + targetStackTraceElement.getLineNumber() + ")");
        Log.v(TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg)
    {
        if (!isDebug) return;
        StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
        Log.i(tag, "(" + targetStackTraceElement.getFileName() + ":"
                + targetStackTraceElement.getLineNumber() + ")");
        Log.i(tag, msg);
    }

    public static void d(String tag, String msg)
    {
        if (!isDebug) return;
        StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
        Log.d(tag, "(" + targetStackTraceElement.getFileName() + ":"
                + targetStackTraceElement.getLineNumber() + ")");
        Log.d(tag, msg);
    }

    public static void e(String tag, String msg)
    {
        if (!isDebug) return;
        StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
        Log.e(tag, "(" + targetStackTraceElement.getFileName() + ":"
                + targetStackTraceElement.getLineNumber() + ")");
        Log.e(tag, msg);
    }

    public static void v(String tag, String msg)
    {
        if (!isDebug) return;
        StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
        Log.v(tag, "(" + targetStackTraceElement.getFileName() + ":"
                + targetStackTraceElement.getLineNumber() + ")");
        Log.v(tag, msg);
    }

    private static StackTraceElement getTargetStackTraceElement() {
        // find the target invoked method
        StackTraceElement targetStackTrace = null;
        boolean shouldTrace = false;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            boolean isLogMethod = stackTraceElement.getClassName().equals(L.class.getName());
            if (shouldTrace && !isLogMethod) {
                targetStackTrace = stackTraceElement;
                break;
            }
            shouldTrace = isLogMethod;
        }
        return targetStackTrace;
    }
}
