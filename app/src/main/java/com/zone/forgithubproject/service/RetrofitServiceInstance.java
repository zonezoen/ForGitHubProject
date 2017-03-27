package com.zone.forgithubproject.service;


import android.content.Context;
import android.text.TextUtils;
import com.zone.forgithubproject.myApplication.MyApplication;
import com.zone.forgithubproject.utils.L;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by john on 2016/9/10.
 */
public class RetrofitServiceInstance {
    private static RetrofitService instance = null;
    public static RetrofitService getInstance() {

        Authenticator authenticator = new Authenticator() {//当服务器返回的状态码为401时，会自动执行里面的代码，也就实现了自动刷新token
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                L.d("==========>   重新刷新了token");
                return response.request().newBuilder()
                        .addHeader("token","")
                        .build();
            }
        };
        Interceptor tokenInterceptor = new Interceptor() {//全局拦截器，往请求头部添加token字段，就实现了全局token
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request tokenRequest = null;
                MyApplication application = new MyApplication();
                    if (TextUtils.isEmpty(MyApplication.getToken())) {
                    L.d("MyApplication 的token值为空！！！！！！！！！！！！！");
                    return chain.proceed(originalRequest);
                }
                    tokenRequest = originalRequest.newBuilder()
                        .header("token",MyApplication.getToken())
                        .build();

                return chain.proceed(tokenRequest);
            }
        };
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {//log拦截器，打印所有的log
            @Override
            public void log(String message) {
                L.d(message);
            }
        });
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addNetworkInterceptor(tokenInterceptor)
                .addInterceptor(loggingInterceptor)
                .authenticator(authenticator)
                .build();
        if (instance == null) {
            synchronized (RetrofitService.class) {
                if (instance == null) {
                    Retrofit retrofit = new Retrofit.Builder()                              //生成实例
                            .baseUrl("http://115.159.212.89:3000/")                        //基础url，会拼接NetService中的参数
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())            //使用Gson解析
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())      //加入RxJava的适配器
                            .build();

                    instance = retrofit.create(RetrofitService.class);
                }
            }
        }
        return instance;
    }
}
