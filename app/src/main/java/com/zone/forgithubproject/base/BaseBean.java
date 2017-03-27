package com.zone.forgithubproject.base;

/**
 * Created by john on 2016/9/8.
 */
public class BaseBean<T> {
    public String code;
    public String message;
    public T data;

    public boolean success() {
        return code.equals("200");
    }

    public boolean error404() {
        return code.equals("404");
    }

    @Override
    public String toString() {
        return "code=>"+code+"\nmessage=>"+message+"\ndata=>"+data;
    }
}
