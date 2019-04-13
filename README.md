# Android 启动页与广告页 [CSDN 地址](https://blog.csdn.net/zone_/article/details/66476733)
## **如果我的文章对你有帮助，欢迎 start、follow，这样我会更有动力做原创分享。**

   在我们APP的开发过程中，启动页面是绕不开的，广告页面说不定，但是不得不说，这两个界面都是经常要用到的。接下来我记录一下我的实现过程。项目架构为MVP。
　　那么先看看我们的需求和流程：（当然这也是可以根据实际需求改动的）
　　


 - 展示 logo 页面3秒
 - 服务端可以控制是否播放广告
 - 服务端可以控制播放广告的秒数
 - 服务端可以控制广告的内容（图片）和广告详情页面的链接

![这里写图片描述](http://img.blog.csdn.net/20170327222116907?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvem9uZV8=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
　　**这里需要注意的一点是，从服务端请求数据是在展示 3 秒启动页的时候获取的。**

　　

启动页
---
　　如果我们稍微有留意的话，都会发现，我们自己的应用启动的时候都有一段白屏的状态。但是微信却没有，我们现在要做的是解决这个问题。
　　首先我们来了解一下，冷启动，热启动。
　　

 - 冷启动：是指进程从无到有的过程。因为要进行页面初始化，所以相对其他两个启动方式，消耗的时间是相对比较多的。
 - 热启动：是指之前的进程还在，在之前进程的基础上创建 Activity 的过程。这里耗时相对少一点。
　　我们可以通过 Activity 的 theme 来修改这个白屏所显示的界面。根据上面的需求，我们需要显示3秒 logo 的页面。那么，我们干脆将我们的logo设置为背景图就行。
　　

```
    <style name="AppTheme.NoActionBarWithBackGround">
        <item name="windowActionBar">false</item>//取消Actionbar
        <item name="windowNoTitle">true</item>
        <item name="android:windowFullscreen">true</item>//设置全屏
        <item name="android:windowBackground">@drawable/splash_jyz</item>//设置背景
    </style>
```
然后让我们的 Activity 使用这个 theme 即可。

```
        <activity
            android:name=".View.iml.AdActivity"
            android:theme="@style/AppTheme.NoActionBarWithBackGround">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
```

那么我们来看看效果图：
![这里写图片描述](http://img.blog.csdn.net/20170327175316045?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvem9uZV8=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

## 广告页 ##
　　广告页我尝试过两种方式：
			 1. glide 加载
			 2. 通过下载文件，然后再加载
		如果使用glide加载广告图片，如果网络比较差，会存在广告页面空白的情况，因为使用 glide 无法判断在 3 秒展示 logo 的页面是否加载好了广告图片。这给用户的体验是比较差的，也是不太友好的，因为用户在空白界面拜拜等待了 3 秒。所以后面使用了将广告图片下载到本地的方法。
　　这里进行数秒，这里是通过增加数字值来控制的，解决了服务端要控制广告时间的问题。之前使用过减少数字值来实现，但是要解决服务端控制广告时间，就没那么容易了。
## View ##
```
    private int countNum() {//数秒
        timeCount++;
        if (timeCount == 3) {//数秒，超过3秒后如果没有网络，则进入下一个页面
            if (!NetUtils.isConnected(AdActivity.this)) {
                continueCount = false;
                toNextActivity();
                finish();
            }
            if (!loginCheckBean.isPlayAd()) {//如果服务端不允许播放广告，则直接进入下一个页面
                continueCount = false;
                toNextActivity();
                finish();
            } else {//如果播放，则进行显示
                ivAdvertising.setVisibility(View.VISIBLE);
                layoutSkip.setVisibility(View.VISIBLE);
            }
        }
        if (timeCount == initTimeCount) {//超过广告显示的最长时间，进入下一个界面
            continueCount = false;
            toNextActivity();
            finish();
        }
        return timeCount;
    }
```

```
    public void toNextActivity() {//根据是否保存有 token 判断去登录界面还是主界面
        L.d("到下一个界面");
        Intent intent = null;
        String token = (String) SPUtils.get(AdActivity.this, "token", "");
        if (TextUtils.isEmpty(token)) {
            intent = new Intent(AdActivity.this, LoginActivity.class);
        } else {
            intent = new Intent(AdActivity.this, MainActivity.class);
            MyApplication.setToken(token);
        }
        startActivity(intent);
        finish();
    }
```
## Presenter##
向服务端请求是否要播放广告

```
    public void getLoginCheck() {//向服务器请求是否要播放广告
        mAdModel.getLoginCheck()
                .subscribeOn(Schedulers.io())                            //发布者在后台线程中运行
                .observeOn(AndroidSchedulers.mainThread())               //订阅者在Android主线程中运行
                .subscribe(new RxSubscribe<LoginCheckBean>() {
                    @Override
                    protected void _onNext(LoginCheckBean loginCheckBean) {
                        getMyView().setLoginCheckBean(loginCheckBean);
                        if (loginCheckBean.isPlayAd()) {//这里需要添加一个是否已经下载的判断，如果已经下载，则不再进行下载
                            getAdMessage();
                        }
                    }

                    @Override
                    protected void _onError(String message) {

                    }

                    @Override
                    public void onCompleted() {

                    }
                });
    }

```

获取图片地址，详情页面链接，广告的播放时间

```
    public void getAdMessage() {
        mAdModel.getAdMessage()
                .subscribeOn(Schedulers.io())                            
                .observeOn(AndroidSchedulers.mainThread())               
                .subscribe(new RxSubscribe<AdMessageBean>() {
                    @Override
                    protected void _onNext(AdMessageBean adMessageBean) {
                        getMyView().setAdTime(adMessageBean.getAdTime());
                        getAdPicture(adMessageBean.getAdPictureUrl(), "123.jpg");
                    }

                    @Override
                    protected void _onError(String message) {

                    }

                    @Override
                    public void onCompleted() {

                    }
                });
    }

```

获取广告图片

```
    private void getLocalPicture(String localUrl) {
        Bitmap bitmap = BitmapFactory.decodeFile(localUrl);
        getMyView().setAdImg(bitmap);
    }

    public void getAdPicture(final String fileUrl, final String fileName) {//获取要展示的广告图片
        if (SPUtils.get((Context) getMyView(), "adPictureUrl", "").equals(fileUrl)) {//判断是否存在缓存
            L.d("从本地获取图片");
            getLocalPicture((String) SPUtils.get((Context) getMyView(),"adPictureAddress",""));
        } else {
            L.d("从网络中获取图片");
            mAdModel.downLoadFile(fileUrl)
                    .subscribeOn(Schedulers.newThread())                            
                    .observeOn(AndroidSchedulers.mainThread())               
                    .map(new Func1<ResponseBody, Bitmap>() {
                        @Override
                        public Bitmap call(ResponseBody responseBody) {
                            if (responseBody != null) {
                                L.d("收到的responseBody不为空！");
                            }
                            if (writeResponseBodyToDisk(responseBody, fileName, fileUrl)) {
                                Bitmap bitmap = BitmapFactory.decodeFile(((Context) getMyView()).getExternalFilesDir(null) + File.separator + fileName);
                                return bitmap;
                            }
                            return null;
                        }
                    }).subscribe(new RxSubscribe<Bitmap>((Context) getMyView()) {
                @Override
                protected void _onNext(Bitmap bitmap) {
                    getMyView().setAdImg(bitmap);
                }

                @Override
                protected void _onError(String message) {

                }

                @Override
                public void onCompleted() {

                }
            });
        }

    }


    private boolean writeResponseBodyToDisk(ResponseBody body, String fileName, String fileUrl) {//保存图片到本地
        try {
            // todo change the file location/name according to your needs

            File futureStudioIconFile = new File(((Context) getMyView()).getExternalFilesDir(null) + File.separator + fileName);
            L.d("文件的保存地址为：" + ((Context) getMyView()).getExternalFilesDir(null) + File.separator + fileName);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;

                    L.d("file download: " + fileSizeDownloaded / fileSize * 100);
                    L.d("file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();

                SPUtils.put((Context) getMyView(), "adPictureAddress", ((Context) getMyView()).getExternalFilesDir(null) + File.separator + fileName);//下载好广告图片后，保存好当前广告图片的地址，为判断是否已经下载好图片做准备
                SPUtils.put((Context) getMyView(), "adPictureUrl", fileUrl);
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

```
## Model ##

```
    public Observable<LoginCheckBean> getLoginCheck() {//假装服务器要展示广告
        return Observable.create(new Observable.OnSubscribe<LoginCheckBean>() {
            @Override
            public void call(Subscriber<? super LoginCheckBean> subscriber) {
                subscriber.onNext(new LoginCheckBean(true));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<AdMessageBean> getAdMessage() {
        return Observable.create(new Observable.OnSubscribe<AdMessageBean>() {
            @Override
            public void call(Subscriber<? super AdMessageBean> subscriber) {//假装要展示 3 秒广告，且广告图为如下地址
                subscriber.onNext(new AdMessageBean(3,"http://odjfpxwey.bkt.clouddn.com/2017-3-3-20-141110180-Screenshot_2017-02-23-23-10-26-062_com.tmall.wireless.png","http://www.baidu.com"));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<ResponseBody> downLoadFile(String fileUrl) {
        return retrofitService.downLoadFile(fileUrl);
    }
```

　　哈哈，启动页和广告页的内容暂时是这些，github 地址如下：https://github.com/zonezoen/ForGitHubProject

## 关注微信公众号，获取最新技术文章
![zone_qrcode.jpg](http://upload-images.jianshu.io/upload_images/2470773-2500f5fb1c11f43f?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
