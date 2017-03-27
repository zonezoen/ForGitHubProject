package com.zone.forgithubproject.utils.Rx;

import com.zone.forgithubproject.base.BaseBean;
import com.zone.forgithubproject.utils.L;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by john on 2016/9/8.
 */
public class RxHelper {

    public static <T> Observable.Transformer<BaseBean<T>, T> handleResult() {
        return new Observable.Transformer<BaseBean<T>, T>() {
            @Override
            public Observable<T> call(Observable<BaseBean<T>> baseBeanObservable) {
                return baseBeanObservable.flatMap(new Func1<BaseBean<T>, Observable<T>>() {
                    @Override
                    public Observable<T> call(BaseBean<T> result) {
                        if (result.success()) {//判断返回的code是否为200，如果是则取到结果往下传
                            L.d("====>code==200");
                            L.d("====>message=="+result.message);
                            return createData(result.data);
//                            return Observable.concat().first();
                        } else {
                            L.d("====code==>"+result.code);
//                            return Observable.error(new Exception(result.message));
                            return createError(result.message);
                        }
                    }
                }).subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }




    private static <T> Observable<T> createData(final T data) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }
    private static Observable createError(final String data) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    subscriber.onError(new Exception(data));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }
    private static <T> Observable<T> createData2(final T data) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }
}



