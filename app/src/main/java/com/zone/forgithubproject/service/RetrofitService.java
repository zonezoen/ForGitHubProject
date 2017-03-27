package com.zone.forgithubproject.service;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by john on 2016/9/7.
 */
public interface RetrofitService {
    @GET
    Observable<ResponseBody> downLoadFile(@Url String fileUrl);
}
